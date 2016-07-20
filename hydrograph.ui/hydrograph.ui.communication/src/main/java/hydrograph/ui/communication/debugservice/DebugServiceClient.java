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

package hydrograph.ui.communication.debugservice;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.communication.debugservice.method.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 
 * Debug service client responsible for requesting debug service to get or delete debug files
 * 
 * @author Bitwise
 *
 */
public class DebugServiceClient {
	public static DebugServiceClient INSTANCE = new DebugServiceClient();
	
	private DebugServiceClient() {		
	}

	/**
	 * 
	 * Method to delete debug file
	 * 
	 * @param jobDetails
	 * @throws NumberFormatException
	 * @throws HttpException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void deleteDebugFile(JobDetails jobDetails)
			throws NumberFormatException, HttpException, MalformedURLException, IOException {
		executePostMethod(Provider.INSTANCE.getDeleteDebugFileMethod(jobDetails));
	}

	/**
	 * 
	 * Method to get debug file
	 * 
	 * @param jobDetails
	 * @param fileSize
	 * @return debug file
	 * @throws NumberFormatException
	 * @throws HttpException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getDebugFile(JobDetails jobDetails,String fileSize)
			throws NumberFormatException, HttpException, MalformedURLException, IOException {
		PostMethod method = Provider.INSTANCE.getDebugFileMethod(jobDetails,fileSize);
		executePostMethod(method);
		return getFilePathFromPostResponse(method);
	}
	
	private void executePostMethod(PostMethod postMethod) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		httpClient.executeMethod(postMethod);
	}
	
	private String getFilePathFromPostResponse(PostMethod postMethod) throws IOException {
		String path = null;
		InputStream inputStream = postMethod.getResponseBodyAsStream();
		byte[] buffer = new byte[1024];
		while ((inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		return path;
	}
	
	/**
	 * 
	 * Method to delete debug base files
	 * 
	 * @param host
	 * @param port
	 * @param unique job ID
	 * @param base path
	 * @param User
	 * @param password
	 * @throws NumberFormatException
	 * @throws HttpException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void deleteBasePathFiles(String host, String port, String uniqueJobID, String basePath, String user, String password) 
			throws NumberFormatException, HttpException, MalformedURLException, IOException{
		executePostMethod(Provider.INSTANCE.getDeleteBasePathFileMethod(host, port, uniqueJobID, basePath, user, password));
	}
	
	/**
	 * Method to get file based on the filter condition
	 * @param jsonString Filter condition string
	 * @param jobDetails
	 * @return
	 * @throws NumberFormatException
	 * @throws HttpException
	 * @throws IOException
	 */
	public String getFilteredFile(String jsonString,JobDetails jobDetails) throws NumberFormatException, HttpException, IOException {
		PostMethod method=Provider.INSTANCE.getFilteredFileMethod(jsonString, jobDetails);
		executePostMethod(method);
		return getFilePathFromPostResponse(method);
	}
	
}