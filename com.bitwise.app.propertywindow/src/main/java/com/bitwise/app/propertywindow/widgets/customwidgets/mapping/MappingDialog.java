package com.bitwise.app.propertywindow.widgets.customwidgets.mapping;



import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures.MappingDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable.InputTable;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.mappingtable.MappingTable;
import com.bitwise.app.propertywindow.widgets.interfaces.IOperationClassDialog;

public class MappingDialog extends Dialog implements IOperationClassDialog{
	private InputTable inputTable;
	private MappingTable mappingTable;
	
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private ATMapping atMapping;
	private WidgetConfig widgetConfig;
	private MappingDialogButtonBar mappingDialogButtonBar;
	private String componentName;
	
	private boolean isOKButtonPressed=false;
	private boolean isCancelButtonPressed=false;
	
	public MappingDialog(Shell shell,
			PropertyDialogButtonBar propertyDialogButtonBar,
			ATMapping atMapping,
			WidgetConfig widgetConfig,String componeName) {
		super(shell);
		
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		this.atMapping = atMapping;
		this.widgetConfig = widgetConfig;
		this.componentName = componeName;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		OperationClassConfig operationClassConfig = (OperationClassConfig)widgetConfig;
		container.getShell().setText(operationClassConfig.getWindowTitle());
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		inputTable = createInputTable(composite);
		inputTable.setData(atMapping.getInputFields());
		
		mappingTable = createMappingTable(composite);
		mappingTable.setData(atMapping.getMappingSheetRows(),atMapping.getInputFields());

		return container;
	}

	private MappingTable createMappingTable(Composite composite) {
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_2.widthHint = 597;
		composite_2.setLayoutData(gd_composite_2);
		
		MappingTable mappingTable = new MappingTable(widgetConfig,
				propertyDialogButtonBar, mappingDialogButtonBar, componentName,
				this);
		mappingTable.createTable(composite_2);
		
		return mappingTable;

	}
	
	private InputTable createInputTable(Composite composite) {
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 405;
		gd_composite_1.widthHint = 222;
		composite_1.setLayoutData(gd_composite_1);
		
		InputTable inputTable = new InputTable();
		inputTable.createTable(composite_1);
		return inputTable;
		
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		
		mappingDialogButtonBar = new MappingDialogButtonBar();
		
		mappingDialogButtonBar.setOkButton(okButton);
		mappingDialogButtonBar.setCancelButton(cancelButton);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(840, 496);
	}
	
	@Override
	protected void okPressed() {
		if(mappingTable.isValidTable() && inputTable.isValidTable()){			
			atMapping = new ATMapping(inputTable.getData(), mappingTable.getData());
			super.okPressed();
		}else{
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );
			messageBox.setText(Messages.COULD_NOT_SAVE_PROPERTY_SHEET);
			messageBox.setMessage(Messages.INVALID_MAPPING);
			messageBox.open();
		}
		
	}

	public ATMapping getATMapping() {
		return atMapping;
	}

	@Override
	public void pressOK() {
		isOKButtonPressed=true;
		okPressed();
	}

	@Override
	public void pressCancel() {
		isCancelButtonPressed=true;
		cancelPressed();
	}

	/**
	 * 
	 * returns true if ok button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isOkPressed() {
		return isOKButtonPressed;
	}

	/**
	 * 
	 * returns true of cancel button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isCancelPressed() {
		return isCancelButtonPressed;
	}
}
