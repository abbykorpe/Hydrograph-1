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

import cascading.flow.FlowProcess;
import cascading.management.annotation.Property;
import cascading.management.annotation.PropertyDescription;
import cascading.management.annotation.Visibility;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;
import cascading.operation.OperationCall;
import hydrograph.engine.cascading.assembly.context.RecordFilterContext;
import hydrograph.engine.cascading.assembly.handlers.FilterCustomHandler;
import hydrograph.engine.cascading.assembly.handlers.RecordFilterHandlerBase;
import hydrograph.engine.transformation.userfunctions.base.FilterBase;
import hydrograph.engine.utilities.UserClassLoader;

public class RecordFilter extends BaseOperation<RecordFilterContext>implements Filter<RecordFilterContext> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8615924319575363829L;
	private RecordFilterHandlerBase filterHandler;
	private String counterName;
	private String filterClassName;

	public RecordFilter(RecordFilterHandlerBase selectCustomHandler, String previousName) {

		this.filterHandler = selectCustomHandler;
		this.counterName = previousName;
		this.filterClassName = ((FilterCustomHandler) filterHandler).getTransformClass();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(FlowProcess flowProcess, OperationCall<RecordFilterContext> call) {

		Object filterObject = UserClassLoader.loadAndInitClass(filterClassName);
		RecordFilterContext context = new RecordFilterContext();
		call.setContext(context);
		context.setFilterClass(filterObject);
		if (filterObject instanceof FilterBase) {
			context.setHandlerContext(filterHandler.prepare());
		} else {
			context.setCounterName(counterName);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isRemove(FlowProcess flowProcess, FilterCall<RecordFilterContext> call) {
		if (call.getContext().getFilterClass() instanceof FilterBase) {
			return filterHandler.isRemove(call);
		} else {
			return ((Filter<RecordFilterContext>) call.getContext().getFilterClass()).isRemove(flowProcess, call);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void cleanup(FlowProcess flowProcess, OperationCall<RecordFilterContext> call) {
		if (call.getContext().getFilterClass() instanceof FilterBase) {
			filterHandler.cleanup(call);
		}
	}

	@Property(name = "Operation Class", visibility = Visibility.PUBLIC)
	@PropertyDescription("Filter Operation Executed by this Component")
	public String getOperationClass() {

		return ((FilterCustomHandler) filterHandler).getTransformClass();
	}

}
