package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.StraightPullConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
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

public class RemoveDupsConverter extends StraightPullConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(RemoveDupsConverter.class);

	public RemoveDupsConverter(Component component) {
		super();
		this.baseComponent = new RemoveDups();
		this.component = component;
		this.properties = component.getProperties();
	}

	@Override
	public void prepareForXML(){
		logger.debug("Genrating XML for : {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		RemoveDups dedup = (RemoveDups) baseComponent;
		dedup.setKeep(getKeep());
		dedup.setPrimaryKeys(getPrimaryKeys());
		dedup.setSecondaryKeys(getSecondaryKeys());
	}

	private TypePrimaryKeyFields getPrimaryKeys() {

		Set<String> fieldValueSet = (HashSet<String>) properties.get(PropertyNameConstants.DEDUP_FILEDS.value());

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
	
	private TypeSecondaryKeyFields getSecondaryKeys() {

		Map<String,String> fieldValueMap = (TreeMap<String,String>) properties.get(PropertyNameConstants.SECONDARY_COLUMN_KEYS.value());

		TypeSecondaryKeyFields  typeSecondaryKeyFields= null;
		if (fieldValueMap != null) {
			typeSecondaryKeyFields = new TypeSecondaryKeyFields();
			List<TypeSecondayKeyFieldsAttributes> fieldNameList = typeSecondaryKeyFields.getField();
			for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
				TypeSecondayKeyFieldsAttributes field = new TypeSecondayKeyFieldsAttributes();
				field.setName(entry.getKey());
				field.setOrder(TypeSortOrder.fromValue(entry.getValue()));
				fieldNameList.add(field);
			}
		}
		return typeSecondaryKeyFields;
	}

	private Keep getKeep() {
		logger.debug("Genrating Retention Logic for ::{}", componentName);
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
		logger.debug("Genrating TypeStraightPullOutSocket data for : {}",
				properties.get(Constants.PARAM_NAME));
		List<TypeStraightPullOutSocket> outSockectList = new ArrayList<TypeStraightPullOutSocket>();
		
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
		}
		return outSockectList;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Genrating TypeBaseInSocket data for :{}", component
				.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			
			inSocket.setFromComponentId((String) link.getSource()
					.getProperties().get(Constants.PARAM_NAME));
			inSocket.setFromSocketId(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort())+link.getLinkNumber());
			inSocket.setId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			inSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
			
		}
		return inSocketsList;
	}

}
