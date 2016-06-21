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

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.communication.utilities.SCPUtility;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.preferencepage.ViewDataPreferences;
import hydrograph.ui.dataviewer.utilities.DataViewerUtility;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import org.apache.commons.httpclient.HttpException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;

import com.jcraft.jsch.JSchException;

/**
 * 
 * ReloadAction reloads the data from debug file based on file size and page size preferences from internal preference window
 * 
 * @author Bitwise
 * 
 */
public class ReloadAction extends Action {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ReloadAction.class);

	private JobDetails jobDetails;
	private DebugDataViewer debugDataViewer;
	private ViewDataPreferences viewDataPreferences;	
	private static final String LABEL = "Reload";

	private Integer lastDownloadedFileSize;
	
	public ReloadAction(DebugDataViewer debugDataViewer) {
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
		lastDownloadedFileSize = Integer.valueOf(Utils.INSTANCE.getFileSize());
	}

	private String getDebugFilePathFromDebugService() throws IOException {
		return DebugServiceClient.INSTANCE.getDebugFile(jobDetails, Integer.toString(viewDataPreferences.getFileSize())).trim();
	}
	
	private void deleteCSVDebugDataFile() {		
		
		try {
			DebugServiceClient.INSTANCE.deleteDebugFile(jobDetails);
		} catch (NumberFormatException e) {
			logger.warn("Unable to delete debug csv file",e);
		} catch (HttpException e) {
			logger.warn("Unable to delete debug csv file",e);
		} catch (MalformedURLException e) {
			logger.warn("Unable to delete debug csv file",e);
		} catch (IOException e) {
			logger.warn("Unable to delete debug csv file",e);
		}
			
	}
	
	@Override
	public void run() {
		viewDataPreferences = debugDataViewer.getViewDataPreferences();
		DataViewerUtility.INSTANCE.resetSort(debugDataViewer);
		
		Job job = new Job(Messages.LOADING_DEBUG_FILE) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
				debugDataViewer.disbleDataViewerUIControls();
				
				if(lastDownloadedFileSize!=viewDataPreferences.getFileSize()){
					int returnCode=downloadDebugFile();	
					
					if(StatusConstants.ERROR == returnCode){
						return Status.CANCEL_STATUS;
					}			
				}
				
				try {
					closeExistingDebugFileConnection();
					if(lastDownloadedFileSize!=viewDataPreferences.getFileSize()){
						debugDataViewer.getDataViewerAdapter().reinitializeAdapter(viewDataPreferences.getPageSize(),true);	
					}else{
						debugDataViewer.getDataViewerAdapter().reinitializeAdapter(viewDataPreferences.getPageSize(),false);
					}
					
				} catch (ClassNotFoundException | SQLException e1) {
					Utils.INSTANCE.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_RELOAD_DEBUG_FILE);
					if(debugDataViewer.getDataViewerAdapter()!=null){
						debugDataViewer.getDataViewerAdapter().closeConnection();
					}
					debugDataViewer.close();
				}
				
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						debugDataViewer.getStatusManager().getStatusLineManager().getProgressMonitor().done();
						debugDataViewer.getActionFactory().enableAllActions(true);
						
						debugDataViewer.getStatusManager().setStatus(new StatusMessage(StatusConstants.SUCCESS));
						debugDataViewer.getStatusManager().enableInitialPaginationContols();
						debugDataViewer.getStatusManager().clearJumpToPageText();
						updateDataViewerViews();
						if(lastDownloadedFileSize!=viewDataPreferences.getFileSize()){
							debugDataViewer.submitRecordCountJob();
						}					
						lastDownloadedFileSize = viewDataPreferences.getFileSize();
						DataViewerUtility.INSTANCE.resetSort(debugDataViewer);
					}
				});
				
				return Status.OK_STATUS;
					
			}
		};
		
		job.schedule();
	}

	
	private int downloadDebugFile(){
		loadNewDebugFileInformation();

		String dataViewerFilePath = getLocalDataViewerFileLocation();

		String csvDebugFileLocation = getCSVDebugFileLocation();
		if(csvDebugFileLocation==null){
			logger.error("No debug file recieved from service");
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_LOAD_DEBUG_FILE);
			return StatusConstants.ERROR;
		}
		
		String csvDebugFileName = getCSVDebugFileName(csvDebugFileLocation);
		
		int returnCode;
		if (!jobDetails.isRemote()) {			
			String dataViewerFileAbsolutePath = getDataViewerFileAbsolutePath(dataViewerFilePath, csvDebugFileName);
			 returnCode=copyCSVDebugFileAtDataViewerDebugFileLocation(csvDebugFileLocation, dataViewerFileAbsolutePath);
			 deleteCSVDebugDataFile();
		} else {
			 returnCode=scpRemoteCSVDebugFileToDataViewerDebugFileLocation(dataViewerFilePath, csvDebugFileLocation);
			 deleteCSVDebugDataFile();
		}
		return returnCode;
	}
	
	private void updateDataViewerViews() {
		this.debugDataViewer.getDataViewLoader().updateDataViewLists();
		this.debugDataViewer.getDataViewLoader().reloadloadViews();
	}

	private int scpRemoteCSVDebugFileToDataViewerDebugFileLocation(String dataViewerFilePath, String csvDebugFileLocation) {
		try {
			SCPUtility.INSTANCE.scpFileFromRemoteServer(jobDetails.getHost(), jobDetails.getUsername(),
					jobDetails.getPassword(), csvDebugFileLocation.trim(), dataViewerFilePath);
		} catch (JSchException | IOException e) {
			logger.error("unable to copy Debug csv file to data viewer file location",e);
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_RELOAD_DEBUG_FILE);
			return StatusConstants.ERROR;
		}
		
		return StatusConstants.SUCCESS;		
	}

	private int copyCSVDebugFileAtDataViewerDebugFileLocation(String csvDebugFileLocation, String dataViewerFileAbsolutePath) {
		try {
			Files.copy(Paths.get(csvDebugFileLocation), Paths.get(dataViewerFileAbsolutePath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("unable to copy Debug csv file to data viewer file location",e);
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_RELOAD_DEBUG_FILE);
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
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR,Messages.UNABLE_TO_RELOAD_DEBUG_FILE + e.getMessage());
			logger.error("Failed to get csv debug file path from service", e);
		}
		return csvDebugFileLocation;
	}

	private String getLocalDataViewerFileLocation() {
		String debugFileLocation = Utils.INSTANCE.getDataViewerDebugFilePath().trim();
		if (OSValidator.isWindows()) {
			if (debugFileLocation.startsWith("/")) {
				debugFileLocation = debugFileLocation.replaceFirst("/", "").replace("/", "\\");
			}
		}
		return debugFileLocation;
	}

	private void loadNewDebugFileInformation() {
		jobDetails = debugDataViewer.getJobDetails();
	}

	private void closeExistingDebugFileConnection() {
		this.debugDataViewer.getDataViewerAdapter().closeConnection();
	}

}
