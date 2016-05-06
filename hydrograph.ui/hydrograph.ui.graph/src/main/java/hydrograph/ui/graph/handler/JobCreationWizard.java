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

package hydrograph.ui.graph.handler;

import hydrograph.ui.graph.Messages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Create new new .job-file. Those files can be used with the GraphicalEditor
 * (see plugin.xml).
 * 
 * @author Bitwise
 */
public class JobCreationWizard extends Wizard implements INewWizard {

	private JobCreationPage page1;
	
	public JobCreationWizard(){
	setWindowTitle(Messages.NEW_JOB);
	}

	/*
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		// add pages to this wizard
		addPage(page1);
	}

	/*
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// create pages for this wizard
		Boolean openProjectFound = false;
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (projects != null && projects.length != 0) {
			openProjectFound = isOpenProjectExists(openProjectFound, projects);
			if (openProjectFound) {
				page1 = new JobCreationPage(workbench, selection);
			} else {
				MessageBox messageBox = createErrorDialog(Messages.OPEN_PROJECT_ERROR_MESSAGE);
				if (messageBox.open() == SWT.OK) {
				}
			}
		} else {
			MessageBox messageBox = createErrorDialog(Messages.OPEN_PROJECT_ERROR_MESSAGE);
			if (messageBox.open() == SWT.OK) {
			}
		}
		
	}

	/*
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		return page1.finish();
	}
	private Boolean isOpenProjectExists(Boolean openProjectFound, IProject[] projects) {
		for (IProject project : projects) {
			if (project.isOpen()) {
				openProjectFound = true;
				break;
			}
		}
		return openProjectFound;
	}

	private MessageBox createErrorDialog(String errorMessage) {
		MessageBox messageBox = new MessageBox(new Shell(), SWT.ERROR | SWT.OK);
		messageBox.setMessage(errorMessage);
		messageBox.setText("Error");
		return messageBox;
	}

}
