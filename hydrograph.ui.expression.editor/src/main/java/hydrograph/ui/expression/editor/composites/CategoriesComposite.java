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

import hydrograph.ui.expression.editor.datastructure.ClassDetails;
import hydrograph.ui.expression.editor.datastructure.MethodDetails;
import hydrograph.ui.expression.editor.repo.ClassRepo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class CategoriesComposite extends Composite {
	private List classNamelist;
	private List methodList;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CategoriesComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		CategoriesUpperComposite categoriesUpperComposite = new CategoriesUpperComposite(this, SWT.BORDER);
		categoriesUpperComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		classNamelist= new List(this, SWT.BORDER);
		classNamelist.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		loadClassesFromRepo(classNamelist);
		addListnersToClassNameList(classNamelist);
	}
	
	private void loadClassesFromRepo(List classNamelist) {
		for(ClassDetails classDetails:ClassRepo.INSTANCE.getClassList()){
			classNamelist.add(classDetails.getcName());
			classNamelist.setData(String.valueOf(classNamelist.getItemCount()-1), classDetails);
		}
	}


	private void addListnersToClassNameList(final List classNamelist) {
		classNamelist.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClassDetails classDetails=(ClassDetails) classNamelist.getData(String.valueOf(classNamelist.getSelectionIndex()));
				methodList.removeAll();
				for(MethodDetails methodDetails:classDetails.getMethodList()){
					methodList.add(methodDetails.getSignature());
					methodList.setData(String.valueOf(methodList.getItemCount()-1), methodDetails);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setMethodList(List methodList) {
		this.methodList = methodList;
	}
}
