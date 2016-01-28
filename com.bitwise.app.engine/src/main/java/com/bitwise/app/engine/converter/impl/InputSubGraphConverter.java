package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.InputConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;
import com.bitwiseglobal.graph.inputtypes.Subgraph;
import com.bitwiseglobal.graph.itfd.TypeInputDelimitedOutSocket;

public class InputSubGraphConverter extends InputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputSubGraphConverter.class);
	private ConverterHelper converterHelper;

	public InputSubGraphConverter(Component component) {
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
		path.setUri((String)properties.get("path"));
		subgraph.setPath(path);
		subgraph.setRuntimeProperties(getRuntimeProperties());
		
		
	}

	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputOutSocket outSocket = new TypeInputOutSocket();
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.setSchema(getSchema());
			outSocket.getOtherAttributes();
			outSockets.add(outSocket);
		}
		return outSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> gridList) {
		logger.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();

		if (gridList != null && gridList.size() != 0) {
			for (GridRow object : gridList)
				typeBaseFields.add(converterHelper.getFixedWidthTargetData((FixedWidthGridRow) object));
		}
		return typeBaseFields;
	}


}
