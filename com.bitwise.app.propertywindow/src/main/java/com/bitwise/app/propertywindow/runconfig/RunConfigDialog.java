package com.bitwise.app.propertywindow.runconfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.bitwise.app.common.util.SWTResourceManager;
import com.bitwise.app.propertywindow.factory.ListenerFactory;


public class RunConfigDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text textEdgeNode;
	private Text textUser;
	private Text textPassword;
	private Text textRunUtility;
	private Text textJobXML;
	private Text textLibs;
	private Text textParamFiles;

	private boolean runGraph;
	
	private String password;

	Composite compositeServerDetails, compositePathConfig;
	Button btnLocalMode, btnRemoteMode;
	
	Properties buildProps;
	
	HashMap<String, Text> textBoxes;
	
	private final String LOCAL_MODE="local";
	private final String REMOTE_MODE="remote";
	private final String HOST="host";
	private final String USER_NAME="userName";
	private final String RUN_UTILITY="runUtility";
	private final String JOB_XML="jobXML";
	private final String LIB_PATH="libs";
	private final String PARAM_FILE="paramFile";
	private Composite container;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public RunConfigDialog(Shell parentShell) {
		super(parentShell);
		this.runGraph = false;
		buildProps = new Properties();
		textBoxes= new HashMap<>();
		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		container.getShell().setText("Run Configuration Settings");

		new Label(container, SWT.NONE);

		Composite compositeRunMode = new Composite(container, SWT.BORDER);
		GridData gd_compositeRunMode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compositeRunMode.heightHint = 80;
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

		btnLocalMode.addSelectionListener(selectionListener);


		btnRemoteMode = new Button(compositeRunMode, SWT.RADIO);
		btnRemoteMode.setBounds(109, 43, 76, 16);
		formToolkit.adapt(btnRemoteMode, true, true);
		btnRemoteMode.setText("Remote");
		new Label(container, SWT.NONE);

		btnRemoteMode.addSelectionListener(selectionListener);

		compositeServerDetails = new Composite(container, SWT.BORDER);
		GridData gd_compositeServerDetails = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compositeServerDetails.widthHint = 350;
		gd_compositeServerDetails.heightHint = 162;
		compositeServerDetails.setLayoutData(gd_compositeServerDetails);
		formToolkit.adapt(compositeServerDetails);
		formToolkit.paintBordersFor(compositeServerDetails);

		
		
		Label lblServerDetails = new Label(compositeServerDetails, SWT.NONE);
		
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(lblServerDetails.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(lblServerDetails.getDisplay());
		lblServerDetails.setFont( boldFont );
		

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
		formToolkit.adapt(textEdgeNode, true, true);
		textBoxes.put("host", textEdgeNode);
		

		textUser = new Text(compositeServerDetails, SWT.BORDER);
		textUser.setBounds(110, 73, 206, 21);
		formToolkit.adapt(textUser, true, true);
		textBoxes.put("userName", textUser);

		textPassword = new Text(compositeServerDetails, SWT.PASSWORD|SWT.BORDER);
		textPassword.setBounds(110, 116, 206, 21);
		formToolkit.adapt(textPassword, true, true);
		
		compositeServerDetails.setVisible(false);
		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		compositePathConfig = new Composite(container, SWT.BORDER);
		GridData gd_compositePathConfig = new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1);
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
		textBoxes.put("jobXML", textJobXML);

		Label lblLibs = new Label(compositePathConfig, SWT.NONE);
		lblLibs.setText("Jar Files");
		lblLibs.setBounds(24, 138, 70, 15);
		formToolkit.adapt(lblLibs, true, true);

		textLibs = new Text(compositePathConfig, SWT.BORDER);
		textLibs.setBounds(110, 132, 206, 21);
		formToolkit.adapt(textLibs, true, true);
		textBoxes.put("libs", textLibs);

		Label lblParamFiles = new Label(compositePathConfig, SWT.NONE);
		lblParamFiles.setText("Param Files");
		lblParamFiles.setBounds(24, 175, 70, 15);
		formToolkit.adapt(lblParamFiles, true, true);

		textParamFiles = new Text(compositePathConfig, SWT.BORDER);
		textParamFiles.setBounds(110, 169, 206, 21);
		formToolkit.adapt(textParamFiles, true, true);
		textBoxes.put("paramFile", textParamFiles);

		Label lblPathConfiguration = new Label(compositePathConfig, SWT.NONE);
		lblPathConfiguration.setText("Path Configuration");
		lblPathConfiguration.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblPathConfiguration.setBounds(24, 22, 113, 15);
		formToolkit.adapt(lblPathConfiguration, true, true);
		
		compositePathConfig.setVisible(false);

		loadbuildProperties();
	
		return container;
	}
	
	public void loadbuildProperties(){
		String buildPropFilePath = buildPropFilePath();
		IPath bldPropPath =new Path(buildPropFilePath);
		IFile iFile=ResourcesPlugin.getWorkspace().getRoot().getFile(bldPropPath);
		try {
			InputStream reader = iFile.getContents();
			buildProps.load(reader);
			
		} catch (CoreException | IOException e) {
			MessageDialog.openError(new Shell(), "Error", "Exception occured while loading build properties from file -\n"+e.getMessage());
		}
		
		Enumeration<?> e = buildProps.propertyNames();
		populateTextBoxes(e);
	    
		
	}
	public void populateTextBoxes(Enumeration e){
		while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      if(key.equals(LOCAL_MODE) && buildProps.getProperty(key).equals("true")){
		    	  btnLocalMode.setSelection(true);
		    	  btnRemoteMode.setSelection(false);
		      }else if(key.equals(REMOTE_MODE) && buildProps.getProperty(key).equals("true")){
		    	  btnRemoteMode.setSelection(true);
		    	  btnLocalMode.setSelection(false);
		    	  container.getShell().setSize(380,730);
		    	  compositeServerDetails.setVisible(true);
		    	  compositePathConfig.setVisible(true);
		      }else if(!(key.equals(LOCAL_MODE) || key.equals(REMOTE_MODE))){
		    	  textBoxes.get(key).setText(buildProps.getProperty(key));  
		      }
		    }
	}
	

	public String buildPropFilePath(){
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IFileEditorInput input=(IFileEditorInput) page.getActiveEditor().getEditorInput();

		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		String activeProjectName = activeProject.getName();
		return "/"+activeProjectName+ "/build.properties";
	}

	public String getClusterPassword(){
		return this.password;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		okButton.setEnabled(false);
		
		EmptyTextListener emptyTextListener = new EmptyTextListener(okButton);
		textEdgeNode.addModifyListener(emptyTextListener);
		textUser.addModifyListener(emptyTextListener);
		textPassword.addModifyListener(emptyTextListener);
		textRunUtility.addModifyListener(emptyTextListener);
		textJobXML.addModifyListener(emptyTextListener);
		textLibs.addModifyListener(emptyTextListener);
		textParamFiles.addModifyListener(emptyTextListener);
		
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	
	@Override
	protected void okPressed() {

		IFile iFile;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			buildProps.put(LOCAL_MODE, String.valueOf(btnLocalMode.getSelection()));
			buildProps.put(REMOTE_MODE, String.valueOf(btnRemoteMode.getSelection()));
			buildProps.put(HOST, textEdgeNode.getText());
			buildProps.put(USER_NAME, textUser.getText());
			buildProps.put(RUN_UTILITY, textRunUtility.getText());
			buildProps.put(JOB_XML, textJobXML.getText());
			buildProps.put(LIB_PATH, textLibs.getText());
			buildProps.put(PARAM_FILE, textParamFiles.getText());

			buildProps.store(out, null);

			String buildPropFilePath = buildPropFilePath();

			IPath bldPropPath =new Path(buildPropFilePath);
			iFile=ResourcesPlugin.getWorkspace().getRoot().getFile(bldPropPath);	
			iFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, null);

		} catch (IOException |CoreException  e) {
			MessageDialog.openError(new Shell(), "Error", "Exception occured while saving run configuration file -\n"+e.getMessage());
		}
		this.password = textPassword.getText();
		this.runGraph = true;
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		runGraph=false;
		super.cancelPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(380, 171);
	}

	public boolean proceedToRunGraph(){
		return runGraph;
	}
	
	SelectionListener selectionListener = new SelectionAdapter () {

		@Override
		public void widgetSelected(SelectionEvent event) {

			Button button = ((Button) event.widget);

			if(button.getText().equals("Local")){
				container.getShell().setSize(380,171);
				compositeServerDetails.setVisible(false);
				compositePathConfig.setVisible(false);

			}else if(button.getText().equals("Remote")){
				container.getShell().setSize(380,671);
				compositeServerDetails.setVisible(true);
				compositePathConfig.setVisible(true);
			}


		};
	};
}
