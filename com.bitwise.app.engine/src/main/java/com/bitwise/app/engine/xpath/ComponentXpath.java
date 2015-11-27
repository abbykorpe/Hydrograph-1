package com.bitwise.app.engine.xpath;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.util.ConverterUtil;

public class ComponentXpath {

	private static final String VALUE = "value";
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(ConverterUtil.class);
	public static final ComponentXpath INSTANCE = new ComponentXpath();
	private Map<String, String> xpathMap;

	public Map<String, String> getXpathMap() {
		if (xpathMap == null) {
			xpathMap = new HashMap<String, String>();
		}
		return xpathMap;
	}

	public ByteArrayOutputStream addParameters(ByteArrayOutputStream out) {
		Document doc=null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			doc = dBuilder.parse(new ByteArrayInputStream(out.toByteArray()));
			doc.getDocumentElement().normalize();
			XPath xPath = XPathFactory.newInstance().newXPath();
			LOGGER.debug("GENRATED COMPONENTS XPATH {}", getXpathMap().toString());
			for(Map.Entry<String, String> entry: getXpathMap().entrySet()){
				NodeList nodeList = (NodeList) xPath.compile(entry.getKey()).evaluate(doc, XPathConstants.NODESET);
			
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node nNode = nodeList.item(i);
					
					if (Node.ELEMENT_NODE == nNode.getNodeType()) {
						Element eElement = (Element) nNode;
						eElement.setAttribute(VALUE, entry.getValue());
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
		} catch (ParserConfigurationException |SAXException|IOException|XPathExpressionException|TransformerException e) {
			LOGGER.error("Exception occurred while parametrizing the XML", e);
		}
		
		return out;
	}

}
