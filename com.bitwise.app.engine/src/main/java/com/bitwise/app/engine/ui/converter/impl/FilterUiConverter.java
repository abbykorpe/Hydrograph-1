package com.bitwise.app.engine.ui.converter.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Filter;

public class FilterUiConverter extends TransformUIConverter{

	private Filter filter;
	private static final String COMPONENT_NAME_SUFFIX = "Filter_";

	public FilterUiConverter(TypeBaseComponent typeBaseComponent,Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Filter();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();

		filter = (Filter) typeBaseComponent;
		
		propertyMap.put(PropertyNameConstants.OPERATION_CLASS.value(),getOperationClass());
		propertyMap.put(PropertyNameConstants.OPERATION_FILEDS.value(), getOperationFileds());
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
		
		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY	.value());
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(COMPONENT_NAME);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.REMOVE_DUPS.value());
		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY.value());
	
	}

	private OperationClassProperty getOperationClass() {
		OperationClassProperty operationClassProperty=null;
		String clazz=null;
		if(filter.getOperation()!=null && filter.getOperation().size()!=0){
			clazz=filter.getOperation().get(0).getClazz();
			operationClassProperty=new OperationClassProperty(clazz, isParameter(clazz));
		}
		return operationClassProperty;
	}


	private HashSet<String> getOperationFileds() {
		HashSet<String> componentOperationFileds=null;
		
			for(TypeTransformOperation transformOperation:filter.getOperation())
			{	if(transformOperation.getInputFields()!=null){
					componentOperationFileds=new HashSet<>();
						for(TypeInputField inputFileds:transformOperation.getInputFields().getField()){
							componentOperationFileds.add(inputFileds.getName());
				}
			}}
			return componentOperationFileds;
	}
	
}