package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.DummyCategory;

public class DummyComponent extends DummyCategory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2406782326279531800L;

	@Override
	public String getConverter() {
		// TODO Auto-generated method stub
		return "com.bitwise.app.engine.converter.impl.DummyConverter";
	}

}
