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
package hydrograph.engine.execution.tracking.listener;

import cascading.stats.CascadingStats;
import cascading.stats.CascadingStats.Status;
import cascading.stats.StatsListener;
import hydrograph.engine.execution.tracking.JobInfo;
import hydrograph.engine.execution.tracking.JobInfo.ElementGraphNotFoundException;

public class ComponentStatsListener implements StatsListener {

	@Override
	public void notify(CascadingStats stats, Status fromStatus, Status toStatus) {
		try {
			JobInfo.getInstance().storeComponentStats(stats);
		} catch (ElementGraphNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
