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

 
package com.bitwise.app.graph.debug.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;

/**
 * @author Bitwise
 *
 */
public class DebugRestClient {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DebugRestClient.class);
	private String jobId;
	private String componentId;
	private String socketId;
	private String basePath;
	private String ipAddress;
	private String userId;
	private String password;
	
	/**
	 * @param args
	 * @throws HttpException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public DebugRestClient(String IPAddress,String basePath, String jobId, String componentId, String socketId, String userId, String password) {
		logger.debug("call to rest service..."); 
		this.ipAddress = IPAddress;
		 this.basePath = basePath;
		 this.jobId = jobId;
		 this.componentId = componentId;
		 this.socketId = socketId;
		 this.userId = userId;
		 this.password = password;
	}
	
	public JSONArray callRestService() throws HttpException, IOException, JSONException{
		HttpClient httpClient = new HttpClient();
		String ip = Constants.HTTP_PROTOCOL + ipAddress + Constants.PORT_NO + Constants.ROUTE;
		 
		PostMethod postMethod = new PostMethod(ip);//"http://10.130.248.53:4567/debug"
		postMethod.addParameter(Constants.USER_ID, userId);
		postMethod.addParameter(Constants.PASSWORD, password);
		postMethod.addParameter(Constants.BASE_PATH, basePath);
		postMethod.addParameter(Constants.JOB_ID, jobId);
		postMethod.addParameter(Constants.COMPONENT_ID, componentId);
		postMethod.addParameter(Constants.SOCKET_ID, socketId);

		int status = httpClient.executeMethod(postMethod);
		logger.debug("Rest Service Response Status :{}, status: {}", status);
		
		InputStream inputStream = postMethod.getResponseBodyAsStream();
		
		JSONArray jsonArray = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		 
		 String line = "";
		   while ((line = br.readLine()) != null) {
			   
			   jsonArray =  new JSONArray(line);
		   }

		   return jsonArray;
	}
 
}
