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

 
package hydrograph.ui.engine.util;

import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.Converter;
import hydrograph.ui.engine.converter.ConverterFactory;
import hydrograph.ui.engine.xpath.ComponentXpath;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.main.Graph;
import hydrograph.engine.jaxb.main.ObjectFactory;



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
			graph.setRuntimeProperties(getRuntimeProperties(container));
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

	private TypeProperties getRuntimeProperties(Container container) {
		TypeProperties typeProperties = null;
		Map<String, String> runtimeProps = container.getGraphRuntimeProperties();
		if (runtimeProps != null && !runtimeProps.isEmpty()) {
			typeProperties = new TypeProperties();
			List<TypeProperties.Property> runtimePropertyList = typeProperties.getProperty();
			for (Map.Entry<String, String> entry : runtimeProps.entrySet()) {
				Property runtimeProperty = new Property();
				runtimeProperty.setName(entry.getKey());
				runtimeProperty.setValue(entry.getValue());
				runtimePropertyList.add(runtimeProperty);
			}
		}
		return typeProperties;
	}
	
}

