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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.LookupConfigProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.common.datastructure.property.MatchValueProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ParameterUtil;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.MatchValue;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.hashjoin.TypeKeyFields;
import com.bitwiseglobal.graph.operationstypes.HashJoin;
import com.bitwiseglobal.graph.operationstypes.HashJoin.Match;


/**
 * @author Bitwise Converter implementation for Lookup component
 */

public class LookupConverter extends TransformConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterConverter.class);
	private LookupMappingGrid lookupPropertyGrid;
	private ConverterHelper converterHelper;

	public LookupConverter(Component component) {
		super();
		this.baseComponent = new HashJoin();
		this.component = component;
		this.properties = component.getProperties();
		lookupPropertyGrid = (LookupMappingGrid) properties.get(Constants.LOOKUP_MAP_FIELD);
		converterHelper = new ConverterHelper(component);

	}

	@Override
	public void prepareForXML() {
		super.prepareForXML();
		HashJoin hashJoin = (HashJoin) baseComponent;
		if (properties.get(Constants.LOOKUP_CONFIG_FIELD) != null) {
			hashJoin.getKeys().addAll(getLookupConfigKeys());
		}
		hashJoin.setMatch(getMatchValueFromUi());
	}

	private Match getMatchValueFromUi() {
		Match match = new Match();
		MatchValueProperty matchValueProperty =  (MatchValueProperty) properties.get(Constants.MATCH_PROPERTY_WIDGET);

		if(matchValueProperty != null){
			if(Constants.LAST.equalsIgnoreCase(matchValueProperty.getMatchValue())){
				match.setValue(MatchValue.LAST);
				return match;
			}else if(Constants.ALL.equalsIgnoreCase(matchValueProperty.getMatchValue())){
				match.setValue(MatchValue.ALL);
				return match;
			}else{
				match.setValue(MatchValue.FIRST);
				return match;
			}
		}
		return match;
	}

	private List<TypeKeyFields> getLookupConfigKeys() {
		List<TypeKeyFields> typeKeyFieldsList = null;
		TypeKeyFields typeKeyField = null;
		LookupConfigProperty keyFields = (LookupConfigProperty) properties.get(Constants.LOOKUP_CONFIG_FIELD);
		if (keyFields != null) {
			typeKeyFieldsList = new ArrayList<>();

			if (keyFields.getDriverKey() != null) {
				typeKeyField = new TypeKeyFields();
				typeKeyField.setInSocketId("in0");
				typeKeyField.getField().addAll(getTypeFieldName("in0", keyFields.getDriverKey()));
				typeKeyFieldsList.add(typeKeyField);
			}
			if (keyFields.getLookupKey() != null) {
				typeKeyField = new TypeKeyFields();
				typeKeyField.setInSocketId("in1");
				typeKeyField.getField().addAll(getTypeFieldName("in1", keyFields.getLookupKey()));
				typeKeyFieldsList.add(typeKeyField);
			}

		}
		return typeKeyFieldsList;
	}

	private List<TypeFieldName> getTypeFieldName(String socketID, String keyData) {
		List<TypeFieldName> typeFieldNameList = null;
		TypeFieldName typeFieldName = null;
		if (keyData != null) {
			typeFieldNameList = new ArrayList<>();
			String keyList[] = keyData.split(",");
			for (String key : keyList) {
				if (!ParameterUtil.INSTANCE.isParameter(key)) {
					typeFieldName = new TypeFieldName();
					typeFieldName.setName(key);
					typeFieldNameList.add(typeFieldName);
				} else {
					converterHelper.addParamTag(this.ID, key, ComponentXpathConstants.LOOKUP_KEYS.value().replace("$inSocketId", socketID));
				}
			}

		}

		return typeFieldNameList;
	}

	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		logger.debug("Generating TypeStraightPullOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		TypeBaseInSocket inSocketsList = new TypeBaseInSocket();
		Object obj = properties.get(Constants.LOOKUP_MAP_FIELD);

		List<TypeOperationsOutSocket> outSockectList = new ArrayList<TypeOperationsOutSocket>();
		for (Link link : component.getSourceConnections()) {
			TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();

			outSocketAsInsocket.setInSocketId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			outSocketAsInsocket.getOtherAttributes();
			// outSocket.setCopyOfInsocket(outSocketAsInsocket);
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.getOtherAttributes();
			outSockectList.add(outSocket);
			if (properties.get(Constants.LOOKUP_MAP_FIELD) != null)
				outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(
						converterHelper.getLookuporJoinOutputMaping(lookupPropertyGrid));

		}
		return outSockectList;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		int inSocketCounter = 0;
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			inSocket.setFromSocketId(converterHelper.getFromSocketId(link));
			inSocket.setId(link.getTargetTerminal());
			inSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal())
					.getNameOfPort()));
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
			inSocketCounter++;
		}
		return inSocketsList;
	}

	private void setInputProperty(HashJoin hashJoin) {
		List<TypeBaseInSocket> inputField = new ArrayList<>();
		Map<String, String> mapFields = (TreeMap<String, String>) properties.get(Constants.LOOKUP_MAP_FIELD);
		if (mapFields != null) {
			TypeInputField typeInputField = new TypeInputField();
			TypeMapField mapField = new TypeMapField();

			for (Entry<String, String> entry : mapFields.entrySet()) {
				String[] value = entry.getKey().split(Pattern.quote("."));
				if (entry.getKey().equalsIgnoreCase(entry.getValue())) {

					typeInputField.setName(entry.getKey());
					typeInputField.setInSocketId(value[0]);

				} else {
					mapField.setSourceName(entry.getKey());
					mapField.setName(entry.getValue());
					mapField.setInSocketId(value[0]);
				}
			}
		}

	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		// TODO Auto-generated method stub
		return null;
	}

}
