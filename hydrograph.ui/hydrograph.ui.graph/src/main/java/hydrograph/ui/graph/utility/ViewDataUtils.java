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
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.model.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;

/**
 * View Data Utils
 * 
 * @author Bitwise
 *
 */
public class ViewDataUtils {

	/** The jobUniqueId map. */
	private static Map<String, List<Job>> viewDataUniqueIdMap;
	
	public static ViewDataUtils INSTANCE = new ViewDataUtils();
	
	public ViewDataUtils() {
		viewDataUniqueIdMap = new HashMap<>();
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
			if(viewDataUniqueIdMap.get(jobName).size() > 5){
				viewDataUniqueIdMap.get(jobName).clear();
			}
			viewDataUniqueIdMap.get(jobName).add(jobDetails);
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
}
