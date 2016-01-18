package com.bitwise.app.graph.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.slf4j.Logger;

import com.bitwise.app.cloneableinterface.IDataStructure;
import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

// TODO: Auto-generated Javadoc
/**
 * The Class Component.
 * 
 * @author Bitwise
 */
public abstract class Component extends Model {
	private static final Logger logger = LogFactory.INSTANCE
			.getLogger(Component.class);

	private static final long serialVersionUID = 2587870876576884352L;

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
	private final List<String> inputportTerminals;
	private final List<String> outputPortTerminals;
	private boolean newInstance;
	private String type;
	private String prefix;
	private String category;
	private HashMap<String, Port> ports;
	private String componentName;
	List<PortSpecification> portSpecification;

	private int inPortCount;
	private int outPortCount;
	private int unusedPortCount;

	private boolean changeInPortsCntDynamically;
	private boolean changeOutPortsCntDynamically;
	private boolean changeUnusedPortsCntDynamically;

	private ComponentLabel componentLabel;
	private int componentLabelMargin;

	@XStreamOmitField
	private Map<String, PropertyToolTipInformation> tooltipInformation;

	// @XStreamOmitField
	private Map<String, String> toolTipErrorMessages; // <propertyName,ErrorMessage>

	/*
	 * @XStreamOmitField private Map<String,PropertyToolTipInformation>
	 * paletteTooltipMessage;
	 */

	/**
	 * Instantiates a new component.
	 */
	public Component() {
		location = new Point(0, 0);
		size = new Dimension(100, 75);
		properties = new LinkedHashMap<>();
		inputLinksHash = new Hashtable<String, ArrayList<Link>>();

		inputLinks = new ArrayList<Link>();
		outputLinksHash = new Hashtable<String, ArrayList<Link>>();
		outputLinks = new ArrayList<Link>();
		inputportTerminals = new ArrayList<String>();
		outputPortTerminals = new ArrayList<String>();
		newInstance = true;
		validityStatus = ValidityStatus.WARN.name();
		componentName = DynamicClassProcessor.INSTANCE.getClazzName(this
				.getClass());

		componentLabel = new ComponentLabel(componentName);
		componentLabelMargin = 16;

		prefix = XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getDefaultNamePrefix();
		initPortSettings();
		initDynamicPortFlags();
		toolTipErrorMessages = new LinkedHashMap<>();
	}

	public Map<String, String> getToolTipErrorMessages() {
		return toolTipErrorMessages;
	}

	public void setToolTipErrorMessages(Map<String, String> toolTipErrorMessages) {
		this.toolTipErrorMessages = toolTipErrorMessages;
	}

	private void initPortSettings() {

		portSpecification = XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getPort().getPortSpecification();

		ports = new HashMap<String, Port>();

		for (PortSpecification p : portSpecification) {
			setPortCount(p.getTypeOfPort().value(), p.getNumberOfPorts());
			String portTerminal = p.getTypeOfPort().value()
					+ p.getSequenceOfPort();
			Port port = new Port(p.getNameOfPort(), p.getLabelOfPort(),
					portTerminal, this, p.getNumberOfPorts(), p.getTypeOfPort()
							.value(), p.getSequenceOfPort());
			ports.put(portTerminal, port);
		}
	}

	private void setPortCount(String portType, int portCount) {
		if (portType.equals("in")) {
			inPortCount = portCount;
			properties.put("inPortCount", String.valueOf(portCount));
		} else if (portType.equals("out")) {
			outPortCount = portCount;
			properties.put("outPortCount", String.valueOf(portCount));
		} else if (portType.equals("unused")) {
			unusedPortCount = portCount;
			properties.put("unusedPortCount", String.valueOf(portCount));
		}
	}

	public int getInPortCount() {
		return inPortCount;
	}

	public void setInPortCount(int inPortCount) {
		this.inPortCount = inPortCount;
	}

	public int getOutPortCount() {
		return outPortCount;
	}

	public void setOutPortCount(int outPortCount) {
		this.outPortCount = outPortCount;
	}

	public int getUnusedPortCount() {
		return unusedPortCount;
	}

	public void setUnusedPortCount(int unusedPortCount) {
		this.unusedPortCount = unusedPortCount;
	}

	public List<PortSpecification> getPortSpecification() {
		return portSpecification;
	}

	private void initDynamicPortFlags() {
		changeInPortsCntDynamically = XMLConfigUtil.INSTANCE.getComponent(
				componentName).isChangeInPortsDynamically();
		changeOutPortsCntDynamically = XMLConfigUtil.INSTANCE.getComponent(
				componentName).isChangeOutPortsDynamically();
		changeUnusedPortsCntDynamically = XMLConfigUtil.INSTANCE.getComponent(
				componentName).isChangeUnusedPortsDynamically();
	}

	public boolean isChangeInPortsCntDynamically() {
		return changeInPortsCntDynamically;
	}

	public boolean isChangeOutPortsCntDynamically() {
		return changeOutPortsCntDynamically;
	}

	public boolean isChangeUnusedPortsCntDynamically() {
		return changeUnusedPortsCntDynamically;
	}

	public void incrementInPorts(int newPortCount, int oldPortCount) {

		for (int i = oldPortCount; i < newPortCount; i++) {
			Port inPort = new Port("in" + i, "in" + i, "in" + i, this,
					newPortCount, "in", i);
			ports.put("in" + i, inPort);
			firePropertyChange("Component:add", null, inPort);
		}
	}

	public void incrementOutPorts(int newPortCount, int oldPortCount) {
		for (int i = oldPortCount; i < newPortCount; i++) {
			Port unusedPort = new Port("out" + i, "out" + i, "out" + i, this,
					newPortCount, "out", i);
			ports.put("unused" + i, unusedPort);
			firePropertyChange("Component:add", null, unusedPort);
		}
	}

	public void incrementUnusedPorts(int newPortCount, int oldPortCount) {
		for (int i = oldPortCount; i < newPortCount; i++) {
			Port unusedPort = new Port("unused" + i, "un" + i, "unused" + i,
					this, newPortCount, "unused", i);
			ports.put("unused" + i, unusedPort);
			firePropertyChange("Component:add", null, unusedPort);
		}
	}

	public void changeInPortCount(int newPortCount) {

		List<String> portTerminals = new ArrayList<>();
		portTerminals.addAll(ports.keySet());
		for (String key : portTerminals) {
			if (key.contains("in")) {
				ports.get(key).setNumberOfPortsOfThisType(newPortCount);
			}
		}
		setInPortCount(newPortCount);
	}

	public void changeUnusedPortCount(int newPortCount) {

		List<String> portTerminals = new ArrayList<>();
		portTerminals.addAll(ports.keySet());
		for (String key : portTerminals) {
			if (key.contains("unused")) {
				ports.get(key).setNumberOfPortsOfThisType(newPortCount);
			}
		}
		setUnusedPortCount(newPortCount);
	}

	public void changeOutPortCount(int newPortCount) {

		List<String> portTerminals = new ArrayList<>();
		portTerminals.addAll(ports.keySet());
		for (String key : portTerminals) {
			if (key.contains("out")) {
				ports.get(key).setNumberOfPortsOfThisType(newPortCount);
			}
		}
		setOutPortCount(newPortCount);
	}

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
	 * @param c
	 *            the c
	 */
	public void connectInput(Link c) {
		inputLinks.add(c);
		inputLinksHash.put(c.getTargetTerminal(), inputLinks);
		updateConnectionProperty(Props.INPUTS.getValue(), c);
	}

	/**
	 * Connect output.
	 * 
	 * @param c
	 *            the c
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

	/* add comments as function called by gef */
	public List<Link> getSourceConnections() {
		return outputLinks;
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

	public int getComponentLabelMargin() {
		return componentLabelMargin;
	}

	public void setComponentLabelMargin(int componentLabelMargin) {
		this.componentLabelMargin = componentLabelMargin;
		firePropertyChange("componentLabelMargin", null, componentLabelMargin);
	}

	public Container getParent() {
		return parent;
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}

	public LinkedHashMap<String, Object> getProperties() {
		return (LinkedHashMap<String, Object>) properties;
	}

	private class PropertyNotAvailableException extends RuntimeException {
		private static final long serialVersionUID = -7978238880803956846L;

	}

	public boolean isNewInstance() {
		return newInstance;
	}

	public void setNewInstance(boolean newInstance) {
		this.newInstance = newInstance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getValidityStatus() {
		return validityStatus;
	}

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
					else if (entry.getValue() instanceof HashMap)
					clonedHashMap.put(entry.getKey(),new HashMap<>((HashMap<String, String>) entry.getValue()));
						
					
					else if (entry.getValue() instanceof HashSet)
					clonedHashMap.put(entry.getKey(),new HashSet<>((HashSet<String>) entry.getValue()));
						
					
					else if (entry.getValue() instanceof TreeMap)
					clonedHashMap.put(entry.getKey(), new TreeMap<>((TreeMap<String,String>) entry.getValue()));
						
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
		return component;
	}

	public void setTooltipInformation(
			Map<String, PropertyToolTipInformation> tooltipInformation) {
		this.tooltipInformation = tooltipInformation;
	}

	public Map<String, PropertyToolTipInformation> getTooltipInformation() {
		return tooltipInformation;
	}

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

	public void setComponentLabel(String label) {
		setPropertyValue(Component.Props.NAME_PROP.getValue(), label);
		componentLabel.setLabelContents(label);
	}

	public String getComponentDescription() {
		return XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getDescription();
	}

	public void importPortSettings(int newPortCount) {

		changePortCount(newPortCount);

		for (int i = 0; i < (newPortCount - 2); i++) {

			Port inPort = new Port("in" + (i + 2), "in" + (i + 2), "in"
					+ (i + 2), this, newPortCount, "in", (i + 2));
			ports.put("in" + (i + 2), inPort);
			firePropertyChange("Component:add", null, inPort);

			Port unusedPort = new Port("unused" + (i + 2), "un" + (i + 2),
					"unused" + (i + 2), this, newPortCount, "unused", (i + 2));
			ports.put("unused" + (i + 2), unusedPort);
			firePropertyChange("Component:add", null, unusedPort);
		}
	}

	public void incrementPorts(int newPortCount, int oldPortCount) {

		for (int i = oldPortCount; i < newPortCount; i++) {
			Port inPort = new Port("in" + i, "in" + i, "in" + i, this,
					newPortCount, "in", i);
			ports.put("in" + i, inPort);
			firePropertyChange("Component:add", null, inPort);

			Port unusedPort = new Port("unused" + i, "un" + i, "unused" + i,
					this, newPortCount, "unused", i);
			ports.put("unused" + i, unusedPort);
			firePropertyChange("Component:add", null, unusedPort);
		}
	}

	public void changePortCount(int newPortCount) {

		List<String> portTerminals = new ArrayList<>();
		portTerminals.addAll(ports.keySet());
		for (String key : portTerminals) {
			if (key.contains("in") || key.contains("unused")) {
				ports.get(key).setNumberOfPortsOfThisType(newPortCount);
			}
		}
	}

	public void decrementPorts(List<String> portsToBeRemoved) {

		deleteInputLinks(portsToBeRemoved);
		deleteOutputLinks(portsToBeRemoved);
		removePorts(portsToBeRemoved);
	}

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

	private void removePorts(List<String> portsToBeRemoved) {
		for (String portRemove : portsToBeRemoved) {
			ports.remove(portRemove);
		}

	}

}
