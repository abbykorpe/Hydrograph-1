package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.SchemaGrid;
import com.bitwise.app.propertywindow.widgets.utility.DataType;


/**
 * @author Bitwise
 * This class represents the cell modifier for the SchemaEditor program
 */

class SchemaGridCellModifier implements ICellModifier {
	private Viewer viewer;

	/**
	 * Instantiates a new schema grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public SchemaGridCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}


	@Override
	public boolean canModify(Object element, String property) {
		// Allow editing of all values
		SchemaGrid schemaGrid = (SchemaGrid) element;
		if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
		{
			if(DataType.DATE_CLASS.equals(schemaGrid.getDataTypeValue()))
				return true;
			else 
				return false; 	
		}
		if (ELTSchemaGridWidget.SCALE.equals(property))
		{
			if(DataType.FLOAT_CLASS.equals(schemaGrid.getDataTypeValue()) 
					||DataType.DOUBLE_CLASS.getValue().equals(schemaGrid.getDataTypeValue())
					||DataType.BIGDECIMAL_CLASS.getValue().equals(schemaGrid.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}
		
		if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
		{
			if(DataType.FLOAT_CLASS.equals(schemaGrid.getDataTypeValue()) 
					||DataType.DOUBLE_CLASS.getValue().equals(schemaGrid.getDataTypeValue())
					||DataType.BIGDECIMAL_CLASS.getValue().equals(schemaGrid.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}
		
		if (ELTSchemaGridWidget.PRECISION.equals(property))
		{
			if(DataType.FLOAT_CLASS.equals(schemaGrid.getDataTypeValue()) 
					||DataType.DOUBLE_CLASS.getValue().equals(schemaGrid.getDataTypeValue())
					||DataType.BIGDECIMAL_CLASS.getValue().equals(schemaGrid.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}

		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		SchemaGrid schemaGrid = (SchemaGrid) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			return schemaGrid.getFieldName();
		else if (ELTSchemaGridWidget.DATATYPE.equals(property))
			return schemaGrid.getDataType();
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			return String.valueOf(schemaGrid.getDateFormat());
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
			return schemaGrid.getPrecision();
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			return String.valueOf(schemaGrid.getScale());
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
			return schemaGrid.getScaleType();
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			return schemaGrid.getDescription();
		
		else
			return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();

		SchemaGrid schemaGrid = (SchemaGrid) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			schemaGrid.setFieldName(((String) value).trim());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property)){
			schemaGrid.setDataType((Integer)value);
			schemaGrid.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]);
		}
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			schemaGrid.setDateFormat( ((String) value).trim()); 
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
			schemaGrid.setPrecision(((String) value).trim()); 
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			schemaGrid.setScale(((String) value).trim());
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property)){
			schemaGrid.setScaleType((Integer)value); 
			schemaGrid.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[(Integer)value]);
		}
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			schemaGrid.setDescription(((String) value).trim());
		

		resetScale(schemaGrid, property);
		resetScaleType(schemaGrid, property);
		resetDateFormat(schemaGrid, property);
		resetPrecision(schemaGrid, property);

		viewer.refresh();
	}

	private void resetScale(SchemaGrid row, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(row.getDataTypeValue())){
			if(DataType.INTEGER_CLASS.equals(row.getDataTypeValue()) 
					||DataType.STRING_CLASS.equals(row.getDataTypeValue())
					||DataType.SHORT_CLASS.equals(row.getDataTypeValue())
					||DataType.BOOLEAN_CLASS.equals(row.getDataTypeValue())
					||DataType.DATE_CLASS.equals(row.getDataTypeValue())){
				row.setScale("");
			}

		}
	}
	
	private void resetScaleType(SchemaGrid row, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(row.getDataTypeValue())){
			if(DataType.INTEGER_CLASS.equals(row.getDataTypeValue()) 
					||DataType.STRING_CLASS.equals(row.getDataTypeValue())
					||DataType.SHORT_CLASS.equals(row.getDataTypeValue())
					||DataType.BOOLEAN_CLASS.equals(row.getDataTypeValue())
					||DataType.DATE_CLASS.equals(row.getDataTypeValue())){
				
				row.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[0]);
			}

		}
	}

	private void resetDateFormat(SchemaGrid row, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(row.getDataTypeValue())){
			if(!(DataType.DATE_CLASS.equals(row.getDataTypeValue()))){
				row.setDateFormat("");
			}

		}
	}
	
	private void resetPrecision(SchemaGrid row, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(row.getDataTypeValue())){
			if(DataType.INTEGER_CLASS.equals(row.getDataTypeValue()) 
					||DataType.STRING_CLASS.equals(row.getDataTypeValue())
					||DataType.SHORT_CLASS.equals(row.getDataTypeValue())
					||DataType.BOOLEAN_CLASS.equals(row.getDataTypeValue())
					||DataType.DATE_CLASS.equals(row.getDataTypeValue())){
				row.setPrecision("");
			}

		}
	}


}