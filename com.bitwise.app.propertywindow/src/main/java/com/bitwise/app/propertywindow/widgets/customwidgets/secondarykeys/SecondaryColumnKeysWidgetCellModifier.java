package com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;



// TODO: Auto-generated Javadoc
/**
 * The Class RunTimePropertyCellModifier.
 * 
 * @author Bitwise
 */
public class SecondaryColumnKeysWidgetCellModifier implements ICellModifier {
	private Viewer viewer;
	private static final String COLUMNNAME = "Column Name"; //$NON-NLS-1$
	private static final String SORTORDER = "Sort Order"; //$NON-NLS-1$

	/**
	 * Instantiates a new run time property cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public SecondaryColumnKeysWidgetCellModifier(Viewer viewer) {
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
		SecondaryColumnKeysInformation p = (SecondaryColumnKeysInformation) element;
		
		if (COLUMNNAME.equals(property)) {

			return p.getPropertyName();

		} else if (SORTORDER.equals(property))
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

		SecondaryColumnKeysInformation p = (SecondaryColumnKeysInformation) element;
		
		if (COLUMNNAME.equals(property))
			p.setPropertyName(((String) value));

		else if (SORTORDER.equals(property))
			p.setPropertyValue((String) value);
		// Force the viewer to refresh
		viewer.refresh();
	}
}