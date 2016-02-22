package com.bitwise.app.engine.converter.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.aggregate.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeSortOrder;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Aggregate;

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
		List<String> columnNameProperties = (List<String>) component.getProperties().get(
				Constants.PROPERTY_COLUMN_NAME);
		if (columnNameProperties != null) {
			TypePrimaryKeyFields primaryKeyFields = new TypePrimaryKeyFields();
			aggregate.setPrimaryKeys(primaryKeyFields);
			List<TypeFieldName> field = primaryKeyFields.getField();
			for (String columnNameProperty : columnNameProperties) {
				TypeFieldName fieldName = new TypeFieldName();
				fieldName.setName(columnNameProperty);
				field.add(fieldName);
			}
		}
	}

	private void setSecondaryKeys(Aggregate aggregate) {
		logger.debug("Generating XML for :{}", properties.get(Constants.PROPERTY_SECONDARY_COLUMN_KEYS));
		Map<String, String> gridRow = (Map<String, String>) component.getProperties().get(
				Constants.PROPERTY_SECONDARY_COLUMN_KEYS);
		if (gridRow != null) {
			TypeSecondaryKeyFields secondaryKeyFields = new TypeSecondaryKeyFields();
			aggregate.setSecondaryKeys(secondaryKeyFields);
			List<TypeSecondayKeyFieldsAttributes> field = secondaryKeyFields.getField();
			for (Entry<String, String> gridRowEntry : gridRow.entrySet()) {
				TypeSecondayKeyFieldsAttributes fieldsAttributes = new TypeSecondayKeyFieldsAttributes();
				TypeSortOrder order = TypeSortOrder.fromValue(gridRowEntry.getValue().toLowerCase());
				fieldsAttributes.setName(gridRowEntry.getKey());
				fieldsAttributes.setOrder(order);
				field.add(fieldsAttributes);
			}
		}
	}
}
