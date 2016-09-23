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

package hydrograph.server.execution.tracking.utils;

import hydrograph.server.execution.tracking.server.websocket.StartServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ExecutionTrackingUtils.
 */
public class ExecutionTrackingUtils {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(ExecutionTrackingUtils.class);
	
	/** The Constant PORT_NO. */
	private static final String PORT_NO = "EXECUTION_TRACKING_PORT";
	
	/** The Constant LOCAL_URL. */
	private static final String LOCAL_URL = "WEBSOCKET_LOCAL_HOST";
	
	/** The Constant TRACKING_ROUTE. */
	private static final String TRACKING_ROUTE = "WEBSOCKET_ROUTE";
	
	private static final String STATUS_FREQUENCY = "STATUS_FREQUENCY";
	
	private static final long defaultStatusFrequency = 2000;

	/** The route. */
	private String route = "/executionTracking/engine-client";
	
	/** The host. */
	private String host = "ws://localhost:";
	
	/** The port no. */
	private String portNo = "8877";
	
	private long statusFrequency = defaultStatusFrequency;
	
	private String PROPERTY_FILE = "socket-server.properties";
	
	/** The Constant INSTANCE. */
	public static final ExecutionTrackingUtils INSTANCE = new ExecutionTrackingUtils();

	/**
	 * Instantiates a new execution tracking utils.
	 */
	private ExecutionTrackingUtils() {
	}


	/**
	 * Gets the tracking url.
	 *
	 * @return the tracking url
	 */
	public String getTrackingUrl(String trackingClientSocketPort) {
		if(!StringUtils.isNotBlank(trackingClientSocketPort)){
			loadPropertyFile();
		}else{
			portNo=trackingClientSocketPort;
		}
		String url = host + portNo + route;
		
		logger.info("Hydrograph Execution Tracking URL: " + url);
		return url;
	}

	/**
	 * Gets the port no.
	 *
	 * @return the port no
	 */
	public String getPortNo() {
		return portNo;
	}

	
	public long getStatusFrequency() {
		return statusFrequency;
	}
	
	/**
	 * This function will load property file to initialize the parameters values.
	 *
	 */
	public void loadPropertyFile(){
		try {
			Properties properties = new Properties();
			FileInputStream stream = getExternalPropertyFilePath();
			if(stream != null){
				properties.load(stream);
			}else {
				InputStream inputStream = this.getClass().getResourceAsStream(getInternalPropertyFilePath());
				properties.load(inputStream);
			}
			
			if (!properties.isEmpty()) {
				portNo = properties.getProperty(PORT_NO);
				host = properties.getProperty(LOCAL_URL);
				route = properties.getProperty(TRACKING_ROUTE);
				String frequency = properties.getProperty(STATUS_FREQUENCY);
				statusFrequency = Long.parseLong(frequency);
			}
		} catch (IOException exception) {
			logger.error("Failed to load properties file", exception);
		}
	}
	
	private String getInternalPropertyFilePath(){
		 	String filePath = "/" + PROPERTY_FILE;
		return filePath;
	}
	
	private FileInputStream getExternalPropertyFilePath(){
		int index = 0;
		String dirPath = "";
		FileInputStream fileInputStream = null;
		try {
			Path path = Paths.get(StartServer.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			String filePath = path.toString();
			if (OSValidator.isWindows()) {
				index = filePath.lastIndexOf("\\");
				dirPath = filePath.substring(0, index) + "\\" + PROPERTY_FILE;
			} else if (OSValidator.isMac()) {
				index = filePath.lastIndexOf("/");
				dirPath = filePath.substring(0, index) + "/" + PROPERTY_FILE;
			} else if (OSValidator.isUnix()) {
				index = filePath.lastIndexOf("/");
				dirPath = filePath.substring(0, index) + "/" + PROPERTY_FILE;
			}
			File file = new File(dirPath);
			if (file.exists()) {
				fileInputStream = new FileInputStream(file);
			}
		} catch (URISyntaxException exception) {
			logger.error("File Path does not exist:"+dirPath, exception);
		} catch (FileNotFoundException exception) {
			logger.error("File not found: "+dirPath, exception);
		}
		return fileInputStream;
	}
}
