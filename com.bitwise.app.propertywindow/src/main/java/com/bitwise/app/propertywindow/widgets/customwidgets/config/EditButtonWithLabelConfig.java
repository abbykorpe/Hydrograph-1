package com.bitwise.app.propertywindow.widgets.customwidgets.config;


/**
 * Configuration for class for Widget having Button and label 
 * @author BITWISE
 */
public class EditButtonWithLabelConfig implements WidgetConfig {
	private String name;
	private String windowName;
	private String headerName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWindowName() {
		return windowName;
	}
	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}
	
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	public String getHeaderName() {
		return headerName;
	}
}