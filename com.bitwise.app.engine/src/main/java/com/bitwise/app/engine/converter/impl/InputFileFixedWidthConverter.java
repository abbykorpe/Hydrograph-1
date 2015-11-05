package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.InputConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.propertywindow.fixedwidthschema.FixedWidthGridRow;
import com.bitwiseglobal.graph.commontypes.FieldDataTypes;
import com.bitwiseglobal.graph.commontypes.ScaleTypeList;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;
import com.bitwiseglobal.graph.inputtypes.TextFileFixedWidth;
import com.bitwiseglobal.graph.itffw.TypeInputFixedwidthOutSocket;

public class InputFileFixedWidthConverter extends InputConverter {

	Logger LOGGER = LogFactory.INSTANCE.getLogger(InputFileFixedWidthConverter.class);

	public InputFileFixedWidthConverter(Component component) {
		super();
		this.baseComponent = new TextFileFixedWidth();
		this.component = component;
		this.properties = component.getProperties();
	}
	
	@Override
	public void prepareForXML(){
		LOGGER.debug("prepareForXML - Genrating XML data for "+component);
		super.prepareForXML();
		TextFileFixedWidth fileFixedWidth = (TextFileFixedWidth) baseComponent;
		TextFileFixedWidth.Path path = new TextFileFixedWidth.Path();
		path.setUri((String) properties.get(PropertyNameConstants.PATH.value()));
		TextFileFixedWidth.Charset charset = new TextFileFixedWidth.Charset();
		charset.setValue(getCharset());
		
		fileFixedWidth.setPath(path);
		fileFixedWidth.setStrict(getBoolean(PropertyNameConstants.STRICT.value()));
		fileFixedWidth.setSafe(getBoolean(PropertyNameConstants.IS_SAFE.value()));
		fileFixedWidth.setCharset(charset);
		fileFixedWidth.setRuntimeProperties(getRuntimeProperties());
	}
	@Override
	protected List<TypeInputOutSocket> getInOutSocket(){
		LOGGER.debug("getInOutSocket - Genrating TypeInputOutSocket data for "+component);
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputFixedwidthOutSocket outSocket = new TypeInputFixedwidthOutSocket();
			outSocket.setId(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort()) + link.getLinkNumber());
			outSocket.setType(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort()));
			outSocket.setSchema(getSchema());
			outSocket.getOtherAttributes();
			outSockets.add(outSocket);
		}
		return outSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord() {
		{
			LOGGER.debug("Genrating data for {} for property {}", new Object[]{properties.get(NAME),PropertyNameConstants.SCHEMA.value()});
			List<FixedWidthGridRow> schemaList = (List) properties.get(PropertyNameConstants.SCHEMA.value());
			List<TypeBaseField> typeBaseFields = new ArrayList<>();
			if(schemaList!=null){
				try{
					for (FixedWidthGridRow object : schemaList ) {
						TypeBaseField typeBaseField = new TypeBaseField();
						typeBaseField.setName(object.getFieldName());
						typeBaseField.setFormat(object.getDateFormat());
						if(!object.getScale().trim().isEmpty())
							typeBaseField.setScale(Integer.parseInt(object.getScale()));
						typeBaseField.setScaleType(ScaleTypeList.IMPLICIT );
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
					LOGGER.warn("Exception while creating schema for component : {}{}", new Object[]{properties.get(NAME),exception});
					
				}
			}
			return typeBaseFields;
		}
	}

}
