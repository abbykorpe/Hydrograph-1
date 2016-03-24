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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.converter.StraightPullConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpath;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.engine.xpath.ComponentsAttributeAndValue;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;
import com.bitwiseglobal.graph.straightpulltypes.Limit;

/**
 * Converter to convert Limit component into engine specific limit object
 * 
 * @author BITWISE
 */
public class LimitConverter extends StraightPullConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(LimitConverter.class);
	private ConverterHelper converterHelper;
	
	public LimitConverter(Component component) {
		super();
		this.baseComponent = new Limit();
		this.component = component;
		this.properties = component.getProperties();
		this.converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Limit limit = (Limit) baseComponent;
		String count =(String)properties.get(Constants.PARAM_COUNT);
		
		if (StringUtils.isNotBlank(count)){
			Limit.MaxRecords value = new Limit.MaxRecords();
			limit.setMaxRecords(value);
			try{
				Long longCount = Long.parseLong(count);
				value.setValue(StringUtils.isBlank(count) ? null : longCount);
			}
			catch(NumberFormatException exception){
				ComponentXpath.INSTANCE.getXpathMap().put(
						(ComponentXpathConstants.COMPONENT_XPATH_COUNT.value().replace(ID, componentName)),
						new ComponentsAttributeAndValue(null, count));
			}
		}		
	}

	@Override
	protected List<TypeStraightPullOutSocket> getOutSocket() {
		logger.debug("getOutSocket - Generating TypeStraightPullOutSocket data for :{}",
				properties.get(Constants.PARAM_NAME));
		List<TypeStraightPullOutSocket> outSockectList = new ArrayList<TypeStraightPullOutSocket>();

		for (Link link : component.getSourceConnections()) {
			TypeStraightPullOutSocket outSocket = new TypeStraightPullOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
			outSocketAsInsocket.setInSocketId(Constants.IN_0);
			outSocketAsInsocket.getOtherAttributes();
			outSocket.setCopyOfInsocket(outSocketAsInsocket);
			outSocket.setId(link.getSource().getPort(link.getSourceTerminal()).getPortType() + link.getLinkNumber());
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