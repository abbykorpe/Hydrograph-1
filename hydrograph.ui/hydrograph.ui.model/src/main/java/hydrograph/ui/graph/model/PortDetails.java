package hydrograph.ui.graph.model;

import java.util.List;

public class PortDetails extends Model implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8713585457081109591L;
	private List<Port> ports;
	private PortTypeEnum portType;
	private int numberOfPorts;
	private boolean changePortCountDynamically;
	private boolean allowMultipleLinks;
	private boolean linkMandatory;
	
	public PortDetails(List<Port> p, PortTypeEnum portTypeEnum, int noOfPorts, boolean changePortCount, boolean allowMultLinks, boolean linkMan){
		this.ports = p;
		this.portType = portTypeEnum;
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

	public List<Port> getPort() {
		return ports;
	}

	public PortTypeEnum getPortType() {
		return portType;
	}

	public int getNumberOfPorts() {
		return numberOfPorts;
	}

	public boolean isChangePortCountDynamically() {
		return changePortCountDynamically;
	}
	
	
}
