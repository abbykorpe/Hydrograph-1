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

package hydrograph.ui.graph.execution.tracking.connection;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.graph.debugconverter.DebugHelper;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.graph.handler.StopJobHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.graph.utility.JobScpAndProcessUtility;
import hydrograph.ui.joblogger.JobLogger;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import javax.websocket.Session;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;

import com.google.gson.Gson;

public class HydrographServerConnection {

	private static Logger logger = LogFactory.INSTANCE.getLogger(HydrographServerConnection.class);
	private static final String JOB_KILLED_SUCCESSFULLY = "JOB KILLED SUCCESSFULLY";
	int counter =0;
	private int selection;
	
	
	/**
	 * Instantiates HydrographUiClientSocket and establishes connection with server in order to get execution status.
	 * @param job
	 * @param jobID
	 * @param url
	 * @return Session 
	 */
	public Session connectToLocalServer(final Job job, String jobID, String url) {
		Session session=null;
		try {
			counter++;
			HydrographUiClientSocket socket = new HydrographUiClientSocket();
			ClientManager clientManager = ClientManager.createClient();
			session =clientManager.connectToServer(socket, new URI(url));
			socket.sendMessage(getJson(jobID));
			return session;
 
		} catch (Throwable t) {
			
			return reConnectToServer(job, jobID, url);
		}
	}
	
	/**
	 * Instantiates HydrographUiClientSocket and establishes connection with server in order to get execution status.
	 * @param job
	 * @param jobID
	 * @param url
	 * @return Session 
	 */
	public Session connectToServer(final Job job, String jobID, String url) {
		Session session=null;
		try {
			HydrographUiClientSocket socket = new HydrographUiClientSocket();
			ClientManager clientManager = ClientManager.createClient();
			session =clientManager.connectToServer(socket, new URI(url));
			socket.sendMessage(getJson(jobID));
			return session;
 
		} catch (Throwable t) {
			
			Display.getDefault().asyncExec(new Runnable() {			
				@Override
				public void run() {
					messageDialogForExecutionTracking(job,Display.getDefault().getActiveShell());
					
				}});
				logger.error("Error while connection to server");
		}
		return session;
	}


	public Session reConnectToServer(final Job job, String jobID, String url) {
			Session session=null;
			String portId="";
			try {
				portId = DebugHelper.INSTANCE.getServicePortPID(Integer.parseInt(TrackingDisplayUtils.INSTANCE.restServicePort()));
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
			TrackingDisplayUtils.INSTANCE.reStartExecutionTrackingService(portId) ;
			session=connectToServer(job,jobID,url);
			if(session==null){
			if(counter>2){
			Display.getDefault().asyncExec(new Runnable() {			
			@Override
			public void run() {
				messageDialogForExecutionTracking(job,Display.getDefault().getActiveShell());
				
			}});
			logger.error("Error while connection to server");
			}
		}
			return session;
	}

	
	/**
	 * Instantiates HydrographUiClientSocket and establishes connection with server in order to kill the job.
	 * @param jobID
	 * @param url
	 * @return Session
	 * @throws Throwable
	 */
	public Session connectToKillJob(String jobID,String url) throws Throwable {
		
		try {

			HydrographUiClientSocket socket = new HydrographUiClientSocket();
			ClientManager clientManager = ClientManager.createClient();
			Session session =clientManager.connectToServer(socket, new URI(url));
			socket.sendMessage(getKillReq(jobID));
			Thread.sleep(3000);
			return session;
 
		} catch (Throwable t) {
			throw t; 
		}
	}
	
	private String getJson(String jobID){
		ExecutionStatus executionStatus = new ExecutionStatus(Collections.EMPTY_LIST);
		executionStatus.setJobId(jobID);
		executionStatus.setType("get");
		Gson gson = new Gson();
		return gson.toJson(executionStatus);
	}
	private String getKillReq(String jobID){
		ExecutionStatus executionStatus = new ExecutionStatus(Collections.EMPTY_LIST);
		executionStatus.setJobId(jobID);
		executionStatus.setType("kill");
		Gson gson = new Gson();
		return gson.toJson(executionStatus);
	}
	
	
	/**
	 * Opens a message dialog in case if there are issues while making connection to server.
	 * @param job
	 */
	public void messageDialogForExecutionTracking(Job job,Shell shell){
		String portNo = TrackingDisplayUtils.INSTANCE.getPortFromPreference();
		String msg = "Execution tracking can't be displayed as connection refused on host: "+job.getHost()+" with port no: "+portNo;
		MessageDialog dialog = new MessageDialog(shell, "Warning", null, msg, SWT.ICON_WARNING, new String[]{"Continue", "Kill Job"}, 0);
		selection = dialog.open();
		switch(selection){
		case 0:
			break;
		case 1:
			((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
			DefaultGEFCanvas gefCanvas = CanvasUtils.INSTANCE.getComponentCanvas();
			JobLogger joblogger = JobManager.INSTANCE.initJobLogger(gefCanvas);
			joblogger.logMessage(JOB_KILLED_SUCCESSFULLY);
			JobScpAndProcessUtility.INSTANCE.killJobProcess(job);
			logger.debug("Job Killed");
			break;
		}
	}
	
	/**
	 * @return the choice either continue / kill job from Execution tracking dialog.
	 */
	public int getSelection(){
		return selection;
	}
}
