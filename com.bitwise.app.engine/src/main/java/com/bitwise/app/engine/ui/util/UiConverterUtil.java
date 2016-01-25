package com.bitwise.app.engine.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.bitwise.app.common.util.CanvasDataAdpater;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.engine.exceptions.EngineException;
import com.bitwise.app.engine.parsing.XMLParser;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.UiConverter;
import com.bitwise.app.engine.ui.converter.UiConverterFactory;
import com.bitwise.app.engine.ui.exceptions.ComponentNotFoundException;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.engine.ui.xygenration.CoordinateProcessor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.main.Graph;
import com.thoughtworks.xstream.XStream;

/**
 * The class UiConverterUtil
 * 
 * @author Bitwise
 * 
 */
public class UiConverterUtil {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UiConverterUtil.class);

	private static final String FIXED_OUTPUT_PORT = "out0";
	private static final String FIXED_UNUSED_PORT = "unused0";

	public UiConverterUtil() {
		UIComponentRepo.INSTANCE.flusRepository();
	}

	/**
	 * Initiates the conversion process of source xml into graph
	 * 
	 * @param sourceXML
	 *            , XML file that will be executed in engine.
	 * @param jobFile
	 *            , Job file path
	 * @param parameterFile
	 *            , Parameter file path
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws EngineException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public void convertToUiXML(File sourceXML, IFile jobFile, IFile parameterFile) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, EngineException, JAXBException, ParserConfigurationException, SAXException, IOException,
			ComponentNotFoundException {
		LOGGER.debug("Creating UI-Converter based on component");
		loadClass();
		Graph graph = unMarshall(sourceXML);
		Container container = new Container();

		List<TypeBaseComponent> children = graph.getInputsOrOutputsOrStraightPulls();
		if (children != null && !children.isEmpty()) {
			for (TypeBaseComponent typeBaseComponent : children) {
				UiConverter uiConverter = UiConverterFactory.INSTANCE.getUiConverter(typeBaseComponent, container);
				uiConverter.prepareUIXML();
				Component component = uiConverter.getComponent();
				container.getChildren().add(component);
			}
			createLinks();
		}
		genrateUIXML(container, jobFile, parameterFile);

	}

	/**
	 * Creates the job file based for the container object.
	 * 
	 * @param container
	 * @param jobFile
	 * @param parameterFile
	 */
	private void genrateUIXML(Container container, IFile jobFile, IFile parameterFile) {
		LOGGER.debug("Generating UI-XML");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String jobXmlData = null;
		try {
			XStream xs = new XStream();
			xs.autodetectAnnotations(true);
			jobXmlData = xs.toXML(container);
			storeParameterData(parameterFile, jobXmlData);
			jobFile.create(new ByteArrayInputStream(jobXmlData.getBytes()), true, null);

		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating UI-XML", e);
		} finally {
			UIComponentRepo.INSTANCE.flusRepository();
			try {
				out.close();
			} catch (IOException ioe) {
				LOGGER.error("IOException occurred while closing output stream", ioe);
			}
		}
	}

	private Graph unMarshall(File inputFile) throws JAXBException, ParserConfigurationException, SAXException,
			IOException {
		LOGGER.debug("Un-Marshaling generated object into target XML");
		JAXBContext jaxbContext;
		Graph graph = null;

		parseXML(inputFile);
		jaxbContext = JAXBContext.newInstance(Graph.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		graph = (Graph) jaxbUnmarshaller.unmarshal(inputFile);
		if (graph != null) {
			UIComponentRepo.INSTANCE.genrateComponentRepo(graph);
		}

		return graph;
	}

	private void parseXML(File inputFile) throws ParserConfigurationException, SAXException, IOException {
		XMLParser xmlParser = new XMLParser();
		xmlParser.parseXML(inputFile);
	}

	/**
	 * Load classes for all component classes. i.e. creates a logical palette into memory.
	 */
	public void loadClass() {
		LOGGER.debug("Loading class from component's XML");
		List<com.bitwise.app.common.component.config.Component> componentsConfig;
		try {
			componentsConfig = XMLConfigUtil.INSTANCE.getComponentConfig();

			for (com.bitwise.app.common.component.config.Component componentConfig : componentsConfig) {
				DynamicClassProcessor.INSTANCE.createClass(componentConfig);

			}

		} catch (Exception e) {
			LOGGER.error("Error occurred while loading classes from xml: ", e);
		}

	}

	/**
	 * Create links between the components based on there
	 */
	public void createLinks() throws ComponentNotFoundException {
		LOGGER.debug("Creating UI-Links between Components");
		preProcessLinkData();
		CoordinateProcessor pc = new CoordinateProcessor();
		pc.initiateCoordinateGenration();
		for (LinkingData linkingData : UIComponentRepo.INSTANCE.getComponentLinkList()) {
			Component sourceComponent = UIComponentRepo.INSTANCE.getComponentUiFactory().get(
					linkingData.getSourceComponentId());
			Component targetComponent = UIComponentRepo.INSTANCE.getComponentUiFactory().get(
					linkingData.getTargetComponentId());

			Link link = new Link();
			link.setSourceTerminal(linkingData.getSourceTerminal());
			link.setTargetTerminal(linkingData.getTargetTerminal());
			link.setSource(sourceComponent);
			link.setTarget(targetComponent);
			sourceComponent.connectOutput(link);
			targetComponent.connectInput(link);
			LOGGER.debug("Links Details:" + linkingData);
		}

	}

	private void preProcessLinkData() {
		LOGGER.debug("Process links data for one to many port generation");
		boolean isMultiplePortAllowed;
		for (LinkingData linkingData : UIComponentRepo.INSTANCE.getComponentLinkList()) {
			LOGGER.debug("Process links data for one to many port generation : {}", linkingData);
			if (UIComponentRepo.INSTANCE.getComponentUiFactory().get(linkingData.getSourceComponentId()) != null) {
				isMultiplePortAllowed = UIComponentRepo.INSTANCE.getComponentUiFactory()
						.get(linkingData.getSourceComponentId()).getPortSpecification().get(0).getPort().get(0).isAllowMultipleLinks();
				
				if (isMultiplePortAllowed) {
					if (linkingData.getSourceTerminal().contains("out"))
						linkingData.setSourceTerminal(FIXED_OUTPUT_PORT);
					else if (linkingData.getSourceTerminal().contains("unused"))
						linkingData.setSourceTerminal(FIXED_UNUSED_PORT);
				}
			}
		}
	}

	private void storeParameterData(IFile parameterFile, String jobXmlData) {
		LOGGER.debug("Creating Parameter(i.e *properties) File at {}", parameterFile.getFullPath());
		CanvasDataAdpater canvasDataAdpater = new CanvasDataAdpater(jobXmlData);
		String defaultParameterValue = "";
		Properties properties = new Properties();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		for (String parameter : canvasDataAdpater.getParameterList()) {
			properties.setProperty(parameter, defaultParameterValue);
		}
		try {
			properties.store(out, null);
			parameterFile.create(new ByteArrayInputStream(out.toByteArray()), true, null);
		} catch (IOException | CoreException e) {
			LOGGER.error("Exception occurred while creating parameter file -", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				LOGGER.error("Exception occurred while closing parameter file's out stream -", e);
			}
		}
	}
}
