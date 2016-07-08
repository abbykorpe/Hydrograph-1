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

import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.dataviewer.adapters.DataViewerAdapter;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.filemanager.DataViewerFileManager;
import hydrograph.ui.dataviewer.utilities.DataViewerUtility;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import com.google.gson.Gson;

public class FilterHelper {
	
	public static final FilterHelper INSTANCE = new FilterHelper();

	public static final String TYPE_BOOLEAN = "java.lang.Boolean";
	public static final String TYPE_DOUBLE = "java.lang.Double";
	public static final String TYPE_FLOAT = "java.lang.Float";
	public static final String TYPE_SHORT = "java.lang.Short";
	public static final String TYPE_LONG = "java.lang.Long";
	public static final String TYPE_BIGDECIMAL = "java.math.BigDecimal";
	public static final String TYPE_INTEGER = "java.lang.Integer";
	public static final String TYPE_DATE = "java.util.Date";
	public static final String TYPE_STRING = "java.lang.String";
	
	private static final String DOWNLOADED="Downloaded";
	private static final String REGEX_DIGIT = "\\d";
	private static final String SINGLE_SPACE = " ";
	private static final String OPEN_BRACKET = "(";
	private static final String CLOSE_BRACKET = ")";
	private static final String SINGLE_QOUTE = "'";
	private static final String DELIM_COMMA = ",";
	private static final String NOT_IN = "not in";
	private static final String IN = "in";
	private DataViewerAdapter dataViewerAdapter;
	private DebugDataViewer debugDataViewer;
	private FilterConditionsDialog filterConditionsDialog;
	private String SCHEMA_FILE_EXTENTION=".xml";
	private String filteredFileLocation;
	private String filteredFileName = "";
	private String localCondition = "";
	private String remoteCondition;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterHelper.class);
	private FilterHelper() {
	}
	
	public Map<String, String[]> getTypeBasedOperatorMap(){
		Map<String, String[]> typeBasedConditionalOperators = new HashMap<String, String[]>();
		typeBasedConditionalOperators.put(TYPE_STRING, new String[]{"LIKE", "NOT LIKE", "IN","NOT IN"}); 
		typeBasedConditionalOperators.put(TYPE_INTEGER, new String[]{">", "<", "<=", ">=", "<>", "=", "IN", "NOT IN"}); 
		typeBasedConditionalOperators.put(TYPE_DATE, new String[]{">", "<", "<=",">=", "<>", "=",  "IN", "NOT IN"}); 
		typeBasedConditionalOperators.put(TYPE_BIGDECIMAL, new String[]{">", "<", "<=", ">=", "<>", "=",  "IN","NOT IN"});
		typeBasedConditionalOperators.put(TYPE_LONG, new String[]{">", "<", "<=", ">=", "<>", "=",  "IN", "NOT IN"});
		typeBasedConditionalOperators.put(TYPE_SHORT, new String[]{">", "<", "<=", ">=", "<>", "=",  "IN", "NOT IN"});
		typeBasedConditionalOperators.put(TYPE_FLOAT, new String[]{">", "<", "<=", ">=", "<>", "=",  "IN", "NOT IN"});
		typeBasedConditionalOperators.put(TYPE_DOUBLE, new String[]{">", "<", "<=", ">=", "<>", "=",  "IN", "NOT IN"});
		typeBasedConditionalOperators.put(TYPE_BOOLEAN, new String[]{"<>", "="});
		return typeBasedConditionalOperators;
	}
	
	public  Listener getTextBoxListener(final List<Condition> conditionsList, 
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		Listener listener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Text text = (Text)event.widget;
				int index = (int) text.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				filterConditions.setValue(text.getText());
				validateText(text, filterConditions.getFieldName(), fieldsAndTypes, filterConditions.getConditionalOperator());
				toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
		};
		return listener;
	}
	public String getLocalCondition() {
		return localCondition;
	}

	public String getRemoteCondition() {
		return remoteCondition;
	}
	
	private void toggleOkApplyButton(final List<Condition> conditionsList, final Map<String, String> fieldsAndTypes,
			final String[] fieldNames, final Button okButton, final Button applyButton) {
		if(FilterValidator.INSTANCE.isAllFilterConditionsValid(conditionsList, fieldsAndTypes, fieldNames)){
			okButton.setEnabled(true);
			applyButton.setEnabled(true);
		}
		else{
			okButton.setEnabled(false);
			applyButton.setEnabled(false);
		}
	}
	
	public SelectionListener getFieldNameSelectionListener(final TableViewer tableViewer, final List<Condition> conditionsList,
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				CCombo source = (CCombo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				String fieldName = source.getText();
				filterConditions.setFieldName(fieldName);
				
				if(StringUtils.isNotBlank(fieldName)){
					String fieldType = fieldsAndTypes.get(fieldName);
					TableItem item = tableViewer.getTable().getItem(index);
					CCombo conditionalCombo = (CCombo) item.getData(FilterConditionsDialog.CONDITIONAL_OPERATORS);
					conditionalCombo.setItems(FilterHelper.INSTANCE.getTypeBasedOperatorMap().get(fieldType));
					new AutoCompleteField(conditionalCombo, new CComboContentAdapter(), conditionalCombo.getItems());
				}
				validateCombo(source);
				toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public ModifyListener getFieldNameModifyListener(final TableViewer tableViewer, final List<Condition> conditionsList,
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		ModifyListener listener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				CCombo source = (CCombo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				String fieldName = source.getText();
				filterConditions.setFieldName(fieldName);
				
				if(StringUtils.isNotBlank(fieldName)){
					String fieldType = fieldsAndTypes.get(fieldName);
					TableItem item = tableViewer.getTable().getItem(index);
					CCombo conditionalCombo = (CCombo) item.getData(FilterConditionsDialog.CONDITIONAL_OPERATORS);
					if(conditionalCombo != null && StringUtils.isNotBlank(fieldType)){
						conditionalCombo.setText(filterConditions.getConditionalOperator());
						conditionalCombo.setItems(FilterHelper.INSTANCE.getTypeBasedOperatorMap().get(fieldType));
						new AutoCompleteField(conditionalCombo, new CComboContentAdapter(), conditionalCombo.getItems());
					}
				}
				validateCombo(source);
				toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
		};
		return listener;
	}
	
	public SelectionListener getConditionalOperatorSelectionListener(final List<Condition> conditionsList, 
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				CCombo source = (CCombo) e.getSource();
				processConditionalOperator(source, conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public ModifyListener getConditionalOperatorModifyListener(final List<Condition> conditionsList, 
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		ModifyListener listener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				CCombo source = (CCombo) e.getSource();
				processConditionalOperator(source, conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
		};
		return listener;
	}
	
	private void processConditionalOperator(CCombo source, List<Condition> conditionsList, Map<String, String> fieldsAndTypes,
			String[] fieldNames, Button okButton, Button applyButton){
		int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
		Condition filterConditions = conditionsList.get(index);
		filterConditions.setConditionalOperator(source.getText());
		validateCombo(source);
		toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
	}
	
	public SelectionListener getRelationalOpSelectionListener(final List<Condition> conditionsList,  
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				CCombo source = (CCombo) e.getSource();
				processRelationalOperator(source, conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public ModifyListener getRelationalOpModifyListener(final List<Condition> conditionsList,  
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		ModifyListener listener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				CCombo source = (CCombo) e.getSource();
				processRelationalOperator(source, conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
			
		};
		return listener;
	}
	
	private void processRelationalOperator(CCombo source, List<Condition> conditionsList, Map<String, String> fieldsAndTypes,
			String[] fieldNames, Button okButton, Button applyButton){
		int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
		Condition filterConditions = conditionsList.get(index);
		filterConditions.setRelationalOperator(source.getText());
		if(index != 0){
			validateCombo(source);
		}
		toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
	}
	
	public SelectionListener addButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList, 
			final List<Condition> dummyList, final TreeMap<Integer, List<List<Integer>>> groupSelectionMap) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.getSource();
				int index = (int) button.getData(FilterConditionsDialog.ROW_INDEX);
				conditionsList.add(index, new Condition());
				dummyList.clear();
				dummyList.addAll(cloneList(conditionsList));
				FilterHelper.INSTANCE.refreshGroupSelections(tableViewer, index, "ADD", groupSelectionMap);
				tableViewer.refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	
	public SelectionListener getOkButtonListener(final List<Condition> conditionsList, final Map<String, String> fieldsAndTypes,
			final Map<Integer,List<List<Integer>>> groupSelectionMap, final String dataset,final FilterConditions originalFilterConditions) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//put number of elements in the list
				//1 2 3 4 5
				List<String> actualStringList = new LinkedList<>();
				for (int conditionIndex = 0; conditionIndex < conditionsList.size(); conditionIndex++) {
					actualStringList.add(conditionIndex, String.valueOf((conditionIndex)));
				}
				logger.trace(actualStringList.toString());
				//start adding brackets for grouping
				Set<Integer> treeSet  = (Set<Integer>) groupSelectionMap.keySet();
				if(treeSet.size() > 0){
					for (Integer position : treeSet) {
					List<List<Integer>> groupsInColumn = groupSelectionMap.get(position);
						for (int groupIndex = 0; groupIndex < groupsInColumn.size(); groupIndex++) {
							List<Integer> group = groupsInColumn.get(groupIndex);
							//add opening bracket before first element in the group
							if(!group.isEmpty()){
							Integer firstItem = group.get(0);
							Integer firstItemIndex = actualStringList.indexOf(String.valueOf(firstItem));
							actualStringList.add(firstItemIndex, OPEN_BRACKET);
							//add closing bracket after last element in the group							
							Integer lastItem = group.get(group.size()-1);
							Integer lastItemIndex = actualStringList.indexOf(String.valueOf(lastItem));
							actualStringList.add(lastItemIndex + 1, CLOSE_BRACKET);
							}
						}
					}
				}
				
				//start adding relational operators
				int indexOfRelational = 1;
				//start from 2nd index
				for (int item = 1; item < conditionsList.size(); item++) {
					int indexOfItem = actualStringList.indexOf(String.valueOf(item));
					while(true){
						if((actualStringList.get(indexOfItem-1)).matches(REGEX_DIGIT) 
								||(actualStringList.get(indexOfItem-1)).equalsIgnoreCase(CLOSE_BRACKET)){
							actualStringList.add(indexOfItem, conditionsList.get(indexOfRelational).getRelationalOperator());
							break;
						}else{
							indexOfItem = indexOfItem - 1;
						}
					}
					indexOfRelational += 1;
					logger.trace(actualStringList.toString());
				}
				
				StringBuffer buffer = new StringBuffer();
				for(int item = 0; item < conditionsList.size(); item++){
					StringBuffer conditionString = new StringBuffer();
					
					Condition condition = conditionsList.get(item);
					conditionString.append(condition.getFieldName()).append(SINGLE_SPACE).append(condition.getConditionalOperator()).append(SINGLE_SPACE)
					.append(getConditionValue(condition.getFieldName(), condition.getValue(), condition.getConditionalOperator(),
							fieldsAndTypes));
					int index = actualStringList.indexOf(String.valueOf(item));
					actualStringList.set(index, conditionString.toString());
				}
				
				for (String item : actualStringList) {
					buffer.append(item + SINGLE_SPACE);
				}
				logger.debug("Query String : " + buffer);
				
				if(dataset.equalsIgnoreCase(DOWNLOADED))
				{	
					if(!originalFilterConditions.getRetainRemote()){
						originalFilterConditions.setRemoteCondition("");
						originalFilterConditions.getLocalConditions().clear();
						filterConditionsDialog.getRemoteConditionsList().clear();
						debugDataViewer.setRemoteCondition("");
						remoteCondition="";
					}
					localCondition=buffer.toString();
					showLocalFilteredData(StringUtils.trim(buffer.toString()));
					debugDataViewer.setLocalCondition(localCondition);
				}
				else
				{
					if(!originalFilterConditions.getRetainLocal()){
						originalFilterConditions.setLocalCondition("");
						originalFilterConditions.getLocalConditions().clear();
						filterConditionsDialog.getLocalConditionsList().clear();
						debugDataViewer.setLocalCondition("");
						localCondition = "";
					}
					remoteCondition=buffer.toString();
					showRemoteFilteredData(StringUtils.trim(buffer.toString()));
					debugDataViewer.setRemoteCondition(remoteCondition);
				}
				filterConditionsDialog.close();
			}

			
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	
	private void showRemoteFilteredData(String buffer) {
		try {
			String filterJson = createJsonObjectForRemoteFilter(buffer);
			String filteredFilePath=DebugServiceClient.INSTANCE.getFilteredFile(filterJson, debugDataViewer.getJobDetails());
			DataViewerFileManager dataViewerFileManager=new DataViewerFileManager();
			dataViewerFileManager.downloadDataViewerFilterFile(filteredFilePath,debugDataViewer.getJobDetails());
			filteredFileName = dataViewerFileManager.getDataViewerFileName();
			filteredFileLocation = dataViewerFileManager.getDataViewerFilePath();
			debugDataViewer.setDebugFileLocation(filteredFileLocation);
			debugDataViewer.setDebugFileName(filteredFileName);
			debugDataViewer.showDataInDebugViewer(true,true);
			
		} catch (NumberFormatException | IOException exception) {
			logger.error("Error occuring while showing remote filtered data",exception);
		}
	}

	private void showLocalFilteredData(String buffer) {
		try {
			dataViewerAdapter.setFilterCondition(buffer);
			dataViewerAdapter.initializeTableData();
			debugDataViewer.getDataViewLoader().updateDataViewLists();
			debugDataViewer.getDataViewLoader().reloadloadViews();
			int noOfFilteredRows=dataViewerAdapter.getFileData().size();
			int pageSize=debugDataViewer.getViewDataPreferences().getPageSize();
			if (noOfFilteredRows < pageSize) {
				debugDataViewer.getStatusManager().enableNextPageButton(false);
				debugDataViewer.getStatusManager().updatePageNumberDisplayPanelIfFilteredDataSizeIsLessthanPageSize();
			} else {
				debugDataViewer.getStatusManager().enableNextPageButton(true);
			}
		} catch (SQLException exception) {
			logger.error("Error occuring while showing local filtered data",exception);
		}
	}

	public String createJsonObjectForRemoteFilter(String buffer) {
		Gson gson=new Gson();
		RemoteFilterJson remoteFilterJson = new RemoteFilterJson(buffer,
				DataViewerUtility.INSTANCE.getSchema(debugDataViewer.getDebugFileLocation() + 
						debugDataViewer.getDebugFileName()+SCHEMA_FILE_EXTENTION), 
						debugDataViewer.getViewDataPreferences().getFileSize(), debugDataViewer.getJobDetails());
		
		String filterJson=gson.toJson(remoteFilterJson);
		return filterJson;
	}
	

	public void setDataViewerAdapter(DataViewerAdapter dataViewerAdapter, FilterConditionsDialog filterConditionsDialog) {
		this.dataViewerAdapter=dataViewerAdapter;
		this.filterConditionsDialog=filterConditionsDialog;
	}

	public void setDebugDataViewer(DebugDataViewer debugDataViewer) {
		this.debugDataViewer=debugDataViewer;
	}
	
	protected String getConditionValue(String fieldName, String value, String conditional, Map<String, String> fieldsAndTypes) {
		String trimmedCondition = StringUtils.trim(conditional);
		String dataType = fieldsAndTypes.get(fieldName);
		if(TYPE_STRING.equalsIgnoreCase(dataType) || TYPE_DATE.equalsIgnoreCase(dataType) || TYPE_BOOLEAN.equalsIgnoreCase(dataType)){
			if(IN.equalsIgnoreCase(trimmedCondition) || NOT_IN.equalsIgnoreCase(trimmedCondition)){
				if(StringUtils.isNotBlank(value) && value.contains(DELIM_COMMA)){
					StringTokenizer tokenizer = new StringTokenizer(value, DELIM_COMMA);
					StringBuffer temp = new StringBuffer();
					int numberOfTokens = tokenizer.countTokens();
					temp.append(OPEN_BRACKET); 
					for (int index = 0; index < numberOfTokens; index++) {
						temp.append(SINGLE_QOUTE).append(tokenizer.nextToken()).append(SINGLE_QOUTE);
						if(index < numberOfTokens - 1){
							temp.append(DELIM_COMMA);
						}
					}
					temp.append(CLOSE_BRACKET);
					return temp.toString();
				}
				else{
					return OPEN_BRACKET + SINGLE_QOUTE + value + SINGLE_QOUTE + CLOSE_BRACKET;
				}
			}
			else{
				return SINGLE_QOUTE + value + SINGLE_QOUTE;
			}
		}
		else{
			if(IN.equalsIgnoreCase(trimmedCondition) || NOT_IN.equalsIgnoreCase(trimmedCondition)){
				return OPEN_BRACKET + value + CLOSE_BRACKET;
			}
			else{
				return value;
			}
		}
	}

	private boolean validateCombo(CCombo combo){
		if((Arrays.asList(combo.getItems())).contains(combo.getText())){
			combo.setBackground(new Color(null, 255, 255, 255));
			return true;
		}else {
			combo.setBackground(new Color(null, 255, 244, 113));
			return false;
		}
	}
	
	private boolean validateText(Text text, String fieldName, Map<String, String> fieldsAndTypes, String conditionalOperator) {
		String type = FilterValidator.INSTANCE.getType(fieldName, fieldsAndTypes);
		if(StringUtils.isNotBlank(text.getText()) && FilterValidator.INSTANCE.validateDataBasedOnTypes(type, text.getText(), conditionalOperator)){
			text.setBackground(new Color(null, 255, 255, 255));
			return true;
		}else {
			text.setBackground(new Color(null, 255, 244, 113));
			return false;
		}
	}



	public List<Condition> cloneList(List<Condition> conditionsList) {
		List<Condition> tempList = new ArrayList<>();
		for (Condition condition : conditionsList) {
			Condition newCondition = new Condition();
			tempList.add(newCondition.copy(condition));
		}
		return tempList;
	}

	public SelectionListener getRemoteApplyButtonListener(final FilterConditions originalFilterConditions,
			final List<Condition> remoteConditionsList, final RetainFilter retainFilter) {
		SelectionListener listner = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(retainFilter.getRetainFilter()){
					originalFilterConditions.setRemoteConditions(remoteConditionsList);
					originalFilterConditions.setRetainRemote(retainFilter.getRetainFilter());
				}
				Button button = (Button) e.widget;
				button.setEnabled(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		return listner;
	}

	public SelectionListener getLocalApplyButtonListener(final FilterConditions originalFilterConditions,
			final List<Condition> localConditionsList, final RetainFilter retainFilter) {
		SelectionListener listner = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(retainFilter.getRetainFilter()){
					originalFilterConditions.setLocalConditions(localConditionsList);
					originalFilterConditions.setRetainLocal(retainFilter.getRetainFilter());
				}
				Button button = (Button) e.widget;
				button.setEnabled(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		return listner;
	}
	
	public SelectionListener getRetainButtonListener(final RetainFilter retainFilter) {
		SelectionListener listner = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button)e.getSource();
				retainFilter.setRetainFilter(button.getSelection());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		return listner;
	}
   
	public SelectionAdapter getAddAtEndListener(final TableViewer tableViewer, final List<Condition> conditionList, 
			final List<Condition> dummyList) {
        return new SelectionAdapter() {
              @Override
              public void widgetSelected(SelectionEvent e) {
                    conditionList.add(conditionList.size(), new Condition());
                    dummyList.clear();
    				dummyList.addAll(cloneList(conditionList));
                    tableViewer.refresh();
              }
        };
  }
	
	public SelectionListener checkButtonListener(final TableViewer tableViewer,
			final List<Condition> conditionsList, final Button btnAddGrp) {
		SelectionListener listener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int count=0;
				boolean isEnabled=false;
				List<Integer> selectionPattern= new ArrayList<>();
				TableItem[] items = tableViewer.getTable().getItems();
				
				   for (TableItem tableItem : items) {
					   Button button = (Button) tableItem.getData(FilterConditionsDialog.GROUP_CHECKBOX);
					   if(button.getSelection()){
					     count++;
					     selectionPattern.add(tableViewer.getTable().indexOf(tableItem));				     
					    
					   }
				   }
				   
				if(count>=2){
					if(validateCheckSequence(selectionPattern)){
					 
						isEnabled=true;
					}						
				}
				
				btnAddGrp.setEnabled(isEnabled);	
		
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
		return listener;
	}
	
	
	
	private boolean validateCheckSequence(List<Integer> selectionPattern){
		
		  boolean retval=true;
		  
		  for(int i=0;i<selectionPattern.size()-1;i++){
			  
			  if((selectionPattern.get(i+1)-selectionPattern.get(i))>1){
				 
				  retval=false;
				  break;
				  
			  }
			  
		  }
			
			
			
			return retval;
		}
	
	public boolean validatUserGroupSelection(Map<Integer,List<List<Integer>>> groupSelectionMap,List<Integer> selectionList){
		
		boolean retValue=true;
		
		for (int key : groupSelectionMap.keySet()) {

			List<List<Integer>> groups = groupSelectionMap.get(key);

			for (List<Integer> grp : groups) {

				if (selectionList.size() == grp.size()) {

					if (!ListUtils.isEqualList(selectionList, grp) && ListUtils.intersection(selectionList, grp).size()==0) {

						retValue = true;
						
					}else if(ListUtils.isEqualList(selectionList, grp)){
						
						if (createErrorDialog(Messages.GROUP_CLAUSE_ALREADY_EXISTS).open() == SWT.OK) {
							retValue=false;
							break;
						}
											
					}else if(ListUtils.intersection(selectionList, grp).size() > 0){
													
						if (createErrorDialog(Messages.CANNOT_CREATE_GROUP_CLAUSE).open() == SWT.OK) {
							retValue=false;
							break;
						}
						
						}

				
					
				}else {

					if (ListUtils.isEqualList(ListUtils.intersection(selectionList, grp), grp)) {

						retValue = true;

					} else if(ListUtils.isEqualList(ListUtils.intersection(grp,selectionList),selectionList)){
					    	
					    	retValue=true;
					    	
				   }else if (ListUtils.intersection(selectionList, grp).size() == 0) {

						retValue = true;
						
						
					}else{
						
						if (createErrorDialog(Messages.CANNOT_CREATE_GROUP_CLAUSE).open() == SWT.OK) {
						
						retValue=false;
						break;
						}
					}

				}

			}
			
			if(!retValue){ break; }

		}
		
		
		
		
		return retValue;
				
	}
	
	
	public boolean isColumnModifiable(TreeMap<Integer,List<List<Integer>>> groupSelectionMap,List<Integer> selectionList){
		
		boolean retValue = false;

		for(int i=groupSelectionMap.lastKey();i>=0;i--){
			
			retValue=true;
			
			List<List<Integer>> groups = new ArrayList<>(groupSelectionMap.get(i));
		  
			for (List<Integer> grp : groups) {
			
				if (ListUtils.intersection(selectionList, grp).size()>0) {
				
					retValue=false;
												
				}
		    }
		
			if(retValue){
			groupSelectionMap.get(i).add(selectionList);
			break;
			}
		}
		
		return retValue;
			
	}
	
	
	public MessageBox createErrorDialog(String errorMessage) {
		MessageBox messageBox = new MessageBox(new Shell(), SWT.ERROR | SWT.OK);
		messageBox.setMessage(errorMessage);
		messageBox.setText("Error");
		return messageBox;
	}
	
	
	public Color getColor(int colorIndex){
		
		Map<Integer,Color> colorMap = new HashMap();
		
		colorMap.put(0, new Color(null,255,196,196)); // Light yellow
		colorMap.put(1, new Color(null,176,255,176)); //Light green
		colorMap.put(2, new Color(null,149,255,255)); //Light blue
		colorMap.put(3, new Color(null,254,194,224)); //Light Pink
		colorMap.put(4, new Color(null,147,194,147)); 
		colorMap.put(5, new Color(null,255,81,168)); 
	
	   return colorMap.get(colorIndex);
	}
	
	public boolean refreshGroupSelections(TableViewer tableViewer, int indexOfRow,String addOrDeleteRow,TreeMap<Integer,List<List<Integer>>> groupSelectionMap){
		
		    boolean isRemoveColumn = false;
			
			for(int key:groupSelectionMap.keySet()){
				
			  List<List<Integer>> groups = groupSelectionMap.get(key);

			  boolean isNewIndexAddedorRemoved=false;
				
			  for (List<Integer> grp : groups) {
					
				 
				  List<Integer> tempGrp= new ArrayList<>(grp);
					
												
							
							if("ADD".equalsIgnoreCase(addOrDeleteRow)){

								for (int i = 0; i < grp.size(); i++) {
									
									if(grp.get(i)>=indexOfRow){
									    										
										grp.set(i, grp.get(i) + 1);
									}
								
								}
							 if(tempGrp.contains(indexOfRow)){
								if(!isNewIndexAddedorRemoved && tempGrp.get(0)!=indexOfRow){//other than starting index then add row.
									
									grp.add(tempGrp.indexOf(indexOfRow),indexOfRow);
									isNewIndexAddedorRemoved=true;
									
								   }
								 }
								
							}else if("DEL".equalsIgnoreCase(addOrDeleteRow)){
								
								if(tempGrp.contains(indexOfRow)){
									
								if(!isNewIndexAddedorRemoved){//other than starting index then add row.
									
									grp.remove(grp.indexOf(indexOfRow));
									
																		
									if(grp.size()==1){
										
										grp.clear();
									}
									
									if(reArrangeGroupsAfterDeleteRow(groupSelectionMap, grp)&& groupSelectionMap.lastKey()!=key){
										
										grp.clear();
									}
									List tempList=new ArrayList<>(); 
											for (List lst : groupSelectionMap.get(key)) {
												tempList.addAll(lst);
											}
											
											if(tempList.isEmpty()){
												
												isRemoveColumn=true;
											}
											
										
									}
									isNewIndexAddedorRemoved=true;
									
								 }
								
								
								for (int i = 0; i < grp.size(); i++) {
									
									if(grp.get(i)>=indexOfRow){
									    										
										grp.set(i, grp.get(i) -1);
									}
								
								}
							}
						 
							tempGrp.clear();	
				}
				
			}
			
			
			return isRemoveColumn;
		}
			
			
		
	public void reArrangeGroups(TreeMap<Integer, List<List<Integer>>> groupSelectionMap,List<Integer> selectionList) {
		
	
		
		List<Integer> tempList = new ArrayList<>();
		int lastKey=groupSelectionMap.lastKey();
		for (int i = lastKey; i >= 0; i--) {

			List<List<Integer>> groups =groupSelectionMap.get(i);

			for (int j=0; j<=groups.size()-1;j++) {

				if (selectionList.size()< groups.get(j).size()&& ListUtils.intersection(selectionList, groups.get(j)).size() > 0) {
					
					
					tempList.addAll(groups.get(j));
					groups.get(j).clear();
					groups.set(j,new ArrayList<Integer>(selectionList));					
					selectionList.clear();
					selectionList.addAll(tempList);
					
				
			}
			tempList.clear();
		  }
		}
	}
	
	public boolean reArrangeGroupsAfterDeleteRow(
			TreeMap<Integer, List<List<Integer>>> groupSelectionMap,
			List<Integer> selectionList) {

		boolean retValue = false;

		int lastKey = groupSelectionMap.lastKey();
		int count = 0;

		for (int i = lastKey; i >= 0; i--) {

			List<List<Integer>> groups = groupSelectionMap.get(i);

			for (int j = 0; j <= groups.size() - 1; j++) {

				if (selectionList.size() == groups.get(j).size()
						&& ListUtils.isEqualList(selectionList, groups.get(j))) {

					count++;

					if (count >= 2) {
						retValue = true;
					}

				}

			}

		}
		return retValue;
	}
	
	
	
	public void disposeAllColumns(TableViewer tableViewer){
		 TableColumn[] columns = tableViewer.getTable().getColumns();
		 TableItem[] items = tableViewer.getTable().getItems();
		 for (int i = 0; i < items.length; i++) {
			 items[i].dispose();
		 }
	    
	    for (TableColumn tc : columns) {
	    	tc.dispose();
		}
	}	
	 
	 public void reArrangeGroupColumns(TreeMap<Integer, List<List<Integer>>> groupSelectionMap){
		 
		 TreeMap<Integer, List<List<Integer>>> tempMap = new TreeMap(groupSelectionMap);
		 	
		 for(int key:tempMap.keySet()){
				
			  List<List<Integer>> groups = tempMap.get(key);
	
			   List tempList=new ArrayList<>();
				
			  for (List<Integer> grp : groups) {
				
				  	tempList.addAll(grp);
					
			  	}
			  
			  if(tempList.isEmpty()){
				 
				for(int i=key ;i<tempMap.size()-1;i++){
					
					groupSelectionMap.put(i, tempMap.get(i+1));
					
				}
				
				groupSelectionMap.remove(groupSelectionMap.lastKey());
			  
			  }
			  
			  
		 	}
		 
	 }
 
}

