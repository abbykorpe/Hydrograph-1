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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class FunctionsUpperComposite extends Composite {

	private FunctionsComposite functionsComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FunctionsUpperComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		
		Label lblFunctions = new Label(this, SWT.NONE);
		lblFunctions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblFunctions.setText("Functions");
		

		this.functionsComposite=(FunctionsComposite) parent;		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}












//		Text searchText = new Text(this, SWT.NONE);
//		GridData gd_searchText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		gd_searchText.widthHint = 98;
//		searchText.setLayoutData(gd_searchText);