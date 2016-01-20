package com.bitwise.app.graph.model;


// TODO: Auto-generated Javadoc
/**
 * The Class Port.
 * 
 * @author Bitwise
 */
public class Port extends Model implements Cloneable{
	
	private static final long serialVersionUID = 302760655288792415L;
	private String terminal;
	private int numberOfPortsOfThisType;
	private String portType;
	private int sequence;
	private Component parent;
	private String nameOfPort;
	private String labelOfPort;
	
	/**
	 * Instantiates a new port.
	 * * @param nameOfPort
	 *            the nameOfPort
	 * @param terminal
	 *            the terminal
	 * @param component
	 *            the component
	 * @param noPortsOfThisType
	 *            the no ports of this type
	 * @param type
	 *            the type
	 * @param seq
	 *            the seq
	 */
	public Port(String nameOfPort,String labelOfPort,String terminal, Component component, int noPortsOfThisType, String type, int seq){
		this.terminal = terminal;
		this.numberOfPortsOfThisType = noPortsOfThisType;
		this.portType = type;
		this.sequence = seq;
		this.parent =component;
		this.nameOfPort=nameOfPort;
		this.labelOfPort=labelOfPort;
	}
	
	

	public String getLabelOfPort() {
		return labelOfPort;
	}
	
	public void setLabelOfPort(String label) {
		this.labelOfPort=label;
	}

	public Component getParent() {
		return parent;
	}
	
	public void setParent(Component parent) {
		this.parent = parent;
	}



	public String getTerminal() {
		return terminal;
	}

	public int getNumberOfPortsOfThisType() {
		return numberOfPortsOfThisType;
	}

	public String getPortType() {
		return portType;
	}

	public int getSequence() {
		return sequence;
	}
	
	public String getNameOfPort() {
		return nameOfPort;
	}
	public void setNumberOfPortsOfThisType(int NewPortCount){
		this.numberOfPortsOfThisType = NewPortCount;
	}
	
	@Override
	public String toString() {
				
		 return "\n******************************************"+
				"\nTerminal: "+terminal+
				"\nnumberOfPortsOfThisType: "+this.numberOfPortsOfThisType+
				"\nportType: "+this.portType+
				"\nsequence: "+this.sequence+
				"\nparent: "+this.parent+
				"\nnameOfPort: "+this.nameOfPort+
				"\nlabelOfPort: "+this.labelOfPort+
				"\n******************************************\n";
		 
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Port) {
			Port p = (Port) o;
			
			if ( p.getTerminal().equals(this.getTerminal()) &&
					p.getSequence() == this.getSequence() &&
					p.getPortType().equals(this.getPortType()) &&
					p.getParent().equals( this.getParent()) &&
					p.getNameOfPort().equals(this.getNameOfPort()) &&
					p.getLabelOfPort().equals(this.getLabelOfPort())
				)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
	 
		result = 31 * result + sequence;
		result = 31 * result + terminal.hashCode();
		result = 31 * result + portType.hashCode();
		result = 31 * result + parent.hashCode();
		result = 31 * result + nameOfPort.hashCode();
		result = 31 * result + labelOfPort.hashCode();
	 
		return result;
		
		
	}
	
	@Override
	protected Port clone() throws CloneNotSupportedException {
		Port clonedPort = (Port) super.clone();
		clonedPort.terminal = terminal;
		clonedPort.numberOfPortsOfThisType = numberOfPortsOfThisType;
		clonedPort.portType = portType;
		clonedPort.sequence = sequence;
		clonedPort.nameOfPort = nameOfPort;
		clonedPort.labelOfPort = labelOfPort;
		
		return clonedPort;
	}
	
}
