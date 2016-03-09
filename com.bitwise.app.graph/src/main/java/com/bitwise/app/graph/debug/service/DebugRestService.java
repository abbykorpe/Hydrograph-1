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

import com.bitwise.app.logging.factory.LogFactory;

/**
 * @author Bitwise
 *
 */
public class DebugRestService {

	Logger logger = LogFactory.INSTANCE.getLogger(DebugRestService.class);
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
	public DebugRestService(String IPAddress,String basePath, String jobId, String componentId, String socketId, String userId, String password) {
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
		String ip = "http://"+ipAddress+":8001/debug";
		 
		PostMethod postMethod = new PostMethod(ip);//"http://10.130.248.53:4567/debug"
		postMethod.addParameter("userID", userId);
		postMethod.addParameter("password", password);
		postMethod.addParameter("basePath", basePath);
		postMethod.addParameter("jobId", jobId);
		postMethod.addParameter("componentId", componentId);
		postMethod.addParameter("socketId", socketId);

		 
		int status = httpClient.executeMethod(postMethod);
	 
		InputStream inputStream = postMethod.getResponseBodyAsStream();
		JSONArray jsonArray = null;
		 
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		System.out.println("Response:"+postMethod.getResponseBodyAsStream());
		 String line = "";
		   while ((line = br.readLine()) != null) {
			   
			   jsonArray =  new JSONArray(line);
		   }

		   return jsonArray;
	}

	public static void main(String[] args) throws HttpException, IOException, JSONException {
		///AcceleroDebug3/debug/Test_Debug_Remote_Job_Job_1_2066724424_1457430351703/IFDelimited_01_out0
		///debug01/debug/TestDebugMarch_Job_1_1565981287_1457449202907/IFDelimited_01_out0
		System.out.println("before service call");
		DebugRestService debugRestService = new DebugRestService("10.130.248.53", "/debug01", "TestDebugMarch_Job_1_1565981287_1457449202907", "IFDelimited_01", 
				"out0", "hduser", "Bitwise2012");
		debugRestService.callRestService();
		System.out.println("after service call");
	}
}
