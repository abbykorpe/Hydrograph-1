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

 
package hydrograph.ui.graph.action.subjob;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.utility.SubJobUtility;
import hydrograph.ui.logging.factory.LogFactory;


/**
 * The Class SubJobTrackingAction use to view subjob tracking,on this action new temp subjob tracking container will be open.
 * 
 * @author Bitwise
 */
public class SubJobTrackingAction extends SelectionAction{

	private static final String TEMP_DIRECTORY="temp"; 
	
	private static Logger logger = LogFactory.INSTANCE.getLogger(SubJobTrackingAction.class);

	/**
	 * Instantiates a new SubJobTracking action.
	 * 
	 * @param part
	 *            the part
	 * @param action
	 *            the action
	 */
	public SubJobTrackingAction(IWorkbenchPart part, IAction action) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();

		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Constants.SUBJOB_TRACKING); 
		setId(Constants.SUBJOB_TRACKING);
		setEnabled(false);
	}



	/*
	 * Open the sub graph to show tracking.
	 */
	@SuppressWarnings("unused")
	@Override
	public void run() {
		List<Object> selectedObjects = getSelectedObjects();
		SubJobUtility subJobUtility = new SubJobUtility();
		if (selectedObjects != null && !selectedObjects.isEmpty()) {
			for (Object obj : selectedObjects) {
				if (obj instanceof ComponentEditPart) {
					if (((ComponentEditPart) obj).getCastedModel().getCategory().equalsIgnoreCase(Constants.SUBJOB_COMPONENT_CATEGORY)) {
						Component subjobComponent = ((ComponentEditPart) obj).getCastedModel();
						ELTGraphicalEditor eltGraphicalEditor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
						Container container=(Container)subjobComponent.getProperties().get(Constants.SUBJOB_CONTAINER);
						if(subjobComponent.getParent().isCurrentGraphSubjob()){
							container.setUniqueJobId(subjobComponent.getParent().getUniqueJobId());
						}
						else{
							container.setUniqueJobId(eltGraphicalEditor.getJobId());
						}
						String tempFileName =new Path(eltGraphicalEditor.getJobName()+"_"+subjobComponent.getComponentLabel().getLabelContents()).toString();
						IFolder folder=ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(eltGraphicalEditor.getActiveProject()+"/"+TEMP_DIRECTORY));
						if(!folder.exists()){
							try {
								folder.create(true, true,new NullProgressMonitor());
							} catch (CoreException e) {
								logger.error("Failed to create temp subjob tracking file", e);
							}
						}
						
						IFile tempFile=folder.getFile(tempFileName+Constants.JOB_EXTENSION);
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						subJobUtility.doSaveAsSubJob(tempFile, container);
						try {
							ELTGraphicalEditor editorPart=(ELTGraphicalEditor) IDE.openEditor(page, tempFile,true);
						editorPart.setDeleteOnDispose(true);
						eltGraphicalEditor.addSubJobEditor(editorPart);
						} catch (PartInitException e) {
							logger.error("Failed to open tracking view for subjob", e);
						}
					}
				}
			}
		}
	}

		
	@Override
	protected boolean calculateEnabled() {
		List<Object> selectedObjects = getSelectedObjects();
		if (selectedObjects != null && !selectedObjects.isEmpty() && selectedObjects.size() == 1) {
			for (Object obj : selectedObjects) {
				if (obj instanceof ComponentEditPart) {
					if (Constants.SUBJOB_COMPONENT.equalsIgnoreCase(((ComponentEditPart) obj).getCastedModel()
							.getComponentName()))
						return true;
				}
			}
		}
		return false;
	}	
	
   }
