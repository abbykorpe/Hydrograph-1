package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.OutputCategory;

/**
 * The model class for Discard
 * 
 * @author Jay Tripathi
 */
public class Discard extends OutputCategory{
	
	/**
	 * Instantiates a new Discard Component.
	 */
	public Discard() {
		super();
	}

	public String getConverter() {
		return "com.bitwise.app.engine.converter.impl.DiscardConverter";

	}

}
