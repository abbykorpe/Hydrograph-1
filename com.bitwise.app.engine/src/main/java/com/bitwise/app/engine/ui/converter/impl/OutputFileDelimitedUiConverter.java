package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.OutputUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.OFDelimited;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.outputtypes.TextFileDelimited;

public class OutputFileDelimitedUiConverter extends OutputUIConverter {
	private static final String COMPONENT_NAME_SUFFIX="OFDelimited";	
	public OutputFileDelimitedUiConverter(TypeBaseComponent typeBaseComponent,Container container) {
		this.container=container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OFDelimited();
		this.propertyMap=new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {
	
		super.prepareUIXML();
		
		TextFileDelimited fileDelimited=(TextFileDelimited)typeBaseComponent;
		uiComponent.setComponentLabel(fileDelimited.getId());
		propertyMap.put(PropertyNameConstants.HAS_HEADER.value(), convertBooleanVlaue(fileDelimited.getHasHeader(),PropertyNameConstants.HAS_HEADER.value()));
		propertyMap.put(PropertyNameConstants.PATH.value(), fileDelimited.getPath().getUri());
		propertyMap.put(PropertyNameConstants.STRICT.value(), convertBooleanVlaue(fileDelimited.getStrict(),PropertyNameConstants.STRICT.value()));
		propertyMap.put(PropertyNameConstants.CHAR_SET.value(), getCharSet());
		propertyMap.put(PropertyNameConstants.DELIMITER.value(), fileDelimited.getDelimiter().getValue());
		propertyMap.put(PropertyNameConstants.SCHEMA.value(),getSchema());
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
		
		uiComponent.setType(UIComponentsConstants.FILE_DELIMITED.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(fileDelimited.getId());
		uiComponent.setProperties(propertyMap);
		
	}

	private Object getCharSet() {
		TextFileDelimited fileDelimited=(TextFileDelimited)typeBaseComponent;
		Object value=fileDelimited.getCharset().getValue();
		if(value!=null)	{
			return	fileDelimited.getCharset().getValue().value();
		}else{
			value = getValue(PropertyNameConstants.CHAR_SET.value());			
		}
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
		TypeProperties typeProperties = ((TextFileDelimited)typeBaseComponent).getRuntimeProperties();
		if(typeProperties!=null){
			runtimeMap=new TreeMap<>();
					for(Property runtimeProperty:typeProperties.getProperty()){
						runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
					}
		}
		return runtimeMap;
	}
	
	
}
