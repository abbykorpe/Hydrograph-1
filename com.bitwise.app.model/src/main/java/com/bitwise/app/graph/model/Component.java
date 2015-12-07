package com.bitwise.app.graph.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
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
	private static final long serialVersionUID = 2587870876576884352L;

	/**
	 * The Enum Props.
	 * 
	 * @author Bitwise
	 */
	public static enum Props {
		NAME_PROP("name"),
		LOCATION_PROP("Location"),
		SIZE_PROP("Size"),
		INPUTS("inputs"),
		OUTPUTS("outputs"),
		VALIDITY_STATUS("validityStatus");
		
		private String value;
		private Props(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}
		
		/**
		 * Eq.
		 * 
		 * @param property
		 *            the property
		 * @return true, if successful
		 */
		public boolean eq(String property){
			return this.value.equals(property);
		}
	}
	
	/**
	 * The Enum ValidityStatus.
	 * 
	 * @author Bitwise
	 */
	public static enum ValidityStatus {
		WARN,
		ERROR,
		VALID;
	}
	
	private final Point location;
	private final Dimension size;
	private Map<String, Object> properties;
	private Container parent;
	private String validityStatus;
	
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
	
	private ComponentLabel componentLabel;
	private int componentLabelMargin;
		
	@XStreamOmitField
	private Map<String,PropertyToolTipInformation> tooltipInformation;
	
	//@XStreamOmitField
	private Map<String,String> toolTipErrorMessages; //<propertyName,ErrorMessage>
	
	
	/*@XStreamOmitField
	private Map<String,PropertyToolTipInformation> paletteTooltipMessage;*/

	/**
	 * Instantiates a new component.
	 */
	public Component(){
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
		componentName = DynamicClassProcessor.INSTANCE
				.getClazzName(this.getClass());
		
		componentLabel = new ComponentLabel(componentName);
		
		prefix = XMLConfigUtil.INSTANCE.getComponent(componentName).getDefaultNamePrefix();
		initPortSettings();
		toolTipErrorMessages = new LinkedHashMap<>();
	}
	
	public Map<String, String> getToolTipErrorMessages() {
		return toolTipErrorMessages;
	}

	public void setToolTipErrorMessages(Map<String, String> toolTipErrorMessages) {
		this.toolTipErrorMessages = toolTipErrorMessages;
	}

	private void initPortSettings(){
		
		portSpecification = XMLConfigUtil.INSTANCE.getComponent(componentName).getPort().getPortSpecification();
		
		ports = new HashMap<String, Port>();
		
		for(PortSpecification p:portSpecification)
		{ 	
			String portTerminal = p.getTypeOfPort() + p.getSequenceOfPort();
			Port port = new Port(p.getNameOfPort(),p.getLabelOfPort(),portTerminal, this, p.getNumberOfPorts(), p.getTypeOfPort(), p.getSequenceOfPort());
			ports.put(portTerminal, port);
		}
	}
	
	public List<PortSpecification> getPortSpecification() {
		return portSpecification;
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
		firePropertyChange(prop, null,newValue);
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
		if(outputLinksHash.get(c.getSourceTerminal())!=null)
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
	
	/* add comments as function called by gef*/
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
	public void engageInputPort(String terminal){
		inputportTerminals.add(terminal);
	}

	/**
	 * Free input port.
	 * 
	 * @param terminal
	 *            the terminal
	 */
	public void freeInputPort(String terminal){
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
	public void engageOutputPort(String terminal){
		outputPortTerminals.add(terminal);
	}

	/**
	 * Free output port.
	 * 
	 * @param terminal
	 *            the terminal
	 */
	public void freeOutputPort(String terminal){
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
		//tooltipInformation.get(propertyId).setPropertyValue(value);
	}

	public ComponentLabel getLogicLabel() {
		return componentLabel;
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
	 *  reset if x or y of components are negative
	 * @param newLocation
	 */
	private void resetLocation(Point newLocation) {
		if(newLocation.x < 0 ){
			newLocation.x = 0;			
		}
		
		if(newLocation.y < 0){
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
	
	//For Target XML
	public abstract String getConverter();
	
	@Override
	public Component clone() throws CloneNotSupportedException {
		Component component = null;
		try {
			component = this.getClass().newInstance();
		} catch (Exception e) {
			//TODO : add logger
			e.printStackTrace();
		} 
		component.setType(getType());
		component.setCategory(getCategory());
		component.setParent(getParent());
		component.setProperties((Map<String, Object>) getProperties().clone());
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
		for(String propertyName: properties.keySet()){
			if(tooltipInformation != null){
				if(tooltipInformation.get(propertyName) != null){
					tooltipInformation.get(propertyName).setPropertyValue(properties.get(propertyName));
					if(toolTipErrorMessages!=null)
						tooltipInformation.get(propertyName).setErrorMessage(toolTipErrorMessages.get(propertyName));
				}
			}	
		}		
	}
	
	public void setComponentLabel(String label) {
		setPropertyValue(Component.Props.NAME_PROP.getValue(), label);
		componentLabel.setLabelContents(label);
	}
	
	
	public String getComponentDescription(){
		return XMLConfigUtil.INSTANCE.getComponent(componentName).getDescription();
	}
	
	public void changePortSettings(int newPortCount){

		for(PortSpecification p:portSpecification)
		{ 	
			String portTerminal = p.getTypeOfPort() + p.getSequenceOfPort();
			Port port = new Port(p.getNameOfPort(), p.getLabelOfPort(), portTerminal, this, p.getNumberOfPorts(), p.getTypeOfPort(), p.getSequenceOfPort());
			if(p.getTypeOfPort().equals("in")){
				port.setNumberOfPortsOfThisType(newPortCount);
			}
			ports.put(portTerminal, port);
		}
		
		for(int i=0; i< (newPortCount-2); i++){

			Port port = new Port("in"+(i+2) , "in"+(i+2), "in"+(i+3), this, newPortCount, "in", (i+3));
			ports.put("in"+(i+3), port);
			firePropertyChange("Component:add", null, port );
		}
	}
	
	public void clearPorts(){

		deleteInputLinks();
		deleteOutputLinks();
		ports.clear();

		firePropertyChange("Component:remove", ports.values(), null );
	}
	private void deleteInputLinks(){
		if(inputLinks.size() > 0){
			Link[] inLinks = new Link[inputLinks.size()];
			inputLinks.toArray(inLinks);
			for (Link l: inLinks){
				l.detachSource();
				l.detachTarget();
				l.getSource().freeOutputPort(l.getSourceTerminal());
				l.getTarget().freeInputPort(l.getTargetTerminal());
				l.setTarget(null);
				l.setSource(null);
			}
		}
	}
	private void deleteOutputLinks(){
		if(outputLinks.size() > 0){
			Link[] outLinks = new Link[outputLinks.size()];
			outputLinks.toArray(outLinks);
			for (Link l: outLinks){
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
