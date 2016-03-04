package com.bitwise.app.engine.converter;

import java.util.List;

import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.transform.TypeTransformOutSocket;

public abstract class SubgraphConverter extends Converter {
	
	@Override
	public void prepareForXML(){
		super.prepareForXML();
		((TypeOperationsComponent) baseComponent).getInSocket().addAll(getInSocket());
		((TypeOperationsComponent)baseComponent).getOutSocket().addAll(getOutSocket());		
	}
	
	/**
	 * Returns {@link List} of classes of type {@link TypeTransformOutSocket}
	 * @return {@link List}
	 */
	protected abstract  List<TypeOperationsOutSocket> getOutSocket();
	
	/**
	 * Returns {@link List} of classes of type {@link TypeTransformOperation} 
	 * @return {@link List}
	 */
	protected abstract List<TypeTransformOperation> getOperations();
	public abstract List<TypeBaseInSocket> getInSocket();
}
