package com.bitwise.app.engine.ui.constants;

public class UIComponentsPort {
	private static final String INPUT_PORT_TYPE = "in";
	private static final String OUTPUT_PORT_TYPE = "out";
	

	public static String getPortType(String PortType) {
		if (PortType.startsWith("in"))
			return INPUT_PORT_TYPE;
		if (PortType.startsWith("out"))
			return OUTPUT_PORT_TYPE;
		if (PortType.startsWith("unused"))
			return OUTPUT_PORT_TYPE;

		return null;

	}
}
