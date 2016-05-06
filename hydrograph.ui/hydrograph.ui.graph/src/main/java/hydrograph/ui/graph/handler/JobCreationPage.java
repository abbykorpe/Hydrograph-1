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
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.editor.ELTGraphicalEditorInput;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;

/**
 * This WizardPage can create an empty .job file for the GraphicalEditor.
 * @author Bitwise
 */
public class JobCreationPage extends WizardNewFileCreationPage {
	private Logger logger = LogFactory.INSTANCE.getLogger(JobCreationPage.class);
	private static int jobCounter = 1;
	private static final String DEFAULT_EXTENSION = ".job";
	private final IWorkbench workbench;

	/**
	 * Create a new wizard page instance.
	 * 
	 * @param workbench
	 *            the current workbench
	 * @param selection
	 *            the current object selection
	 * @see JobCreationWizard#init(IWorkbench, IStructuredSelection)
	 */
	JobCreationPage(IWorkbench workbench, IStructuredSelection selection) {
		super("jobCreationPage1", selection);
		this.workbench = workbench;
		setTitle(Messages.JOB_WIZARD_TITLE);
		setDescription(Messages.CREATE_NEW + DEFAULT_EXTENSION + " " + Messages.FILE);
	}

	/*
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createControl(org .eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName(Messages.JOB + jobCounter++ + DEFAULT_EXTENSION);
		setPageComplete(validatePage());
	}

	/**
	 * This method will be invoked, when the "Finish" button is pressed.
	 * 
	 * @see JobCreationWizard#performFinish()
	 */
	boolean finish() {

		// open newly created job in the editor
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		ELTGraphicalEditorInput input = new ELTGraphicalEditorInput(getFileName());
		if (page != null) {
			try {
				page.openEditor(input, ELTGraphicalEditor.ID, true);
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
				if (activeWindow != null) {
					final IWorkbenchPage activePage = activeWindow.getActivePage();
					if (activePage != null) {
						activePage.activate(activePage.findEditor(input));
					}
				}
			} catch (PartInitException e) {
				logger.error("Error while opening job", e);
				return false;
			}
		}
		saveJob();
		return true;

	}

	private void saveJob() {
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();
		IPath filePath = new Path(this.getContainerFullPath() + "/" + this.getFileName());
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
		if (file != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				editor.createOutputStream(out);
				if (file.exists())
					file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, null);
				else
					file.create(new ByteArrayInputStream(out.toByteArray()), true, null);
				logger.info("Resetting EditorInput data from GraphicalEditorInput to FileEditorInput");
				editor.setInput(new FileEditorInput(file));
				editor.initializeGraphicalViewer();
				editor.genrateTargetXml(file, null, null);
				editor.getCommandStack().markSaveLocation();
			} catch (CoreException | IOException ce) {
				logger.error("Failed to Save the file : ", ce);
				MessageDialog.openError(new Shell(), "Error",
						"Exception occured while saving the graph -\n" + ce.getMessage());
			}
			editor.setDirty(false);
		}
	}

	/*
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
	 */
	protected InputStream getInitialContents() {
		ByteArrayInputStream bais = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.flush();
			oos.close();
			bais = new ByteArrayInputStream(baos.toByteArray());
		} catch (IOException e) {
			logger.error("Error while job wizard creation", e);
		}
		return bais;
	}

	/**
	 * Return true, if the file name entered in this page is valid.
	 */
	private boolean validateFilename() {
		if (getFileName() != null && getFileName().endsWith(DEFAULT_EXTENSION)) {
			return true;
		}
		setErrorMessage(Messages.FILE_END_MESSAGE + " " + DEFAULT_EXTENSION);
		return false;
	}

	/*
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	protected boolean validatePage() {
		return super.validatePage() && validateFilename();
	}
}