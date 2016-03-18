package com.bitwise.app.engine.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
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
	private ConverterUtil(){
		
	}
	
	public void convertToXML(Container container, boolean validate, IFile outPutFile,  IFileStore externalOutputFile) throws Exception{
		LOGGER.debug("Creating converter based on component");
			Graph graph = new ObjectFactory().createGraph();
			graph.setName(getGraphName(outPutFile,externalOutputFile));
			List<Component> children = container.getChildren();
			if(children != null && !children.isEmpty()){
				for (Component component : children) {
					Converter converter = ConverterFactory.INSTANCE.getConverter(component); 
					converter.prepareForXML();
					TypeBaseComponent typeBaseComponent = converter.getComponent();
					graph.getInputsOrOutputsOrStraightPulls().add(typeBaseComponent);
				}
			}
			marshall(graph, validate,outPutFile,externalOutputFile);
	}
	
	
	private String getGraphName(IFile outPutFile, IFileStore externalOutputFile) {
		if (outPutFile != null && StringUtils.isNotBlank(outPutFile.getName()))
			return outPutFile.getName();
		else if (externalOutputFile != null && StringUtils.isNotBlank(externalOutputFile.getName()))
			return externalOutputFile.getName();
		return null;
	}

	private void marshall(Graph graph, boolean validate,IFile outPutFile, IFileStore externalOutputFile) {
		LOGGER.debug("Marshaling generated object into target XML");
		ByteArrayOutputStream out = null;
		try {
			 if (outPutFile!=null)
				 storeFileIntoWorkspace(graph,outPutFile,out);
			else if(externalOutputFile!=null)
				storeFileIntoLocalFileSystem(graph,externalOutputFile,out);
			
			
		} catch (Exception exception) {
			LOGGER.error("Failed in marshal", exception);
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

	private void storeFileIntoLocalFileSystem(Graph graph, IFileStore externalOutputFile, ByteArrayOutputStream out) throws CoreException, JAXBException, IOException {
		File externalFile=externalOutputFile.toLocalFile(0, null);
		OutputStream outputStream = new FileOutputStream (externalFile.getAbsolutePath().replace(".job", ".xml")); 
		JAXBContext jaxbContext = JAXBContext.newInstance(graph.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		out = new ByteArrayOutputStream();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(graph, out);
		out = ComponentXpath.INSTANCE.addParameters(out);
			out.writeTo(outputStream);
			outputStream.close();
		
	}

	private void storeFileIntoWorkspace(Graph graph, IFile outPutFile, ByteArrayOutputStream out) throws JAXBException, CoreException {
		
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
		
	}

}

