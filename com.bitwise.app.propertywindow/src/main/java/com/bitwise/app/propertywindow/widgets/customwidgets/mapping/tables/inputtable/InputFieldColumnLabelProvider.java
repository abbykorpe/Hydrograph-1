package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable;

import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TableItem;

import com.bitwise.app.common.datastructure.property.mapping.InputField;

public class InputFieldColumnLabelProvider extends ColumnLabelProvider{
	
	@Override
	public String getText(Object element) {
		return ((InputField)element).getFieldName();
	}			
		
	@Override
	  public String getToolTipText(Object element) {
		if(((InputField)element).getErrorObject().isHasError()){
			return ((InputField)element).getErrorObject().getErrorMessage();
		}
		return null;
	  }

	  @Override
	  public Point getToolTipShift(Object object) {
	    return new Point(5, 5);
	  }

	  @Override
	  public int getToolTipDisplayDelayTime(Object object) {
	    return 100; // msec
	  }

	  @Override
	  public int getToolTipTimeDisplayed(Object object) {
	    return 5000; // msec
	  }

	  @Override
	public void update(ViewerCell cell) {
		  TableItem item = (TableItem) cell.getItem();
		  
		  Pattern pattern = Pattern.compile("^[a-zA-Z0-9 _]*$");
			
			if (!pattern.matcher(((InputField)cell.getElement()).getFieldName()).matches()) {
				item.setBackground(cell.getControl().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
				  item.setForeground(cell.getControl().getDisplay().getSystemColor(SWT.COLOR_RED));
				  ((InputField)cell.getElement()).getErrorObject().setErrorMessage("Input field should match regular expression- \"^[a-zA-Z0-9_]*$\" ");
				  ((InputField)cell.getElement()).getErrorObject().setHasError(true);
		    }else{
		    	item.setBackground(cell.getControl().getDisplay().getSystemColor(SWT.COLOR_WHITE));
				  item.setForeground(cell.getControl().getDisplay().getSystemColor(SWT.COLOR_BLACK));
				  ((InputField)cell.getElement()).getErrorObject().setHasError(false);
		    }
		super.update(cell);
	}

	
}
