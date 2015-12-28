package com.bitwise.app.engine.exceptions;

public class DummyComponentException extends EngineException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2005154924124632290L;
	private static final String MSG_SUFFIX = "Dummy component present in graph. Target XML cannot be genrated";
	
	public DummyComponentException(String message, Throwable cause) {
		super(MSG_SUFFIX , cause);
	}
}
