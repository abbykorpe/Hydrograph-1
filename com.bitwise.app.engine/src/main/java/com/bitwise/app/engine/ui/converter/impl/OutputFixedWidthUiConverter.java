
package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.OutputUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.OFixedWidth;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.outputtypes.TextFileFixedWidth;
public class OutputFixedWidthUiConverter extends OutputUIConverter {
	List<String> schemaLst;
	private static final String COMPONENT_NAME_SUFFIX="OFixedWidth";	
	public OutputFixedWidthUiConverter(TypeBaseComponent typeBaseComponent,Container container) {
		this.container=container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OFixedWidth();
		this.propertyMap=new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		
		TextFileFixedWidth fileFixedWidth=(TextFileFixedWidth)typeBaseComponent;
		
		propertyMap.put(PropertyNameConstants.PATH.value(), fileFixedWidth.getPath().getUri());
		propertyMap.put(PropertyNameConstants.CHAR_SET.value(), getValue(PropertyNameConstants.CHAR_SET.value(),fileFixedWidth.getCharset().getValue().value()));
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),new TreeMap<>());
		propertyMap.put(PropertyNameConstants.IS_SAFE.value(), convertBooleanVlaue(fileFixedWidth.getSafe(),PropertyNameConstants.IS_SAFE.value()));
		propertyMap.put(PropertyNameConstants.SCHEMA.value(),new ArrayList<>());
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(), UIComponentsConstants.VALID.value());
		
		uiComponent.setType(UIComponentsConstants.FILE_FIXEDWIDTH.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(fileFixedWidth.getId());
		uiComponent.setProperties(propertyMap);
		
	
	}}