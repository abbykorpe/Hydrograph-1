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

 
package com.bitwise.app.propertywindow.generaterecords.schema;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.SchemaGridContentProvider;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import com.bitwise.app.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;


/**
 * This class is used to configure GenerateRecords Schema Grid Widget.
 * 
 * @author Bitwise
 */
public class GenerateRecordsGridWidget extends ELTSchemaGridWidget {

	public GenerateRecordsGridWidget(PropertyDialogButtonBar propertyDialogButtonBar) {
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		this.gridRowType = Messages.GENERATE_RECORD_GRID_ROW;
	}

	/**
	 * Instantiates a new GenerateRecordsGridWidget.
	 *
	 */
	public GenerateRecordsGridWidget(ComponentConfigrationProperty componentConfigurationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigurationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
		this.gridRowType = Messages.GENERATE_RECORD_GRID_ROW;
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getPropertiesToShow()
	 */
	@Override
	protected String[] getPropertiesToShow() {
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, PRECISION, SCALE, SCALE_TYPE, FIELD_DESCRIPTION, LENGTH, RANGE_FROM, RANGE_TO, DEFAULT_VALUE };
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getGridWidgetBuilder()
	 */
	@Override
	protected GenerateRecordsGridWidgetBuilder getGridWidgetBuilder() {
		return GenerateRecordsGridWidgetBuilder.INSTANCE;
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getContentProvider()
	 */
	protected SchemaGridContentProvider getContentProvider() {
		return new SchemaGridContentProvider();
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getLableProvider()
	 */
	protected GenerateRecordsGridLabelProvider getLableProvider() {
		return new GenerateRecordsGridLabelProvider();
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getCellModifier()
	 */
	protected GenerateRecordsGridCellModifier getCellModifier() {
		return new GenerateRecordsGridCellModifier(tableViewer);
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#addValidators()
	 */
	@Override
	protected void addValidators() {
		
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
		editors[4].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 
		editors[7].setValidator(new ELTCellEditorIsNumericValidator(lengthDecorator,propertyDialogButtonBar)); 
		

	}

	
	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#setDecorator()
	 */
	@Override
	protected void setDecorator() {
		
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(), Messages.FIELDNAMEERROR);
		scaleDecorator = WidgetUtility.addDecorator(editors[4].getControl(), Messages.SCALEERROR);
		lengthDecorator = WidgetUtility.addDecorator(editors[7].getControl(), Messages.LENGTHERROR);
		isFieldNameAlphanumericDecorator = WidgetUtility.addDecorator(editors[0].getControl(),
				Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);
	}

}