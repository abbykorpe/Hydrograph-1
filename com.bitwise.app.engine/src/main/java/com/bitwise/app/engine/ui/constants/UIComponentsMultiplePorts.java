package com.bitwise.app.engine.ui.constants;

public enum UIComponentsMultiplePorts {
	CLONE("Clone");
	private final String value;

	UIComponentsMultiplePorts(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static UIComponentsMultiplePorts fromValue(String value) {
		for (UIComponentsMultiplePorts componentsMultiplePorts : UIComponentsMultiplePorts.values()) {
			if (componentsMultiplePorts.value.equals(value)) {
				return componentsMultiplePorts;
			}
		}
		return null;
	}
}