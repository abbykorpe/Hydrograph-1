package com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

/**
 * The Class RunTimePropertyCellModifier.
 * 
 * @author Bitwise
 */
public class RunTimePropertyCellModifier implements ICellModifier {
	private Viewer viewer;
	private static final String PROPERTY_NAME = "PROPERTY_NAME"; 
	private static final String PROPERTY_VALUE = "PROPERTY_VALUE";

	/**
	 * Instantiates a new run time property cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public RunTimePropertyCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Returns whether the property can be modified
	 * 
	 * @param element
	 *            the element
	 * @param property
	 *            the property
	 * @return boolean
	 */
	public boolean canModify(Object element, String property) {
		// Allow editing of all values
		return true;
	}

	/**
	 * Returns the value for the property
	 * 
	 * @param element
	 *            the element
	 * @param property
	 *            the property
	 * @return Object
	 */
	public Object getValue(Object element, String property) {
		RuntimeProperties p = (RuntimeProperties) element;
		
		if (PROPERTY_NAME.equals(property)) {

			return p.getPropertyName();

		} else if (PROPERTY_VALUE.equals(property))
			return p.getPropertyValue();
		else
			return null;
	}

	/**
	 * Modifies the element
	 * 
	 * @param element
	 *            the element
	 * @param property
	 *            the property
	 * @param value
	 *            the value
	 */
	public void modify(Object element, String property, Object value) {

		if (element instanceof Item)
			element = ((Item) element).getData();

		RuntimeProperties p = (RuntimeProperties) element;
		
		if (PROPERTY_NAME.equals(property))
			p.setPropertyName(((String) value));

		else if (PROPERTY_VALUE.equals(property))
			p.setPropertyValue((String) value);
		// Force the viewer to refresh
		viewer.refresh();
	}
}