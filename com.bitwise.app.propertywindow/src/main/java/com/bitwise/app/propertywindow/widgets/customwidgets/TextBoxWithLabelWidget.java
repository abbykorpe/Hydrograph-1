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

 
package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.factory.ListenerFactory.Listners;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.TextBoxWithLableConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.IELTListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * Widget for showing text box in property window.
 * 
 * @author Bitwise
 */
public class TextBoxWithLabelWidget extends AbstractWidget{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(TextBoxWithLabelWidget.class);
	protected Text textBox;
	protected String propertyValue;
	protected String propertyName;
	protected ControlDecoration txtDecorator;
	protected TextBoxWithLableConfig textBoxConfig;
	protected ELTDefaultSubgroupComposite lableAndTextBox ;
	/**
	 * Instantiates a new text box widget with provided configurations
	 * 
	 * @param componentConfigProp
	 *            the component configration property
	 * @param componentMiscProps
	 *            the component miscellaneous properties
	 * @param propDialogButtonBar
	 *            the property dialog button bar
	 */
	public TextBoxWithLabelWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propertyName = componentConfigProp.getPropertyName();
		this.propertyValue =  String.valueOf(componentConfigProp.getPropertyValue());
	}

	protected void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
				
		if(txtDecorator.isVisible())
			toolTipErrorMessage = txtDecorator.getDescriptionText();
		
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName, textBox.getText());
		setToolTipErrorMessage();
		return property;
	}
	
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		textBoxConfig = (TextBoxWithLableConfig) widgetConfig;
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		logger.debug("Starting {} textbox creation", textBoxConfig.getName());
		lableAndTextBox = new ELTDefaultSubgroupComposite(container.getContainerControl());
		lableAndTextBox.createContainerWidget();
		
		AbstractELTWidget label = new ELTDefaultLable(textBoxConfig.getName() + " ");
		lableAndTextBox.attachWidget(label);
		setPropertyHelpWidget((Control) label.getSWTWidgetControl());
		
		AbstractELTWidget textBoxWidget = new ELTDefaultTextBox().
				grabExcessHorizontalSpace(textBoxConfig.getGrabExcessSpace()).textBoxWidth(textBoxConfig.getwidgetWidth());
		lableAndTextBox.attachWidget(textBoxWidget);
		
		textBox = (Text) textBoxWidget.getSWTWidgetControl();
		txtDecorator = WidgetUtility.addDecorator(textBox, Messages.bind(Messages.EMPTY_FIELD, textBoxConfig.getName()));
		txtDecorator.setMarginWidth(3);
		
		ListenerHelper helper = prepareListenerHelper();
		
		try {
			for (Listners listenerNameConstant : textBoxConfig.getListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				textBoxWidget.attachListener(listener, propertyDialogButtonBar, helper, textBoxWidget.getSWTWidgetControl());
			}
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", textBoxConfig.getName(), exception);
		}
		
		populateWidget();
		logger.debug("Finished {} textbox creation", textBoxConfig.getName());
	}

	protected ListenerHelper prepareListenerHelper() {
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		helper.put(HelperType.CURRENT_COMPONENT, getComponent());
		return helper;
	}
	
	protected void populateWidget(){
		logger.debug("Populating {} textbox", textBoxConfig.getName());
		String property = propertyValue;
		if(StringUtils.isNotBlank(property)){
			textBox.setText(property);
			txtDecorator.hide();
		}
		else{
			textBox.setText("");
			txtDecorator.show();
		}
	}
	
	protected boolean isParameter(String input) {
		if (input != null) {
			Matcher matchs = Pattern.compile(Constants.PARAMETER_REGEX).matcher(input);
			if (matchs.matches()) {
				return true;
			}
		}
		return false;
	}
}
