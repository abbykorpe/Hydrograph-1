package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.InputCategory;

/**
 * The Sort class.
 * 
 * @author Bitwise
 */
public class SortComponent extends InputCategory {

	private static final long serialVersionUID = 5336234307203189573L;

	public SortComponent() {
		super();
	}
	
	public String getConverter(){
		return "com.bitwise.app.engine.converter.impl.SortConverter";
	}
}