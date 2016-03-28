package com.bitwise.app.parametergrid.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Group;

public class ProjectParameterFileDialog extends Dialog {
	private Table table;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ProjectParameterFileDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new ColumnLayout());
		
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		composite.setLayout(gl_composite);
		ColumnLayoutData cld_composite = new ColumnLayoutData();
		cld_composite.heightHint = 408;
		composite.setLayoutData(cld_composite);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 400;
		gd_composite_1.widthHint = 446;
		composite_1.setLayoutData(gd_composite_1);
		
		Group grpProjectLevelParameter = new Group(composite_1, SWT.NONE);
		grpProjectLevelParameter.setLayout(new GridLayout(1, false));
		grpProjectLevelParameter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpProjectLevelParameter.setText("Project Level Parameter Files");
		
		TableViewer tableViewer = new TableViewer(grpProjectLevelParameter, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnFileName = tableViewerColumn.getColumn();
		tblclmnFileName.setWidth(189);
		tblclmnFileName.setText("File Name");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnPath = tableViewerColumn_1.getColumn();
		tblclmnPath.setWidth(235);
		tblclmnPath.setText("Path");
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.widthHint = 89;
		composite_2.setLayoutData(gd_composite_2);
		
		Button btnImport = new Button(composite_2, SWT.NONE);
		btnImport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnImport.setText("Import");
		
		Button btnExport = new Button(composite_2, SWT.NONE);
		btnExport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnExport.setText("Move Up");
		
		Button btnMoveDown = new Button(composite_2, SWT.NONE);
		btnMoveDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnMoveDown.setText("Move Down");
		
		Button btnRemove = new Button(composite_2, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemove.setText("Remove");

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
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(559, 497);
	}

}
