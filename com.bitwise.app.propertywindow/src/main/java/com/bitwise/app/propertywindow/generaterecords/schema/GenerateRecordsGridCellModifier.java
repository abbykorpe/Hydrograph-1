package com.bitwise.app.propertywindow.generaterecords.schema;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.GenerateRecordSchemaGridRow;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.GeneralGridWidgetBuilder;

/**
 * This class is used for cell modification of GenerateRecords Schema Grid.
 * 
 * @author Bitwise
 */
public class GenerateRecordsGridCellModifier implements ICellModifier {
	private Viewer viewer;

	/**
	 * Instantiates a new Generate Records Grid CellModifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public GenerateRecordsGridCellModifier(Viewer viewer) {
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

		GenerateRecordSchemaGridRow generateRecordsSchemaGridRow = (GenerateRecordSchemaGridRow) element;
		if (ELTSchemaGridWidget.DATEFORMAT.equals(property)) {
			if (generateRecordsSchemaGridRow.getDataTypeValue().equalsIgnoreCase("java.util.date"))
				return true;
			else
				return false;
		}
		if (ELTSchemaGridWidget.SCALE.equals(property)) {
			if (generateRecordsSchemaGridRow.getDataTypeValue().equalsIgnoreCase("java.lang.Float")
					|| generateRecordsSchemaGridRow.getDataTypeValue().equalsIgnoreCase("java.lang.Double")
					|| generateRecordsSchemaGridRow.getDataTypeValue().equalsIgnoreCase("java.math.BigDecimal"))
				return true;
			else
				return false;
		}
		if (ELTSchemaGridWidget.RANGE_FROM.equals(property) || ELTSchemaGridWidget.RANGE_TO.equals(property)) {
			if (generateRecordsSchemaGridRow.getDataTypeValue().equalsIgnoreCase("java.lang.String")
					|| generateRecordsSchemaGridRow.getDataTypeValue().equalsIgnoreCase("java.lang.Boolean"))
				return false;
			else
				return true;
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
		GenerateRecordSchemaGridRow p = (GenerateRecordSchemaGridRow) element;
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
		else if (ELTSchemaGridWidget.RANGE_FROM.equals(property))
			return p.getRangeFrom();
		else if (ELTSchemaGridWidget.RANGE_TO.equals(property))
			return p.getRangeTo();
		else if (ELTSchemaGridWidget.DEFAULT_VALUE.equals(property))
			return p.getDefaultValue();
		else
			return null;
	}

	/*
	 * Modifies the GenerateRecordSchemaGridRow object by cell data
	 * 
	 * @param element current item
	 * 
	 * @param property property to modify
	 * 
	 * @param value modified value
	 */
	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();

		GenerateRecordSchemaGridRow p = (GenerateRecordSchemaGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			p.setFieldName(((String) value).trim());
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			p.setDateFormat(((String) value).trim());
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			p.setScale(((String) value).trim());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property)) {
			p.setDataType((Integer) value);
			p.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer) value]);
		} else if (ELTSchemaGridWidget.LENGTH.equals(property)) {
			p.setLength(((String) value).trim());
		} else if (ELTSchemaGridWidget.RANGE_FROM.equals(property)) {
			p.setRangeFrom(((String) value).trim());
		} else if (ELTSchemaGridWidget.RANGE_TO.equals(property)) {
			p.setRangeTo(((String) value).trim());
		} else if (ELTSchemaGridWidget.DEFAULT_VALUE.equals(property)) {
			p.setDefaultValue(((String) value).trim());
		}

		if (ELTSchemaGridWidget.DATATYPE.equals(property) && p.getDataTypeValue() != null) {
			if (p.getDataTypeValue().equalsIgnoreCase("integer")
					|| p.getDataTypeValue().equalsIgnoreCase("java.lang.Integer")
					|| p.getDataTypeValue().equalsIgnoreCase("string")
					|| p.getDataTypeValue().equalsIgnoreCase("java.lang.String")
					|| p.getDataTypeValue().equalsIgnoreCase("short")
					|| p.getDataTypeValue().equalsIgnoreCase("java.lang.Short")
					|| p.getDataTypeValue().equalsIgnoreCase("boolean")
					|| p.getDataTypeValue().equalsIgnoreCase("java.lang.Boolean")
					|| p.getDataTypeValue().equalsIgnoreCase("date")
					|| p.getDataTypeValue().equalsIgnoreCase("java.util.Date")) {
				p.setScale("");
			}

		}

		viewer.refresh();
	}

}
