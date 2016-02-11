package com.bitwise.app.propertywindow.generaterecords.schema;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.bitwise.app.common.datastructure.property.GenerateRecordSchemaGridRow;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

/**
 * The Class GenerateRecordsGridLabelProvider.
 * 
 * @author Bitwise
 */
public class GenerateRecordsGridLabelProvider implements ITableLabelProvider, ITableColorProvider {

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
		GenerateRecordSchemaGridRow generateRecordsSchemaGridRow = (GenerateRecordSchemaGridRow) element;
		switch (columnIndex) {
		case 0:
			return generateRecordsSchemaGridRow.getFieldName();
		case 1:
			return GridWidgetCommonBuilder.getDataTypeKey()[generateRecordsSchemaGridRow.getDataType().intValue()];
		case 2:
			return generateRecordsSchemaGridRow.getDateFormat();
		case 3:
			return generateRecordsSchemaGridRow.getScale();
		case 4:
			return generateRecordsSchemaGridRow.getLength().toString();
		case 5:
			return generateRecordsSchemaGridRow.getRangeFrom().toString();
		case 6:
			return generateRecordsSchemaGridRow.getRangeTo().toString();
		case 7:
			return generateRecordsSchemaGridRow.getDefaultValue().toString();
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
		return;
	}

	/**
	 * Disposes any created resources
	 */
	public void dispose() {
		return;
	}

	/**
	 * Returns whether altering this property on this element will affect the label
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
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
	 */
	@Override
	public Color getBackground(Object element, int columnIndex) {

		return new Color(Display.getDefault(), new RGB(255, 255, 230));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
	 */
	@Override
	public Color getForeground(Object element, int columnIndex) {
		return new Color(Display.getDefault(), new RGB(100, 0, 0));
	}
}