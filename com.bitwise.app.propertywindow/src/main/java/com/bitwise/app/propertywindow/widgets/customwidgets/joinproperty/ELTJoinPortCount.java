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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.joinproperty;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.TextBoxWithLabelWidget;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

public class ELTJoinPortCount extends TextBoxWithLabelWidget{

	private String unusedPortPropertyName;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTJoinPortCount.class);
	private int minimunPortCount;
	private String firstPortPropertyName;
	protected ComponentConfigrationProperty additionalComponentConfigrationProperty;

	public ELTJoinPortCount(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		setPropertyNames();
	}

	private void setPropertyNames() {
		String propertyNameArray[] = StringUtils.split(this.propertyName, "|");
		if (StringUtils.isNotEmpty(propertyNameArray[0]))
			firstPortPropertyName = propertyNameArray[0];

		if (propertyNameArray.length == 2
				&& StringUtils.equals(Constants.UNUSED_PORT_COUNT_PROPERTY, propertyNameArray[1]))
			this.unusedPortPropertyName = propertyNameArray[1];

	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		try {
			if(Integer.parseInt(textBox.getText()) < minimunPortCount || Integer.parseInt(textBox.getText()) > 25 ){
				property.put(propertyName, textBox.getText());
				property.put(firstPortPropertyName, textBox.getText());
				if (StringUtils.isNotEmpty(unusedPortPropertyName))
					property.put(unusedPortPropertyName, textBox.getText());
				setToolTipErrorMessage();
			}else{
				property.put(propertyName, textBox.getText());
				property.put(firstPortPropertyName, textBox.getText());
				if (StringUtils.isNotEmpty(unusedPortPropertyName))
					property.put(unusedPortPropertyName, textBox.getText());
			}	
		}
		catch (NumberFormatException nfe) {
			logger.error("Error while saving port Count. Numerical value expected", nfe);
		}

		return property;

	}

	protected ListenerHelper prepareListenerHelper() {
		minimunPortCount=Integer.parseInt(textBoxConfig.getOtherAttributes().get(HelperType.MINIMUM_PORT_COUNT.toString()));
		ListenerHelper helper = super.prepareListenerHelper();
		helper.put(HelperType.MINIMUM_PORT_COUNT,
				textBoxConfig.getOtherAttributes().get(HelperType.MINIMUM_PORT_COUNT.toString()));
		return helper;
	}

}
