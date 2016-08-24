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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import hydrograph.engine.assembly.entity.FilterEntity;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.handlers.FilterCustomHandler;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.filters.RecordFilter;
import hydrograph.engine.utilities.ComponentHelper;

public class FilterAssembly extends BaseComponent<FilterEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7728233694925104673L;

	private FilterEntity filterEntity;
	private static Logger LOG = LoggerFactory.getLogger(FilterAssembly.class);

	public FilterAssembly(FilterEntity baseComponentEntity,
			ComponentParameters parameters) {
		super(baseComponentEntity, parameters);
	}

	@Override
	protected void createAssembly() {
		try {
			if (LOG.isTraceEnabled()) {
				LOG.trace(filterEntity.toString());
			}
			for (OutSocket outSocket : filterEntity.getOutSocketList()) {
				LOG.trace("Creating filter assembly for '"
						+ filterEntity.getComponentId() + "' for socket: '"
						+ outSocket.getSocketId() + "' of type: '"
						+ outSocket.getSocketType() + "'");
				createAssemblyFor(outSocket.getSocketId(),
						outSocket.getSocketType());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	private void createAssemblyFor(String socketId, String socketType) {

		boolean isUnused = false;

		if (socketType.equals("unused")) {
			isUnused = true;
		}

		Pipe filterPipe = new Pipe(ComponentHelper.getComponentName("filter",filterEntity.getComponentId() ,
				 socketId), componentParameters.getInputPipe());

		FilterCustomHandler filterCustomHandler = new FilterCustomHandler(
				new Fields(filterEntity.getOperation()
						.getOperationInputFields()), filterEntity
						.getOperation().getOperationClass(), filterEntity
						.getOperation().getOperationProperties(), isUnused);

		RecordFilter selectCustomFilter = new RecordFilter(filterCustomHandler,componentParameters.getInputPipe().getName());

		setHadoopProperties(filterPipe.getStepConfigDef());

		filterPipe = new Each(filterPipe, new Fields(filterEntity
				.getOperation().getOperationInputFields()), selectCustomFilter);

		setOutLink(socketType, socketId, filterEntity.getComponentId(),
				filterPipe, componentParameters.getInputFields());
	}

	@Override
	public void initializeEntity(FilterEntity assemblyEntityBase) {
		this.filterEntity=assemblyEntityBase;
	}

}
