package com.bitwise.app.engine.exceptions;

public class SchemaException extends Exception {
	static String messagesuffix = " component is having invalid Schema data ";

	public SchemaException(String id) {
		super("\n"+id + messagesuffix);
	}

}
