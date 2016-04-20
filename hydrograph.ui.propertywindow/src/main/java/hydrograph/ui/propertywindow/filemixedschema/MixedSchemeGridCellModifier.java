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

package hydrograph.ui.propertywindow.filemixedschema;

import hydrograph.ui.datastructure.property.MixedSchemeGridRow;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.GeneralGridWidgetBuilder;
import hydrograph.ui.propertywindow.widgets.utility.DataType;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

public class MixedSchemeGridCellModifier implements ICellModifier{
	private Viewer viewer;
	
	/**
	 * Instantiates a new fixed width grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public MixedSchemeGridCellModifier(Viewer viewer) {
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

		MixedSchemeGridRow mixedSchemeGridRow = (MixedSchemeGridRow) element;
		if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
		{
			if(DataType.DATE_CLASS.equals(mixedSchemeGridRow.getDataTypeValue()))
				return true;
			else 
				return false; 	
		}
		if (ELTSchemaGridWidget.SCALE.equals(property))
		{
			if(DataType.FLOAT_CLASS.equals(mixedSchemeGridRow.getDataTypeValue())||
					DataType.DOUBLE_CLASS.equals(mixedSchemeGridRow.getDataTypeValue())||
					DataType.BIGDECIMAL_CLASS.equals(mixedSchemeGridRow.getDataTypeValue()))
				return true;
			else 
				return false; 	
		}
		if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
		{
			if(DataType.FLOAT_CLASS.equals(mixedSchemeGridRow.getDataTypeValue()) 
					||DataType.DOUBLE_CLASS.getValue().equals(mixedSchemeGridRow.getDataTypeValue())
					||DataType.BIGDECIMAL_CLASS.getValue().equals(mixedSchemeGridRow.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}
		if (ELTSchemaGridWidget.PRECISION.equals(property))
		{
			if(DataType.FLOAT_CLASS.equals(mixedSchemeGridRow.getDataTypeValue()) 
					||DataType.DOUBLE_CLASS.getValue().equals(mixedSchemeGridRow.getDataTypeValue())
					||DataType.BIGDECIMAL_CLASS.getValue().equals(mixedSchemeGridRow.getDataTypeValue()))
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
		MixedSchemeGridRow mixedSchemeGridRow = (MixedSchemeGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			return mixedSchemeGridRow.getFieldName();
		else if (ELTSchemaGridWidget.DATATYPE.equals(property))
			return mixedSchemeGridRow.getDataType();
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			return String.valueOf(mixedSchemeGridRow.getDateFormat());
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
			return mixedSchemeGridRow.getPrecision();
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			return String.valueOf(mixedSchemeGridRow.getScale());
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
			return mixedSchemeGridRow.getScaleType();
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			return mixedSchemeGridRow.getDescription();
		else if (ELTSchemaGridWidget.LENGTH.equals(property))
			return mixedSchemeGridRow.getLength();
		else if (ELTSchemaGridWidget.DELIMITER.equals(property))
			return mixedSchemeGridRow.getDelimiter();

		else
			return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();

		MixedSchemeGridRow mixedSchemeGridRow = (MixedSchemeGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			mixedSchemeGridRow.setFieldName(((String) value).trim());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property)) {
			mixedSchemeGridRow.setDataType((Integer) value);
			mixedSchemeGridRow.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]); 
		}
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			mixedSchemeGridRow.setDateFormat(((String) value).trim());
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
			mixedSchemeGridRow.setPrecision(((String) value).trim()); 
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			mixedSchemeGridRow.setScale(((String) value).trim());
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property)) {
			mixedSchemeGridRow.setScaleType((Integer) value);
			mixedSchemeGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[(Integer)value]); 
		}
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			mixedSchemeGridRow.setDescription(((String) value).trim());
		else if (ELTSchemaGridWidget.LENGTH.equals(property)) {
			mixedSchemeGridRow.setLength(((String) value).trim());
		}
		else if (ELTSchemaGridWidget.DELIMITER.equals(property)) {
			mixedSchemeGridRow.setDelimiter(((String) value).trim());
		}		

		if (isResetNeeded(mixedSchemeGridRow, property)){
			mixedSchemeGridRow.setScale("");
			mixedSchemeGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[0]);
			mixedSchemeGridRow.setScaleType(0);
			mixedSchemeGridRow.setPrecision("");
		}
		resetDateFormat(mixedSchemeGridRow, property);

		viewer.refresh();
	}
	
	private void resetDateFormat(MixedSchemeGridRow row, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(row.getDataTypeValue())){
			if(!(DataType.DATE_CLASS.equals(row.getDataTypeValue()))){
				row.setDateFormat("");
			}

		}
	}


	private boolean isResetNeeded(MixedSchemeGridRow mixedSchemeGridRow, String property) {
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(mixedSchemeGridRow.getDataTypeValue())){
			if(DataType.INTEGER_CLASS.equals(mixedSchemeGridRow.getDataTypeValue()) 
					||DataType.LONG_CLASS.equals(mixedSchemeGridRow.getDataTypeValue())
					||DataType.STRING_CLASS.equals(mixedSchemeGridRow.getDataTypeValue())
					||DataType.SHORT_CLASS.equals(mixedSchemeGridRow.getDataTypeValue())
					||DataType.BOOLEAN_CLASS.equals(mixedSchemeGridRow.getDataTypeValue())
					||DataType.DATE_CLASS.equals(mixedSchemeGridRow.getDataTypeValue())){
				return true;
			}	
		}
		return false;
	}	
		
	}
