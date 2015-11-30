package com.bitwise.app.engine.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.engine.exceptions.EngineException;
import com.bitwise.app.engine.parsing.XMLParser;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.UIConverter;
import com.bitwise.app.engine.ui.converter.UIConverterFactory;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.engine.ui.xygenration.Processor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.main.Graph;
import com.thoughtworks.xstream.XStream;

public class UIConverterUtil {
	private static final Logger LOGGER = LogFactory.INSTANCE
			.getLogger(UIConverterUtil.class);
	public static final UIConverterUtil INSTANCE = new UIConverterUtil();

	public void convertToUiXML(File InPutFile) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, EngineException {
		LOGGER.debug("Creating converter based on component");

		Graph graph = unMarshall(InPutFile);
		loadClass();
		Container container = new Container();
		List<TypeBaseComponent> children = graph.getInputsOrOutputsOrStraightPulls();
		if (children != null && !children.isEmpty()) {
			for (TypeBaseComponent typeBaseComponent : children) {
				UIConverter uiConverter = UIConverterFactory.INSTANCE.getUiConverter(typeBaseComponent, container);
				uiConverter.prepareUIXML();
				Component component = uiConverter.getComponent();
				container.getChildren().add(component);
			}
			createLink();
		}
		genrateUIXML(container);
	}

	private void genrateUIXML(Container container) {
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(
					"C:\\WorkSpace\\runtime-com.bitwise.app.perspective.product\\UI_VS_TARGET\\new.job");

			XStream xs = new XStream();

			xs.autodetectAnnotations(true);
			xs.toXML(container, fout);
			fout.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private Graph unMarshall(File outPutFile) {
		LOGGER.debug("UnMarshling genrated object into target XML");
		JAXBContext jaxbContext;
		Graph graph = null;
		try {
			parseXML(outPutFile);
			
			jaxbContext = JAXBContext.newInstance(Graph.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			graph = (Graph) jaxbUnmarshaller.unmarshal(outPutFile);
			if (graph != null) {
				UIComponentRepo.INSTANCE.genrateComponentRepo(graph);
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return graph;
	}

	private void parseXML(File outPutFile) {
		XMLParser obj=new XMLParser();
		obj.parseXML(outPutFile);
	}

	public void loadClass() {
		List<com.bitwise.app.common.component.config.Component> componentsConfig;
		try {
			componentsConfig = XMLConfigUtil.INSTANCE.getComponentConfig();

			for (com.bitwise.app.common.component.config.Component componentConfig : componentsConfig) {
				Class<?> clazz = DynamicClassProcessor.INSTANCE
						.createClass(componentConfig);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void createLink() {
		preProcessLinkData();
		Processor pc=new Processor();
		pc.processLinks();
				
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
				System.out.println(linkingData);
		}
		
	}

	private void preProcessLinkData() {
		boolean isMultiplePortAllowed;
		for(LinkingData linkingData:UIComponentRepo.INSTANCE.getComponentLinkList()){
			isMultiplePortAllowed=UIComponentRepo.INSTANCE.getComponentUiFactory().get(linkingData.getSourceComponentId()).getPortSpecification().get(0).isAllowMultipleLinks();
			if (isMultiplePortAllowed) {
				linkingData.setSourceTerminal("out0");
			}
		}
	}
}

