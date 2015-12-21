package com.bitwise.app.propertywindow.runconfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;



public class RunConfigDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text textEdgeNode;
	private Text textUser;
	private Text textPassword;
	
	Composite compositeServerDetails;
	Button btnLocalMode, btnRemoteMode;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public RunConfigDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		container.getShell().setText("Run Configuration Settings");

		new Label(container, SWT.NONE);

		Label lblRunConfigSettings = new Label(container, SWT.CENTER);

		FontDescriptor boldDescriptor = FontDescriptor.createFrom(lblRunConfigSettings.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(lblRunConfigSettings.getDisplay());
		lblRunConfigSettings.setFont( boldFont );

		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 249;
		lblRunConfigSettings.setLayoutData(gd_lblNewLabel);
		formToolkit.adapt(lblRunConfigSettings, true, true);
		lblRunConfigSettings.setText("Run Configuration settings");
		new Label(container, SWT.NONE);



		Composite compositeRunMode = new Composite(container, SWT.BORDER);
		GridData gd_compositeRunMode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compositeRunMode.heightHint = 80;
		gd_compositeRunMode.widthHint = 343;
		compositeRunMode.setLayoutData(gd_compositeRunMode);
		formToolkit.adapt(compositeRunMode);
		formToolkit.paintBordersFor(compositeRunMode);

		Label lblRunMode = new Label(compositeRunMode, SWT.NONE);
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
		gd_compositeServerDetails.widthHint = 345;
		gd_compositeServerDetails.heightHint = 162;
		compositeServerDetails.setLayoutData(gd_compositeServerDetails);
		formToolkit.adapt(compositeServerDetails);
		formToolkit.paintBordersFor(compositeServerDetails);

		Label lblServerDetails = new Label(compositeServerDetails, SWT.NONE);
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

		textUser = new Text(compositeServerDetails, SWT.BORDER);
		textUser.setBounds(110, 73, 206, 21);
		formToolkit.adapt(textUser, true, true);

		textPassword = new Text(compositeServerDetails, SWT.PASSWORD|SWT.BORDER);
		textPassword.setBounds(110, 116, 206, 21);
		formToolkit.adapt(textPassword, true, true);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void okPressed() {
		
		ContainerSelectionDialog containerSelectionDialog=new ContainerSelectionDialog(Display.getCurrent().getActiveShell(), null, true, null);
		containerSelectionDialog.open();
		Object[] projectName = containerSelectionDialog.getResult();
		
		if (projectName.length == 1) {
			System.out.println("result[0] "+projectName[0].toString()+"//"+"build.properties");
		}
		
		IPath iPath=new Path("/"+projectName[0].toString()+"/build.properties");
		IFile iFile=ResourcesPlugin.getWorkspace().getRoot().getFile(iPath);		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	
		try {
			if(btnLocalMode.getSelection()==true)
				out.write(("Local: "+btnLocalMode.getSelection()).getBytes());
			else{
				out.write(("Remote: "+btnRemoteMode.getSelection()).getBytes());
				out.write(("\nEdge node: "+textEdgeNode.getText()).getBytes());
				out.write(("\nUser: "+textUser.getText()).getBytes());
				out.write(("\nPassword: "+textPassword.getText()).getBytes());
			}

			out.close();

			iFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, null);
		} catch (IOException |CoreException e) {
			MessageDialog.openError(new Shell(), "Error", "Exception occured while saving run configuration file -\n"+e.getMessage());
		}
		
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		System.out.println("Cancel");
		super.cancelPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(424, 426);
	}


	SelectionListener selectionListener = new SelectionAdapter () {

		@Override
		public void widgetSelected(SelectionEvent event) {
			Button button = ((Button) event.widget);

			if(button.getText().equals("Local")){

				compositeServerDetails.setVisible(false);
			}else if(button.getText().equals("Remote")){

				compositeServerDetails.setVisible(true);
			}


		};
	};

	public static void main(String[] args) {
		Display dis = new Display();
		Shell sh = new Shell(dis);
		RunConfigDialog dia = new RunConfigDialog(sh);
		dia.open();
	}
}
