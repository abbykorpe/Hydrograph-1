/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.cascading.integration;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.cascade.Cascade;
import cascading.cascade.CascadeConnector;
import cascading.flow.FlowConnector;
import cascading.flow.FlowDef;
import cascading.flow.process.ProcessFlow;
import cascading.flow.tez.Hadoop2TezFlowConnector;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.hadoop.BigDecimalSerialization;
import cascading.tuple.hadoop.TupleSerializationProps;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.AssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.generator.base.CommandComponentGeneratorBase;
import hydrograph.engine.cascading.assembly.generator.base.GeneratorBase;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.generator.base.StraightPullAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.core.entity.LinkInfo;
import hydrograph.engine.core.helper.JAXBTraversal;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseOutSocket;
import hydrograph.engine.jaxb.inputtypes.SequenceInputFile;
import hydrograph.engine.jaxb.outputtypes.SequenceOutputFile;
import hydrograph.engine.utilities.ComponentParameterBuilder;
import hydrograph.engine.utilities.PlatformHelper;

@SuppressWarnings("rawtypes")
public class FlowBuilder {

	private static final Logger LOG = LoggerFactory
			.getLogger(FlowBuilder.class);

	public FlowBuilder() {
	}

	public void buildFlow(RuntimeContext runtimeContext) {
		LOG.info("Building flow");
		JAXBTraversal traversal = runtimeContext.getTraversal();
		Properties hadoopProps = runtimeContext.getHadoopProperties();
		HydrographJob hydrographJob = runtimeContext.getHydrographJob();
		addSerializations(hadoopProps);

		FlowConnector flowConnector = PlatformHelper
				.getHydrographEngineFlowConnector(hadoopProps);

		Cascade[] cascades = new Cascade[traversal.getFlowsNumber().size()];
		int phaseIndex = -1;
		String flowName = "flow";
		for (String phase : traversal.getFlowsNumber()) {
			phaseIndex = phaseIndex + 1;
			FlowContext flowContext = new FlowContext(hydrographJob, traversal,
					hadoopProps);
			traverseAndConnect(runtimeContext, flowContext, phase);
			FlowDef flowDef = flowContext.getFlowDef();
			if (flowConnector instanceof Hadoop2TezFlowConnector) {
				flowDef = PlatformHelper
						.addJarsToClassPathInCaseOfTezExecution(flowDef);
			}
			if (flowDef.getSources().size() > 0) {
				flowContext.getCascadeDef().addFlow(
						flowConnector.connect(flowDef));
			}
			cascades[phaseIndex] = (new CascadeConnector().connect(flowContext
					.getCascadeDef().setName(flowName + "_" + phaseIndex)));
			runtimeContext.setFlowContext(phase, flowContext);
		}
		runtimeContext.setCascade(cascades);

	}

	private void addSerializations(Properties hadoopProps) {
		TupleSerializationProps.addSerialization(hadoopProps,
				BigDecimalSerialization.class.getName());
	}

	private void traverseAndConnect(RuntimeContext runtimeContext,
			FlowContext flowContext, String phase) {

		JAXBTraversal traversal = flowContext.getTraversal();

		for (String componentId : traversal.getOrderedComponentsList(phase)) {
			LOG.info("Building parameters for " + componentId);

			ComponentParameters cp = null;
			GeneratorBase assemblyGeneratorBase = runtimeContext
					.getAssemblyGeneratorMap().get(componentId);
			if (assemblyGeneratorBase instanceof InputAssemblyGeneratorBase) {
				cp = new ComponentParameterBuilder.Builder(componentId,
						new ComponentParameters(), flowContext, runtimeContext)
						.setFlowdef().setJobConf().setSchemaFields().build();
			} else if (assemblyGeneratorBase instanceof OutputAssemblyGeneratorBase) {
				cp = new ComponentParameterBuilder.Builder(componentId,
						new ComponentParameters(), flowContext, runtimeContext)
						.setFlowdef().setInputPipes().setInputFields().setSchemaFields().build();
			} else if (assemblyGeneratorBase instanceof StraightPullAssemblyGeneratorBase) {
				cp = new ComponentParameterBuilder.Builder(componentId,
						new ComponentParameters(), flowContext, runtimeContext)
						.setFlowdef().setJobConf().setInputPipes().setSchemaFields()
						.setInputFields().build();
			} else if (assemblyGeneratorBase instanceof OperationAssemblyGeneratorBase) {
				cp = new ComponentParameterBuilder.Builder(componentId,
						new ComponentParameters(), flowContext, runtimeContext)
						.setFlowdef().setJobConf().setInputPipes().setUDFPath()
						.setInputFields().setSchemaFields().build();
			} else if (assemblyGeneratorBase instanceof CommandComponentGeneratorBase) {
				CommandComponentGeneratorBase command = ((CommandComponentGeneratorBase) assemblyGeneratorBase)
						.getCommandComponent();
				flowContext.getCascadeDef().addFlow(
						new ProcessFlow(componentId, command));
				continue;

			}

			if (isPhaseTempComponent(componentId, runtimeContext)) {
//				buildForPhaseBreak(componentId, runtimeContext, cp);
			}

			((AssemblyGeneratorBase) assemblyGeneratorBase).createAssembly(cp);
			BaseComponent component = ((AssemblyGeneratorBase) assemblyGeneratorBase)
					.getAssembly();

			flowContext.getAssemblies().put(componentId, component);

			LOG.info("Assembly creation completed for " + componentId);
		}
	}

	private String getTempPath(String prefix, JobConf jobConf) {
		String name = prefix + "_" + UUID.randomUUID().toString();
		name = name.replaceAll("\\s+|\\*|\\+|/+", "_");

		return (new Path(Hfs.getTempPath(jobConf), name)).toString();

	}

	private void buildForPhaseBreak(String componentId,
			RuntimeContext runtimeContext, ComponentParameters cp) {
		JAXBTraversal traversal = runtimeContext.getTraversal();

		if (runtimeContext.getTraversal().getComponentFromComponentId(
				componentId) instanceof SequenceOutputFile) {
			List<? extends TypeBaseInSocket> originalPhaseSourceSocket = traversal
					.getInputSocketFromComponentId(componentId);

			LinkInfo originalPhaseLink = traversal
					.getPhaseLinkFromInputSocket(originalPhaseSourceSocket
							.iterator().next());

			cp.setPathUri(getTempPath(componentId, runtimeContext.getJobConf()));

			runtimeContext.addTempPathParameter(originalPhaseLink.toString(),
					cp);
		} else {
			List<? extends TypeBaseOutSocket> originalPhaseTargetSocket = traversal
					.getOutputSocketFromComponentId(componentId);
			LinkInfo originalPhaseLink = traversal
					.getPhaseLinkFromOutputSocket(originalPhaseTargetSocket
							.iterator().next());

			ComponentParameters inputCp = runtimeContext
					.getTempPathParameter(originalPhaseLink.toString());
			cp.setOutputFieldsList(inputCp.getInputFieldsList());
			cp.setPathUri(inputCp.getPathUri());
		}

	}

	private boolean isPhaseTempComponent(String componentId,
			RuntimeContext runtimeContext) {

		return runtimeContext.getTraversal().getComponentFromComponentId(
				componentId) instanceof SequenceOutputFile
				|| runtimeContext.getTraversal().getComponentFromComponentId(
						componentId) instanceof SequenceInputFile;
	}

	public void cleanup(List<String> list,RuntimeContext runtimeContext) {
		deleteTempPaths(list,runtimeContext);

	}
//
//	private void deleteTempPaths(RuntimeContext runtimeContext) {
//		for (ComponentParameters cp : runtimeContext.getAllTempPathParameters()) {
//
//			Path fullPath = new Path(cp.getPathUri());
//			// do not delete the root directory
//			if (fullPath.depth() == 0)
//				continue;
//			FileSystem fileSystem;
//
//			LOG.info("Deleting temp path:" + cp.getPathUri());
//			try {
//				fileSystem = FileSystem.get(runtimeContext.getJobConf());
//
//				fileSystem.delete(fullPath, true);
//			} catch (NullPointerException exception) {
//				// hack to get around npe thrown when fs reaches root directory
//				// if (!(fileSystem instanceof NativeS3FileSystem))
//				throw new RuntimeException(exception);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//
//		}
//	}

	private void deleteTempPaths(List<String> tmpPathList,RuntimeContext runtimeContext) {
		for (String tmpPath : tmpPathList) {

			Path fullPath = new Path(tmpPath);
			// do not delete the root directory
			if (fullPath.depth() == 0)
				continue;
			FileSystem fileSystem;

			LOG.info("Deleting temp path:" + tmpPath);
			try {
				fileSystem = FileSystem.get(runtimeContext.getJobConf());

				fileSystem.delete(fullPath, true);
			} catch (NullPointerException exception) {
				// hack to get around npe thrown when fs reaches root directory
				// if (!(fileSystem instanceof NativeS3FileSystem))
				throw new RuntimeException(exception);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
	}

}
