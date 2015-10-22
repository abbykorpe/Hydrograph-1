package com.bitwise.app.project.structure.wizard;

import java.net.URI;

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
		ProjectStructureCreator.INSTANCE.createProject(projectName, location);
	    BasicNewProjectResourceWizard.updatePerspective(configurationElement);
		return true;
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
