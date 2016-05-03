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

import hydrograph.engine.assembly.entity.CloneEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Pipe;

public class CloneAssembly extends BaseComponent {

	private static final long serialVersionUID = 8145806669418685707L;

	CloneEntity cloneEntity;
	private static Logger LOG = LoggerFactory.getLogger(CloneAssembly.class);

	public CloneAssembly(AssemblyEntityBase baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		cloneEntity = (CloneEntity) assemblyEntityBase;
	}

	@Override
	protected void createAssembly() {
		try {
			if (LOG.isTraceEnabled()) {
				LOG.trace(cloneEntity.toString());
			}
			Pipe inputPipes = componentParameters.getInputPipe();
			Pipe clonePipe;
			for (OutSocket outSocket : cloneEntity.getOutSocketList()) {

				LOG.trace("Creating clone assembly for '" + cloneEntity.getComponentId() + "' for socket: '"
						+ outSocket.getSocketId() + "' of type: '" + outSocket.getSocketType() + "'");
				clonePipe = new Pipe(cloneEntity.getComponentId() + "_out" + outSocket.getSocketId(), inputPipes);
				setHadoopProperties(clonePipe.getStepConfigDef());
				setOutLink(outSocket.getSocketType(), outSocket.getSocketId(), cloneEntity.getComponentId(), clonePipe,
						componentParameters.getInputFields());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}
}