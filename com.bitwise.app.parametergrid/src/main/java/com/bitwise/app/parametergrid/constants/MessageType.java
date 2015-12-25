package com.bitwise.app.parametergrid.constants;

public enum MessageType {
	ERROR("Error"),
	WARNING("Warning"),
	INFO("Info");
	
	private String messageType;
	private MessageType(String messageType){
		this.messageType = messageType;
	}
	
	public String messageType(){
		return messageType;
	}
}
