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

package hydrograph.ui.graph.handler;

import hydrograph.execution.tracking.server.websocket.StartServer;
import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

/**
 * Handler use to run the job using gradle command.
 * 
 * @author Bitwise
 * @version 1.0
 * @since 2015-10-27
 */
public class RunJobHandler{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(RunJobHandler.class);

	private static final String EXECUTION_TRACK_START = " hydrograph.execution.tracking.server.websocket.StartServer";
	public static final String EXECUTION_TRACK_SERVICE = "EXECUTION_TRACK_SERVICE";
	public static final String PROPERY_FILE_PATH = "/service/hydrograph-service.properties";
	
	private Job getJob(String localJobID, String consoleName, String canvasName) {
		return new Job(localJobID, consoleName, canvasName, null, null, null, null);
	}

	private DefaultGEFCanvas getComponentCanvas() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}
	
	/*
	 * 
	 * Execute command to run the job.
	 */ 
	public Object execute(RunConfigDialog runConfigDialog) {
				
		((ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()).getViewer().deselectAll();
		String consoleName = getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();
		String canvasName = consoleName;
		String localJobID = consoleName;
		
		startExecutionTrackingService() ;

		JobManager.INSTANCE.executeJob(getJob(localJobID, consoleName, canvasName), null,runConfigDialog);
		
		CanvasUtils.INSTANCE.getComponentCanvas().restoreMenuToolContextItemsState();		
		return null;
	}

	private void startExecutionTrackingService() {
		if (OSValidator.isWindows()) {
			try {
			String command = "java -cp " + getInstallationPathForExeTrack()
					+ EXECUTION_TRACK_START;
			ProcessBuilder builder = new ProcessBuilder(new String[] { "cmd",
					"/c", command });
				Runtime.getRuntime().exec(command);
				
				//builder.start();
				
			} catch (Exception e) {
				logger.info("Failed to start web socket server");
			}
		}
	}

	
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

	private String getInstallationPathForExeTrack() {
		String path = Platform.getInstallLocation().getURL().getPath();
		String executionTraJar = getExecutionTrackingServiceJar();
		if (StringUtils.isNotBlank(path) && StringUtils.startsWith(path, "/")
				&& OSValidator.isWindows()) {
			path = StringUtils.substring(path, 1);
		}
		return path + "config/service/" + executionTraJar;
	}

	
}
