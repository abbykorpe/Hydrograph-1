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

import hydrograph.ui.expression.editor.Messages;
import hydrograph.ui.expression.editor.datastructure.MethodDetails;
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;

public class FunctionsComposite extends Composite {
	
	private List methodList;
	protected StyledText descriptionStyledText;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param categoriesComposite 
	 * @param style
	 */
	public FunctionsComposite(Composite parent, CategoriesComposite categoriesComposite, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		FunctionsUpperComposite composite = new FunctionsUpperComposite(this, SWT.BORDER);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 35;
		composite.setLayoutData(gd_composite);
		
		methodList = new List(this, SWT.BORDER|SWT.V_SCROLL);
		methodList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		addDragSupport();
	
		linkFunctionAndClassComposite(categoriesComposite);
		
		addListnersToMethodList(methodList);
	}


	private void addDragSupport() {
		ExpressionEditorUtil.INSTANCE.getDragSource(methodList).addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) { 
				MethodDetails methodDetails=(MethodDetails) methodList.getData(String.valueOf(methodList.getSelectionIndex()));
				event.data=methodDetails.getPlaceHolder();
			}
		});
	}


	private void linkFunctionAndClassComposite(CategoriesComposite categoriesComposite) {
		categoriesComposite.setMethodList(methodList);
	}

	private void addListnersToMethodList(final List methodsList) {
		methodsList.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				MethodDetails methodDetails=(MethodDetails) methodsList.getData(String.valueOf(methodsList.getSelectionIndex()));
				if (methodDetails != null && StringUtils.isNotBlank(methodDetails.getJavaDoc())) {
					descriptionStyledText.setText(methodDetails.getJavaDoc());
				} else {
					descriptionStyledText.setText(Messages.JAVA_DOC_NOT_AVAILABLE);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setDescriptionStyledText(StyledText descriptionStyledText){
		this.descriptionStyledText=descriptionStyledText;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
