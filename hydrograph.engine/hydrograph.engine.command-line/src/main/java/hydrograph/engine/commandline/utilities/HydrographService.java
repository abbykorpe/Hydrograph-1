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
package hydrograph.engine.commandline.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hydrograph.engine.execution.tracking.ComponentInfo;

public class HydrographService {
	private HydrographExecution execution;
	public HydrographService() {
		execution = new HydrographExecution();
	}

	public void executeGraph(String[] args) throws Exception {
			execution.run(args);
	}
	public List<ComponentInfo> getStatus() {
		if(execution.getExecutionTracking()!=null)
			return new ArrayList<>(execution.getExecutionTracking());
		else
			return Collections.emptyList();
	}

	public void kill() {
		execution.kill();
	}
}