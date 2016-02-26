package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.InputCategory;

/**
 * The Limit class.
 * 
 * @author Bitwise
 */
public class LimitComponent extends InputCategory {

	private static final long serialVersionUID = -6038215992214041586L;

	public LimitComponent() {
		super();
	}
	
	public String getConverter(){
		return "com.bitwise.app.engine.converter.impl.LimitConverter";
	}
}