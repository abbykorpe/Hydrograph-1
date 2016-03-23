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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ParameterUtil;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.aggregate.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeSortOrder;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Aggregate;

/**
 * @author Bitwise Aggregate converter
 */

public class AggregateConverter extends TransformConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(AggregateConverter.class);
	private ATMapping atMapping;
	private List<FixedWidthGridRow> fixedWidthGridRows;
	ConverterHelper converterHelper;

	public AggregateConverter(Component component) {
		super();
		this.baseComponent = new Aggregate();
		this.component = component;
		this.properties = component.getProperties();
		atMapping = (ATMapping) properties.get(Constants.PARAM_OPERATION);
		converterHelper = new ConverterHelper(component);
		initFixedWidthGridRows();
	}


	private void initFixedWidthGridRows() {
		fixedWidthGridRows = new LinkedList<>();
		Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) properties
				.get(Constants.SCHEMA_TO_PROPAGATE);
		if (schemaMap != null && schemaMap.get(Constants.FIXED_OUTSOCKET_ID) != null) {
			ComponentsOutputSchema componentsOutputSchema = schemaMap.get(Constants.FIXED_OUTSOCKET_ID);
			List<FixedWidthGridRow> gridRows = componentsOutputSchema.getFixedWidthGridRowsOutputFields();

			for (FixedWidthGridRow row : gridRows) {
				fixedWidthGridRows.add((FixedWidthGridRow) row.copy());
			}
		}
	}


	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();

		Aggregate aggregate = (Aggregate) baseComponent;
		aggregate.getOperation().addAll(getOperations());
		setPrimaryKeys(aggregate);
		setSecondaryKeys(aggregate);
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		return converterHelper.getOperations(atMapping,fixedWidthGridRows);
	}

	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		return converterHelper.getOutSocket(atMapping,fixedWidthGridRows);
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		return converterHelper.getInSocket();
	}

	private void setPrimaryKeys(Aggregate aggregate) {
		logger.debug("Generating XML for :{}", properties.get(Constants.PROPERTY_COLUMN_NAME));
		List<String> columnNameProperties = (List<String>) component.getProperties()
				.get(Constants.PROPERTY_COLUMN_NAME);
		if (columnNameProperties != null && !columnNameProperties.isEmpty()) {
			TypePrimaryKeyFields primaryKeyFields = new TypePrimaryKeyFields();
			aggregate.setPrimaryKeys(primaryKeyFields);
			List<TypeFieldName> field = primaryKeyFields.getField();
			if (!isALLParameterizedFields(columnNameProperties)) {
				for (String columnNameProperty : columnNameProperties) {
					if (!ParameterUtil.INSTANCE.isParameter(columnNameProperty)) {
						TypeFieldName fieldName = new TypeFieldName();
						fieldName.setName(columnNameProperty);
						field.add(fieldName);
					} else {
						converterHelper.addParamTag(this.ID, columnNameProperty,
								ComponentXpathConstants.OPERATIONS_PRIMARY_KEYS.value(), false);
					}
				}
			} else {
				StringBuffer parameterFieldNames = new StringBuffer();
				TypeFieldName fieldName = new TypeFieldName();
				fieldName.setName("");
				field.add(fieldName);
				for (String fName : columnNameProperties)
					parameterFieldNames.append(fName + " ");
				converterHelper.addParamTag(this.ID, parameterFieldNames.toString(),
						ComponentXpathConstants.OPERATIONS_PRIMARY_KEYS.value(), true);
			}
		}
	}

	private boolean isALLParameterizedFields(List<String> componentOperationFields) {
		for (String fieldName : componentOperationFields)
			if (!ParameterUtil.INSTANCE.isParameter(fieldName))
				return false;
		return true;
	}
	
	private boolean isALLParameterizedFields(Map<String, String> secondaryKeyRow) {
		for (Entry<String, String> secondaryKeyRowEntry : secondaryKeyRow.entrySet())
			if (!ParameterUtil.INSTANCE.isParameter(secondaryKeyRowEntry.getKey()))
				return false;
		return true;
	}


	private void setSecondaryKeys(Aggregate aggregate) {
		logger.debug("Generating XML for :{}", properties.get(Constants.PROPERTY_SECONDARY_COLUMN_KEYS));
		Map<String, String> secondaryKeyRow = (Map<String, String>) component.getProperties().get(
				Constants.PROPERTY_SECONDARY_COLUMN_KEYS);
		if (secondaryKeyRow != null && !secondaryKeyRow.isEmpty()) {
			TypeSecondaryKeyFields secondaryKeyFields = new TypeSecondaryKeyFields();
			aggregate.setSecondaryKeys(secondaryKeyFields);
			List<TypeSecondayKeyFieldsAttributes> field = secondaryKeyFields.getField();
			if (!isALLParameterizedFields(secondaryKeyRow)) {

				for (Entry<String, String> secondaryKeyRowEntry : secondaryKeyRow.entrySet()) {

					if (!ParameterUtil.INSTANCE.isParameter(secondaryKeyRowEntry.getKey())) {
						TypeSecondayKeyFieldsAttributes fieldsAttributes = new TypeSecondayKeyFieldsAttributes();
						fieldsAttributes.setName(secondaryKeyRowEntry.getKey());
						TypeSortOrder order = TypeSortOrder.fromValue(secondaryKeyRowEntry.getValue().toLowerCase());
						fieldsAttributes.setOrder(order);
						field.add(fieldsAttributes);
					} else {
						converterHelper.addParamTag(this.ID, secondaryKeyRowEntry.getKey(),
								ComponentXpathConstants.OPERATIONS_SECONDARY_KEYS.value(), false);
					}
				}
			} else {
				StringBuffer parameterFieldNames = new StringBuffer();
				TypeSecondayKeyFieldsAttributes fieldsAttributes = new TypeSecondayKeyFieldsAttributes();
				fieldsAttributes.setName("");
				field.add(fieldsAttributes);
				for (Entry<String, String> secondaryKeyRowEntry : secondaryKeyRow.entrySet())
					parameterFieldNames.append(secondaryKeyRowEntry.getKey() + " ");
				converterHelper.addParamTag(this.ID, parameterFieldNames.toString(),
						ComponentXpathConstants.OPERATIONS_SECONDARY_KEYS.value(), true);
			}
		}
	}
}
