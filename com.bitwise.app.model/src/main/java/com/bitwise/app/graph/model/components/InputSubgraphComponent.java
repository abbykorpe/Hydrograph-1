package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.SubgraphCategory;
/**
 * Return sub graph component converter. 
 * @author Bitwise
 *
 */
public class InputSubgraphComponent extends SubgraphCategory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2406782326279531800L;
	

	@Override
	public String getConverter() {
			return "com.bitwise.app.engine.converter.impl.InputComponentSubGraphConverter";

	}

}
