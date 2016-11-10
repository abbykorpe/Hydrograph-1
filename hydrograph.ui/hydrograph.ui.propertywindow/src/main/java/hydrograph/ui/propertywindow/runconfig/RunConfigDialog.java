/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package hydrograph.ui.propertywindow.runconfig;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RunConfigDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text textEdgeNode;
	private Text textUser;
	private Text textPassword;
	private Text textRunUtility;
	private Text textDirectory;
	private Text basepathText;

	private boolean runGraph;

	private String password;
	private String edgeNodeText;
	private String userId;
	private String basePath;
	private boolean isDebug;

	private Composite compositeServerDetails, compositePathConfig;
	private Button btnLocalMode, btnRemoteMode, okButton;

	private Properties buildProps;

	private HashMap<String, Text> textBoxes;

	private final String LOCAL_MODE = "local";
	private final String REMOTE_MODE = "remote";
	private final String HOST = "host";
	private final String USER_NAME = "userName";

	private final String RUN_UTILITY = "runUtility";
	private final String REMOTE_DIRECTORY = "remoteDirectory";
	private final String Base_PATH = "basePath";

	private Composite container;
	private String username;
	private String host;
	private boolean remoteMode = false;

	private static String LOCAL_HOST = "localhost";

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public RunConfigDialog(Shell parentShell) {
		super(parentShell);
		this.runGraph = false;
		buildProps = new Properties();
		textBoxes = new HashMap<>();

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.getShell().setText("Run Configuration Settings");
		
		Composite compositeRunMode = new Composite(container, SWT.BORDER);
		compositeRunMode.setBackground(new Color(null, 255, 255, 255));
		compositeRunMode.setLayout(null);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 77;
		gd_composite.widthHint = 346;
		compositeRunMode.setLayoutData(gd_composite);

		Label lblRunMode = new Label(compositeRunMode, SWT.NONE);
		lblRunMode.setFont(new Font(null, "Segoe UI", 9, SWT.BOLD));
		lblRunMode.setBounds(20, 0, 62, 15);
		formToolkit.adapt(lblRunMode, true, true);
		lblRunMode.setText("Run Mode");

		btnLocalMode = new Button(compositeRunMode, SWT.RADIO);
		btnLocalMode.setBounds(20, 21, 90, 16);
		btnLocalMode.setText("Local");
		formToolkit.adapt(btnLocalMode, true, true);

		btnLocalMode.setSelection(true);
		btnLocalMode.addSelectionListener(selectionListener);

		btnRemoteMode = new Button(compositeRunMode, SWT.RADIO);
		btnRemoteMode.setBounds(20, 49, 90, 16);
		btnRemoteMode.setText("Remote");
		formToolkit.adapt(btnRemoteMode, true, true);
		btnRemoteMode.addSelectionListener(selectionListener);

		Label lblDebug = new Label(compositeRunMode, SWT.NONE);
		lblDebug.setBounds(129, 22, 57, 16);
		formToolkit.adapt(lblDebug, true, true);
		lblDebug.setText("View Data");

		final Button isDebugCheck = new Button(compositeRunMode, SWT.CHECK);
		isDebugCheck.setBounds(190, 21, 15, 16);
		formToolkit.adapt(isDebugCheck, true, true);

		final Label lblBasePath = new Label(compositeRunMode, SWT.NONE);
		lblBasePath.setBounds(129, 49, 55, 15);
		lblBasePath.setText("Base Path");
		lblBasePath.setVisible(false);
		formToolkit.adapt(lblBasePath, true, true);
		basepathText = new Text(compositeRunMode, SWT.BORDER);
		basepathText.setBounds(191, 44, 141, 21);
		basepathText.setVisible(false);
		
		final ControlDecoration basePathTxtDecorator = WidgetUtility.addDecorator(basepathText, Messages.ABSOLUTE_PATH_TEXT);

		basepathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				basePathTxtDecorator.hide();
				Text text = (Text) event.widget;
				String data = text.getText();
				IPath path = new Path(data);
				// ^(?!-)[a-z0-9-]+(?<!-)(/(?!-)[a-z0-9-]+(?<!-))*$
				Matcher matchs = Pattern.compile("^(?!-)[a-z0-9-]+(?<!-)(/(?!-)[a-z0-9-]+(?<!-))*$").matcher(data);
				if (!path.isAbsolute()) {
					basePathTxtDecorator.setMarginWidth(3);
					basePathTxtDecorator.show();
				} else {
					basePathTxtDecorator.hide();
					basePathTxtDecorator.setMarginWidth(3);
				}

			}
		});

		final EmptyTextListener textEdgeNodeListener1 = new EmptyTextListener("Base Path");
		basepathText.addModifyListener(textEdgeNodeListener1);

		isDebugCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isDebugCheck.getSelection()) {
					basepathText.removeModifyListener(textEdgeNodeListener1);
					lblBasePath.setVisible(false);
					basepathText.setVisible(false);
					isDebug = false;
					basePathTxtDecorator.hide();
				} else {

					lblBasePath.setVisible(true);
					basepathText.setVisible(true);
					isDebug = true;
				}
			}
		});

		formToolkit.adapt(basepathText, true, true);
		textBoxes.put("basePath", basepathText);

		compositeServerDetails = new Composite(container, SWT.BORDER);
		compositeServerDetails.setBackground(new Color(null, 255, 255, 255));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.widthHint = 345;
		gd_composite_1.heightHint = 110;
		compositeServerDetails.setLayoutData(gd_composite_1);

		Label lblServerDetails = new Label(compositeServerDetails, SWT.NONE);
		lblServerDetails.setFont(new Font(null, "Segoe UI", 9, SWT.BOLD));
		lblServerDetails.setBounds(25, 0, 86, 15);
		formToolkit.adapt(lblServerDetails, true, true);
		lblServerDetails.setText("Server Details");

		Label lblEdgeNode = new Label(compositeServerDetails, SWT.NONE);
		lblEdgeNode.setBounds(25, 24, 67, 15);
		formToolkit.adapt(lblEdgeNode, true, true);
		lblEdgeNode.setText("Edge Node");

		Label lblUser = new Label(compositeServerDetails, SWT.NONE);
		lblUser.setBounds(25, 54, 55, 15);
		formToolkit.adapt(lblUser, true, true);
		lblUser.setText("User");

		Label lblPassword = new Label(compositeServerDetails, SWT.NONE);
		lblPassword.setBounds(25, 81, 55, 15);
		formToolkit.adapt(lblPassword, true, true);
		lblPassword.setText("Password");

		textEdgeNode = new Text(compositeServerDetails, SWT.BORDER);
		textEdgeNode.setBounds(115, 21, 220, 21);
		EmptyTextListener textEdgeNodeListener = new EmptyTextListener("Edge Node");
		textEdgeNode.addModifyListener(textEdgeNodeListener);
		formToolkit.adapt(textEdgeNode, true, true);
		textBoxes.put("host", textEdgeNode);

		textUser = new Text(compositeServerDetails, SWT.BORDER);
		textUser.setBounds(115, 48, 220, 21);
		EmptyTextListener textUserListener = new EmptyTextListener("Host");
		textUser.addModifyListener(textUserListener);
		formToolkit.adapt(textUser, true, true);
		textBoxes.put("userName", textUser);

		textPassword = new Text(compositeServerDetails, SWT.PASSWORD | SWT.BORDER);
		textPassword.setBounds(115, 75, 220, 21);
		EmptyTextListener textPasswordListener = new EmptyTextListener("Password");
		textPassword.addModifyListener(textPasswordListener);
		formToolkit.adapt(textPassword, true, true);
		textBoxes.put("password", textPassword);

		compositeServerDetails.setVisible(false);

		compositePathConfig = new Composite(container, SWT.BORDER);
		compositePathConfig.setBackground(new Color(null, 255, 255, 255));
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.heightHint = 88;
		gd_composite_2.widthHint = 344;
		compositePathConfig.setLayoutData(gd_composite_2);

		Label lblPathConfiguration = new Label(compositePathConfig, SWT.NONE);
		lblPathConfiguration.setFont(new Font(null, "Segoe UI", 9, SWT.BOLD));
		formToolkit.adapt(lblPathConfiguration, true, true);
		lblPathConfiguration.setBounds(27, 0, 160, 15);
		lblPathConfiguration.setText("Remote Path Configuration");

		Label lblRunUtility = new Label(compositePathConfig, SWT.NONE);
		lblRunUtility.setText("Run Utility");
		lblRunUtility.setBounds(27, 29, 62, 15);
		formToolkit.adapt(lblRunUtility, true, true);

		textRunUtility = new Text(compositePathConfig, SWT.BORDER);
		textRunUtility.setBounds(117, 26, 217, 21);
		formToolkit.adapt(textRunUtility, true, true);
		textBoxes.put("runUtility", textRunUtility);

		Label lblJobXml = new Label(compositePathConfig, SWT.NONE);
		lblJobXml.setText("Project Path");
		lblJobXml.setBounds(27, 59, 70, 15);
		formToolkit.adapt(lblJobXml, true, true);

		textDirectory = new Text(compositePathConfig, SWT.BORDER);
		textDirectory.setBounds(117, 53, 217, 21);
		formToolkit.adapt(textDirectory, true, true);
		textBoxes.put("remoteDirectory", textDirectory);
		compositePathConfig.setVisible(false);

		loadbuildProperties();

		return container;
	}

	private void loadbuildProperties() {
		String buildPropFilePath = buildPropFilePath();
		IPath bldPropPath = new Path(buildPropFilePath);
		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(bldPropPath);
		try {
			InputStream reader = iFile.getContents();
			buildProps.load(reader);

		} catch (CoreException | IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",
					"Exception occured while loading build properties from file -\n" + e.getMessage());
		}

		Enumeration<?> e = buildProps.propertyNames();
		populateTextBoxes(e);

	}

	private String buildPropFilePath() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFileEditorInput input = (IFileEditorInput) page.getActiveEditor().getEditorInput();

		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		String activeProjectName = activeProject.getName();
		return "/" + activeProjectName + "/build.properties";
	}

	private void populateTextBoxes(Enumeration e) {
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (LOCAL_MODE.equals(key) && buildProps.getProperty(key).equals("true")) {
				btnLocalMode.setSelection(true);
				btnRemoteMode.setSelection(false);

			} else if (REMOTE_MODE.equals(key) && buildProps.getProperty(key).equals("true")) {
				btnRemoteMode.setSelection(true);
				btnLocalMode.setSelection(false);
				compositeServerDetails.setVisible(true);
				compositePathConfig.setVisible(true);
			} else if (!(LOCAL_MODE.equals(key) || REMOTE_MODE.equals(key))) {
				if (!"password".equalsIgnoreCase(key) && textBoxes.get(key) != null) {
					textBoxes.get(key).setText(buildProps.getProperty(key));
				}
			}
		}
	}

	public String getClusterPassword() {
		return this.password;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getEdgeNodeIp() {
		return this.edgeNodeText;
	}

	public String getBasePath() {
		return this.basePath;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		okButton.setEnabled(true);

		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

	}

	@Override
	protected void okPressed() {
		remoteMode = btnRemoteMode.getSelection();
		IFile iFile;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			buildProps.put(LOCAL_MODE, String.valueOf(btnLocalMode.getSelection()));
			buildProps.put(REMOTE_MODE, String.valueOf(btnRemoteMode.getSelection()));
			buildProps.put(HOST, textEdgeNode.getText());
			buildProps.put(USER_NAME, textUser.getText());
			buildProps.put(RUN_UTILITY, textRunUtility.getText());
			buildProps.put(REMOTE_DIRECTORY, textDirectory.getText());
			buildProps.put(Base_PATH, basepathText.getText());
			buildProps.store(out, null);

			String buildPropFilePath = buildPropFilePath();

			IPath bldPropPath = new Path(buildPropFilePath);
			iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(bldPropPath);
			iFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, null);

		} catch (IOException | CoreException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",
					"Exception occured while saving run configuration file -\n" + e.getMessage());
		}
		this.userId = textUser.getText();
		this.password = textPassword.getText();
		this.username = textUser.getText();
		this.host = textEdgeNode.getText();
		this.basePath = basepathText.getText();

		try {
			checkBuildProperties(btnRemoteMode.getSelection());
			this.runGraph = true;
			super.okPressed();
		} catch (IllegalArgumentException e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", e.getMessage());
			this.runGraph = false;
		}
		
		setPreferences();
	}

	private void setPreferences() {
		if(StringUtils.isBlank(PlatformUI.getPreferenceStore().getString(Constants.HOST)))
			PlatformUI.getPreferenceStore().setValue(Constants.HOST,this.host);
		
	}

	@Override
	protected void cancelPressed() {
		runGraph = false;
		super.cancelPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		if (btnRemoteMode.getSelection()) {
			return new Point(365, 394);
		} else {
			return new Point(365, 181);
		}

	}

	private void checkBuildProperties(boolean remote) {
		Notification notification = validate(remote);
		if (notification.hasErrors()) {
			throw new IllegalArgumentException(notification.errorMessage());
		}
	}

	private Notification validate(boolean remote) {
		Notification note = new Notification();
		if (remote) {
			if (StringUtils.isEmpty(textEdgeNode.getText()))
				note.addError("Edge Node value not specified");

			if (StringUtils.isEmpty(textUser.getText()))
				note.addError("User value not specified");

			if (StringUtils.isEmpty(textPassword.getText()))
				note.addError("Password not specified");
		}
		if (isDebug && StringUtils.isEmpty(basepathText.getText()))
			note.addError("Base Path not specified");

		IPath path = new Path(basepathText.getText());
		if (isDebug && !path.isAbsolute()) {
			note.addError("Base Path should not be relative");
		}

		return note;
	}

	public boolean proceedToRunGraph() {
		return runGraph;
	}

	SelectionListener selectionListener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent event) {

			Button button = ((Button) event.widget);

			if (button.getText().equals("Local")) {
				container.getShell().setSize(365, 181);
				compositeServerDetails.setVisible(false);
				compositePathConfig.setVisible(false);

			} else if (button.getText().equals("Remote")) {
				container.getShell().setSize(365, 394);
				compositeServerDetails.setVisible(true);
				compositePathConfig.setVisible(true);
			}

		};
	};

	public String getHost() {
		if (remoteMode) {
			return this.host;
		} else {
			return LOCAL_HOST;
		}

	}

	public String getUsername() {
		return this.username;
	}

	public boolean isRemoteMode() {
		return remoteMode;
	}

	public boolean isDebug() {
		return isDebug;
	}

}
