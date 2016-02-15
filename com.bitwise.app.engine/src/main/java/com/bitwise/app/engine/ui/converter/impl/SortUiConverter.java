package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.StraightpullUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.RemovedupsComponent;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.sort.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.sort.TypePrimaryKeyFieldsAttributes;
import com.bitwiseglobal.graph.sort.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.sort.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.straightpulltypes.Sort;

/**
 * Converter to convert jaxb sort object into sort component
 *
 */
public class SortUiConverter extends StraightpullUiConverter {
	private Sort sort;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(SortUiConverter.class);

	public SortUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new RemovedupsComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Sort-Properties for -{}", componentName);
		sort = (Sort) typeBaseComponent;

		propertyMap.put(Constants.PARAM_PRIMARY_COLUMN_KEYS, getPrimaryKeys());
		propertyMap.put(Constants.PARAM_SECONDARY_COLUMN_KEYS, getSecondaryKeys());

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.SORT.value());
		validateComponentProperties(propertyMap);
	}

	private Map<String, String> getSecondaryKeys() {
		LOGGER.debug("Fetching Sort-Secondary-Keys-Properties for -{}", componentName);
		Map<String, String> secondaryKeyMap = null;
		sort = (Sort) typeBaseComponent;
		TypeSecondaryKeyFields typeSecondaryKeyFields = sort.getSecondaryKeys();

		if (typeSecondaryKeyFields != null) {
			secondaryKeyMap = new LinkedHashMap<String, String>();
			for (TypeSecondayKeyFieldsAttributes secondayKeyFieldsAttributes : typeSecondaryKeyFields.getField()) {
				secondaryKeyMap.put(secondayKeyFieldsAttributes.getName(), secondayKeyFieldsAttributes.getOrder().value());
			}
		}
		return secondaryKeyMap;
	}

	private Map<String, String> getPrimaryKeys() {
		LOGGER.debug("Fetching RemoveDups-Primary-Keys-Properties for -{}", componentName);
		Map<String, String> primaryKeyMap = null;
		sort = (Sort) typeBaseComponent;
		TypePrimaryKeyFields typePrimaryKeyFields = sort.getPrimaryKeys();

		if (typePrimaryKeyFields != null) {
			primaryKeyMap = new LinkedHashMap<String, String>();
			for (TypePrimaryKeyFieldsAttributes primaryKeyFieldsAttributes : typePrimaryKeyFields.getField()) {
				primaryKeyMap.put(primaryKeyFieldsAttributes.getName(), primaryKeyFieldsAttributes.getOrder().value());
			}
		}
		return primaryKeyMap;
	}

}