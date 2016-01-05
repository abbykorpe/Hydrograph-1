package com.bitwise.app.engine.ui.converter.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.StraightpullUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.RemovedupsComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.removedups.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.removedups.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.removedups.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;

public class RemoveDupsUiConverter extends StraightpullUiConverter {
	private RemoveDups removeDups;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(RemoveDupsUiConverter.class);

	public RemoveDupsUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new RemovedupsComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching RemoveDups-Properties for -{}", componentName);
		removeDups = (RemoveDups) typeBaseComponent;

		propertyMap.put(PropertyNameConstants.RETENTION_LOGIC_KEEP.value(), removeDups.getKeep().getValue().value());
		propertyMap.put(PropertyNameConstants.DEDUP_FILEDS.value(), getPrimaryKeys());
		propertyMap.put(PropertyNameConstants.SECONDARY_COLUMN_KEYS.value(), getSecondaryKeys());

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.REMOVE_DUPS.value());
		validateComponentProperties(propertyMap);
	}

	private Map<String, String> getSecondaryKeys() {
		LOGGER.debug("Fetching RemoveDups-Secondary-Keys-Properties for -{}", componentName);
		Map<String, String> secondaryKeyMap = null;
		removeDups = (RemoveDups) typeBaseComponent;
		TypeSecondaryKeyFields typeSecondaryKeyFields = removeDups.getSecondaryKeys();

		if (typeSecondaryKeyFields != null) {
			secondaryKeyMap = new TreeMap<String, String>();
			for (TypeSecondayKeyFieldsAttributes secondayKeyFieldsAttributes : typeSecondaryKeyFields.getField()) {
				secondaryKeyMap.put(secondayKeyFieldsAttributes.getName(), secondayKeyFieldsAttributes.getOrder()
						.value());

			}
		}

		return secondaryKeyMap;
	}

	private Set<String> getPrimaryKeys() {
		LOGGER.debug("Fetching RemoveDups-Primary-Keys-Properties for -{}", componentName);
		HashSet<String> primaryKeySet = null;
		removeDups = (RemoveDups) typeBaseComponent;
		TypePrimaryKeyFields typePrimaryKeyFields = removeDups.getPrimaryKeys();
		if (typePrimaryKeyFields != null) {

			primaryKeySet = new HashSet<String>();
			for (TypeFieldName fieldName : typePrimaryKeyFields.getField()) {
				primaryKeySet.add(fieldName.getName());
			}
		}
		return primaryKeySet;
	}

}
