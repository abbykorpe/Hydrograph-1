package com.bitwise.app.engine.converter.impl;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.converter.Converter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commandtypes.Subgraph;

public class CommandSubgraphConverter extends Converter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputSubGraphConverter.class);
	private ConverterHelper converterHelper;
	private String JOBPATH="path";
	private String JOBEXTENSION=".job";
	private String XMLEXTENSION=".xml";
	public CommandSubgraphConverter(Component component) {
		super();
		this.baseComponent = new Subgraph();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Subgraph subgraph = (Subgraph) baseComponent;
		Subgraph.Path path = new Subgraph.Path();
		String[] temp;
		if(properties.get(JOBPATH)!=null){
		String subGraphFilePath=	((String)properties.get(JOBPATH)).replace(JOBEXTENSION, XMLEXTENSION);
		temp = subGraphFilePath.split("\\\\",3);
		path.setUri(temp[temp.length-1].replaceAll("\\\\", "/"));
		subgraph.setPath(path);
		}
		subgraph.setSubgraphParameter(getRuntimeProperties());
		
		
	}

	

}
