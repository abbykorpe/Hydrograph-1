package com.bitwise.app.graph.debug.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.bitwise.app.common.debug.service.IDebugService;
import com.bitwise.app.graph.handler.DebugHandler;
import com.bitwise.app.graph.job.Job;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.debug.api.DebugFilesReader;

public class DebugServiceHandler  implements IDebugService{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DebugServiceHandler.class);

	@Override
	public void deleteDebugFiles() {
		logger.info("call to api to remove debug files::::::::");
		try {
		
		Map<String, Job> jobMap = DebugHandler.getJobMap();
		
		Set<String> keySet = jobMap.keySet();
		
		for (String jobId : keySet) {
			Job job=jobMap.get(jobId);
			DebugFilesReader debugFilesReader = new DebugFilesReader(job.getBasePath(), job.getUniqueJobId(), "IFDelimite_01", "out0");
			try {
				debugFilesReader.delete();
				jobMap.remove(jobId);
			} catch (IOException exception) {
				logger.error("No files available to remove" +exception);
			}
		}
    	} catch (Exception exception) {
    		logger.error("Failes to call DebugApi" +exception);
		}
	}
}
