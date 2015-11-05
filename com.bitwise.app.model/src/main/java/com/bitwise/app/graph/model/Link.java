package com.bitwise.app.graph.model;

import org.eclipse.draw2d.Graphics;

// TODO: Auto-generated Javadoc
/**
 * The Class Link.
 * 
 * @author Bitwise
 */
public class Link extends Model {
	private static final long serialVersionUID = -4969635974273718739L;

	/** Line drawing style for this connection. */
	private int lineStyle = Graphics.LINE_SOLID;
	private int linkNumber;
	/**
	 * Used for indicating that a Connection with solid line style should be
	 * created.
	 */
	public static final Integer SOLID_CONNECTION = new Integer(Graphics.LINE_SOLID);
	/** Property ID to use when the line style of this connection is modified. */
	public static final String LINESTYLE_PROP = "LineStyle";
	
	/** True, if the connection is attached to its endpoints. */
	private boolean isConnected;
	
	/** Connection's source endpoint. */
	private Component source;
	
	/** Connection's target endpoint. */
	private Component target;
	
	private String sourceTerminal, targetTerminal;
	
	
	
	
	/**
	 * Instantiates a new link.
	 */
	public Link() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Attach source.
	 */
	public void attachSource() {
		if (getSource() == null
				|| getSource().getSourceConnections().contains(this))
			return;
		
		getSource().connectOutput(this);
	}

	/**
	 * Attach target.
	 */
	public void attachTarget() {
		if (getTarget() == null
				|| getTarget().getTargetConnections().contains(this))
			return;
		getTarget().connectInput(this);
	}
	
	/**
	 * Detach source.
	 */
	public void detachSource() {
		if (getSource() == null)
			return;
		getSource().disconnectOutput(this);
	}
	
	/**
	 * Detach target.
	 */
	public void detachTarget() {
		if (getTarget() == null)
			return;
		getTarget().disconnectInput(this);
	}
	
	public Component getSource() {
		return source;
	}

	public String getSourceTerminal() {
		return sourceTerminal;
	}
	
	public Component getTarget() {
		return target;
	}

	public String getTargetTerminal() {
		return targetTerminal;
	}
	
	public void setSource(Component e) {
		Object old = source;
		source = e;
		firePropertyChange("source", old, source);
	}
	public void setSourceTerminal(String s) {
		Object old = sourceTerminal;
		sourceTerminal = s;
		firePropertyChange("sourceTerminal", old, sourceTerminal);
	}

	public void setTarget(Component e) {
		Object old = target;
		target = e;
		firePropertyChange("target", old, target);
	}

	public void setTargetTerminal(String s) {
		Object old = targetTerminal;
		targetTerminal = s;
		firePropertyChange("targetTerminal", old, targetTerminal);
	}
	/**
	 * Returns the line drawing style of this connection.
	 * 
	 * @return an int value (Graphics.LINE_DASH or Graphics.LINE_SOLID)
	 */
	public int getLineStyle() {
		return lineStyle;
	}
	
		
	/**
	 * Set the line drawing style of this connection.
	 * @param lineStyle one of following values: Graphics.LINE_SOLID
	 * @see Graphics#LINE_SOLID
	 * @throws IllegalArgumentException
	 *             if lineStyle does not have one of the above values
	 */
	public void setLineStyle(int lineStyle) {
		if (lineStyle != Graphics.LINE_SOLID) {
			throw new IllegalArgumentException();
		}
		this.lineStyle = lineStyle;
		firePropertyChange(LINESTYLE_PROP, null, new Integer(this.lineStyle));
	}
	
	public int getLinkNumber() {
		return linkNumber;
	}

	public void setLinkNumber(int linkNumber) {
		this.linkNumber = linkNumber;
	}

	/**
	 * Reconnect.
	 */
	public void reconnect(){
		if(!isConnected){
			source.connectOutput(this);
			source.engageOutputPort(sourceTerminal);
			
			isConnected=true;
		}
	}
	
	/**
	 * Reconnect.
	 * 
	 * @param newSource
	 *            the new source
	 * @param sourceTerminal
	 *            the source terminal
	 */
	//Reconnect to different Source or Target
	public void reconnect(Component newSource,String sourceTerminal){
		if(newSource==null && sourceTerminal==null){
			throw new IllegalArgumentException();
		}
		detachSource();
		this.source=newSource;
		this.sourceTerminal=sourceTerminal;
		reconnect();
	}
}
