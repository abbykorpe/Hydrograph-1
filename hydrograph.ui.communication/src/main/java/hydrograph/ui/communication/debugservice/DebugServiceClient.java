package hydrograph.ui.communication.debugservice;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.communication.debugservice.method.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class DebugServiceClient {
	public static DebugServiceClient INSTANCE = new DebugServiceClient();
	
	private DebugServiceClient() {		
	}

	public void deleteDebugFile(JobDetails jobDetails)
			throws NumberFormatException, HttpException, MalformedURLException, IOException {
		executePostMethod(Provider.INSTANCE.getDeleteDebugFileMethod(jobDetails));
	}

	public String getDebugFile(JobDetails jobDetails,String fileSize)
			throws NumberFormatException, HttpException, MalformedURLException, IOException {
		PostMethod method = Provider.INSTANCE.getDebugFileMethod(jobDetails,fileSize);
		executePostMethod(method);
		return getDebugFilePathFromPostResponse(method);
	}
	
	private void executePostMethod(PostMethod postMethod) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		httpClient.executeMethod(postMethod);
	}
	
	private String getDebugFilePathFromPostResponse(PostMethod postMethod) throws IOException {
		String path = null;
		InputStream inputStream = postMethod.getResponseBodyAsStream();
		byte[] buffer = new byte[1024];
		while ((inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		return path;
	}
}
