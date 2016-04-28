package hydrograph.ui.graph.model;

import java.util.Map;

public class PortDetails extends Model implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8713585457081109591L;
	private Map<String, Port> ports;
	//private PortTypeEnum portType;
	private PortAlignmentEnum portAlignment;
	private int numberOfPorts;
	private boolean changePortCountDynamically;
	private boolean allowMultipleLinks;
	private boolean linkMandatory;
	
	public PortDetails(Map<String, Port> p,  PortAlignmentEnum portAlignment, int noOfPorts, boolean changePortCount, boolean allowMultLinks, boolean linkMan){
		this.ports = p;
		//this.portType = portTypeEnum;
		this.portAlignment = portAlignment;
		this.numberOfPorts = noOfPorts;
		this.changePortCountDynamically = changePortCount;
		this.allowMultipleLinks = allowMultLinks;
		this.linkMandatory = linkMan;
	}

	public boolean isLinkMandatory() {
		return linkMandatory;
	}

	public boolean isAllowMultipleLinks() {
		return allowMultipleLinks;
	}

	public Map<String, Port> getPorts() {
		return ports;
	}

	public void setPorts(Map<String, Port> ports) {
		this.ports = ports;
	}

	/*public PortTypeEnum getPortType() {
		return portType;
	}*/

	public PortAlignmentEnum getPortAlignment() {
		return portAlignment;
	}

	public int getNumberOfPorts() {
		return numberOfPorts;
	}

	public boolean isChangePortCountDynamically() {
		return changePortCountDynamically;
	}
	
	
}
