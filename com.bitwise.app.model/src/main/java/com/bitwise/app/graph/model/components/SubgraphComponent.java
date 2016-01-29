package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.SubgraphCategory;

public class SubgraphComponent extends SubgraphCategory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2406782326279531800L;
	

	@Override
	public String getConverter() {
		String type =(String) this.getProperties().get("type");
		if(type.equalsIgnoreCase("input"))
		return "com.bitwise.app.engine.converter.impl.InputSubGraphConverter";
		if(type.equalsIgnoreCase("output"))
		return "com.bitwise.app.engine.converter.impl.OutputSubGraphConverter";
		if(type.equalsIgnoreCase("operation"))
		return "com.bitwise.app.engine.converter.impl.OperationSubGraphConverter";
		if(type.equalsIgnoreCase("outputsubgraph"))
		return "com.bitwise.app.engine.converter.impl.OutputComponentSubGraphConverter";	
		if(type.equalsIgnoreCase("inputsubgraph"))
		return "com.bitwise.app.engine.converter.impl.InputComponentSubGraphConverter";

		return "com.bitwise.app.engine.converter.impl.InputSubGraphConverter";

	}

}
