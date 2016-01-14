package com.bitwise.app.propertywindow.schmeapropagation;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;

public class SchemaPropagation {
	public static final SchemaPropagation INSTANCE = new SchemaPropagation();
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(SchemaPropagation.class);
	private ComponentsOutputSchema componentsOutputSchema;

	public void recursiveSchemaPropagation(Component component, ComponentsOutputSchema componentsOutputSchema) {
		LOGGER.debug("Initiating recursive schema propagation");
		if (component != null)
			applySchemaToTargetComponents(component, componentsOutputSchema);
	}

	private void applySchemaToTargetComponents(Component destinationComponent,
			ComponentsOutputSchema componentsOutputSchema) {
		LOGGER.debug("Applying Schema to :" + destinationComponent.getComponentLabel());
		destinationComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE, componentsOutputSchema);
		if (destinationComponent.getSourceConnections().isEmpty()) {
			return;
		}

		for (Link link : destinationComponent.getSourceConnections()) {
			if (!(link.getTarget().getCategory().equals("TRANSFORM") & !link.getTarget().getComponentName()
					.equals("Filter"))
					&& !link.getTarget().getProperties().containsValue(componentsOutputSchema))
				applySchemaToTargetComponents(link.getTarget(), componentsOutputSchema);
		}
	}

	public ComponentsOutputSchema getComponentsOutputSchema(Link link) {
		LOGGER.debug("Getting Source Output Schema for component.");
		this.componentsOutputSchema = null;
		getSourceSchemaForUnusedPorts(link);
		return this.componentsOutputSchema;
	}

	private void getSourceSchemaForUnusedPorts(Link link) {
		LOGGER.debug("Reverse propagation for fetching source schema for component.");
		String socketId = link.getSourceTerminal();
		if (!checkUnusedSocketAsSourceTerminal(link)) {
			this.componentsOutputSchema = (ComponentsOutputSchema) link.getSource().getProperties()
					.get(Constants.SCHEMA_TO_PROPAGATE);
			return;
		}
		for (Link link2 : link.getSource().getTargetConnections()) {
			if (link2.getTargetTerminal().equals(getInSocketForUnusedSocket(socketId))
					) {
				getSourceSchemaForUnusedPorts(link2);
			}
		}

	}

	private String getInSocketForUnusedSocket(String unusedSocketId) {
		String unusedPortNo = unusedSocketId.substring(6);
		String inSocket = "in" + unusedPortNo;

		return inSocket;
	}

	private boolean checkUnusedSocketAsSourceTerminal(Link link) {
		LOGGER.debug("Checking whether link is connected to unused port");
		if (link.getSource().getPort(link.getSourceTerminal()).getPortType().equals(Constants.UNUSED_SOCKET_TYPE))
			return true;
		return false;
	}
}
