package com.bitwise.app.parametergrid.dialog.support;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import com.bitwise.app.parametergrid.dialog.models.ParameterWithFilePath;

public class SearchParameterViewerComparator extends ViewerComparator {
	 private int propertyIndex;
	  private static final int DESCENDING = 1;
	//  private int direction = DESCENDING;

	  public SearchParameterViewerComparator() {
	    this.propertyIndex = 0;
	    //direction = DESCENDING;
	  }

	  /*public int getDirection() {
	    return direction == 1 ? SWT.DOWN : SWT.UP;
	  }*/

	  
	  /*public void setColumn(int column) {
	    if (column == this.propertyIndex) {
	      // Same column as last sort; toggle the direction
	      direction = 1 - direction;
	    } else {
	      // New column; do an ascending sort
	      this.propertyIndex = column;
	      direction = DESCENDING;
	    }
	  }*/

	  @Override
	  public int compare(Viewer viewer, Object e1, Object e2) {
		ParameterWithFilePath p1 = (ParameterWithFilePath) e1;
		ParameterWithFilePath p2 = (ParameterWithFilePath) e2;
	    int rc = 0;
	    switch (propertyIndex) {
	    case 0:
	      rc = p1.getFilePath().getFilePathViewString().compareTo(p2.getFilePath().getFilePathViewString());
	      break;
	    case 1:
	      rc = p1.getParameterName().compareTo(p2.getParameterName());
	      break;
	    case 2:
	      rc = p1.getParameterValue().compareTo(p2.getParameterValue());
	      break;
	    default:
	      rc = 0;
	    }
	    // If descending order, flip the direction
	    /*if (direction == DESCENDING) {
	      rc = -rc;
	    }*/
	    return rc;
	  }
}
