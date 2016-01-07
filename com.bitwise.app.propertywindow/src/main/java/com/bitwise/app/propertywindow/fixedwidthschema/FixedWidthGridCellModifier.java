package com.bitwise.app.propertywindow.fixedwidthschema;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.GeneralGridWidgetBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class FixedWidthGridCellModifier.
 * 
 * @author Bitwise
 */
public class FixedWidthGridCellModifier implements ICellModifier{
   private Viewer viewer;
   
   
	/**
	 * Instantiates a new fixed width grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public FixedWidthGridCellModifier(Viewer viewer) {
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
	@Override
	public boolean canModify(Object element, String property) {
	
		FixedWidthGridRow p1 = (FixedWidthGridRow) element;
		  if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
		      {
			    if(p1.getDataTypeValue().equalsIgnoreCase("java.util.date"))
			    return true;
			    else 
			   return false; 	
		      }
		  if (ELTSchemaGridWidget.SCALE.equals(property))
	      {
		    if(p1.getDataTypeValue().equalsIgnoreCase("java.lang.Float") ||p1.getDataTypeValue().equalsIgnoreCase("java.lang.Double")||p1.getDataTypeValue().equalsIgnoreCase("java.math.BigDecimal"))
		    return true;
		    else 
		   return false; 	
	      }
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
	@Override
	public Object getValue(Object element, String property) {
		FixedWidthGridRow p = (FixedWidthGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			return p.getFieldName();
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			return String.valueOf(p.getDateFormat());
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			return String.valueOf(p.getScale());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property))
			return p.getDataType();
		else if (ELTSchemaGridWidget.LENGTH.equals(property))
			return p.getLength();
		else
			return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();

		FixedWidthGridRow p = (FixedWidthGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			p.setFieldName(((String) value).trim());
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			p.setDateFormat(((String) value).trim());
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			p.setScale(((String) value).trim());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property)) {
			p.setDataType((Integer) value);
			p.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]); 
		} else if (ELTSchemaGridWidget.LENGTH.equals(property)) {
			p.setLength(((String) value).trim());
		}
		viewer.refresh();
	}

}
