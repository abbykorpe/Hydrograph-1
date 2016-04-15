/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package hydrograph.ui.engine.converter.impl;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.OutputConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.ohivetextfile.FieldBasicType;
import hydrograph.engine.jaxb.ohivetextfile.HivePartitionFieldsType;
import hydrograph.engine.jaxb.ohivetextfile.HivePathType;
import hydrograph.engine.jaxb.ohivetextfile.HiveType;
import hydrograph.engine.jaxb.ohivetextfile.TypeOutputHiveTextFileDelimitedInSocket;
import hydrograph.engine.jaxb.outputtypes.HiveTextFile;
/**
 * Converter implementation for Output Hive TextFile component
 * 
 * @author eyy445 
 */
public class OutputHiveTextFileConverter extends OutputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputHiveTextFileConverter.class);

	public OutputHiveTextFileConverter(Component component) {
		super(component);
		this.baseComponent = new HiveTextFile();
		this.component = component;
		this.properties = component.getProperties();
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		HiveTextFile hiveTextfile = (HiveTextFile) baseComponent;
		hiveTextfile.setRuntimeProperties(getRuntimeProperties());

		hiveTextfile.setDatabaseName(getHiveType(PropertyNameConstants.DATABASE_NAME.value()));
		hiveTextfile.setTableName(getHiveType(PropertyNameConstants.TABLE_NAME.value()));
		if(StringUtils.isNotBlank((String)properties.get(PropertyNameConstants.EXTERNAL_TABLE_PATH.value()))){
			hiveTextfile.setExternalTablePath(getHivePathType(PropertyNameConstants.EXTERNAL_TABLE_PATH.value()));
		}
		hiveTextfile.setPartitionKeys(getPartitionKeys());
		HiveTextFile.Delimiter delimiter = new HiveTextFile.Delimiter();
		delimiter.setValue((String) properties.get(PropertyNameConstants.DELIMITER.value()));
		HiveTextFile.Quote quote = new HiveTextFile.Quote();
		quote.setValue((String) properties.get(PropertyNameConstants.QUOTE.value()));
		hiveTextfile.setDelimiter(delimiter);
		if(StringUtils.isNotBlank(quote.getValue())){
			hiveTextfile.setQuote(quote);
		}
		hiveTextfile.setStrict(getBoolean(PropertyNameConstants.STRICT.value()));
		hiveTextfile.setSafe(getBoolean(PropertyNameConstants.IS_SAFE.value()));
		
	}

	/*
	 * returns hiveType
	 */
	protected HiveType getHiveType(String propertyName) {
		logger.debug("Getting HypeType Value for {}={}", new Object[] {
				propertyName, properties.get(propertyName) });
		if (properties.get(propertyName) != null) {
			HiveType hiveType = new HiveType();
			hiveType.setValue(String.valueOf((String) properties
					.get(propertyName)));
				
				return hiveType;
		}
		return null;
	}

	/*
	 * returns hivePathType
	 */
	protected HivePathType getHivePathType(String propertyName) {
		logger.debug("Getting HypePathType Value for {}={}", new Object[] {
				propertyName, properties.get(propertyName) });
		if (properties.get(propertyName) != null) {
			HivePathType hivePathType = new HivePathType();
			hivePathType.setUri(String.valueOf((String) properties
					.get(propertyName)));
				return hivePathType;
		}
		return null;
	}

	/*
	 * returns HivePartitionFieldsType
	 */
	private HivePartitionFieldsType getPartitionKeys() {

		List<String> fieldValueSet = (List<String>) properties.get(PropertyNameConstants.PARTITION_KEYS.value());
		
		HivePartitionFieldsType typeHivePartitionFields = null;
		if (fieldValueSet != null) {
			typeHivePartitionFields = new HivePartitionFieldsType();
			List<FieldBasicType> fieldNameList = typeHivePartitionFields.getField();
			for (String value : fieldValueSet) {
				FieldBasicType field = new FieldBasicType();
				field.setName(value);
				fieldNameList.add(field);
			}

		}
		return typeHivePartitionFields;
	}
	
	
	@Override
	protected List<TypeOutputInSocket> getOutInSocket() {
		logger.debug("Generating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputHiveTextFileDelimitedInSocket outInSocket = new TypeOutputHiveTextFileDelimitedInSocket();
			outInSocket.setId(link.getTargetTerminal());
			outInSocket.setFromSocketId(converterHelper.getFromSocketId(link));
			outInSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
			outInSocket.setSchema(getSchema());
			outInSocket.getOtherAttributes();
			outInSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			outputinSockets.add(outInSocket);
		}
		return outputinSockets;
	}
	
	@Override
	protected List<TypeBaseField> getFieldOrRecord(ComponentsOutputSchema outputSchema) {
		List<FixedWidthGridRow> gridList=outputSchema.getFixedWidthGridRowsOutputFields();
		logger.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (gridList != null && gridList.size() != 0) {
			for (GridRow object : gridList)
				typeBaseFields.add(converterHelper.getSchemaGridTargetData(object));

		}
		return typeBaseFields;
	}
}

