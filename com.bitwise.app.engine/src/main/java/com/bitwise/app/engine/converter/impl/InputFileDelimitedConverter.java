package com.bitwise.app.engine.converter.impl;




import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.InputConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.SchemaGrid;
import com.bitwiseglobal.graph.commontypes.FieldDataTypes;
import com.bitwiseglobal.graph.commontypes.ScaleTypeList;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;
import com.bitwiseglobal.graph.inputtypes.TextFileDelimited;
import com.bitwiseglobal.graph.itfd.TypeInputDelimitedOutSocket;

public class InputFileDelimitedConverter extends InputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputFileDelimitedConverter.class);
	
	public InputFileDelimitedConverter(Component component) {
		super();
		this.baseComponent = new TextFileDelimited();
		this.component = component;
		this.properties = component.getProperties();
	}
	
	@Override
	public void prepareForXML(){
		logger.debug("Genrating XML for {}", properties.get(Constants.PARAM_NAME));	
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
		fileDelimited.setStrict(getBoolean(PropertyNameConstants.STRICT.value()));
		fileDelimited.setSafe(getBoolean(PropertyNameConstants.IS_SAFE.value()));
		fileDelimited.setCharset(charset);
		fileDelimited.setRuntimeProperties(getRuntimeProperties());
	}

	@Override
	protected List<TypeInputOutSocket> getInOutSocket(){
		logger.debug("Genrating TypeInputOutSocket data for {}", properties.get(Constants.PARAM_NAME));
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputDelimitedOutSocket outSocket = new TypeInputDelimitedOutSocket();
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
		logger.debug("Genrating data for {} for property {}", new Object[]{properties.get(Constants.PARAM_NAME),PropertyNameConstants.SCHEMA.value()});
		List<SchemaGrid> schemaList = (List) properties.get(PropertyNameConstants.SCHEMA.value());
		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if(schemaList!=null){
			try{
				for (SchemaGrid object : schemaList ) {
					TypeBaseField typeBaseField = new TypeBaseField();
					typeBaseField.setName(object.getFieldName());
					if(object.getDataTypeValue().equals(FieldDataTypes.JAVA_UTIL_DATE.value())&& !object.getDateFormat().trim().isEmpty() )
							typeBaseField.setFormat(object.getDateFormat());
					
					if(!object.getScale().trim().isEmpty())
						typeBaseField.setScale(Integer.parseInt(object.getScale()));
					
					if(object.getDataTypeValue().equals(FieldDataTypes.JAVA_LANG_DOUBLE.value())||object.getDataTypeValue().equals(FieldDataTypes.JAVA_MATH_BIG_DECIMAL.value()))
						{	typeBaseField.setScaleType(ScaleTypeList.EXPLICIT );
							if(!object.getScale().trim().isEmpty())
								typeBaseField.setScale(Integer.parseInt(object.getScale()));
						}
					
					for(FieldDataTypes fieldDataType:FieldDataTypes.values()){
						if(fieldDataType.value().equalsIgnoreCase(object.getDataTypeValue()))
							typeBaseField.setType(fieldDataType);
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
