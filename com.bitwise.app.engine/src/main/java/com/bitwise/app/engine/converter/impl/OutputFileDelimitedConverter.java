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

 
package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.OutputConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.otfd.TypeOutputDelimitedInSocket;
import com.bitwiseglobal.graph.outputtypes.TextFileDelimited;

public class OutputFileDelimitedConverter extends OutputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputFileDelimitedConverter.class);
	private ConverterHelper converterHelper;

	public OutputFileDelimitedConverter(Component component) {
		super();
		this.component = component;
		this.properties = component.getProperties();
		this.baseComponent = new TextFileDelimited();
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML data for {}", properties.get(Constants.PARAM_NAME));
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
		fileDelimited.setStrict(getBoolean(PropertyNameConstants.STRICT.value()));
		fileDelimited.setHasHeader(getBoolean(PropertyNameConstants.HAS_HEADER.value()));
		fileDelimited.setSafe(getBoolean(PropertyNameConstants.IS_SAFE.value()));
		fileDelimited.setCharset(charset);
		fileDelimited.setRuntimeProperties(getRuntimeProperties());
	}

	@Override
	protected List<TypeOutputInSocket> getOutInSocket() {
		logger.debug("Generating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputDelimitedInSocket outInSocket = new TypeOutputDelimitedInSocket();
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
