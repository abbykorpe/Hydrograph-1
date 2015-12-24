package com.bitwise.app.menus.importWizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.exceptions.EngineException;
import com.bitwise.app.engine.ui.exceptions.ComponentNotFoundException;
import com.bitwise.app.engine.ui.util.UiConverterUtil;
import com.bitwise.app.menus.Activator;
import com.bitwise.app.menus.messages.Messages;

public class ImportEngineXmlWizardPage extends WizardNewFileCreationPage {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(ImportEngineXmlWizard.class);
	private FileFieldEditor editor;

	private static final String JOB_FILE_EXTENTION = "job";
	private static final String PARAMETER_FOLDER = "/param/";
	private static final String PARAMETER_FILE_EXTENTION = "properties";
	private static final String ALLOWED_EXTENSIONS = "*.xml";
	private static final String TIMES_NEW_ROMAN_BALTIC_FONT = "Times New Roman Baltic";
	private IPath parameterFilePath, jobFilePath;
	private String targetxmlFilePath;

	public ImportEngineXmlWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle(pageName);
		setDescription(Messages.TITLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ImportEngineXmlWizardPage#createAdvancedControls (org.eclipse.swt.widgets.Composite)
	 */
	protected void createAdvancedControls(Composite parent) {
		LOGGER.debug("Creating Import Engine XML layout");
		Composite fileSelectionArea = new Composite(parent, SWT.NONE);
		fileSelectionArea.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));

		GridLayout fileSelectionLayout = new GridLayout();
		fileSelectionLayout.makeColumnsEqualWidth = false;
		fileSelectionLayout.marginWidth = 0;
		fileSelectionLayout.marginHeight = 0;
		fileSelectionArea.setLayout(fileSelectionLayout);

		editor = new FileFieldEditor("fileSelect", Messages.SELECT_FILE_LABEL_TEXT, fileSelectionArea);
		editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				IPath path = new Path(ImportEngineXmlWizardPage.this.editor.getStringValue());
				if (path.segment(0) != null) {
					targetxmlFilePath = editor.getStringValue();
					setFileName(path.lastSegment());
				} else {
					targetxmlFilePath = null;
					displayError();
				}
			}
		});
		String[] extensions = new String[] { ALLOWED_EXTENSIONS }; // NON-NLS-1
		editor.setFileExtensions(extensions);
		fileSelectionArea.moveAbove(null);

		Composite fileSelectionArea2 = new Composite(parent, SWT.NONE);
		fileSelectionArea2.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		GridLayout fileSelectionLayout2 = new GridLayout();
		fileSelectionLayout2.numColumns = 2;

		fileSelectionLayout2.makeColumnsEqualWidth = false;
		fileSelectionLayout2.marginWidth = 0;
		fileSelectionLayout2.marginHeight = 0;
		fileSelectionArea2.setLayout(fileSelectionLayout2);
		Font fontNote = new Font(fileSelectionArea2.getDisplay(), TIMES_NEW_ROMAN_BALTIC_FONT, 9, SWT.BOLD);
		Label lblNoteHeader = new Label(fileSelectionArea2, SWT.NONE);
		lblNoteHeader.setText(Messages.NOTE_LABEL_HEADER_TEXT);
		lblNoteHeader.setFont(fontNote);
		Label lblNote = new Label(fileSelectionArea2, SWT.NONE);

		GridData gd_lblNote = new GridData(SWT.BOTTOM, SWT.CENTER, false, false, 1, 1);
		gd_lblNote.widthHint = 391;
		lblNote.setLayoutData(gd_lblNote);
		lblNote.setText(Messages.NOTE_MESSAGE_TEXT);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ImportEngineXmlWizardPage#createLinkTarget()
	 */

	protected void displayError() {
		setErrorMessage(Messages.SOURCE_EMPTY_ERROR_MESSAGE);
		setPageComplete(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ImportEngineXmlWizardPage#getInitialContents()
	 */
	protected InputStream getInitialContents() {
		try {
			return new FileInputStream(new File(editor.getStringValue()));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ImportEngineXmlWizardPage#getNewFileLabel()
	 */
	protected String getNewFileLabel() {
		return Messages.NEW_FILE_LABEL_TEXT; // NON-NLS-1
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ImportEngineXmlWizardPage#validateLinkedResource()
	 */
	protected IStatus validateLinkedResource() {
		return new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, "", null);
	}

	public IFile createNewFile() {
		LOGGER.debug("Creating new files");
		UiConverterUtil uiConverterUtil = new UiConverterUtil();
		IFile jobFile = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFilePath);
		IFile parameterFile = ResourcesPlugin.getWorkspace().getRoot().getFile(parameterFilePath);
		try {
			uiConverterUtil.convertToUiXML(new File(targetxmlFilePath), jobFile, parameterFile);
			LOGGER.debug("Successfully created *job,*properties files in workspace");
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | EngineException  | IOException | ComponentNotFoundException exception) {

			LOGGER.error("Error occurred while creating new files in workspace", exception);
			showMessageBox(exception, Messages.EXCEPTION_OCCURED);
			return null;
		} catch (JAXBException | ParserConfigurationException
				| SAXException exception) {
			LOGGER.error("Error occurred while creating new files in workspace", exception);
			showMessageBox(exception, Messages.INVALID_TARGET_FILE_ERROR);
			return null;
		}
		LOGGER.debug("Importing *xml file");
		return super.createNewFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	protected boolean validatePage() {
		LOGGER.debug("Validating Import Wizard Page");
		boolean valid = false;
		if (targetxmlFilePath == null) {
			valid = false;
			displayError();
		} else if (super.validatePage()) {
			valid = true;
			String fileName = getFileName();
			parameterFilePath = createParameterFilePath(fileName);
			jobFilePath = getContainerFullPath().append(fileName).removeFileExtension()
					.addFileExtension(JOB_FILE_EXTENTION);
			if (isFilesAvailable(parameterFilePath)) {
				setErrorMessage(parameterFilePath + " " + Messages.ALREADY_EXISTS_ERROR_MESSAGE);
				valid = false;
			}
			if (isFilesAvailable(jobFilePath)) {
				setErrorMessage(jobFilePath + " " + Messages.ALREADY_EXISTS_ERROR_MESSAGE);
				valid = false;
			}
		}

		return valid;
	}

	public boolean isFilesAvailable(IPath path) {
		LOGGER.debug("Checking file availability at path :{}", path);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot().getFile(path).exists();
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
	}

	private IPath createParameterFilePath(String fileName) {
		LOGGER.debug("Creating parameter file's path for filename :{}", fileName);
		IPath parameterFilePath = new Path("/" + getContainerFullPath().segment(0));
		parameterFilePath = parameterFilePath.append(PARAMETER_FOLDER + fileName);
		return parameterFilePath.removeFileExtension().addFileExtension(PARAMETER_FILE_EXTENTION);
	}

	protected void createLinkTarget() {
	}

	private void showMessageBox(Exception exception, String message) {
		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
		messageBox.setMessage(message + "\n" + exception.getMessage());
		messageBox.open();
	}
}
