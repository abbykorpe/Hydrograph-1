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
package hydrograph.ui.propertywindow.widgets.customwidgets.sql;

import hydrograph.ui.datastructure.property.QueryProperty;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The class QueryDialog
 * 
 * @author Bitwise
 * 
 */
public class QueryDialog extends Dialog{
	private Text text;
	private QueryProperty query;
	private QueryProperty oldQuery;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	
	public QueryDialog(Shell parentShell,
			PropertyDialogButtonBar propertyDialogButtonBar, QueryProperty query) {
		super(parentShell);
		this.query = query;
		this.oldQuery = query.clone();
		this.propertyDialogButtonBar = propertyDialogButtonBar;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(container, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 291;
		gd_composite.widthHint = 663;
		composite.setLayoutData(gd_composite);
		
		text = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setBounds(10, 24, 643, 248);
		//null check
		if( query != null){
			text.setText(query.getQueryText());
		}
		return super.createDialogArea(parent);
	}
	
	@Override
	protected void okPressed() {
		query.setQueryText(text.getText());
		if(!oldQuery.equals(query)){
			propertyDialogButtonBar.enableApplyButton(true);
			
		}
		super.okPressed();
	}
}
