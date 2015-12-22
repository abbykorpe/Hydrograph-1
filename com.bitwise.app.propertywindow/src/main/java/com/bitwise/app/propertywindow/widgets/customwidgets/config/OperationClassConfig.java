package com.bitwise.app.propertywindow.widgets.customwidgets.config;

public class OperationClassConfig implements WidgetConfig {
	String componentName;
	String componentDisplayName;
	
	public String getComponentDisplayName() {
		return componentDisplayName;
	}
	public void setComponentDisplayName(String componentDisplayName) {
		this.componentDisplayName = componentDisplayName;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
}
