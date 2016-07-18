
/********************************************************************************
* Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/

package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.propertywindow.widgets.customwidgets.mapping.tables.inputtable.TableContentProvider;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Button;

public class OperationClassDeleteDialog extends Dialog {

	private static final String DELETE_OPERATION = "Delete Operation";
	private Table table;
	private List<MappingSheetRow> mappingSheetRowList;
	private List<String> operationIdList = new ArrayList<>();
	List<String> checkedElements = new ArrayList<>();
	private ExpandBar expandBar;
	private CheckboxTableViewer checkboxTableViewer;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public OperationClassDeleteDialog(Shell parentShell, List<MappingSheetRow> mappingSheetRowList, ExpandBar expandBar) {
		super(parentShell);
		this.mappingSheetRowList = mappingSheetRowList;
		this.expandBar = expandBar;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText(DELETE_OPERATION);
		
		final Button selectAllCheckButton = new Button(container, SWT.CHECK);
		GridData gd_btnCheckButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCheckButton.widthHint = 190;
		selectAllCheckButton.setLayoutData(gd_btnCheckButton);
		selectAllCheckButton.setText("Select All");
		selectAllCheckButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selectAllCheckButton.getSelection())
				{
					checkboxTableViewer.setAllChecked(true);
				}	
				else
				{
					checkboxTableViewer.setAllChecked(false);
				}
			}
		});
		Composite composite = new Composite(container, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 204;
		gd_composite.widthHint = 402;
		composite.setLayoutData(gd_composite);

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION|SWT.MULTI);
		table = checkboxTableViewer.getTable();
		table.setBounds(0, 0, 227, 204);
		checkboxTableViewer.setContentProvider(new TableContentProvider());
		
		for (MappingSheetRow m : mappingSheetRowList) {
			operationIdList.add(m.getOperationID());
		}
		checkboxTableViewer.setInput(operationIdList);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
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
		return new Point(260, 344);
	}

	@Override
	protected void okPressed() {
		for (ExpandItem expandItem : expandBar.getItems()) {
			
			for (Object object : checkboxTableViewer.getCheckedElements()) {
				if (expandItem.getText().equals(object.toString())) {
					expandItem.setExpanded(false);
					for (int i = 0; i < mappingSheetRowList.size(); i++) {
						if (mappingSheetRowList.get(i).getOperationID().equals(object.toString())) {
							mappingSheetRowList.remove(i);
							break;
						}
					}
					expandItem.dispose();

					break;
				}
			}
		}
		super.okPressed();
	}
}
