/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.core.xmlparser;

import hydrograph.engine.core.core.HydrographDebugInfo;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.jaxb.debug.Debug;
import hydrograph.engine.jaxb.main.Graph;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class HydrographJobGenerator {
	JAXBContext context;
	Unmarshaller unmarshaller;
	Graph graph;
	Debug debug;
	private static Logger LOG = LoggerFactory.getLogger(HydrographJobGenerator.class);

	public HydrographJob createHydrographJob(String xmlContent) throws JAXBException {

		context = JAXBContext.newInstance(Graph.class);

		unmarshaller = context.createUnmarshaller();

		graph = (Graph) unmarshaller.unmarshal(new StringReader(xmlContent));

		HydrographJob hydrographJob = new HydrographJob(graph);

		return hydrographJob;

	}

	public HydrographDebugInfo createHydrographDebugInfo(String xmlContent) throws JAXBException {

		context = JAXBContext.newInstance(Debug.class);

		unmarshaller = context.createUnmarshaller();

		debug = (Debug) unmarshaller.unmarshal(new StringReader(xmlContent));

		HydrographDebugInfo hydrographDebugInfo = new HydrographDebugInfo(debug);

		return hydrographDebugInfo;

	}

	/**
	 * Creates the object of type {@link HydrographJob} from the graph xml of type
	 * {@link Document}.
	 * 
	 * The method uses jaxb framework to unmarshall the xml document
	 * 
	 * @param graphDocument
	 *            the xml document with all the graph contents to unmarshall
	 * @return an object of type "{@link HydrographJob}
	 * @throws SAXException
	 * @throws IOException
	 */
	public HydrographJob createHydrographJob(Document graphDocument, String xsdLocation) throws SAXException {
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(ClassLoader.getSystemResource(xsdLocation));
			LOG.trace("Creating HydrographJob object from jaxb");
			context = JAXBContext.newInstance(Graph.class);
			unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(schema);
			unmarshaller.setEventHandler(new ComponentValidationEventHandler());
			graph = (Graph) unmarshaller.unmarshal(graphDocument);
			HydrographJob hydrographJob = new HydrographJob(graph);
			LOG.trace("HydrographJob object created successfully");
			return hydrographJob;

		} catch (JAXBException e) {
			LOG.error("Error while creating JAXB objects from job XML.", e);
			throw new RuntimeException("Error while creating JAXB objects from job XML.", e);
		}
	}

	/**
	 * Creates the object of type {@link HydrographDebugInfo} from the graph xml of type
	 * {@link Document}.
	 * 
	 * The method uses jaxb framework to unmarshall the xml document
	 * 
	 * @param graphDocument
	 *            the xml document with all the graph contents to unmarshall
	 * @return an object of type "{@link HydrographDebugInfo}
	 * @throws SAXException
	 */
	public HydrographDebugInfo createHydrographDebugInfo(Document graphDocument, String debugXSDLocation) throws SAXException {
		try {
			LOG.trace("Creating DebugJAXB object.");
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(ClassLoader.getSystemResource(debugXSDLocation));
			context = JAXBContext.newInstance(Debug.class);
			unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(schema);
			unmarshaller.setEventHandler(new ComponentValidationEventHandler());
			debug = (Debug) unmarshaller.unmarshal(graphDocument);
			HydrographDebugInfo hydrographDebugInfo = new HydrographDebugInfo(debug);
			LOG.trace("DebugJAXB object created successfully");
			return hydrographDebugInfo;
		} catch (JAXBException e) {
			LOG.error("Error while creating JAXB objects from debug XML.", e);
			throw new RuntimeException("Error while creating JAXB objects from debug XML.", e);
		}
	}
}