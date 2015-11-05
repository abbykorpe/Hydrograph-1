package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.OutputCategory;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputFixedWidth.
 * 
 * @author Bitwise
 */
public class OFixedWidth extends OutputCategory {

	/**
	 * Instantiates a new output fixed width.
	 */
	public OFixedWidth() {
		super();
	}

	public String getConverter() {
		return "com.bitwise.app.engine.converter.impl.OutputFileFixedWidthConverter";

	}
}