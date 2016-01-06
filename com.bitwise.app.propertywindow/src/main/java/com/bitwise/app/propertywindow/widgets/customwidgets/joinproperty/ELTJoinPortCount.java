package com.bitwise.app.propertywindow.widgets.customwidgets.joinproperty;

import java.util.LinkedHashMap;

import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.TextBoxWithLabelWidget;

public class ELTJoinPortCount extends TextBoxWithLabelWidget{

	private String unusedPortPropertyName;
	
	protected ComponentConfigrationProperty additionalComponentConfigrationProperty;
	
	public ELTJoinPortCount(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps,
			PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.unusedPortPropertyName="unusedPortCount";
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName, textBox.getText());
		property.put(unusedPortPropertyName, textBox.getText());
		setToolTipErrorMessage();
		return property;
	}
}
