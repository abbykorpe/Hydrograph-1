package hydrograph.ui.graph.debug.service;

import hydrograph.ui.common.debug.service.IDebugService;
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

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
				logger.error("No files available to remove", exception);
			}
		}
    	} catch (Exception exception) {
    		logger.error("Failes to call DebugApi", exception);
		}
	}
}
