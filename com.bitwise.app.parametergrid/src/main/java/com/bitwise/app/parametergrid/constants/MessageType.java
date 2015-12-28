package com.bitwise.app.parametergrid.constants;

/**
 * 
 * 
 * Message type enum
 * 
 * @author Bitwise
 *
 */
public enum MessageType {
	ERROR("Error"),
	WARNING("Warning"),
	INFO("Info");
	
	private String messageType;
	private MessageType(String messageType){
		this.messageType = messageType;
	}
	
	/**
	 * 
	 * get message type
	 * 
	 * @return
	 */
	public String messageType(){
		return messageType;
	}
}
