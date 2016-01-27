package com.bitwise.app.graph.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Model.
 * 
 * @author Bitwise
 */
public abstract class Model implements Serializable {

	private static final long serialVersionUID = -4073149938391231758L;
	
	private final PropertyChangeSupport propertyChangeSupport;
		
	/**
	 * Instantiates a new model.
	 */
	Model(){
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	/**
	 * Attach a non-null PropertyChangeListener to this object.
	 * 
	 * @param listner a non-null PropertyChangeListener instance
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener listner) {
		propertyChangeSupport.addPropertyChangeListener(listner);
	}
	
	
	/**
	 * Remove a PropertyChangeListener from this component.
	 * @param listner PropertyChangeListener instance
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener listner) {
		if (listner != null) {
			propertyChangeSupport.removePropertyChangeListener(listner);
		}
	}
	
	/**
	 * Report a property change to registered listeners (for example editparts).
	 * @param property the programmatic name of the property that changed
	 * @param oldValue the old value of this property
	 * @param newValue the new value of this property
	 */
	protected void firePropertyChange(String property, Object oldValue,
			Object newValue) {
		if (propertyChangeSupport.hasListeners(property)) {
			propertyChangeSupport.firePropertyChange(property, oldValue, newValue);
		}
	}
}
