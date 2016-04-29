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

import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.utils.WordUtils;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;



// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Sep 08, 2015
 * 
 */

public class MyCustomWidget extends AbstractWidget{

	

	private Object properties;
	private String propertyName;
	
	private Text textBox;
	
	/**
	 * Instantiates a new my custom widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public MyCustomWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);

		this.properties =  componentConfigrationProperty.getPropertyValue();
		this.propertyName = componentConfigrationProperty.getPropertyName();
		
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container){
		
		ListenerFactory listenerFactory = new ListenerFactory();
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Addess :");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		setPropertyHelpWidget((Control) eltDefaultLable.getSWTWidgetControl());
		
		
		AbstractELTWidget eltDefaultTextBox = new ELTDefaultTextBox().defaultText("Hello").grabExcessHorizontalSpace(true).textBoxWidth(100);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultTextBox);
		
		textBox = (Text) eltDefaultTextBox.getSWTWidgetControl();
		
		AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Submit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		WordUtils wordUtils= new WordUtils();
		
		ListenerHelper listenerHelper = new ListenerHelper("WordUtils", wordUtils);
		
	}

	private void populateWidget(){	
		
		if(properties != null)
			textBox.setText((String) properties);
		else
			textBox.setText("");
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName, textBox.getText());
		return property;
	}

	@Override
	public boolean applyValidationRule() {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void setComponentName(String componentName) {
		// TODO Auto-generated method stub
		
	}*/

}
