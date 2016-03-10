package com.bitwise.app.graph.debug.service;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
 

/**
 * @author Bitwise
 *
 */
public class DebugRemoteWizard extends Dialog {
	 
	Logger logger = LogFactory.INSTANCE.getLogger(DebugRemoteWizard.class);
	private static String[] columnValue;
	private static String[] objectValue;
	private JSONObject jsonObj;
	private JSONArray jsonArray;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DebugRemoteWizard(Shell parentShell, JSONArray jsonArray) {
		super(parentShell);
		this.jsonArray = jsonArray;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent){
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
			
			jsonObj = jsonArray.getJSONObject(0);
			columnValue = new String[jsonObj.length()];
			objectValue = new String[jsonObj.length()];
			
			for(int j=0; j<jsonObj.length(); j++){
				jsonObj = jsonArray.getJSONObject(0);
				String data = jsonObj.names().getString(j);
				columnValue[j] = data;
			}
			createTableColumns(table, columnValue, 132);
			for(int i = 0; i < jsonArray.length(); i++){
				TableItem tableItem = new TableItem(table, SWT.None);
				for(int j=0;j<jsonObj.length();j++){
					jsonObj = jsonArray.getJSONObject(i);
					String data = jsonObj.names().getString(j);
					objectValue[j] = jsonObj.getString(data);
				}	
				
					tableItem.setText(objectValue);
				}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
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
		return new Point(733, 443);
	}
	
}
