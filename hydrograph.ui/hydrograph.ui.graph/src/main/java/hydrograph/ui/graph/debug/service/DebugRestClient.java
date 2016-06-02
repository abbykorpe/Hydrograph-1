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

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;

/**
 * @author Bitwise
 * call to the rest service
 *
 */
public class DebugRestClient {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DebugRestClient.class);
	
	
	public DebugRestClient() {
		 
	}
	
	
	public String calltoReadService(String ipAddress, String portNumber, String basePath, String jobId, String componentId, String socketId, String userId, String password,String FILE_SIZE_TO_READ) throws  IOException {

		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://" + ipAddress + ":"
				+ portNumber + "/read");
		postMethod.addParameter("jobId", jobId);
		postMethod.addParameter("componentId", componentId);
		postMethod.addParameter("socketId", socketId);
		postMethod.addParameter("basePath", basePath);
		postMethod.addParameter("userId", userId);
		postMethod.addParameter("password", password);
		postMethod.addParameter("file_size", FILE_SIZE_TO_READ);
		postMethod.addParameter("host_name", ipAddress);

		java.util.Date date = new java.util.Date();
		System.out.println("+++ Start: " + new Timestamp(date.getTime()));

		int response = httpClient.executeMethod(postMethod);
		System.out.println("response: " + response);
		InputStream inputStream = postMethod.getResponseBodyAsStream();

		byte[] buffer = new byte[1024 * 1024 * 5];
		String path = null;
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		System.out.println("response of service: "+path);
		return path;
	}
	
	/**
	 *	Request to service with following parameters: BasePath, JobId, ComponentId, SocketId, UserId, Password and 
	 *	get response in JSON format.
	 */
	public JSONArray callRestService(String ipAddress, String portNumber, String basePath, String jobId, String componentId, String socketId, String userId, String password) throws IOException, JSONException {
		HttpClient httpClient = new HttpClient();
		String url = Constants.HTTP_PROTOCOL + ipAddress + ":" + portNumber + Constants.ROUTE_TO_READ_DATA;
		
		logger.debug("Host: {}, Port_No: {}, Route: {}, Job Id: {}, Component Id: {}, Socket ID: {}, User ID:{}", 
				new Object[] { ipAddress, portNumber, Constants.ROUTE_TO_READ_DATA, jobId, componentId, socketId, userId});
		
		PostMethod postMethod = new PostMethod(url); //"http://10.130.248.53:4567/read"
		postMethod.addParameter(Constants.BASE_PATH, basePath);
		postMethod.addParameter(Constants.JOB_ID, jobId);
		postMethod.addParameter(Constants.COMPONENT_ID, componentId);
		postMethod.addParameter(Constants.SOCKET_ID, socketId);
		postMethod.addParameter(Constants.USER_ID, userId);
		postMethod.addParameter(Constants.PASSWORD, password);

		BufferedReader bufferedReader = null;
		JSONArray jsonArray = null;
	 
			long start = System.currentTimeMillis();
			int status = httpClient.executeMethod(postMethod);
			logger.trace("Requesting to rest service...");
			logger.info("Time taken by /read endpoint ( in milliseconds): ", System.currentTimeMillis()-start);
			
			InputStream inputStream = postMethod.getResponseBodyAsStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			
			while ((line = bufferedReader.readLine()) != null) {
				jsonArray =  new JSONArray(line);
			}
			return jsonArray;
		 
	}
	
	/**
	 *	Request to service with following parameters: BasePath, JobId, ComponentId, SocketId, UserId, Password 
	 *	to remove files from edge node.
	 *	
	 */
	public void removeDebugFiles(String ipAddress, String portNumber, String basePath, String jobId, String componentId, String socketId, String userId, String password){
		HttpClient httpClient = new HttpClient();
		String url = Constants.HTTP_PROTOCOL + ipAddress + ":"+portNumber + Constants.ROUTE_TO_REMOVE_FILES;
		 
		PostMethod postMethod = new PostMethod(url); //"http://10.130.248.53:4567/delete"
		postMethod.addParameter(Constants.BASE_PATH, basePath);
		postMethod.addParameter(Constants.JOB_ID, jobId);
		postMethod.addParameter(Constants.COMPONENT_ID, componentId);
		postMethod.addParameter(Constants.SOCKET_ID, socketId);
		postMethod.addParameter(Constants.USER_ID, userId);
		postMethod.addParameter(Constants.PASSWORD, password);
		
		try {
			long start = System.currentTimeMillis();
			int status = httpClient.executeMethod(postMethod);
			logger.trace("Requesting to rest service...");
			logger.info("Time taken by /read endpoint ( in milliseconds): ", System.currentTimeMillis()-start);
		} catch (HttpException e) {
			logger.error("Invalid response code: ", e);
		} catch (IOException exception) {
			logger.error("Connection failed", exception);
		}
	}
 
}


