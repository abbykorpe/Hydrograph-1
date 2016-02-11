package com.bitwise.app.engine.ui.helper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.SchemaGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;

/**
 * The class ConverterUiHelper
 * 
 * @author Bitwise a
 * 
 *         This is a helper class for converter implementation. Contains the helper methods for conversion.
 */
public class ConverterUiHelper {

	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected Component uIComponent = null;
	protected String componentName = null;

	/**
	 * Instantiate ConverterUiHelper
	 * 
	 * @param component
	 */
	public ConverterUiHelper(Component component) {
		this.uIComponent = component;
		this.properties = component.getProperties();
		this.componentName = (String) properties.get(Constants.PARAM_NAME);
	}

	/**
	 * Create Fixed-Width-Schema for the components.
	 * 
	 * @param record
	 * @return FixedWidthGridRow, this object is responsible for displaying fixed-width schema on property window
	 */
	public FixedWidthGridRow getFixedWidthSchema(Object record) {
		if ((TypeExternalSchema.class).isAssignableFrom(record.getClass())) {
			return null;
		} else if ((TypeBaseField.class).isAssignableFrom(record.getClass())) {
			FixedWidthGridRow fixedWidthGrid = new FixedWidthGridRow();
			TypeBaseField typeBaseField = (TypeBaseField) record;
			fixedWidthGrid.setDataTypeValue(getStringValue(typeBaseField.getType().value()));
			fixedWidthGrid.setDateFormat(getStringValue(typeBaseField.getFormat()));
			fixedWidthGrid.setFieldName(getStringValue(typeBaseField.getName()));
			fixedWidthGrid.setScale(getStringValue(String.valueOf(typeBaseField.getScale())));
			fixedWidthGrid.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(typeBaseField.getType().value()));
			fixedWidthGrid.setLength(getStringValue(getLength(typeBaseField)));
			return fixedWidthGrid;
		}
		return null;
	}

	/**
	 * Create Schema for the components.
	 * 
	 * @param record
	 * @return SchemaGrid, this object is responsible for displaying schema on property window
	 */
	public SchemaGrid getSchema(Object record) {
		if ((TypeExternalSchema.class).isAssignableFrom(record.getClass())) {
			return null;
		} else if ((TypeBaseField.class).isAssignableFrom(record.getClass())) {
			SchemaGrid schemaGridData = new SchemaGrid();
			TypeBaseField typeBaseField = (TypeBaseField) record;
			schemaGridData.setDataTypeValue(getStringValue(typeBaseField.getType().value()));
			schemaGridData.setDateFormat(getStringValue(typeBaseField.getFormat()));
			schemaGridData.setFieldName(getStringValue(typeBaseField.getName()));
			schemaGridData.setScale(getStringValue(String.valueOf(typeBaseField.getScale())));
			schemaGridData.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(typeBaseField.getType().value()));
			return schemaGridData;
		}
		return null;
	}

	/**
	 * Fetches value of Qname having name length.
	 * 
	 * @param typeBaseField
	 * @return String, length value
	 */
	private String getLength(TypeBaseField typeBaseField) {
		for (Entry<QName, String> entry : typeBaseField.getOtherAttributes().entrySet()) {
			if (entry.getKey().toString().equals(Constants.LENGTH_QNAME))
				return entry.getValue();
		}
		return null;
	}

	/**
	 * Create empty string for null values.
	 * 
	 * @param value
	 * @return
	 */
	public String getStringValue(String value) {
		String emptyString = "";
		if (value == null || value.equals("null"))
			return emptyString;
		else
			return value;
	}

	/**
	 * Fetches value of given Qname from component's schema
	 * 
	 * @param typeBaseField
	 *            engine component's schema object
	 * @param qname
	 *            qualified name of an schema field
	 * @return String value of given Qname
	 */
	public String getQnameValue(TypeBaseField typeBaseField, String qname) {
		for (Entry<QName, String> entry : typeBaseField.getOtherAttributes().entrySet()) {
			if (entry.getKey().toString().equals(qname))
				return entry.getValue();
		}
		return null;
	}
}
