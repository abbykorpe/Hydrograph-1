package com.bitwise.app.engine.ui.converter;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;

/**
 * The class StraightfullUiConverter
 * 
 * @author Bitwise
 * 
 */

public abstract class StraightpullUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(StraightpullUiConverter.class);

	/**
	 * Generate common properties of straight-pull component.
	 * 
	 * @param
	 * 
	 * @return
	 */
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}", componentName);
		getInPort((TypeStraightPullComponent) typeBaseComponent);
		getOutPort((TypeStraightPullComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(), getRuntimeProperties());
	}

	/**
	 * Create input ports for straight-pull component.
	 * 
	 * @param TypeStraightPullComponent
	 *            the straightPullComponent
	 * @return
	 */
	protected void getInPort(TypeStraightPullComponent straightPullComponent) {
		LOGGER.debug("Generating InPut Ports for -{}", componentName);
		int portCounter = 1;
		if (straightPullComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : straightPullComponent.getInSocket()) {
				uiComponent.engageInputPort(getInputSocketType(inSocket) + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(), straightPullComponent.getId(),
								inSocket.getFromSocketId(), inSocket.getId()));
				portCounter++;
			}
		}
	}

	/**
	 * Create output ports for straight-pull component.
	 * 
	 * @param TypeStraightPullComponent
	 *            the straightPullComponent
	 * @return
	 */
	protected void getOutPort(TypeStraightPullComponent straightPullComponent) {
		LOGGER.debug("Generating OutPut Ports for -{}", componentName);
		int portCounter = 0;
		int unusedportCounter = 0;
		if (straightPullComponent.getOutSocket() != null) {
			for (TypeStraightPullOutSocket outSocket : straightPullComponent.getOutSocket()) {
				if (getOutputSocketType(outSocket).equals("unused"))
					uiComponent.engageOutputPort(getOutputSocketType(outSocket) + (++unusedportCounter));
				else
					uiComponent.engageOutputPort(getOutputSocketType(outSocket) + (++portCounter));
			}

		}
	}

	/**
	 * Generate runtime properties for straight-pull component.
	 * 
	 * @return Map<String,String>
	 */
	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
		TreeMap<String, String> runtimeMap = null;
		TypeProperties typeProperties = ((TypeStraightPullComponent) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}
}
