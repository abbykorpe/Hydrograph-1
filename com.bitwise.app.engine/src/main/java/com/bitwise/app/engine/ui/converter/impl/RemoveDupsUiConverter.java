package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.StraightfullUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.RemovedupsComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;

public class RemoveDupsUiConverter extends StraightfullUiConverter {
		private RemoveDups removeDups;
		private static final String COMPONENT_NAME_SUFFIX = "RemoveDups_";

		public RemoveDupsUiConverter(TypeBaseComponent typeBaseComponent,
				Container container) {
			this.container = container;
			this.typeBaseComponent = typeBaseComponent;
			this.uiComponent = new RemovedupsComponent();
			this.propertyMap = new LinkedHashMap<>();
		}

		@Override
		public void prepareUIXML() {

			super.prepareUIXML();

			removeDups = (RemoveDups) typeBaseComponent;
			propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),new TreeMap<>());
			propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
			uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY	.value());
			container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
			container.getComponentNames().add(COMPONENT_NAME);
			uiComponent.setProperties(propertyMap);
			uiComponent.setType(UIComponentsConstants.REMOVE_DUPS.value());
			uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY.value());
		
		}

		
	}
