package com.bitwise.app.graph.debug.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.bitwise.app.common.services.IDebugService;
import com.bitwise.app.graph.handler.DebugHandler;
import com.bitwise.app.graph.job.Job;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.debug.api.DebugFilesReader;

public class DebugServiceHandler implements IDebugService{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DebugServiceHandler.class);
	
	@Override
	public void delete() {
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
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}
    	} catch (Exception e) {
    		logger.debug(e.getMessage());
		}
	}

}
