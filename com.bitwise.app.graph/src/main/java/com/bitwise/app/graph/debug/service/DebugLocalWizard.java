package com.bitwise.app.graph.debug.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.json.JSONObject;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.debug.api.DebugDataReader;
 
/**
 * @author Bitwise
 *
 */
public class DebugLocalWizard extends Dialog {

	Logger logger = LogFactory.INSTANCE.getLogger(DebugRemoteWizard.class);
	private static String[] columnValue;
	private static String[] objectValue;
	private List<String> debugArray;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DebugLocalWizard(Shell parentShell, List<String> debugArray) {
		super(parentShell);
		this.debugArray = debugArray;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Debug Wizard");
		container.setLayout(new GridLayout(2, false));
		
		Composite composite = new Composite(container, SWT.BORDER);
		composite.setLayout(new GridLayout(1, false));
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_composite.heightHint = 348;
		gd_composite.widthHint = 715;
		composite.setLayoutData(gd_composite);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scrolledComposite.heightHint = 318;
		gd_scrolledComposite.widthHint = 681;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Button btnNewButton = new Button(scrolledComposite, SWT.BORDER);
		btnNewButton.setText("New Button");
		final Table table = new Table(scrolledComposite, SWT.BORDER|SWT.FULL_SELECTION);
		scrolledComposite.setContent(table);
		scrolledComposite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		 
		try {
			 
			String obj = debugArray.get(0).toString();
			
			JSONObject jsonObject = new JSONObject(debugArray.get(0).toString());
			 
			columnValue = new String[jsonObject.length()];
			objectValue = new String[jsonObject.length()];
			for(int i=0; i<jsonObject.length();i++){
				JSONObject jsonOb = new JSONObject(obj);
				 String data = (String) jsonOb.names().get(i);
				 columnValue[i] = data;
			}
			createTableColumns(table, columnValue, 132);
			
			for(int i = 0; i < debugArray.size(); i++){
				TableItem tableItem = new TableItem(table, SWT.None);
				for(int j=0;j<jsonObject.length();j++){
					JSONObject jsonObj = new JSONObject(debugArray.get(i).toString());
					 
					String data = jsonObj.names().getString(j);
					objectValue[j] = jsonObj.getString(data);
				}	
				tableItem.setText(objectValue);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return container;
	}

	public void createTableColumns(Table table, String[] fields, int width) {
		for (String field : fields) {
			TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
			tableColumn.setText(field);
			tableColumn.setWidth(width);
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

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
		return new Point(735, 442);
	}

	/*public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException, IOException {
		// :/debug01, jobid: TestDebugMarch_Job_1_641084634_1457447040075, componetid: IFDelimited_01, socketid: out0
		DebugDataReader dataReader = new DebugDataReader("C:/debug01", "TestDebugMarch_Job_1_641084634_1457447040075", "IFDelimited_01", "out0");
		while(dataReader.hasNext()){
			System.out.println(dataReader.next().toString());
		}
	}*/

}
