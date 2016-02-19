package com.bitwise.app.common.constants.ports;
/**
 * 
 * Enum for common port ids
 * 
 * @author Bitwise
 *
 */
public enum PortConstants {
	IN_PORT0("IN0"),
	IN_PORT1("IN1");
	
	private String portID;
	
	private PortConstants(String portID){
		this.portID = portID;
	}
	
	/**
	 * returns port id
	 * 
	 * @return
	 */
	public String getPortID(){
		return portID;
	}
}
