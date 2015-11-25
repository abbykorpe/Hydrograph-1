package com.bitwise.app.engine.converter.impl;

import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.exceptions.PhaseException;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Transform;

public class TransformComponentConverter extends TransformConverter {
	Logger logger = LogFactory.INSTANCE.getLogger(TransformComponentConverter.class);
	TransformPropertyGrid transformPropertyGrid;
	ConverterHelper converterHelper;
	
	public TransformComponentConverter(Component component){
		super();	
		this.baseComponent = new Transform();
		this.component = component;
		this.properties = component.getProperties();
		transformPropertyGrid = (TransformPropertyGrid) properties.get("operation") ;
		converterHelper = new ConverterHelper(component); 
	}
	
	@Override
	public void prepareForXML() throws PhaseException, SchemaException {
		logger.debug("Generating XML for :{}", properties.get(NAME));
		super.prepareForXML();
		Transform transform = (Transform) baseComponent;
		transform.getOperation().addAll(getOperations());
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
}
