package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.InputUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.IFDelimited;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.inputtypes.TextFileDelimited;

public class InputFileDelimitedUiConverter extends InputUIConverter {
	List<String> schemaLst;
	
	private TextFileDelimited fileDelimited;
	private static final String COMPONENT_NAME_SUFFIX="IFDelimited";	
	public InputFileDelimitedUiConverter(TypeBaseComponent typeBaseComponent,Container container) {
		this.container=container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new IFDelimited();
		this.propertyMap=new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		
		fileDelimited=(TextFileDelimited)typeBaseComponent;
		
		propertyMap.put(PropertyNameConstants.HAS_HEADER.value(), convertBooleanVlaue(fileDelimited.getHasHeader(),PropertyNameConstants.HAS_HEADER.value()));
		propertyMap.put(PropertyNameConstants.PATH.value(), fileDelimited.getPath().getUri());
		propertyMap.put(PropertyNameConstants.CHAR_SET.value(), getValue(PropertyNameConstants.CHAR_SET.value(),fileDelimited.getCharset().getValue().value()));
		propertyMap.put(PropertyNameConstants.DELIMITER.value(), fileDelimited.getDelimiter().getValue());
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),new TreeMap<>());
		propertyMap.put(PropertyNameConstants.IS_SAFE.value(), convertBooleanVlaue(fileDelimited.getSafe(),PropertyNameConstants.IS_SAFE.value()));
		propertyMap.put(PropertyNameConstants.SCHEMA.value(),new ArrayList<>());
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
		uiComponent.setType(UIComponentsConstants.FILE_DELIMITED.value());
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());

		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);

		uiComponent.setProperties(propertyMap);

		
	}

	

}
