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

package hydrograph.ui.graph.execution.tracking.utils;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.preferences.ExecutionPreferenceConstants;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.model.ComponentExecutionStatus;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

/**
 * The Class TrackingDisplayUtils.
 */
public class TrackingDisplayUtils {

	/** The instance. */
	public static TrackingDisplayUtils INSTANCE = new TrackingDisplayUtils();

	/** The logger. */
	private static Logger logger = LogFactory.INSTANCE.getLogger(TrackingDisplayUtils.class);
	
	/** The Constant PORT_NUMBER. */
	private static final String PORT_NUMBER = "EXECUTION_TRACKING_PORT";
	
	/** The Constant LOCAL_URL. */
	private static final String LOCAL_URL = "WEBSOCKET_LOCAL_HOST";
	
	/** The Constant WEBSOCKET_ROUTE. */
	private static final String WEBSOCKET_ROUTE = "WEBSOCKET_UI_ROUTE";

	/** The Constant EXECUTION_TRACK_START. */
	private static final String EXECUTION_TRACK_START = " hydrograph.execution.tracking.server.websocket.StartServer";
	
	/** The Constant EXECUTION_TRACK_SERVICE. */
	private static final String EXECUTION_TRACK_SERVICE = "EXECUTION_TRACK_SERVICE_JAR";
	
	/** The Constant EXECUTION_TRACKING_PORT. */
	private static final String EXECUTION_TRACKING_PORT = "EXECUTION_TRACKING_PORT";
	
	/** The Constant PROPERY_FILE_PATH. */
	private static final String PROPERY_FILE_PATH = "/service/hydrograph-service.properties";
	
	/** The Constant EXECUTION_TRACKING_PROPERY_FILE. */
	private static final String EXECUTION_TRACKING_PROPERY_FILE = "/service/socket-server.properties";

	/** The local host. */
	private String localHost;

	/** The websocket route. */
	private String websocketRoute;
	private static final String WEB_SOCKET = "ws://";
	private static final String COLON = ":";
	/**
	 * Instantiates a new tracking display utils.
	 */
	private TrackingDisplayUtils() {
	}

	/**
	 * Clears the status of all components. Also Initiates record count to 0.
	 */
	public void clearTrackingStatus() {

		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI
				.getWorkbench().getWorkbenchWindows()[0].getActivePage()
				.getActiveEditor();
		if (editor != null && editor instanceof ELTGraphicalEditor) {
			clearTrackingStatusForEditor(editor);
		}
	}

	/**
	 * Clear tracking status.
	 * 
	 * @param jobId
	 *            the job id
	 */
	public void clearTrackingStatus(String jobId) {

		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI
				.getWorkbench().getWorkbenchWindows()[0].getActivePage()
				.getActiveEditor();
		if (editor != null && editor instanceof ELTGraphicalEditor
				&& (editor.getJobId().equals(jobId))) {
			clearTrackingStatusForEditor(editor);
		}
	}

	/**
	 * Clear tracking status for editor.
	 * 
	 * @param editor
	 *            the editor
	 */
	private void clearTrackingStatusForEditor(ELTGraphicalEditor editor) {
		GraphicalViewer graphicalViewer = (GraphicalViewer) ((GraphicalEditor) editor)
				.getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry()
				.values().iterator(); ite.hasNext();) {
			EditPart editPart = (EditPart) ite.next();
			if (editPart instanceof ComponentEditPart) {
				Component component = ((ComponentEditPart) editPart)
						.getCastedModel();
				component.updateStatus(ComponentExecutionStatus.BLANK.value());
			} else if (editPart instanceof LinkEditPart) {
				((LinkEditPart) editPart).clearRecordCountAtPort();
			}
		}
	}

	/**
	 * Gets the executiontracking port no.
	 * 
	 * @return the executiontracking port no
	 */
	public String getExecutiontrackingPortNo() {
		String portNumber = null;
		try {
			FileReader fileReader = new FileReader(
					XMLConfigUtil.CONFIG_FILES_PATH + EXECUTION_TRACKING_PROPERY_FILE);
			Properties properties = new Properties();
			properties.load(fileReader);
			if (StringUtils.isNotBlank(properties.getProperty(PORT_NUMBER))) {
				portNumber = properties.getProperty(PORT_NUMBER);
				localHost = properties.getProperty(LOCAL_URL);
				websocketRoute = properties.getProperty(WEBSOCKET_ROUTE);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return portNumber;
	}

	/**
	 * Gets the web socket remote url.
	 * 
	 * @return the web socket remote url
	 */
	public String getWebSocketRemoteUrl(Job job) {
		String portNo = getPortFromPreference();
		String remoteUrl = WEB_SOCKET + job.getHost() + COLON + portNo + websocketRoute;
		return remoteUrl;
	}

	/**
	 * Gets the web socket local host.
	 * 
	 * @return the web socket local host
	 */
	public String getWebSocketLocalHost() {
		String portNo = getPortFromPreference();
		String localUrl = localHost + portNo + websocketRoute;
		return localUrl;

	}

	/**
	 * Gets the port from preference.
	 * 
	 * @return the port from preference
	 */
	public String getPortFromPreference() {
		String portNo = Platform.getPreferencesService().getString(
				Activator.PLUGIN_ID,
				ExecutionPreferenceConstants.TRACKING_PORT_NO,
				getExecutiontrackingPortNo(), null);

		return portNo;
	}

	/**
	 * Start execution tracking service.
	 */
	public void startExecutionTrackingService() {
		if (OSValidator.isWindows()) {
			try {
				String command = "java -cp " + getInstallationPathForExeTrack()
						+ EXECUTION_TRACK_START;
				ProcessBuilder builder = new ProcessBuilder(new String[] {
						"cmd", "/c", command });

				builder.start();

			} catch (Exception e) {
				logger.info("Failed to start web socket server");
			}
		}
	}

	/**
	 * Re start execution tracking service.
	 * 
	 * @param pid
	 *            the pid
	 */
	public void reStartExecutionTrackingService(String pid) {
		if (OSValidator.isWindows()) {
			try {
				logger.info("Taskkill /PID :" + pid);
				String command = "Taskkill /PID " + pid + " /F";
				ProcessBuilder builder = new ProcessBuilder(new String[] {
						"cmd", "/c", command });
				builder.start();
				startExecutionTrackingService();
			} catch (Exception e) {
				logger.info("Failed to start web socket server");
			}
		}
	}

	/**
	 * Gets the execution tracking service jar.
	 * 
	 * @return the execution tracking service jar
	 */
	private String getExecutionTrackingServiceJar() {
		String exeTrackServiceJar = null;
		try {
			FileReader fileReader = new FileReader(
					XMLConfigUtil.CONFIG_FILES_PATH + PROPERY_FILE_PATH);
			Properties properties = new Properties();
			properties.load(fileReader);
			if (StringUtils.isNotBlank(properties
					.getProperty(EXECUTION_TRACK_SERVICE))) {
				exeTrackServiceJar = properties
						.getProperty(EXECUTION_TRACK_SERVICE);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return exeTrackServiceJar;
	}

	/**
	 * Gets the installation path for exe track.
	 * 
	 * @return the installation path for exe track
	 */
	private String getInstallationPathForExeTrack() {
		String path = Platform.getInstallLocation().getURL().getPath();
		String executionTraJar = getExecutionTrackingServiceJar();
		if (StringUtils.isNotBlank(path) && StringUtils.startsWith(path, "/")
				&& OSValidator.isWindows()) {
			path = StringUtils.substring(path, 1);
		}
		return path + "config/service/" + executionTraJar;
	}

	/**
	 * This function used to return Rest Service port Number which running on
	 * local.
	 * 
	 * @return the string
	 */
	public static String restServicePort() {
		String portNumber = null;
		try {
			FileReader fileReader = new FileReader(
					XMLConfigUtil.CONFIG_FILES_PATH + PROPERY_FILE_PATH);
			Properties properties = new Properties();
			properties.load(fileReader);
			if (StringUtils.isNotBlank(properties
					.getProperty(EXECUTION_TRACK_SERVICE))) {
				portNumber = properties.getProperty(EXECUTION_TRACKING_PORT);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return portNumber;
	}

	/**
	 * This function will be return process ID which running on defined port.
	 * 
	 * @return the service port pid
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String getServicePortPID() throws IOException {
		int portNumber = Integer.parseInt(restServicePort());
		if (OSValidator.isWindows()) {
			ProcessBuilder builder = new ProcessBuilder(new String[] { "cmd",
					"/c", "netstat -a -o -n |findstr :" + portNumber });
			Process process = builder.start();
			InputStream inputStream = process.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String str = bufferedReader.readLine();
			str = StringUtils.substringAfter(str, "LISTENING");
			str = StringUtils.trim(str);
			return str;
		}
		return "";
	}

	/**
	 * 
	 * Return tools Installation path
	 * 
	 * @return {@link String}
	 */
	public String getInstallationPath() {
		String installationPath = XMLConfigUtil.CONFIG_FILES_PATH + "/logger/JobTrackingLog";
		if (OSValidator.isWindows()) {
			if (installationPath.startsWith("/")) {
				installationPath = installationPath.replaceFirst("/", "").replace("/", "\\");
			}
		}
		return installationPath;

	}
	
	/**
	 * 
	 * Close websocket client connection.
	 * @param session
	 */
	public void closeWebSocketConnection(Session session){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
		}
		if (session != null  && session.isOpen()) {
			try {
				CloseReason closeReason = new CloseReason(CloseCodes.NORMAL_CLOSURE,"Closed");
				session.close(closeReason);
				logger.info("Session closed");
			} catch (IOException e) {
				logger.error("Fail to close connection ",e);
			}
			
		}

	}
}
