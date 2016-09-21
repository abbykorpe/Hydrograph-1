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

import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.Messages;
import hydrograph.ui.expression.editor.datastructure.ClassDetails;
import hydrograph.ui.expression.editor.datastructure.MethodDetails;
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class FunctionsUpperComposite extends Composite {
	private static final String TITLE = "Functions";
	private List methodList;
	private Text searchTextBox;
	private Browser descriptionStyledText;
	private List classNameList;
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public FunctionsUpperComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		Label lblFunctions = new Label(this, SWT.NONE);
		lblFunctions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblFunctions.setText(TITLE);

		createSearchTextBox(this);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setMethodList(List methodList) {
		this.methodList = methodList;
	}

	private void createSearchTextBox(Composite headerComposite) {
		searchTextBox = new Text(headerComposite, SWT.BORDER);
		GridData gd_searchTextBox = new GridData(SWT.RIGHT, SWT.CENTER, true, true, 0, 0);
		gd_searchTextBox.widthHint = 150;
		searchTextBox.setLayoutData(gd_searchTextBox);
		searchTextBox.setForeground(new Color(null, 128, 128, 128));
		searchTextBox.setText(Constants.DEFAULT_SEARCH_TEXT);
		addListnersToSearchTextBox();
		ExpressionEditorUtil.INSTANCE.addFocusListenerToSearchTextBox(searchTextBox);
	}

	private void addListnersToSearchTextBox() {
		searchTextBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!StringUtils.equals(Constants.DEFAULT_SEARCH_TEXT, searchTextBox.getText()) && classNameList.getSelectionCount()!=0) {
					methodList.removeAll();
					ClassDetails classDetails = (ClassDetails) methodList
							.getData(CategoriesComposite.KEY_FOR_ACCESSING_CLASS_FROM_METHOD_LIST);
					if (classDetails != null) {
						for (MethodDetails methodDetails : classDetails.getMethodList()) {
							if (StringUtils.containsIgnoreCase(methodDetails.getMethodName(), searchTextBox.getText())) {
								methodList.add(methodDetails.getSignature());
								methodList.setData(String.valueOf(methodList.getItemCount() - 1), methodDetails);
							}
						}
					}
					if(methodList.getItemCount()==0 && StringUtils.isNotBlank(searchTextBox.getText())){
						methodList.add(Messages.CANNOT_SEARCH_INPUT_STRING+searchTextBox.getText());
					}
					descriptionStyledText.setText(Constants.EMPTY_STRING);
				}
			}
		});
	}

	public void refresh() {
		searchTextBox.setText(Constants.DEFAULT_SEARCH_TEXT);
	}

	public void setDescriptionText(Browser descriptionStyledText) {
		this.descriptionStyledText=descriptionStyledText;
	}

	public void setClassNameList(List classNamelist) {
		this.classNameList=classNamelist;
	}

}
