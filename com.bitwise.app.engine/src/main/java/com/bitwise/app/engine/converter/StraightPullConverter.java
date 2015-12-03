package com.bitwise.app.engine.converter;

import java.util.List;

import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;

/**
 * Converter implementation for StraightPull type components
 */
public abstract class StraightPullConverter extends Converter {

	@Override
	public void prepareForXML(){
		super.prepareForXML();
		((TypeStraightPullComponent) baseComponent).setRuntimeProperties(getRuntimeProperties());
		((TypeStraightPullComponent) baseComponent).getInSocket().addAll(getInSocket());
		((TypeStraightPullComponent) baseComponent).getOutSocket().addAll(getOutSocket());
	}

	/**
	 *  Returns {@link List} of classes of type {@link TypeStraightPullOutSocket}
	 * @return {@link List}
	 */
	protected abstract List<TypeStraightPullOutSocket> getOutSocket();
	public abstract List<TypeBaseInSocket> getInSocket();


}
