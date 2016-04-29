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

 
package hydrograph.ui.propertywindow.generaterecords.schema;

import java.util.ArrayList;
import java.util.List;

import hydrograph.ui.common.util.ComponentCacheUtil;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.SchemaGridContentProvider;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import hydrograph.ui.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;
import hydrograph.ui.validators.impl.IValidator;



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
	 * @see hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getPropertiesToShow()
	 */
	@Override
	protected String[] getPropertiesToShow() {
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, PRECISION, SCALE, SCALE_TYPE, FIELD_DESCRIPTION, LENGTH, RANGE_FROM, RANGE_TO, DEFAULT_VALUE };
	}

	/* (non-Javadoc)
	 * @see hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getGridWidgetBuilder()
	 */
	@Override
	protected GenerateRecordsGridWidgetBuilder getGridWidgetBuilder() {
		return GenerateRecordsGridWidgetBuilder.INSTANCE;
	}

	/* (non-Javadoc)
	 * @see hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getContentProvider()
	 */
	protected SchemaGridContentProvider getContentProvider() {
		return new SchemaGridContentProvider();
	}

	/* (non-Javadoc)
	 * @see hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getLableProvider()
	 */
	protected GenerateRecordsGridLabelProvider getLableProvider() {
		return new GenerateRecordsGridLabelProvider();
	}

	/* (non-Javadoc)
	 * @see hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#getCellModifier()
	 */
	protected GenerateRecordsGridCellModifier getCellModifier() {
		return new GenerateRecordsGridCellModifier(this,tableViewer);
	}

	/* (non-Javadoc)
	 * @see hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#addValidators()
	 */
	@Override
	protected void addValidators() {
		
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
		editors[4].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 
		editors[7].setValidator(new ELTCellEditorIsNumericValidator(lengthDecorator,propertyDialogButtonBar)); 
		

	}

	
	/* (non-Javadoc)
	 * @see hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#setDecorator()
	 */
	@Override
	protected void setDecorator() {
		
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(), Messages.FIELDNAMEERROR);
		scaleDecorator = WidgetUtility.addDecorator(editors[4].getControl(), Messages.SCALEERROR);
		lengthDecorator = WidgetUtility.addDecorator(editors[7].getControl(), Messages.LENGTHERROR);
		isFieldNameAlphanumericDecorator = WidgetUtility.addDecorator(editors[0].getControl(),
				Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);
	}

	@Override
	public boolean applyValidationRule() {
		return applySchemaValidationRule();
	}

	

	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		attachListener();
		
	}

}