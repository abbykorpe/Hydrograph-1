package hydrograph.ui.dataviewer.support;




import hydrograph.ui.dataviewer.datastructures.RowData;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public class MyViewerComparator extends ViewerComparator{

	private int propertyIndex;
	  private static final int DESCENDING = 1;
	  private int direction = DESCENDING;

	  public MyViewerComparator() {
	    this.propertyIndex = 0;
	    direction = DESCENDING;
	  }

	  public int getDirection() {
	    return direction == 1 ? SWT.DOWN : SWT.UP;
	  }

	  public void setColumn(int column) {
	    if (column == this.propertyIndex) {
	      // Same column as last sort; toggle the direction
	      direction = 1 - direction;
	    } else {
	      // New column; do an ascending sort
	      this.propertyIndex = column;
	      direction = DESCENDING;
	    }
	  }

	  @Override
	  public int compare(Viewer viewer, Object e1, Object e2) {
		RowData p1 = (RowData) e1;
		RowData p2 = (RowData) e2;
	    int rc = 0;
	    
	    //rc = p1.getColumns().get(propertyIndex).getValue().compareTo(p2.getColumns().get(propertyIndex).getValue());
	    Object object1 = StringConverter.convert(p1.getColumns().get(propertyIndex).getValue(), p1.getColumns().get(propertyIndex).getSchema());
	    Object object2 = StringConverter.convert(p2.getColumns().get(propertyIndex).getValue(), p2.getColumns().get(propertyIndex).getSchema());
	    
	    if(object1 instanceof java.lang.String){
	    	rc =((java.lang.String)object1).compareTo((java.lang.String)object2);
	    }else if(object1 instanceof java.lang.Integer){
	    	rc =((java.lang.Integer)object1).compareTo((java.lang.Integer)object2);
	    }else if(object1 instanceof java.lang.Double){
	    	rc =((java.lang.Double)object1).compareTo((java.lang.Double)object2);
	    }else if(object1 instanceof java.lang.Float){
	    	rc =((java.lang.Float)object1).compareTo((java.lang.Float)object2);
	    }else if(object1 instanceof java.lang.Short){
	    	rc =((java.lang.Short)object1).compareTo((java.lang.Short)object2);
	    }else if(object1 instanceof java.util.Date){
	    	rc =((java.util.Date)object1).compareTo((java.util.Date)object2);
	    }else if(object1 instanceof java.math.BigDecimal){
	    	rc =((java.math.BigDecimal)object1).compareTo((java.math.BigDecimal)object2);
	    }else if(object1 instanceof java.lang.Long){
	    	rc =((java.lang.Long)object1).compareTo((java.lang.Long)object2);
	    }
	   /* switch (propertyIndex) {
	    case 0:
	      rc = p1.getFirstName().compareTo(p2.getFirstName());
	      break;
	    case 1:
	      rc = p1.getLastName().compareTo(p2.getLastName());
	      break;
	    case 2:
	      rc = p1.getGender().compareTo(p2.getGender());
	      break;
	    case 3:
	      if (p1.isMarried() == p2.isMarried()) {
	        rc = 0;
	      } else
	        rc = (p1.isMarried() ? 1 : -1);
	      break;
	    default:
	      rc = 0;
	    }*/
	    // If descending order, flip the direction
	    if (direction == DESCENDING) {
	      rc = -rc;
	    }
	    return rc;
	  }

}
