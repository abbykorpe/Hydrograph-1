package com.bitwise.app.propertywindow.fixedwidthschema;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

/**
 * The Class FixedWidthGridLabelProvider.
 * 
 * @author Bitwise
 */
public class FixedWidthGridLabelProvider implements ITableLabelProvider,ITableColorProvider {

	/**
	   * Returns the image
	   * 
	   * @param element
	   *            the element
	   * @param columnIndex
	   *            the column index
	   * @return Image
	   */
	  public Image getColumnImage(Object element, int columnIndex) {
	    return null;
	  }

	  /**
	   * Returns the column text
	   * 
	   * @param element
	   *            the element
	   * @param columnIndex
	   *            the column index
	   * @return String
	   */
	  public String getColumnText(Object element, int columnIndex) {
	   FixedWidthGridRow fixedGrid = (FixedWidthGridRow) element;
		  switch (columnIndex) {
	    case 0:
	      return fixedGrid.getFieldName(); 
	    case 1:
	    	 return GridWidgetCommonBuilder.getDataTypeKey()[fixedGrid.getDataType().intValue()];   
	    case 2:
	    	 return fixedGrid.getDateFormat();
	    case 3:
	   	 	 return fixedGrid.getPrecision(); 
	    case 4: 
	    	return fixedGrid.getScale().toString();
	    case 5:
	    	return GridWidgetCommonBuilder.getScaleTypeKey()[fixedGrid.getScaleType().intValue()];  
	    case 6: 
	    	return fixedGrid.getDescription();
	    case 7: 
	    	return fixedGrid.getLength().toString();
	    }
	    return null;
	  }
	 
	  /**
	   * Adds a listener
	   * 
	   * @param listener
	   *            the listener
	   */
	  public void addListener(ILabelProviderListener listener) {
	    // Ignore it
	  }

	  /**
	   * Disposes any created resources
	   */
	  public void dispose() {
	    // Nothing to dispose
	  }

	  /**
	   * Returns whether altering this property on this element will affect the
	   * label
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @return boolean
	   */
	  public boolean isLabelProperty(Object element, String property) {
	    return false;
	  }

	  /**
	   * Removes a listener
	   * 
	   * @param listener
	   *            the listener
	   */
	  public void removeListener(ILabelProviderListener listener) {
	    // Ignore
	  }

	  @Override
		public Color getBackground(Object element, int columnIndex) {

			return new Color(Display.getDefault(), new RGB(255, 255, 230));
		}

		@Override
		public Color getForeground(Object element, int columnIndex) {
			return new Color(Display.getDefault(), new RGB(100, 0, 0));
		} 
	}