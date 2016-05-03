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
package hydrograph.engine.core.xmlparser.externalschema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import hydrograph.engine.core.xmlparser.XmlParsingUtils;
import hydrograph.engine.core.xmlparser.parametersubstitution.ParameterSubstitutor;

public class ParseExternalSchema {

	private static Logger LOG = LoggerFactory
			.getLogger(ParseExternalSchema.class);

	private final String URI = "uri";
	private final String EXTERNAL_SCHEMA = "includeExternalSchema";
	private final String FIELD = "field";

	private DocumentBuilderFactory builderFactory = DocumentBuilderFactory
			.newInstance();
	private DocumentBuilder documentBuilder;
	private Document xmlDocument = null;
	private String xmlContent;
	private ParameterSubstitutor parameterSubstitutor;

	public ParseExternalSchema(ParameterSubstitutor parameterSubstitutor,
			String xmlContent) {
		this.xmlContent = parameterSubstitutor.substitute(xmlContent);
		this.parameterSubstitutor = parameterSubstitutor;
	}

	public Document getXmlDom() {
		return getExternalSchemaDocument(xmlContent);
	}

	private Document getExternalSchemaDocument(String xmlContents) {
		LOG.info("Parsing external schemas");
		builderFactory.setValidating(false);
		builderFactory.setNamespaceAware(true);
		try {
			documentBuilder = builderFactory.newDocumentBuilder();
			try {
				xmlDocument = documentBuilder.parse(new InputSource(
						new StringReader(xmlContents)));
			} catch (SAXException | IOException e) {
				LOG.error("", e);
			}
		} catch (ParserConfigurationException e) {
			LOG.error("", e);
		}
		NodeList externalNodes = xmlDocument
				.getElementsByTagName(EXTERNAL_SCHEMA);

		int externalNodesLen = externalNodes.getLength();
		for (int k = 0; k < externalNodesLen; k++) {
			Node parent = externalNodes.item(0).getParentNode();

			List<Node> nodes = createNodes(parent);
			int len = parent.getChildNodes().getLength();

			removeChileNodes(parent, len);

			addChildNodes(xmlDocument, parent, nodes);
		}
		return xmlDocument;

	}

	private void addChildNodes(Document xmlDocument, Node parent,
			List<Node> nodes) {
		Node copiedNode;
		for (int i = 0; i < nodes.size(); i++) {
			copiedNode = xmlDocument.importNode(nodes.get(i), true);
			parent.appendChild(copiedNode);
		}
	}

	private void removeChileNodes(Node parent, int len) {
		for (int i = 0; i < len; i++) {
			parent.removeChild(parent.getChildNodes().item(0));
		}
	}

	private List<Node> createNodes(Node parent) {
		List<Node> nodeList = new ArrayList<Node>();
		Document xmlDocument2 = null;
		try {
			documentBuilder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOG.error("", e);
		}
		for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
			if (parent.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (parent.getChildNodes().item(i).getNodeName().equals(FIELD))
					nodeList.add(parent.getChildNodes().item(i));
				else if(parent.getChildNodes().item(i).getNodeName().equals(EXTERNAL_SCHEMA)){
					String path = parent.getChildNodes().item(i)
							.getAttributes().getNamedItem(URI).getNodeValue();
					if (!new File(path).exists()) {
						try {
							throw new FileNotFoundException(
									"External schema file doesn't exist: "
											+ path);
						} catch (FileNotFoundException e) {
							LOG.error("", e);
						}
					}
					try {
						String xml = parameterSubstitutor
								.substitute(XmlParsingUtils
										.getXMLStringFromPath(path));
						xmlDocument2 = documentBuilder.parse(new InputSource(
								new StringReader(xml)));
					} catch (SAXException | IOException e) {
						LOG.error("", e);
					}
					nodeList.addAll(createNodes(xmlDocument2.getElementsByTagName("fields").item(0)));
				}
			}
		}
		return nodeList;
	}
}
