package hydrograph.ui.propertywindow.testdata;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import hydrograph.ui.propertywindow.factory.WidgetFactory.Widgets;
import hydrograph.ui.propertywindow.property.Property;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Sep 04, 2015
 * 
 */

public class PropertyStore {
	LinkedHashMap<String,ArrayList<Property>> properties;
	
	/**
	 * Instantiates a new property store.
	 */
	public PropertyStore(){
		properties=new LinkedHashMap<>();
		addInputComponentProperties();
	}
	
	/**
	 * Adds the input component properties.
	 */
	public void addInputComponentProperties(){
		ArrayList<Property> inputComponentProperties = new ArrayList<>();
		
		
		//--------------------
		
				Property name= new Property.Builder("String", "name", Widgets.COMPONENT_NAME_WIDGET.name())
				.group("TEXT_PROPERTIES").build();
				
				Property path= new Property.Builder("String", "path", Widgets.FILE_PATH_WIDGET.name())
				.group("TEXT_PROPERTIES").build();
				
				Property delimiter= new Property.Builder("String", "strict", Widgets.STRICT_CLASS_WIDGET.name())
				.group("TEXT_PROPERTIES").build();
				
				//------
				
				Property charset= new Property.Builder("String", "charset", Widgets.CHARACTER_SET_WIDGET.name())
				.group("TEXT_PROPERTIES")
				.subGroup("AAAA").build();
						
				Property phase= new Property.Builder("String", "phase", Widgets.PHASE_WIDGET.name())
				.group("TEXT_PROPERTIES")
				.subGroup("AAAA").build();
				
				//--------------------
				
				Property safe= new Property.Builder("boolean", "safe", Widgets.SAFE_PROPERTY_WIDGET.name())
				.group("RADIO_PROPERTIES")
				.subGroup("safe").build();
				
				Property has_header= new Property.Builder("boolean", "has_header", Widgets.HAS_HEADER_WIDGET.name())
				.group("RADIO_PROPERTIES")
				.subGroup("header").build();
				//--------------------
				
				//--------------------
		
				Property schema= new Property.Builder("boolean", "Schema", Widgets.SCHEMA_WIDGET.name())
				.group("Schema").build();
				
				Property runtimeProps= new Property.Builder("boolean", "RuntimeProps", Widgets.RUNTIME_PROPERTIES_WIDGET.name())
				.group("RUNTIME_PROP").build();
				//--------------------
		
		inputComponentProperties.add(name);
		inputComponentProperties.add(path);
		inputComponentProperties.add(delimiter);
		inputComponentProperties.add(safe);
		inputComponentProperties.add(has_header);
		inputComponentProperties.add(charset);
		inputComponentProperties.add(phase);
		inputComponentProperties.add(schema);
		inputComponentProperties.add(runtimeProps);
		
		properties.put("Input", inputComponentProperties);
	}
	
	/**
	 * Gets the properties.
	 * 
	 * @param componentName
	 *            the component name
	 * @return the properties
	 */
	public ArrayList<Property> getProperties(String componentName){
		return properties.get(componentName);
	}

	@Override
	public String toString() {
		return "PropertyStore [properties=" + properties + "]";
	}
	
	
}
