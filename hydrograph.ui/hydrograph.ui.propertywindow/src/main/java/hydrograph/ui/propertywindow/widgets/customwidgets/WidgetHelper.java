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

 
package hydrograph.ui.propertywindow.widgets.customwidgets;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.factory.ListenerFactory.Listners;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.DropDownConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.EditButtonWithLabelConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.RuntimeConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.SingleColumnGridConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.TextBoxWithLableConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;

import java.util.List;


/**
 * Helper class to provide configuration for customizing widgets.
 * Customizing can be of label, types of listeners etc. 
 * @author BITWISE
 *
 */
public class WidgetHelper {
	public static final WidgetHelper INSTANCE = new WidgetHelper();
	private WidgetHelper(){
	}

	public WidgetConfig getColumnNameConfig(){
		return populateSingleColumnGridConfig(Messages.LABEL_KEY_FIELDS, Constants.KEY_FIELDS_WINDOW_TITLE);
	}
	
	public WidgetConfig getPartitionKeysConfig(){
		return populateSingleColumnGridConfig(Messages.LABEL_PARTITION_KEYS, Constants.PARTITION_KEYS_WINDOW_TITLE);
	}
	
	public WidgetConfig getPartitionKeysConfigInputHive(){
		return populateSingleColumnGridConfig(Messages.LABEL_PARTITION_KEYS, Constants.PARTITION_KEYS_WINDOW_TITLE);
	}

	public WidgetConfig getOperationFieldsConfig(){
		return populateSingleColumnGridConfig(Messages.LABEL_OPERATION_FIELDS, Constants.OPERATION_FIELDS_WINDOW_TITLE);
	}
	
	/**
	 * Configuration to customize text box as delimiter property 
	 */
	public WidgetConfig getDelimiterWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_DELIMITER);
		addDelimiterTextBoxListeners(textBoxConfig);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as quote property 
	 */
	public WidgetConfig getQuoteWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_QUOTE);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as count property 
	 */
	public WidgetConfig getCountWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_COUNT);
		textBoxConfig.getListeners().add(Listners.MODIFY);
		textBoxConfig.getListeners().add(Listners.EVENT_CHANGE);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC_OR_PARAMETER_FOCUS_IN);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC_OR_PARAMETER_FOCUS_OUT);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as batch property 
	 */
	public WidgetConfig getBatchWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.BATCH);
		textBoxConfig.setCharacterLimit(2);
		addTextBoxListeners(textBoxConfig);
		textBoxConfig.getListeners().add(Listners.VERIFY_DIGIT_LIMIT_NUMERIC_LISTENER);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as User Name property 
	 */
	public WidgetConfig getUserNameWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_USER_NAME);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as Password property 
	 */
	public WidgetConfig getPasswordWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_PASSWORD);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	
	/**
	 * Configuration to customize text box as Password property 
	 */
	public WidgetConfig getQueryWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_QUERY);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as JDBC URL property 
	 */
	public WidgetConfig getJDBCURLWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_JDBC_URL);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as Batch Size property 
	 */
	public WidgetConfig getBatchSizeWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_BATCH_SIZE);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as Table Name property 
	 */
	public WidgetConfig getTableNameWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_TABLE_NAME);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as Database Name property 
	 */
	public WidgetConfig getDatabaseNameWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_DATABASE_NAME);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as Database Type property 
	 */
	public WidgetConfig getDatabaseTypeWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_DATABASE_TYPE);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as External Table Path property 
	 */
	public WidgetConfig getExternalTablePathWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_EXTERNAL_TABLE_PATH);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as noOfRecords property 
	 */
	public WidgetConfig getNoOfRecordsWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Messages.LABEL_NO_OF_RECORDS);
		textBoxConfig.setCharacterLimit(10);
		addTextBoxListeners(textBoxConfig);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC);
		textBoxConfig.getListeners().add(Listners.VERIFY_DIGIT_LIMIT_NUMERIC_LISTENER);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration for sequence widget
	 */
	public WidgetConfig getSequenceFieldWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.SEQUENCE_FIELD);
		addTextBoxListeners(textBoxConfig);
		textBoxConfig.getListeners().add(Listners.VERIFY_SEQUENCE_FIELD_NAME_EXISTS);
		textBoxConfig.setWidgetWidth(180);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as count property 
	 */
	public WidgetConfig getInputCountWidgetConfig(String propertyLabel,int minimumPortCount){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(propertyLabel);
		List<Listners> listeners = textBoxConfig.getListeners();
		String portCount = "1";
		if (minimumPortCount > 0) {
			portCount = String.valueOf(minimumPortCount);
		}
		textBoxConfig.getOtherAttributes().put(HelperType.MINIMUM_PORT_COUNT.toString(), portCount);
		listeners.add(Listners.VERIFY_NUMERIC);
		listeners.add(Listners.JOIN_INPUT_COUNT);
		listeners.add(Listners.JOIN_INPUT_COUNT_FOCUS_OUT);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize dropdown as safe property 
	 */
	public WidgetConfig getSafeWidgetConfig(){
		DropDownConfig dropDownConfig = populateTrueFalseConfig(Messages.LABEL_SAFE_PROPERTY);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	/**
	 * Configuration to customize dropdown as hasHeader property 
	 */
	public WidgetConfig getHasHeaderWidgetConfig(){
		DropDownConfig dropDownConfig =  populateTrueFalseConfig(Messages.LABEL_HAS_HEADER);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}

	/**
	 * Configuration to customize dropdown as overWrite property 
	 */
	public WidgetConfig getOverWriteWidgetConfig(){
		DropDownConfig dropDownConfig =  populateTrueFalseConfig(Messages.LABEL_OVERWRITE);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	/**
	 * Configuration to customize dropdown as strict property 
	 */
	public WidgetConfig getStrictWidgetConfig(){
		DropDownConfig dropDownConfig =  populateTrueFalseConfig(Messages.LABEL_STRICT);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	/**
	 * Configuration to customize dropdown as characterSet property 
	 */
	public WidgetConfig getCharacterSetWidgetConfig(){
		DropDownConfig dropDownConfig = new DropDownConfig();
		dropDownConfig.setName(Messages.LABEL_CHARACTER_SET);
		dropDownConfig.getItems().add(Constants.UTF_8);
		dropDownConfig.getItems().add(Constants.US_ASCII);
		dropDownConfig.getItems().add(Constants.ISO_8859_1);
		dropDownConfig.getItems().add(Constants.IUTF_16BE);
		dropDownConfig.getItems().add(Constants.UTF_16LE);
		dropDownConfig.getItems().add(Constants.UTF_16);
		dropDownConfig.getItems().add(Constants.PARAMETER);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	/**
	 * Configuration for operation class widget
	 */
	public WidgetConfig getOperationClassForFilterWidgetConfig() {
		OperationClassConfig operationClassConfig = new OperationClassConfig();
		operationClassConfig.setComponentName(Constants.FILTER);
		return operationClassConfig;
	}
	
	/**
	 * Configuration for Transform component operation class widget
	 */
	public WidgetConfig getOperationClassForTransformWidgetConfig(String componentName, String componentDisplayName,String windowTitle) {
		OperationClassConfig operationClassConfig = new OperationClassConfig();
		operationClassConfig.setComponentName(componentName);
		operationClassConfig.setComponentDisplayName(componentDisplayName);
		operationClassConfig.setWindowTitle(windowTitle);
		return operationClassConfig;
	}
	
	/**
	 * Configuration for table as Primary key table
	 */
	public WidgetConfig getPrimaryKeyWidgetConfig() {
		EditButtonWithLabelConfig buttonWithLabelConfig = new EditButtonWithLabelConfig();
		buttonWithLabelConfig.setName(Messages.LABEL_KEY_FIELDS);
		buttonWithLabelConfig.setWindowName(Messages.PRIMARY_COLUMN_KEY_WINDOW_NAME);
		return buttonWithLabelConfig;
	}
	
	/**
	 * Configuration for table as PrimaSecondary key table
	 */
	public WidgetConfig getSecondaryKeyWidgetConfig() {
		EditButtonWithLabelConfig buttonWithLabelConfig = new EditButtonWithLabelConfig();
		buttonWithLabelConfig.setName(Messages.LABEL_SECONDARY_KEYS);
		buttonWithLabelConfig.setWindowName(Messages.SECONDARY_COLUMN_KEY_WINDOW_NAME);
		return buttonWithLabelConfig;
	}
		
	private SingleColumnGridConfig populateSingleColumnGridConfig(String lable, String componentName) {
		SingleColumnGridConfig gridConfig = new SingleColumnGridConfig();
		gridConfig.setLabelName(lable);
		gridConfig.setComponentName(componentName);
		return gridConfig;		
	}
	
	private void addComboBoxListeners(DropDownConfig dropDownConfig) {
		List<Listners> dropDownListeners = dropDownConfig.getDropDownListeners();
		dropDownListeners.add(Listners.SELECTION);
		
		List<Listners> textBoxListeners = dropDownConfig.getTextBoxListeners();
		textBoxListeners.add(Listners.EVENT_CHANGE);
		textBoxListeners.add(Listners.VERIFY_TEXT);
		textBoxListeners.add(Listners.FOCUS_OUT);
		textBoxListeners.add(Listners.FOCUS_IN);
		textBoxListeners.add(Listners.MODIFY);
	}
	
	private void addTextBoxListeners(TextBoxWithLableConfig textBoxConfig) {
		List<Listners> listeners = textBoxConfig.getListeners();
		listeners.add(Listners.NORMAL_FOCUS_IN);
		listeners.add(Listners.NORMAL_FOCUS_OUT);
		listeners.add(Listners.EVENT_CHANGE);
		listeners.add(Listners.MODIFY);
	}
	
	private void addDelimiterTextBoxListeners(TextBoxWithLableConfig textBoxConfig) {
		List<Listners> listeners = textBoxConfig.getListeners();
		listeners.add(Listners.DELIMITER_FOCUS_IN);
		listeners.add(Listners.DELIMITER_FOCUS_OUT);
		listeners.add(Listners.EVENT_CHANGE);
		listeners.add(Listners.DELIMITER_MODIFY);
	}
	
	private DropDownConfig populateTrueFalseConfig(String name){
		DropDownConfig dropDownConfig = new DropDownConfig();
		dropDownConfig.setName(name);
		dropDownConfig.getItems().add(Constants.FALSE);
		dropDownConfig.getItems().add(Constants.TRUE);
		dropDownConfig.getItems().add(Constants.PARAMETER);
		return dropDownConfig;
	}
	
	public WidgetConfig getRunTimeWidgetConfig(String label,String windowLabel) {
		RuntimeConfig runtimeConfig = new RuntimeConfig();
		runtimeConfig.setLabel(label);
		runtimeConfig.setWindowLabel(windowLabel);
		return runtimeConfig;
	}

	public WidgetConfig getRunInputWidgetConfig() {
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.INPUT_DATA);
		textBoxConfig.setWidgetWidth(250);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	public WidgetConfig getRunModelWidgetConfig() {
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.MODEL_DATA);
		textBoxConfig.setWidgetWidth(250);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	public WidgetConfig getRunOutputWidgetConfig() {
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.OUTPUT_DATA);
		textBoxConfig.setWidgetWidth(250);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	public WidgetConfig getRunProgramWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.COMMAND);
		textBoxConfig.setWidgetWidth(250);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	public WidgetConfig getRunTrainingWidgetConfig() {
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.TRAINING_DATA);
		textBoxConfig.setWidgetWidth(250);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}

	public WidgetConfig getRunTestWidgetConfig() {
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.TEST_DATA);
		textBoxConfig.setWidgetWidth(250);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	public WidgetConfig getThresholdOutputWidgetConfig() {
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.THRESHOLD_DATA);
		textBoxConfig.setWidgetWidth(250);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}	

}
