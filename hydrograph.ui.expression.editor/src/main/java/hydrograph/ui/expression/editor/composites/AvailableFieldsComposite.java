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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class AvailableFieldsComposite extends Composite {
	private static final String AVAILABLE_INPUT_FIELDS = "Selected Input Fields";
	private Table table;
	private TableColumn availableFieldsColumn ;
	private List<String> inputFields;
	private TableViewer tableViewer;
	private DragSource dragSource;
	private StyledText expressionEditor;
	
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
		GridData gd_headerComposite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_headerComposite.widthHint = 437;
		gd_headerComposite.heightHint = 39;
		headerComposite.setLayoutData(gd_headerComposite);
		
		Label lblAvailableFields = new Label(headerComposite, SWT.NONE);
		lblAvailableFields.setBounds(10, 10, 417, 19);
		lblAvailableFields.setText(AVAILABLE_INPUT_FIELDS);
		
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
