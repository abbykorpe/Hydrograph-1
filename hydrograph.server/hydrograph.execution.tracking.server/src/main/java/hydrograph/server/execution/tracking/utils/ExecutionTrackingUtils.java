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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.log4j.Logger;

import hydrograph.server.execution.tracking.server.websocket.StartServer;

/**
 * The Class ExecutionTrackingUtils.
 */
public class ExecutionTrackingUtils {

	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(ExecutionTrackingUtils.class);
	
	/** The Constant PORT_NO. */
	private static final String PORT_NO = "EXECUTION_TRACKING_PORT";
	
	/** The Constant LOCAL_URL. */
	private static final String LOCAL_URL = "WEBSOCKET_LOCAL_HOST";
	
	/** The Constant TRACKING_ROUTE. */
	private static final String TRACKING_ROUTE = "WEBSOCKET_ROUTE";
	
	private static final String STATUS_FREQUENCY = "STATUS_FREQUENCY";

	/** The route. */
	private String route = "/executionTracking/engine-client";
	
	/** The host. */
	private String host = "ws://localhost:";
	
	/** The port no. */
	private String portNo = "8877";
	
	private long statusFrequency = 2000;
	
	/** The Constant INSTANCE. */
	public static final ExecutionTrackingUtils INSTANCE = new ExecutionTrackingUtils();

	/**
	 * Instantiates a new execution tracking utils.
	 */
	private ExecutionTrackingUtils() {
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		int index = 0;
		File file = null;
		String dirPath = "";
		try {
			Path path = Paths.get(StartServer.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			String filePath = path.toString();
			if (OSValidator.isWindows()) {
				index = filePath.lastIndexOf("\\");
				dirPath = filePath.substring(0, index) + "\\socket-server.properties";
			} else if (OSValidator.isMac()) {
				index = filePath.lastIndexOf("/");
				dirPath = filePath.substring(0, index) + "/socket-server.properties";
			} else if (OSValidator.isUnix()) {
				index = filePath.lastIndexOf("/");
				dirPath = filePath.substring(0, index) + "/socket-server.properties";
			}

			logger.debug("Server config file Path - " + filePath);
			Properties properties = new Properties();
			file = new File(dirPath);
			if (file.exists()) {
				FileInputStream fileInputStream = new FileInputStream(file);
				properties.load(fileInputStream);
			}
			if (!properties.isEmpty()) {
				portNo = properties.getProperty(PORT_NO);
				host = properties.getProperty(LOCAL_URL);
				route = properties.getProperty(TRACKING_ROUTE);
				statusFrequency = Long.parseLong(properties.getProperty(STATUS_FREQUENCY));
			}

			return properties.getProperty("path");
		} catch (FileNotFoundException exception) {
			logger.error("File not found:" + exception);
		} catch (IOException exception) {
			logger.error("IOException: " + exception);
		} catch (URISyntaxException exception) {
			logger.error("URISyntaxException: " + exception);
		}
		return "";
	}

	/**
	 * Gets the tracking url.
	 *
	 * @return the tracking url
	 */
	public String getTrackingUrl() {
		String url = host + portNo + route;
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
	
	
}
