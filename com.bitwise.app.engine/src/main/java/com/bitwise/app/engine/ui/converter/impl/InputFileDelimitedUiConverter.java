package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.InputUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.IFDelimited;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.SchemaGrid;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.inputtypes.TextFileDelimited;

public class InputFileDelimitedUiConverter extends InputUiConverter {
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputFileDelimitedUiConverter.class);
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
		LOGGER.debug("Fetching Input-Delimited-Properties for {}",COMPONENT_NAME);
		fileDelimited=(TextFileDelimited)typeBaseComponent;
		propertyMap.put(PropertyNameConstants.HAS_HEADER.value(), convertBooleanVlaue(fileDelimited.getHasHeader(),PropertyNameConstants.HAS_HEADER.value()));
		propertyMap.put(PropertyNameConstants.PATH.value(), fileDelimited.getPath().getUri());
		propertyMap.put(PropertyNameConstants.CHAR_SET.value(), getCharSet());
		propertyMap.put(PropertyNameConstants.STRICT.value(), convertBooleanVlaue(fileDelimited.getStrict(),PropertyNameConstants.STRICT.value()));
		propertyMap.put(PropertyNameConstants.DELIMITER.value(), fileDelimited.getDelimiter().getValue());
		propertyMap.put(PropertyNameConstants.IS_SAFE.value(), convertBooleanVlaue(fileDelimited.getSafe(),PropertyNameConstants.IS_SAFE.value()));
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
				
		uiComponent.setType(UIComponentsConstants.FILE_DELIMITED.value());
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);

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
	protected Map<String,String> getRuntimeProperties()
	{ 	
		LOGGER.debug("Generating Runtime Properties for -{}",COMPONENT_NAME);
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

	@Override
	protected Object getSchema(TypeInputOutSocket outSocket) {
		LOGGER.debug("Generating UI-Schema data for {}",COMPONENT_NAME);
		List<SchemaGrid> schemaList =new ArrayList<>();
		for(Object record: outSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema())
		{		
			if((TypeExternalSchema.class).isAssignableFrom(record.getClass())){
				return null;
			}
			else if((TypeBaseField.class).isAssignableFrom(record.getClass())){
				SchemaGrid schemaGrid=new SchemaGrid();
				TypeBaseField typeBaseField=(TypeBaseField)record;
				schemaGrid.setDataTypeValue(getStringValue(typeBaseField.getType().value()));
				schemaGrid.setDateFormat(getStringValue(typeBaseField.getFormat()));
				schemaGrid.setFieldName(getStringValue(typeBaseField.getName()));
				schemaGrid.setScale(getStringValue(String.valueOf(typeBaseField.getScale())));
				schemaGrid.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(typeBaseField.getType().value()));
				schemaList.add(schemaGrid);
			}
			
		}
		return schemaList;
	}

	
	

	
	

}
