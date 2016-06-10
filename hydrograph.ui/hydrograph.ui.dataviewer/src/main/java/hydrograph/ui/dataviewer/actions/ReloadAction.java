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

package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.ReloadInformation;
import hydrograph.ui.dataviewer.constants.DebugServiceMethods;
import hydrograph.ui.dataviewer.constants.DebugServicePostParameters;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.preferances.ViewDataPreferences;
import hydrograph.ui.dataviewer.utilities.SCPUtility;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jface.action.Action;
import org.slf4j.Logger;

/**
 * 
 * ReloadAction reload the data from debug file based on file size and page size preferences from internal preference window
 * 
 * @author Bitwise
 * 
 */
public class ReloadAction extends Action {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ReloadAction.class);

	private ReloadInformation reloadInformation;
	private DebugDataViewer debugDataViewer;
	private ViewDataPreferences viewDataPreferences;
	
	private static final String LABEL = "Reload";
	private static final String LOCAL_HOST="localhost";

	public ReloadAction(DebugDataViewer debugDataViewer) {
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
	}

	private String getDebugFilePathFromDebugService() throws IOException {
		PostMethod postMethod = getDebugFilePostMethod();
		executePostMethod(postMethod);
		String path = getDebugFilePathFromPostResponse(postMethod).trim();
		return path;
	}

	private String getDebugFilePathFromPostResponse(PostMethod postMethod) throws IOException {
		String path = null;
		InputStream inputStream = postMethod.getResponseBodyAsStream();
		byte[] buffer = new byte[1024];
		while ((inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		return path;
	}

	private void executePostMethod(PostMethod postMethod) throws IOException, HttpException {
		HttpClient httpClient = new HttpClient();
		httpClient.executeMethod(postMethod);
	}

	private PostMethod getDebugFilePostMethod() {
		PostMethod postMethod = new PostMethod(getDebugServiceURL());
		postMethod.addParameter(DebugServicePostParameters.JOB_ID, reloadInformation.getUniqueJobID());
		postMethod.addParameter(DebugServicePostParameters.COMPONENT_ID, reloadInformation.getComponentID());
		postMethod.addParameter(DebugServicePostParameters.SOCKET_ID, reloadInformation.getComponentSocketID());
		postMethod.addParameter(DebugServicePostParameters.BASE_PATH, reloadInformation.getBasepath());
		postMethod.addParameter(DebugServicePostParameters.USER_ID, reloadInformation.getUsername());
		postMethod.addParameter(DebugServicePostParameters.PASSWORD, reloadInformation.getPassword());
		postMethod.addParameter(DebugServicePostParameters.FILE_SIZE, Integer.toString(viewDataPreferences.getFileSize()));
		postMethod.addParameter(DebugServicePostParameters.HOST_NAME, reloadInformation.getHost());
		return postMethod;
	}

	private String getDebugServiceURL() {
		return "http://" + reloadInformation.getHost() + ":" + reloadInformation.getPort()
				+ DebugServiceMethods.GET_DEBUG_FILE_PATH;
	}

	
	private void deleteCSVDebugDataFile() {
		try{
			HttpClient httpClient = new HttpClient();

			String host=reloadInformation.getHost();
			if(reloadInformation.getIsLocalJob()){
				host=LOCAL_HOST;
			}
						
			PostMethod postMethod = new PostMethod("http://" + host + ":" + reloadInformation.getPort() + DebugServiceMethods.DELETE_DEBUG_CSV_FILE);
			postMethod.addParameter(DebugServicePostParameters.JOB_ID, reloadInformation.getUniqueJobID());
			postMethod.addParameter(DebugServicePostParameters.COMPONENT_ID, reloadInformation.getComponentID());
			postMethod.addParameter(DebugServicePostParameters.SOCKET_ID, reloadInformation.getComponentSocketID());
			
			httpClient.executeMethod(postMethod);

		}catch(Exception e){
			logger.debug("Unable to delete debug csv file");
		}		
	}
	
	@Override
	public void run() {
		
		viewDataPreferences = debugDataViewer.getViewDataPreferences();
		
		closeExistingDebugFileConnection();		
		loadNewDebugFileInformation();

		String dataViewerFilePath = getLocalDataViewerFileLocation();

		String csvDebugFileLocation = getCSVDebugFileLocation();
		if(csvDebugFileLocation==null){
			return;
		}
		
		String csvDebugFileName = getCSVDebugFileName(csvDebugFileLocation);
		
		if (reloadInformation.getIsLocalJob()) {			
			String dataViewerFileAbsolutePath = getDataViewerFileAbsolutePath(dataViewerFilePath, csvDebugFileName);
			int returnCode=copyCSVDebugFileAtDataViewerDebugFileLocation(csvDebugFileLocation, dataViewerFileAbsolutePath);
			if(StatusConstants.ERROR == returnCode){
				return;
			}
		} else {
			int returnCode=scpRemoteCSVDebugFileToDataViewerDebugFileLocation(dataViewerFilePath, csvDebugFileLocation);
			if(StatusConstants.ERROR == returnCode){
				return;
			}
		}

		deleteCSVDebugDataFile();
		
		try {
			this.debugDataViewer.getCsvAdapter().reinitializeAdapter(viewDataPreferences.getPageSize());
		} catch (Exception e) {
			Utils.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_LOAD_DEBUG_FILE);
			this.debugDataViewer.getStatusManager().setStatus(new StatusMessage(StatusConstants.ERROR,Messages.UNABLE_TO_LOAD_DEBUG_FILE));
			updateDataViewerViews();
			this.debugDataViewer.getStatusManager().clearJumpToText();
			return;
		}

		this.debugDataViewer.getStatusManager().setStatus(new StatusMessage(StatusConstants.SUCCESS));
		this.debugDataViewer.getStatusManager().enableInitialPaginationContols();
		this.debugDataViewer.getStatusManager().clearJumpToText();
		updateDataViewerViews();

	}

	private void updateDataViewerViews() {
		this.debugDataViewer.getDataViewLoader().updateDataViewLists();
		this.debugDataViewer.getDataViewLoader().reloadloadViews();
	}

	private int scpRemoteCSVDebugFileToDataViewerDebugFileLocation(String dataViewerFilePath, String csvDebugFileLocation) {
		SCPUtility scpFrom = new SCPUtility();
		try {
			scpFrom.scpFileFromRemoteServer(reloadInformation.getHost(), reloadInformation.getUsername(),
					reloadInformation.getPassword(), csvDebugFileLocation.trim(), dataViewerFilePath);
		} catch (Exception e) {
			logger.debug("unable to copy Debug csv file to data viewer file location",e);
			Utils.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_LOAD_DEBUG_FILE);
			return StatusConstants.ERROR;
		}
		return StatusConstants.SUCCESS;		
	}

	private int copyCSVDebugFileAtDataViewerDebugFileLocation(String csvDebugFileLocation, String dataViewerFileAbsolutePath) {
		try {
			Files.copy(Paths.get(csvDebugFileLocation), Paths.get(dataViewerFileAbsolutePath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.debug("unable to copy Debug csv file to data viewer file location",e);
			Utils.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_LOAD_DEBUG_FILE);
			return StatusConstants.ERROR;
		}
		
		return StatusConstants.SUCCESS;
	}

	private String getDataViewerFileAbsolutePath(String dataViewerFilePath, String csvDebugFileName) {
		String dataViewerFile;
		if (OSValidator.isWindows()) {
			dataViewerFile = (dataViewerFilePath + "\\" + csvDebugFileName.trim() + ".csv").trim();
		} else {
			dataViewerFile = (dataViewerFilePath + "/" + csvDebugFileName.trim() + ".csv").trim();
		}
		return dataViewerFile;
	}

	private String getCSVDebugFileName(String csvDebugFileLocation) {
		String csvDebugFileName = csvDebugFileLocation.substring(csvDebugFileLocation.lastIndexOf("/") + 1, csvDebugFileLocation.length()).replace(".csv", "");
		return csvDebugFileName;
	}

	private String getCSVDebugFileLocation() {
		String csvDebugFileLocation = null;		
		try {
			csvDebugFileLocation = getDebugFilePathFromDebugService();
		} catch (IOException e) {
			Utils.showMessage(MessageBoxText.ERROR,Messages.UNABLE_TO_LOAD_DEBUG_FILE + e.getMessage());
			logger.debug("Failed to get csv debug file path from service", e);
		}
		return csvDebugFileLocation;
	}

	private String getLocalDataViewerFileLocation() {
		String debugFileLocation = Utils.getDebugPath().trim();
		if (OSValidator.isWindows()) {
			if (debugFileLocation.startsWith("/")) {
				debugFileLocation = debugFileLocation.replaceFirst("/", "").replace("/", "\\");
			}
		}
		return debugFileLocation;
	}

	private void loadNewDebugFileInformation() {
		reloadInformation = debugDataViewer.getReloadInformation();
	}

	private void closeExistingDebugFileConnection() {
		this.debugDataViewer.getCsvAdapter().dispose();
	}

}
