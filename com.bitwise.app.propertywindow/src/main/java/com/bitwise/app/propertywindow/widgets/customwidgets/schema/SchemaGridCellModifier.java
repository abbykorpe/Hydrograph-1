package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.SchemaGrid;


/**
 * @author Bitwise
 * This class represents the cell modifier for the SchemaEditor program
 */

class SchemaGridCellModifier implements ICellModifier {
  private Viewer viewer;

  /**
	 * Instantiates a new schema grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
  public SchemaGridCellModifier(Viewer viewer) {
    this.viewer = viewer;
  }
 
  
  @Override
  public boolean canModify(Object element, String property) {
    // Allow editing of all values
	  SchemaGrid p1 = (SchemaGrid) element;
	  if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
	      {
		    if(p1.getDataTypeValue().equalsIgnoreCase("java.util.date"))
		    return true;
		    else 
		   return false; 	
	      }
	  if (ELTSchemaGridWidget.SCALE.equals(property))
      {
	    if(p1.getDataTypeValue().equalsIgnoreCase("java.lang.Float") 
	    		||p1.getDataTypeValue().equalsIgnoreCase("java.lang.Double")
	    		||p1.getDataTypeValue().equalsIgnoreCase("java.math.BigDecimal"))
	    	return true;
	    else {
	    	return false; 	
	    }
	    	
      }
	  
    return true;
  }

  @Override
  public Object getValue(Object element, String property) {
    SchemaGrid p = (SchemaGrid) element;
    if (ELTSchemaGridWidget.FIELDNAME.equals(property))
      return p.getFieldName();
    else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
      return String.valueOf(p.getDateFormat());
    else if (ELTSchemaGridWidget.SCALE.equals(property))
    	return String.valueOf(p.getScale());
    else if (ELTSchemaGridWidget.DATATYPE.equals(property))
      return p.getDataType();
    else
      return null;
  }

  @Override
  public void modify(Object element, String property, Object value) {
    if (element instanceof Item)
      element = ((Item) element).getData();
 
    SchemaGrid p = (SchemaGrid) element;
    if (ELTSchemaGridWidget.FIELDNAME.equals(property))
      p.setFieldName(((String) value).trim());
    else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
      p.setDateFormat( ((String) value).trim()); 
    else if (ELTSchemaGridWidget.SCALE.equals(property))
        p.setScale(((String) value).trim()); 
    else if (ELTSchemaGridWidget.DATATYPE.equals(property)){
      p.setDataType((Integer)value);
      p.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]);
    }
    
    resetScale(p, property);
    
    resetDateFormat(p, property);
    
    viewer.refresh();
  }
  
  private void resetScale(SchemaGrid row, String property){
	  if(ELTSchemaGridWidget.DATATYPE.equals(property) && row.getDataTypeValue()!=null){
		  if(row.getDataTypeValue().equalsIgnoreCase("integer")||
				  row.getDataTypeValue().equalsIgnoreCase("java.lang.Integer") 
				  ||row.getDataTypeValue().equalsIgnoreCase("string")
				  ||row.getDataTypeValue().equalsIgnoreCase("java.lang.String")
				  ||row.getDataTypeValue().equalsIgnoreCase("short")
				  ||row.getDataTypeValue().equalsIgnoreCase("java.lang.Short")
				  ||row.getDataTypeValue().equalsIgnoreCase("boolean")
				  ||row.getDataTypeValue().equalsIgnoreCase("java.lang.Boolean")
				  ||row.getDataTypeValue().equalsIgnoreCase("date")
				  ||row.getDataTypeValue().equalsIgnoreCase("java.util.Date")){
			  row.setScale("");
		  }
			 
	  }
  }
  
  private void resetDateFormat(SchemaGrid row, String property){
	  if(ELTSchemaGridWidget.DATATYPE.equals(property) && row.getDataTypeValue()!=null){
		  if(!(row.getDataTypeValue().equalsIgnoreCase("date")
				  ||row.getDataTypeValue().equalsIgnoreCase("java.util.Date"))){
			  row.setDateFormat("");
		  }
			 
	  }
  }
  
  
}