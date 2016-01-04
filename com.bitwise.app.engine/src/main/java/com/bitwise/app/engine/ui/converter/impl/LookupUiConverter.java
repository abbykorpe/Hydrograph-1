package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.LookupConfigProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.hashjoin.TypeKeyFields;
import com.bitwiseglobal.graph.operationstypes.HashJoin;

public class LookupUiConverter extends TransformUiConverter {


	private HashJoin lookup;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(LookupUiConverter.class);
		
	
	public LookupUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Lookup();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Lookup-Properties for -{}", componentName);
		lookup = (HashJoin) typeBaseComponent;
		LOGGER.info("LOOKUP_CONFIG_FIELD::{}",getLookupConfigProperty());
		propertyMap.put(Constants.LOOKUP_CONFIG_FIELD, getLookupConfigProperty());

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);

		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.LOOKUP.value());
		validateComponentProperties(propertyMap);
	}

	private LookupConfigProperty getLookupConfigProperty() {
		LookupConfigProperty lookupConfigProperty = null;
		List<TypeKeyFields> typeKeyFieldsList = lookup.getKeys();
		if (typeKeyFieldsList != null && !typeKeyFieldsList.isEmpty()) {
			lookupConfigProperty = new LookupConfigProperty();
			for (TypeKeyFields typeKeyFields : typeKeyFieldsList) {
				if (typeKeyFields.getInSocketId().equalsIgnoreCase("in0")
						|| typeKeyFields.getInSocketId().equalsIgnoreCase("lookup"))
					lookupConfigProperty.setLookupKey(getKeyNames(typeKeyFields));
				else if (typeKeyFields.getInSocketId().equalsIgnoreCase("in1")
						|| typeKeyFields.getInSocketId().equalsIgnoreCase("driver"))
					lookupConfigProperty.setDriverKey(getKeyNames(typeKeyFields));
			}
		}
		return lookupConfigProperty;
	}

	private String getKeyNames(TypeKeyFields typeKeyFields) {
		StringBuilder lookupKey = new StringBuilder("");
		if (typeKeyFields != null && !typeKeyFields.getField().isEmpty()) {
			for (TypeFieldName typeFieldName : typeKeyFields.getField()) {
				lookupKey.append(typeFieldName.getName()).append(",");
			}
		}
		if( lookupKey.lastIndexOf(",")!=-1)
			lookupKey=lookupKey.deleteCharAt(lookupKey.lastIndexOf(","));
		return lookupKey.toString();
	}
}
