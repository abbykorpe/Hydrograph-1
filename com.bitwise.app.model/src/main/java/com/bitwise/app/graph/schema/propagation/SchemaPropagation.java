package com.bitwise.app.graph.schema.propagation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;

/**
 * This class is used to propagate schema.
 * 
 * @author Bitwise
 * 
 */
public class SchemaPropagation {
	public static final SchemaPropagation INSTANCE = new SchemaPropagation();
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(SchemaPropagation.class);
	private ComponentsOutputSchema componentsOutputSchema;
	private List<Link> componentsLinkList = new ArrayList<>();
	private List<Link> mainLinkList = new ArrayList<>();

	/**
	 * This method propagates component's schema to its successor components.
	 * 
	 * @param component
	 * @param componentsOutputSchema
	 */
	public void continuousSchemaPropagation(Component component, ComponentsOutputSchema componentsOutputSchema) {
		LOGGER.debug("Initiating recursive schema propagation");
		if (component != null)
			applySchemaToTargetComponents(component, componentsOutputSchema);
		flushLinkLists();
	}

	private void flushLinkLists() {
		mainLinkList.clear();
		componentsLinkList.clear();
	}

	private void applySchemaToTargetComponents(Component destinationComponent,
			ComponentsOutputSchema componentsOutputSchema) {
		LOGGER.debug("Applying Schema to :" + destinationComponent.getComponentLabel());
		destinationComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE, componentsOutputSchema);
		if (destinationComponent.getSourceConnections().isEmpty()) {
			mainLinkList.clear();
			return;
		}

		for (Link link : destinationComponent.getSourceConnections()) {
			if ((!(link.getTarget().getCategory().equals("TRANSFORM") & !link.getTarget().getComponentName()
					.equals("Filter")) && !link.getTarget().getProperties().containsValue(componentsOutputSchema)))
				if (!checkUnusedSocketAsSourceTerminal(link))
					applySchemaToTargetComponents(link.getTarget(), componentsOutputSchema);
				else {
					getComponentsOutputSchema(link);
					applySchemaToTargetComponents(link.getTarget(), this.componentsOutputSchema);
				}
			else {
				for (Link link2 : link.getTarget().getSourceConnections()) {
					if (checkUnusedSocketAsSourceTerminal(link2) && isMainLinkChecked(link2)
							&& getComponentsOutputSchema(link2) != null) {
						applySchemaToTargetComponents(link2.getTarget(), this.componentsOutputSchema);
					}
				}
			}
		}
	}

	/**
	 * This method retrieves schema from source component
	 * 
	 * @param link
	 * @return ComponentsOutputSchema, the componentsOutputSchema is output schema of component.
	 */
	public ComponentsOutputSchema getComponentsOutputSchema(Link link) {
		LOGGER.debug("Getting Source Output Schema for component.");
		this.componentsOutputSchema = null;
		getSourceSchemaForUnusedPorts(link);
		flushLinkLists();
		return this.componentsOutputSchema;
	}

	private void getSourceSchemaForUnusedPorts(Link link) {
		LOGGER.debug("Reverse propagation for fetching source schema for component.");
		String socketId = link.getSourceTerminal();
		if (isLinkChecked(link))
			return;
		if (!checkUnusedSocketAsSourceTerminal(link)) {
			this.componentsOutputSchema = (ComponentsOutputSchema) link.getSource().getProperties()
					.get(Constants.SCHEMA_TO_PROPAGATE);
			return;
		}
		for (Link link2 : link.getSource().getTargetConnections()) {
			if (link2.getTargetTerminal().equals(getInSocketForUnusedSocket(socketId))) {
				getSourceSchemaForUnusedPorts(link2);
			}
		}

	}

	private boolean isLinkChecked(Link link) {
		if (componentsLinkList.contains(link)) {
			componentsLinkList.clear();
			return true;
		}
		componentsLinkList.add(link);
		return false;
	}

	private boolean isMainLinkChecked(Link link) {
		if (mainLinkList.contains(link)) {
			mainLinkList.clear();
			return true;
		}
		mainLinkList.add(link);
		return false;
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
