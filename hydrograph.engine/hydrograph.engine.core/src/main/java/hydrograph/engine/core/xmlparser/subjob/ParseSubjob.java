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
package hydrograph.engine.core.xmlparser.subjob;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseSubjob {

	private static final String SUBJOB_OUTPUT = "subjobOutput";
	private static final String SUBJOB_INPUT = "subjobInput";
	private static final String ID = "id";
	private static final Object INSOCKET = "inSocket";
	private static final String FROMCOMPONENTID = "fromComponentId";
	private static final String FROMSOCKETID = "fromSocketId";
	private static final String TYPE = "xsi:type";
	private static final String SUBJOB_COMPONENTS = "operations|inputs|outputs|commands";
	private Document parentXmlDocument = null;
	private Document subjobXmlDocument = null;
	private String subjobName;

	public ParseSubjob(Document parentjobXml, Document subjobXml, String subjobName) {
		this.parentXmlDocument = parentjobXml;
		this.subjobXmlDocument = subjobXml;
		this.subjobName = subjobName;
	}

	public Document expandSubjob() {
		if (!parentXmlDocument.equals(subjobXmlDocument)) {
			Map<String, Map<String, String>> subjobMap = null;
			renameComponentIDsInSubjob(subjobXmlDocument, subjobName);
			subjobMap = getComponentToPortMappingFromSubjob(SUBJOB_OUTPUT);
			Map<String, Map<String, String>> parentjobMap = getComponentToPortMappingFromParentjob(subjobName);

			substituteComponentNamesInSubjob(subjobXmlDocument, parentjobMap, SUBJOB_INPUT);
			substituteComponentNamesInParentjob(parentXmlDocument, subjobMap, subjobName);

			removeSubjobCustomComponent(subjobXmlDocument, SUBJOB_INPUT);
			removeSubjobCustomComponent(subjobXmlDocument, SUBJOB_OUTPUT);
			removeSubjobComponentFromParentjob(parentXmlDocument, subjobName);

			mergeParentjobAndSubjob();
		}
		return parentXmlDocument;
	}

	private void mergeParentjobAndSubjob() {
		Node x = parentXmlDocument.importNode(subjobXmlDocument.getFirstChild(), true);
		while (x.hasChildNodes()) {
			parentXmlDocument.getFirstChild().appendChild(x.getFirstChild());
		}
	}

	private void renameComponentIDsInSubjob(Document subjobXmlDocument, String subjobName) {
		NodeList nodeList = subjobXmlDocument.getFirstChild().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).hasAttributes()) {
				Node componentNode = nodeList.item(i);
				String componentId = componentNode.getAttributes().getNamedItem(ID).getNodeValue();
				componentNode.getAttributes().getNamedItem(ID).setNodeValue(subjobName + "." + componentId);
				for (int j = 0; j < componentNode.getChildNodes().getLength(); j++) {
					if (componentNode.getChildNodes().item(j).hasAttributes()) {
						if (componentNode.getChildNodes().item(j).getNodeName().equals(INSOCKET)) {
							String fromComponentId = componentNode.getChildNodes().item(j).getAttributes()
									.getNamedItem(FROMCOMPONENTID).getNodeValue();
							componentNode.getChildNodes().item(j).getAttributes().getNamedItem(FROMCOMPONENTID)
									.setNodeValue(subjobName + "." + fromComponentId);
						}
					}
				}
			}
		}
	}

	private void removeSubjobCustomComponent(Document XmlDocument, String subjobType) {
		NodeList nodeList = XmlDocument.getFirstChild().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName().matches(SUBJOB_COMPONENTS)) {
				String componentType  = nodeList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue().split(":")[1];
				if (componentType.equals(subjobType)) {
					Node parent = nodeList.item(i).getParentNode();
					parent.removeChild(nodeList.item(i));
				}
			}
		}
	}

	private void removeSubjobComponentFromParentjob(Document XmlDocument, String subjobId) {
		NodeList nodeList = XmlDocument.getFirstChild().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName().matches(SUBJOB_COMPONENTS)) {
				String componentId = nodeList.item(i).getAttributes().getNamedItem(ID).getNodeValue();
				if (componentId.equals(subjobId)) {
					Node parent = nodeList.item(i).getParentNode();
					parent.removeChild(nodeList.item(i));
				}
			}
		}
	}

	private void substituteComponentNamesInParentjob(Document graphXml,
			Map<String, Map<String, String>> componentToPortMap, String subjobComponentId) {
		NodeList nodeList = graphXml.getFirstChild().getChildNodes();
		setComponentAndSocketId(nodeList, subjobComponentId, componentToPortMap);
	}

	private void substituteComponentNamesInSubjob(Document graphXml,
			Map<String, Map<String, String>> componentToPortMap, String componentType) {
		String componentId = null;
		NodeList nodeList = graphXml.getFirstChild().getChildNodes();

		// get id of subjob-input component
		for (int i = 0; i < nodeList.getLength(); i++) {

			if (nodeList.item(i).getAttributes() != null)
				if (nodeList.item(i).getAttributes().getNamedItem(TYPE) != null) {
					String type = nodeList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue().split(":")[1];
					if (type.equals(componentType))
						componentId = nodeList.item(i).getAttributes().getNamedItem(ID).getNodeValue();
				}
		}

		// replace subjob-input component id with one from componentToPortMap
		// in fromComponentId attribute
		setComponentAndSocketId(nodeList, componentId, componentToPortMap);
	}
	
	private void setComponentAndSocketId(NodeList nodeList, String subjobComponentId,
			Map<String, Map<String, String>> componentToPortMap) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodes = nodeList.item(i).getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) {
				if (childNodes.item(j) != null)
					if (childNodes.item(j).getNodeName().equals(INSOCKET)) {
						String fromComponentId = childNodes.item(j).getAttributes().getNamedItem(FROMCOMPONENTID)
								.getNodeValue();
						String fromSockettId = childNodes.item(j).getAttributes().getNamedItem(FROMSOCKETID)
								.getNodeValue();
						if (fromComponentId.equals(subjobComponentId)) {
							if (componentToPortMap.containsKey(fromSockettId)) {
								Map<String, String> portToComponentMap = componentToPortMap.get(fromSockettId);

								Element e = (Element) childNodes.item(j);
								for (String newFromSocketId : portToComponentMap.keySet()) {
									e.setAttribute(FROMSOCKETID, newFromSocketId);
									e.setAttribute(FROMCOMPONENTID, portToComponentMap.get(newFromSocketId));
								}

							}
						}
					}
			}
		}
	}

	private Map<String, Map<String, String>> getComponentToPortMappingFromSubjob(String componentType) {
		Map<String, Map<String, String>> socketToFromComponentMap = new HashMap<String, Map<String, String>>();

		NodeList nodeList = subjobXmlDocument.getFirstChild().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getAttributes() != null)
				if (nodeList.item(i).getAttributes().getNamedItem(TYPE) != null)
					if (nodeList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue().split(":")[1]
							.equals(componentType)) {
						NodeList childNodes = nodeList.item(i).getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							Map<String, String> fromComponentMap = new HashMap<String, String>();
							
							if (childNodes.item(j) != null)
								if (childNodes.item(j).getNodeName().equals(INSOCKET)) {
									String socketId = childNodes.item(j).getAttributes().getNamedItem(ID)
											.getNodeValue();

									String fromComponentId = childNodes.item(j).getAttributes()
											.getNamedItem(FROMCOMPONENTID).getNodeValue();
									String fromSockettId = childNodes.item(j).getAttributes().getNamedItem(FROMSOCKETID)
											.getNodeValue();
									
									fromComponentMap.put(fromSockettId, fromComponentId);
									socketToFromComponentMap.put(socketId, fromComponentMap);
								}
						}
					}
		}
		return socketToFromComponentMap;
	}

	private Map<String, Map<String, String>> getComponentToPortMappingFromParentjob(String componentName) {
		Map<String, Map<String, String>> socketToFromComponentMap = new HashMap<String, Map<String, String>>();
		
		NodeList nodeList = parentXmlDocument.getFirstChild().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodes = nodeList.item(i).getChildNodes();
			if (nodeList.item(i).getAttributes() != null)
				if (nodeList.item(i).getAttributes().getNamedItem(ID) != null)
					if (nodeList.item(i).getAttributes().getNamedItem(ID).getNodeValue().equals(componentName))
						for (int j = 0; j < childNodes.getLength(); j++) {
							
							Map<String, String> fromComponentMap = new HashMap<String, String>();
							
							if (childNodes.item(j) != null)
								if (childNodes.item(j).getNodeName().equals(INSOCKET)) {
									String socketId = childNodes.item(j).getAttributes().getNamedItem(ID).getNodeValue();
									String fromComponentId = childNodes.item(j).getAttributes()
											.getNamedItem(FROMCOMPONENTID).getNodeValue();
									String fromSockettId = childNodes.item(j).getAttributes().getNamedItem(FROMSOCKETID)
											.getNodeValue();
									fromComponentMap.put(fromSockettId, fromComponentId);
									socketToFromComponentMap.put(socketId, fromComponentMap);
								}
						}

		}
		return socketToFromComponentMap;
	}

}
