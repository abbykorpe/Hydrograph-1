package com.bitwise.app.propertywindow.generaterecords.schema;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class GenerateRecordsGridContentProvider.
 * 
 * @author Bitwise
 */
public class GenerateRecordsGridContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List) inputElement).toArray();
	}
	/**
	   * Disposes any created resources
	   */
	@Override
	public void dispose() {
		// Do nothing

	}

	  /**
	   * Called when the input changes
	   */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// Ignore

	}

}
