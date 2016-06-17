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
package hydrograph.engine.cascading.filters;

import hydrograph.engine.cascading.assembly.context.RecordFilterContext;
import hydrograph.engine.cascading.assembly.handlers.FilterCustomHandler;
import hydrograph.engine.cascading.assembly.handlers.RecordFilterHandlerBase;
import cascading.flow.FlowProcess;
import cascading.management.annotation.Property;
import cascading.management.annotation.PropertyDescription;
import cascading.management.annotation.Visibility;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;
import cascading.operation.OperationCall;

public class RecordFilter extends BaseOperation<RecordFilterContext> implements
		Filter<RecordFilterContext> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8615924319575363829L;
	private RecordFilterHandlerBase filterHandler;

	public RecordFilter(RecordFilterHandlerBase selectCustomHandler) {

		this.filterHandler = selectCustomHandler;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(FlowProcess flowProcess,
			OperationCall<RecordFilterContext> call) {

		RecordFilterContext context = new RecordFilterContext();
		call.setContext(context);
		context.setHandlerContext(filterHandler.prepare());

	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isRemove(FlowProcess arg0,
			FilterCall<RecordFilterContext> call) {

		return filterHandler.isRemove(call);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void cleanup(FlowProcess flowProcess,
			OperationCall<RecordFilterContext> call) {

		filterHandler.cleanup(call);

	}

	@Property(name = "Operation Class", visibility = Visibility.PUBLIC)
	@PropertyDescription("Filter Operation Executed by this Component")
	public String getOperationClass() {

		return ((FilterCustomHandler) filterHandler).getTransformClass();
	}

}
