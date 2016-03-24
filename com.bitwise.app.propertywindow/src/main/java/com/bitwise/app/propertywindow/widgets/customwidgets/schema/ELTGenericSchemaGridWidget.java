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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import com.bitwise.app.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The Class ELTGenericSchemaGridWidget.
 * 
 * @author Bitwise
 */
public class ELTGenericSchemaGridWidget extends ELTSchemaGridWidget {

	/**
	 * Instantiates a new ELT generic schema grid widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTGenericSchemaGridWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,propertyDialogButtonBar);
		this.gridRowType = Messages.GENERIC_GRID_ROW;
	}
	
	@Override
	protected String[] getPropertiesToShow() {
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, PRECISION, SCALE, SCALE_TYPE, FIELD_DESCRIPTION };
	}

	@Override
	protected GeneralGridWidgetBuilder getGridWidgetBuilder() {
		return GeneralGridWidgetBuilder.INSTANCE;
	}
	
	protected SchemaGridContentProvider getContentProvider() {
		return new SchemaGridContentProvider();
	}
	
	protected SchemaGridLabelProvider getLableProvider() {
		return new SchemaGridLabelProvider();
	}
	
	protected SchemaGridCellModifier getCellModifier() {
		return new SchemaGridCellModifier(tableViewer);
	}

	@Override
	protected void addValidators() {
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
		editors[4].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 
	}
	//Adding the decorator to show error message when field name same.
	@Override
	protected void setDecorator() {
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);	
		scaleDecorator = WidgetUtility.addDecorator(editors[4].getControl(),Messages.SCALEERROR);
		fieldNameDecorator.setMarginWidth(8);
		scaleDecorator.setMarginWidth(8);
		isFieldNameAlphanumericDecorator.setMarginWidth(8);

	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		schemaFromConnectedLinks();
		super.attachToPropertySubGroup(container);
	}
}
