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
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.dataviewer.adapters.DataViewerAdapter;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import hydrograph.ui.dataviewer.actions.ReloadAction;

/**
 * Dialog for Data Viewer Filter conditions
 * @author Bitwise
 *
 */
public class FilterConditionsDialog extends Dialog {
	private static final String TRUE = "TRUE";

	private static final String VALUE1_TEXT_BOX = "value1TextBox";
	private static final String VALUE2_TEXT_BOX = "value2TextBox";
	private static final String FIELD_NAMES = "fieldNames";
	private static final String RELATIONAL_OPERATORS = "relationalOperators";
	private static final String REMOVE = "-";
	private static final String ADD = "+";
	public static final String GROUP_CHECKBOX = "groupCheckBox";
	public static final String CONDITIONAL_OPERATORS = "conditionalOperators";
	
	private static final String ADD_BUTTON_PANE = "addButtonPane";
	private static final String REMOVE_BUTTON_PANE = "removeButtonPane";
	private static final String GROUP_CHECKBOX_PANE = "groupCheckBoxPane";
	private static final String RELATIONAL_COMBO_PANE = "relationalComboPane";
	private static final String FIELD_COMBO_PANE = "fieldComboPane";
	private static final String CONDITIONAL_COMBO_PANE = "conditionalComboPane";
	private static final String VALUE1_TEXT_PANE = "value1TextPane";
	private static final String VALUE2_TEXT_PANE = "value2TextPane";
	
	private static final String ADD_EDITOR = "add_editor";
	private static final String REMOVE_EDITOR = "remove_editor";
	private static final String GROUP_EDITOR = "group_editor";
	private static final String RELATIONAL_EDITOR = "relational_editor";
	private static final String FIELD_EDITOR = "field_editor";
	private static final String CONDITIONAL_EDITOR = "conditional_editor";
	private static final String VALUE1_EDITOR = "value1_editor";
	private static final String VALUE2_EDITOR = "value2_editor";
	
	private Map<String,String[]> typeBasedConditionalOperators = new HashMap<>();
	private FilterConditions originalFilterConditions;
	private RetainFilter retainLocalFilter;
	private RetainFilter retainRemoteFilter;
	
	private String relationalOperators[] = new String[]{FilterConstants.AND, FilterConstants.OR};
	private String fieldNames[];
	private Map<String, String> fieldsAndTypes;
	private TableViewer remoteTableViewer;
	private TableViewer localTableViewer;
	
	private List<Condition> localConditionsList; 
	private List<Condition> remoteConditionsList; 
	private List<Condition> dummyList = new ArrayList<>();
	
	//Map for adding group index with list of list of row indexes
	private TreeMap<Integer,List<List<Integer>>> localGroupSelectionMap;
	private TreeMap<Integer,List<List<Integer>>> remoteGroupSelectionMap;
	
	
	private DataViewerAdapter dataViewerAdapter;
	private DebugDataViewer debugDataViewer;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterConditionsDialog.class);

	Button localOkButton;
	Button localApplyButton;
	Button remoteOkButton;
	Button remoteApplyButton;
	Button remoteBtnAddGrp;
	Button localBtnAddGrp;
	
	public void setFieldsAndTypes(Map<String, String> fieldsAndTypes) {
		this.fieldsAndTypes = fieldsAndTypes;
		fieldNames = (String[]) this.fieldsAndTypes.keySet().toArray(new String[this.fieldsAndTypes.size()]);
		Arrays.sort(fieldNames, new Comparator<String>() {
			public int compare(String string1, String string2) {
				return string1.compareToIgnoreCase(string2);
			};
		});
	}
	
	/**
	 * Create the dialog.	
	 * @param parentShell
	 */
	public FilterConditionsDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.RESIZE|SWT.APPLICATION_MODAL);
		localConditionsList = new ArrayList<>();
		remoteConditionsList = new ArrayList<>();
		localGroupSelectionMap = new TreeMap<>();
		remoteGroupSelectionMap = new TreeMap<>();		
		retainLocalFilter= new RetainFilter();
		retainRemoteFilter= new RetainFilter();
		typeBasedConditionalOperators = FilterHelper.INSTANCE.getTypeBasedOperatorMap();
		this.originalFilterConditions = new FilterConditions();
	}

	public void setFilterConditions(FilterConditions filterConditions) {
		this.originalFilterConditions = filterConditions;
		localConditionsList.addAll(FilterHelper.INSTANCE.cloneList(filterConditions.getLocalConditions()));
		remoteConditionsList.addAll(FilterHelper.INSTANCE.cloneList(filterConditions.getRemoteConditions()));
		retainLocalFilter.setRetainFilter(filterConditions.getRetainLocal());
		retainRemoteFilter.setRetainFilter(filterConditions.getRetainRemote());
		localGroupSelectionMap.putAll(filterConditions.getLocalGroupSelectionMap());
		remoteGroupSelectionMap.putAll(filterConditions.getRemoteGroupSelectionMap());
	
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		parent.getShell().setText(Messages.DATA_VIEWER + " " + Messages.FILTER);
		container.setLayout(new GridLayout(1, false));
		
		Composite mainComposite = new Composite(container, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		GridData gdMainComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdMainComposite.heightHint = 355;
		gdMainComposite.widthHint = 832;
		mainComposite.setLayoutData(gdMainComposite);
		
		TabFolder tabFolder = new TabFolder(mainComposite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createRemoteTabItem(tabFolder, remoteTableViewer);
		createLocalTabItem(tabFolder, localTableViewer);
		FilterHelper.INSTANCE.setDataViewerAdapter(dataViewerAdapter,this);
		FilterHelper.INSTANCE.setDebugDataViewer(debugDataViewer);
		return container;
	}


	private void createRemoteTabItem(TabFolder tabFolder, TableViewer tableViewer) {
		TabItem tbtmLocal = new TabItem(tabFolder, SWT.NONE);
		tbtmLocal.setText(Messages.ORIGINAL_DATASET);
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmLocal.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		tableViewer.setContentProvider(new ArrayContentProvider());
		Table table = tableViewer.getTable();
		
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayout(new GridLayout(4, false));
        
        Button btnAddRowAt = new Button(composite_3, SWT.NONE);
        btnAddRowAt.addSelectionListener(FilterHelper.INSTANCE.getAddAtEndListener(tableViewer, remoteConditionsList, dummyList));
        btnAddRowAt.setText(Messages.ADD_ROW_AT_END);
        
        remoteBtnAddGrp = new Button(composite_3, SWT.NONE);
        Button clearGroupsRemote=new Button(composite_3,SWT.NONE);
        clearGroupsRemote.addSelectionListener(clearGroupsListner(tableViewer, remoteGroupSelectionMap, clearGroupsRemote,remoteBtnAddGrp,remoteConditionsList,true));
        clearGroupsRemote.setText(Messages.CLEAR_GROUPS);
        if(remoteGroupSelectionMap.size()!=0 &&  remoteConditionsList.size() !=0)
		{
			clearGroupsRemote.setEnabled(true);
		} else {
			clearGroupsRemote.setEnabled(false);
		}
        
        
        remoteBtnAddGrp.setText(Messages.CREATE_GROUP);		
        remoteBtnAddGrp.setEnabled(false);
        remoteBtnAddGrp.addSelectionListener(getAddGroupButtonListner(tableViewer,clearGroupsRemote,remoteConditionsList, remoteBtnAddGrp,remoteGroupSelectionMap));
		
		Button retainButton = new Button(composite_3, SWT.CHECK);
		retainButton.setText(Messages.RETAIN_REMOTE_FILTER);
		retainButton.addSelectionListener(FilterHelper.INSTANCE.getRetainButtonListener(retainRemoteFilter));
		
		Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setLayout(new GridLayout(4, false));
		composite_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				
						
		remoteOkButton = new Button(composite_4, SWT.NONE);
		remoteOkButton.setText(Messages.OK2);
		
		remoteApplyButton = new Button(composite_4, SWT.NONE);
		remoteApplyButton.setText(Messages.APPLY);
		
		Button btnCancel = new Button(composite_4, SWT.NONE);
		btnCancel.setText(Messages.CANCEL2);
		btnCancel.addMouseListener(getCancelButtonListener());
		
		Button clearButton = new Button(composite_4, SWT.NONE);
		clearButton.setText(Messages.CLEAR);
		clearButton.addSelectionListener(getClearButtonListener(tableViewer, remoteConditionsList, dummyList, originalFilterConditions, true, retainButton, remoteGroupSelectionMap,remoteBtnAddGrp));

		remoteApplyButton.addSelectionListener(FilterHelper.INSTANCE.getRemoteApplyButtonListener(originalFilterConditions, 
		remoteConditionsList, retainRemoteFilter));
		remoteOkButton.addSelectionListener(FilterHelper.INSTANCE.getOkButtonListener(remoteConditionsList, fieldsAndTypes,remoteGroupSelectionMap,Messages.ORIGINAL,originalFilterConditions));
		
		if(retainRemoteFilter.getRetainFilter() == true){
			retainButton.setSelection(true);
		}
		
		TableViewerColumn addButtonTableViewerColumn = createTableColumns(tableViewer, "", 28);
		addButtonTableViewerColumn.setLabelProvider(getAddButtonCellProvider(tableViewer, remoteConditionsList, remoteGroupSelectionMap));
		
		TableViewerColumn removeButtonTableViewerColumn = createTableColumns(tableViewer, "", 28);
		removeButtonTableViewerColumn.setLabelProvider(getRemoveButtonCellProvider(tableViewer, remoteConditionsList,remoteBtnAddGrp,remoteGroupSelectionMap));
		
		TableViewerColumn groupButtonTableViewerColumn = createTableColumns(tableViewer, "", 40);
		groupButtonTableViewerColumn.setLabelProvider(getGroupCheckCellProvider(tableViewer, remoteConditionsList,remoteBtnAddGrp));
		
		for (int key  : remoteGroupSelectionMap.keySet()) {	
			TableViewerColumn dummyTableViewerColumn = createTableColumns(tableViewer, "",20);
			dummyTableViewerColumn.setLabelProvider(getDummyColumn(tableViewer,remoteConditionsList, key,remoteGroupSelectionMap));	
		}
		
		TableViewerColumn relationalDropDownColumn = createTableColumns(tableViewer, Messages.RELATIONAL_OPERATOR, 120);
		relationalDropDownColumn.setLabelProvider(getRelationalCellProvider(tableViewer, remoteConditionsList, true));
		
		
		TableViewerColumn fieldNameDropDownColumn = createTableColumns(tableViewer, Messages.FIELD_NAME, 150);
		fieldNameDropDownColumn.setLabelProvider(getFieldNameCellProvider(tableViewer, remoteConditionsList, true));
		
		TableViewerColumn conditionalDropDownColumn = createTableColumns(tableViewer, Messages.CONDITIONAL_OPERATOR, 130);
		conditionalDropDownColumn.setLabelProvider(getConditionalCellProvider(tableViewer, remoteConditionsList, true));
		
		TableViewerColumn value1TextBoxColumn = createTableColumns(tableViewer, Messages.VALUE1, 150);
		value1TextBoxColumn.setLabelProvider(getValue1CellProvider(tableViewer, remoteConditionsList, true));
		
		TableViewerColumn value2TextBoxColumn = createTableColumns(tableViewer, Messages.VALUE2, 150);
		value2TextBoxColumn.setLabelProvider(getValue2CellProvider(tableViewer, remoteConditionsList, true));
		
		if(remoteConditionsList.isEmpty()){
			remoteConditionsList.add(0, new Condition());
		}
		dummyList.clear();
		dummyList.addAll(FilterHelper.INSTANCE.cloneList(remoteConditionsList));
		tableViewer.setInput(remoteConditionsList);
		tableViewer.refresh();
		
	}

	private MouseListener getCancelButtonListener() {
		return new MouseListener() {
			
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			}
			
			@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				cancelPressed();
			}
			
			@Override
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
			}
		};
	}

	private void createLocalTabItem(TabFolder tabFolder, TableViewer tableViewer) {
		TabItem tbtmLocal = new TabItem(tabFolder, SWT.NONE);
		tbtmLocal.setText(Messages.DOWNLOADED_DATASET);
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmLocal.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		tableViewer.setContentProvider(new ArrayContentProvider());
		Table table = tableViewer.getTable();
		
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayout(new GridLayout(4, false));
        
        Button btnAddRowAt = new Button(composite_3, SWT.NONE);
        btnAddRowAt.addSelectionListener(FilterHelper.INSTANCE.getAddAtEndListener(tableViewer, localConditionsList, dummyList));
        btnAddRowAt.setText(Messages.ADD_ROW_AT_END);
        
        localBtnAddGrp = new Button(composite_3, SWT.NONE);
        Button clearGroupsLocal=new Button(composite_3,SWT.NONE);
        clearGroupsLocal.addSelectionListener(clearGroupsListner(tableViewer, localGroupSelectionMap,clearGroupsLocal,localBtnAddGrp,localConditionsList,false));
        clearGroupsLocal.setText(Messages.CLEAR_GROUPS);
        if(localGroupSelectionMap.size() !=0 && localConditionsList.size() !=0)
		{
			clearGroupsLocal.setEnabled(true);
		} else {
			clearGroupsLocal.setEnabled(false);
		}

        
        localBtnAddGrp.setText(Messages.CREATE_GROUP);
        localBtnAddGrp.setEnabled(false);
        localBtnAddGrp.addSelectionListener(getAddGroupButtonListner(tableViewer, clearGroupsLocal,localConditionsList,localBtnAddGrp,localGroupSelectionMap));
        		
        		        
		Button retainButton = new Button(composite_3, SWT.CHECK);
		retainButton.setText(Messages.RETAIN_LOCAL_FILTER);
		retainButton.addSelectionListener(FilterHelper.INSTANCE.getRetainButtonListener(retainLocalFilter));
		
		Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setLayout(new GridLayout(4, false));
		composite_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		localOkButton = new Button(composite_4, SWT.NONE);
		localOkButton.setText(Messages.OK2);
		localOkButton.addSelectionListener(FilterHelper.INSTANCE.getOkButtonListener(localConditionsList, fieldsAndTypes,localGroupSelectionMap, Messages.DOWNLOADED,originalFilterConditions));
		
		localApplyButton = new Button(composite_4, SWT.NONE);
		localApplyButton.setText(Messages.APPLY);
		localApplyButton.addSelectionListener(FilterHelper.INSTANCE.getLocalApplyButtonListener(originalFilterConditions, 
				localConditionsList, retainLocalFilter));
		
		Button btnCancel = new Button(composite_4, SWT.NONE);
		btnCancel.setText(Messages.CANCEL2);
		btnCancel.addMouseListener(getCancelButtonListener());
		
		Button clearButton = new Button(composite_4, SWT.NONE);
		clearButton.addSelectionListener(getClearButtonListener(tableViewer, localConditionsList,dummyList,originalFilterConditions,false,retainButton,localGroupSelectionMap, localBtnAddGrp));
		clearButton.setText(Messages.CLEAR);
		
		if(retainLocalFilter.getRetainFilter() == true){
			retainButton.setSelection(true);
		}
		
		TableViewerColumn addButtonTableViewerColumn = createTableColumns(tableViewer, "", 28);
		addButtonTableViewerColumn.setLabelProvider(getAddButtonCellProvider(tableViewer, localConditionsList,localGroupSelectionMap));
		
		TableViewerColumn removeButtonTableViewerColumn = createTableColumns(tableViewer, "", 28);
		removeButtonTableViewerColumn.setLabelProvider(getRemoveButtonCellProvider(tableViewer, localConditionsList,localBtnAddGrp,localGroupSelectionMap));
		
		TableViewerColumn groupButtonTableViewerColumn = createTableColumns(tableViewer, "", 40);
		groupButtonTableViewerColumn.setLabelProvider(getGroupCheckCellProvider(tableViewer, localConditionsList,localBtnAddGrp));
		
		for (int key  : localGroupSelectionMap.keySet()) {
			
			TableViewerColumn dummyTableViewerColumn = createTableColumns(tableViewer, "",20);
			dummyTableViewerColumn.setLabelProvider(getDummyColumn(tableViewer,localConditionsList, key,localGroupSelectionMap));	
		}
		
		TableViewerColumn relationalDropDownColumn = createTableColumns(tableViewer, Messages.RELATIONAL_OPERATOR, 120);
		relationalDropDownColumn.setLabelProvider(getRelationalCellProvider(tableViewer, localConditionsList, false));
		
		
		TableViewerColumn fieldNameDropDownColumn = createTableColumns(tableViewer, Messages.FIELD_NAME, 150);
		fieldNameDropDownColumn.setLabelProvider(getFieldNameCellProvider(tableViewer, localConditionsList, false));
		
		TableViewerColumn conditionalDropDownColumn = createTableColumns(tableViewer, Messages.CONDITIONAL_OPERATOR, 130);
		conditionalDropDownColumn.setLabelProvider(getConditionalCellProvider(tableViewer, localConditionsList, false));
		
		TableViewerColumn value1TextBoxColumn = createTableColumns(tableViewer, Messages.VALUE1, 150);
		value1TextBoxColumn.setLabelProvider(getValue1CellProvider(tableViewer, localConditionsList, false));

		TableViewerColumn value2TextBoxColumn = createTableColumns(tableViewer, Messages.VALUE2, 150);
		value2TextBoxColumn.setLabelProvider(getValue2CellProvider(tableViewer, localConditionsList, false));
		
		if(localConditionsList.isEmpty()){
			localConditionsList.add(0, new Condition());
		}
		dummyList.clear();
		dummyList.addAll(FilterHelper.INSTANCE.cloneList(localConditionsList));
		tableViewer.setInput(localConditionsList);
		tableViewer.refresh();
		
		
	}

	private CellLabelProvider getValue1CellProvider(final TableViewer tableViewer, final List<Condition> conditionsList,
			final boolean isRemote) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_VALUE") == null) {
					item.setData("ADDED_VALUE", TRUE);
					Text text;
					if(isRemote){
						text = addTextBoxInTable(tableViewer, item, VALUE1_TEXT_BOX, VALUE1_TEXT_PANE, VALUE1_EDITOR, 
								cell.getColumnIndex(),FilterHelper.INSTANCE.getTextBoxValue1Listener(conditionsList, 
										fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton));
					}
					else {
						text = addTextBoxInTable(tableViewer, item, VALUE1_TEXT_BOX, VALUE1_TEXT_PANE, VALUE1_EDITOR, 
								cell.getColumnIndex(),FilterHelper.INSTANCE.getTextBoxValue1Listener(conditionsList, 
										fieldsAndTypes, fieldNames, localOkButton, localApplyButton));
					}
					
					text.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getValue1());
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_VALUE") == null) {
								item.setData("DISPOSED_VALUE", TRUE);
								Text valueText = (Text) item.getData(VALUE1_TEXT_BOX);
								((TableEditor) valueText.getData(VALUE1_EDITOR)).dispose();
								valueText.dispose();
								
								Composite composite = (Composite)item.getData(VALUE1_TEXT_PANE);
								composite.dispose();
							}
						}
					});
				} else {
					Text text = (Text) item.getData(VALUE1_TEXT_BOX);
					text.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getValue1());
				}
			}
		};
	}
	
	private CellLabelProvider getValue2CellProvider(final TableViewer tableViewer, final List<Condition> conditionsList,
			final boolean isRemote) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_VALUE2") == null) {
					item.setData("ADDED_VALUE2", TRUE);
					Text text;
					if(isRemote){
						text = addTextBoxInTable(tableViewer, item, VALUE2_TEXT_BOX, VALUE2_TEXT_PANE, VALUE2_EDITOR, 
								cell.getColumnIndex(),FilterHelper.INSTANCE.getTextBoxValue2Listener(conditionsList, 
										fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton));
					}
					else {
						text = addTextBoxInTable(tableViewer, item, VALUE2_TEXT_BOX, VALUE2_TEXT_PANE, VALUE2_EDITOR, 
								cell.getColumnIndex(),FilterHelper.INSTANCE.getTextBoxValue2Listener(conditionsList, 
										fieldsAndTypes, fieldNames, localOkButton, localApplyButton));
					}
					text.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getValue2());
					enableAndDisableValue2TextBox(dummyList, tableViewer.getTable().indexOf(item), text);
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_VALUE2") == null) {
								item.setData("DISPOSED_VALUE2", TRUE);
								Text valueText = (Text) item.getData(VALUE2_TEXT_BOX);
								((TableEditor)valueText.getData(VALUE2_EDITOR)).dispose();
								valueText.dispose();
								
								Composite composite = (Composite)item.getData(VALUE2_TEXT_PANE);
								composite.dispose();
							}
						}
					});
				} else {
					Text text = (Text) item.getData(VALUE2_TEXT_BOX);
					text.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getValue2());
					enableAndDisableValue2TextBox(dummyList, tableViewer.getTable().indexOf(item), text);
				}
			}
		};
	}
	
	private void enableAndDisableValue2TextBox(final List<Condition> conditionsList, int index, Text text) {
		if (conditionsList.get(index).getConditionalOperator().equalsIgnoreCase(FilterConstants.BETWEEN)) {
			text.setVisible(true);
		} else {
			text.setVisible(false);
		}
	}

	private CellLabelProvider getConditionalCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList,
			final boolean isRemote) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_CONDITIONAL") == null) {
					item.setData("ADDED_CONDITIONAL", TRUE);
					CCombo combo;
					if(isRemote){
						combo = addComboInTable(tableViewer, item, CONDITIONAL_OPERATORS, CONDITIONAL_COMBO_PANE, 
								CONDITIONAL_EDITOR,	cell.getColumnIndex(), new String[]{}, 
								FilterHelper.INSTANCE.getConditionalOperatorSelectionListener(conditionsList, 
										fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton),
								FilterHelper.INSTANCE.getConditionalOperatorModifyListener(conditionsList, 
										fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton));
					}else{
						combo = addComboInTable(tableViewer, item, CONDITIONAL_OPERATORS, CONDITIONAL_COMBO_PANE, 
								CONDITIONAL_EDITOR,	cell.getColumnIndex(), new String[]{}, 
								FilterHelper.INSTANCE.getConditionalOperatorSelectionListener(conditionsList, 
										fieldsAndTypes, fieldNames, localOkButton, localApplyButton),
								FilterHelper.INSTANCE.getConditionalOperatorModifyListener(conditionsList, 
										fieldsAndTypes, fieldNames, localOkButton, localApplyButton));
					}
					
					if(StringUtils.isNotBlank(dummyList.get(tableViewer.getTable().indexOf(item)).getFieldName())){
						String fieldsName = dummyList.get(tableViewer.getTable().indexOf(item)).getFieldName();
						combo.setItems(typeBasedConditionalOperators.get(fieldsAndTypes.get(fieldsName)));
					}
					else{
						combo.setItems(new String[]{});
					}
					combo.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getConditionalOperator());
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_CONDITIONAL") == null) {
								item.setData("DISPOSED_CONDITIONAL", TRUE);
								CCombo combo = (CCombo) item.getData(CONDITIONAL_OPERATORS);
								((TableEditor)combo.getData(CONDITIONAL_EDITOR)).dispose();
								combo.dispose();
								
								Composite composite = (Composite)item.getData(CONDITIONAL_COMBO_PANE);
								composite.dispose();
							}
						}
					});
				}
				else{
					CCombo combo = (CCombo) item.getData(CONDITIONAL_OPERATORS);
					if(StringUtils.isNotBlank(dummyList.get(tableViewer.getTable().indexOf(item)).getFieldName())){
						String fieldsName = dummyList.get(tableViewer.getTable().indexOf(item)).getFieldName();
						combo.setItems(typeBasedConditionalOperators.get(fieldsAndTypes.get(fieldsName)));
					}
					else{
						combo.setItems(new String[]{});
					}
					combo.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getConditionalOperator());
				}
			}
		};
	}

	private CellLabelProvider getFieldNameCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList, 
			final boolean isRemote) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_FIELD") == null) {
					item.setData("ADDED_FIELD", TRUE);
					CCombo combo;
					if(isRemote){
						combo = addComboInTable(tableViewer, item, FIELD_NAMES, FIELD_COMBO_PANE, FIELD_EDITOR,
								cell.getColumnIndex(), fieldNames, FilterHelper.INSTANCE.getFieldNameSelectionListener(tableViewer, 
										conditionsList, fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton),
										FilterHelper.INSTANCE.getFieldNameModifyListener(tableViewer, 
												conditionsList, fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton));
					}
					else {
						combo = addComboInTable(tableViewer, item, FIELD_NAMES, FIELD_COMBO_PANE, FIELD_EDITOR,
								cell.getColumnIndex(), fieldNames, FilterHelper.INSTANCE.getFieldNameSelectionListener(tableViewer, 
										conditionsList, fieldsAndTypes, fieldNames, localOkButton, localApplyButton),
										FilterHelper.INSTANCE.getFieldNameModifyListener(tableViewer, 
												conditionsList, fieldsAndTypes, fieldNames, localOkButton, localApplyButton));
					}
				
					combo.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getFieldName());
					item.addDisposeListener(new DisposeListener() {
						
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (item.getData("DISPOSED_FIELD") == null) {
								item.setData("DISPOSED_FIELD", TRUE);
								CCombo combo = (CCombo) item.getData(FIELD_NAMES);
								((TableEditor)combo.getData(FIELD_EDITOR)).dispose();
								combo.dispose();
								
								Composite composite = (Composite)item.getData(FIELD_COMBO_PANE);
								composite.dispose();
							}
						}
					});
				}
				else{
					CCombo fieldNameCombo = (CCombo) item.getData(FIELD_NAMES);
					fieldNameCombo.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getFieldName());
				}
			}
		};
	}

	private CellLabelProvider getRelationalCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList, 
			final boolean isRemote) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("ADDED_RELATIONAL") == null) {
					item.setData("ADDED_RELATIONAL", TRUE);
					CCombo combo;
					if(isRemote){
						combo = addComboInTable(tableViewer, item, RELATIONAL_OPERATORS, RELATIONAL_COMBO_PANE, RELATIONAL_EDITOR,
								cell.getColumnIndex(), relationalOperators,	
								FilterHelper.INSTANCE.getRelationalOpSelectionListener(conditionsList, 
										fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton),
								FilterHelper.INSTANCE.getRelationalOpModifyListener(conditionsList, 
										fieldsAndTypes, fieldNames, remoteOkButton, remoteApplyButton));
					}
					else{
						combo = addComboInTable(tableViewer, item, RELATIONAL_OPERATORS, RELATIONAL_COMBO_PANE, RELATIONAL_EDITOR,
								cell.getColumnIndex(), relationalOperators,	
								FilterHelper.INSTANCE.getRelationalOpSelectionListener(conditionsList, 
										fieldsAndTypes, fieldNames, localOkButton, localApplyButton),
								FilterHelper.INSTANCE.getRelationalOpModifyListener(conditionsList, 
										fieldsAndTypes, fieldNames, localOkButton, localApplyButton));
					}
					
					combo.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getRelationalOperator());
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
								item.setData("DISPOSED_RELATIONAL", TRUE);
								CCombo combo = (CCombo) item.getData(RELATIONAL_OPERATORS);
								((TableEditor)combo.getData(RELATIONAL_EDITOR)).dispose();
								combo.dispose();
								
								Composite composite = (Composite)item.getData(RELATIONAL_COMBO_PANE);
								composite.dispose();
							}
						}
					});
				}
				else{
					CCombo combo = (CCombo) item.getData(RELATIONAL_OPERATORS);
					combo.setText((dummyList.get(tableViewer.getTable().indexOf(item))).getRelationalOperator());
				}
			}
		};
	}

	private CellLabelProvider getGroupCheckCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList, 
			final Button btnAddGrp) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("UPDATED3") == null) {
					item.setData("UPDATED3", TRUE);
				} else {
					return;
				}
				addCheckButtonInTable(tableViewer, item, GROUP_CHECKBOX, GROUP_CHECKBOX_PANE, GROUP_EDITOR, cell.getColumnIndex(), 
						FilterHelper.INSTANCE.checkButtonListener(tableViewer, conditionsList,btnAddGrp));
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

	private CellLabelProvider getRemoveButtonCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList, 
			final Button btnAddGrp, final TreeMap<Integer, List<List<Integer>>> groupSelectionMap) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("UPDATED2") == null) {
					item.setData("UPDATED2", TRUE);
				} else {
					return;
				}
				addButtonInTable(tableViewer, item, REMOVE, REMOVE_BUTTON_PANE, REMOVE_EDITOR, cell.getColumnIndex(), 
						removeButtonListener(tableViewer, conditionsList, dummyList,groupSelectionMap, btnAddGrp), 
						ImagePathConstant.DELETE_BUTTON);
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

	private CellLabelProvider getAddButtonCellProvider(final TableViewer tableViewer, final List<Condition> conditionsList
			,final TreeMap<Integer, List<List<Integer>>> groupSelectionMap) {
		return new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				// DO NOT REMOVE THIS CONDITION. The condition is return to
				// prevent multiple updates on single item
				if (item.getData("UPDATED1") == null) {
					item.setData("UPDATED1", TRUE);
				} else {
					return;
				}
				addButtonInTable(tableViewer, item, ADD, ADD_BUTTON_PANE, ADD_EDITOR, cell.getColumnIndex(), 
						FilterHelper.INSTANCE.addButtonListener(tableViewer,conditionsList, dummyList,groupSelectionMap), 
						ImagePathConstant.ADD_BUTTON);
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
	
	private CellLabelProvider getDummyColumn(final TableViewer tableViewer,	final List<Condition> conditionsList, final Integer columnIndex,final TreeMap<Integer, List<List<Integer>>> groupSelectionMap) {
		return new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();
				List<List<Integer>> checkedGrpRowIndices = groupSelectionMap.get(columnIndex);
				if (cell.getColumnIndex() == (columnIndex + 3)) {
					if (null != checkedGrpRowIndices
							&& !checkedGrpRowIndices.isEmpty()) {
						List tempList = new ArrayList();
						for (List<Integer> checkedIndex : checkedGrpRowIndices) {
							tempList.addAll(checkedIndex);
						}
						int indexOf = tableViewer.getTable().indexOf(item);
						if(tempList.contains(indexOf)){
							for (int i = 0; i < checkedGrpRowIndices.size(); i++) {
								if((checkedGrpRowIndices.get(i)).contains(indexOf)){
									cell.setBackground(FilterHelper.INSTANCE.getColor(i));
									break;
								}
							}	
						}
						else{
							cell.setBackground(new Color(null, 255,255,255));
						}
					}
				}
			}
		};
	}
	
	private TableViewerColumn createTableColumns(TableViewer tableViewer, String columnLabel, int width) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.CENTER);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(width);
		tableColumn.setText(columnLabel);
		return tableViewerColumn;
	}
	
	
	private Text addTextBoxInTable(TableViewer tableViewer, TableItem tableItem, String textBoxName, 
			String valueTextPane, String editorName, int columnIndex, Listener listener) {
		final Composite buttonPane = new Composite(tableViewer.getTable(), SWT.NONE);
		buttonPane.setLayout(new FillLayout());
		final Text text = new Text(buttonPane, SWT.NONE);
		text.addListener(SWT.Modify, listener);
		text.setData(FilterConstants.ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		tableItem.setData(textBoxName, text);
		tableItem.setData(valueTextPane, buttonPane);
		//text.addModifyListener(FilterHelper.INSTANCE.getTextModifyListener());
		
		final TableEditor editor = new TableEditor(tableViewer.getTable());
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		editor.setEditor(buttonPane, tableItem, columnIndex);
		editor.layout();
		text.setData(editorName, editor);
		return text;
	}
	
	private CCombo addComboInTable(TableViewer tableViewer, TableItem tableItem, String comboName, String comboPaneName, 
			String editorName, int columnIndex,	String[] relationalOperators, SelectionListener dropDownSelectionListener,
			ModifyListener modifyListener) {
		final Composite buttonPane = new Composite(tableViewer.getTable(), SWT.NONE);
		buttonPane.setLayout(new FillLayout());
		final CCombo combo = new CCombo(buttonPane, SWT.NONE);
		combo.setItems(relationalOperators);
		combo.setData(FilterConstants.ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		tableItem.setData(comboName, combo);
		tableItem.setData(comboPaneName, buttonPane);
		combo.addSelectionListener(dropDownSelectionListener);
		combo.addModifyListener(modifyListener);
		new AutoCompleteField(combo, new CComboContentAdapter(), combo.getItems());
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
		button.setData(FilterConstants.ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		tableItem.setData(columnName, button);
		tableItem.setData(buttonPaneName, buttonPane);
		button.addSelectionListener(buttonSelectionListener);
		button.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + imagePath));
		
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
		button.setData(FilterConstants.ROW_INDEX, tableViewer.getTable().indexOf(tableItem));
		if(null != buttonSelectionListener){
			button.addSelectionListener(buttonSelectionListener);
		}
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
	}
	
	private SelectionListener getAddGroupButtonListner(final TableViewer tableViewer,final Button clearGroups,
		final List<Condition> conditionsList,final Button btnAddGrp, final TreeMap<Integer, List<List<Integer>>> groupSelectionMap) {
		
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(storeGroupSelection(tableViewer,groupSelectionMap)){              			     
					FilterHelper.INSTANCE.disposeAllColumns(tableViewer);
				    dummyList.clear();
				    dummyList.addAll(FilterHelper.INSTANCE.cloneList(conditionsList));
				    redrawAllColumns(tableViewer,conditionsList,btnAddGrp,groupSelectionMap);
				   	clearGroups.setEnabled(true);		 
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		
		return listener;
	}

	
	
	public void redrawAllColumns(TableViewer tableViewer, List<Condition> conditionsList, Button btnAddGrp, 
			TreeMap<Integer, List<List<Integer>>> groupSelectionMap){
		
		TableViewerColumn addButtonTableViewerColumn = createTableColumns(tableViewer, "", 28);
		addButtonTableViewerColumn.setLabelProvider(getAddButtonCellProvider(tableViewer, conditionsList,groupSelectionMap));
		
		TableViewerColumn removeButtonTableViewerColumn = createTableColumns(tableViewer, "", 28);
		removeButtonTableViewerColumn.setLabelProvider(getRemoveButtonCellProvider(tableViewer, conditionsList,btnAddGrp,groupSelectionMap));
		
		TableViewerColumn groupButtonTableViewerColumn = createTableColumns(tableViewer, "", 40);
		groupButtonTableViewerColumn.setLabelProvider(getGroupCheckCellProvider(tableViewer, conditionsList,btnAddGrp));
		
	
		for (int key  : groupSelectionMap.keySet()) {
					
			TableViewerColumn dummyTableViewerColumn = createTableColumns(tableViewer, "",20);
			dummyTableViewerColumn.setLabelProvider(getDummyColumn(tableViewer,conditionsList, key,groupSelectionMap));	
		}
		
		
		TableViewerColumn relationalDropDownColumn = createTableColumns(tableViewer, Messages.RELATIONAL_OPERATOR, 120);
		relationalDropDownColumn.setLabelProvider(getRelationalCellProvider(tableViewer, conditionsList, true));
		
		
		TableViewerColumn fieldNameDropDownColumn = createTableColumns(tableViewer, Messages.FIELD_NAME, 150);
		fieldNameDropDownColumn.setLabelProvider(getFieldNameCellProvider(tableViewer, conditionsList, true));
		
		TableViewerColumn conditionalDropDownColumn = createTableColumns(tableViewer, Messages.CONDITIONAL_OPERATOR, 130);
		conditionalDropDownColumn.setLabelProvider(getConditionalCellProvider(tableViewer, conditionsList, true));
		
		TableViewerColumn value1TextBoxColumn = createTableColumns(tableViewer, Messages.VALUE1, 150);
		value1TextBoxColumn.setLabelProvider(getValue1CellProvider(tableViewer, conditionsList, true));
		
		TableViewerColumn valueTextBoxValue2Column = createTableColumns(tableViewer, Messages.VALUE2, 150);
		valueTextBoxValue2Column.setLabelProvider(getValue2CellProvider(tableViewer, conditionsList, true));
		
		btnAddGrp.setEnabled(false);
		
		tableViewer.refresh();
	}


	private boolean storeGroupSelection(TableViewer tableViewer, TreeMap<Integer, List<List<Integer>>> groupSelectionMap){
		
		boolean retVal=false;
		List<List<Integer>> grpList = new ArrayList<>();
		List<Integer> selectionList = new ArrayList<>();
		
		TableItem[] items = tableViewer.getTable().getItems();
		
		for (TableItem tableItem : items) {
			Button button = (Button) tableItem.getData(GROUP_CHECKBOX);
			if(button.getSelection()){
				selectionList.add(tableViewer.getTable().indexOf(tableItem));
			}
		}
			
		if (groupSelectionMap.isEmpty()) {
			grpList.add(selectionList);
			groupSelectionMap.put(0, grpList);
			retVal=true;
		} else {
			if (FilterHelper.INSTANCE.validatUserGroupSelection(groupSelectionMap, selectionList)) {
				if(FilterHelper.INSTANCE.isColumnModifiable(groupSelectionMap, selectionList)){
					retVal=true;
				}else{
					grpList.add(selectionList);
					Map<Integer, List<List<Integer>>> tempMap = new TreeMap<>();
					tempMap.putAll(groupSelectionMap);
					groupSelectionMap.clear();
					groupSelectionMap.put(0, grpList);
					for (int i = 0; i < tempMap.size(); i++) {
						groupSelectionMap.put(i + 1, tempMap.get(i));
					}
					retVal=true;
					FilterHelper.INSTANCE.reArrangeGroups(groupSelectionMap, selectionList);
				}
			} 
		}
		return retVal;  
	}
	
	public SelectionListener removeButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList,
			final List<Condition> dummyList, final TreeMap<Integer, List<List<Integer>>> groupSelectionMap, final Button btnAddGrp) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(conditionsList.size() > 1){
					Button button = (Button) e.getSource();
					int removeIndex = (int) button.getData(FilterConstants.ROW_INDEX);
					
					conditionsList.remove(removeIndex);				
					dummyList.clear();
					dummyList.addAll(FilterHelper.INSTANCE.cloneList(conditionsList));
					boolean isRemoveAllColumns = FilterHelper.INSTANCE.refreshGroupSelections(tableViewer,removeIndex, "DEL", groupSelectionMap);
					
					TableItem[] items = tableViewer.getTable().getItems();
					items[removeIndex].dispose();
					FilterHelper.INSTANCE.disposeAllColumns(tableViewer);
					redrawAllColumns(tableViewer,conditionsList,btnAddGrp,groupSelectionMap);
					
					if(isRemoveAllColumns){
						FilterHelper.INSTANCE.reArrangeGroupColumns(groupSelectionMap);
						FilterHelper.INSTANCE.disposeAllColumns(tableViewer);
						redrawAllColumns(tableViewer,conditionsList,btnAddGrp,groupSelectionMap);
					}
					
				}
				tableViewer.refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public FilterConditions getOriginalFilterConditions() {
		return originalFilterConditions;
	}

	public void setOriginalFilterConditions(
			FilterConditions originalFilterConditions) {
		this.originalFilterConditions = originalFilterConditions;
	}

	public void setDebugDataViewerAdapterAndViewer(DataViewerAdapter adapter, DebugDataViewer dataViewer)
			throws ClassNotFoundException, SQLException {
		dataViewerAdapter = adapter;
		debugDataViewer = dataViewer;
	}

	public FilterConditions getConditions(){
		return originalFilterConditions;
		
	}

	public List<Condition> getLocalConditionsList() {
		return localConditionsList;
	}

	public void setLocalConditionsList(List<Condition> localConditionsList) {
		this.localConditionsList = localConditionsList;
	}
	
	@Override
	protected void cancelPressed() {
		setReturnCode(CANCEL);
		close();
	}
	
	public SelectionListener clearGroupsListner(final TableViewer tableViewer, final TreeMap<Integer, List<List<Integer>>> groupSelectionMap,
			final Button clearGroups, final Button btnAddGrp, final  List<Condition> conditionsList,final boolean isRemote) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				dummyList.clear();
				dummyList.addAll(FilterHelper.INSTANCE.cloneList(conditionsList));
				groupSelectionMap.clear();
				
				if(isRemote){
					originalFilterConditions.setRemoteGroupSelectionMap(groupSelectionMap);
					
				}else{
					
					originalFilterConditions.setLocalGroupSelectionMap(groupSelectionMap);				
			   }
				
				FilterHelper.INSTANCE.disposeAllColumns(tableViewer);
				redrawAllColumns(tableViewer, conditionsList, btnAddGrp,groupSelectionMap);			
				clearGroups.setEnabled(false);
			
				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		
		return listener;
	}
	  
	
	
	public SelectionListener getClearButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList,
			final List<Condition> dummyList, final FilterConditions originalFilterConditions, final boolean isRemote,final Button retainButton,
			final TreeMap<Integer,List<List<Integer>>> groupSelectionMap,final Button btnAddGrp) {
		SelectionListener listner = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				dummyList.clear();
				conditionsList.clear();
				dummyList.add(new Condition());
				retainButton.setSelection(false);
				
				groupSelectionMap.clear(); 
			
				if(isRemote)
				{
					originalFilterConditions.setRetainRemote(false);
					originalFilterConditions.setRemoteCondition("");
					originalFilterConditions.setRemoteConditions(new ArrayList<Condition>());
					debugDataViewer.setRemoteCondition("");
					originalFilterConditions.setRemoteGroupSelectionMap(groupSelectionMap);
					debugDataViewer.getDataViewerAdapter().setFilterCondition(debugDataViewer.getLocalCondition());
					retainRemoteFilter.setRetainFilter(false);
					((ReloadAction)debugDataViewer.getActionFactory().getAction(ReloadAction.class.getName())).setIfFilterReset(true);
				}else{
					originalFilterConditions.setRetainLocal(false);
					originalFilterConditions.setLocalCondition("");
					originalFilterConditions.setLocalConditions(new ArrayList<Condition>());
					debugDataViewer.setLocalCondition("");
					originalFilterConditions.setLocalGroupSelectionMap(groupSelectionMap);
					debugDataViewer.getDataViewerAdapter().setFilterCondition(debugDataViewer.getRemoteCondition());
					retainLocalFilter.setRetainFilter(false);
				}
				TableItem[] items = tableViewer.getTable().getItems();
				tableViewer.refresh();
				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}
				conditionsList.add(0, new Condition());
				
				 
				FilterHelper.INSTANCE.disposeAllColumns(tableViewer);
				redrawAllColumns(tableViewer,conditionsList,btnAddGrp,groupSelectionMap);
				((ReloadAction)debugDataViewer.getActionFactory().getAction(ReloadAction.class.getName())).run();
				debugDataViewer.submitRecordCountJob();
				cancelPressed();
			}
			
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		return listner;
	}
	
	public boolean ifSetLocalFilter(){
		return retainLocalFilter.getRetainFilter();
		
	}
	
	public boolean ifSetRemoteFilter(){
		
		return retainRemoteFilter.getRetainFilter();
	}
	
	
	public List<Condition> getRemoteConditionsList() {
		return remoteConditionsList;
	}

	public void setRemoteConditionsList(List<Condition> remoteConditionsList) {
		this.remoteConditionsList = remoteConditionsList;
	}

	public FilterConditions getFilterConditions() {
		return originalFilterConditions;
	}
	
	public Map<Integer, List<List<Integer>>> getLocalGroupSelections(){
		
		return localGroupSelectionMap;
	}
	
    public Map<Integer, List<List<Integer>>> getRemoteGroupSelections(){
		
		return remoteGroupSelectionMap;
	}
}

