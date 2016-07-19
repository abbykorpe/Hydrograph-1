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
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

public class DebugServiceHandler  implements IDebugService{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DebugServiceHandler.class);

	@Override
	public void deleteDebugFiles() {
		logger.info("call to api to remove debug files::::::::");
		Map<String, Job> jobMap = DebugHandler.getJobMap();
		
		Set<String> keySet = jobMap.keySet();
		
		/*try {
			int portPID = Integer.parseInt(DebugHelper.INSTANCE.getServicePortPID());
			DebugHelper.INSTANCE.killPortProcess(portPID);
		} catch (NumberFormatException exception) {
			logger.error(exception.getMessage());
		} catch (IOException exception) {
			logger.error(exception.getMessage());
		}*/
		
		/*for (String jobId : keySet) {
			Job job=jobMap.get(jobId);
			DebugFilesReader debugFilesReader = new DebugFilesReader(job.getBasePath(), job.getUniqueJobId(), "IFDelimite_01", "out0");
			try {
				debugFilesReader.delete();
				jobMap.remove(jobId);
			} catch (IOException exception) {
				logger.error("No files available to remove", exception);
			}
		}*/
    	
	}
}
