package com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.bitwise.app.common.datastructure.property.LookupMapProperty;

public class LookupLabelProvider implements ITableLabelProvider , ITableColorProvider{

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return new Color(Display.getDefault(), new RGB(255, 255, 230));
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		LookupMapProperty operation = (LookupMapProperty) element;
	    switch (columnIndex) {
	    case 0:
	      return operation.getSource_Field();
	    case 1:
	        return operation.getOutput_Field();

	    }
	    return null;
	}

}
