/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *******************************************************************************/
package hydrograph.engine.cascading.filters;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;

@SuppressWarnings("rawtypes")
public class RemoveDupsOutLinkFilter extends BaseOperation implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4244170052729253134L;

	@Override
	public boolean isRemove(FlowProcess flowProcess, FilterCall filterCall) {

		return !filterCall.getArguments().getBoolean("keep");
	}

}
