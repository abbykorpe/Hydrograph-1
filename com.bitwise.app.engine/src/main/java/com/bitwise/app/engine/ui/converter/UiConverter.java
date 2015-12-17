package com.bitwise.app.engine.ui.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.repository.ParameterData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.BooleanValueType;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;

public abstract class UiConverter {
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UiConverter.class);
	protected Container container;
	protected Component uiComponent = null;
	protected TypeBaseComponent typeBaseComponent=null;
	protected LinkedHashMap<String, Object> propertyMap=null;
	protected static final String PHASE = "phase";
	protected static final String NAME = "name";
	protected String COMPONENT_NAME;
	
	
	
	public void prepareUIXML() {
		COMPONENT_NAME=typeBaseComponent.getId();
		LOGGER.debug("Preparing basic properties for component {}",COMPONENT_NAME);		
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
		LOGGER.debug("Converting Boolean to String - {}",propertyName);		
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
		}
		
	}
	
	protected Object getValue(String propertyName) {
		LOGGER.debug("Getting Parameter for - {}",propertyName);	
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
		if(parameter!=null){
			String Regex="[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}";
			Matcher matchs = Pattern.compile(Regex).matcher(parameter);
			if(matchs.matches())
			{
				return true;
			}
		}
		return false;
	}
	
	protected abstract Map<String,String> getRuntimeProperties();
	
	protected String getStringValue(String value)
	{ String emptyString="";
		if(value==null || value.equals("null"))
			return emptyString;
		else
			return value;
	}

}
