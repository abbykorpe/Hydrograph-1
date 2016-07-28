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
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class AvailableFieldsComposite extends Composite {
	private static final String AVAILABLE_INPUT_FIELDS = "Selected Input Fields";
	private Table table;
	private TableColumn availableFieldsColumn ;
	private List<String> inputFields;
	private TableViewer tableViewer;
	private DragSource dragSource;
	private StyledText expressionEditor;
	private Text searchTextBox;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AvailableFieldsComposite(Composite parent, int style , StyledText expressionEditor,List<String> fieldNameList) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		this.inputFields=fieldNameList;
		this.expressionEditor=expressionEditor;
		Composite headerComposite = new Composite(this, SWT.NONE);
		headerComposite.setLayout(new GridLayout(2, false));
		GridData gd_headerComposite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_headerComposite.widthHint = 437;
		gd_headerComposite.heightHint = 39;
		headerComposite.setLayoutData(gd_headerComposite);
		
		Label lblAvailableFields = new Label(headerComposite, SWT.NONE);
		lblAvailableFields.setText(AVAILABLE_INPUT_FIELDS);
		
		createSearchTextBox(headerComposite);
		
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		availableFieldsColumn = new TableColumn(table, SWT.NONE);
		availableFieldsColumn.setWidth(100);
		
		
		ExpressionEditorUtil.INSTANCE.addDragSupport(table);

		loadData();
		addListners();
		
	}

	private void createSearchTextBox(Composite headerComposite) {
		searchTextBox = new Text(headerComposite, SWT.BORDER);
		GridData gd_searchTextBox = new GridData(SWT.RIGHT, SWT.CENTER, true, true, 0, 0);
		gd_searchTextBox.widthHint = 191;
		searchTextBox.setLayoutData(gd_searchTextBox);
		searchTextBox.setForeground(new Color(null,128,128,128));
		searchTextBox.setText(Constants.DEFAULT_SEARCH_TEXT);
		addListnersToSearchTextBox();
		ExpressionEditorUtil.INSTANCE.addFocusListenerToSearchTextBox(searchTextBox);
	}

	private void addListnersToSearchTextBox() {
		searchTextBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(!StringUtils.equals(Constants.DEFAULT_SEARCH_TEXT, searchTextBox.getText())){
				table.removeAll();
				for(String field:inputFields){
					if(StringUtils.containsIgnoreCase(field,searchTextBox.getText())){
						new TableItem(table, SWT.NONE).setText(field);
					}
				}
			}}
		});
	}

	private void addListners() {
		table.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				availableFieldsColumn.setWidth(table.getSize().x-4);
			}
		});
	}
	
	private void loadData() {
		if (inputFields != null) {
			for (String field : inputFields) {
				new TableItem(table, SWT.NONE).setText(field);
			}
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
