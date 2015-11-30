package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.OutputConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.propertywindow.fixedwidthschema.FixedWidthGridRow;
import com.bitwiseglobal.graph.commontypes.FieldDataTypes;
import com.bitwiseglobal.graph.commontypes.ScaleTypeList;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.otffw.TypeOutputFixedwidthInSocket;
import com.bitwiseglobal.graph.outputtypes.TextFileFixedWidth;

public class OutputFileFixedWidthConverter extends OutputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputFileDelimitedConverter.class);
	
	public OutputFileFixedWidthConverter(Component component) {
		super();
		this.component = component;
		this.properties = component.getProperties();
		this.baseComponent = new TextFileFixedWidth();
	}
	
	@Override
	public void prepareForXML(){
		logger.debug("prepareForXML - Genrating XML data for "+component);
		super.prepareForXML();
		TextFileFixedWidth fileFixedWidth = (TextFileFixedWidth) baseComponent;
		TextFileFixedWidth.Path path = new TextFileFixedWidth.Path();
		path.setUri((String) properties.get(PropertyNameConstants.PATH.value()));
		TextFileFixedWidth.Charset charset = new TextFileFixedWidth.Charset();
		charset.setValue(getCharset());
		fileFixedWidth.setSafe(getBoolean(PropertyNameConstants.IS_SAFE.value()));
		fileFixedWidth.setPath(path);
		fileFixedWidth.setStrict(getBoolean(PropertyNameConstants.HAS_HEADER.value()));
	    fileFixedWidth.setCharset(charset);
		fileFixedWidth.setRuntimeProperties(getRuntimeProperties());
	}

	@Override
	protected List<TypeOutputInSocket> getOutInSocket(){
		logger.debug("getInOutSocket - Genrating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputFixedwidthInSocket outInSocket = new TypeOutputFixedwidthInSocket();
			outInSocket.setId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			outInSocket.setFromSocketId(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort())+link.getLinkNumber());
			outInSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
			outInSocket.setSchema(getSchema());
			outInSocket.getOtherAttributes();
			outInSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			outputinSockets.add(outInSocket);
		}
		return outputinSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord() {
		logger.debug("Genrating data for {} for property {}", new Object[]{properties.get(Constants.PARAM_NAME),PropertyNameConstants.SCHEMA.value()});
		List<FixedWidthGridRow> schemaList = (List) properties.get(PropertyNameConstants.SCHEMA.value());
		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if(schemaList!=null){
			try{
				for (FixedWidthGridRow object : schemaList ) {
					TypeBaseField typeBaseField = new TypeBaseField();
					typeBaseField.setName(object.getFieldName());
					typeBaseField.setDescription("");
					typeBaseField.setFormat(object.getDateFormat());
					if(!object.getScale().trim().isEmpty())
						typeBaseField.setScale(Integer.parseInt(object.getScale()));
					typeBaseField.setScaleType(ScaleTypeList.EXPLICIT );
					for(FieldDataTypes fieldDataType:FieldDataTypes.values()){
						if(fieldDataType.value().equalsIgnoreCase(object.getDataTypeValue()))
							typeBaseField.setType(fieldDataType);
					}
					if(object.getLength()!=null)
					{
						typeBaseField.getOtherAttributes().put(new QName("length"), object.getLength());
					}
					typeBaseFields.add(typeBaseField);
				}
			}
			catch (Exception exception) {
				logger.warn("Exception while creating schema for component : {}{}", new Object[]{properties.get(Constants.PARAM_NAME),exception});
				
			}
		}
		return typeBaseFields;
	}
}
