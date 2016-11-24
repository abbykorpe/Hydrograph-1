package hydrograph.ui.propertywindow.runconfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

import hydrograph.ui.common.swt.customwidget.HydroGroup;
import hydrograph.ui.common.util.Constants;

public class RunConfigDialog extends Dialog {
	private Text txtBasePath;
	private Text txtEdgeNode;
	private Text txtUserName;
	private Text txtPassword;
	private Text txtRunUtility;
	private Text txtProjectPath;

	private HydroGroup runModeGroup;
	private HydroGroup serverDetailsGroup;
	private HydroGroup remotePathConfigGroup;
	private Composite groupHolderComposite;
	private Composite remoteRunDetailsHolder;
	
	private Button viewDataCheckBox;
	private Button btnLocalMode;
	private Button btnRemoteMode;
	
	private Properties buildProps;
	
	private final String LOCAL_MODE = "local";
	private final String REMOTE_MODE = "remote";
	private final String HOST = "host";
	private final String USER_NAME = "userName";

	private final String RUN_UTILITY = "runUtility";
	private final String REMOTE_DIRECTORY = "remoteDirectory";
	private final String Base_PATH = "basePath";
	private final String VIEW_DATA_CHECK = "viewDataCheck";

	
	private String password;
	private String userId;
	private String edgeNodeText;
	private String basePath;
	private boolean remoteMode;
	private String host;
	private String username;
	private boolean isDebug;
	private boolean runGraph;	
	
	private static String LOCAL_HOST = "localhost";
	
	
	Composite container;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public RunConfigDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		this.runGraph = false;
		buildProps = new Properties();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.getShell().setText("Run Configuration Settings");
		
		groupHolderComposite = new Composite(container, SWT.BORDER);
		groupHolderComposite.setLayout(new GridLayout(1, false));
		groupHolderComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		runModeGroup = new HydroGroup(groupHolderComposite, SWT.NONE);
		runModeGroup.setHydroGroupText("Run Mode");
		runModeGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		runModeGroup.getHydroGroupClientArea().setLayout(new GridLayout(1, false));
		
		Composite composite_3 = new Composite(runModeGroup.getHydroGroupClientArea(), SWT.NONE);
		composite_3.setLayout(new GridLayout(1, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_1 = new Composite(composite_3, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		composite_4.setLayout(new GridLayout(2, false));
		
		btnLocalMode = new Button(composite_4, SWT.RADIO);
		btnLocalMode.setText("Local Mode");
		btnLocalMode.addSelectionListener(runModeSelectionListener);
		
		btnRemoteMode = new Button(composite_4, SWT.RADIO);
		btnRemoteMode.setText("Remote mode");
		btnRemoteMode.addSelectionListener(runModeSelectionListener);
		
		Composite composite_5 = new Composite(composite_1, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite_5.setLayout(new GridLayout(1, false));
		
		viewDataCheckBox = new Button(composite_5, SWT.CHECK);
		viewDataCheckBox.setText("View Data");
		viewDataCheckBox.addSelectionListener(viewDataSelectionListener);
		
		Composite composite_2 = new Composite(composite_3, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDebugFileLocation = new Label(composite_2, SWT.NONE);
		lblDebugFileLocation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDebugFileLocation.setText("Base Path ");
		
		txtBasePath = new Text(composite_2, SWT.BORDER);
		txtBasePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBasePath.setEnabled(false);
		
		remoteRunDetailsHolder = new Composite(groupHolderComposite, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		remoteRunDetailsHolder.setLayout(gl_composite);
		remoteRunDetailsHolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		serverDetailsGroup = new HydroGroup(remoteRunDetailsHolder, SWT.NONE);
		serverDetailsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		serverDetailsGroup.setHydroGroupText("Server Details");
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 15;
		serverDetailsGroup.getHydroGroupClientArea().setLayout(gridLayout);
		
		Label lblEdgeNode = new Label(serverDetailsGroup.getHydroGroupClientArea(), SWT.NONE);
		lblEdgeNode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEdgeNode.setText("Edge Node ");
		
		txtEdgeNode = new Text(serverDetailsGroup.getHydroGroupClientArea(), SWT.BORDER);
		txtEdgeNode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		EmptyTextListener textEdgeNodeListener = new EmptyTextListener("Edge Node");
		txtEdgeNode.addModifyListener(textEdgeNodeListener);
		
		Label lblUser = new Label(serverDetailsGroup.getHydroGroupClientArea(), SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText("User");
		
		txtUserName = new Text(serverDetailsGroup.getHydroGroupClientArea(), SWT.BORDER);
		txtUserName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		EmptyTextListener textUserListener = new EmptyTextListener("Host");
		txtUserName.addModifyListener(textUserListener);
		
		Label lblPassword = new Label(serverDetailsGroup.getHydroGroupClientArea(), SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");
		
		txtPassword = new Text(serverDetailsGroup.getHydroGroupClientArea(), SWT.PASSWORD | SWT.BORDER);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		EmptyTextListener textPasswordListener = new EmptyTextListener("Password");
		txtPassword.addModifyListener(textPasswordListener);
		
		remotePathConfigGroup = new HydroGroup(remoteRunDetailsHolder, SWT.NONE);
		remotePathConfigGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		remotePathConfigGroup.setHydroGroupText("Remote Path Configurations");
		GridLayout gridLayout_1 = new GridLayout(2, false);
		gridLayout_1.horizontalSpacing = 15;
		remotePathConfigGroup.getHydroGroupClientArea().setLayout(gridLayout_1);
		
		Label lblRunUtility = new Label(remotePathConfigGroup.getHydroGroupClientArea(), SWT.NONE);
		lblRunUtility.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRunUtility.setText("Run Utility ");
		
		txtRunUtility = new Text(remotePathConfigGroup.getHydroGroupClientArea(), SWT.BORDER);
		txtRunUtility.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProjectPath = new Label(remotePathConfigGroup.getHydroGroupClientArea(), SWT.NONE);
		lblProjectPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProjectPath.setText("Project Path ");
		
		txtProjectPath = new Text(remotePathConfigGroup.getHydroGroupClientArea(), SWT.BORDER);
		txtProjectPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		serverDetailsGroup.setVisible(false);
		remotePathConfigGroup.setVisible(false);
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		
		loadbuildProperties();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(499, 483);
	}
	
	SelectionListener runModeSelectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			Button button = ((Button) event.widget);
			if (button.getText().equals("Remote mode")) {				
				showRemoteRunDetailsHolderComposite();				
			}else{
				hideRemoteRunDetailsHolderComposite();
			}
		}		
	};
	
	private void showRemoteRunDetailsHolderComposite() {
		Point shellSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		getShell().setSize(shellSize);
		
		serverDetailsGroup.setVisible(true);
		remotePathConfigGroup.setVisible(true);
	}

	private void hideRemoteRunDetailsHolderComposite() {
		Point remoteRunDetailsHolderSize = remoteRunDetailsHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT);				
		Point shellSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point newShellSize = new Point(shellSize.x, shellSize.y-remoteRunDetailsHolderSize.y);
		getShell().setSize(newShellSize);
		
		serverDetailsGroup.setVisible(false);
		remotePathConfigGroup.setVisible(false);
	}
	
	SelectionListener viewDataSelectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			Button button = ((Button) event.widget);
			
			txtBasePath.setEnabled(button.getSelection());
		}
	};
	
	
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

		Enumeration<?> propertyNames = buildProps.propertyNames();
		populateTextBoxes(propertyNames);

	}
	
	private String buildPropFilePath() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFileEditorInput input = (IFileEditorInput) page.getActiveEditor().getEditorInput();

		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		String activeProjectName = activeProject.getName();
		return "/" + activeProjectName + "/build.properties";
	}
	
	private void populateTextBoxes(Enumeration propertyNames) {
		if (StringUtils.equals(buildProps.getProperty("local"), "true")) {
			btnLocalMode.setSelection(true);
			btnRemoteMode.setSelection(false);
			hideRemoteRunDetailsHolderComposite();
		} else {
			btnRemoteMode.setSelection(true);
			btnLocalMode.setSelection(false);
			showRemoteRunDetailsHolderComposite();
		}
		txtEdgeNode.setText(getBuildProperty(HOST));
		txtUserName.setText(getBuildProperty(USER_NAME));
		txtRunUtility.setText(getBuildProperty(RUN_UTILITY));
		txtProjectPath.setText(getBuildProperty(REMOTE_DIRECTORY));
		txtBasePath.setText(getBuildProperty(Base_PATH));
		
		if(StringUtils.equals(buildProps.getProperty(VIEW_DATA_CHECK), "true")){
			viewDataCheckBox.setSelection(true);
			txtBasePath.setEnabled(true);
		}	
	}
	
	private String getBuildProperty(String key){
		if(buildProps.getProperty(VIEW_DATA_CHECK) == null){
			return "";
		}else{
			return buildProps.getProperty(key);
		}
	}
	
	/*public static void main(String[] args) {
		NewRunConfigDialog newRunConfigDialog = new NewRunConfigDialog(new Shell());
		newRunConfigDialog.open();
	}*/
	
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
	
	@Override
	protected void okPressed() {
		saveRunConfigurations();
	}

	private void saveRunConfigurations() {
		remoteMode = btnRemoteMode.getSelection();
		IFile iFile;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			buildProps.put(LOCAL_MODE, String.valueOf(btnLocalMode.getSelection()));
			buildProps.put(REMOTE_MODE, String.valueOf(btnRemoteMode.getSelection()));
			buildProps.put(HOST, txtEdgeNode.getText());
			buildProps.put(USER_NAME, txtUserName.getText());
			buildProps.put(RUN_UTILITY, txtRunUtility.getText());
			buildProps.put(REMOTE_DIRECTORY, txtProjectPath.getText());
			buildProps.put(Base_PATH, txtBasePath.getText());
			buildProps.put(VIEW_DATA_CHECK, String.valueOf(viewDataCheckBox.getSelection()));
			buildProps.store(out, null);

			String buildPropFilePath = buildPropFilePath();

			IPath bldPropPath = new Path(buildPropFilePath);
			iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(bldPropPath);
			iFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, null);

		} catch (IOException | CoreException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",
					"Exception occured while saving run configuration file -\n" + e.getMessage());
		}
		this.userId = txtUserName.getText();
		this.password = txtPassword.getText();
		this.username = txtUserName.getText();
		this.host = txtEdgeNode.getText();
		this.basePath = txtBasePath.getText();
		this.isDebug = viewDataCheckBox.getSelection();
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
	
	private void checkBuildProperties(boolean remote) {
		Notification notification = validate(remote);
		if (notification.hasErrors()) {
			throw new IllegalArgumentException(notification.errorMessage());
		}
	}

	private Notification validate(boolean remote) {
		Notification note = new Notification();
		if (remote) {
			if (StringUtils.isEmpty(txtEdgeNode.getText()))
				note.addError("Edge Node value not specified");

			if (StringUtils.isEmpty(txtUserName.getText()))
				note.addError("User value not specified");

			if (StringUtils.isEmpty(txtPassword.getText()))
				note.addError("Password not specified");
		}
		if (isDebug && StringUtils.isEmpty(txtBasePath.getText()))
			note.addError("Base Path not specified");

		IPath path = new Path(txtBasePath.getText());
		if (isDebug && !path.isAbsolute()) {
			note.addError("Base Path should not be relative");
		}

		return note;
	}

	public boolean proceedToRunGraph() {
		return runGraph;
	}
	
}
