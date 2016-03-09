package com.bitwise.app.propertywindow.widgets.customwidgets.operational;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.NameValueProperty;

/**
 * @author Bitwise
 * This class represents the cell modifier for the SchemaEditor program
 */

public class PropertyGridCellModifier implements ICellModifier {
  private Viewer viewer;

  /** The Constant PROPERTY_NAME. */
	private static final String PROPERTY_NAME = "Source";
	
	/** The Constant PROPERTY_VALUE. */
	private static final String PROPERTY_VALUE = "Target";
	
  /**
	 * Instantiates a new schema grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
  public PropertyGridCellModifier(Viewer viewer) {
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
	  NameValueProperty p = (NameValueProperty) element;
	  
    if (PROPERTY_NAME.equals(property))
        return p.getPropertyName();
    else if (PROPERTY_VALUE.equals(property))
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
 
    NameValueProperty p = (NameValueProperty) element;
    if (PROPERTY_NAME.equals(property))
      p.setPropertyName(((String) value).trim());
    if (PROPERTY_VALUE.equals(property))
        p.setPropertyValue(((String) value).trim());
    viewer.refresh();
  }
  
  
  
}