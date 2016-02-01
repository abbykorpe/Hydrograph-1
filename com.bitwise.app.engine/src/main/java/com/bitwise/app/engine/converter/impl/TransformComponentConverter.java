package com.bitwise.app.engine.converter.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Transform;

public class TransformComponentConverter extends TransformConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(TransformComponentConverter.class);
	private ATMapping atMapping;
	private List<FixedWidthGridRow> fixedWidthGridRows;
	ConverterHelper converterHelper;

	public TransformComponentConverter(Component component) {
		super();
		this.baseComponent = new Transform();
		this.component = component;
		this.properties = component.getProperties();
		atMapping = (ATMapping) properties.get(Constants.PARAM_OPERATION);
		converterHelper = new ConverterHelper(component);
		initFixedWidthGridRows();
	}

	
	private void initFixedWidthGridRows(){
		fixedWidthGridRows = new LinkedList<>();
		ComponentsOutputSchema componentsOutputSchema  = (ComponentsOutputSchema) properties.get(Constants.SCHEMA_TO_PROPAGATE);
		if(componentsOutputSchema!=null){
			List<FixedWidthGridRow> gridRows = componentsOutputSchema.getFixedWidthGridRowsOutputFields();			
			for(FixedWidthGridRow row : gridRows){
				fixedWidthGridRows.add((FixedWidthGridRow) row.copy());
			}
		}
			
	}
	
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Transform transform = (Transform) baseComponent;
		transform.getOperation().addAll(getOperations());
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
}
