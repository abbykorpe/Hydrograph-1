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

 
package com.bitwise.app.project.structure.wizard;

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.bitwise.app.project.structure.CustomMessages;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomWizard.
 * 
 * @author Bitwise
 */
public class CustomWizard extends Wizard implements INewWizard, IExecutableExtension  {
	
	private static final String ETL_PROJECT_WIZARD = "ELT Project Wizard"; //$NON-NLS-1$
	private static final String WINDOW_TITLE = "New ELT Project"; //$NON-NLS-1$
	private WizardNewProjectCreationPage pageOne;
	private IConfigurationElement configurationElement;

	/**
	 * Instantiates a new custom wizard.
	 */
	public CustomWizard() {
		setWindowTitle(WINDOW_TITLE);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		String projectName = pageOne.getProjectName();
		URI location = null;
		if(!pageOne.useDefaults()){
			location = pageOne.getLocationURI();
		}
		IProject project=null;
		if(project==null||project!=null)
		project=ProjectStructureCreator.INSTANCE.createProject(projectName, location);
	    BasicNewProjectResourceWizard.updatePerspective(configurationElement);
	    return project!=null?true:false;
	    
	}

	@Override
	public void addPages() {
		super.addPages();
		pageOne = new WizardNewProjectCreationPage(ETL_PROJECT_WIZARD);
	    pageOne.setTitle(CustomMessages.CustomWizard_CREATE_ETL_PROJECT);
	    pageOne.setDescription(CustomMessages.CustomWizard_ENTER_PROJECT_NAME);
	 
	    addPage(pageOne);
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		configurationElement = config;
		
	}
}
