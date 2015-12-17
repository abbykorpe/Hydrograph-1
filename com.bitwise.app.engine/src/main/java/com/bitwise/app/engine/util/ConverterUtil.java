package com.bitwise.app.engine.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.core.resources.IFile;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.Converter;
import com.bitwise.app.engine.converter.ConverterFactory;
import com.bitwise.app.engine.xpath.ComponentXpath;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.main.Graph;
import com.bitwiseglobal.graph.main.ObjectFactory;



public class ConverterUtil {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(ConverterUtil.class);
	public static final ConverterUtil INSTANCE = new ConverterUtil();
	private static final String GRAPH_NAME="Graph_1";
	private ConverterUtil(){
		
	}
	
	public void convertToXML(Container container, boolean validate, IFile outPutFile) throws Exception{
		LOGGER.debug("Creating converter based on component");
		
			Graph graph = new ObjectFactory().createGraph();
			graph.setName(GRAPH_NAME);
			List<Component> children = container.getChildren();
			if(children != null && !children.isEmpty()){
				for (Component component : children) {
					Converter converter = ConverterFactory.INSTANCE.getConverter(component); 
					converter.prepareForXML();
					TypeBaseComponent typeBaseComponent = converter.getComponent();
					graph.getInputsOrOutputsOrStraightPulls().add(typeBaseComponent);
				}
			}
			
			marshall(graph, validate,outPutFile);
		
		
	}
	
	
	private void marshall(Graph graph, boolean validate,IFile outPutFile) {
		LOGGER.debug("Marshling genrated object into target XML");
		ByteArrayOutputStream out = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(graph.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			out = new ByteArrayOutputStream();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(graph, out);
		 	 
			out = ComponentXpath.INSTANCE.addParameters(out);
			if (outPutFile.exists())
				outPutFile.setContents(new ByteArrayInputStream(out.toByteArray()), true,false, null);
			else
				outPutFile.create(new ByteArrayInputStream(out.toByteArray()),true, null);
			out.close();
			
		} catch (Exception exception) {
			LOGGER.error("Failed in marshall", exception);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
				LOGGER.error("ERROR WHILE CLOSING OUT STREAM OF TARGETXML"+e);
				}
			}
		}
	}

}

