/*
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
 */
package hydrograph.engine.cascading.assembly;

import hydrograph.engine.assembly.entity.UnionAllEntity;
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

public class UnionAllAssembly extends BaseComponent<UnionAllEntity> {

	private static final long serialVersionUID = -1271944466197184074L;
	private static Logger LOG = LoggerFactory.getLogger(UnionAllAssembly.class);

	private UnionAllEntity unionAllEntity;

	public UnionAllAssembly(UnionAllEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	protected void createAssembly() {
		try {
			if (LOG.isTraceEnabled()) {
				LOG.trace(unionAllEntity.toString());
			}
			LOG.trace("Creating union all assembly for '"
					+ unionAllEntity.getComponentId() + "' for socket: '"
					+ unionAllEntity.getOutSocket().getSocketId()
					+ "' of type: '"
					+ unionAllEntity.getOutSocket().getSocketType() + "'");

			ArrayList<Fields> fieldList = componentParameters
					.getInputFieldsList();

			Pipe[] inputPipes = alignfields(
					componentParameters.getInputPipes(), fieldList);

			Pipe outPipe = new Merge("unionAll:"+unionAllEntity.getComponentId()
					+ "_"+unionAllEntity.getOutSocket().getSocketId(), inputPipes);

			setHadoopProperties(outPipe.getStepConfigDef());

			setOutLink("out", unionAllEntity.getOutSocket().getSocketId(),
					unionAllEntity.getComponentId(), outPipe, fieldList.get(0));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}

	}

	private String generateMessageForAllFields(ArrayList<Fields> fieldList) {

		String fields = null;
		for (int i = 0; i < fieldList.size(); i++) {
			if (fields == null)
				fields = "[" + fieldList.get(i) + "],";
			else if (i != fieldList.size() - 1)
				fields = fields + "[" + fieldList.get(i) + "],";
			else
				fields = fields + "[" + fieldList.get(i) + "]";
		}

		return fields;
	}

	private Pipe[] alignfields(ArrayList<Pipe> arrayList,
			ArrayList<Fields> fieldList) {
		Pipe[] inputPipes = new Pipe[componentParameters.getInputPipes().size()];
		int i = 0;
		for (Pipe eachPipe : arrayList) {
			inputPipes[i] = new Each(eachPipe, fieldList.get(i),
					new CopyFields(fieldList.get(i)), Fields.RESULTS);
			i++;
		}
		return inputPipes;
	}

	@Override
	public void initializeEntity(UnionAllEntity assemblyEntityBase) {
		this.unionAllEntity = assemblyEntityBase;
	}

}
