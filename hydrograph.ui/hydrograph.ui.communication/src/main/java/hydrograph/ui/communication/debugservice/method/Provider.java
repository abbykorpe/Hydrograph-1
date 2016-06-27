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

package hydrograph.ui.communication.debugservice.method;

import java.net.MalformedURLException;
import java.net.URL;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.communication.debugservice.constants.DebugServiceMethods;
import hydrograph.ui.communication.debugservice.constants.DebugServicePostParameters;

import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 
 * This class provides post methods for Debug service
 * 
 * @author Bitwise
 *
 */
public class Provider {	
	private static String POST_PROTOCOL="http";
	public static Provider INSTANCE = new Provider();
	
	private Provider(){
		
	}

	/**
	 * 
	 * Get post method to delete csv debug file
	 * 
	 * @param jobDetails
	 * @return {@link PostMethod}
	 * @throws NumberFormatException
	 * @throws MalformedURLException
	 */
	public PostMethod getDeleteDebugFileMethod(JobDetails jobDetails) throws NumberFormatException, MalformedURLException {
		
		URL url = new URL(POST_PROTOCOL, jobDetails.getHost(), Integer.valueOf(jobDetails.getPort()), DebugServiceMethods.DELETE_DEBUG_CSV_FILE);
		
		PostMethod postMethod = new PostMethod(url.toString());
		postMethod.addParameter(DebugServicePostParameters.JOB_ID, jobDetails.getUniqueJobID());
		postMethod.addParameter(DebugServicePostParameters.COMPONENT_ID, jobDetails.getComponentID());
		postMethod.addParameter(DebugServicePostParameters.SOCKET_ID, jobDetails.getComponentSocketID());

		return postMethod;
	}

	/**
	 * 
	 * Get post method to get csv debug file
	 * 
	 * @param jobDetails
	 * @param fileSize
	 * @return {@link PostMethod}
	 * @throws NumberFormatException
	 * @throws MalformedURLException
	 */
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
	
	/**
	 * 
	 * Get post method to delete basepath debug files
	 * 
	 * @param host
	 * @param port
	 * @param unique job ID
	 * @param base path
	 * @param User
	 * @param password
	 * @return {@link PostMethod}
	 * @throws NumberFormatException
	 * @throws MalformedURLException
	 */
	public PostMethod getDeleteBasePathFileMethod(String host, String port, String uniqJobID, String basePath, String user, String password
			) throws NumberFormatException, MalformedURLException {
		
		URL url = new URL(POST_PROTOCOL, host, Integer.valueOf(port), DebugServiceMethods.DELETE_BASEPATH_FILES);
		
		PostMethod postMethod = new PostMethod(url.toString());
		postMethod.addParameter(DebugServicePostParameters.JOB_ID, uniqJobID);
		postMethod.addParameter(DebugServicePostParameters.BASE_PATH, basePath);
		postMethod.addParameter(DebugServicePostParameters.USER_ID, user);
		postMethod.addParameter(DebugServicePostParameters.PASSWORD, password);
		return postMethod;
	}
	
	public PostMethod getFilteredFileMethod(String jsonObject,JobDetails jobDetails) throws NumberFormatException, MalformedURLException {
		URL url = new URL(POST_PROTOCOL,jobDetails.getHost(),Integer.valueOf(jobDetails.getPort()), DebugServiceMethods.GET_FILTERED_FILE_PATH);
		PostMethod postMethod = new PostMethod(url.toString());
		postMethod.addParameter(DebugServicePostParameters.FILTER_JSON_OBJECT,jsonObject);
		return postMethod;
	}
}
