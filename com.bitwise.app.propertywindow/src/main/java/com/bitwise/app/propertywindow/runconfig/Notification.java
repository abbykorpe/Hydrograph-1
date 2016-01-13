package com.bitwise.app.propertywindow.runconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class Notification {
	private List<String> errors = new ArrayList<>();

	public void addError(String message) { 
		errors.add(message); 
	}
	
	public boolean hasErrors() {
		return ! errors.isEmpty();
	}
	
	public String errorMessage() {
	    return StringUtils.join(errors,",");
	  }
}