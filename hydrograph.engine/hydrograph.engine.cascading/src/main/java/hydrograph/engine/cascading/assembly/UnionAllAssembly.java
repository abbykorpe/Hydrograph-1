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
package hydrograph.engine.cascading.assembly;

import hydrograph.engine.assembly.entity.UnionAllEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.functions.CopyFields;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Each;
import cascading.pipe.Merge;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;

public class UnionAllAssembly extends BaseComponent {

	private static final long serialVersionUID = -1271944466197184074L;
	private static Logger LOG = LoggerFactory.getLogger(UnionAllAssembly.class);

	private UnionAllEntity unionAllEntity;

	public UnionAllAssembly(AssemblyEntityBase baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	protected void createAssembly() {
		try {
			if (LOG.isTraceEnabled()) {
				LOG.trace(unionAllEntity.toString());
			}
			LOG.trace("Creating union all assembly for '" + unionAllEntity.getComponentId() + "' for socket: '"
					+ unionAllEntity.getOutSocket().getSocketId() + "' of type: '"
					+ unionAllEntity.getOutSocket().getSocketType() + "'");

			Fields firstInputFields = componentParameters.getInputFields();

			Pipe[] inputPipes = alignfields(componentParameters.getInputPipes(), firstInputFields);

			Pipe outPipe = new Merge(unionAllEntity.getComponentId() + "_merged_out", inputPipes);

			setHadoopProperties(outPipe.getStepConfigDef());

			setOutLink("out", unionAllEntity.getOutSocket().getSocketId(), unionAllEntity.getComponentId(), outPipe,
					firstInputFields);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}

	}

	private Pipe[] alignfields(ArrayList<Pipe> arrayList, Fields requiredFields) {
		Pipe[] inputPipes = new Pipe[componentParameters.getInputPipes().size()];
		int i = 0;
		for (Pipe eachPipe : arrayList) {
			inputPipes[i++] = new Each(eachPipe, requiredFields, new CopyFields(requiredFields), Fields.RESULTS);
		}
		return inputPipes;
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		unionAllEntity = (UnionAllEntity) assemblyEntityBase;
	}

}
