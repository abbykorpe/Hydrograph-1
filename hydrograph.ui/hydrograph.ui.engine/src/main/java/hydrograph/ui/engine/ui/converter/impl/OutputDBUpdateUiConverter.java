package hydrograph.ui.engine.ui.converter.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.ui.engine.ui.converter.OutputUiConverter;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.OOracle;
import hydrograph.ui.logging.factory.LogFactory;

public class OutputDBUpdateUiConverter extends OutputUiConverter{
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputDBUpdateUiConverter.class);
	
	public OutputDBUpdateUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OOracle(); 
		this.propertyMap = new LinkedHashMap<>();
	}
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
	}

	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, String> getRuntimeProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void setValueInPropertyMap(String propertyName,Object value){
		propertyMap.put(propertyName, getParameterValue(propertyName,value));
	}

}
