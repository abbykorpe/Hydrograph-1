package com.bitwise.app.engine.ui.converter.impl;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.UiConverter;
import com.bitwise.app.engine.ui.repository.InSocketDetail;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.DummyComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.main.Graph;
import com.thoughtworks.xstream.XStream;

public class DummyUiConverter extends UiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(DummyUiConverter.class);

	public DummyUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new DummyComponent();
		this.propertyMap = new LinkedHashMap<>();

	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Straight-Pull-Properties for -{}", componentName);

		LOGGER.info("Converting TYPEBASE TO XML STRING");
		propertyMap.put("xml_properties_content", marshall());
		if (getInPort() && getOutPort()) {
			container.getComponentNextNameSuffixes().put(name_suffix, 0);
			container.getComponentNames().add(componentName);
			uiComponent.setProperties(propertyMap);
			uiComponent.setCategory(Constants.UNKNOWN_COMPONENT_TYPE);
			uiComponent.setType(typeBaseComponent.getClass().getSimpleName());

		}
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(), "ERROR");

	}

	private boolean getOutPort() {
		LOGGER.debug("Fetching default-component Output port for -{}", componentName);
		uiComponent.engageOutputPort("out0");
		uiComponent.engageOutputPort("unused0");
		return true;

	}

	private boolean getInPort() {
		LOGGER.debug("Generating default-component inputport for -{}", componentName);

		String fixedInsocket = "in0";
		if (UIComponentRepo.INSTANCE.getInsocketMap().get(componentName) != null) {
			for (InSocketDetail inSocketDetail : UIComponentRepo.INSTANCE.getInsocketMap().get(componentName)) {
					uiComponent.engageInputPort(fixedInsocket);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocketDetail.getFromComponentId(), componentName, inSocketDetail
								.getFromSocketId(), fixedInsocket));

			}

		}
		return true;
	}

	@Override
	protected Map<String, String> getRuntimeProperties() {

		return null;
	}

	public String marshall() {
		String properties = null;
		ByteArrayOutputStream out = null;
		Graph graph = new Graph();
		graph.getInputsOrOutputsOrStraightPulls().add(typeBaseComponent);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(graph.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			out = new ByteArrayOutputStream();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(graph, out);
			properties = out.toString();
		} catch (JAXBException e) {

			LOGGER.error("ERROR OCCURED", e);
		}

		return properties;
	}
}
