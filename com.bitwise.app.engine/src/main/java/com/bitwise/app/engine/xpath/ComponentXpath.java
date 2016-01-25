package com.bitwise.app.engine.xpath;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.engine.util.ConverterUtil;

public class ComponentXpath {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(ConverterUtil.class);
	public static final ComponentXpath INSTANCE = new ComponentXpath();
	private Map<String, ComponentsAttributeAndValue> xpathMap;
	private Document doc;

	public Map<String, ComponentsAttributeAndValue> getXpathMap() {
		if (xpathMap == null) {
			xpathMap = new HashMap<String, ComponentsAttributeAndValue>();
		}
		return xpathMap;
	}

	public ByteArrayOutputStream addParameters(ByteArrayOutputStream out) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
		try {
			XPath xPath = createXPathInstance(inputStream, null);
			LOGGER.debug("GENRATED COMPONENTS XPATH {}", getXpathMap().toString());
			for (Map.Entry<String, ComponentsAttributeAndValue> entry : getXpathMap().entrySet()) {
				NodeList nodeList = (NodeList) xPath.compile(entry.getKey()).evaluate(doc, XPathConstants.NODESET);

				for (int i = 0; i < nodeList.getLength(); i++) {
					Node nNode = nodeList.item(i);

					if (Node.ELEMENT_NODE == nNode.getNodeType()) {
						Element eElement = (Element) nNode;
						eElement.setAttribute(entry.getValue().getAttributeName(), entry.getValue().getAttributeValue());
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			out.reset();

			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
			getXpathMap().clear();

		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException
				| TransformerException e) {
			LOGGER.error("Exception occurred while parametrizing the XML", e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException ioe) {
				LOGGER.error("Exception occurred while closing input stream", ioe);
			}
		}

		return out;
	}

	public XPath createXPathInstance(ByteArrayInputStream inputStream, File file) throws ParserConfigurationException,
			SAXException, IOException {
		LOGGER.debug("Invoking X-Path instance");
		XPath xPath = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		if (inputStream != null)
			doc = dBuilder.parse(inputStream);
		else if (file != null) {
			doc = dBuilder.parse(file);
		}
		doc.getDocumentElement().normalize();
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		xPath = XPathFactory.newInstance().newXPath();
		return xPath;
	}

}
