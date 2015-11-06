package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.OutputConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.SchemaGrid;
import com.bitwiseglobal.graph.commontypes.FieldDataTypes;
import com.bitwiseglobal.graph.commontypes.ScaleTypeList;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.otfd.TypeOutputDelimitedInSocket;
import com.bitwiseglobal.graph.outputtypes.TextFileDelimited;

public class OutputFileDelimitedConverter extends OutputConverter {
	
	Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputFileDelimitedConverter.class);
	
	public OutputFileDelimitedConverter(Component component) {
		super();
		this.component = component;
		this.properties = component.getProperties();
		this.baseComponent = new TextFileDelimited();
	}
	
	@Override
	public void prepareForXML(){
		LOGGER.debug("Genrating XML data for {}", properties.get(NAME));
		super.prepareForXML();
		
		TextFileDelimited fileDelimited = (TextFileDelimited) baseComponent;
		
		TextFileDelimited.Path path = new TextFileDelimited.Path();
		path.setUri((String) properties.get(PropertyNameConstants.PATH.value()));
		
		TextFileDelimited.Charset charset = new TextFileDelimited.Charset();
		charset.setValue(getCharset());
		
		TextFileDelimited.Delimiter delimiter = new TextFileDelimited.Delimiter();
		delimiter.setValue((String) properties.get(PropertyNameConstants.DELIMITER.value()));
		
		fileDelimited.setPath(path);
		fileDelimited.setDelimiter(delimiter);
		fileDelimited.setHasHeader(getBoolean(PropertyNameConstants.HAS_HEADER.value()));
		fileDelimited.setCharset(charset);
		fileDelimited.setRuntimeProperties(getRuntimeProperties());
	}

	@Override
	protected List<TypeOutputInSocket> getOutInSocket(){
		LOGGER.debug("Genrating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputDelimitedInSocket outInSocket = new TypeOutputDelimitedInSocket();
			outInSocket.setId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			outInSocket.setFromSocketId(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort())+link.getLinkNumber());
			outInSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
			outInSocket.setSchema(getSchema());
			outInSocket.getOtherAttributes();
			outInSocket.setFromComponentId((String) link.getSource().getProperties().get(NAME));
			outputinSockets.add(outInSocket);
		}
		return outputinSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord() {
		LOGGER.debug("Genrating data for {} for property {}", new Object[]{properties.get(NAME),PropertyNameConstants.SCHEMA.value()});
		List<SchemaGrid> schemaList = (List) properties.get(PropertyNameConstants.SCHEMA.value());
		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if(schemaList!=null){
			try{
				for (SchemaGrid object : schemaList ) {
					TypeBaseField typeBaseField = new TypeBaseField();
					typeBaseField.setName(object.getFieldName());
					typeBaseField.setDescription("");
					typeBaseField.setFormat(object.getDateFormat());
					if(!object.getScale().trim().isEmpty())
						typeBaseField.setScale(Integer.parseInt(object.getScale()));
					typeBaseField.setScaleType(ScaleTypeList.IMPLICIT );
					for(FieldDataTypes fieldDataType:FieldDataTypes.values()){
						if(fieldDataType.value().equalsIgnoreCase(object.getDataTypeValue()))
							typeBaseField.setType(fieldDataType);
					}
					
					typeBaseFields.add(typeBaseField);
				}
			}
			catch (Exception exception) {
				LOGGER.warn("Exception while creating schema for component : {}{}", new Object[]{properties.get(NAME),exception});
				
			}
		}
		return typeBaseFields;
	}
}
