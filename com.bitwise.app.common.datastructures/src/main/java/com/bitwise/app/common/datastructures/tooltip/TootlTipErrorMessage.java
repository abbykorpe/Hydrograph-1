package com.bitwise.app.common.datastructures.tooltip;

public class TootlTipErrorMessage {
	String errorMessage;
	
	public TootlTipErrorMessage() {
		errorMessage = "";
	}

	public String getErrorMessage(){
		return errorMessage;
	}
	public void setErrorMessage(String message){
		errorMessage = message;
	}
}
