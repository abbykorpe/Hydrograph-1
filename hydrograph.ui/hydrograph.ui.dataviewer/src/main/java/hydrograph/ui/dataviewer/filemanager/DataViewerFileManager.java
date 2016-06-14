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
package hydrograph.ui.dataviewer.filemanager;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.communication.utilities.SCPUtility;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;

import com.jcraft.jsch.JSchException;

/**
 * 
 * DataViewerFileManager is responsible to download debug file in data viewer workspace
 * 
 * @author Bitwise
 *
 */
public class DataViewerFileManager {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(DebugDataViewer.class);
	
	private JobDetails jobDetails;
	private static final String DEBUG_DATA_FILE_EXTENTION=".csv";

	private String dataViewerFilePath;
	private String dataViewerFileName;
	
	public DataViewerFileManager(JobDetails jobDetails) {
		super();
		this.jobDetails = jobDetails;
	}
	
	/**
	 * 
	 * Donload debug file in data viewer wrokspace
	 * 
	 * @return error code
	 */
	public int downloadDataViewerFiles(){
		// Get csv debug file name and location 
		String csvDebugFileAbsolutePath = null;
		String csvDebugFileName = null;
		
		try {
			csvDebugFileAbsolutePath = DebugServiceClient.INSTANCE.getDebugFile(jobDetails, Utils.INSTANCE.getFileSize()).trim();
		} catch (NumberFormatException | HttpException  | MalformedURLException e4) {
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_FETCH_DEBUG_FILE);
			logger.error("Unable to fetch debug file", e4);
			return StatusConstants.ERROR;
		}  catch (IOException e4) {
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_FETCH_DEBUG_FILE);
			logger.error("Unable to fetch debug file", e4);
			return StatusConstants.ERROR;
		}
		
		csvDebugFileName = csvDebugFileAbsolutePath.substring(csvDebugFileAbsolutePath.lastIndexOf("/") + 1,
				csvDebugFileAbsolutePath.length()).replace(DEBUG_DATA_FILE_EXTENTION, "").trim();
				
		//Copy csv debug file to Data viewers temporary file location
		String dataViewerDebugFile = getDataViewerDebugFile(csvDebugFileName);		
		try {
			copyCSVDebugFileToDataViewerStagingArea(jobDetails, csvDebugFileAbsolutePath, dataViewerDebugFile);
		} catch (IOException | JSchException e1) {
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR, Messages.UNABLE_TO_FETCH_DEBUG_FILE);
			logger.error("Unable to fetch debug file", e1);
			return StatusConstants.ERROR;
		}
				
		//Delete csv debug file after copy
		dataViewerFilePath= getDataViewerDebugFilePath().trim();
		dataViewerFileName= csvDebugFileName.trim();	
		try {
			DebugServiceClient.INSTANCE.deleteDebugFile(jobDetails);
		} catch (NumberFormatException | HttpException  | MalformedURLException e1) {
			logger.warn("Unable to delete debug file",e1);
		} catch (IOException e1) {
			logger.warn("Unable to delete debug file",e1);
		}
		
		//Check for empty csv debug file
		if(isEmptyDebugCSVFile(dataViewerFilePath, dataViewerFileName)){
			Utils.INSTANCE.showMessage(MessageBoxText.ERROR,Messages.UNABLE_TO_READ_DEBUG_FILE);
			logger.error("Empty debug file");
		    return StatusConstants.ERROR;
		}
		
		return StatusConstants.SUCCESS;
	}
	
	public String getDataViewerFilePath() {
		return dataViewerFilePath;
	}

	public String getDataViewerFileName() {
		return dataViewerFileName;
	}

	private boolean isEmptyDebugCSVFile(String dataViewerFilePath, final String dataViewerFileh) {
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(dataViewerFilePath + dataViewerFileh + DEBUG_DATA_FILE_EXTENTION))) {
			if (bufferedReader.readLine() == null) {
			    return true;
			}
		} catch (Exception e1) {
			logger.error("Unable to read debug file",e1);
			return true;
		}
		return false;
	}
	
	private void copyCSVDebugFileToDataViewerStagingArea(JobDetails jobDetails, String csvDebugFileAbsolutePath, String dataViewerDebugFile) throws IOException, JSchException{
		if (!jobDetails.isRemote()) {
			String sourceFile = csvDebugFileAbsolutePath.trim();
			File file = new File(dataViewerDebugFile);
			if (!file.exists()) {
				Files.copy(Paths.get(sourceFile), Paths.get(dataViewerDebugFile), StandardCopyOption.REPLACE_EXISTING);
			}
		} else {
			File file = new File(dataViewerDebugFile);
			if (!file.exists()) {				
				SCPUtility.INSTANCE.scpFileFromRemoteServer(jobDetails.getHost(), jobDetails.getUsername(), jobDetails.getPassword(),
						csvDebugFileAbsolutePath.trim(), getDataViewerDebugFilePath());
			}
		}
	}

	private String getDataViewerDebugFile(String csvDebugFileName) {
		String dataViewerDebugFile = getDataViewerDebugFilePath();
		if (OSValidator.isWindows()) {
			dataViewerDebugFile = (dataViewerDebugFile + "\\" + csvDebugFileName.trim() + DEBUG_DATA_FILE_EXTENTION).trim();
		} else {
			dataViewerDebugFile = (dataViewerDebugFile + "/" + csvDebugFileName.trim() + DEBUG_DATA_FILE_EXTENTION).trim();
		}
		return dataViewerDebugFile;
	}
	
	private String getDataViewerDebugFilePath() {
		String dataViewerDebugFilePath = Utils.INSTANCE.getDataViewerDebugFilePath();
		dataViewerDebugFilePath=dataViewerDebugFilePath.trim();
				
		if(OSValidator.isWindows()){
			if(dataViewerDebugFilePath.startsWith("/")){
				dataViewerDebugFilePath = dataViewerDebugFilePath.replaceFirst("/", "").replace("/", "\\");
			}		
			dataViewerDebugFilePath = dataViewerDebugFilePath + "\\";
		}else{
			dataViewerDebugFilePath = dataViewerDebugFilePath + "/";
		}
		
		return dataViewerDebugFilePath;
	}
}
