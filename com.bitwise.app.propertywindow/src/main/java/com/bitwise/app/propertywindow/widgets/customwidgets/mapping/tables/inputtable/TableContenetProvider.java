package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class TableContenetProvider implements IStructuredContentProvider{

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		ArrayContentProvider.getInstance();
		if(inputElement instanceof Collection){
			return ((Collection) inputElement).toArray();
		}
		return null;
	}

}
