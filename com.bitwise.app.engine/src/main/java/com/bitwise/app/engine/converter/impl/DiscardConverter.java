package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.OutputConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.otdiscard.TypeOutputInSocketIno;
import com.bitwiseglobal.graph.outputtypes.Discard;

/**
 * This class is used to create target XML for Discard component.
 * 
 * @author Jay Tripathi
 *
 */
public class DiscardConverter extends OutputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DiscardConverter.class);
	private ConverterHelper converterHelper;

	public DiscardConverter(Component component) {
		super();
		this.component = component;
		this.properties = component.getProperties();
		this.baseComponent = new Discard();
		converterHelper = new ConverterHelper(component);
	}

	/* *
	 * This method initiates target XML generation of Discard component.
	 * 
	 */
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML data for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Discard discard = (Discard) baseComponent;
	}

	@Override
	protected List<TypeOutputInSocket> getOutInSocket() {
		logger.debug("Generating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputInSocketIno outInSocket = new TypeOutputInSocketIno();
			outInSocket.setId(link.getTargetTerminal());
			if (converterHelper.isMultipleLinkAllowed(link.getSource(), link.getSourceTerminal()))
				outInSocket.setFromSocketId(link.getSource().getPort(link.getSourceTerminal()).getPortType()
						+ link.getLinkNumber());
			else
				outInSocket.setFromSocketId(link.getSourceTerminal());
			outInSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());

			outInSocket.getOtherAttributes();
			outInSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			outputinSockets.add(outInSocket);
		}
		return outputinSockets;
	}


	@Override
	protected List<TypeBaseField> getFieldOrRecord(ComponentsOutputSchema outputSchema) {
		logger.debug("Returning Null for getFieldOrRecord in DiscardConverter");

		return null;
	}
}
