package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.List;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.factory.ListenerFactory.Listners;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.DropDownConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.EditButtonWithLabelConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.RuntimeConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.SingleColumnGridConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.TextBoxWithLableConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

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
		return populateSingleColumnGridConfig(Constants.COLUMN_NAME, Constants.COLUMN_NAME2);
	}

	public WidgetConfig getOperationFieldsConfig(){
		return populateSingleColumnGridConfig(Constants.OPERATION_FIELDS, Constants.OPERATION_FIELD);
	}
	
	/**
	 * Configuration to customize text box as delimiter property 
	 */
	public WidgetConfig getDelimiterWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.DELIMITER);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as count property 
	 */
	public WidgetConfig getCountWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.COUNT);
		textBoxConfig.getListeners().add(Listners.MODIFY);
		textBoxConfig.getListeners().add(Listners.EVENT_CHANGE);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC_OR_PARAMETER_FOCUS_IN);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC_OR_PARAMETER_FOCUS_OUT);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as phase property 
	 */
	public WidgetConfig getPhaseWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.PHASE);
		addTextBoxListeners(textBoxConfig);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize text box as noOfRecords property 
	 */
	public WidgetConfig getNoOfRecordsWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.NO_OF_RECORDS);
		addTextBoxListeners(textBoxConfig);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC);
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
		addTextBoxListeners(textBoxConfig);
		String portCount = "1";
		if (minimumPortCount > 0) {
			portCount = String.valueOf(minimumPortCount);
		}
		textBoxConfig.getOtherAttributes().put(HelperType.MINIMUM_PORT_COUNT.toString(), portCount);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC);
		textBoxConfig.getListeners().add(Listners.JOIN_INPUT_COUNT);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	/**
	 * Configuration to customize dropdown as safe property 
	 */
	public WidgetConfig getSafeWidgetConfig(){
		DropDownConfig dropDownConfig = populateTrueFalseConfig(Constants.SAFE_PROPERTY);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	/**
	 * Configuration to customize dropdown as hasHeader property 
	 */
	public WidgetConfig getHasHeaderWidgetConfig(){
		DropDownConfig dropDownConfig =  populateTrueFalseConfig(Constants.HAS_HEADER);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	/**
	 * Configuration to customize dropdown as strict property 
	 */
	public WidgetConfig getStrictWidgetConfig(){
		DropDownConfig dropDownConfig =  populateTrueFalseConfig(Constants.STRICT);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	/**
	 * Configuration to customize dropdown as characterSet property 
	 */
	public WidgetConfig getCharacterSetWidgetConfig(){
		DropDownConfig dropDownConfig = new DropDownConfig();
		dropDownConfig.setName(Constants.CHARACTER_SET);
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
		buttonWithLabelConfig.setName(Constants.PRIMARY_KEYS);
		buttonWithLabelConfig.setWindowName(Messages.PRIMARY_COLUMN_KEY_WINDOW_NAME);
		return buttonWithLabelConfig;
	}
	
	/**
	 * Configuration for table as PrimaSecondary key table
	 */
	public WidgetConfig getSecondaryKeyWidgetConfig() {
		EditButtonWithLabelConfig buttonWithLabelConfig = new EditButtonWithLabelConfig();
		buttonWithLabelConfig.setName(Constants.SECONDARY_KEYS);
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
	
	private DropDownConfig populateTrueFalseConfig(String name){
		DropDownConfig dropDownConfig = new DropDownConfig();
		dropDownConfig.setName(name);
		dropDownConfig.getItems().add(Constants.FALSE);
		dropDownConfig.getItems().add(Constants.TRUE);
		dropDownConfig.getItems().add(Constants.PARAMETER);
		return dropDownConfig;
	}
	
	public WidgetConfig getRunTimeWidgetConfig(String label) {
		RuntimeConfig runtimeConfig = new RuntimeConfig();
		runtimeConfig.setLabel(label);
		return runtimeConfig;
	}


}
