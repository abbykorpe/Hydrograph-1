package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.InputUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.IFixedWidth;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.inputtypes.TextFileFixedWidth;
public class InputFixedWidthUiConverter extends InputUIConverter {
	List<String> schemaLst;
	private static final String COMPONENT_NAME_SUFFIX="IFixedWidth";	
	
	public InputFixedWidthUiConverter(TypeBaseComponent typeBaseComponent,Container container) {
		this.container=container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new IFixedWidth();
		this.propertyMap=new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		
		TextFileFixedWidth fileFixedWidth=(TextFileFixedWidth)typeBaseComponent;
		
		propertyMap.put(PropertyNameConstants.PATH.value(), fileFixedWidth.getPath().getUri());
		propertyMap.put(PropertyNameConstants.CHAR_SET.value(), getCharSet());
		propertyMap.put(PropertyNameConstants.STRICT.value(), convertBooleanVlaue(fileFixedWidth.getStrict(),PropertyNameConstants.STRICT.value()));
		propertyMap.put(PropertyNameConstants.IS_SAFE.value(), convertBooleanVlaue(fileFixedWidth.getSafe(),PropertyNameConstants.IS_SAFE.value()));
		propertyMap.put(PropertyNameConstants.SCHEMA.value(),getSchema());
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(), UIComponentsConstants.VALID.value());
		
		uiComponent.setType(UIComponentsConstants.FILE_FIXEDWIDTH.value());
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(fileFixedWidth.getId());
		uiComponent.setProperties(propertyMap);
	
	}

	private Object getCharSet() {
		TextFileFixedWidth fileFixedWidth=(TextFileFixedWidth)typeBaseComponent;
		Object value=null;
		if(fileFixedWidth.getCharset()!=null){
			value=fileFixedWidth.getCharset().getValue();
		if(value!=null)	{
			return	fileFixedWidth.getCharset().getValue().value();
		}else{
			value = getValue(PropertyNameConstants.CHAR_SET.value());			
		}}
		return value;
	}

	@Override
	protected Object getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TreeMap getRuntimeProperties()
	{
		TreeMap<String,String> runtimeMap=null;
		TypeProperties typeProperties = ((TextFileFixedWidth)typeBaseComponent).getRuntimeProperties();
		if(typeProperties!=null){
			runtimeMap=new TreeMap<>();
					for(Property runtimeProperty:typeProperties.getProperty()){
						runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
					}
		}
		return runtimeMap;
	}
}