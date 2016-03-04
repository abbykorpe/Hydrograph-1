package com.bitwise.app.graph.model.components;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.categories.SubgraphCategory;

/**
 * Return sub graph component converter.
 * 
 * @author Bitwise
 * 
 */
public class SubgraphComponent extends SubgraphCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2406782326279531800L;

	@Override
	public String getConverter() {
		String type = (String) this.getProperties().get(Constants.TYPE);
		if (StringUtils.isNotBlank(type)) {
			if (type.equalsIgnoreCase(Constants.INPUT))
				return "com.bitwise.app.engine.converter.impl.InputSubGraphConverter";
			if (type.equalsIgnoreCase(Constants.OUTPUT))
				return "com.bitwise.app.engine.converter.impl.OutputSubGraphConverter";
			if (type.equalsIgnoreCase(Constants.OPERATION))
				return "com.bitwise.app.engine.converter.impl.OperationSubGraphConverter";
			if (type.equalsIgnoreCase(Constants.OUTPUT_SUBGRAPH))
				return "com.bitwise.app.engine.converter.impl.OutputComponentSubGraphConverter";
			if (type.equalsIgnoreCase(Constants.INPUT_SUBGRAPH))
				return "com.bitwise.app.engine.converter.impl.InputComponentSubGraphConverter";
		}
		return "com.bitwise.app.engine.converter.impl.InputSubGraphConverter";

	}

}
