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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ParameterUtil;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.StraightPullConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.KeepValue;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeSortOrder;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;
import com.bitwiseglobal.graph.removedups.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.removedups.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.removedups.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups.Keep;

/**
 * @author Bitwise Converter implementation for RemoveDups component
 */

public class RemoveDupsConverter extends StraightPullConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(RemoveDupsConverter.class);
	private ConverterHelper converterHelper;

	public RemoveDupsConverter(Component component) {
		super();
		this.baseComponent = new RemoveDups();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for : {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		RemoveDups dedup = (RemoveDups) baseComponent;
		dedup.setKeep(getKeep());
		dedup.setPrimaryKeys(getPrimaryKeys());
		dedup.setSecondaryKeys(getSecondaryKeys());
	}

	private TypePrimaryKeyFields getPrimaryKeys() {

		List<String> fieldValueSet = (List<String>) properties.get(PropertyNameConstants.DEDUP_FILEDS.value());

		TypePrimaryKeyFields typePrimaryKeyFields = null;
		if (fieldValueSet != null) {
			typePrimaryKeyFields = new TypePrimaryKeyFields();
			List<TypeFieldName> fieldNameList = typePrimaryKeyFields.getField();
			for (String value : fieldValueSet) {
				if(!ParameterUtil.INSTANCE.isParameter(value)){
					TypeFieldName field = new TypeFieldName();
					field.setName(value);
					fieldNameList.add(field);
				}else{
					converterHelper.addParamTag(this.ID, value, ComponentXpathConstants.STRAIGHTPULL_PRIMARY_KEYS.value());
				}
			}

		}
		return typePrimaryKeyFields;
	}

	private TypeSecondaryKeyFields getSecondaryKeys() {

		Map<String, String> fieldValueMap = (Map<String, String>) properties.get(PropertyNameConstants.SECONDARY_COLUMN_KEYS.value());

		TypeSecondaryKeyFields typeSecondaryKeyFields = null;
		if (fieldValueMap != null && !fieldValueMap.isEmpty()) {
			typeSecondaryKeyFields = new TypeSecondaryKeyFields();
			List<TypeSecondayKeyFieldsAttributes> fieldNameList = typeSecondaryKeyFields.getField();
			for (Map.Entry<String, String> secondaryKeyRowEntry : fieldValueMap.entrySet()) {
				if(!ParameterUtil.INSTANCE.isParameter(secondaryKeyRowEntry.getKey())){
					TypeSecondayKeyFieldsAttributes field = new TypeSecondayKeyFieldsAttributes();
					field.setName(secondaryKeyRowEntry.getKey());
					field.setOrder(TypeSortOrder.fromValue(secondaryKeyRowEntry.getValue().toLowerCase()));
					fieldNameList.add(field);
				}else{
					converterHelper.addParamTag(this.ID, secondaryKeyRowEntry.getKey(), ComponentXpathConstants.STRAIGHTPULL_SECONDARY_KEYS.value());
				}
			}
		}
		return typeSecondaryKeyFields;
	}

	private Keep getKeep() {
		logger.debug("Generating Retention Logic for ::{}", componentName);
		String keepValue =(String) properties.get(PropertyNameConstants.RETENTION_LOGIC_KEEP.value());
		Keep keep = new Keep();
		if(StringUtils.isNotBlank(keepValue))
		keep.setValue(KeepValue.fromValue(StringUtils.lowerCase(keepValue)));
		else
		keep.setValue(KeepValue.fromValue(StringUtils.lowerCase(Constants.FIRST)));	
		return keep;
	}

	@Override
	protected List<TypeStraightPullOutSocket> getOutSocket() {
		logger.debug("Generating TypeStraightPullOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeStraightPullOutSocket> outSockectList = new ArrayList<TypeStraightPullOutSocket>();

		for (Link link : component.getSourceConnections()) {
			TypeStraightPullOutSocket outSocket = new TypeStraightPullOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
			outSocketAsInsocket.setInSocketId(Constants.FIXED_INSOCKET_ID);
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
