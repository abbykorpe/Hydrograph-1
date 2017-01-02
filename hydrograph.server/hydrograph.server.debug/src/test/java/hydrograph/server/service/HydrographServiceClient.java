/*******************************************************************************
 *  Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package hydrograph.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.ClientProtocolException;

public class HydrographServiceClient {

	String JOB_ID = "CloneDebug";
	String COMPONENT_ID = "input1";
	String SOCKET_ID = "out0";

	// String BASE_PATH = "C:/Users/Bhaveshs/git/hydrograph";
	String BASE_PATH = "hdfs://UbuntuD1.bitwiseglobal.net:8020/user/hduser";
	String USER_ID = "hduser";
	String PASSWORD = "Bitwise2012";
	String FILE_SIZE_TO_READ = "1";
	String HOST_NAME = "127.0.0.1";
	// String HOST_NAME = "10.130.248.53";
	String PORT = "8004";

	public void chcekConnectionStatus() throws ClientProtocolException, IOException {

		HttpClient httpClient = new HttpClient();

		String oraclejson = "{\"username\":\"htcd\",\"password\":\"htcd\",\"hostname\":\"DBDEVSRV\",\"sid\":\"PRACTICE\",\"drivertype\":\"thin\",\"dbtype\":\"oracle\"}";
		// String mysqljson =
		// "{\"username\":\"hduser\",\"password\":\"Bitwise2012\",\"hostname\":\"10.130.248.53\",\"port\":\"3306\",\"database\":\"test1\",\"dbtype\":\"mysql\"}";
		PostMethod postMethod = new PostMethod("http://" + HOST_NAME + ":" + PORT + "/getConnectionStatus");

		postMethod.addParameter("request_parameters", oraclejson);

		int response = httpClient.executeMethod(postMethod);
		InputStream inputStream = postMethod.getResponseBodyAsStream();

		byte[] buffer = new byte[1024 * 1024 * 5];
		String path = null;
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		System.out.println("Response of service: " + path);
		System.out.println("==================");
	}

	public void calltoReadMetastore() throws ClientProtocolException, IOException {

		HttpClient httpClient = new HttpClient();

		// String json =
		// "{\"condition\":\"abc\",\"schema\":[{\"fieldName\":\"f1\",\"dateFormat\":\"\",\"dataType\":\"1\",\"scale\":\"scale\",\"dataTypeValue\":\"java.lang.String\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f2\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.util.Date\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f3\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.util.Date\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f4\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.math.BigDecimal\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"}],\"fileSize\":1,\"jobDetails\":{\"host\":\"127.0.0.1\",\"port\":\"8005\",\"username\":\"hduser\",\"password\":\"Bitwise2012\",\"basepath\":\"C:/Users/santlalg/git/Hydrograph/hydrograph.engine/hydrograph.engine.command-line\",\"uniqueJobID\":\"debug_job\",\"componentID\":\"input\",\"componentSocketID\":\"out0\",\"isRemote\":false}}";
		// String oraclejson = "{\"query\":\"Select * from
		// testingTable414\",\"username\":\"htcd\",\"password\":\"htcd\",\"hostname\":\"DBDEVSRV\",\"port\":\"1521\",\"sid\":\"PRACTICE\",\"drivertype\":\"thin\",\"dbtype\":\"oracle\"}";
		// String oraclejson =
		// "{\"table\":\"testingTable414\",\"username\":\"htcd\",\"password\":\"htcd\",\"hostname\":\"DBDEVSRV\",\"port\":\"1521\",\"sid\":\"PRACTICE\",\"drivertype\":\"thin\",\"dbtype\":\"oracle\"}";
		String oraclejson = "{\"table\":\"testingTable1113\",\"username\":\"htcd\",\"password\":\"htcd\",\"hostname\":\"DBDEVSRV\",\"sid\":\"PRACTICE\",\"drivertype\":\"thin\",\"dbtype\":\"oracle\"}";
		// String mysqljson = "{\"query\":\"Select * from
		// ideaJDBC\",\"username\":\"hduser\",\"password\":\"Bitwise2012\",\"hostname\":\"10.130.248.53\",\"port\":\"3306\",\"database\":\"test\",\"dbtype\":\"mysql\"}";
		// String mysqljson = "{\"query\":\"Select * from
		// ideaJDBC\",\"username\":\"hduser\",\"password\":\"Bitwise2012\",\"hostname\":\"10.130.248.53\",\"database\":\"test\",\"dbtype\":\"mysql\"}";
		// String mysqljson =
		// "{\"table\":\"ideaJDBC\",\"username\":\"hduser\",\"password\":\"Bitwise2012\",\"hostname\":\"10.130.248.53\",\"database\":\"test\",\"dbtype\":\"mysql\"}";
		// String hivejson =
		// "{\"database\":\"textdata\",\"table\":\"personal\",\"username\":\"username\",\"password\":\"yourpassword\",\"dbtype\":\"hive\"}";

		PostMethod postMethod = new PostMethod("http://" + HOST_NAME + ":" + PORT + "/readFromMetastore");

		postMethod.addParameter("request_parameters", oraclejson);

		int response = httpClient.executeMethod(postMethod);
		InputStream inputStream = postMethod.getResponseBodyAsStream();

		byte[] buffer = new byte[1024 * 1024 * 5];
		String path = null;
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		System.out.println("Response of service: " + path);
		System.out.println("==================");
	}

	public void calltoFilterService() throws ClientProtocolException, IOException {

		HttpClient httpClient = new HttpClient();

		// String json =
		// "{\"condition\":\"abc\",\"schema\":[{\"fieldName\":\"f1\",\"dateFormat\":\"\",\"dataType\":\"1\",\"scale\":\"scale\",\"dataTypeValue\":\"java.lang.String\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f2\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.util.Date\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f3\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.util.Date\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f4\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.math.BigDecimal\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"}],\"fileSize\":1,\"jobDetails\":{\"host\":\"127.0.0.1\",\"port\":\"8005\",\"username\":\"hduser\",\"password\":\"Bitwise2012\",\"basepath\":\"C:/Users/santlalg/git/Hydrograph/hydrograph.engine/hydrograph.engine.command-line\",\"uniqueJobID\":\"debug_job\",\"componentID\":\"input\",\"componentSocketID\":\"out0\",\"isRemote\":false}}";
		String json = "{\"condition\":\"(f1 LIKE 'should')\",\"schema\":[{\"fieldName\":\"f1\",\"dateFormat\":\"\",\"dataType\":\"1\",\"scale\":\"scale\",\"dataTypeValue\":\"java.lang.String\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f2\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.util.Date\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f3\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.lang.Float\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"},{\"fieldName\":\"f4\",\"dateFormat\":\"\",\"dataType\":1,\"scale\":\"scale\",\"dataTypeValue\":\"java.lang.Double\",\"scaleType\":1,\"scaleTypeValue\":\"scaleTypeValue\",\"precision\":\"precision\",\"description\":\"description\"}],\"fileSize\":"
				+ FILE_SIZE_TO_READ + ",\"jobDetails\":{\"host\":\"" + HOST_NAME + "\",\"port\":\"" + PORT
				+ "\",\"username\":\"" + USER_ID + "\",\"password\":\"" + PASSWORD + "\",\"basepath\":\"" + BASE_PATH
				+ "\",\"uniqueJobID\":\"" + JOB_ID + "\",\"componentID\":\"" + COMPONENT_ID
				+ "\",\"componentSocketID\":\"" + SOCKET_ID + "\",\"isRemote\":false}}";

		PostMethod postMethod = new PostMethod("http://" + HOST_NAME + ":" + PORT + "/filter");

		postMethod.addParameter("request_parameters", json);

		int response = httpClient.executeMethod(postMethod);
		InputStream inputStream = postMethod.getResponseBodyAsStream();

		byte[] buffer = new byte[1024 * 1024 * 5];
		String path = null;
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		System.out.println("response of service: " + path);
	}

	public void calltoReadService() throws ClientProtocolException, IOException {

		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://" + HOST_NAME + ":" + PORT + "/read");
		postMethod.addParameter("jobId", JOB_ID);
		postMethod.addParameter("componentId", COMPONENT_ID);
		postMethod.addParameter("socketId", SOCKET_ID);
		postMethod.addParameter("basePath", BASE_PATH);
		postMethod.addParameter("userId", USER_ID);
		postMethod.addParameter("password", PASSWORD);
		postMethod.addParameter("file_size", FILE_SIZE_TO_READ);
		postMethod.addParameter("host_name", HOST_NAME);

		InputStream inputStream = postMethod.getResponseBodyAsStream();

		byte[] buffer = new byte[1024 * 1024 * 5];
		String path = null;
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		System.out.println("response of service: " + path);
	}

	public void calltoDeleteLocalDebugService() throws HttpException, IOException {

		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://" + HOST_NAME + ":" + PORT + "/deleteLocalDebugFile");
		postMethod.addParameter("jobId", JOB_ID);
		postMethod.addParameter("componentId", COMPONENT_ID);
		postMethod.addParameter("socketId", SOCKET_ID);

		java.util.Date date = new java.util.Date();
		System.out.println("+++ Start: " + new Timestamp(date.getTime()));

		int response = httpClient.executeMethod(postMethod);
		System.out.println("response: " + response);
	}

	public static void main(String[] args) {
		HydrographServiceClient client = new HydrographServiceClient();
		try {
			System.out.println("+++ Start: " + new Timestamp((new Date()).getTime()));
			// client.calltoReadService();
			// client.calltoFilterService();
			client.calltoReadMetastore();
			// client.chcekConnectionStatus();
			// client.calltoDeleteLocalDebugService();
			System.out.println("done:");
			System.out.println("+++ End: " + new Timestamp((new Date()).getTime()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
