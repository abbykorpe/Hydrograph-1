package com.bitwise.app.engine.ui.exceptions;


public class ComponentNotFoundException extends ImportXMLException {
	
	private static final long serialVersionUID = -7050865517559484599L;
	private static final String MSG_SUFFIX = " - no component found.";

	public ComponentNotFoundException(String message, Throwable cause) {
		super(message+MSG_SUFFIX, cause);
	}
}
