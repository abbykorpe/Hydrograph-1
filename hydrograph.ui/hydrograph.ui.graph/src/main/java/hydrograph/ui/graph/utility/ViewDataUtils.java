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

package hydrograph.ui.graph.utility;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

/**
 * View Data Utils
 * 
 * @author Bitwise
 *
 */
public class ViewDataUtils {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ViewDataUtils.class);
	/** The jobUniqueId map. */
	private static Map<String, List<Job>> viewDataUniqueIdMap;
	
	public static ViewDataUtils INSTANCE = new ViewDataUtils();
	
	private ViewDataUtils() {
		viewDataUniqueIdMap = new HashMap<>();
	}
	
	
	/**
	 * Static 'instance' method
	 *
	 */
	public static ViewDataUtils getInstance( ) {
      return INSTANCE;
	}
	   
	/**
	 * Gets the job map.
	 *
	 * @param jobName the job name
	 */
	public static Map<String, List<Job>> getJob() {
		return viewDataUniqueIdMap;
	}
	
	/**
	 * Adds the viewData uniqueJobId.
	 *
	 * @param jobName
	 * @param jobDetails
	 */
	public void addDebugJob(String jobName, Job jobDetails){
		
		if(viewDataUniqueIdMap.get(jobName)==null){
			List<Job> jobs = new ArrayList<>();
			jobs.add(jobDetails);
			viewDataUniqueIdMap.put(jobName, jobs);
		}else{
			viewDataUniqueIdMap.get(jobName).add(jobDetails);
		}
	}
	
	/**
	 * Purge ViewData Files
	 *
	 * @param Map<String, List<Job>>
	 */
	public void purgeViewDataFiles(Map<String, List<Job>> viewDataJobMap){
		for(Entry<String, List<Job>> entry : viewDataJobMap.entrySet()){
			List<Job> value =  (List<Job>) entry.getValue();
	        for(Job job : value){
	        	deleteBasePathDebugFiles(job);
	        	deleteSchemaAndDataViewerFiles(job.getUniqueJobId());
	        }
		}
	}
	
	/**
	 * Gets the component canvas.
	 *
	 * @return the component canvas
	 */
	public DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}
	
	/**
	 * Return true if watch point enable otherwise false
	 *
	 * @return boolean
	 */
	public boolean checkWatcher(Component selectedComponent, String portName) {
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		GraphicalViewer graphicalViewer = (GraphicalViewer) ((GraphicalEditor) editor).getAdapter(GraphicalViewer.class);

		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();) {
			EditPart editPart = (EditPart) iterator.next();
			if (editPart instanceof ComponentEditPart) {
				Component comp = ((ComponentEditPart) editPart).getCastedModel();
				if (comp.equals(selectedComponent)) {
					List<PortEditPart> portEditParts = editPart.getChildren();
					for (AbstractGraphicalEditPart part : portEditParts) {
						if (part instanceof PortEditPart) {
							String port_Name = ((PortEditPart) part).getCastedModel().getTerminal();
							if (port_Name.equals(portName)) {
								return ((PortEditPart) part).getPortFigure().isWatched();
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * The function will return timeStamp in milliseconds.
	 *
	 * @return String
	 */
	public String getTimeStamp(){
		long milliSeconds = System.currentTimeMillis();
	    String timeStampLong = Long.toString(milliSeconds);
		return timeStampLong;
	}
	
	public void deleteBasePathDebugFiles(Job job){
		try {
			DebugServiceClient.INSTANCE.deleteBasePathFiles(job.getHost(), job.getPortNumber(), job.getUniqueJobId(), job.getBasePath(),
					job.getUserId(), job.getPassword());
		} catch (Exception exception) {
			logger.warn("Unable to delete debug Base path file",exception);
		} 
	}
	
	public void deleteSchemaAndDataViewerFiles(String uniqueJobId){
		String dataViewerDirectoryPath = Utils.INSTANCE.getDataViewerDebugFilePath();
		IPath path = new Path(dataViewerDirectoryPath);
		boolean deleted = false;
		String dataViewerSchemaFilePathToBeDeleted = "";
		if(path.toFile().isDirectory()){
			String[] fileList = path.toFile().list();
			for (String file: fileList){
				if(file.contains(uniqueJobId)){
					if (OSValidator.isWindows()){
						dataViewerSchemaFilePathToBeDeleted = dataViewerDirectoryPath+ "\\" + file;
					}else{
						dataViewerSchemaFilePathToBeDeleted = dataViewerDirectoryPath+ "/" + file;
					}
					path = new Path(dataViewerSchemaFilePathToBeDeleted);
					if(path.toFile().exists()){
						deleted = path.toFile().delete();
						if(deleted){
							logger.debug("Deleted Data Viewer file {}", dataViewerSchemaFilePathToBeDeleted);
						}else{
							logger.warn("Unable to delete Viewer file {}", dataViewerSchemaFilePathToBeDeleted);
						}
					}
				}
			}
		}
	}
	
}
