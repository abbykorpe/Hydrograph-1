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

import java.util.ArrayList;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.SchemaGridContentProvider;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTCellEditorIsEmptyValidator;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import hydrograph.ui.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

/**
 * The Class ELTMixedScheme.
 * 
 * @author Bitwise
 */
public class ELTMixedSchemeWidget extends ELTSchemaGridWidget {
	
	
	public ELTMixedSchemeWidget(PropertyDialogButtonBar propertyDialogButtonBar) {
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		this.gridRowType = Messages.MIXEDSCHEME_GRID_ROW;
	}
	
	/**
	 * Instantiates a new ELT Mixed scheme.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTMixedSchemeWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
		this.gridRowType = Messages.MIXEDSCHEME_GRID_ROW;
	}	
	

	@Override
	protected String[] getPropertiesToShow() {
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, PRECISION, SCALE, SCALE_TYPE, FIELD_DESCRIPTION, LENGTH, DELIMITER};
	}

	@Override
	protected MixedSchemeGridWidgetBuilder getGridWidgetBuilder() {
		return MixedSchemeGridWidgetBuilder.INSTANCE;
	}

	@Override
	protected SchemaGridContentProvider getContentProvider() {
		return new SchemaGridContentProvider();
	}

	@Override
	protected MixedSchemeGridLabelProvider getLableProvider() {
		return new MixedSchemeGridLabelProvider();
	}

	@Override
	protected MixedSchemeGridCellModifier getCellModifier() {
		return new MixedSchemeGridCellModifier(this,tableViewer);
	}

	@Override
	protected void addValidators() {
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
		editors[2].setValidator(new ELTCellEditorIsEmptyValidator(fieldEmptyDecorator));
		editors[3].setValidator(new ELTCellEditorIsNumericValidator(precisionDecorator)); 
		editors[4].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator)); 
		editors[7].setValidator(new ELTCellEditorIsNumericValidator(lengthDecorator)); 
	}

	@Override
	protected void setDecorator() {
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
		fieldEmptyDecorator = WidgetUtility.addDecorator(editors[2].getControl(), Messages.EMPTYFIELDMESSAGE);
		precisionDecorator = WidgetUtility.addDecorator(editors[3].getControl(),Messages.SCALEERROR);
		scaleDecorator = WidgetUtility.addDecorator(editors[4].getControl(),Messages.SCALEERROR);
		lengthDecorator = WidgetUtility.addDecorator(editors[7].getControl(),Messages.LENGTHERROR);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);
		fieldNameDecorator.setMarginWidth(8);
		scaleDecorator.setMarginWidth(8);
		lengthDecorator.setMarginWidth(8);
		isFieldNameAlphanumericDecorator.setMarginWidth(8);

	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		super.attachToPropertySubGroup(container);
	}

	@Override
	public boolean isWidgetValid() {
		return applySchemaValidationRule();
	}

	

	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		attachListener();
	}
}
