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

package hydrograph.ui.expression.editor.composites;

import hydrograph.ui.expression.editor.dialogs.AddCategoreisDialog;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

public class CategoriesUpperComposite extends Composite {
	private static final String ADD_CATEGORIES = "Add Categories";
	private CategoriesComposite categoriesComposite;
	private Button btnAddPackages;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CategoriesUpperComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		Label lblCategories = new Label(this, SWT.NONE);
		lblCategories.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblCategories.setText("Categories");
		
		btnAddPackages = new Button(this, SWT.NONE);
		btnAddPackages.setText(ADD_CATEGORIES);
		btnAddPackages.setVisible(true);
		
		addListnersToAddPackageButton();

	}

	private void addListnersToAddPackageButton() {
		btnAddPackages.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddCategoreisDialog addCategoreisDialog=new AddCategoreisDialog(Display.getCurrent().getActiveShell());
				addCategoreisDialog.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
