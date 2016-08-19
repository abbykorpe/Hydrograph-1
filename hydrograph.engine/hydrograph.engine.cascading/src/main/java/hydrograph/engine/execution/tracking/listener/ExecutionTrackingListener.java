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

import java.io.IOException;

import cascading.cascade.Cascade;
import cascading.cascade.CascadeListener;
import cascading.flow.Flow;
import cascading.stats.FlowNodeStats;
import cascading.stats.FlowStepStats;
import hydrograph.engine.execution.tracking.JobInfo;
import hydrograph.engine.utilities.OrderedProperties;
import hydrograph.engine.utilities.OrderedPropertiesHelper;

/**
 * @author ankitsh
 *
 */
public class ExecutionTrackingListener {

	private static boolean isPluginPresent;

	/** Method to add listener if execution tracking is enabled
	 *  also checks whether execution is done on local or cluster mode.  
	 *  @param cascade
	 * @param jobInfo 
	 */
	public static void addListener(Cascade cascade, JobInfo jobInfo) {
		ComponentStatsListener statsListener = new ComponentStatsListener(jobInfo);
		for (Flow<?> flow : cascade.getFlows()) {
			for (FlowStepStats flowStepStats : flow.getFlowStats().getFlowStepStats()) {
				if (isLocalFlowExecution(cascade)) {
					flowStepStats.addListener(statsListener);
				} else {
					for (FlowNodeStats flowNodeStats : flowStepStats.getFlowNodeStats()) {
						flowNodeStats.addListener(statsListener);
					}
				}
			}
		}
	}
	

	private static boolean isLocalFlowExecution(Cascade cascade) {
		Flow<?> flow = cascade.getFlows().get(0);
		// PlatformInfo PlatformInfo = flow.getPlatformInfo();
		return flow.stepsAreLocal();
	}

	private static void checkTrackingPlugin() {
		OrderedProperties properties = new OrderedProperties();
		try {
			properties = OrderedPropertiesHelper.getOrderedProperties("RegisterPlugin.properties");
		} catch (IOException e) {
			throw new RuntimeException("Error reading the properties file: RegisterPlugin.properties" + e);
		}

		for (Object pluginClass : properties.keySet()) {
			if (pluginClass.equals("ExecutionTrackingPlugin")) {
				isPluginPresent = true;
			}
		}
	}

	/** Method to check if execution tracking plugin is enabled or not
	 ** @return boolean value
	 */
	public static boolean isTrackingPluginPresent() {
		checkTrackingPlugin();
		return isPluginPresent;
	}
	
}
