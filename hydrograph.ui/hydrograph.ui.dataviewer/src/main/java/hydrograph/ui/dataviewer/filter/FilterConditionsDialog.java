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
package hydrograph.ui.dataviewer.filter;

import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.dataviewer.adapters.DataViewerAdapter;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

public class FilterConditionsDialog extends Dialog {
	private static final String VALUE_TEXT_BOX = "valueTextBox";
	private static final String FIELD_NAMES = "fieldNames";
	private static final String RELATIONAL_OPERATORS = "relationalOperators";
	private static final String REMOVE = "-";
	private static final String ADD = "+";
	private static final String GROUP_CHECKBOX = "groupCheckBox";
	public static final String CONDITIONAL_OPERATORS = "conditionalOperators";
	public static final String ROW_INDEX = "rowIndex";
	
	private static final String ADD_BUTTON_PANE = "addButtonPane";
	private static final String REMOVE_BUTTON_PANE = "removeButtonPane";
	private static final String GROUP_CHECKBOX_PANE = "groupCheckBoxPane";
	private static final String RELATIONAL_COMBO_PANE = "relationalComboPane";
	private static final String FIELD_COMBO_PANE = "fieldComboPane";
	private static final String CONDITIONAL_COMBO_PANE = "conditionalComboPane";
	private static final String VALUE_TEXT_PANE = "valueTextPane";
	
	private static final String ADD_EDITOR = "add_editor";
	private static final String REMOVE_EDITOR = "remove_editor";
	private static final String GROUP_EDITOR = "group_editor";
	private static final String RELATIONAL_EDITOR = "relational_editor";
	private static final String FIELD_EDITOR = "field_editor";
	private static final String CONDITIONAL_EDITOR = "conditional_editor";
	private static final String VALUE_EDITOR = "vale_editor";
	
	private Map<String,String[]> typeBasedConditionalOperators = new HashMap<>();
	private FilterConditions originalFilterConditions;
	private RetainFilter retainLocalFilter;
	private RetainFilter retainRemoteFilter;
	
	private String relationalOperators[] = new String[]{"and", "or"};
	private String fieldNames[];
	private Map<String, String> fieldsAndTypes;
	private TableViewer remoteTableViewer;
	private TableViewer localTableViewer;
	
	private List<Condition> localConditionsList; 
	private List<Condition> remoteConditionsList; 
	//Map for adding group index with list of list of row indexes
	private TreeMap<Integer,List<List<Integer>>> groupSelectionMap;
	private DataViewerAdapter dataViewerAdapter;
	private DebugDataViewer debugDataViewer;
	private static final String REMOTE="Remote";
	private static final String LOCAL="local";
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterConditionsDialog.class);
	
	
	public void setFieldsAndTypes(Map<String, String> fieldsAndTypes) {
		this.fieldsAndTypes = fieldsAndTypes;
		fieldNames = (String[]) this.fieldsAndTypes.keySet().toArray(new String[this.fieldsAndTypes.size()]);
		Arrays.sort(fieldNames);
	}
	
	/**
	 * Create the dialog.	
	 * @param parentShell
	 */
	public FilterConditionsDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.RESIZE);
		localConditionsList = new ArrayList<>();
		remoteConditionsList = new ArrayList<>();
		groupSelectionMap = new TreeMap<>();
		typeBasedConditionalOperators = FilterHelper.INSTANCE.getTypeBasedOperatorMap();
	}

	public void setFilterConditions(FilterConditions filterConditions) {
		this.originalFilterConditions = filterConditions;
		localConditionsList.addAll(FilterHelper.INSTANCE.cloneList(filterConditions.getLocalConditions()));
		remoteConditionsList.addAll(FilterHelper.INSTANCE.cloneList(filterConditions.getRemoteConditions()));
		retainLocalFilter.setRetainFilter(originalFilterConditions.getRetainLocal());
		retainRemoteFilter.setRetainFilter(originalFilterConditions.getRetainRemote());
	}
	
	public FilterConditions getFilterConditions() {
		return originalFilterConditions;
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		parent.getShell().setText("Viewer");
		container.setLayout(new GridLayout(1, false));
		
		Composite mainComposite = new Composite(container, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		GridData gdMainComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdMainComposite.heightHint = 355;
		gdMainComposite.widthHint = 682;
		mainComposite.setLayoutData(gdMainComposite);
		
		TabFolder tabFolder = new TabFolder(mainComposite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createRemoteTabItem(tabFolder, remoteTableViewer);
		createLocalTabItem(tabFolder, localTableViewer);
		FilterHelper.INSTANCE.setDataViewerAdapter(dataViewerAdapter,this);
		FilterHelper.INSTANCE.setDebugDataViewer(debugDataViewer);
		FilterHelper.INSTANCE.setFilterType(REMOTE);
		tabFolder.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem tabItem = (TabItem) e.item;
				if (tabItem.getText().equalsIgnoreCase(LOCAL)) {
					FilterHelper.INSTANCE.setFilterType(LOCAL);
				} else {
					FilterHelper.INSTANCE.setFilterType(REMOTE);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		return container;
	}


	private void createRemoteTabItem(TabFolder tabFolder, TableViewer tableViewer) {
		TabItem tbtmLocal = new TabItem(tabFolder, SWT.NONE);
		tbtmLocal.setText("Remote");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmLocal.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		tableViewer.setContentProvider(new ArrayContentProvider());
		Table table = tableViewer.getTable();
		
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		TableViewerColumn addButtonTableViewerColumn = createTableColumns(tableViewer, "");
		addButtonTableViewerColumn.setLabelProvider(getAddButtonCellProvider(tableViewer, remoteConditionsList));
		
		TableViewerColumn removeButtonTableViewerColumn = createTableColumns(tableViewer, "");
		removeButtonTableViewerColumn.setLabelProvider(getRemoveButtonCellProvider(tableViewer, remoteConditionsList));
		
		TableViewerColumn groupButtonTableViewerColumn = createTableColumns(tableViewer, "Group");
		groupButtonTableViewerColumn.setLabelProvider(getGroupCheckCellProvider(tableViewer, remoteConditionsList));
		
		TableViewerColumn relationalDropDownColumn = createTableColumns(tableViewer, "Relational Operator");
		relationalDropDownColumn.setLabelProvider(getRelationalCellProvider(tableViewer, remoteConditionsList));
		
		
		TableViewerColumn fieldNameDropDownColumn = createTableColumns(tableViewer, "Field Name");
		fieldNameDropDownColumn.setLabelProvider(getFieldNamecellProvider(tableViewer, remoteConditionsList));
		
		TableViewerColumn conditionalDropDownColumn = createTableColumns(tableViewer, "Conditional Operator");
		conditionalDropDownColumn.setLabelProvider(getConditionalCellProvider(tableViewer, remoteConditionsList));
		
		TableViewerColumn valueTextBoxColumn = createTableColumns(tableViewer, "Value");
		valueTextBoxColumn.setLabelProvider(getValueCellProvider(tableViewer, remoteConditionsList));
		
		tableViewer.setInput(remoteConditionsList);
		remoteConditionsList.add(0, new Condition());
		tableViewer.refresh();
		
		
		Composite buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(6, false));
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Button retainButton = new Button(buttonComposite, SWT.CHECK);
		retainButton.setText("Retain Remote Filter");
		retainButton.addSelectionListener(FilterHelper.INSTANCE.getRetainButtonListener(retainRemoteFilter));
		
		Button btnAddGrp = new Button(buttonComposite, SWT.NONE);
		btnAddGrp.setText("Create Group");
		btnAddGrp.addSelectionListener(getAddGroupButtonListner(tableViewer));
		
		Button clearButton = new Button(buttonComposite, SWT.NONE);
		clearButton.setText("Clear");
		clearButton.addSelectionListener(FilterHelper.INSTANCE.getClearButtonListener(tableViewer, remoteConditionsList));
		
		Button btnOk = new Button(buttonComposite, SWT.NONE);
		btnOk.setText("OK");
		btnOk.addSelectionListener(FilterHelper.INSTANCE.getOkButtonListener(remoteConditionsList, fieldsAndTypes));
		
		Button btnCancel = new Button(buttonComposite, SWT.NONE);
		btnCancel.setText("Cancel");
		
		Button applyButton = new Button(buttonComposite, SWT.NONE);
		applyButton.setText("Apply");
		applyButton.addSelectionListener(FilterHelper.INSTANCE.getApplyButtonListener(originalFilterConditions, 
				remoteConditionsList, retainRemoteFilter));
	}

	private void createLocalTabItem(TabFolder tabFolder, TableViewer tableViewer) {
		TabItem tbtmLocal = new TabItem(tabFolder, SWT.NONE);
		tbtmLocal.setText("Local");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmLocal.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		tableViewer.setContentProvider(new ArrayContentProvider());
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		TableViewerColumn addButtonTableViewerColumn = createTableColumns(tableViewer, "");
		addButtonTableViewerColumn.setLabelProvider(getAddButtonCellProvider(tableViewer, localConditionsList));
		
		TableViewerColumn removeButtonTableViewerColumn = createTableColumns(tableViewer, "");
		removeButtonTableViewerColumn.setLabelProvider(getRemoveButtonCellProvider(tableViewer, localConditionsList));
		
		TableViewerColumn groupButtonTableViewerColumn = createTableColumns(tableViewer, "Group");
		groupButtonTableViewerColumn.setLabelProvider(getGroupCheckCellProvider(tableViewer, localConditionsList));
		
		TableViewerColumn relationalDropDownColumn = createTableColumns(tableViewer, "Relational Operator");
		relationalDropDownColumn.setLabelProvider(getRelationalCellProvider(tableViewer, localConditionsList));
		
		
		TableViewerColumn fieldNameDropDownColumn = createTableColumns(tableViewer, "Field Name");
		fieldNameDropDownColumn.setLabelProvider(getFieldNamecellProvider(tableViewer, localConditionsList));
		
		TableViewerColumn conditionalDropDownColumn = createTableColumns(tableViewer, "Conditional Operator");
		conditionalDropDownColumn.setLabelProvider(getConditionalCellProvider(tableViewer, localConditionsList));
		
		TableViewerColumn valueTextBoxColumn = createTableColumns(tableViewer, "Value");
		valueTextBoxColumn.setLabelProvider(getValueCellProvider(tableViewer, localConditionsList));
		
		tableViewer.setInput(localConditionsList);
		localConditionsList.add(0, new Condition());
		tableViewer.refresh();
		
		
		Composite buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(6, false));
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Button retainButton = new Button(buttonComposite, SWT.CHECK);
		retainButton.setText("Retain Local Filter");
		retainButton.addSelectionListener(FilterHelper.INSTANCE.getRetainButtonListener(retainLocalFilter));
		
		Button btnAddGrp = new Button(buttonComposite, SWT.NONE);
		btnAddGrp.setText("Create Group");
		btnAddGrp.addSelectionListener(getAddGroupButtonListner(tableViewer));
	
		Button clearButton = new Button(buttonComposite, SWT.NONE);
		clearButton.setText("Clear");
		clearButton.addSelectionListener(FilterHelper.INSTANCE.getClearButtonListener(tableViewer, localConditionsList));
		
		Button btnOk = new Button(buttonComposite, SWT.NONE);
		btnOk.setText("OK");
		btnOk.addSelectionListener(FilterHelper.INSTANCE.getOkButtonListener(localConditionsList, fieldsAndTypes));
		
		Button btnCancel = new Button(buttonComposite, SWT.NONE);
		btnCancel.setText("Cancel");
		
		Button applyButton = new Button(buttonComposite, SWT.NONE);
		applyButton.setText("Apply");
		applyButton.addSelectionListener(FilterHelper.INSTANCE.getApplyButtonListener(originalFilterConditions, 
				localConditionsList, retainLocalFilter));
	}

	private CellLabelProvider getValueCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_VALUE") == null) {
					item.setData("ADDED_VALUE", "TRUE");
					Text text = addTextBoxInTable(tableViewer, item, VALUE_TEXT_BOX, VALUE_TEXT_PANE, VALUE_EDITOR, cell.getColumnIndex(), 
							FilterHelper.INSTANCE.getTextBoxListener(conditionsList));
					text.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getValue());
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_VALUE") == null) {
								item.setData("DISPOSED_VALUE", "TRUE");
								Text valueText = (Text) item.getData(VALUE_TEXT_BOX);
								((TableEditor)valueText.getData(VALUE_EDITOR)).dispose();
								valueText.dispose();
								
								Composite composite = (Composite)item.getData(VALUE_TEXT_PANE);
								composite.dispose();
							}
						}
					});
				} else {
					Text text = (Text) item.getData(VALUE_TEXT_BOX);
					text.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getValue());
				}
			}
		};
	}

	private CellLabelProvider getConditionalCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_CONDITIONAL") == null) {
					item.setData("ADDED_CONDITIONAL", "TRUE");
					Combo combo = addComboInTable(tableViewer, item, CONDITIONAL_OPERATORS, CONDITIONAL_COMBO_PANE, CONDITIONAL_EDITOR,
							cell.getColumnIndex(), new String[]{}, 
							FilterHelper.INSTANCE.getConditionalOperatorSelectionListener(conditionsList));
					if(StringUtils.isNotBlank(conditionsList.get(tableViewer.getTable().indexOf(item)).getFieldName())){
						String fieldsName = conditionsList.get(tableViewer.getTable().indexOf(item)).getFieldName();
						combo.setItems(typeBasedConditionalOperators.get(fieldsAndTypes.get(fieldsName)));
					}
					else{
						combo.setItems(new String[]{});
					}
					combo.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getConditionalOperator());
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_CONDITIONAL") == null) {
								item.setData("DISPOSED_CONDITIONAL", "TRUE");
								Combo combo = (Combo) item.getData(CONDITIONAL_OPERATORS);
								((TableEditor)combo.getData(CONDITIONAL_EDITOR)).dispose();
								combo.dispose();
								
								Composite composite = (Composite)item.getData(CONDITIONAL_COMBO_PANE);
								composite.dispose();
							}
						}
					});
				}
				else{
					Combo combo = (Combo) item.getData(CONDITIONAL_OPERATORS);
					if(StringUtils.isNotBlank(conditionsList.get(tableViewer.getTable().indexOf(item)).getFieldName())){
						String fieldsName = conditionsList.get(tableViewer.getTable().indexOf(item)).getFieldName();
						combo.setItems(typeBasedConditionalOperators.get(fieldsAndTypes.get(fieldsName)));
					}
					else{
						combo.setItems(new String[]{});
					}
					combo.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getConditionalOperator());
				}
			}
		};
	}

	private CellLabelProvider getFieldNamecellProvider(final TableViewer tableViewer, final List<Condition> conditionsList) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_FIELD") == null) {
					item.setData("ADDED_FIELD", "TRUE");
					Combo combo = addComboInTable(tableViewer, item, FIELD_NAMES, FIELD_COMBO_PANE, FIELD_EDITOR,
							cell.getColumnIndex(), fieldNames, FilterHelper.INSTANCE.getFieldNameSelectionListener(tableViewer, 
									conditionsList, fieldsAndTypes, typeBasedConditionalOperators));
					combo.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getFieldName());
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_FIELD") == null) {
								item.setData("DISPOSED_FIELD", "TRUE");
								Combo combo = (Combo) item.getData(FIELD_NAMES);
								((TableEditor)combo.getData(FIELD_EDITOR)).dispose();
								combo.dispose();
								
								Composite composite = (Composite)item.getData(FIELD_COMBO_PANE);
								composite.dispose();
							}
						}
					});
				}
				else{
					Combo fieldNameCombo = (Combo) item.getData(FIELD_NAMES);
					fieldNameCombo.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getFieldName());
				}
			}
		};
	}

	private CellLabelProvider getRelationalCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_RELATIONAL") == null) {
					item.setData("ADDED_RELATIONAL", "TRUE");
					Combo combo = addComboInTable(tableViewer, item, RELATIONAL_OPERATORS, RELATIONAL_COMBO_PANE, RELATIONAL_EDITOR,
							cell.getColumnIndex(), relationalOperators,	
							FilterHelper.INSTANCE.getRelationalOpSelectionListener(conditionsList));
					combo.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getRelationalOperator());
					if(tableViewer.getTable().indexOf(item) == 0){
						combo.setVisible(false);
					}
					else {
						combo.setVisible(true);
					}
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_RELATIONAL") == null) {
								item.setData("DISPOSED_RELATIONAL", "TRUE");
								Combo combo = (Combo) item.getData(RELATIONAL_OPERATORS);
								((TableEditor)combo.getData(RELATIONAL_EDITOR)).dispose();
								combo.dispose();
								
								Composite composite = (Composite)item.getData(RELATIONAL_COMBO_PANE);
								composite.dispose();
							}
						}
					});
				}
				else{
					Combo combo = (Combo) item.getData(RELATIONAL_OPERATORS);
					combo.setText((conditionsList.get(tableViewer.getTable().indexOf(item))).getRelationalOperator());
				}
			}
		};
	}

	private CellLabelProvider getGroupCheckCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("UPDATED3") == null) {
					item.setData("UPDATED3", "TRUE");
				} else {
					return;
				}
				addCheckButtonInTable(tableViewer, item, GROUP_CHECKBOX, GROUP_CHECKBOX_PANE, GROUP_EDITOR, cell.getColumnIndex(), 
						FilterHelper.INSTANCE.removeButtonListener(tableViewer, conditionsList));
				item.addDisposeListener(new DisposeListener() {
					
					@Override
					public void widgetDisposed(DisposeEvent e) {
						Button groupButton = (Button) item.getData(GROUP_CHECKBOX);
						((TableEditor)groupButton.getData(GROUP_EDITOR)).dispose();
						groupButton.dispose();
						
						Composite composite = (Composite)item.getData(GROUP_CHECKBOX_PANE);
						composite.dispose();
					}
				});
			}
		};
	}

	private CellLabelProvider getRemoveButtonCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("UPDATED2") == null) {
					item.setData("UPDATED2", "TRUE");
				} else {
					return;
				}
				addButtonInTable(tableViewer, item, REMOVE, REMOVE_BUTTON_PANE, REMOVE_EDITOR, cell.getColumnIndex(), 
						FilterHelper.INSTANCE.removeButtonListener(tableViewer, conditionsList), ImagePathConstant.DELETE_BUTTON);
				item.addDisposeListener(new DisposeListener() {
					
					@Override
					public void widgetDisposed(DisposeEvent e) {
						Button removeButton = (Button) item.getData(REMOVE);
						((TableEditor)removeButton.getData(REMOVE_EDITOR)).dispose();
						removeButton.dispose();
						
						Composite composite = (Composite)item.getData(REMOVE_BUTTON_PANE);
						composite.dispose();
					}
				});
			}
		};
	}

	private CellLabelProvider getAddButtonCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("UPDATED1") == null) {
					item.setData("UPDATED1", "TRUE");
				} else {
					return;
				}
				addButtonInTable(tableViewer, item, ADD, ADD_BUTTON_PANE, ADD_EDITOR, cell.getColumnIndex(), 
						FilterHelper.INSTANCE.addButtonListener(tableViewer,conditionsList), ImagePathConstant.ADD_BUTTON);
				item.addDisposeListener(new DisposeListener() {
					
					@Override
					public void widgetDisposed(DisposeEvent e) {
						Button addButton = (Button) item.getData(ADD);
						((TableEditor)addButton.getData(ADD_EDITOR)).dispose();
						addButton.dispose();
						
						Composite composite = (Composite)item.getData(ADD_BUTTON_PANE);
						composite.dispose();
					}
				});
			}
			
		};
	}
	
	private TableViewerColumn createTableColumns(TableViewer tableViewer, String columnLabel) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText(columnLabel);
		return tableViewerColumn;
	}

	private Text addTextBoxInTable(TableViewer tableViewer, TableItem tableItem, String textBoxName, 
			String valueTextPane, String editorName, int columnIndex, Listener listener) {
		final Composite buttonPane = new Composite(tableViewer.getTable(), SWT.NONE);
		buttonPane.setLayout(new FillLayout());
		final Text text = new Text(buttonPane, SWT.NONE);
		text.addListener(SWT.Modify, listener);
		text.setData(ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		tableItem.setData(textBoxName, text);
		tableItem.setData(valueTextPane, buttonPane);
		text.addModifyListener(FilterHelper.INSTANCE.getTextModifyListener());
		
		final TableEditor editor = new TableEditor(tableViewer.getTable());
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		editor.setEditor(buttonPane, tableItem, columnIndex);
		editor.layout();
		text.setData(editorName, editor);
		return text;
	}
	
	private Combo addComboInTable(TableViewer tableViewer, TableItem tableItem, String comboName, String comboPaneName, 
			String editorName, int columnIndex,	String[] relationalOperators, SelectionListener dropDownSelectionListener) {
		final Composite buttonPane = new Composite(tableViewer.getTable(), SWT.NONE);
		buttonPane.setLayout(new FillLayout());
		final Combo combo = new Combo(buttonPane, SWT.NONE);
		combo.setItems(relationalOperators);
		combo.setData(ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		tableItem.setData(comboName, combo);
		tableItem.setData(comboPaneName, buttonPane);
		combo.addSelectionListener(dropDownSelectionListener);
		combo.addModifyListener(FilterHelper.INSTANCE.getComboModifyListener());
		new AutoCompleteField(combo, new ComboContentAdapter(), combo.getItems());
		
		final TableEditor editor = new TableEditor(tableViewer.getTable());
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		editor.setEditor(buttonPane, tableItem, columnIndex);
		editor.layout();
		combo.setData(editorName, editor);
		return combo;
	}

	private void addButtonInTable(TableViewer tableViewer, TableItem tableItem, String columnName, 
			String buttonPaneName, String editorName, int columnIndex, SelectionListener buttonSelectionListener,
			String imagePath) {
		final Composite buttonPane = new Composite(tableViewer.getTable(), SWT.NONE);
		buttonPane.setLayout(new FillLayout());
		final Button button = new Button(buttonPane, SWT.NONE);
		//button.setText(columnName);
		button.setData(ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		tableItem.setData(columnName, button);
		tableItem.setData(buttonPaneName, buttonPane);
		button.addSelectionListener(buttonSelectionListener);
		//TODO to be enabled once integrated
		//button.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + imagePath));
		
		final TableEditor editor = new TableEditor(tableViewer.getTable());
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		editor.setEditor(buttonPane, tableItem, columnIndex);
		editor.layout();
		button.setData(editorName, editor);
	}

	private void addCheckButtonInTable(TableViewer tableViewer, TableItem tableItem, String columnName, 
			String groupPaneName, String editorName, int columnIndex, SelectionListener buttonSelectionListener) {
		final Composite buttonPane = new Composite(tableViewer.getTable(), SWT.NONE);
		buttonPane.setLayout(new FillLayout());
		final Button button = new Button(buttonPane, SWT.CHECK);
		button.setData(ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		tableItem.setData(columnName, button);
		tableItem.setData(groupPaneName, buttonPane);
		
		final TableEditor editor = new TableEditor(tableViewer.getTable());
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		editor.setEditor(buttonPane, tableItem, columnIndex);
		editor.layout();
		button.setData(editorName, editor);
	}
	

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		//super.createButtonsForButtonBar(parent);
	}
	
private SelectionListener getAddGroupButtonListner(final TableViewer tableViewer) {
		
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
			
			      
				storeGroupSelection(tableViewer) ;              			     
			     
				GROUP_COLUMNS_COUNT++;
			    
			  
			    TableColumn[] columns = tableViewer.getTable().getColumns();
			   
			    TableItem[] items = tableViewer.getTable().getItems();
			   
			    for (int i = 0; i < items.length; i++) {
			    	items[i].dispose();
				}
			    
			    for (TableColumn tc : columns) {
			    	
			     tc.dispose();
							    	
				}
			    
			    redrawAllColumns(tableViewer);
			   			 
			      
			}
			
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		return listener;
	}

	
	
	private void redrawAllColumns(TableViewer tableViewer){
		
		TableViewerColumn addButtonTableViewerColumn = createTableColumns(
				tableViewer, "");
		addButtonTableViewerColumn.setLabelProvider(getAddButtonCellProvider(
				tableViewer, remoteConditionsList));

		TableViewerColumn removeButtonTableViewerColumn = createTableColumns(
				tableViewer, "");
		removeButtonTableViewerColumn
				.setLabelProvider(getRemoveButtonCellProvider(tableViewer,
						remoteConditionsList));

		TableViewerColumn groupButtonTableViewerColumn = createTableColumns(
				tableViewer, "Group");
		groupButtonTableViewerColumn
				.setLabelProvider(getGroupCheckCellProvider(tableViewer,
						remoteConditionsList));

		for(int i=0; i<GROUP_COLUMNS_COUNT; i++){
			TableViewerColumn dummyTableViewerColumn = createTableColumns(
					tableViewer, "");
			dummyTableViewerColumn
					.setLabelProvider(getDummyColumn(tableViewer,
							remoteConditionsList));	
		}
		
		TableViewerColumn relationalDropDownColumn = createTableColumns(
				tableViewer, "Relational Operator");
		relationalDropDownColumn.setLabelProvider(getRelationalCellProvider(
				tableViewer, remoteConditionsList));

		TableViewerColumn fieldNameDropDownColumn = createTableColumns(
				tableViewer, "Field Name");
		fieldNameDropDownColumn.setLabelProvider(getFieldNamecellProvider(
				tableViewer, remoteConditionsList));

		TableViewerColumn conditionalDropDownColumn = createTableColumns(
				tableViewer, "Conditional Operator");
		conditionalDropDownColumn.setLabelProvider(getConditionalCellProvider(
				tableViewer, remoteConditionsList));

		TableViewerColumn valueTextBoxColumn = createTableColumns(tableViewer,
				"Value");
		valueTextBoxColumn.setLabelProvider(getValueCellProvider(tableViewer,
				remoteConditionsList));
		
		tableViewer.refresh();
	}


	private void storeGroupSelection(TableViewer tableViewer){
		List<List<Integer>> grpList = new ArrayList<>();
		List<Integer> selectionList = new ArrayList<>();
		
		
		 TableItem[] items = tableViewer.getTable().getItems();
		
		   for (TableItem tableItem : items) {
			   Button button = (Button) tableItem.getData(GROUP_CHECKBOX);
			   if(button.getSelection()){
			   selectionList.add(tableViewer.getTable().indexOf(tableItem));
			   }
		}
		   
		   grpList.add(selectionList);
		   if(groupSelectionMap.isEmpty()){
			   groupSelectionMap.put(1, grpList); 
		   }else{
			   groupSelectionMap.put(groupSelectionMap.lastKey()+1,grpList);
		   }
		  
		
	}

	public void setDebugDataViewerAdapterAndViewer(DataViewerAdapter adapter, DebugDataViewer dataViewer)
			throws ClassNotFoundException, SQLException {
		dataViewerAdapter = adapter;
		debugDataViewer = dataViewer;
	}

	
}