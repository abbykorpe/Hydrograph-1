/********************************************************************************
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
 ******************************************************************************/


package hydrograph.ui.graph.debug.service;

import hydrograph.ui.common.debug.service.IDebugService;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.utility.ViewDataUtils;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;


/**
 * The Class PurgeViewDataFiles will remove ViewData Files on tool close.
 * @author Bitwise
 *
 */
public class PurgeViewDataFiles  implements IDebugService{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(PurgeViewDataFiles.class);

	@Override
	public void deleteDebugFiles() {
		logger.info("call to api to remove debug files::::::::");
		ViewDataUtils dataUtils = ViewDataUtils.getInstance();
		Map<String, List<Job>> viewDataJobMap = ViewDataUtils.getJob();
		
		if(Utils.INSTANCE.isPurgeViewDataPrefSet()){
			for(Entry<String, List<Job>> entry : viewDataJobMap.entrySet()){
				List<Job> value =  (List<Job>) entry.getValue();
		        for(Job job : value){
		        	dataUtils.deleteBasePathDebugFiles(job);
		        	dataUtils.deleteSchemaAndDataViewerFiles(job.getUniqueJobId());
		        }
			}
			ViewDataUtils.getJob().clear();
		}
	}
	
	
	
	
}
