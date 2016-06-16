/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.server.debug.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.ClientProtocolException;

public class DebugServiceClient {

	String JOB_ID = "generateRecord";
	String COMPONENT_ID = "input1";
	String SOCKET_ID = "out0";
	String BASE_PATH = "hdfs://vm2ubuntu01.bitwiseglobal.net:8020/home/hduser/TEXTDEBUG/TextData_400MB/DebugFiles/";
	String USER_ID = "hduser";
	String PASSWORD = "Bitwise2012";
	String FILE_SIZE_TO_READ = "419430400";
	String HOST_NAME = "10.130.248.53";
	String PORT = "8004";

	public void calltoReadService() throws ClientProtocolException, IOException {

		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://" + HOST_NAME + ":"
				+ PORT + "/read");
		postMethod.addParameter("jobId", JOB_ID);
		postMethod.addParameter("componentId", COMPONENT_ID);
		postMethod.addParameter("socketId", SOCKET_ID);
		postMethod.addParameter("basePath", BASE_PATH);
		postMethod.addParameter("userId", USER_ID);
		postMethod.addParameter("password", PASSWORD);
		postMethod.addParameter("file_size", FILE_SIZE_TO_READ);
		postMethod.addParameter("host_name", HOST_NAME);

		BufferedReader bufferedReader = null;

		int response = httpClient.executeMethod(postMethod);
		// System.out.println("response: " + response);
		InputStream inputStream = postMethod.getResponseBodyAsStream();

		byte[] buffer = new byte[1024 * 1024 * 5];
		String path = null;
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		// System.out.println("response of service: " + path);
	}

	public void calltoDeleteLocalDebugService() throws HttpException,
			IOException {

		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://" + HOST_NAME + ":"
				+ PORT + "/deleteLocalDebugFile");
		postMethod.addParameter("jobId", JOB_ID);
		postMethod.addParameter("componentId", COMPONENT_ID);
		postMethod.addParameter("socketId", SOCKET_ID);

		BufferedReader bufferedReader = null;

		java.util.Date date = new java.util.Date();
		System.out.println("+++ Start: " + new Timestamp(date.getTime()));

		int response = httpClient.executeMethod(postMethod);
		System.out.println("response: " + response);
	}

	public static void main(String[] args) {
		DebugServiceClient client = new DebugServiceClient();
		try {
			System.out.println("+++ Start: "
					+ new Timestamp((new Date()).getTime()));
			client.calltoReadService();
			// client.calltoDeleteLocalDebugService();
			System.out.println("done:");
			System.out.println("+++ End: "
					+ new Timestamp((new Date()).getTime()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
