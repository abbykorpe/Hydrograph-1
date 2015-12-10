package com.bitwise.app.engine.converter.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
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
	TransformPropertyGrid transformPropertyGrid;
	ConverterHelper converterHelper;
	
	public AggregateConverter(Component component){
		super();	
		this.baseComponent = new Aggregate();
		this.component = component;
		this.properties = component.getProperties();
		transformPropertyGrid = (TransformPropertyGrid) properties.get(Constants.PARAM_OPERATION);
		converterHelper = new ConverterHelper(component); 
	}
	
	@Override
	public void prepareForXML(){
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		
		Aggregate aggregate = (Aggregate) baseComponent;
		aggregate.getOperation().addAll(getOperations());
		setPrimaryKeys(aggregate);
		setSecondaryKeys(aggregate);
	}
	
	@Override
	protected List<TypeTransformOperation> getOperations() {
		return converterHelper.getOperations(transformPropertyGrid);
	}
	
	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		return converterHelper.getOutSocket(transformPropertyGrid);
	}
	
	@Override
	public List<TypeBaseInSocket> getInSocket() {
		return converterHelper.getInSocket();
	}
	
	
	private void setPrimaryKeys(Aggregate aggregate){
		logger.debug("Generating XML for :{}", properties.get(Constants.PROPERTY_COLUMN_NAME));
		Set<String> columnNameProperties = (HashSet<String>) component.getProperties().get(Constants.PROPERTY_COLUMN_NAME);
		if(columnNameProperties != null){
		TypePrimaryKeyFields primaryKeyFields = new TypePrimaryKeyFields();
		aggregate.setPrimaryKeys(primaryKeyFields);
		List<TypeFieldName> field = primaryKeyFields.getField();
			for(String columnNameProperty : columnNameProperties){
				TypeFieldName fieldName = new TypeFieldName(); 
				fieldName.setName(columnNameProperty);
				field.add(fieldName);
			}
		}
	}
	
	private void setSecondaryKeys(Aggregate aggregate) {
		logger.debug("Generating XML for :{}", properties.get(Constants.PROPERTY_SECONDARY_COLUMN_KEYS));
		Map<String,String> gridRow = (Map<String,String>) component.getProperties().get(Constants.PROPERTY_SECONDARY_COLUMN_KEYS);
		if(gridRow != null){
		TypeSecondaryKeyFields secondaryKeyFields = new TypeSecondaryKeyFields();
		aggregate.setSecondaryKeys(secondaryKeyFields);
		List<TypeSecondayKeyFieldsAttributes> field = secondaryKeyFields.getField();
			for(Entry<String, String> gridRowEntry : gridRow.entrySet()){
				TypeSecondayKeyFieldsAttributes fieldsAttributes = new TypeSecondayKeyFieldsAttributes();
				TypeSortOrder order = TypeSortOrder.fromValue(gridRowEntry.getValue().toLowerCase());
				fieldsAttributes.setName(gridRowEntry.getKey());
				fieldsAttributes.setOrder(order);
				field.add(fieldsAttributes);
			}
		}
	}
}
