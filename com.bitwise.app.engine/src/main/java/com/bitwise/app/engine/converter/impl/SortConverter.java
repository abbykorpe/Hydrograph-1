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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.StraightPullConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.KeepValue;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeSortOrder;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;
import com.bitwiseglobal.graph.sort.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.sort.TypePrimaryKeyFieldsAttributes;
import com.bitwiseglobal.graph.sort.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.sort.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups.Keep;
import com.bitwiseglobal.graph.straightpulltypes.Sort;

/**
 * Converter to convert sort component into engine specific sort object
 *
 *@author BITWISE
 */
public class SortConverter extends StraightPullConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(SortConverter.class);
	private ConverterHelper converterHelper;

	public SortConverter(Component component) {
		super();
		this.baseComponent = new Sort();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for : {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Sort sort = (Sort) baseComponent;
		sort.setPrimaryKeys(getPrimaryKeys());
		sort.setSecondaryKeys(getSecondaryKeys());
	}

	private TypePrimaryKeyFields getPrimaryKeys() {

		Map<String, String> fieldValueMap = (Map<String, String>) properties.get(Constants.PARAM_PRIMARY_COLUMN_KEYS);

		TypePrimaryKeyFields primaryKeyFields = null;
		if (fieldValueMap != null) {
			primaryKeyFields = new TypePrimaryKeyFields();
			List<TypePrimaryKeyFieldsAttributes> fieldNameList = primaryKeyFields.getField();
			for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
				TypePrimaryKeyFieldsAttributes field = new TypePrimaryKeyFieldsAttributes();
				field.setName(entry.getKey());
				field.setOrder(TypeSortOrder.fromValue(entry.getValue().toLowerCase()));
				fieldNameList.add(field);
			}
		}
		return primaryKeyFields;
	}

	private TypeSecondaryKeyFields getSecondaryKeys() {

		Map<String, String> fieldValueMap = (LinkedHashMap<String, String>) properties.get(Constants.PARAM_SECONDARY_COLUMN_KEYS);

		TypeSecondaryKeyFields typeSecondaryKeyFields = null;
		if (fieldValueMap != null && !fieldValueMap.isEmpty()) {
			typeSecondaryKeyFields = new TypeSecondaryKeyFields();
			List<TypeSecondayKeyFieldsAttributes> fieldNameList = typeSecondaryKeyFields.getField();
			for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
				TypeSecondayKeyFieldsAttributes field = new TypeSecondayKeyFieldsAttributes();
				field.setName(entry.getKey());
				field.setOrder(TypeSortOrder.fromValue(entry.getValue().toLowerCase()));
				fieldNameList.add(field);
			}
		}
		return typeSecondaryKeyFields;
	}

	@Override
	protected List<TypeStraightPullOutSocket> getOutSocket() {
		logger.debug("Generating TypeStraightPullOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeStraightPullOutSocket> outSockectList = new ArrayList<TypeStraightPullOutSocket>();

		for (Link link : component.getSourceConnections()) {
			TypeStraightPullOutSocket outSocket = new TypeStraightPullOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
			outSocketAsInsocket.setInSocketId(link.getTargetTerminal());
			outSocketAsInsocket.getOtherAttributes();
			outSocket.setCopyOfInsocket(outSocketAsInsocket);
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.getOtherAttributes();
			outSockectList.add(outSocket);
		}
		return outSockectList;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			inSocket.setFromSocketId(converterHelper.getFromSocketId(link));
			inSocket.setId(link.getTargetTerminal());
			inSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);

		}
		return inSocketsList;
	}
}