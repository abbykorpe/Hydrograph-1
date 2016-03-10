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
		String ip = Constants.HTTP_PROTOCOL+ipAddress+Constants.PORT_NO;
		 
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
