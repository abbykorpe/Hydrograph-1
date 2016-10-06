/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hydrograph.ui.propertywindow.sqlschema;

import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.GeneralGridWidgetBuilder;
import hydrograph.ui.propertywindow.widgets.utility.DataType;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;
/**
 * The Class SQLSchemaGridCellModifier.
 * 
 * @author Bitwise
 */
public class SQLSchemaGridCellModifier implements ICellModifier{
	private Viewer viewer;
    private SQLSchemaWidget sqlSchemaWidget;

	/**
	 * Instantiates a new fixed width grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public SQLSchemaGridCellModifier(SQLSchemaWidget sqlSchemaWidget,Viewer viewer) {
		this.viewer = viewer;
		this.sqlSchemaWidget=sqlSchemaWidget;
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

		FixedWidthGridRow sqlGridRow = (FixedWidthGridRow) element;
		if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
		{
			if(DataType.DATE_CLASS.equals(sqlGridRow.getDataTypeValue()))
				return true;
			else 
				return false; 	
		}
		if (ELTSchemaGridWidget.SCALE.equals(property))
		{
			if(DataType.BIGDECIMAL_CLASS.equals(sqlGridRow.getDataTypeValue()))
				return true;
			else 
				return false; 	
		}
		if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
		{
			if(DataType.BIGDECIMAL_CLASS.getValue().equals(sqlGridRow.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}
		if (ELTSchemaGridWidget.PRECISION.equals(property))
		{
			if(DataType.BIGDECIMAL_CLASS.getValue().equals(sqlGridRow.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
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
		FixedWidthGridRow sqlGridRow = (FixedWidthGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			return sqlGridRow.getFieldName();
		else if (ELTSchemaGridWidget.DATATYPE.equals(property))
			return sqlGridRow.getDataType();
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			return String.valueOf(sqlGridRow.getDateFormat());
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
			return sqlGridRow.getPrecision();
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			return String.valueOf(sqlGridRow.getScale());
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
			return sqlGridRow.getScaleType();
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			return sqlGridRow.getDescription();
		else if (ELTSchemaGridWidget.COL_DEF.equals(property))
			return sqlGridRow.getColumnDefinition();

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
		else if (ELTSchemaGridWidget.DATATYPE.equals(property)) {
			if(StringUtils.equals(DataType.BIGDECIMAL_CLASS.getValue(), GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]))
			{
				fixedWidthGridRow.setScaleType(2); 
				fixedWidthGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[2]);
				fixedWidthGridRow.setScale(String.valueOf(0));
			}
			fixedWidthGridRow.setDataType((Integer) value);
			fixedWidthGridRow.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]); 
		}
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			fixedWidthGridRow.setDateFormat(((String) value).trim());
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
			fixedWidthGridRow.setPrecision(((String) value).trim()); 
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			fixedWidthGridRow.setScale(((String) value).trim());
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property)) {
			fixedWidthGridRow.setScaleType((Integer) value);
			fixedWidthGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[(Integer)value]); 
		}
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			fixedWidthGridRow.setDescription(((String) value).trim());
		else if (ELTSchemaGridWidget.COL_DEF.equals(property)) {
			fixedWidthGridRow.setColumnDefinition(((String) value).trim());
		}

		if (isResetNeeded(fixedWidthGridRow, property)){
			fixedWidthGridRow.setScale("");
			fixedWidthGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[0]);
			fixedWidthGridRow.setScaleType(0);
			fixedWidthGridRow.setPrecision("");
		}
		resetDateFormat(fixedWidthGridRow, property);
		viewer.refresh();
		sqlSchemaWidget.highlightInvalidRowWithRedColor(fixedWidthGridRow);
		sqlSchemaWidget.showHideErrorSymbol(sqlSchemaWidget.isWidgetValid());
	}
	
	private void resetDateFormat(FixedWidthGridRow row, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(row.getDataTypeValue())){
			if(!(DataType.DATE_CLASS.equals(row.getDataTypeValue()))){
				row.setDateFormat("");
			}

		}
	}


	private boolean isResetNeeded(FixedWidthGridRow fixedWidthGridRow, String property) {
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(fixedWidthGridRow.getDataTypeValue())){
			if(DataType.INTEGER_CLASS.equals(fixedWidthGridRow.getDataTypeValue()) 
					||DataType.LONG_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.STRING_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.SHORT_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.BOOLEAN_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.FLOAT_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
 					||DataType.DOUBLE_CLASS.equals(fixedWidthGridRow.getDataTypeValue())
					||DataType.DATE_CLASS.equals(fixedWidthGridRow.getDataTypeValue())){
				return true;
			}	
		}
		return false;
	}

}
