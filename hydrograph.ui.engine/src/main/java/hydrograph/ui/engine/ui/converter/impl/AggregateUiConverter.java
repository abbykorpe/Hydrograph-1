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

 
package hydrograph.ui.engine.ui.converter.impl;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.TransformUiConverter;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

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
		this.uiComponent = new hydrograph.ui.graph.model.components.Aggregate();
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
