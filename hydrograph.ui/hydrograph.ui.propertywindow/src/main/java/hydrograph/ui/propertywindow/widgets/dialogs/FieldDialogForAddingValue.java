package hydrograph.ui.propertywindow.widgets.dialogs;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class FieldDialogForAddingValue extends Dialog {
	private Table table;
	
	
	
	
	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FieldDialogForAddingValue(Shell parentShell) {
		super(parentShell);
	}

	


	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
	
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite btnsComposite = new Composite(container, SWT.NONE);
		btnsComposite.setLayout(new GridLayout(2, false));
		GridData gd_btnsComposite = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnsComposite.heightHint = 35;
		btnsComposite.setLayoutData(gd_btnsComposite);
		
		Button btnAdd = new Button(btnsComposite, SWT.NONE);
		btnAdd.setBounds(0, 0, 75, 25);
		btnAdd.setText("Add");
		
		Button btnDel = new Button(btnsComposite, SWT.NONE);
		GridData gd_btnDel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDel.widthHint = 36;
		btnDel.setLayoutData(gd_btnDel);
		btnDel.setBounds(0, 0, 75, 25);
		btnDel.setText("Del");
		
		
		Composite tableComposite = new Composite(container, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1, false));
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewer tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn fieldColumn1 = tableViewerColumn.getColumn();
		fieldColumn1.setWidth(100);
		fieldColumn1.setText("Field1");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn fieldColumn2 = tableViewerColumn_1.getColumn();
		fieldColumn2.setWidth(100);
		fieldColumn2.setText("Field2");

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

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(548, 485);
	}
	
	
	public static void main(String[] args) {
		FieldDialogForAddingValue addingValue=new FieldDialogForAddingValue(new Shell());
		addingValue.open();
	}
}
