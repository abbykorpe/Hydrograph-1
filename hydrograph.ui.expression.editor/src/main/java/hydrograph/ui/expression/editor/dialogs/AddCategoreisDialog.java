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

import hydrograph.ui.expression.editor.composites.CategoriesDialogSourceComposite;
import hydrograph.ui.expression.editor.composites.CategoriesDialogTargetComposite;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class AddCategoreisDialog extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddCategoreisDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX|SWT.MIN|SWT.CLOSE);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite mainComposite = new Composite(container, SWT.BORDER);
		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		SashForm sashForm = new SashForm(mainComposite, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		CategoriesDialogSourceComposite categoriesDialogSourceComposite=new CategoriesDialogSourceComposite(sashForm, SWT.NONE);
		CategoriesDialogTargetComposite categoriesDialogTargetComposite=new CategoriesDialogTargetComposite(sashForm, SWT.NONE);
		
		sashForm.setWeights(new int[] {1, 1});

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
		return new Point(450, 300);
	}
	
	
	public static void main(String[] args) {
		AddCategoreisDialog dialog=new AddCategoreisDialog(new Shell());
		dialog.setShellStyle(SWT.MAX|SWT.MIN|SWT.CLOSE);
		dialog.open();
	}
}
