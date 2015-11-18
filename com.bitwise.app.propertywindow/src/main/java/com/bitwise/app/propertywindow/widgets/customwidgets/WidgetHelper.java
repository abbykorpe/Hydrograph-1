package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.List;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.factory.ListenerFactory.Listners;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.DropDownConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.SingleColumnGridConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.TextBoxWithLableConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;

public class WidgetHelper {
	public static WidgetHelper INSTANCE = new WidgetHelper();
	private WidgetHelper(){
	}

	public WidgetConfig getColumnNameConfig(){
		return populateSingleColumnGridConfig(Constants.COLUMN_NAME, Constants.COLUMN_NAME2);
	}

	public WidgetConfig getSecondaryKeysConfig(){
		return populateSingleColumnGridConfig(Constants.SECONDARY_KEYS, Constants.SECONDARY_KEYS2);
	}

	public WidgetConfig getOperationFieldsConfig(){
		return populateSingleColumnGridConfig(Constants.OPERATION_FIELDS, Constants.OPERATION_FIELD);
	}
	
	public WidgetConfig getDelimiterWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.DELIMITER);
		textBoxConfig.setGrabExcessSpace(true);
		addTextBoxListeners(textBoxConfig);
		return textBoxConfig;
	}

	public WidgetConfig getPhaseWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.PHASE);
		addTextBoxListeners(textBoxConfig);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	public WidgetConfig getInputCountWidgetConfig(){
		TextBoxWithLableConfig textBoxConfig = new TextBoxWithLableConfig();
		textBoxConfig.setName(Constants.INPUT_COUNT);
		addTextBoxListeners(textBoxConfig);
		textBoxConfig.getListeners().add(Listners.VERIFY_NUMERIC);
		textBoxConfig.setWidgetWidth(78);
		return textBoxConfig;
	}
	
	public WidgetConfig getSafeWidgetConfig(){
		DropDownConfig dropDownConfig = populateTrueFalseConfig(Constants.SAFE_PROPERTY);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	public WidgetConfig getHasHeaderWidgetConfig(){
		DropDownConfig dropDownConfig =  populateTrueFalseConfig(Constants.HAS_HEADER);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
	public WidgetConfig getStrictWidgetConfig(){
		DropDownConfig dropDownConfig =  populateTrueFalseConfig(Constants.STRICT);
		addComboBoxListeners(dropDownConfig);
		return dropDownConfig;
	}
	
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
}
