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

package hydrograph.ui.graph.model;

import hydrograph.ui.common.cloneableinterface.IDataStructure;
import hydrograph.ui.common.component.config.PortInfo;
import hydrograph.ui.common.component.config.PortSpecification;
import hydrograph.ui.common.component.config.Property;
import hydrograph.ui.common.datastructures.tooltip.PropertyToolTipInformation;
import hydrograph.ui.common.util.ComponentCacheUtil;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.JoinConfigProperty;
import hydrograph.ui.graph.model.components.InputSubjobComponent;
import hydrograph.ui.graph.model.components.OutputSubjobComponent;
import hydrograph.ui.graph.model.processor.DynamicClassProcessor;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.validators.impl.IValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.slf4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * The Class Component.
 * 
 * @author Bitwise
 */
public abstract class Component extends Model {
	private static final Logger logger = LogFactory.INSTANCE
			.getLogger(Component.class);

	private static final long serialVersionUID = 2587870876576884352L;
	
	private final String UniqueComponentName = "Component" + Math.random() * 10000;
	
	/**
	 * The Enum Props.
	 * 
	 * @author Bitwise
	 */
	public static enum Props {
		NAME_PROP("name"), LOCATION_PROP("Location"), SIZE_PROP("Size"), INPUTS(
				"inputs"), OUTPUTS("outputs"), VALIDITY_STATUS("validityStatus");

		private String value;

		private Props(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		/**
		 * Eq.
		 * 
		 * @param property
		 *            the property
		 * @return true, if successful
		 */
		public boolean eq(String property) {
			return this.value.equals(property);
		}
	}

	/**
	 * The Enum ValidityStatus.
	 * 
	 * @author Bitwise
	 */
	public static enum ValidityStatus {
		WARN, ERROR, VALID;
	}

	private final Point location;
	private final Dimension size;
	private Map<String, Object> properties;
	private Container parent;
	private String validityStatus;

	private Map<String, Object> clonedHashMap;
	
	private ArrayList<JoinConfigProperty>  clonedArrayList;
	private final Hashtable<String, ArrayList<Link>> inputLinksHash;
	private final Hashtable<String, ArrayList<Link>> outputLinksHash;
	private ArrayList<Link> inputLinks = new ArrayList<Link>();
	private ArrayList<Link> outputLinks = new ArrayList<Link>();
	private  List<String> inputportTerminals;
	private  List<String> outputPortTerminals;
	private boolean newInstance;
	private String type;
	private String prefix;
	private String category;
	private HashMap<String, Port> ports;
	private String componentName;
	private String acronym;
	private List<PortSpecification> portSpecification;

	private int inPortCount;
	private int outPortCount;
	private int unusedPortCount;

	private boolean changeInPortsCntDynamically;
	private boolean changeOutPortsCntDynamically;
	private boolean changeUnusedPortsCntDynamically;

	private ComponentLabel componentLabel;
	private int componentLabelMargin;
	 
	private Map<String, Long> watcherTerminals;

	@XStreamOmitField
	private Map<String, PropertyToolTipInformation> tooltipInformation;

	// @XStreamOmitField
	private Map<String, String> toolTipErrorMessages; // <propertyName,ErrorMessage>

	@XStreamOmitField
	private Object componentEditPart;
	
	/**
	 * Instantiates a new component.
	 */
	public Component() {
		location = new Point(0, 0);
		size = new Dimension(100, 80);
		properties = new LinkedHashMap<>();
		inputLinksHash = new Hashtable<String, ArrayList<Link>>();

		inputLinks = new ArrayList<Link>();
		outputLinksHash = new Hashtable<String, ArrayList<Link>>();
		outputLinks = new ArrayList<Link>();
		inputportTerminals = new ArrayList<String>();
		outputPortTerminals = new ArrayList<String>();
		watcherTerminals = new HashMap();
		newInstance = true;
		validityStatus = ValidityStatus.WARN.name();
		componentName = DynamicClassProcessor.INSTANCE.getClazzName(this
				.getClass());

		componentLabel = new ComponentLabel(componentName);
		componentLabelMargin = 16;

		prefix = XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getDefaultNamePrefix();
		acronym = XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getDefaultNamePrefix();
		initPortSettings();
		toolTipErrorMessages = new LinkedHashMap<>();
	}

	/**
	 * 
	 * Returns tooltip error message
	 * 
	 * @return {@link Map}
	 */
	public Map<String, String> getToolTipErrorMessages() {
		return toolTipErrorMessages;
	}
	
	/**
	 *	
	 *	Set Tooltip error message 
	 * 
	 * @param toolTipErrorMessages
	 */
	public void setToolTipErrorMessages(Map<String, String> toolTipErrorMessages) {
		this.toolTipErrorMessages = toolTipErrorMessages;
	}
   	
	private void initPortSettings() {

		portSpecification = XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getPort().getPortSpecification();

		ports = new HashMap<String, Port>();

		for (PortSpecification p : portSpecification) {
			setPortCount(p.getTypeOfPort().value(), p.getNumberOfPorts(), p.isChangePortCountDynamically());
			for(PortInfo portInfo :p.getPort()){
				String portTerminal = p.getTypeOfPort().value() + portInfo.getSequenceOfPort();
				Port port = new Port(portInfo.getNameOfPort(), portInfo.getLabelOfPort(),
						portTerminal, this, p.getNumberOfPorts(), p.getTypeOfPort()
								.value(), portInfo.getSequenceOfPort());
				ports.put(portTerminal, port);
			}
		}
	}

	private void setPortCount(String portType, int portCount, boolean changePortCount) {
		if (portType.equals(Constants.INPUT_SOCKET_TYPE)) {
			inPortCount = portCount;
			properties.put("inPortCount", String.valueOf(portCount));
			changeInPortsCntDynamically=changePortCount;
		} else if (portType.equals(Constants.OUTPUT_SOCKET_TYPE)) {
			outPortCount = portCount;
			properties.put("outPortCount", String.valueOf(portCount));
			changeOutPortsCntDynamically=changePortCount;
		} else if (portType.equals(Constants.UNUSED_SOCKET_TYPE)) {
			unusedPortCount = portCount;
			properties.put("unusedPortCount", String.valueOf(portCount));
			changeUnusedPortsCntDynamically=changePortCount;
		}
	}

	/**
	 * 
	 * Returns list of input ports
	 * 
	 * @return
	 */
	public List<String> getInputportTerminals() {
		return inputportTerminals;
	}
	
	/**
	 * 
	 * Set input port names
	 * 
	 * @param portTerminals
	 */
	public void setInputportTerminals(List<String> portTerminals){
		this.inputportTerminals=portTerminals;
	}

	/**
	 * 
	 * Returns list of output port
	 * 
	 * @return
	 */
	public List<String> getOutputPortTerminals() {
		return outputPortTerminals;
	}
	
	/**
	 * 
	 * Set output ports
	 * 
	 * @param portTerminals
	 */
	public void setOutputPortTerminals(List<String> portTerminals) {
		this.outputPortTerminals=portTerminals;
	}
	
	/**
	 * 
	 * Set true when number of input port count changed dynamically
	 * 
	 */
	public void setChangeInPortsCntDynamically(boolean changeInPortsCntDynamically) {
		this.changeInPortsCntDynamically = changeInPortsCntDynamically;
	}

	/**
	 * 
	 * Set true when number of output port count changed dynamically
	 * 
	 */	
	public void setChangeOutPortsCntDynamically(boolean changeOutPortsCntDynamically) {
		this.changeOutPortsCntDynamically = changeOutPortsCntDynamically;
	}

	/**
	 * 
	 * Set true when number of unused port count changed
	 * 
	 * @param changeUnusedPortsCntDynamically
	 */
	public void setChangeUnusedPortsCntDynamically(
			boolean changeUnusedPortsCntDynamically) {
		this.changeUnusedPortsCntDynamically = changeUnusedPortsCntDynamically;
	}

	/**
	 * 
	 * Get input port count
	 * 	
	 * @return - number of input ports
	 */
	public int getInPortCount() {
		return inPortCount;
	}

	/**
	 * 
	 * set number of input ports
	 * 
	 * @param inPortCount
	 */
	public void setInPortCount(int inPortCount) {
		this.inPortCount = inPortCount;
		this.properties.put("inPortCount", String.valueOf(inPortCount));
	}

	/**
	 * 
	 * Get number of output port
	 * 
	 * @return
	 */
	public int getOutPortCount() {
		return outPortCount;
	}

	/**
	 * 
	 * Set number of output port
	 * 
	 * @param outPortCount
	 */
	public void setOutPortCount(int outPortCount) {
		this.outPortCount = outPortCount;
		this.properties.put("outPortCount", String.valueOf(outPortCount));
	}

	/**
	 * 
	 * Get number of unused port
	 * 
	 * @return
	 */
	public int getUnusedPortCount() {
		return unusedPortCount;
	}

	/**
	 * 
	 * set number of unused port
	 * 
	 * @param unusedPortCount
	 */
	public void setUnusedPortCount(int unusedPortCount) {
		this.unusedPortCount = unusedPortCount;
		this.properties.put("unusedPortCount", String.valueOf(unusedPortCount));
	}

	/**
	 * 
	 * Get port specification list
	 * 
	 * @return - list of {@link PortSpecification}
	 */
	public List<PortSpecification> getPortSpecification() {
		return portSpecification;
	}
	
	/**
	 * 
	 * Set ports
	 * 
	 * @param ports
	 */
	public void setPorts(HashMap<String, Port> ports) {
		this.ports = ports;
	}
	
	/**
	 * 
	 * returns true when there is change in count of input port
	 * 
	 * @return boolean
	 */
	public boolean isChangeInPortsCntDynamically() {
		return changeInPortsCntDynamically;
	}

	/**
	 * 
	 * returns true when there is change in count of output port
	 * 
	 * @return boolean
	 */
	public boolean isChangeOutPortsCntDynamically() {
		return changeOutPortsCntDynamically;
	}

	/**
	 * 
	 * returns true when there is change in count of unused port
	 * 
	 * @return boolean
	 */
	public boolean isChangeUnusedPortsCntDynamically() {
		return changeUnusedPortsCntDynamically;
	}

	/**
	 * 
	 * Add input ports
	 * 
	 * @param newPortCount
	 * @param oldPortCount
	 */
	public void incrementInPorts(int newPortCount, int oldPortCount) {

		for (int i = oldPortCount; i < newPortCount; i++) {
			Port inPort = new Port(Constants.INPUT_SOCKET_TYPE + i, Constants.INPUT_SOCKET_TYPE + i, Constants.INPUT_SOCKET_TYPE + i, this,
					newPortCount, Constants.INPUT_SOCKET_TYPE, i);
			ports.put(Constants.INPUT_SOCKET_TYPE + i, inPort);
			firePropertyChange("Component:add", null, inPort);
		}
	}

	
	/**
	 * 
	 * Add output ports
	 * 
	 * @param newPortCount
	 * @param oldPortCount
	 */
	public void incrementOutPorts(int newPortCount, int oldPortCount) {
		for (int i = oldPortCount; i < newPortCount; i++) {
			Port outputPort = new Port(Constants.OUTPUT_SOCKET_TYPE + i, Constants.OUTPUT_SOCKET_TYPE + i, Constants.OUTPUT_SOCKET_TYPE + i, this,
					newPortCount, Constants.OUTPUT_SOCKET_TYPE, i);
			ports.put(Constants.OUTPUT_SOCKET_TYPE + i, outputPort);
			firePropertyChange("Component:add", null, outputPort);
		}
	}

	/**
	 * 
	 * Add unused ports
	 * 
	 * @param newPortCount
	 * @param oldPortCount
	 */
	public void incrementUnusedPorts(int newPortCount, int oldPortCount) {
		for (int i = oldPortCount; i < newPortCount; i++) {
			Port unusedPort = new Port(Constants.UNUSED_SOCKET_TYPE + i, "un" + i, Constants.UNUSED_SOCKET_TYPE + i,
					this, newPortCount,Constants.UNUSED_SOCKET_TYPE, i);
			ports.put(Constants.UNUSED_SOCKET_TYPE + i, unusedPort);
			firePropertyChange("Component:add", null, unusedPort);
		}
	}

	/**
	 * 
	 *  Change input port count
	 * 
	 * @param newPortCount
	 */
	public void changeInPortCount(int newPortCount) {

		List<String> portTerminals = new ArrayList<>();
		portTerminals.addAll(ports.keySet());
		for (String key : portTerminals) {
			if (key.contains(Constants.INPUT_SOCKET_TYPE)) {
				ports.get(key).setNumberOfPortsOfThisType(newPortCount);
			}
		}
		setInPortCount(newPortCount);
	}

	/**
	 * 
	 * Change unused port count
	 * 
	 * @param newPortCount
	 */
	public void changeUnusedPortCount(int newPortCount) {

		List<String> portTerminals = new ArrayList<>();
		portTerminals.addAll(ports.keySet());
		for (String key : portTerminals) {
			if (key.contains(Constants.UNUSED_SOCKET_TYPE)) {
				ports.get(key).setNumberOfPortsOfThisType(newPortCount);
			}
		}
		setUnusedPortCount(newPortCount);
	}

	/**
	 * 
	 * Change output port count
	 * 
	 * @param newPortCount
	 */
	public void changeOutPortCount(int newPortCount) {

		List<String> portTerminals = new ArrayList<>();
		portTerminals.addAll(ports.keySet());
		for (String key : portTerminals) {
			if (key.contains(Constants.OUTPUT_SOCKET_TYPE)) {
				ports.get(key).setNumberOfPortsOfThisType(newPortCount);
			}
		}
		setOutPortCount(newPortCount);
	}

	/**
	 * 
	 * Get ports
	 *  
	 */
	public HashMap<String, Port> getPorts() {
		return ports;
	}

	/**
	 * Gets the port.
	 * 
	 * @param terminal
	 *            the terminal
	 * @return the port
	 */
	public Port getPort(String terminal) {
		return ports.get(terminal);
	}

	/**
	 * 
	 * Get list of component children
	 * 
	 * @return list of {@link Model}
	 */
	public List<Model> getChildren() {

		List<Model> children = new ArrayList<Model>(ports.values());
		children.add(componentLabel);

		return children;

	}
	
	private void updateConnectionProperty(String prop, Object newValue) {
		firePropertyChange(prop, null, newValue);
	}

	/**
	 * Connect input.
	 * 
	 * @param {@link Link}
	 */
	public void connectInput(Link c) {
		inputLinks.add(c);
		inputLinksHash.put(c.getTargetTerminal(), inputLinks);
		updateConnectionProperty(Props.INPUTS.getValue(), c);
	}

	/**
	 * Connect output.
	 * 
	 * @param {{@link Link}
	 * 
	 */
	public void connectOutput(Link c) {
		if (outputLinksHash.get(c.getSourceTerminal()) != null)
			c.setLinkNumber(outputLinksHash.get(c.getSourceTerminal()).size());
		else
			c.setLinkNumber(0);
		outputLinks.add(c);
		outputLinksHash.put(c.getSourceTerminal(), outputLinks);
		updateConnectionProperty(Props.OUTPUTS.getValue(), c);
	}

	/**
	 * Disconnect input.
	 * 
	 * @param c
	 *            the c
	 */
	public void disconnectInput(Link c) {
		inputLinks.remove(c);
		inputLinksHash.remove(c.getTargetTerminal());
		updateConnectionProperty(Props.INPUTS.getValue(), c);
	}

	/**
	 * Disconnect output.
	 * 
	 * @param c
	 *            the c
	 */
	public void disconnectOutput(Link c) {
		outputLinks.remove(c);
		outputLinksHash.remove(c.getSourceTerminal());
		updateConnectionProperty(Props.OUTPUTS.getValue(), c);
	}

	/**
	 * 
	 * Get source connections
	 * 
	 * @return
	 */
	public List<Link> getSourceConnections() {
		return outputLinks;
	}
	
	/**
	 * 
	 * Set source connections
	 * 
	 * @param links
	 */
	public void  setSourceConnections(List<Link> links) {
		 outputLinks=(ArrayList<Link>) links;
	}

	public List<Link> getTargetConnections() {
		return inputLinks;
	}

	/**
	 * Engage input port.
	 * 
	 * @param terminal
	 *            the terminal
	 */
	public void engageInputPort(String terminal) {
		inputportTerminals.add(terminal);
	}

	/**
	 * Free input port.
	 * 
	 * @param terminal
	 *            the terminal
	 */
	public void freeInputPort(String terminal) {
		inputportTerminals.remove(terminal);
	}

	/**
	 * Checks if is input port engaged.
	 * 
	 * @param terminal
	 *            the terminal
	 * @return true, if is input port engaged
	 */
	public boolean isInputPortEngaged(String terminal) {
		return inputportTerminals.contains(terminal);

	}

	/**
	 * Engage output port.
	 * 
	 * @param terminal
	 *            the terminal
	 */
	public void engageOutputPort(String terminal) {
		outputPortTerminals.add(terminal);
	}

	/**
	 * Free output port.
	 * 
	 * @param terminal
	 *            the terminal
	 */
	public void freeOutputPort(String terminal) {
		outputPortTerminals.remove(terminal);
	}

	/**
	 * Checks if is output port engaged.
	 * 
	 * @param terminal
	 *            the terminal
	 * @return true, if is output port engaged
	 */
	public boolean isOutputPortEngaged(String terminal) {
		return outputPortTerminals.contains(terminal);
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * Return the property value for the given propertyId, or null.
	 */
	public Object getPropertyValue(Object propertyId) {
		if (properties.containsKey(propertyId)) {
			return properties.get(propertyId);
		}
		throw new PropertyNotAvailableException();
	}

	/**
	 * Set the property value for the given property id.
	 */
	public void setPropertyValue(Object propertyId, Object value) {
		properties.put((String) propertyId, value);
		// tooltipInformation.get(propertyId).setPropertyValue(value);
	}

	public String getComponentName() {
		return componentName;
	}

	public ComponentLabel getComponentLabel() {
		return componentLabel;
	}

	public void setComponentLabel(ComponentLabel componentLabel) {
		this.componentLabel = componentLabel;
	}

	/**
	 * Set the Location of this shape.
	 * 
	 * @param newLocation
	 *            a non-null Point instance
	 * @throws IllegalArgumentException
	 *             if the parameter is null
	 */
	public void setLocation(Point newLocation) {
		resetLocation(newLocation);
		location.setLocation(newLocation);
		firePropertyChange(Props.LOCATION_PROP.getValue(), null, location);
	}

	/**
	 * reset if x or y of components are negative
	 * 
	 * @param newLocation
	 */
	private void resetLocation(Point newLocation) {
		if (newLocation.x < 0) {
			newLocation.x = 0;
		}

		if (newLocation.y < 0) {
			newLocation.y = 0;
		}
	}

	/**
	 * Return the Location of this shape.
	 * 
	 * @return a non-null location instance
	 */
	public Point getLocation() {
		return location.getCopy();
	}

	/**
	 * Set the Size of this shape. Will not modify the size if newSize is null.
	 * 
	 * @param newSize
	 *            a non-null Dimension instance or null
	 */
	public void setSize(Dimension newSize) {
		if(newSize.height<80)
			newSize.height=80;
		if (newSize != null) {
			size.setSize(newSize);
			firePropertyChange(Props.SIZE_PROP.getValue(), null, size);
		}
	}

	/**
	 * Return the Size of this shape.
	 * 
	 * @return a non-null Dimension instance
	 */
	public Dimension getSize() {
		return size.getCopy();
	}

	/**
	 * 
	 * get component label margin
	 * 
	 */
	public int getComponentLabelMargin() {
		return componentLabelMargin;
	}

	/**
	 * 
	 * Set component label margin
	 * 
	 * @param componentLabelMargin
	 */
	public void setComponentLabelMargin(int componentLabelMargin) {
		this.componentLabelMargin = componentLabelMargin;
		firePropertyChange("componentLabelMargin", null, componentLabelMargin);
	}

	/**
	 * 
	 * Get parent of the component
	 * 
	 * @return {@link Container}
	 */
	public Container getParent() {
		return parent;
	}

	/**
	 * 
	 * Set component parent
	 * 
	 * @param parent
	 */
	public void setParent(Container parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * Returns component properties
	 * 
	 * @return
	 */
	public LinkedHashMap<String, Object> getProperties() {
		return (LinkedHashMap<String, Object>) properties;
	}

	/**
	 * 
	 * PropertyNotAvailableException exception
	 * 
	 * @author Bitwise
	 *
	 */
	private class PropertyNotAvailableException extends RuntimeException {
		private static final long serialVersionUID = -7978238880803956846L;

	}

	public boolean isNewInstance() {
		return newInstance;
	}

	public void setNewInstance(boolean newInstance) {
		this.newInstance = newInstance;
	}

	/**
	 * 
	 * Returns component type
	 * 
	 * @return - component type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * Set Component type
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * Get Component prefix i.e. base name
	 * 
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 
	 * Set Component prefix i.e. base name
	 * 
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 
	 * Get Component category 
	 * 
	 * @return
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * Set component category
	 * 
	 * @param category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 
	 * returns component validation status
	 * 
	 * @return
	 */
	public String getValidityStatus() {
		return validityStatus;
	}

	/**
	 * 
	 * Set component validation status
	 * 
	 * @param validityStatus
	 */
	public void setValidityStatus(String validityStatus) {
		this.validityStatus = validityStatus;
	}

	// For Target XML
	public abstract String getConverter();

	@SuppressWarnings("unchecked")
	@Override
	public Component clone() throws CloneNotSupportedException {
		Component component = null;
		clonedHashMap = new LinkedHashMap<String, Object>();
		try {
			component = this.getClass().newInstance();
			
			for (Map.Entry<String, Object> entry : getProperties().entrySet()) {
				if (entry.getValue() instanceof String)
				{	
					clonedHashMap.put(entry.getKey(), entry.getValue());
				}
				else {
					
					if(entry.getValue() instanceof ArrayList)
					{
						clonedArrayList=new ArrayList<>();
					     ArrayList<JoinConfigProperty>  t= (ArrayList<JoinConfigProperty>) entry.getValue();
					     for(int i=0;i<t.size();i++)
					     {
					    	 clonedArrayList.add(t.get(i).clone());
					     } 
						clonedHashMap.put(entry.getKey(), clonedArrayList);
					}
					
					else if (entry.getValue() instanceof LinkedHashMap)
						clonedHashMap.put(entry.getKey(),new LinkedHashMap<>((LinkedHashMap<String, String>) entry.getValue()));
					
					else if (entry.getValue() instanceof HashMap)
					clonedHashMap.put(entry.getKey(),new HashMap<>((HashMap<String, String>) entry.getValue()));
						
					else if (entry.getValue() instanceof LinkedHashSet)
						clonedHashMap.put(entry.getKey(),new LinkedHashSet<>((LinkedHashSet<String>) entry.getValue()));
							
					
					else if (entry.getValue() instanceof HashSet)
					clonedHashMap.put(entry.getKey(),new HashSet<>((HashSet<String>) entry.getValue()));
						
					
					else if (entry.getValue() instanceof TreeMap)
					clonedHashMap.put(entry.getKey(), new TreeMap<>((TreeMap<String,String>) entry.getValue()));
						
					else if (entry.getValue() instanceof InputSubjobComponent) {
						clonedHashMap.put(entry.getKey(), null);
					}

					else if (entry.getValue() instanceof OutputSubjobComponent) {
						clonedHashMap.put(entry.getKey(), null);
					} 
					else  if(entry.getValue()!=null)
					{   
						IDataStructure c=(IDataStructure) entry.getValue();
						clonedHashMap.put(entry.getKey(), c.clone());
					}
				}

			}
		} catch (Exception e) {
			logger.debug("Unable to clone Component ", e);
		}
		component.setType(getType());
		component.setCategory(getCategory());
		component.setParent(getParent());
		component.setProperties(clonedHashMap);
		component.setPropertyValue("name", getPrefix());
		component.setSize(getSize());
		component.setLocation(getLocation());
		HashMap<String, Port> clonedPorts=new HashMap<String, Port>();
		
	    for (Map.Entry<String, Port> entry : ports.entrySet()) {
	    	Port p = entry.getValue();
	    	Port clonedPort  = p.clone();
	    	clonedPort.setParent(component);
	    	clonedPorts.put(entry.getKey(), clonedPort);
	    }
		
		component.setPorts(clonedPorts);
		
		component.setInputportTerminals(new ArrayList<String> ());
		component.setOutputPortTerminals(new ArrayList<String> ());
		component.setValidityStatus(validityStatus);
		component.setChangeInPortsCntDynamically(changeInPortsCntDynamically);
		component.setChangeOutPortsCntDynamically(changeOutPortsCntDynamically);
		component.setChangeUnusedPortsCntDynamically(changeUnusedPortsCntDynamically);
		component.setInPortCount(inPortCount);
		component.setOutPortCount(outPortCount);
		component.setUnusedPortCount(unusedPortCount);
		return component;
	}

	/**
	 * 
	 * Set tooltip information
	 * 
	 * @param tooltipInformation
	 */
	public void setTooltipInformation(
			Map<String, PropertyToolTipInformation> tooltipInformation) {
		this.tooltipInformation = tooltipInformation;
	}

	/**
	 * 
	 * get tooltip information
	 * 
	 * @return
	 */
	public Map<String, PropertyToolTipInformation> getTooltipInformation() {
		return tooltipInformation;
	}

	/**
	 * 
	 * Update tooltip information
	 * 
	 */
	public void updateTooltipInformation() {
		for (String propertyName : properties.keySet()) {
			if (tooltipInformation != null) {
				if (tooltipInformation.get(propertyName) != null) {
					tooltipInformation.get(propertyName).setPropertyValue(
							properties.get(propertyName));
					if (toolTipErrorMessages != null)
						tooltipInformation.get(propertyName).setErrorMessage(
								toolTipErrorMessages.get(propertyName));
				}
			}
		}
	}

	/**
	 * 
	 * Set component label
	 * 
	 * @param label
	 */
	public void setComponentLabel(String label) {
		setPropertyValue(Component.Props.NAME_PROP.getValue(), label);
		componentLabel.setLabelContents(label);
	}

	/**
	 * 
	 * Get component Description
	 * 
	 * @return
	 */
	public String getComponentDescription() {
		return XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getDescription();
	}

	/**
	 * 
	 * unused port settings
	 * 
	 * @param newPortCount
	 */
	public void unusedPortSettings(int newPortCount) {
       changeUnusedPortCount(newPortCount);
		for (int i = 0; i < (newPortCount - 2); i++) {
			Port unusedPort = new Port(Constants.UNUSED_SOCKET_TYPE + (i + 2), "un" + (i + 2),
					Constants.UNUSED_SOCKET_TYPE + (i + 2), this, newPortCount, Constants.UNUSED_SOCKET_TYPE, (i + 2));
			ports.put(Constants.UNUSED_SOCKET_TYPE + (i + 2), unusedPort);
			firePropertyChange("Component:add", null, unusedPort);
		}
	}
	
	/**
	 * 
	 * Input port settings
	 * 
	 * @param newPortCount
	 */
	public void inputPortSettings(int newPortCount) {
       changeInPortCount(newPortCount);
		for (int i = 0; i < (newPortCount); i++) {
			Port inPort = new Port(Constants.INPUT_SOCKET_TYPE + (i), Constants.INPUT_SOCKET_TYPE + (i), Constants.INPUT_SOCKET_TYPE
					+ (i), this, newPortCount, Constants.INPUT_SOCKET_TYPE, (i));
			ports.put(Constants.INPUT_SOCKET_TYPE + (i), inPort);
			firePropertyChange("Component:add", null, inPort);
		}
	}
	
	/**
	 * 
	 * Output port settings
	 * 
	 * @param newPortCount
	 */
	public void outputPortSettings(int newPortCount) {
  changeOutPortCount(newPortCount);
		for (int i = 0; i < (newPortCount); i++) {

			Port outPort = new Port(Constants.OUTPUT_SOCKET_TYPE + (i), Constants.OUTPUT_SOCKET_TYPE + (i), Constants.OUTPUT_SOCKET_TYPE
					+ (i), this, newPortCount, Constants.OUTPUT_SOCKET_TYPE, (i));
			ports.put(Constants.OUTPUT_SOCKET_TYPE + (i), outPort);
			firePropertyChange("Component:add", null, outPort);
		}
	} 


	/**
	 * 
	 * Increments port
	 * 
	 * @param newPortCount
	 * @param oldPortCount
	 */
	public void incrementPorts(int newPortCount, int oldPortCount) {

		for (int i = oldPortCount; i < newPortCount; i++) {
			Port inPort = new Port(Constants.INPUT_SOCKET_TYPE + i, Constants.INPUT_SOCKET_TYPE + i, Constants.INPUT_SOCKET_TYPE + i, this,
					newPortCount, Constants.INPUT_SOCKET_TYPE, i);
			ports.put(Constants.INPUT_SOCKET_TYPE + i, inPort);
			firePropertyChange("Component:add", null, inPort);

			Port unusedPort = new Port(Constants.UNUSED_SOCKET_TYPE + i, "un" + i, Constants.UNUSED_SOCKET_TYPE + i,
					this, newPortCount, Constants.UNUSED_SOCKET_TYPE, i);
			ports.put(Constants.UNUSED_SOCKET_TYPE + i, unusedPort);
			firePropertyChange("Component:add", null, unusedPort);
		}
	}

	/**
	 * 
	 * Decrements port
	 * 
	 * @param portsToBeRemoved
	 */
	public void decrementPorts(List<String> portsToBeRemoved) {

		deleteInputLinks(portsToBeRemoved);
		deleteOutputLinks(portsToBeRemoved);
		removePorts(portsToBeRemoved);
	}

	/**
	 * 
	 * Delete input links
	 * 
	 * @param portsToBeRemoved
	 */
	private void deleteInputLinks(List<String> portsToBeRemoved) {
		if (inputLinks.size() > 0) {
			Link[] inLinks = new Link[inputLinks.size()];
			inputLinks.toArray(inLinks);
			for (String portRemove : portsToBeRemoved) {

				for (Link l : inLinks) {
					if (l.getTargetTerminal().equals(portRemove)) {
						l.detachSource();
						l.detachTarget();
						l.getSource().freeOutputPort(l.getSourceTerminal());
						l.getTarget().freeInputPort(l.getTargetTerminal());
						l.setTarget(null);
						l.setSource(null);
					}
				}
			}
		}
	}

	/**
	 * 
	 * Delete output links
	 * 
	 * @param portsToBeRemoved
	 */
	private void deleteOutputLinks(List<String> portsToBeRemoved) {
		if (outputLinks.size() > 0) {
			Link[] outLinks = new Link[outputLinks.size()];
			outputLinks.toArray(outLinks);
			for (String portRemove : portsToBeRemoved) {
				for (Link l : outLinks) {
					if (l.getSourceTerminal().equals(portRemove)) {
						l.detachSource();
						l.detachTarget();
						l.getSource().freeOutputPort(l.getSourceTerminal());
						l.getTarget().freeInputPort(l.getTargetTerminal());
						l.setTarget(null);
						l.setSource(null);
					}
				}
			}
		}
	}

	/**
	 * 
	 * Remove ports
	 * 
	 * @param portsToBeRemoved
	 */
	private void removePorts(List<String> portsToBeRemoved) {
		for (String portRemove : portsToBeRemoved) {
			ports.remove(portRemove);
		}

	}

	/**
	 * 
	 * Get watcher terminals
	 * 
	 * @return
	 */
	public Map<String, Long> getWatcherTerminals() {
		return watcherTerminals;
	}

	/**
	 * 
	 * Set watcher terminals
	 * 
	 * @param watcherTerminals
	 */
	public void setWatcherTerminals(Map<String, Long> watcherTerminals) {
		this.watcherTerminals = watcherTerminals;
	}

	/**
	 * Add watcher terminal
	 * 
	 * @param port
	 * @param limit
	 */
	public void addWatcherTerminal(String port, Long limit) {

		watcherTerminals.put(port, limit);
	}

	/**
	 * 
	 * Remove watcher terminal
	 * 
	 * @param port
	 */
	public void removeWatcherTerminal(String port) {
		watcherTerminals.remove(port);
	}

	/**
	 * 
	 * Clear watchers
	 * 
	 */
	public void clearWatcherMap() {
		watcherTerminals.clear();
	}
	
	@Override

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UniqueComponentName == null) ? 0 : UniqueComponentName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Component other = (Component) obj;
		if (UniqueComponentName == null) {
			if (other.UniqueComponentName != null)
				return false;
		} else if (!UniqueComponentName.equals(other.UniqueComponentName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Component [UniqueComponentName=" + UniqueComponentName + ", type=" + type + ", prefix=" + prefix
				+ ", category=" + category + ", inPortCount=" + inPortCount + ", outPortCount=" + outPortCount
				+ ", unusedPortCount=" + unusedPortCount + ", componentLabel=" + componentLabel + "]";
	}

	public Object getComponentEditPart() {
		return componentEditPart;
	}

	public void setComponentEditPart(Object componentEditPart) {
		this.componentEditPart = componentEditPart;
	}	
	
	/**
	 * Validates all the properties of component and updates its validity status accordingly.
	 *  
	 * @return properties 
	 */
	public Map<String, Object> validateComponentProperties() {
		boolean componentHasRequiredValues = Boolean.TRUE;
		hydrograph.ui.common.component.config.Component component = XMLConfigUtil.INSTANCE.getComponent(this.getComponentName());
		Map<String, Object> properties=this.properties;
		for (Property configProperty : component.getProperty()) {
			Object propertyValue = properties.get(configProperty.getName());
			
			List<String> validators = ComponentCacheUtil.INSTANCE.getValidatorsForProperty(this.getComponentName(), configProperty.getName());
			
			IValidator validator = null;
			for (String validatorName : validators) {
				try {
					validator = (IValidator) Class.forName(Constants.VALIDATOR_PACKAGE_PREFIX + validatorName).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					logger.error("Failed to create validator", e);
					throw new RuntimeException("Failed to create validator", e);
				}
				boolean status = validator.validate(propertyValue, configProperty.getName());
				//NOTE : here if any of the property is not valid then whole component is not valid 
				if(status == false){
					componentHasRequiredValues = Boolean.FALSE;
				}
			}
		}
		
		if (!componentHasRequiredValues && properties.get(Component.Props.VALIDITY_STATUS.getValue()) == null)
			properties.put(Component.Props.VALIDITY_STATUS.getValue(), Component.ValidityStatus.WARN.name());
		else if (!componentHasRequiredValues
				&& StringUtils.equals((String) properties.get(Component.Props.VALIDITY_STATUS.getValue()),
						Component.ValidityStatus.WARN.name()))
			properties.put(Component.Props.VALIDITY_STATUS.getValue(), Component.ValidityStatus.WARN.name());
		else if (!componentHasRequiredValues
				&& StringUtils.equals((String) properties.get(Component.Props.VALIDITY_STATUS.getValue()),
						Component.ValidityStatus.ERROR.name()))
			properties.put(Component.Props.VALIDITY_STATUS.getValue(), Component.ValidityStatus.ERROR.name());
		else if (!componentHasRequiredValues
				&& StringUtils.equals((String) properties.get(Component.Props.VALIDITY_STATUS.getValue()),
						Component.ValidityStatus.VALID.name()))
			properties.put(Component.Props.VALIDITY_STATUS.getValue(), Component.ValidityStatus.ERROR.name());
		else if (componentHasRequiredValues)
			properties.put(Component.Props.VALIDITY_STATUS.getValue(), Component.ValidityStatus.VALID.name());
		return properties;
	}	
}
