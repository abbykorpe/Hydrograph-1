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
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.google.gson.Gson;

public class FilterHelper {
	
	private static final String TYPE_DATE = "java.util.Date";
	private static final String TYPE_STRING = "java.lang.String";
	public static final FilterHelper INSTANCE = new FilterHelper();
	private String filterType;
	private DataViewerAdapter dataViewerAdapter;
	private DebugDataViewer debugDataViewer;
	private FilterConditionsDialog filterConditionsDialog;
	private String SCHEMA_FILE_EXTENTION=".xml";
	private String filteredFileLocation;
	private String filteredFileName;
	private FilterHelper() {
	}
	
	public  Listener getTextBoxListener(final List<Condition> conditionsList) {
		Listener listener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Text text = (Text)event.widget;
				int index = (int) text.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				filterConditions.setValue(text.getText());
			}
		};
		return listener;
	}
	
	public SelectionListener getFieldNameSelectionListener(final TableViewer tableViewer, final List<Condition> conditionsList,
			final Map<String, String> fieldsAndTypes, final Map<String,String[]> typeBasedConditionalOperators) {
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
					conditionalCombo.setItems(typeBasedConditionalOperators.get(fieldType));
					//validateCombo(conditionalCombo);
				}
				validateCombo(source);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener getConditionalOperatorSelectionListener(final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo source = (Combo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				filterConditions.setConditionalOperator(source.getText());
				validateCombo(source);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener getRelationalOpSelectionListener(final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo source = (Combo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				filterConditions.setRelationalOperator(source.getText());
				validateCombo(source);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
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
			
		/*	private void updateIndexes(TableItem tabItem, int index) {
				Button addButton = (Button) tabItem.getData(ADD);
				addButton.setData(ROW_INDEX, index);
				Button removeButton = (Button) tabItem.getData(REMOVE);
				removeButton.setData(ROW_INDEX, index);
				
				Combo conditionalCombo = (Combo) tabItem.getData(CONDITIONAL_OPERATORS);
				conditionalCombo.setData(ROW_INDEX, index);
				Combo fieldNamesCombo = (Combo) tabItem.getData(FIELD_NAMES);
				fieldNamesCombo.setData(ROW_INDEX, index);
				Combo relationalOperatorsCombo = (Combo) tabItem.getData(RELATIONAL_OPERATORS);
				relationalOperatorsCombo.setData(ROW_INDEX, index);
				
				Text text = (Text)tabItem.getData(VALUE_TEXT_BOX);
				text.setData(ROW_INDEX, index);
			}*/
			
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
			
			/*private void updateIndexes(TableItem tabItem, int index) {
				Button addButton = (Button) tabItem.getData(ADD);
				addButton.setData(ROW_INDEX, index);
				Button removeButton = (Button) tabItem.getData(REMOVE);
				removeButton.setData(ROW_INDEX, index);
				
				Combo conditionalCombo = (Combo) tabItem.getData(CONDITIONAL_OPERATORS);
				conditionalCombo.setData(ROW_INDEX, index);
				Combo fieldNamesCombo = (Combo) tabItem.getData(FIELD_NAMES);
				fieldNamesCombo.setData(ROW_INDEX, index);
				Combo relationalOperatorsCombo = (Combo) tabItem.getData(RELATIONAL_OPERATORS);
				relationalOperatorsCombo.setData(ROW_INDEX, index);
				
				Text text = (Text)tabItem.getData(VALUE_TEXT_BOX);
				text.setData(ROW_INDEX, index);
			}*/
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener getOkButtonListener(final List<Condition> conditionsList, final Map<String, String> fieldsAndTypes) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer buffer = new StringBuffer();
				for(int index = 0; index < conditionsList.size(); index++){
					Condition condition = conditionsList.get(index);
					if(index !=0){
						buffer.append(" ").append(condition.getRelationalOperator()).append(" ");
					}
					buffer.append(condition.getFieldName()).append(" ")
					.append(condition.getConditionalOperator()).append(" ")
					.append(getConditionValue(condition.getFieldName(), condition.getValue(), condition.getConditionalOperator(),
							fieldsAndTypes));
				}
				
				System.out.println(buffer);
				if(filterType!=null && filterType.equalsIgnoreCase("local"))
				{
					showLocalFilteredData(buffer);
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
					exception.printStackTrace();
				}
			}

			private void showLocalFilteredData(StringBuffer buffer) {
				try {
					dataViewerAdapter.setFilterCondition(buffer.toString());
					dataViewerAdapter.initializeTableData();
					debugDataViewer.getDataViewLoader().updateDataViewLists();
					debugDataViewer.getDataViewLoader().reloadloadViews();

				} catch (SQLException e1) {
					e1.printStackTrace();
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
		if(TYPE_STRING.equalsIgnoreCase(dataType) || 
				TYPE_DATE.equalsIgnoreCase(dataType)){
			if("in".equalsIgnoreCase(trimmedCondition) || "not in".equalsIgnoreCase(trimmedCondition)){
				return "('" + value + "')";
			}
			else{
				return "'" + value + "'";
			}
		}
		else{
			if("in".equalsIgnoreCase(trimmedCondition) || "not in".equalsIgnoreCase(trimmedCondition)){
				return "(" + value + ")";
			}
			else{
				return value;
			}
		}
	}

	public ModifyListener getTextModifyListener() {
		return new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text)e.widget;
				validateText(text);
			}
		};
	}
	
	public ModifyListener getComboModifyListener() {
		return new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Combo combo = (Combo)e.widget;
				validateCombo(combo);
			}
		};
	}

	public SelectionListener getComboSelectionListener() {
		return new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = (Combo)e.widget;
				validateCombo(combo);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
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
	
	private boolean validateText(Text text) {
		if(StringUtils.isNotBlank(text.getText())){
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

	public SelectionListener getApplyButtonListener(final FilterConditions originalFilterConditions,
			final List<Condition> remoteConditionsList, final RetainFilter retainFilter) {
		SelectionListener listner = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				originalFilterConditions.setRemoteConditions(remoteConditionsList);
				originalFilterConditions.setRetainRemote(retainFilter.getRetainFilter());
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
}
