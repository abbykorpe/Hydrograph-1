/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package hydrograph.ui.engine.ui.util;

import hydrograph.ui.common.util.CanvasDataAdpater;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.engine.exceptions.EngineException;
import hydrograph.ui.engine.parsing.XMLParser;
import hydrograph.ui.engine.ui.converter.LinkingData;
import hydrograph.ui.engine.ui.converter.UiConverter;
import hydrograph.ui.engine.ui.converter.UiConverterFactory;
import hydrograph.ui.engine.ui.exceptions.ComponentNotFoundException;
import hydrograph.ui.engine.ui.repository.ParameterData;
import hydrograph.ui.engine.ui.repository.UIComponentRepo;
import hydrograph.ui.engine.ui.xygenration.CoordinateProcessor;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.model.processor.DynamicClassProcessor;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.main.Graph;
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
	 * Load classes for all component classes. i.e. creates a logical palette into memory.
	 */
	public void loadClass() {
		LOGGER.debug("Loading class from component's XML");
		List<hydrograph.ui.common.component.config.Component> componentsConfig;
		try {
			componentsConfig = XMLConfigUtil.INSTANCE.getComponentConfig();

			for (hydrograph.ui.common.component.config.Component componentConfig : componentsConfig) {
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
		String inputFileAsString = replaceParametersWithDefaultValues(inputFile);
		
		jaxbContext = JAXBContext.newInstance(Graph.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		graph = (Graph) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(inputFileAsString.getBytes()));
		if (graph != null) {
			UIComponentRepo.INSTANCE.genrateComponentRepo(graph);
		}
		return graph;
	}

	private String replaceParametersWithDefaultValues(File inputFile) {
		String fileContentAsString = readFileContentInString(inputFile).toString();
		List<ParameterData> parameterDataList = new ArrayList<>();
		for (Entry<String, List<ParameterData>> entry : UIComponentRepo.INSTANCE.getParammeterFactory().entrySet()) {
			parameterDataList.addAll(entry.getValue());
		}
		for (ParameterData parameterData : parameterDataList) {
			String parameterName = parameterData.getParameterName();
			parameterName = parameterName.replaceAll("\\@\\{", "");
			parameterName = parameterName.replaceAll("\\}", "");
			fileContentAsString = fileContentAsString.replaceAll("value=\"\\@\\{" + parameterName + "\\}\"", "");
			fileContentAsString = fileContentAsString.replaceAll("\\@\\{" + parameterName + "\\}", "");
		}
		return fileContentAsString;
	}

	

	private StringBuilder readFileContentInString(File inputFile) {
		StringBuilder inputStringBuilder = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String sCurrentLine = null;
			while ((sCurrentLine = br.readLine()) != null) {
				inputStringBuilder.append(sCurrentLine);
			}
		} catch (IOException e) {
			LOGGER.error("Failed to read the xml file", e);
		}
		return inputStringBuilder;
	}

	private void parseXML(File inputFile) throws ParserConfigurationException, SAXException, IOException {
		XMLParser xmlParser = new XMLParser();
		xmlParser.parseXML(inputFile);
	}
	
	private void preProcessLinkData() {
		LOGGER.debug("Process links data for one to many port generation");
		boolean isMultiplePortAllowed;
		for (LinkingData linkingData : UIComponentRepo.INSTANCE.getComponentLinkList()) {
			LOGGER.debug("Process links data for one to many port generation : {}", linkingData);
			if (UIComponentRepo.INSTANCE.getComponentUiFactory().get(linkingData.getSourceComponentId()) != null) {
				/*isMultiplePortAllowed = UIComponentRepo.INSTANCE.getComponentUiFactory()
						.get(linkingData.getSourceComponentId()).getPortSpecification().get(0).getPort().get(0).isAllowMultipleLinks();*/
				isMultiplePortAllowed = UIComponentRepo.INSTANCE.getComponentUiFactory()
						.get(linkingData.getSourceComponentId()).getPortDetails().get(0).isAllowMultipleLinks();
				
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
