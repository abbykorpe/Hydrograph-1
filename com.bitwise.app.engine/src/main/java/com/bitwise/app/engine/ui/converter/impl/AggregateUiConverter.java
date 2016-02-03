package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.aggregate.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.operationstypes.Aggregate;

/**
 * The class AggregateUiConverter
 * 
 * @author Bitwise
 * 
 */

public class AggregateUiConverter extends TransformUiConverter {

	private Aggregate aggregate;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(AggregateUiConverter.class);

	public AggregateUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Aggregate();
		this.propertyMap = new LinkedHashMap<>();
	}

	/* 
	 * 
	 */
	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Aggregate-Properties for -{}", componentName);
		aggregate = (Aggregate) typeBaseComponent;

		
		propertyMap.put(Constants.PROPERTY_COLUMN_NAME, getPrimaryKeys());
		propertyMap.put(Constants.PROPERTY_SECONDARY_COLUMN_KEYS, getSecondaryKeys());
		propertyMap.put(Constants.PARAM_OPERATION, createTransformPropertyGrid());

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.AGGREGATE.value());
		
		validateComponentProperties(propertyMap);
	}

	private Map<String, String> getSecondaryKeys() {
		LOGGER.debug("Fetching Aggregate-Secondary-Key-Properties for -{}", componentName);
		Map<String, String> secondaryKeyMap = null;
		aggregate = (Aggregate) typeBaseComponent;
		TypeSecondaryKeyFields typeSecondaryKeyFields = aggregate.getSecondaryKeys();

		if (typeSecondaryKeyFields != null) {
			secondaryKeyMap = new LinkedHashMap<String, String>();
			for (TypeSecondayKeyFieldsAttributes secondayKeyFieldsAttributes : typeSecondaryKeyFields.getField()) {
				secondaryKeyMap.put(secondayKeyFieldsAttributes.getName(), secondayKeyFieldsAttributes.getOrder()
						.value());

			}
		}

		return secondaryKeyMap;
	}

	private List<String> getPrimaryKeys() {
		LOGGER.debug("Fetching Aggregate-Primary-Key-Properties for -{}", componentName);
		List<String> primaryKeySet = null;
		aggregate = (Aggregate) typeBaseComponent;
		TypePrimaryKeyFields typePrimaryKeyFields = aggregate.getPrimaryKeys();
		if (typePrimaryKeyFields != null) {

			primaryKeySet = new LinkedList<String>();
			for (TypeFieldName fieldName : typePrimaryKeyFields.getField()) {
				primaryKeySet.add(fieldName.getName());
			}
		}
		return primaryKeySet;
	}

}
