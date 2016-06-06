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

import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.utils.SWTResourceManager;
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
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private Text textEdgeNode;
	private Text textUser;
	private Text textPassword;
	private Text textRunUtility;
	private Text textJobXML;
	private Text textLibs;
	private Text textParamFiles;
	private Text basepathText;
	private Text txtPortNo;
 

	private boolean runGraph;

	private String password;
	private String edgeNodeText;
	private String userId;
	private String basePath;
	private String port_no;

	private Composite compositeServerDetails, compositePathConfig;
	private Button btnLocalMode, btnRemoteMode, okButton;

	private Properties buildProps;

	private HashMap<String, Text> textBoxes;

	private final String LOCAL_MODE = "local";
	private final String REMOTE_MODE = "remote";
	private final String HOST = "host";
	private final String USER_NAME = "userName";

	private final String RUN_UTILITY = "runUtility";
	private final String JOB_XML = "remoteJobXMLDir";
	private final String LIB_PATH = "remoteLibDir";
	private final String PARAM_FILE = "remoteParameterFileDir";
	private final String Base_PATH = "basePath";
	private final String PORT_NO = "remotePortNo";

	private Composite container;
	private String username;
	private String host;
	private boolean remoteMode=false;
	private boolean isDebug;
	//private EmptyTextListener textPortNoListener;
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public RunConfigDialog(Shell parentShell, boolean isDebug) {
		super(parentShell);
		this.runGraph = false;
		buildProps = new Properties();
		textBoxes = new HashMap<>();
		this.isDebug = isDebug;

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		container.getShell().setText("Run Configuration Settings");

		new Label(container, SWT.NONE);

		Composite compositeRunMode = new Composite(container, SWT.BORDER);
		GridData gd_compositeRunMode = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_compositeRunMode.heightHint = 100;
		gd_compositeRunMode.widthHint = 351;
		compositeRunMode.setLayoutData(gd_compositeRunMode);
		formToolkit.adapt(compositeRunMode);
		formToolkit.paintBordersFor(compositeRunMode);

		Label lblRunMode = new Label(compositeRunMode, SWT.NONE);
		lblRunMode.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblRunMode.setBounds(22, 21, 76, 15);
		formToolkit.adapt(lblRunMode, true, true);
		lblRunMode.setText("Run Mode");
		new Label(container, SWT.NONE);

		btnLocalMode = new Button(compositeRunMode, SWT.RADIO);
		btnLocalMode.setBounds(109, 21, 94, 16);
		formToolkit.adapt(btnLocalMode, true, true);
		btnLocalMode.setText("Local");
		new Label(container, SWT.NONE);
		btnLocalMode.setSelection(true);
		btnLocalMode.addSelectionListener(selectionListener);

		btnRemoteMode = new Button(compositeRunMode, SWT.RADIO );
		btnRemoteMode.setBounds(109, 43, 76, 16);
		formToolkit.adapt(btnRemoteMode, true, true);
		btnRemoteMode.setText("Remote");
		new Label(container, SWT.NONE);
     	btnRemoteMode.addSelectionListener(selectionListener);
     	
     	Label lblBasePath = new Label(compositeRunMode, SWT.NONE);
     	lblBasePath.setBounds(22, 70, 70, 15);
		formToolkit.adapt(lblBasePath, true, true);
		lblBasePath.setText("Base Path");
		
     	basepathText = new Text(compositeRunMode, SWT.BORDER);
     	basepathText.setBounds(109, 70, 206, 21);
     	 
     	basepathText.addModifyListener(new ModifyListener() {
     		ControlDecoration txtDecorator = null;
     		
			@Override
			public void modifyText(ModifyEvent event) {
				if (txtDecorator == null){
					txtDecorator = WidgetUtility.addDecorator(basepathText,Messages.ABSOLUTE_PATH_TEXT);
				}
				txtDecorator.hide();
				Text text = (Text)event.widget;
				String data = text.getText();
				IPath path = new Path(data);
				//^(?!-)[a-z0-9-]+(?<!-)(/(?!-)[a-z0-9-]+(?<!-))*$
				Matcher matchs = Pattern.compile("^(?!-)[a-z0-9-]+(?<!-)(/(?!-)[a-z0-9-]+(?<!-))*$").matcher(data);
				if (!path.isAbsolute()) {
					txtDecorator.setMarginWidth(3);
					txtDecorator.show();
				}else{
					txtDecorator.hide();
					txtDecorator.setMarginWidth(3);
				}
				
			}
		});
	 
     	EmptyTextListener textEdgeNodeListener1 = new EmptyTextListener("Base Path");
     	basepathText.addModifyListener(textEdgeNodeListener1);
     	
     	if(!isDebug){
     		basepathText.removeModifyListener(textEdgeNodeListener1);
     		lblBasePath.setVisible(false);
     		basepathText.setVisible(false);
     	}else{
     		lblBasePath.setVisible(true);
     		basepathText.setVisible(true);
     	}
     	 
		formToolkit.adapt(basepathText, true, true);
		textBoxes.put("basePath", basepathText);

		compositeServerDetails = new Composite(container, SWT.BORDER);
		GridData gd_compositeServerDetails = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_compositeServerDetails.widthHint = 350;
		gd_compositeServerDetails.heightHint = 162;
		compositeServerDetails.setLayoutData(gd_compositeServerDetails);
		formToolkit.adapt(compositeServerDetails);
		formToolkit.paintBordersFor(compositeServerDetails);

		Label lblServerDetails = new Label(compositeServerDetails, SWT.NONE);

		FontDescriptor boldDescriptor = FontDescriptor.createFrom(
				lblServerDetails.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor
				.createFont(lblServerDetails.getDisplay());
		lblServerDetails.setFont(boldFont);

		lblServerDetails.setFont(boldFont);
		lblServerDetails.setBounds(24, 10, 84, 15);
		formToolkit.adapt(lblServerDetails, true, true);
		lblServerDetails.setText("Server Details");

		Label lblEdgeNode = new Label(compositeServerDetails, SWT.NONE);
		lblEdgeNode.setBounds(24, 37, 70, 15);
		formToolkit.adapt(lblEdgeNode, true, true);
		lblEdgeNode.setText("Edge Node");

		Label lblUser = new Label(compositeServerDetails, SWT.NONE);
		lblUser.setBounds(24, 79, 55, 15);
		formToolkit.adapt(lblUser, true, true);
		lblUser.setText("User");

		Label lblPassword = new Label(compositeServerDetails, SWT.NONE);
		lblPassword.setBounds(24, 116, 57, 21);
		formToolkit.adapt(lblPassword, true, true);
		lblPassword.setText("Password");

		textEdgeNode = new Text(compositeServerDetails, SWT.BORDER);
		textEdgeNode.setBounds(110, 31, 206, 21);
		EmptyTextListener textEdgeNodeListener = new EmptyTextListener("Edge Node");
		textEdgeNode.addModifyListener(textEdgeNodeListener);
		formToolkit.adapt(textEdgeNode, true, true);
		textBoxes.put("host", textEdgeNode);

		textUser = new Text(compositeServerDetails, SWT.BORDER);
		textUser.setBounds(110, 73, 206, 21);
		EmptyTextListener textUserListener = new EmptyTextListener("Host");
		textUser.addModifyListener(textUserListener);
		formToolkit.adapt(textUser, true, true);
		textBoxes.put("userName", textUser);

		textPassword = new Text(compositeServerDetails, SWT.PASSWORD
				| SWT.BORDER);
		textPassword.setBounds(110, 116, 206, 21);
		EmptyTextListener textPasswordListener = new EmptyTextListener("Password");
		textPassword.addModifyListener(textPasswordListener);
		formToolkit.adapt(textPassword, true, true);
		textBoxes.put("password", textPassword);

		compositeServerDetails.setVisible(false);

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		compositePathConfig = new Composite(container, SWT.BORDER);
		GridData gd_compositePathConfig = new GridData(SWT.CENTER, SWT.BOTTOM,
				false, false, 1, 1);
		gd_compositePathConfig.heightHint = 256;
		gd_compositePathConfig.widthHint = 349;
		compositePathConfig.setLayoutData(gd_compositePathConfig);
		formToolkit.adapt(compositePathConfig);
		formToolkit.paintBordersFor(compositePathConfig);

		Label lblRunUtility = new Label(compositePathConfig, SWT.NONE);
		lblRunUtility.setText("Run Utility");
		lblRunUtility.setBounds(24, 60, 70, 15);
		formToolkit.adapt(lblRunUtility, true, true);

		textRunUtility = new Text(compositePathConfig, SWT.BORDER);
		textRunUtility.setBounds(110, 54, 206, 21);
		formToolkit.adapt(textRunUtility, true, true);
		textBoxes.put("runUtility", textRunUtility);

		Label lblJobXml = new Label(compositePathConfig, SWT.NONE);
		lblJobXml.setText("Job XML");
		lblJobXml.setBounds(24, 98, 70, 15);
		formToolkit.adapt(lblJobXml, true, true);

		textJobXML = new Text(compositePathConfig, SWT.BORDER);
		textJobXML.setBounds(110, 92, 206, 21);
		formToolkit.adapt(textJobXML, true, true);
		textBoxes.put("remoteJobXMLDir", textJobXML);

		Label lblLibs = new Label(compositePathConfig, SWT.NONE);
		lblLibs.setText("Jar Files");
		lblLibs.setBounds(24, 138, 70, 15);
		formToolkit.adapt(lblLibs, true, true);

		textLibs = new Text(compositePathConfig, SWT.BORDER);
		textLibs.setBounds(110, 132, 206, 21);
		formToolkit.adapt(textLibs, true, true);
		textBoxes.put("remoteLibDir", textLibs);

		Label lblParamFiles = new Label(compositePathConfig, SWT.NONE);
		lblParamFiles.setText("Param Files");
		lblParamFiles.setBounds(24, 175, 70, 15);
		formToolkit.adapt(lblParamFiles, true, true);

		textParamFiles = new Text(compositePathConfig, SWT.BORDER);
		textParamFiles.setBounds(110, 169, 206, 21);
		formToolkit.adapt(textParamFiles, true, true);
		textBoxes.put("remoteParameterFileDir", textParamFiles);

		Label lblPathConfiguration = new Label(compositePathConfig, SWT.NONE);
		lblPathConfiguration.setText("Path Configuration");
		lblPathConfiguration.setFont(SWTResourceManager.getFont("Segoe UI", 9,
				SWT.BOLD));
		lblPathConfiguration.setBounds(24, 22, 113, 15);
		formToolkit.adapt(lblPathConfiguration, true, true);
		
		Label lblPortNo = new Label(compositePathConfig, SWT.NONE);
		lblPortNo.setText("Port No");
		lblPortNo.setBounds(24, 210, 70, 15);
		formToolkit.adapt(lblPortNo, true, true);
		
		txtPortNo = new Text(compositePathConfig, SWT.BORDER);
		txtPortNo.setBounds(110, 210, 206, 21);
		formToolkit.adapt(txtPortNo, true, true);
		textBoxes.put("remotePortNo", txtPortNo);
		
		/*Label label = new Label(compositePathConfig, SWT.READ_ONLY);
		label.setBounds(110, 236, 100, 20);
		label.setText("Default_Port_No#8004");*/
		
		if(!isDebug){
     		lblPortNo.setVisible(false);
     		txtPortNo.setVisible(false);
     	}else{
     		lblPortNo.setVisible(true);
     		txtPortNo.setVisible(true);
     	}

		compositePathConfig.setVisible(false);
	 

		loadbuildProperties();

		return container;
	}

	private void loadbuildProperties() {
		String buildPropFilePath = buildPropFilePath();
		IPath bldPropPath = new Path(buildPropFilePath);
		IFile iFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(bldPropPath);
		try {
			InputStream reader = iFile.getContents();
			buildProps.load(reader);

		} catch (CoreException | IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",
					"Exception occured while loading build properties from file -\n"
							+ e.getMessage());
		}

		Enumeration<?> e = buildProps.propertyNames();
		populateTextBoxes(e);

	}

	private String buildPropFilePath() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IFileEditorInput input = (IFileEditorInput) page.getActiveEditor()
				.getEditorInput();

		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		String activeProjectName = activeProject.getName();
		return "/" + activeProjectName + "/build.properties";
	}
	
	private void populateTextBoxes(Enumeration e) {
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (key.equals(LOCAL_MODE)
					&& buildProps.getProperty(key).equals("true")) {
				btnLocalMode.setSelection(true);
				btnRemoteMode.setSelection(false);

			} else if (key.equals(REMOTE_MODE)
					&& buildProps.getProperty(key).equals("true")) {
				btnRemoteMode.setSelection(true);
				btnLocalMode.setSelection(false);
				compositeServerDetails.setVisible(true);
				compositePathConfig.setVisible(true);
			} else if (!(key.equals(LOCAL_MODE) || key.equals(REMOTE_MODE))) {
				textBoxes.get(key).setText(buildProps.getProperty(key));
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
	
	public String getBasePath(){
		return this.basePath;
	}
	 
	public String getPortNo(){
		return this.port_no;
	}
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		okButton.setEnabled(true);

		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);

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
			buildProps.put(JOB_XML, textJobXML.getText());
			buildProps.put(LIB_PATH, textLibs.getText() );
			buildProps.put(PARAM_FILE, textParamFiles.getText());
			buildProps.put(Base_PATH, basepathText.getText()); 
			buildProps.put(PORT_NO, txtPortNo.getText());

			buildProps.store(out, null);

			String buildPropFilePath = buildPropFilePath();

			IPath bldPropPath = new Path(buildPropFilePath);
			iFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(bldPropPath);
			iFile.setContents(new ByteArrayInputStream(out.toByteArray()),
					true, false, null);

		} catch (IOException | CoreException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",
					"Exception occured while saving run configuration file -\n"
							+ e.getMessage());
		}
		this.userId = textUser.getText();
		this.password = textPassword.getText();
		this.username = textUser.getText();
		this.host = textEdgeNode.getText();
		this.basePath = basepathText.getText();
		this.port_no = txtPortNo.getText();
		
		try {
			checkBuildProperties(btnRemoteMode.getSelection());
			this.runGraph = true;
			super.okPressed();
		} catch (IllegalArgumentException e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error",	e.getMessage());
			this.runGraph = false;
		}
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
		if(btnRemoteMode.getSelection()){
			return new Point(380, 671);
		}else{
			return new Point(380, 190);
		}
		
	}

	private void checkBuildProperties(boolean remote) {
		Notification notification = validate(remote);
		if (notification.hasErrors()){
			throw new IllegalArgumentException(notification.errorMessage());
		}
	}

	private Notification validate(boolean remote) {
		Notification note = new Notification();
		if(remote){
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
		if(isDebug && !path.isAbsolute()){
			note.addError("Base Path should not be relative");
		}
		
		/*if(isDebug && StringUtils.isEmpty(txtPortNo.getText())){
			note.addError("Port Value not specified");
		}*/
		
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
				container.getShell().setSize(380, 190);
				compositeServerDetails.setVisible(false);
				compositePathConfig.setVisible(false);

			} else if (button.getText().equals("Remote")) {
				container.getShell().setSize(380, 671);
				compositeServerDetails.setVisible(true);
				compositePathConfig.setVisible(true);
			}

		};
	};
	
	public String getHost(){
		return this.host;
	}
	
	public String getUsername(){
		return this.username;
	}

	public boolean isRemoteMode() {
		return remoteMode;
	}
}
