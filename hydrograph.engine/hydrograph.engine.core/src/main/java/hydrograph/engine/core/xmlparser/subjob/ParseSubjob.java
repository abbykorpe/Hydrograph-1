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

import hydrograph.engine.core.utilities.XmlUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

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
	private static final String PHASE = "phase";
	private Document parentXmlDocument = null;
	private Document subjobXmlDocument = null;
	private String subjobName;
	private String subjobPhase;

	public ParseSubjob(Document parentjobXml, Document subjobXml,
			String subjobName, String subjobPhase) {
		this.parentXmlDocument = parentjobXml;
		this.subjobXmlDocument = subjobXml;
		this.subjobName = subjobName;
		this.subjobPhase = subjobPhase;
	}

	public Document expandSubjob() {
		if (!parentXmlDocument.equals(subjobXmlDocument)) {
			Map<String, Map<String, String>> subjobMap = null;
			renameComponentIDsInSubjob(subjobXmlDocument, subjobName);
			subjobMap = getComponentToPortMappingFromSubjob(SUBJOB_OUTPUT);
			Map<String, Map<String, String>> parentjobMap = getComponentToPortMappingFromParentjob(subjobName);

			substituteComponentNamesInSubjob(subjobXmlDocument, parentjobMap,
					SUBJOB_INPUT);
			substituteComponentNamesInParentjob(parentXmlDocument, subjobMap,
					subjobName);

			removeSubjobCustomComponent(subjobXmlDocument, SUBJOB_INPUT);
			removeSubjobCustomComponent(subjobXmlDocument, SUBJOB_OUTPUT);
			removeSubjobComponentFromParentjob(parentXmlDocument, subjobName);

			mergeParentjobAndSubjob();
			
			updatePhaseInExpandedXmlDocument();
		}
		return parentXmlDocument;
	}

	private void updatePhaseInExpandedXmlDocument() {
		// phaseLevel is the level of nesting of subjobs
		int phaseLevel = getPhaseLevel(XmlUtilities.getXMLStringFromDocument(parentXmlDocument));
		NodeList componentsWithPhaseAttribute = null;
		try {
			componentsWithPhaseAttribute = XmlUtilities
					.getComponentsWithAttribute(parentXmlDocument, PHASE);

			if (componentsWithPhaseAttribute != null
					&& componentsWithPhaseAttribute.getLength() > 0) {
				setPhaseOfComponentsUptoPhaselevel(phaseLevel,componentsWithPhaseAttribute);
				updatePhaseBasedOnSourceComponent(phaseLevel,componentsWithPhaseAttribute);
			} else {
				throw new RuntimeException("Component tag does not have '"+ PHASE +"' attribute.");
			}

		} catch (XPathExpressionException e) {
			// this exception will never be thrown as XPATH is evaluted on the hardcoded value i.e PHASE
			throw new RuntimeException(e);
		}
	}

	/**Updates phase of component based on its source component
	 * @param phaseLevel
	 * @param componentsWithPhaseAttribute
	 * @throws XPathExpressionException 
	 */
	private void updatePhaseBasedOnSourceComponent(int phaseLevel,
			NodeList componentsWithPhaseAttribute) throws XPathExpressionException {
		for (int componentWithPhase = 0; componentWithPhase < componentsWithPhaseAttribute
				.getLength(); componentWithPhase++) {
			String thisComponentPhase = componentsWithPhaseAttribute
					.item(componentWithPhase).getAttributes().getNamedItem(PHASE).getNodeValue();
			if (!isSubjobIOComponent(componentsWithPhaseAttribute
					.item(componentWithPhase).getAttributes().getNamedItem(TYPE).getNodeValue())) {
				NodeList childNodes = componentsWithPhaseAttribute.item(
						componentWithPhase).getChildNodes();
				for (int childNodeOfComponent = 0; childNodeOfComponent < childNodes
						.getLength(); childNodeOfComponent++) {
					if (childNodes.item(childNodeOfComponent).getNodeName().equals(INSOCKET)) {
						// gets source component from inSocket
						String fromComponentId = childNodes.item(childNodeOfComponent).getAttributes()
								.getNamedItem(FROMCOMPONENTID).getNodeValue();
						NodeList fromComponent = XmlUtilities
								.getComponentsWithAttribute(parentXmlDocument,"id=\"" + fromComponentId + "\"");
						if (!isSubjobIOComponent(fromComponent.item(0)
								.getAttributes().getNamedItem(TYPE).getNodeValue())) {
							// if source component is not of type subjobInput or subjobOutput
							// update phase of this component according to the phase of source component 
							if (fromComponent.item(0)
									.getAttributes().getNamedItem(PHASE) != null){
								String fromComponentPhase = fromComponent.item(0)
										.getAttributes().getNamedItem(PHASE).getNodeValue();
								for (int j = 0; j < phaseLevel; j++) {
									if (Integer.parseInt(thisComponentPhase
											.split("\\.")[j]) < Integer.parseInt(fromComponentPhase
													.split("\\.")[j])) {
										// if main phase is less than source component phase then throw error 
										// else update the subphase of this component with the subphase of source component
										if (j > 0) {
											componentsWithPhaseAttribute
											.item(componentWithPhase).getAttributes()
											.getNamedItem(PHASE).setNodeValue(fromComponentPhase);
										} else {
											throw new RuntimeException(
													"Phase of source component cannot be greater than target component. Source component '"
															+ fromComponentId
															+ "' has phase "
															+ fromComponentPhase.split("\\.")[0]
																	+ " and target component '"
																	+ componentsWithPhaseAttribute
																	.item(componentWithPhase)
																	.getAttributes()
																	.getNamedItem(ID)
																	.getNodeValue()
																	+ "' has phase "
																	+ thisComponentPhase.split("\\.")[0]);
										}
									}
								}
							} else {
								throw new RuntimeException(
										"Phase attribute is not present for '"
												+ fromComponent.item(0)
														.getAttributes()
														.getNamedItem(TYPE)
														.getNodeValue()
														.split(":")[1]
												+ "' component with Id '"
												+ fromComponentId + "'");
							}
						}
					}
				}
			}
		}
	}

	/**updates phase of all the components in the expanded job by appending .0 to phase till phaseLevel.
	 * @param phaseLevel
	 * @param componentsWithPhaseAttribute
	 */
	private void setPhaseOfComponentsUptoPhaselevel(int phaseLevel,
			NodeList componentsWithPhaseAttribute) {
		for (int i = 0; i < componentsWithPhaseAttribute.getLength(); i++) {
			String phase = componentsWithPhaseAttribute.item(i).getAttributes()
					.getNamedItem(PHASE).getNodeValue();
			if (phase.split("\\.").length < phaseLevel) {
				for (int j = 0; j < phaseLevel; j++) {
					phase = componentsWithPhaseAttribute.item(i)
							.getAttributes().getNamedItem(PHASE).getNodeValue();
					if (phase.split("\\.").length < phaseLevel) {
						componentsWithPhaseAttribute.item(i).getAttributes()
								.getNamedItem(PHASE).setNodeValue(phase + ".0");
					}
				}
			}
		}
	}

	/**Returns true if component is of type subjobInput or subjobOutput   
	 * @param nodeValue
	 * @return true if component type is subjobInput or subjobOutput
	 */
	private boolean isSubjobIOComponent(String nodeValue) {
		return nodeValue.split(":")[1].matches(SUBJOB_INPUT + "|"
				+ SUBJOB_OUTPUT);
	}

	private int getPhaseLevel(String xmlAsString) {
		Matcher m = Pattern.compile("phase\\s*=\\s*\"(.*?)\"", Pattern.DOTALL)
				.matcher(xmlAsString);
		int length = 0;
		while (m.find()) {
			if (m.group(1).split("\\.").length > length) {
				length = m.group(1).split("\\.").length;
			}
		}
		return length;
	}

	private void mergeParentjobAndSubjob() {
		Node x = parentXmlDocument.importNode(
				subjobXmlDocument.getFirstChild(), true);
		while (x.hasChildNodes()) {
			parentXmlDocument.getFirstChild().appendChild(x.getFirstChild());
		}
	}

	private void renameComponentIDsInSubjob(Document subjobXmlDocument,
			String subjobName) {
		NodeList nodeList = subjobXmlDocument.getFirstChild().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).hasAttributes()) {
				Node componentNode = updateComponentIdAndPhase(nodeList.item(i));
				updateInSocket(componentNode);
			}
		}
	}

	private void updateInSocket(Node componentNode) {
		for (int j = 0; j < componentNode.getChildNodes().getLength(); j++) {
			if (componentNode.getChildNodes().item(j).hasAttributes()) {
				if (componentNode.getChildNodes().item(j).getNodeName()
						.equals(INSOCKET)) {
					String fromComponentId = componentNode.getChildNodes()
							.item(j).getAttributes()
							.getNamedItem(FROMCOMPONENTID).getNodeValue();
					componentNode.getChildNodes().item(j).getAttributes()
							.getNamedItem(FROMCOMPONENTID)
							.setNodeValue(subjobName + "." + fromComponentId);
				}
			}
		}
	}

	private Node updateComponentIdAndPhase(Node componentNode) {
		String componentId = componentNode.getAttributes().getNamedItem(ID)
				.getNodeValue();
		componentNode.getAttributes().getNamedItem(ID)
				.setNodeValue(subjobName + "." + componentId);
		if (componentId.equals("reformat")){
			System.out.println(componentId);
		}
		if (!isSubjobIOComponent(componentNode.getAttributes()
				.getNamedItem(TYPE).getNodeValue())) {
			if (componentNode.getAttributes().getNamedItem(PHASE) == null){
				throw new RuntimeException("Phase attribute is not present for '" 
						+ componentNode.getAttributes()
						.getNamedItem(TYPE).getNodeValue().split(":")[1] + "' component with Id '"
						+ componentNode.getAttributes().getNamedItem(ID).getNodeValue() +"'");
			} else {
				String componentPhase = this.subjobPhase.split("\\.")[0]
						+ "."
						+ componentNode.getAttributes().getNamedItem(PHASE)
						.getNodeValue();
				componentNode.getAttributes().getNamedItem(PHASE)
				.setNodeValue(componentPhase);
			}

					
			
		}
		return componentNode;
	}

	private void removeSubjobCustomComponent(Document XmlDocument,
			String subjobType) {
		NodeList nodeList = XmlDocument.getFirstChild().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName().matches(SUBJOB_COMPONENTS)) {
				String componentType = nodeList.item(i).getAttributes()
						.getNamedItem(TYPE).getNodeValue().split(":")[1];
				if (componentType.equals(subjobType)) {
					Node parent = nodeList.item(i).getParentNode();
					parent.removeChild(nodeList.item(i));
				}
			}
		}
	}

	private void removeSubjobComponentFromParentjob(Document XmlDocument,
			String subjobId) {
		NodeList nodeList = XmlDocument.getFirstChild().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName().matches(SUBJOB_COMPONENTS)) {
				String componentId = nodeList.item(i).getAttributes()
						.getNamedItem(ID).getNodeValue();
				if (componentId.equals(subjobId)) {
					Node parent = nodeList.item(i).getParentNode();
					parent.removeChild(nodeList.item(i));
				}
			}
		}
	}

	private void substituteComponentNamesInParentjob(Document graphXml,
			Map<String, Map<String, String>> componentToPortMap,
			String subjobComponentId) {
		NodeList nodeList = graphXml.getFirstChild().getChildNodes();
		setComponentAndSocketId(nodeList, subjobComponentId, componentToPortMap);
	}

	private void substituteComponentNamesInSubjob(Document graphXml,
			Map<String, Map<String, String>> componentToPortMap,
			String componentType) {
		String componentId = null;
		NodeList nodeList = graphXml.getFirstChild().getChildNodes();

		// get id of subjob-input component
		for (int i = 0; i < nodeList.getLength(); i++) {

			if (nodeList.item(i).getAttributes() != null)
				if (nodeList.item(i).getAttributes().getNamedItem(TYPE) != null) {
					String type = nodeList.item(i).getAttributes()
							.getNamedItem(TYPE).getNodeValue().split(":")[1];
					if (type.equals(componentType))
						componentId = nodeList.item(i).getAttributes()
								.getNamedItem(ID).getNodeValue();
				}
		}

		// replace subjob-input component id with one from componentToPortMap
		// in fromComponentId attribute
		setComponentAndSocketId(nodeList, componentId, componentToPortMap);
	}

	private void setComponentAndSocketId(NodeList nodeList,
			String subjobComponentId,
			Map<String, Map<String, String>> componentToPortMap) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodes = nodeList.item(i).getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) {
				if (childNodes.item(j) != null) {
					if (childNodes.item(j).getNodeName().equals(INSOCKET)) {
						String fromComponentId = childNodes.item(j)
								.getAttributes().getNamedItem(FROMCOMPONENTID)
								.getNodeValue();
						String fromSockettId = childNodes.item(j)
								.getAttributes().getNamedItem(FROMSOCKETID)
								.getNodeValue();
						if (fromComponentId.equals(subjobComponentId)) {
							if (componentToPortMap.containsKey(fromSockettId)) {
								Map<String, String> portToComponentMap = componentToPortMap
										.get(fromSockettId);

								Element e = (Element) childNodes.item(j);
								for (String newFromSocketId : portToComponentMap
										.keySet()) {
									e.setAttribute(FROMSOCKETID,
											newFromSocketId);
									e.setAttribute(FROMCOMPONENTID,
											portToComponentMap
													.get(newFromSocketId));
								}

							}
						}
					}
				}
			}
		}
	}

	private Map<String, Map<String, String>> getComponentToPortMappingFromSubjob(
			String componentType) {
		Map<String, Map<String, String>> socketToFromComponentMap = new HashMap<String, Map<String, String>>();

		NodeList nodeList = subjobXmlDocument.getFirstChild().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getAttributes() != null)
				if (nodeList.item(i).getAttributes().getNamedItem(TYPE) != null)
					if (nodeList.item(i).getAttributes().getNamedItem(TYPE)
							.getNodeValue().split(":")[1].equals(componentType)) {
						NodeList childNodes = nodeList.item(i).getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							Map<String, String> fromComponentMap = new HashMap<String, String>();

							if (childNodes.item(j) != null)
								if (childNodes.item(j).getNodeName()
										.equals(INSOCKET)) {
									String socketId = childNodes.item(j)
											.getAttributes().getNamedItem(ID)
											.getNodeValue();

									String fromComponentId = childNodes.item(j)
											.getAttributes()
											.getNamedItem(FROMCOMPONENTID)
											.getNodeValue();
									String fromSockettId = childNodes.item(j)
											.getAttributes()
											.getNamedItem(FROMSOCKETID)
											.getNodeValue();

									fromComponentMap.put(fromSockettId,
											fromComponentId);
									socketToFromComponentMap.put(socketId,
											fromComponentMap);
								}
						}
					}
		}
		return socketToFromComponentMap;
	}

	private Map<String, Map<String, String>> getComponentToPortMappingFromParentjob(
			String componentName) {
		Map<String, Map<String, String>> socketToFromComponentMap = new HashMap<String, Map<String, String>>();

		NodeList nodeList = parentXmlDocument.getFirstChild().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodes = nodeList.item(i).getChildNodes();
			if (nodeList.item(i).getAttributes() != null)
				if (nodeList.item(i).getAttributes().getNamedItem(ID) != null)
					if (nodeList.item(i).getAttributes().getNamedItem(ID)
							.getNodeValue().equals(componentName))
						for (int j = 0; j < childNodes.getLength(); j++) {

							Map<String, String> fromComponentMap = new HashMap<String, String>();

							if (childNodes.item(j) != null)
								if (childNodes.item(j).getNodeName()
										.equals(INSOCKET)) {
									String socketId = childNodes.item(j)
											.getAttributes().getNamedItem(ID)
											.getNodeValue();
									String fromComponentId = childNodes.item(j)
											.getAttributes()
											.getNamedItem(FROMCOMPONENTID)
											.getNodeValue();
									String fromSockettId = childNodes.item(j)
											.getAttributes()
											.getNamedItem(FROMSOCKETID)
											.getNodeValue();
									fromComponentMap.put(fromSockettId,
											fromComponentId);
									socketToFromComponentMap.put(socketId,
											fromComponentMap);
								}
						}
		}
		return socketToFromComponentMap;
	}
}
