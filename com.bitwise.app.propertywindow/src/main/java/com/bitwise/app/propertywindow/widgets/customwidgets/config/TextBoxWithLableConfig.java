package com.bitwise.app.propertywindow.widgets.customwidgets.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.bitwise.app.propertywindow.factory.ListenerFactory.Listners;

public class TextBoxWithLableConfig implements WidgetConfig {
	private String name;
	private List<Listners> listeners = new ArrayList<>();
	private boolean grabExcessSpace = false;
	private int widgetWidth=100;
	private Map<String, String> otherAttributes = new HashMap<String, String>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Listners> getListeners() {
		return listeners;
	}
	public void setListeners(List<Listners> listeners) {
		this.listeners = listeners;
	}
	public boolean getGrabExcessSpace() {
		return grabExcessSpace;
	}
	public void setGrabExcessSpace(boolean grabExcessSpace) {
		this.grabExcessSpace = grabExcessSpace;
	}
	public int getwidgetWidth(){
		return widgetWidth;
	}
	public void setWidgetWidth(int widgetWidth){
		this.widgetWidth = widgetWidth;
	}
	public Map<String,String> getOtherAttributes() {
		return otherAttributes;
	}
}
