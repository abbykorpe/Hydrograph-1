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
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.preferances.ViewDataPreferences;
import hydrograph.ui.dataviewer.utilities.ScpFrom;
import hydrograph.ui.dataviewer.utilities.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ReloadAction extends Action {
	private ReloadInformation reloadInformation;
	private String windowName;
	private DebugDataViewer debugDataViewer;
	ViewDataPreferences viewDataPreferences;

	private String fileSize;
	private int pageSize;

	private static final String LABEL="Reload";
	
	public ReloadAction(DebugDataViewer debugDataViewer) {
		super(LABEL);
		
		viewDataPreferences = debugDataViewer.getViewDataPreferences();
		this.debugDataViewer = debugDataViewer;
		

	}

	private void messageDialog(String message) {
		MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.APPLICATION_MODAL | SWT.OK);
		messageBox.setText("Alert");
		messageBox.setMessage(message);
		messageBox.open();
	}

	public String getDebugFile() throws IOException {

		
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://" + reloadInformation.getHost() + ":" + reloadInformation.getPort()
				+ "/read");
		postMethod.addParameter("jobId", reloadInformation.getUniqueJobID());
		postMethod.addParameter("componentId", reloadInformation.getComponentID());
		postMethod.addParameter("socketId", reloadInformation.getComponentSocketID());
		postMethod.addParameter("basePath", reloadInformation.getBasepath());
		postMethod.addParameter("userId", reloadInformation.getUsername());
		postMethod.addParameter("password", reloadInformation.getPassword());
		postMethod.addParameter("file_size", fileSize);
		postMethod.addParameter("host_name", reloadInformation.getHost());

		java.util.Date date = new java.util.Date();
		System.out.println("+++ Start: " + new Timestamp(date.getTime()));

		int response = httpClient.executeMethod(postMethod);
		System.out.println("response: " + response);
		InputStream inputStream = postMethod.getResponseBodyAsStream();

		byte[] buffer = new byte[1024 * 1024 * 5];
		String path = null;
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			path = new String(buffer);
		}
		System.out.println("response of service: " + path);
		return path;
	}

	@Override
	public void run() {
		
		this.debugDataViewer.getCsvAdapter().dispose();

		reloadInformation = debugDataViewer.getReloadInformation();
		fileSize = Utils.getFileSize();

		String tempCopyPath = Utils.getDebugPath();
		tempCopyPath = tempCopyPath.trim();

		if (OSValidator.isWindows()) {
			if (tempCopyPath.startsWith("/")) {
				tempCopyPath = tempCopyPath.replaceFirst("/", "").replace("/", "\\");
			}
		}

		String watchFile = null;
		String watchFileName = null;

		try {
			watchFile = getDebugFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (reloadInformation.getIsLocalJob()) {
			watchFileName = watchFile.substring(watchFile.lastIndexOf("/") + 1, watchFile.length()).replace(".csv", "");
			String destinationFile;
			if(OSValidator.isWindows()){
				destinationFile = (tempCopyPath + "\\" + watchFileName.trim() + ".csv").trim();
			}else{
				destinationFile = (tempCopyPath + "/" + watchFileName.trim() + ".csv").trim();
			}
			String sourceFile = watchFile.trim();

			try {
				Files.copy(Paths.get(sourceFile), Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			watchFileName = watchFile.substring(watchFile.lastIndexOf("/") + 1, watchFile.length()).replace(".csv", "");
			ScpFrom scpFrom = new ScpFrom();
			scpFrom.scpFileFromRemoteServer(reloadInformation.getHost(), reloadInformation.getUsername(),
					reloadInformation.getPassword(), watchFile.trim(), tempCopyPath);
		}
		
		try{
			this.debugDataViewer.getCsvAdapter().reinitializeAdapter(100);
		} catch (Exception e) {
			MessageBox messageBox = new MessageBox(new Shell());
			messageBox.setText("Error");
			messageBox.setMessage("Unable to load debug file - " + e.getMessage());
			messageBox.open();
		}
		
		this.debugDataViewer.getStatusManager().setStatus(new StatusMessage(StatusConstants.SUCCESS));
		this.debugDataViewer.getStatusManager().enableInitialPaginationContols();
		this.debugDataViewer.getDataViewLoader().reloadloadViews();
		
	}

	
	
}
