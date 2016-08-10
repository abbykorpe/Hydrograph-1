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
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.thrift.DelegationTokenIdentifier;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.cascade.Cascade;
import cascading.flow.Flow;
import cascading.property.AppProps;
import hydrograph.engine.cascading.assembly.generator.AssemblyGeneratorFactory;
import hydrograph.engine.core.core.HydrographDebugInfo;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.core.core.HydrographRuntimeService;
import hydrograph.engine.core.helper.JAXBTraversal;
import hydrograph.engine.core.props.PropertiesLoader;
import hydrograph.engine.execution.tracking.ComponentPipeMapping;
import hydrograph.engine.execution.tracking.listener.ExecutionTrackingListener;
import hydrograph.engine.flow.utils.FlowManipulationContext;
import hydrograph.engine.flow.utils.FlowManipulationHandler;
import hydrograph.engine.hadoop.utils.HadoopConfigProvider;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.schemapropagation.SchemaFieldHandler;
import hydrograph.engine.utilities.GeneralUtilities;
import hydrograph.engine.utilities.HiveConfigurationMapping;

@SuppressWarnings({ "rawtypes" })
public class HydrographRuntime implements HydrographRuntimeService {

	private Properties hadoopProperties = new Properties();
	private FlowBuilder flowBuilder;
	private RuntimeContext runtimeContext;
	private String[] args;
	private PropertiesLoader config;
	private FlowManipulationContext flowManipulationContext;
	private static Logger LOG = LoggerFactory.getLogger(HydrographRuntime.class);

	public void executeProcess(String[] args, HydrographJob hydrographJob)  {
		this.args = args;
		config = PropertiesLoader.getInstance();
		LOG.info("Invoking initialize on runtime service");
		initialize(config.getRuntimeServiceProperties(), this.args, hydrographJob,
				new HydrographDebugInfo(null), null, null);
		LOG.info("Preparation started");
		prepareToExecute();
		LOG.info("Preparation completed. Now starting execution");
		LOG.info("Execution Started");
		execute();
		LOG.info("Execution Complete");
		oncomplete();
	}

	public void initialize(Properties config, String[] args, HydrographJob hydrographJob,
			HydrographDebugInfo hydrographDebugInfo, String jobId, String basePath) {

		AppProps.setApplicationName(hadoopProperties, hydrographJob.getJAXBObject()
				.getName());

		hadoopProperties.putAll(config);
		SchemaFieldHandler schemaFieldHandler = new SchemaFieldHandler(hydrographJob.getJAXBObject()
				.getInputsOrOutputsOrStraightPulls());
		
		flowManipulationContext = new FlowManipulationContext(hydrographJob,
				hydrographDebugInfo,schemaFieldHandler, jobId, basePath);

		hydrographJob = FlowManipulationHandler.execute(flowManipulationContext);

		if (hydrographJob.getJAXBObject().getRuntimeProperties() != null
				&& hydrographJob.getJAXBObject().getRuntimeProperties()
						.getProperty() != null) {
			for (Property property : hydrographJob.getJAXBObject()
					.getRuntimeProperties().getProperty()) {
				hadoopProperties.put(property.getName(), property.getValue());
			}
		}

		Configuration conf = new HadoopConfigProvider(hadoopProperties)
				.getJobConf();

		JAXBTraversal traversal = new JAXBTraversal(hydrographJob.getJAXBObject());

		if (traversal.isHiveComponentPresentInFlow()) {
			try {
				obtainTokenForHiveMetastore(conf);
			} catch (TException | IOException e) {
				throw new HydrographRuntimeException(e);
			}
		}

		String[] otherArgs;
		try {
			otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		} catch (IOException e) {
			throw new HydrographRuntimeException(e);
		}

		String argsString = "";
		for (String arg : otherArgs) {
			argsString = argsString + " " + arg;
		}
		LOG.info("After processing arguments are:" + argsString);
		this.args = otherArgs;
		// setJar(otherArgs);

		hadoopProperties.putAll(conf.getValByRegex(".*"));

		AssemblyGeneratorFactory assemblyGeneratorFactory = new AssemblyGeneratorFactory(
				hydrographJob.getJAXBObject());

		flowBuilder = new FlowBuilder();

		runtimeContext = new RuntimeContext(hydrographJob, traversal,
				hadoopProperties, assemblyGeneratorFactory,schemaFieldHandler);

		LOG.info("Graph '"
				+ runtimeContext.getHydrographJob().getJAXBObject().getName()
				+ "' initialized successfully");
	}

	@Override
	public void prepareToExecute()  {
		flowBuilder.buildFlow(runtimeContext);

		if (GeneralUtilities.IsArgOptionPresent(args, CommandLineOptionsProcessor.OPTION_DOT_PATH)) {
			writeDotFiles();
		}

	}

	@Override
	public void execute()  {
		if (GeneralUtilities.IsArgOptionPresent(args,
				CommandLineOptionsProcessor.OPTION_NO_EXECUTION)) {
			LOG.info(CommandLineOptionsProcessor.OPTION_NO_EXECUTION
					+ " option is provided so skipping execution");
			return;
		}

		for (Cascade cascade : runtimeContext.getCascade()) {
			if (ExecutionTrackingListener.isTrackingPluginPresent()) {
				ComponentPipeMapping.generateComponentToPipeMap(runtimeContext.getFlowContext());
				ComponentPipeMapping.generateComponentToFilterMap(runtimeContext);
				ExecutionTrackingListener.addListener(cascade);
			}
			cascade.complete();
		}

	}

	@Override
	public void oncomplete() {
		flowBuilder.cleanup(runtimeContext);

	}

	public Cascade[] getFlow() {
		return runtimeContext.getCascadingFlows();
	}

	private void writeDotFiles()  {

		
		
		String basePath = CommandLineOptionsProcessor.getDotPath(args);

		if (basePath == null) {
			throw new HydrographRuntimeException(
					CommandLineOptionsProcessor.OPTION_DOT_PATH
							+ " option is provided but is not followed by path");
		}
		LOG.info("Dot files will be written under " + basePath);

		String flowDotPath = basePath + "/"
				+ runtimeContext.getHydrographJob().getJAXBObject().getName() + "/"
				+ "flow";
		String flowStepDotPath = basePath + "/"
				+ runtimeContext.getHydrographJob().getJAXBObject().getName() + "/"
				+ "flowstep";

		int phaseCounter = 0;
		for (Cascade cascadingFlow : runtimeContext.getCascadingFlows()) {
			for (Flow flows : cascadingFlow.getFlows()) {
				flows.writeDOT(flowDotPath + "_" + phaseCounter);
				flows.writeStepsDOT(flowStepDotPath + "_" + phaseCounter);
				phaseCounter++;
			}
		}
	}

	private static void obtainTokenForHiveMetastore(Configuration conf)
			throws TException, IOException {
		conf.addResource(new Path(HiveConfigurationMapping
				.getHiveConf("path_to_hive_site_xml")));
		HiveConf hiveConf = new HiveConf();
		hiveConf.addResource(conf);
		try {
			UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
			HiveMetaStoreClient hiveMetaStoreClient = new HiveMetaStoreClient(
					hiveConf);

			if (UserGroupInformation.isSecurityEnabled()) {
				String metastore_uri = conf.get("hive.metastore.uris");

				LOG.trace("Metastore URI:" + metastore_uri);

				// Check for local metastore
				if (metastore_uri != null && metastore_uri.length() > 0) {
					String principal = conf
							.get("hive.metastore.kerberos.principal");
					String username = ugi.getUserName();

					if (principal != null && username != null) {
						LOG.debug("username: " + username);
						LOG.debug("principal: " + principal);

						String tokenStr;
						try {
							// Get a delegation token from the Metastore.
							tokenStr = hiveMetaStoreClient.getDelegationToken(
									username, principal);
							// LOG.debug("Token String: " + tokenStr);
						} catch (TException e) {
							LOG.error(e.getMessage(), e);
							throw new RuntimeException(e);
						}

						// Create the token from the token string.
						Token<DelegationTokenIdentifier> hmsToken = new Token<>();
						hmsToken.decodeFromUrlString(tokenStr);
						// LOG.debug("Hive Token: " + hmsToken);

						// Add the token to the credentials.
						ugi.addToken(
								new Text("hive.metastore.delegation.token"),
								hmsToken);
						LOG.trace("Added hive.metastore.delegation.token to conf.");
					} else {
						LOG.debug("Username or principal == NULL");
						LOG.debug("username= " + username);
						LOG.debug("principal= " + principal);
						throw new IllegalArgumentException(
								"username and/or principal is equal to null!");
					}

				} else {
					LOG.info("HiveMetaStore configured in local mode");
				}
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} catch (MetaException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public class HydrographRuntimeException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7891832980227676974L;

		public HydrographRuntimeException(String msg) {
			super(msg);
		}

		public HydrographRuntimeException(Throwable e) {
			super(e);
		}
	}
	
	/**
	 * Method to kill the job
	 */
	@Override
	public void kill() {
		if (runtimeContext.getCascade() != null) {
			for (Cascade cascade : runtimeContext.getCascade()) {
				cascade.stop();
			}
		} else
			System.exit(0);
	}
}