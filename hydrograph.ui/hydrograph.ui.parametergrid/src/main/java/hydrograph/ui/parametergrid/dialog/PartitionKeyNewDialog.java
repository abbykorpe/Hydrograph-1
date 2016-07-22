package hydrograph.ui.parametergrid.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class PartitionKeyNewDialog extends Dialog {
	private Table table;
	private Table table_1;
	private Table table_2;
	private TableViewer	lowerTableViewer;
	private int count=0;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PartitionKeyNewDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite upperComposite = new Composite(sashForm, SWT.BORDER);
		upperComposite.setLayout(new GridLayout(1, false));
		
		Composite buttonsComposite = new Composite(upperComposite, SWT.NONE);
		buttonsComposite.setSize(604, 23);
		buttonsComposite.setLayout(new GridLayout(34, false));
		GridData gd_buttonsComposite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_buttonsComposite.widthHint = 550;
		gd_buttonsComposite.heightHint = 28;
		buttonsComposite.setLayoutData(gd_buttonsComposite);
		
		
		Button btnNewButton = new Button(buttonsComposite,SWT.RIGHT|SWT.CHECK);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				count=3;
				for (int i = 0; i < count; i++) {

					TableViewerColumn tableViewerColumn = new TableViewerColumn(
							lowerTableViewer, SWT.NONE);
					TableColumn fieldColumn = tableViewerColumn.getColumn();
					fieldColumn.setWidth(100);
					fieldColumn.setText("column_" + i);

				}
				lowerTableViewer.refresh();
				
			}
		});
		btnNewButton.setText("Add Key Values");
		
	
		
		SashForm sashForm_1 = new SashForm(upperComposite, SWT.NONE);
		sashForm_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm_1.setSize(618, 168);
		
		table_1 = new Table(sashForm_1, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		table = new Table(sashForm_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		sashForm_1.setWeights(new int[] {299, 302});
		
		Composite lowerComposite = new Composite(sashForm, SWT.BORDER);
		lowerComposite.setLayout(new GridLayout(1, false));
		
		Composite lowerButtoncomposite = new Composite(lowerComposite, SWT.NONE);
		GridData gd_lowerButtoncomposite = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_lowerButtoncomposite.heightHint = 30;
		gd_lowerButtoncomposite.widthHint = 324;
		lowerButtoncomposite.setLayoutData(gd_lowerButtoncomposite);
		
		Button btnNewButton_1 = new Button(lowerButtoncomposite, SWT.NONE);
		btnNewButton_1.setBounds(519, 0, 75, 25);
		btnNewButton_1.setText("Add Row");
		
		Button btnNewButton_2 = new Button(lowerButtoncomposite, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton_2.setBounds(438, 0, 75, 25);
		btnNewButton_2.setText("Delete Row");
		
		createLowerTable(lowerComposite);
		
		
		
		sashForm.setWeights(new int[] {1, 1});
		
		return container;
	}

	private void createLowerTable(Composite lowerComposite) {
		
		lowerTableViewer = new TableViewer(lowerComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		lowerTableViewer.setContentProvider(new ArrayContentProvider());
		
		table_2 = lowerTableViewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		table_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
	 
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
		return new Point(660, 661);
	}
	
	public static void main(String[] args) {
		PartitionKeyNewDialog dialog=new PartitionKeyNewDialog(new Shell());
		dialog.open();
	}
}
