package com.bitwise.app.engine.ui.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.Converter;
import com.bitwise.app.engine.ui.repository.ParameterData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwiseglobal.graph.commontypes.BooleanValueType;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;

public abstract class UIConverter {
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(Converter.class);
	protected Container container;
	protected Component uiComponent = null;
	protected TypeBaseComponent typeBaseComponent=null;
	protected LinkedHashMap<String, Object> propertyMap=null;
	protected static final String PHASE = "phase";
	protected static final String NAME = "name";
	protected String COMPONENT_NAME;
	protected Link link=null;
	
	
	public void prepareUIXML() {
		COMPONENT_NAME=typeBaseComponent.getId();
		propertyMap.put(NAME,COMPONENT_NAME);
		propertyMap.put(PHASE,typeBaseComponent.getPhase().toString());
		uiComponent.setComponentLabel(COMPONENT_NAME);
		uiComponent.setParent(container);
		
		UIComponentRepo.INSTANCE.getComponentUiFactory().put(COMPONENT_NAME,uiComponent);
	}

	public Component getComponent() {
		
		return uiComponent;
	}
	public String convertBooleanVlaue(BooleanValueType value, String propertyName)
	{
		Object stringValue=null;

		if(value!=null && value.isValue()!=null && value.isValue().toString().equalsIgnoreCase("true"))
		{
			return "True";
		}
		else{  stringValue=getValue(propertyName);
			if(stringValue!=null);
			{
				return (String)stringValue;
			}
//			return stringValue;
		}
		
	}
	
	protected Object getValue(String propertyName) {
	
		List<ParameterData> parameterList=UIComponentRepo.INSTANCE.getParammeterFactory().get(COMPONENT_NAME);
		if(parameterList!=null)
		{
			for(ParameterData param:parameterList)
			{
				if(param.getPropertyName().equals(propertyName))
				{
					return param.getParameterName();
				}
			}
		}
		
		return null;	
	}
	
	protected boolean isParameter(String parameter) {
		String Regex="[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}";
		Matcher matchs = Pattern.compile(Regex).matcher(parameter);
		if(matchs.matches())
		{
			return true;
		}
		return false;
	}
	
	protected abstract TreeMap getRuntimeProperties();
	
	

}
