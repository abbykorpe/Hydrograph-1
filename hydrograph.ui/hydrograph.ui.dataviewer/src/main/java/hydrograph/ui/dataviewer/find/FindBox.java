package hydrograph.ui.dataviewer.find;

import hydrograph.ui.dataviewer.CSVDataViewer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class FindBox extends Dialog {
	private Text text;
	private CSVDataViewer csvDataViewer;
	private int findRowIndex=0;
	private int findColIndex=0;
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param csvDataViewer 
	 */
	public FindBox(Shell parentShell, CSVDataViewer csvDataViewer) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE );
		this.csvDataViewer = csvDataViewer;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFind = new Label(composite_1, SWT.NONE);
		lblFind.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFind.setText("Find");
		
		text = new Text(composite_1, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Button btnPrevious = new Button(composite_2, SWT.NONE);
		btnPrevious.setBounds(0, 0, 75, 25);
		btnPrevious.setText("Previous");
		
		Button btnNext = new Button(composite_2, SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(csvDataViewer.getTableViewer().getData("SELECTED_ROW_INDEX")!=null){
					TableItem previousSelectedTableItem=csvDataViewer.getTableViewer().getTable().getItem((int) csvDataViewer.getTableViewer().getData("SELECTED_ROW_INDEX"));
					
					int colIndex=(int) csvDataViewer.getTableViewer().getData("SEELCTED_COLUMN_INDEX");
					previousSelectedTableItem.setBackground(colIndex, Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				}
				
				Table table = csvDataViewer.getTableViewer().getTable();
				TableItem[] tableItems = table.getItems();
				
				for(;findRowIndex<tableItems.length;findRowIndex++){
					TableItem tableItem = tableItems[findRowIndex];
					for(;findColIndex<table.getColumnCount();findColIndex++){
						if(text.getText().equals(tableItem.getText(findColIndex))){
							table.showItem(tableItem);
							table.showColumn(table.getColumn(findColIndex));
							//table.setSelection(findColIndex);
							//csvDataViewer.getTableViewer().getTable().sho
							tableItem.setBackground(findColIndex,Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
							csvDataViewer.getTableViewer().setData("SELECTED_ROW_INDEX", findRowIndex);
							csvDataViewer.getTableViewer().setData("SEELCTED_COLUMN_INDEX", findColIndex);
							findColIndex++;
							return;
						}
					}
					
					findColIndex=0;
				}
			}
		});
		btnNext.setText("Next");

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
		return new Point(517, 168);
	}
	
	@Override
	public boolean close() {
		csvDataViewer.getTableViewer().setData("SELECTED_ROW_INDEX", null);
		csvDataViewer.getTableViewer().setData("SEELCTED_COLUMN_INDEX", null);
		return super.close();
	}
}
