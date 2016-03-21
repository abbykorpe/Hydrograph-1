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

 
package com.bitwise.app.graph.propertywindow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.adapters.ELTComponentPropertyAdapter;
import com.bitwise.app.propertywindow.factory.WidgetFactory.Widgets;
import com.bitwise.app.propertywindow.property.ELTComponenetProperties;
import com.bitwise.app.propertywindow.property.IPropertyTreeBuilder;
import com.bitwise.app.propertywindow.property.Property;
import com.bitwise.app.propertywindow.property.PropertyTreeBuilder;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialog;

/**
 * 
 * @author Bitwise
 * Sep 25, 2015
 * 
 */

public class ELTPropertyWindow implements IELTPropertyWindow{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTPropertyWindow.class);

	private static final String STRING = "String";
	private static final String TYPE = "Type";
	private static final String BASE_TYPE = "Base Type";
	private static final String DISPLAY = "DISPLAY";
	private static final String GENERAL = "GENERAL";

	
	Object componenetModel;
	ELTComponenetProperties eltComponenetProperties;
	Component component;
	private boolean propertyChanged = false;
	private Map<String, String> toolTipErrorMessages;

	
	/**
	 * Instantiates a new ELT property window.
	 * 
	 * @param componenetModel
	 *            the componenet model
	 */
	public ELTPropertyWindow(Object componenetModel){
		this.componenetModel = componenetModel;
		component = getCastedModel();
		eltComponenetProperties = getELTComponenetProperties();
		toolTipErrorMessages = component.getToolTipErrorMessages();
	}

	private Component getCastedModel() {
		return (Component) componenetModel;
	}
	
	private ELTComponenetProperties getELTComponenetProperties(){
		LinkedHashMap<String, Object> componentConfigurationProperties = component.getProperties();
		LinkedHashMap<String, Object> ComponentMiscellaneousProperties = getComponentMiscellaneousProperties();
		
		ELTComponenetProperties eltComponenetProperties = new ELTComponenetProperties(componentConfigurationProperties, ComponentMiscellaneousProperties);
		return eltComponenetProperties;
	}

	private LinkedHashMap<String, Object> getComponentMiscellaneousProperties() {
		LinkedHashMap<String, Object> ComponentMiscellaneousProperties = new LinkedHashMap<>();
		
		ComponentMiscellaneousProperties.put(Constants.COMPONENT_NAMES, component.getParent().getComponentNames());
		ComponentMiscellaneousProperties.put(Constants.COMPONENT_BASE_TYPE, component.getCategory());
		ComponentMiscellaneousProperties.put(Constants.COMPONENT_TYPE, component.getType());
		ComponentMiscellaneousProperties.put(Constants.COMPONENT_ORIGINAL_NAME, DynamicClassProcessor.INSTANCE.getClazzName(component.getClass()));
		return ComponentMiscellaneousProperties;
	}
	
	private Property getComponentBaseTypeProperty(){
		Property property = new Property.Builder(STRING, BASE_TYPE, Widgets.COMPONENT_BASETYPE_WIDGET.name())
									.group(GENERAL).subGroup(DISPLAY).build();
		return property;
	}
	
	private Property getComponentTypeProperty(){
		Property property = new Property.Builder(STRING, TYPE, Widgets.COMPONENT_TYPE_WIDGET.name())
									.group(GENERAL).subGroup(DISPLAY).build();
		return property;
	}
	
	//@Override
	@Override
	public void open() {
		List<com.bitwise.app.common.component.config.Property> rowProperties = getComponentPropertiesFromComponentXML();		
		try {			
			
				
			ArrayList<Property> componentProperties = transformToPropertyWindowFormat(rowProperties);
			componentProperties.add(getComponentBaseTypeProperty());
			componentProperties.add(getComponentTypeProperty());
			
			IPropertyTreeBuilder propertyTreeBuilder = new PropertyTreeBuilder(componentProperties);

			PropertyDialog propertyDialog = new PropertyDialog(propertyTreeBuilder.getPropertyTree(),
					eltComponenetProperties,toolTipErrorMessages,component);
			propertyDialog.open();

			//component.setSize(getNewComponentSize());
			
			propertyChanged = propertyDialog.isPropertyChanged();
			
		} catch (ELTComponentPropertyAdapter.EmptyComponentPropertiesException e) {
			logger.error("Failed in transforming properties", e);
		}
	}

	public boolean isPropertyChanged(){
			return propertyChanged;
	}
	
   private ArrayList<Property> transformToPropertyWindowFormat(
			List<com.bitwise.app.common.component.config.Property> rowProperties) throws ELTComponentPropertyAdapter.EmptyComponentPropertiesException {
		ELTComponentPropertyAdapter eltComponentPropertyAdapter = new ELTComponentPropertyAdapter(rowProperties);
		eltComponentPropertyAdapter.transform();
		ArrayList<Property> componentProperties = eltComponentPropertyAdapter.getProperties();
		return componentProperties;
	}

	private List<com.bitwise.app.common.component.config.Property> getComponentPropertiesFromComponentXML() {
		return XMLConfigUtil.INSTANCE.getComponent(DynamicClassProcessor.INSTANCE.getClazzName(componenetModel.getClass())).getProperty();
	}
}
