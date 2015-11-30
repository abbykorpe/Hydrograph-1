package com.bitwise.app.engine.ui.converter.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.aggregate.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.aggregate.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.operationstypes.Aggregate;

public class AggregateUiConverter extends TransformUIConverter{

	private Aggregate aggregate;
	private static final String COMPONENT_NAME_SUFFIX = "Aggregate_";

	public AggregateUiConverter(TypeBaseComponent typeBaseComponent,
			Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Aggregate();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();

		aggregate = (Aggregate) typeBaseComponent;
		
	
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
		propertyMap.put(Constants.PROPERTY_COLUMN_NAME,getPrimaryKeys());
		propertyMap.put(Constants.PROPERTY_SECONDARY_COLUMN_KEYS, getSecondaryKeys());
		
	
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(COMPONENT_NAME);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.AGGREGATE.value());
		uiComponent.setCategory(UIComponentsConstants.TRANSFORM_CATEGORY.value());
	
	}

	private Map getSecondaryKeys() {
		Map<String,String> secondaryKeyMap=null;
		aggregate = (Aggregate) typeBaseComponent;
		TypeSecondaryKeyFields  typeSecondaryKeyFields=aggregate.getSecondaryKeys();
		
		if(typeSecondaryKeyFields!=null){
			secondaryKeyMap=new TreeMap<String, String>();
			for(TypeSecondayKeyFieldsAttributes secondayKeyFieldsAttributes:typeSecondaryKeyFields.getField()){
				secondaryKeyMap.put(secondayKeyFieldsAttributes.getName(), secondayKeyFieldsAttributes.getOrder().value());
			
			}
		}
		
		return secondaryKeyMap;
	}

	private HashSet<String> getPrimaryKeys() {
		HashSet<String> primaryKeySet =null;
		aggregate = (Aggregate) typeBaseComponent;
		TypePrimaryKeyFields typePrimaryKeyFields = aggregate.getPrimaryKeys();
		if(typePrimaryKeyFields!=null){	
			
			primaryKeySet=new  HashSet<String>();
				for(TypeFieldName fieldName:typePrimaryKeyFields.getField()){
					primaryKeySet.add(fieldName.getName());
			}
		}
		return primaryKeySet;
	}

	

	
}
