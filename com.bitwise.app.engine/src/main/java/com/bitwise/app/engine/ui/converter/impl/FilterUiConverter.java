package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Filter;
/**
 * The class FilterUiConverter
 * 
 * @author Bitwise
 * 
 */
public class FilterUiConverter extends TransformUiConverter{

	private Filter filter;
	

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
		
		
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.FILTER.value());
		
		validateComponentProperties(propertyMap);
	}

	private OperationClassProperty getOperationClass() {
		OperationClassProperty operationClassProperty=null;
		String clazz=null;
		if(filter.getOperation()!=null && filter.getOperation().size()!=0){
			clazz=filter.getOperation().get(0).getClazz();
			operationClassProperty=new OperationClassProperty(getOperationClassName(clazz),clazz, isParameter(clazz));
		}
		return operationClassProperty;
	}


	private List<String> getOperationFileds() {
		List<String> componentOperationFileds=null;
		
			for(TypeTransformOperation transformOperation:filter.getOperation())
			{	if(transformOperation.getInputFields()!=null){
					componentOperationFileds=new ArrayList<>();
						for(TypeInputField inputFileds:transformOperation.getInputFields().getField()){
							componentOperationFileds.add(inputFileds.getName());
				}
			}}
			return componentOperationFileds;
	}
	
}