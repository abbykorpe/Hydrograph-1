package com.bitwise.app.engine.ui.constants;


public enum UIComponentsConstants {
	VALID("VALID"),
	FILE_DELIMITED("File Delimited"),
	VALIDITY_STATUS("validityStatus"),
	INPUT_CATEGORY("INPUT"),
	FILE_FIXEDWIDTH("File Fixed Width"),
	OUTPUT_CATEGORY("OUTPUT"),
	STRAIGHTPULL_CATEGORY("STRAIGHTPULL"),
	CLONE("Clone"), 
	UNION_ALL("Union All"), 
	REMOVE_DUPS("Remove Dups");
	
	private final String value;

	UIComponentsConstants(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static UIComponentsConstants fromValue(String value) {
		for (UIComponentsConstants uiComponentsConstant : UIComponentsConstants
				.values()) {
			if (uiComponentsConstant.value.equals(value)) {
				return uiComponentsConstant;
			}
		}
		return null;
	}
}
