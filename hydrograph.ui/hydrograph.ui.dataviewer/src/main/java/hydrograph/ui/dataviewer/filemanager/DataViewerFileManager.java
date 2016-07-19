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
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.filter.FilterConditions;
import hydrograph.ui.dataviewer.filter.RemoteFilterJson;
import hydrograph.ui.dataviewer.utilities.DataViewerUtility;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.gson.Gson;
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
	private double debugFileSizeInKB;
	
	
	public DataViewerFileManager(JobDetails jobDetails) {
		super();
		this.jobDetails = jobDetails;
	}
	
	public DataViewerFileManager(){
	}

	
	/**
	 * 
	 * Download debug file in data viewer workspace
	 * @param filterApplied 
	 * @param filterConditions 
	 * 
	 * @return error code
	 */
	public StatusMessage downloadDataViewerFiles(boolean filterApplied, FilterConditions filterConditions,boolean isOverWritten){

		// Get csv debug file name and location 
		String csvDebugFileAbsolutePath = null;
		String csvDebugFileName = null;
		try {
			if (filterConditions != null) {
				if (!filterConditions.getRetainRemote()) {
					if (!filterApplied) {
						csvDebugFileAbsolutePath = getDebugFileAbsolutePath();
					} else {
						csvDebugFileAbsolutePath = getDebugFileAbsolutePath();
						csvDebugFileName = getFileName(csvDebugFileAbsolutePath);
						csvDebugFileAbsolutePath = getFilterFileAbsolutePath(filterConditions, csvDebugFileName);
					}
				} else {
					csvDebugFileAbsolutePath = getDebugFileAbsolutePath();
					csvDebugFileName = getFileName(csvDebugFileAbsolutePath);
					csvDebugFileAbsolutePath = getFilterFileAbsolutePath(filterConditions, csvDebugFileName);
				}
			}
			else
			{
				csvDebugFileAbsolutePath = getDebugFileAbsolutePath();
			}
		} catch (NumberFormatException | HttpException  | MalformedURLException e4) {
			logger.error("Unable to fetch debug file", e4);
			return new StatusMessage(StatusConstants.ERROR,Messages.UNABLE_TO_FETCH_DEBUG_FILE);
		}  catch (IOException e4) {
			logger.error("Unable to fetch debug file", e4);
			return new StatusMessage(StatusConstants.ERROR,Messages.UNABLE_TO_FETCH_DEBUG_FILE);
		}
		if (csvDebugFileAbsolutePath != null) {
			csvDebugFileName = csvDebugFileAbsolutePath
					.substring(csvDebugFileAbsolutePath.lastIndexOf("/") + 1, csvDebugFileAbsolutePath.length())
					.replace(DEBUG_DATA_FILE_EXTENTION, "").trim();
		}
				
		//Copy csv debug file to Data viewers temporary file location
		if (StringUtils.isNotBlank(csvDebugFileName)) {
			String dataViewerDebugFile = getDataViewerDebugFile(csvDebugFileName);
			
			try {
				if (!filterApplied) {
					copyCSVDebugFileToDataViewerStagingArea(jobDetails, csvDebugFileAbsolutePath, dataViewerDebugFile,isOverWritten);
				} else {
					copyFilteredFileToDataViewerStagingArea(jobDetails, csvDebugFileAbsolutePath, dataViewerDebugFile);
				}
			} catch (IOException | JSchException e1) {
				logger.error("Unable to fetch debug file", e1);
				return new StatusMessage(StatusConstants.ERROR, Messages.UNABLE_TO_FETCH_DEBUG_FILE);
			}
			
			File debugFile = new File(dataViewerDebugFile);
			double debugFileSizeInByte = (double) debugFile.length();
			double debugFileSizeKB =(debugFileSizeInByte / 1024);
		    debugFileSizeInKB = new BigDecimal(debugFileSizeKB).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			 
			 
			// Delete csv debug file after copy
			deleteFileOnRemote(jobDetails, csvDebugFileName);

			// Check for empty csv debug file
			if (isEmptyDebugCSVFile(dataViewerFilePath, dataViewerFileName)) {
				logger.error("Empty debug file");
				return new StatusMessage(StatusConstants.ERROR, Messages.EMPTY_DEBUG_FILE);
			}
		}
		return new StatusMessage(StatusConstants.SUCCESS);
	}

	private String getFileName(String csvDebugFileAbsolutePath) {
		
		if (StringUtils.isNotBlank(csvDebugFileAbsolutePath)) {
		String	debugFileName = csvDebugFileAbsolutePath
					.substring(csvDebugFileAbsolutePath.lastIndexOf("/") + 1, csvDebugFileAbsolutePath.length())
					.replace(DEBUG_DATA_FILE_EXTENTION, "").trim();
			if (debugFileName != null) {
				return getDataViewerDebugFile(debugFileName);
			}
		}
		return null;
	}

	private String getFilterFileAbsolutePath(FilterConditions filterConditions, String csvDebugFileName)
			throws HttpException, IOException {
		String csvDebugFileAbsolutePath;
		String filterJson = createJsonObjectForRemoteFilter(filterConditions, csvDebugFileName);
		csvDebugFileAbsolutePath = DebugServiceClient.INSTANCE.getFilteredFile(filterJson, jobDetails);
		return csvDebugFileAbsolutePath;
	}

	private String getDebugFileAbsolutePath() throws HttpException, MalformedURLException, IOException {
		String csvDebugFileAbsolutePath = DebugServiceClient.INSTANCE.getDebugFile(jobDetails,
				Utils.INSTANCE.getFileSize()).trim();
		return csvDebugFileAbsolutePath;
	}

	private String createJsonObjectForRemoteFilter(FilterConditions filterConditions, String csvDebugFileName) {
		Gson gson=new Gson();
		RemoteFilterJson remoteFilterJson=new RemoteFilterJson(filterConditions.getRemoteCondition(),
				DataViewerUtility.INSTANCE.getSchema(csvDebugFileName), 
				Integer.parseInt(Utils.INSTANCE.getFileSize().trim()),jobDetails);
		
		String filterJson=gson.toJson(remoteFilterJson);
		return filterJson;
	}
	
	public String getDataViewerFilePath() {
		return dataViewerFilePath;
	}

	public String getDataViewerFileName() {
		return dataViewerFileName;
	}

	public double getDebugFileSize() {
		return debugFileSizeInKB;
	}
	private boolean isEmptyDebugCSVFile(String dataViewerFilePath, final String dataViewerFileh) {
		File file = new File(dataViewerFilePath + dataViewerFileh + DEBUG_DATA_FILE_EXTENTION);
		if(file.length()==0){
			return true;
		}else{
			return false;
		}
	}
	
	private void copyCSVDebugFileToDataViewerStagingArea(JobDetails jobDetails, String csvDebugFileAbsolutePath, String dataViewerDebugFile,boolean isOverWritten) throws IOException, JSchException{
		if (!jobDetails.isRemote()) {
			String sourceFile = csvDebugFileAbsolutePath.trim();
			File file = new File(dataViewerDebugFile);
			if (!file.exists()|| isOverWritten) {
				Files.copy(Paths.get(sourceFile), Paths.get(dataViewerDebugFile), StandardCopyOption.REPLACE_EXISTING);
			}
		} else {
			File file = new File(dataViewerDebugFile);
			if (!file.exists() || isOverWritten) {				
				SCPUtility.INSTANCE.scpFileFromRemoteServer(jobDetails.getHost(), jobDetails.getUsername(), jobDetails.getPassword(),
						csvDebugFileAbsolutePath.trim(), getDataViewerDebugFilePath());
			}
		}
	}
	
	private void copyFilteredFileToDataViewerStagingArea(JobDetails jobDetails, String csvFilterFileAbsolutePath, String dataViewerDebugFile) throws IOException, JSchException{
		if (!jobDetails.isRemote()) {
			String sourceFile = csvFilterFileAbsolutePath.trim();
			File file = new File(dataViewerDebugFile);
				Files.copy(Paths.get(sourceFile), Paths.get(dataViewerDebugFile), StandardCopyOption.REPLACE_EXISTING);
		} else {
			File file = new File(dataViewerDebugFile);
				SCPUtility.INSTANCE.scpFileFromRemoteServer(jobDetails.getHost(), jobDetails.getUsername(), jobDetails.getPassword(),
						csvFilterFileAbsolutePath.trim(), getDataViewerDebugFilePath());
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

	private void deleteFileOnRemote(JobDetails details, String filterFileName) {
		dataViewerFilePath= getDataViewerDebugFilePath().trim();
		dataViewerFileName= filterFileName.trim();	
		try {
			DebugServiceClient.INSTANCE.deleteDebugFile(details);
		} catch (NumberFormatException | HttpException  | MalformedURLException e1) {
			logger.warn("Unable to delete debug file",e1);
		} catch (IOException e1) {
			logger.warn("Unable to delete debug file",e1);
		}
	}

}
