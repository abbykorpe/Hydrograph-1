package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.PortTypeConstant;
import com.bitwise.app.engine.converter.PropertyNameConstants;
import com.bitwise.app.engine.converter.StraightPullConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwiseglobal.graph.commontypes.KeepValue;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;
import com.bitwiseglobal.graph.removedups.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups.Keep;

public class RemoveDupsConverter extends StraightPullConverter {

	Logger LOGGER = LogFactory.INSTANCE.getLogger(RemoveDupsConverter.class);

	public RemoveDupsConverter(Component component) {
		super();
		this.baseComponent = new RemoveDups();
		this.component = component;
		this.properties = component.getProperties();
	}

	@Override
	public void prepareForXML(){
		LOGGER.debug("Genrating XML for : {}", properties.get(NAME));
		super.prepareForXML();
		RemoveDups dedup = (RemoveDups) baseComponent;
		dedup.setRuntimeProperties(getRuntimeProperties());
		dedup.setKeep(getKeep());
		dedup.setPrimaryKeys(getPrimaryKeys());
	}

	private TypePrimaryKeyFields getPrimaryKeys() {

		HashSet<String> fieldValueSet = ((HashSet<String>) properties.get(PropertyNameConstants.DEDUP_FILEDS.value()));

		TypePrimaryKeyFields typePrimaryKeyFields = null;
		if (fieldValueSet != null) {
			typePrimaryKeyFields = new TypePrimaryKeyFields();
			List<TypeFieldName> fieldNameList = typePrimaryKeyFields.getField();
			for (String value : fieldValueSet) {
				TypeFieldName field = new TypeFieldName();
				field.setName(value);
				fieldNameList.add(field);
			}
			
		}
		return typePrimaryKeyFields;
	}

	private Keep getKeep() {
		LOGGER.debug("Genrating Retention Logic for ::{}", componentName);
		String keepValue = properties.get(
				PropertyNameConstants.RETENTION_LOGIC_KEEP.value()).toString();
		Keep keep = new Keep();
		if(keepValue.toLowerCase().contains("unique"))
			keepValue="uniqueonly";
		keep.setValue(KeepValue.fromValue(keepValue.toLowerCase()));
		return keep;
	}

	@Override
	protected List<TypeStraightPullOutSocket> getOutSocket() {
		LOGGER.debug("Genrating TypeStraightPullOutSocket data for : {}",
				properties.get(NAME));
		List<TypeStraightPullOutSocket> outSockectList = new ArrayList<TypeStraightPullOutSocket>();
		int outSocketCounter=1;
		for (Link link : component.getSourceConnections()) {
			TypeStraightPullOutSocket outSocket = new TypeStraightPullOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
			outSocketAsInsocket.setInSocketId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			outSocketAsInsocket.getOtherAttributes();
			outSocket.setCopyOfInsocket(outSocketAsInsocket);
			outSocket.setId(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort());
			outSocket.setType(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort()));
			outSocket.getOtherAttributes();
			outSockectList.add(outSocket);
			outSocketCounter++;
		}
		return outSockectList;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		LOGGER.debug("Genrating TypeBaseInSocket data for :{}", component
				.getProperties().get(NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			
			inSocket.setFromComponentId((String) link.getSource()
					.getProperties().get(NAME));
			inSocket.setFromSocketId(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort())+link.getLinkNumber());
			inSocket.setId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			inSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
			
		}
		return inSocketsList;
	}

}
