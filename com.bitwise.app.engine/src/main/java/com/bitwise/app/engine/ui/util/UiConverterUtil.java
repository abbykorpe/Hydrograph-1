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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;

import com.bitwise.app.common.util.CanvasDataAdpater;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.engine.exceptions.EngineException;
import com.bitwise.app.engine.parsing.XMLParser;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.UiConverter;
import com.bitwise.app.engine.ui.converter.UiConverterFactory;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.engine.ui.xygenration.CoordinateProcessor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.main.Graph;
import com.thoughtworks.xstream.XStream;

public class UiConverterUtil {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UiConverterUtil.class);
	public static final UiConverterUtil INSTANCE = new UiConverterUtil();
	private static final String FIXED_OUTPUT_PORT="out0";
	private static final String PARAMETER_FOLDER="param";

	public void convertToUiXML(File sourceXML,IFile jobFile,IFile parameterFile) throws InstantiationException,IllegalAccessException, IllegalArgumentException,InvocationTargetException, NoSuchMethodException,SecurityException, EngineException {
		LOGGER.debug("Creating Ui-Converter based on component");
		loadClass();
		Graph graph = unMarshall(sourceXML);
		Container container = new Container();
		container.setParameterFileName(parameterFile.getFullPath().lastSegment());
		container.setParameterFileDirectory(parameterFile.getPathVariableManager().getURIValue("PROJECT_LOC").getPath() + "/" + PARAMETER_FOLDER + "/");
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
		genrateUIXML(container,jobFile,parameterFile);
		
	}

	private void genrateUIXML(Container container,IFile jobFile, IFile parameterFile) {
		LOGGER.debug("Generating UI-XML");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String jobXmlData=null;
		try {
			XStream xs = new XStream();
			xs.autodetectAnnotations(true);
			jobXmlData=xs.toXML(container);
			storeParameterData(parameterFile,jobXmlData);
			jobFile.create(new ByteArrayInputStream(jobXmlData.getBytes()),true, null);
			
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating UI-XML");
		}
		finally{
			UIComponentRepo.INSTANCE.flusRepository();
			try {
				out.close();
			} catch (IOException ioe) {
				LOGGER.error("IOException occurred while closing output stream",ioe);
			}
		}
	}

	private Graph unMarshall(File inputFile) {
		LOGGER.debug("Un-Marshling generated object into target XML");
		JAXBContext jaxbContext;
		Graph graph = null;
		try {
			parseXML(inputFile);
			jaxbContext = JAXBContext.newInstance(Graph.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			graph = (Graph) jaxbUnmarshaller.unmarshal(inputFile);
			if (graph != null) {
				UIComponentRepo.INSTANCE.genrateComponentRepo(graph);
			}
		} catch (JAXBException e) {
			LOGGER.error("JAXBException occurred during un-marshal",e);
		}
		return graph;
	}

	private void parseXML(File inputFile) {
		XMLParser xmlParser=new XMLParser();
		xmlParser.parseXML(inputFile);
	}

	public void loadClass() {
		LOGGER.debug("Loading class from component's XML");
		List<com.bitwise.app.common.component.config.Component> componentsConfig;
		try {
			componentsConfig = XMLConfigUtil.INSTANCE.getComponentConfig();

			for (com.bitwise.app.common.component.config.Component componentConfig : componentsConfig) {
				DynamicClassProcessor.INSTANCE.createClass(componentConfig);

			}

		} catch (Exception e) {
			LOGGER.error("Error occurred while loading classes from xml: ",e);
		}

	}
	
	public void createLinks() {
		LOGGER.debug("Creating UI-Links between Components");
		preProcessLinkData();
		CoordinateProcessor pc=new CoordinateProcessor();
		pc.initiateCoordinateGenration();
		for(LinkingData linkingData:UIComponentRepo.INSTANCE.getComponentLinkList())
		{
			Component sourceComponent=UIComponentRepo.INSTANCE.getComponentUiFactory().get(linkingData.getSourceComponentId());
			Component targetComponent=UIComponentRepo.INSTANCE.getComponentUiFactory().get(linkingData.getTargetComponentId());
			Link link=new Link();
				link.setSourceTerminal(linkingData.getSourceTerminal());
				link.setTargetTerminal(linkingData.getTargetTerminal());
				link.setSource(sourceComponent);
				link.setTarget(targetComponent);
				sourceComponent.connectOutput(link);
				targetComponent.connectInput(link);
		}
		
	}

	private void preProcessLinkData() {
		LOGGER.debug("Process links data for one to many port generation");
		boolean isMultiplePortAllowed;
		for(LinkingData linkingData:UIComponentRepo.INSTANCE.getComponentLinkList()){
			isMultiplePortAllowed=UIComponentRepo.INSTANCE.getComponentUiFactory().get(linkingData.getSourceComponentId()).getPortSpecification().get(0).isAllowMultipleLinks();
			if (isMultiplePortAllowed) {
				linkingData.setSourceTerminal(FIXED_OUTPUT_PORT);
			}
		}
	}
	
	private void storeParameterData(IFile parameterFile,String jobXmlData){
		LOGGER.debug("Creating Parameter(i.e *properties) File at {}",parameterFile.getFullPath());
		CanvasDataAdpater canvasDataAdpater = new CanvasDataAdpater(jobXmlData);
		String defaultParameterValue="";
		Properties properties = new Properties(); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		for( String parameter:canvasDataAdpater.getParameterList())
		  { 
				  properties.setProperty(parameter,defaultParameterValue);
			}
		try {
			properties.store(out, null);
			parameterFile.create(new ByteArrayInputStream(out.toByteArray()),true, null);
		} catch (IOException |CoreException e) {
				LOGGER.error("Exception occurred while creating parameter file -",e);
			}
		finally{
		try {
			out.close();
		} catch (IOException e) {
			LOGGER.error("Exception occurred while closing parameter file's out stream -",e);
		}
		}
	}
}

