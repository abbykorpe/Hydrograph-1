package com.bitwise.app.propertywindow.fixedwidthschema;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.GeneralGridWidgetBuilder;
import com.bitwise.app.propertywindow.widgets.utility.DataType;

/**
 * The Class FixedWidthGridCellModifier.
 * 
 * @author Bitwise
 */
public class FixedWidthGridCellModifier implements ICellModifier{
	private Viewer viewer;


	/**
	 * Instantiates a new fixed width grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public FixedWidthGridCellModifier(Viewer viewer) {
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

		FixedWidthGridRow fixedWidthGridRow = (FixedWidthGridRow) element;
		if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
		{
			if(DataType.DATE_CLASS.equals(fixedWidthGridRow.getDataTypeValue()))
				return true;
			else 
				return false; 	
		}
		if (ELTSchemaGridWidget.SCALE.equals(property))
		{
			if(DataType.FLOAT_CLASS.equals(fixedWidthGridRow.getDataTypeValue())||
					DataType.DOUBLE_CLASS.equals(fixedWidthGridRow.getDataTypeValue())||
					DataType.BIGDECIMAL_CLASS.equals(fixedWidthGridRow.getDataTypeValue()))
				return true;
			else 
				return false; 	
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
		FixedWidthGridRow fixedWidthGridRow = (FixedWidthGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			return fixedWidthGridRow.getFieldName();
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			return String.valueOf(fixedWidthGridRow.getDateFormat());
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			return String.valueOf(fixedWidthGridRow.getScale());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property))
			return fixedWidthGridRow.getDataType();
		else if (ELTSchemaGridWidget.LENGTH.equals(property))
			return fixedWidthGridRow.getLength();
		else
			return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();

		FixedWidthGridRow fixedWidthGridRow = (FixedWidthGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			fixedWidthGridRow.setFieldName(((String) value).trim());
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			fixedWidthGridRow.setDateFormat(((String) value).trim());
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			fixedWidthGridRow.setScale(((String) value).trim());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property)) {
			fixedWidthGridRow.setDataType((Integer) value);
			fixedWidthGridRow.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]); 
		} else if (ELTSchemaGridWidget.LENGTH.equals(property)) {
			fixedWidthGridRow.setLength(((String) value).trim());
		}

		resetScale(fixedWidthGridRow, property);

		resetDateFormat(fixedWidthGridRow, property);

		viewer.refresh();
	}

	private void resetScale(FixedWidthGridRow fixedWidthGridRow, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(fixedWidthGridRow.getDataTypeValue())){
			if(DataType.INTEGER_CLASS.equals(fixedWidthGridRow.getDataTypeValue()) 
					||DataType.STRING_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.SHORT_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.BOLLEAN_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.DATE_CLASS.equals(fixedWidthGridRow.getDataTypeValue())){
				fixedWidthGridRow.setScale("");
			}

		}
	}

	private void resetDateFormat(FixedWidthGridRow fixedWidthGridRow, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(fixedWidthGridRow.getDataTypeValue())){
			if(!(DataType.DATE_CLASS.equals(fixedWidthGridRow.getDataTypeValue()))){
				fixedWidthGridRow.setDateFormat("");
			}

		}
	}

}
