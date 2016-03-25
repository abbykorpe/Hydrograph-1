package com.bitwise.app.parametergrid.dialog.support;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.bitwise.app.parametergrid.dialog.models.ParameterWithFilePath;

public class SearchParameterViewerFilter extends ViewerFilter  {
	 private String searchString;

	  public void setSearchText(String s) {
	    // ensure that the value can be used for matching 
	    this.searchString = ".*" + s + ".*";
	  }

	  @Override
	  public boolean select(Viewer viewer, 
	      Object parentElement, 
	      Object element) {
	    if (searchString == null || searchString.length() == 0) {
	      return true;
	    }
	    ParameterWithFilePath p = (ParameterWithFilePath) element;
	    
	    if (p.getFilePath().getFilePathViewString().matches(searchString)) {
		      return true;
		 }
	    
	    if (p.getParameterName().matches(searchString)) {
	      return true;
	    }
	    if (p.getParameterValue().matches(searchString)) {
	      return true;
	    }

	    return false;
	  }
}
