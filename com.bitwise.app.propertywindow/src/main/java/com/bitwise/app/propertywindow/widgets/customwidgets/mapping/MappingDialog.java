package com.bitwise.app.propertywindow.widgets.customwidgets.mapping;



import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable.InputTable;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.mappingtable.MappingTable;

public class MappingDialog extends Dialog {
	InputTable inputTable;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MappingDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		inputTable = createInputTable(composite);
		
		createMappingTable(composite);

		return container;
	}

	private void createMappingTable(Composite composite) {
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_2.widthHint = 597;
		composite_2.setLayoutData(gd_composite_2);
		
		MappingTable mappingTable = new MappingTable();
		mappingTable.createTable(composite_2);

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
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
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
/*
		System.out.println(table_1.getData());
		TableItem[] items = table_1.getItems();
		
		
		for(TableItem item : items){
			RowData data = (RowData)item.getData();
			System.out.println(data);	
		}
		
		*/
		System.out.println("Table validation : " + inputTable.isValidTable());
		super.okPressed();
	}
	
}
