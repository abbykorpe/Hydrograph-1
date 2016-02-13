package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.TransformCategory;

/**
 * Model class for Normalize component
 * 
 * @author Bitwise
 */
public class Normalize extends TransformCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7251671460714273114L;

	public String getConverter(){
		return "com.bitwise.app.engine.converter.impl.NormalizeConverter";
	}
}
