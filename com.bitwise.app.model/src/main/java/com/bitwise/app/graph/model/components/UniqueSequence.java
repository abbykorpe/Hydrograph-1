package com.bitwise.app.graph.model.components;

import com.bitwise.app.graph.model.categories.TransformCategory;

/**
 * Model class for UniqueSequence component
 * 
 * @author Bitwise
 */
public class UniqueSequence extends TransformCategory {


	private static final long serialVersionUID = -7202649891322732435L;


	public String getConverter(){
		return "com.bitwise.app.engine.converter.impl.UniqueSequenceConverter";
	}
}
