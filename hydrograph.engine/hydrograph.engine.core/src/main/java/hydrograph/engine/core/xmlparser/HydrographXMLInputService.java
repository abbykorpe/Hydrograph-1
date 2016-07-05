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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import hydrograph.engine.core.commandlineparser.CLIParser;
import hydrograph.engine.core.core.HydrographDebugInfo;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.core.core.HydrographInputService;
import hydrograph.engine.core.props.PropertiesLoader;
import hydrograph.engine.core.utilities.XmlUtilities;
import hydrograph.engine.core.xmlparser.externalschema.ParseExternalSchema;
import hydrograph.engine.core.xmlparser.parametersubstitution.ParameterSubstitutor;
import hydrograph.engine.core.xmlparser.parametersubstitution.UserParameters;
import hydrograph.engine.core.xmlparser.subjob.ReadSubjob;

public class HydrographXMLInputService implements HydrographInputService {

	HydrographJobGenerator hydrographJobGenerator;
	PropertiesLoader propertiesLoader;
	Properties config;
	private static Logger LOG = LoggerFactory
			.getLogger(HydrographXMLInputService.class);

	public HydrographXMLInputService() {
		hydrographJobGenerator = new HydrographJobGenerator();
	}

	public HydrographJob parseParameters(String[] args) throws JAXBException, ParseException {
		return parseHydrographJob(PropertiesLoader.getInstance()
				.getRuntimeServiceProperties(), args);
	}

	@Override
	public HydrographJob parseHydrographJob(Properties config, String[] args)
			throws JAXBException, ParseException {
		HydrographJob hydrographJob = null;
		this.config = config;
		String path = CLIParser.getXmlPath(args, config);
		LOG.info("Parsing for graph file: " + path + " started");
		ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor(
				getUserParameters(args));
		
		try {
			ParseExternalSchema parseExternalSchema = new ParseExternalSchema(
					checkSubjobAndExpandXml(parameterSubstitutor,
							XmlParsingUtils.getXMLStringFromPath(path)));
			hydrographJob = hydrographJobGenerator.createHydrographJob(
					parseExternalSchema.getXmlDom(),
					config.getProperty("xsdLocation"));

		} catch (FileNotFoundException e) {
			LOG.error("Error while merging subjob and mainjob.", e);
			throw new RuntimeException("Error while merging subjob and mainjob.", e);
		} catch (SAXException e) {
			LOG.error("Error while parsing XSD.", e);
			throw new RuntimeException("Error while parsing XSD.", e);
		}
			LOG.info("Graph: '" + hydrographJob.getJAXBObject().getName()
					+ "' parsed successfully");
			return hydrographJob;
	}

	@Override
	public HydrographDebugInfo parseHydrographDebugInfo(Properties config, String[] args)
			throws JAXBException, ParseException {
		HydrographDebugInfo hydrographDebugInfo = null;
		this.config = config;
		String path = CLIParser.getDebugXmlPath(args, config);
		if (path != null) {
			
			if(CLIParser.getJobId(args)==null)
				throw new HydrographXMLInputServiceException("job id is required for Debugging");
			if(CLIParser.getBasePath(args)==null)
				throw new HydrographXMLInputServiceException("base path is required for Debugging");
			LOG.info("Parsing for Debug graph file: " + path + " started");
			ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor(
					getUserParameters(args));
			Document debugXmlDoc = XmlUtilities
					.getXMLDocument(parameterSubstitutor
							.substitute(XmlParsingUtils
									.getXMLStringFromPath(path)));
			try {
				hydrographDebugInfo = hydrographJobGenerator
						.createHydrographDebugInfo(debugXmlDoc,
								config.getProperty("debugXSDLocation"));
			} catch (SAXException e) {
				LOG.error("Error while parsing debug XSD.", e);
				throw new RuntimeException("Error while parsing debug XSD.", e);
			}
			LOG.info("Debug graph parsed successfully");
			return hydrographDebugInfo;
		} else {
			return null;
		}
	}

	

	
	
	private String checkSubjobAndExpandXml(
			ParameterSubstitutor parameterSubstitutor, String xmlContents) throws FileNotFoundException {

		LOG.info("Expanding subjobs");
		ReadSubjob subjobParser = new ReadSubjob(
				parameterSubstitutor.substitute(xmlContents));

		Document expandedXmlDocument = subjobParser.getSubjobDom();
		return XmlUtilities.getXMLStringFromDocument(expandedXmlDocument);
	}

	private UserParameters getUserParameters(String[] args) throws ParseException {
		try {
			return new UserParameters(args);
		} catch (IOException e) {
			throw new HydrographXMLInputServiceException(e);
		}
	}

	public class HydrographXMLInputServiceException extends RuntimeException {
		private static final long serialVersionUID = -7709930763943833311L;

		public HydrographXMLInputServiceException(String msg) {
			super(msg);
		}

		public HydrographXMLInputServiceException(Throwable e) {
			super(e);
		}
	}

}