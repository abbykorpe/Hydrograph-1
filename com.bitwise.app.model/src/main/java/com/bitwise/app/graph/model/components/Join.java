package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.TransformCategory;

/**
 * The Class Join.
 * 
 * @author Bitwise
 */

public class Join extends TransformCategory{
	

	private static final long serialVersionUID = 1L;

	@Override
	public String getConverter() {
		
		return "com.bitwise.app.engine.converter.impl.JoinConverter";
	}

}
