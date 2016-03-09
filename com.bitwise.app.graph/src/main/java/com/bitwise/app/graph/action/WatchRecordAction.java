package com.bitwise.app.graph.action;

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
import org.json.JSONException;
import org.slf4j.Logger;

import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.LinkEditPart;
import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.debug.service.DebugLocalWizard;
import com.bitwise.app.graph.debug.service.DebugRemoteWizard;
import com.bitwise.app.graph.debug.service.DebugRestService;
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

	Logger logger = LogFactory.INSTANCE.getLogger(WatchRecordAction.class);
	private boolean checkWatcher;
	private String jobId;
	private String componentId;
	private String socketId;
	private String currentJob;
	 
	
	public WatchRecordAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected boolean calculateEnabled() {
		 
		return true;
	}

	@Override
	protected void init() {
		super.init();
		setText("Watch Records");
		setId("watchRecord");
		setEnabled(true);
	}
	
	private void createWatchCommand(List<Object> selectedObjects)  {
	 
		for(Object obj:selectedObjects)
		{
			if(obj instanceof LinkEditPart)
			{
				Link link = (Link)((LinkEditPart)obj).getModel();
				componentId = link.getSource().getComponentLabel().getLabelContents();
				socketId = link.getSourceTerminal();
				checkWatcher(link.getSource(), link.getSourceTerminal());
			}	
		}
	}
	
	private boolean checkWatcher(Component selectedComponent, String portName) {
		
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IPath path = new Path(editor.getTitleToolTip());
		currentJob = path.lastSegment().replace(".job", "");
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		jobId = editor.getJobId();
		 
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();)
		{
			EditPart editPart = (EditPart) iterator.next();
			if(editPart instanceof ComponentEditPart) 
			{
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				if(comp.equals(selectedComponent)){
					List<PortEditPart> portEditParts = editPart.getChildren();
					for(AbstractGraphicalEditPart part:portEditParts)
					{	
						if(part instanceof PortEditPart){
							if(((PortEditPart)part).getCastedModel().getNameOfPort().equals(portName)){
								checkWatcher = ((PortEditPart)part).getPortFigure().isWatched();
							}
						}
					}
				}
			}
		}
		return checkWatcher;
	}
	
	private String getUserId(){
		DebugHandler debugHandler = debugHandler();
		Job job = debugHandler.getJob(currentJob);
		String userId = job.getUserId();
		
		return userId;
	}
	
	private String getPassword(){
		DebugHandler debugHandler = debugHandler();
		Job job = debugHandler.getJob(currentJob);
		String passowrd = job.getPassword();
		
		return passowrd;
	}
	
	private String getIPAddress(){
		DebugHandler debugHandler = debugHandler();
		Job job = debugHandler.getJob(currentJob);
		String ipaddress = job.getIpAddress();
	 
		
		return ipaddress;
	}
	
	private String getBasePath(){
		DebugHandler debugHandler = debugHandler();
		Job job = debugHandler.getJob(currentJob);
		String path1 = job.getBasePath();
		return path1;
	}
	
	private DebugHandler debugHandler(){
		DebugHandler debugHandler = new DebugHandler();
		return debugHandler;
	}
	
	private void callRestService()throws JSONException, IOException{
		 
		String basePath = getBasePath();
		String ipAddress = getIPAddress();
		String userID = getUserId();
		String password = getPassword();
		DebugRestService debugRestService = new DebugRestService(ipAddress, basePath, jobId, componentId, socketId, userID, password);
		if(debugRestService.callRestService().length() !=0){
			DebugRemoteWizard debugDialogWizard = new DebugRemoteWizard(Display.getDefault().getActiveShell(), debugRestService.callRestService());
			debugDialogWizard.open();
		}
	}
	
	private void localMode() throws Exception{
		
		String basePath = getBasePath();
	//	logger.debug("BasePath :{}, jobid: {}, componetid: {}, socketid: {}",basePath, jobId, componentId, socketId);
		DebugDataReader dataReader = new DebugDataReader(basePath, jobId, componentId, socketId);
		 
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
			logger.debug("file not exists.");
		}
		 
		if(!debugDataList.isEmpty()){
			DebugLocalWizard debugLocalWizard = new DebugLocalWizard(Display.getDefault().getActiveShell(), debugDataList);
			debugLocalWizard.open();
		}
	}
	
	
	private boolean isLocalDebugMode(){
		logger.debug("getRunningMode: "+ JobManager.INSTANCE.isLocalMode());
			 
		return JobManager.INSTANCE.isLocalMode();
	}
	
	private void messageDialog(){
 
		MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.APPLICATION_MODAL | SWT.OK);
		messageBox.setText("Information"); 
		messageBox.setMessage("No Records available. Execute the Job with Watchers");
		messageBox.open();
	}
	
	@Override
	public void run() {
		super.run();
		 
		List<Object> selectedObjects =  getSelectedObjects();
	 
			 createWatchCommand(selectedObjects);
			 if(!checkWatcher){
				 messageDialog();
			 }else{
				 if(isLocalDebugMode()){
					 try {
						 logger.debug("Reading local debug data");
						localMode();
					} catch (Exception e) {
						logger.error("Exception in local mode"+e.getMessage(), e);
					}
				 }else{
					 try {
						 logger.debug("Reading REMOTE debug data");
						callRestService();
					} catch (JSONException | IOException e) {
						logger.error("Exception in remote mode"+e.getMessage(), e);
					}
				 }
			 }
		 
	}

}
