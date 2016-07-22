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

import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

public class CategoriesDialogTargetComposite extends Composite {
	private List targetList;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CategoriesDialogTargetComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite upperComposite = new Composite(this, SWT.NONE);
		upperComposite.setLayout(new GridLayout(2, false));
		GridData gd_upperComposite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_upperComposite.heightHint = 34;
		upperComposite.setLayoutData(gd_upperComposite);
		
		Label lblSelectedCategories = new Label(upperComposite, SWT.NONE);
		lblSelectedCategories.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblSelectedCategories.setText("Selected Categories");
		
		createDelButton(upperComposite);
		
		
		targetList= new List(this, SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION);
		targetList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		addDropSupport();
	}

	private void createDelButton(Composite upperComposite) {
		Button deleteButton = new Button(upperComposite, SWT.NONE);
		deleteButton.setBounds(0, 0, 75, 25);
		deleteButton.setText("Del");
		deleteButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(String field:targetList.getSelection()){
					targetList.remove(field);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void addDropSupport() {
		DropTarget dropTarget = new DropTarget(targetList, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				for (String fieldName :ExpressionEditorUtil.INSTANCE.getformatedData((String) event.data)){
					targetList.add(fieldName);
				}
			}
		});
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
