package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.TransformCategory;

/**
 * The Class Lookup.
 * 
 * @author Bitwise
 */

public class Lookup extends TransformCategory{

	@Override
	public String getConverter() {
		return "com.bitwise.app.engine.converter.impl.LookupConverter";
	}

}
