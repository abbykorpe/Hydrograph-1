package hydrograph.ui.communication.debugservice.method;

import java.net.MalformedURLException;
import java.net.URL;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.communication.debugservice.constants.DebugServiceMethods;
import hydrograph.ui.communication.debugservice.constants.DebugServicePostParameters;

import org.apache.commons.httpclient.methods.PostMethod;

public class Provider {	
	private static String POST_PROTOCOL="http";
	public static Provider INSTANCE = new Provider();
	
	private Provider(){
		
	}

	public PostMethod getDeleteDebugFileMethod(JobDetails jobDetails) throws NumberFormatException, MalformedURLException {
		
		URL url = new URL(POST_PROTOCOL, jobDetails.getHost(), Integer.valueOf(jobDetails.getPort()), DebugServiceMethods.DELETE_DEBUG_CSV_FILE);
		
		PostMethod postMethod = new PostMethod(url.toString());
		postMethod.addParameter(DebugServicePostParameters.JOB_ID, jobDetails.getUniqueJobID());
		postMethod.addParameter(DebugServicePostParameters.COMPONENT_ID, jobDetails.getComponentID());
		postMethod.addParameter(DebugServicePostParameters.SOCKET_ID, jobDetails.getComponentSocketID());

		return postMethod;
	}

	public PostMethod getDebugFileMethod(JobDetails jobDetails,String fileSize) throws NumberFormatException, MalformedURLException {
		
		URL url = new URL(POST_PROTOCOL, jobDetails.getHost(), Integer.valueOf(jobDetails.getPort()), DebugServiceMethods.GET_DEBUG_FILE_PATH);
				
		PostMethod postMethod = new PostMethod(url.toString());
		postMethod.addParameter(DebugServicePostParameters.JOB_ID, jobDetails.getUniqueJobID());
		postMethod.addParameter(DebugServicePostParameters.COMPONENT_ID, jobDetails.getComponentID());
		postMethod.addParameter(DebugServicePostParameters.SOCKET_ID, jobDetails.getComponentSocketID());
		postMethod.addParameter(DebugServicePostParameters.BASE_PATH, jobDetails.getBasepath());
		postMethod.addParameter(DebugServicePostParameters.USER_ID, jobDetails.getUsername());
		postMethod.addParameter(DebugServicePostParameters.PASSWORD, jobDetails.getPassword());
		postMethod.addParameter(DebugServicePostParameters.FILE_SIZE, fileSize);
		postMethod.addParameter(DebugServicePostParameters.HOST_NAME, jobDetails.getHost());
		
		return postMethod;
	}
}
