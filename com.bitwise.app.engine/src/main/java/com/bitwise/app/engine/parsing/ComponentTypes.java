package com.bitwise.app.engine.parsing;




public enum ComponentTypes {
	INPUTS("inputs"),
	OUTPUTS("outputs"),
	OPERATIONS("operations"),
	STRAIGHT_PULLS("straightPulls");
	
	private final String value;

	ComponentTypes(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static ComponentTypes fromValue(String value) {
		for (ComponentTypes componentTypes : ComponentTypes
				.values()) {
			if (componentTypes.value.equals(value)) {
				return componentTypes;
			}
		}
		return null;
	}
}
