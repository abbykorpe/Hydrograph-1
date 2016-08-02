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

package hydrograph.ui.expression.editor.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;

public class Test extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public Test(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX|SWT.CLOSE|SWT.RESIZE);
		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(container, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite = new Composite(sashForm, SWT.BORDER);
		composite.setLayout(new GridLayout(1, false));
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayout(new GridLayout(3, false));
		GridData gd_composite_3 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_composite_3.widthHint = 210;
		gd_composite_3.heightHint = 36;
		composite_3.setLayoutData(gd_composite_3);
		
		Label lblExpressionJars = new Label(composite_3, SWT.NONE);
		lblExpressionJars.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblExpressionJars.setText("Expression Jars");
		
		Button btnDel = new Button(composite_3, SWT.NONE);
		GridData gd_btnDel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDel.widthHint = 37;
		btnDel.setLayoutData(gd_btnDel);
		btnDel.setText("Del");
		
		Button btnAdd = new Button(composite_3, SWT.NONE);
		btnAdd.setText("Add");
		
		List list = new List(composite, SWT.BORDER);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_1 = new Composite(sashForm, SWT.BORDER);
		composite_1.setLayout(new GridLayout(1, false));
		
		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayout(new GridLayout(1, false));
		GridData gd_composite_4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_4.heightHint = 36;
		composite_4.setLayoutData(gd_composite_4);
		
		Label lblAvailablePackages = new Label(composite_4, SWT.NONE);
		lblAvailablePackages.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblAvailablePackages.setText("Available Packages");
		
		List list_1 = new List(composite_1, SWT.BORDER);
		list_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_2 = new Composite(sashForm, SWT.BORDER);
		composite_2.setLayout(new GridLayout(1, false));
		
		Composite composite_5 = new Composite(composite_2, SWT.NONE);
		composite_5.setLayout(new GridLayout(3, false));
		GridData gd_composite_5 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_5.heightHint = 36;
		composite_5.setLayoutData(gd_composite_5);
		
		Label lblSelectedPackages = new Label(composite_5, SWT.NONE);
		lblSelectedPackages.setText("Selected Packages");
		
		Button btnDel_1 = new Button(composite_5, SWT.NONE);
		GridData gd_btnDel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDel_1.widthHint = 37;
		btnDel_1.setLayoutData(gd_btnDel_1);
		btnDel_1.setText("Del");
		
		Button btnView = new Button(composite_5, SWT.NONE);
		btnView.setText("View");
		
		List list_2 = new List(composite_2, SWT.BORDER);
		list_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setWeights(new int[] {1, 1, 1});

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
		return new Point(630, 331);
	}
public static void main(String[] args) {
	Test test=new Test(new Shell());
	test.open();
	
}
}
