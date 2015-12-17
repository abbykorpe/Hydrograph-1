
package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.namespace.QName;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.OutputUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.OFixedWidth;
import com.bitwise.app.propertywindow.fixedwidthschema.FixedWidthGridRow;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.outputtypes.TextFileFixedWidth;


public class OutputFixedWidthUiConverter extends OutputUiConverter {
	private static final String LENGTH_QNAME="length";
	private static final String COMPONENT_NAME_SUFFIX="OFixedWidth";	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputFixedWidthUiConverter.class);
	
	public OutputFixedWidthUiConverter(TypeBaseComponent typeBaseComponent,Container container) {
		this.container=container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OFixedWidth();
		this.propertyMap=new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching OutPut-Fixed-Width-Component for {}",COMPONENT_NAME);
		TextFileFixedWidth fileFixedWidth=(TextFileFixedWidth)typeBaseComponent;
		
		propertyMap.put(PropertyNameConstants.PATH.value(), fileFixedWidth.getPath().getUri());
		propertyMap.put(PropertyNameConstants.CHAR_SET.value(),getCharSet());
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
		propertyMap.put(PropertyNameConstants.IS_SAFE.value(), convertBooleanVlaue(fileFixedWidth.getSafe(),PropertyNameConstants.IS_SAFE.value()));
		propertyMap.put(PropertyNameConstants.STRICT.value(), convertBooleanVlaue(fileFixedWidth.getStrict(),PropertyNameConstants.STRICT.value()));
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(), UIComponentsConstants.VALID.value());
		
		uiComponent.setType(UIComponentsConstants.FILE_FIXEDWIDTH.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(fileFixedWidth.getId());
		uiComponent.setProperties(propertyMap);
		
	
	}

	private Object getCharSet() {
		TextFileFixedWidth fileFixedWidth=(TextFileFixedWidth)typeBaseComponent;
		Object value=fileFixedWidth.getCharset().getValue();
		if(value!=null)	{
			return	fileFixedWidth.getCharset().getValue().value();
		}else{
			value = getValue(PropertyNameConstants.CHAR_SET.value());			
		}
		return value;
	}

	

	@Override
	protected Map<String,String> getRuntimeProperties()
	{	LOGGER.debug("Fetching runtime properties for {}",COMPONENT_NAME);
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

	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		LOGGER.debug("Generating UI-Schema data for OutPut-Fixed-Width-Component -{}",COMPONENT_NAME);
		List<FixedWidthGridRow> schemaList =new ArrayList<>();
		
		for(Object record: inSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema())
		{		
			if((TypeExternalSchema.class).isAssignableFrom(record.getClass())){
				return null;
			}
			else if((TypeBaseField.class).isAssignableFrom(record.getClass())){
				FixedWidthGridRow fixedWidthGrid=new FixedWidthGridRow();
				TypeBaseField typeBaseField=(TypeBaseField)record;
				fixedWidthGrid.setDataTypeValue(getStringValue(typeBaseField.getType().value()));
				fixedWidthGrid.setDateFormat(getStringValue(typeBaseField.getFormat()));
				fixedWidthGrid.setFieldName(getStringValue(typeBaseField.getName()));
				fixedWidthGrid.setScale(getStringValue(String.valueOf(typeBaseField.getScale())));
				fixedWidthGrid.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(typeBaseField.getType().value()));
				fixedWidthGrid.setLength(getStringValue(getLength(typeBaseField)));
				schemaList.add(fixedWidthGrid);
			}
			
		}
		return schemaList;
	}

	private String getLength(TypeBaseField typeBaseField) {
	
		for(Entry<QName,String> entry:typeBaseField.getOtherAttributes().entrySet())
		{
			if(entry.getKey().toString().equals(LENGTH_QNAME))
				return entry.getValue();
		}
		return null;
	}
}