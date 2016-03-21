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

 
package com.bitwise.app.graph.action.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.LinkEditPart;
import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.debug.service.DebugDataWizard;
import com.bitwise.app.graph.debug.service.DebugRestClient;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.handler.DebugHandler;
import com.bitwise.app.graph.job.Job;
import com.bitwise.app.graph.job.JobManager;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.debug.api.DebugDataReader;
 
/**
 * @author Bitwise
 *
 */
public class WatchRecordAction extends SelectionAction {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(WatchRecordAction.class);
	private boolean isWatcher;
	WatchRecordInner watchRecordInner = new WatchRecordInner();
	 
	
	public WatchRecordAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected boolean calculateEnabled() {
		if(createWatchCommand(getSelectedObjects())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void init() {
		super.init();
		setText(Messages.WATCH_RECORD_TEXT);
		setId(Constants.WATCH_RECORD_ID);
		setEnabled(true);
	}
	
	private boolean createWatchCommand(List<Object> selectedObjects)  {
		for(Object obj:selectedObjects){
			if(obj instanceof LinkEditPart)	{
				Link link = (Link)((LinkEditPart)obj).getModel();
				String componentId = link.getSource().getComponentLabel().getLabelContents();
				watchRecordInner.setComponentId(componentId);
				String socketId = link.getSourceTerminal();
				watchRecordInner.setSocketId(socketId);
				isWatcher = checkWatcher(link.getSource(), link.getSourceTerminal());
				return true;
			}	
		}
		return false;
	}
	
	private boolean checkWatcher(Component selectedComponent, String portName) {
		
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IPath path = new Path(editor.getTitleToolTip());
		String currentJob = path.lastSegment().replace(Constants.JOB_EXTENSION, "");
		watchRecordInner.setCurrentJob(currentJob);
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		String uniqueJobId = editor.getJobId();
		watchRecordInner.setUniqueJobId(uniqueJobId);
		 
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();) {
			EditPart editPart = (EditPart) iterator.next();
			if(editPart instanceof ComponentEditPart) {
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				if(comp.equals(selectedComponent)){
					List<PortEditPart> portEditParts = editPart.getChildren();
					for(AbstractGraphicalEditPart part:portEditParts) {	
						if(part instanceof PortEditPart){
							String port_Name = ((PortEditPart)part).getCastedModel().getTerminal();
							if(port_Name.equals(portName)){
								return ((PortEditPart)part).getPortFigure().isWatched();
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private void debugRemoteMode()throws JSONException, IOException{
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());
		String basePath = job.getBasePath();
		String ipAddress = job.getIpAddress();
		String userID = job.getUserId();
		String password = job.getPassword();
		//logger.debug("BasePath :{}, jobid: {}, componetid: {}, socketid: {}",basePath, jobId, componentId, socketId);
		DebugRestClient debugRestClient = new DebugRestClient(ipAddress, basePath, watchRecordInner.getUniqueJobId(), watchRecordInner.getComponentId(), watchRecordInner.getSocketId(), userID, password);
		JSONArray jsonArray = debugRestClient.callRestService();
		if(jsonArray.length() != 0){
			DebugDataWizard debugRemoteWizard = new DebugDataWizard(Display.getDefault().getActiveShell(), jsonArray, isLocalDebugMode());
			debugRemoteWizard.open();
		}else{
			messageDialog(Messages.REMOTE_MODE_TEXT);
		}
	}
	
	private void debugLocalMode() throws Exception{
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());
		String basePath = job.getBasePath();
		//logger.debug("BasePath :{}, jobid: {}, componetid: {}, socketid: {}",basePath, jobId, componentId, socketId);
		DebugDataReader dataReader = new DebugDataReader(basePath, watchRecordInner.getUniqueJobId(), watchRecordInner.getComponentId(), watchRecordInner.getSocketId());
		List<String> debugDataList = new ArrayList<>();
		int count = 0;
		if(dataReader.isFilePathExists()){
			while(dataReader.hasNext()){
				if(count <= 100){
					String data = dataReader.next().toString();
					debugDataList.add(data);
					count++;
				}else{
					break;
				}
			}
		}else{
			messageDialog(Messages.REMOTE_MODE_TEXT);
			logger.info("file not exists.");
		}
		if(!debugDataList.isEmpty()){
			DebugDataWizard debugRemoteWizard = new DebugDataWizard(Display.getDefault().getActiveShell(), debugDataList, isLocalDebugMode());
			debugRemoteWizard.open();
		}else{
			messageDialog("No record fetched.");
		}
	}
	
	
	private boolean isLocalDebugMode(){
		logger.debug("getRunningMode: "+ JobManager.INSTANCE.isLocalMode());
		return JobManager.INSTANCE.isLocalMode();
	}
	
	private void messageDialog(String message){
		MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.APPLICATION_MODAL | SWT.OK);
		messageBox.setText(Messages.DEBUG_ALERT_MESSAGE); 
		messageBox.setMessage(message);
		messageBox.open();
	}
	
	@Override
	public void run() {
		super.run();
		 
		List<Object> selectedObjects =  getSelectedObjects();
			 createWatchCommand(selectedObjects);
			 if(!isWatcher){
				 messageDialog(Messages.MESSAGES_BEFORE_CLOSE_WINDOW);
			 }else{
				 if(isLocalDebugMode()){
					 try {
						 logger.debug("Reading local debug data");
						debugLocalMode();
					} catch (Exception e) {
						logger.error("Exception in local mode"+e.getMessage(), e);
						messageDialog(Messages.REMOTE_MODE_TEXT);
					}
				 }else{
					 try {
						 logger.debug("Reading REMOTE debug data");
						debugRemoteMode();
					} catch (JSONException | IOException e) {
						logger.error("Exception in remote mode"+e.getMessage(), e);
						messageDialog(Messages.REMOTE_MODE_TEXT);
					}
				 }
			 }
	}
}


class WatchRecordInner{
	private String componentId;
	private String socketId;
	private String currentJob;
	private String uniqueJobId;
	
	public WatchRecordInner() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUniqueJobId() {
		return uniqueJobId;
	}
	public void setUniqueJobId(String uniqueJobId) {
		this.uniqueJobId = uniqueJobId;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getSocketId() {
		return socketId;
	}
	public void setSocketId(String socketId) {
		this.socketId = socketId;
	}
	public String getCurrentJob() {
		return currentJob;
	}
	public void setCurrentJob(String currentJob) {
		this.currentJob = currentJob;
	}
	 
}
