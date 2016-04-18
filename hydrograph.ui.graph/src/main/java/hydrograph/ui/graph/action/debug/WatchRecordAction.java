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

package hydrograph.ui.graph.action.debug;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.debug.service.DebugDataWizard;
import hydrograph.ui.graph.debug.service.DebugRestClient;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.FileNotFoundException;
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
import org.slf4j.Logger;

import com.bitwiseglobal.debug.api.DebugFilesReader;
 
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
	
	private void debugRemoteMode() {
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());
		String basePath = job.getBasePath();
		String ipAddress = job.getIpAddress();
		String userID = job.getUserId();
		String password = job.getPassword();
		//logger.debug("BasePath :{}, jobid: {}, componetid: {}, socketid: {}",basePath, jobId, componentId, socketId);
		DebugRestClient debugRestClient = new DebugRestClient(ipAddress, basePath, watchRecordInner.getUniqueJobId(), watchRecordInner.getComponentId(), watchRecordInner.getSocketId(), userID, password);
		JSONArray jsonArray = null;
		try {
			jsonArray = debugRestClient.callRestService();
		} catch (NullPointerException e){
			logger.debug(e.getMessage(), e);
			messageDialog(Messages.REMOTE_MODE_TEXT);
		}
		
		if(jsonArray.length() != 0){
			DebugDataWizard debugRemoteWizard = new DebugDataWizard(Display.getDefault().getActiveShell(), jsonArray, isLocalDebugMode());
			debugRemoteWizard.open();
		}else{
			messageDialog(Messages.REMOTE_MODE_TEXT);
		}
	}
	
	private void debugLocalMode() {
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());
		String basePath = job.getBasePath();
		//logger.debug("BasePath :{}, jobid: {}, componetid: {}, socketid: {}",basePath, jobId, componentId, socketId);
		DebugFilesReader debugFilesReader = new DebugFilesReader(basePath, watchRecordInner.getUniqueJobId(), watchRecordInner.getComponentId(), watchRecordInner.getSocketId());
		List<String> debugDataList = new ArrayList<>();
		int count = 0;
		try {
			if(debugFilesReader.isFilePathExists()){
				while(debugFilesReader.hasNext()){
					if(count <= 100){
						String data = debugFilesReader.next();
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
		} catch (FileNotFoundException e) {
			logger.debug(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.debug(e.getMessage(), e);
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
		
		if(!debugDataList.isEmpty()){
			DebugDataWizard debugRemoteWizard = new DebugDataWizard(Display.getDefault().getActiveShell(), debugDataList, isLocalDebugMode());
			debugRemoteWizard.open();
		}else{
			messageDialog(Messages.NO_RECORD_FETCHED);
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
	protected boolean calculateEnabled() {
		int count =0;
		List<Object> selectedObject = getSelectedObjects();
		createWatchCommand(selectedObject);
		if(!selectedObject.isEmpty() && isWatcher){
			for(Object obj : selectedObject){
				if(obj instanceof LinkEditPart)	{
					count++;
				}
			}
		}
		
		if(count == 1){
			return true;
		}else{
			return false;
		}
	}
	
	
	@Override
	public void run() {
		super.run();
		 createWatchCommand(getSelectedObjects());
		 if(!isWatcher){
			 messageDialog(Messages.MESSAGES_BEFORE_CLOSE_WINDOW);
		 }else{
			 if(isLocalDebugMode()){
					debugLocalMode();
			 }else{
					debugRemoteMode();
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