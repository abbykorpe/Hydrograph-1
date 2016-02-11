package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.InputCategory;

// TODO: Auto-generated Javadoc
/**
 * The Class IFDelimited.
 * 
 * @author Bitwise
 */
public class GenerateRecords extends InputCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7890131953714494139L;

	/**
	 * Instantiates a new IF delimited.
	 */
	public GenerateRecords() {
	super();
	}
	
	public String getConverter()
	{
		return "com.bitwise.app.engine.converter.impl.GenerateRecordsConverter";
		
	}
}
