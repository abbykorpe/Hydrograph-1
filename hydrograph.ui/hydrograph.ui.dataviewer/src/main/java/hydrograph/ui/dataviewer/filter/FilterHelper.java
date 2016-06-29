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

import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.dataviewer.adapters.DataViewerAdapter;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.filemanager.DataViewerFileManager;
import hydrograph.ui.dataviewer.utilities.ViewDataSchemaHelper;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;

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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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
	
	private static final String LOCAL = "local";
	private static final String REGEX_DIGIT = "\\d";
	private static final String SINGLE_SPACE = " ";
	private static final String OPEN_BRACKET = "(";
	private static final String CLOSE_BRACKET = ")";
	private static final String SINGLE_QOUTE = "'";
	private static final String DELIM_COMMA = ",";
	private static final String NOT_IN = "not in";
	private static final String IN = "in";
	private String filterType;
	private DataViewerAdapter dataViewerAdapter;
	private DebugDataViewer debugDataViewer;
	private FilterConditionsDialog filterConditionsDialog;
	private String SCHEMA_FILE_EXTENTION=".xml";
	private String filteredFileLocation;
	private String filteredFileName;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterHelper.class);
	private FilterHelper() {
	}
	
	public Map<String, String[]> getTypeBasedOperatorMap(){
		Map<String, String[]> typeBasedConditionalOperators = new HashMap<String, String[]>();
		typeBasedConditionalOperators.put(TYPE_STRING, new String[]{"LIKE","IN ","NOT IN"}); 
		typeBasedConditionalOperators.put(TYPE_INTEGER, new String[]{">", "<", "<=", ">=", "<>", "=", "LIKE", "IN", "NOT IN"}); 
		typeBasedConditionalOperators.put(TYPE_DATE, new String[]{">", "<", "<=",">=", "<>", "=", "LIKE", "IN", "NOT IN"}); 
		typeBasedConditionalOperators.put(TYPE_BIGDECIMAL, new String[]{">", "<", "<=", ">=", "<>", "=", "LIKE", "IN","NOT IN"});
		typeBasedConditionalOperators.put(TYPE_LONG, new String[]{">", "<", "<=", ">=", "<>", "=", "LIKE", "IN", "NOT IN"});
		typeBasedConditionalOperators.put(TYPE_SHORT, new String[]{">", "<", "<=", ">=", "<>", "=", "LIKE", "IN", "NOT IN"});
		typeBasedConditionalOperators.put(TYPE_FLOAT, new String[]{">", "<", "<=", ">=", "<>", "=", "LIKE", "IN", "NOT IN"});
		typeBasedConditionalOperators.put(TYPE_DOUBLE, new String[]{">", "<", "<=", ">=", "<>", "=", "LIKE", "IN", "NOT IN"});
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
				validateText(text, filterConditions.getFieldName(), fieldsAndTypes);
				toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
		};
		return listener;
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
				Combo source = (Combo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				String fieldName = source.getText();
				filterConditions.setFieldName(fieldName);
				
				if(StringUtils.isNotBlank(fieldName)){
					String fieldType = fieldsAndTypes.get(fieldName);
					TableItem item = tableViewer.getTable().getItem(index);
					Combo conditionalCombo = (Combo) item.getData(FilterConditionsDialog.CONDITIONAL_OPERATORS);
					conditionalCombo.setItems(FilterHelper.INSTANCE.getTypeBasedOperatorMap().get(fieldType));
					new AutoCompleteField(conditionalCombo, new ComboContentAdapter(), conditionalCombo.getItems());
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
				Combo source = (Combo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				String fieldName = source.getText();
				filterConditions.setFieldName(fieldName);
				
				if(StringUtils.isNotBlank(fieldName)){
					String fieldType = fieldsAndTypes.get(fieldName);
					TableItem item = tableViewer.getTable().getItem(index);
					Combo conditionalCombo = (Combo) item.getData(FilterConditionsDialog.CONDITIONAL_OPERATORS);
					if(conditionalCombo != null && StringUtils.isNotBlank(fieldType)){
						conditionalCombo.setText(filterConditions.getConditionalOperator());
						conditionalCombo.setItems(FilterHelper.INSTANCE.getTypeBasedOperatorMap().get(fieldType));
						new AutoCompleteField(conditionalCombo, new ComboContentAdapter(), conditionalCombo.getItems());
					}
				}
				validateCombo(source);
				toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
		};
		return listener;
	}
	
/*	private void processFieldName(TableViewer tableViewer, Combo source, List<Condition> conditionsList, Map<String, String> fieldsAndTypes, String[] fieldNames, Button okButton, Button applyButton){
	
	}*/
	
	public SelectionListener getConditionalOperatorSelectionListener(final List<Condition> conditionsList, 
			final Map<String, String> fieldsAndTypes, final String[] fieldNames, final Button okButton, final Button applyButton) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo source = (Combo) e.getSource();
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
				Combo source = (Combo) e.getSource();
				processConditionalOperator(source, conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
		};
		return listener;
	}
	
	private void processConditionalOperator(Combo source, List<Condition> conditionsList, Map<String, String> fieldsAndTypes,
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
				Combo source = (Combo) e.getSource();
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
				Combo source = (Combo) e.getSource();
				processRelationalOperator(source, conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
			}
			
		};
		return listener;
	}
	
	private void processRelationalOperator(Combo source, List<Condition> conditionsList, Map<String, String> fieldsAndTypes,
			String[] fieldNames, Button okButton, Button applyButton){
		int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
		Condition filterConditions = conditionsList.get(index);
		filterConditions.setRelationalOperator(source.getText());
		if(index != 0){
			validateCombo(source);
		}
		toggleOkApplyButton(conditionsList, fieldsAndTypes, fieldNames, okButton, applyButton);
	}
	
	public SelectionListener addButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.getSource();
				int index = (int) button.getData(FilterConditionsDialog.ROW_INDEX);
				conditionsList.add(index, new Condition());
				tableViewer.refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}

	public SelectionListener removeButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(conditionsList.size() > 1){
					Button button = (Button) e.getSource();
					int removeIndex = (int) button.getData(FilterConditionsDialog.ROW_INDEX);
					
					conditionsList.remove(removeIndex);
				}
				tableViewer.refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener getOkButtonListener(final List<Condition> conditionsList, final Map<String, String> fieldsAndTypes/*,
			final Map<Integer,List<List<Integer>>> groupSelectionMap*/) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO : temp for compilation
				Map<Integer,List<List<Integer>>> groupSelectionMap = new TreeMap<Integer, List<List<Integer>>>();
			
				//put number of elements in the list
				//1 2 3 4 5
				List<String> actualStringList = new LinkedList<>();
				for (int conditionIndex = 0; conditionIndex < conditionsList.size(); conditionIndex++) {
					actualStringList.add(conditionIndex, String.valueOf((conditionIndex + 1)));
				}
				//start adding brackets for grouping
				Set<Integer> treeSet  = (Set<Integer>) groupSelectionMap.keySet();
				if(treeSet.size() > 0){
					for (Integer position : treeSet) {
					List<List<Integer>> groupsInColumn = groupSelectionMap.get(position);
						for (int groupIndex = 0; groupIndex < groupsInColumn.size(); groupIndex++) {
							List<Integer> group = groupsInColumn.get(groupIndex);
							//add opening bracket before first element in the group
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
				
				//start adding relational operators
				int indexOfRelational = 1;
				//start from 2nd index
				for (int item = 2; item <= conditionsList.size(); item++) {
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
				}
				
				StringBuffer buffer = new StringBuffer();
				for(int item = 0; item < conditionsList.size(); item++){
					StringBuffer conditionString = new StringBuffer();
					
					Condition condition = conditionsList.get(item);
					conditionString.append(condition.getFieldName()).append(SINGLE_SPACE).append(condition.getConditionalOperator()).append(SINGLE_SPACE)
					.append(getConditionValue(condition.getFieldName(), condition.getValue(), condition.getConditionalOperator(),
							fieldsAndTypes));
					int index = actualStringList.indexOf(String.valueOf(item + 1));
					actualStringList.set(index, conditionString.toString());
				}
				
				for (String item : actualStringList) {
					buffer.append(item + SINGLE_SPACE);
				}
				System.out.println(buffer);
				
				if(filterType!=null && filterType.equalsIgnoreCase(LOCAL))
				{
					showLocalFilteredData(buffer);
					try {

						dataViewerAdapter.setFilterCondition(StringUtils.trim(buffer.toString()));
						dataViewerAdapter.initializeTableData();
						debugDataViewer.getDataViewLoader().updateDataViewLists();
						debugDataViewer.getDataViewLoader().reloadloadViews();

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				else
				{
					showRemoteFilteredData(buffer);
				}
			
				filterConditionsDialog.close();
			}

			private void showRemoteFilteredData(StringBuffer buffer) {
				try {
					String filterJson = createJsonObjectForRemoteFilter(buffer);
					String filteredFilePath=DebugServiceClient.INSTANCE.getFilteredFile(filterJson, debugDataViewer.getJobDetails());
					DataViewerFileManager dataViewerFileManager=new DataViewerFileManager();
					dataViewerFileManager.downloadDataViewerFilterFile(filteredFilePath,debugDataViewer.getJobDetails());
					filteredFileName = dataViewerFileManager.getDataViewerFileName();
					filteredFileLocation = dataViewerFileManager.getDataViewerFilePath();
					debugDataViewer.setDebugFileLocation(filteredFileLocation);
					debugDataViewer.setDebugFileName(filteredFileName);
					debugDataViewer.showDataInDebugViewer(true);
					
				} catch (NumberFormatException | IOException exception) {
					logger.error("Error occuring while showing remote filtered data",exception);
				}
			}

			private void showLocalFilteredData(StringBuffer buffer) {
				try {
					dataViewerAdapter.setFilterCondition(buffer.toString());
					dataViewerAdapter.initializeTableData();
					debugDataViewer.getDataViewLoader().updateDataViewLists();
					debugDataViewer.getDataViewLoader().reloadloadViews();

				} catch (SQLException exception) {
					logger.error("Error occuring while showing local filtered data",exception);
				}
			}

			private String createJsonObjectForRemoteFilter(StringBuffer buffer) {
				Gson gson=new Gson();
				RemoteFilterJson remoteFilterJson=new RemoteFilterJson();
				remoteFilterJson.setCondition(buffer.toString());
				remoteFilterJson.setSchema(getSchema());
				remoteFilterJson.setFileSize(debugDataViewer.getViewDataPreferences().getFileSize());
				remoteFilterJson.setJobDetails(debugDataViewer.getJobDetails());
				String filterJson=gson.toJson(remoteFilterJson);
				return filterJson;
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public List<GridRow> getSchema() {
		List<GridRow> gridRowList = new ArrayList<>();
		String debugFileName = debugDataViewer.getDebugFileName();
		String debugFileLocation = debugDataViewer.getDebugFileLocation();

		Fields dataViewerFileSchema = ViewDataSchemaHelper.INSTANCE
				.getFieldsFromSchema(debugFileLocation + debugFileName
						+ SCHEMA_FILE_EXTENTION);
		for (Field field : dataViewerFileSchema.getField()) {
			GridRow gridRow = new GridRow();

			gridRow.setFieldName(field.getName());
			gridRow.setDataType(GridWidgetCommonBuilder
					.getDataTypeByValue(field.getType().value()));
			gridRow.setDataTypeValue(field.getType().value());

			if (StringUtils.isNotEmpty(field.getFormat())) {
				gridRow.setDateFormat(field.getFormat());
			} else {
				gridRow.setDateFormat("");
			}
			if (field.getPrecision() != null) {
				gridRow.setPrecision(String.valueOf(field.getPrecision()));
			} else {
				gridRow.setPrecision("");
			}
			if (field.getScale() != null) {
				gridRow.setScale(Integer.toString(field.getScale()));
			} else {
				gridRow.setScale("");
			}

			if (StringUtils.isNotEmpty(field.getDescription()))
				gridRow.setDescription(field.getDescription());
			else {
				gridRow.setDescription("");
			}
			if (field.getScaleType() != null) {
				gridRow.setScaleType(GridWidgetCommonBuilder
						.getScaleTypeByValue(field.getScaleType().value()));
				gridRow.setScaleTypeValue(GridWidgetCommonBuilder
						.getScaleTypeValue()[GridWidgetCommonBuilder
						.getScaleTypeByValue(field.getScaleType().value())]);
			} else {
				gridRow.setScaleType(GridWidgetCommonBuilder
						.getScaleTypeByValue(Messages.SCALE_TYPE_NONE));
				gridRow.setScaleTypeValue(GridWidgetCommonBuilder
						.getScaleTypeValue()[Integer
						.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]);
			}

			gridRowList.add(gridRow);
		}
		return gridRowList;
}

	public void setFilterType(String filterType) {
		this.filterType=filterType;
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

	private boolean validateCombo(Combo combo){
		if((Arrays.asList(combo.getItems())).contains(combo.getText())){
			combo.setBackground(new Color(null, 255, 255, 255));
			return true;
		}else {
			combo.setBackground(new Color(null, 255, 244, 113));
			return false;
		}
	}
	
	private boolean validateText(Text text, String fieldName, Map<String, String> fieldsAndTypes) {
		String type = FilterValidator.INSTANCE.getType(fieldName, fieldsAndTypes);
		if(StringUtils.isNotBlank(text.getText()) && FilterValidator.INSTANCE.validateDataBasedOnTypes(type, text.getText())){
			text.setBackground(new Color(null, 255, 255, 255));
			return true;
		}else {
			text.setBackground(new Color(null, 255, 244, 113));
			return false;
		}
	}

	public SelectionListener getClearButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList) {
		SelectionListener listner = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				conditionsList.clear();
				TableItem[] items = tableViewer.getTable().getItems();

				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}
				conditionsList.add(0, new Condition());
				tableViewer.refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		return listner;
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
   
	public SelectionAdapter getAddAtEndListener(final TableViewer tableViewer, final List<Condition> conditionList) {
        return new SelectionAdapter() {
              @Override
              public void widgetSelected(SelectionEvent e) {
                    conditionList.add(conditionList.size(), new Condition());
                    tableViewer.refresh();
              }
        };
  }

}
