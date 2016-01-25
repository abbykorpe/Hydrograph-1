package com.bitwise.app.engine.converter.impl;

import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
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
	TransformPropertyGrid transformPropertyGrid;
	ConverterHelper converterHelper;

	public TransformComponentConverter(Component component) {
		super();
		this.baseComponent = new Transform();
		this.component = component;
		this.properties = component.getProperties();
		transformPropertyGrid = (TransformPropertyGrid) properties.get(Constants.PARAM_OPERATION);
		converterHelper = new ConverterHelper(component);
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
