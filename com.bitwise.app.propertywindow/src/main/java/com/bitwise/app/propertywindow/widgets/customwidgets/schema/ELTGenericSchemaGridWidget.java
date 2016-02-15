package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import com.bitwise.app.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

// TODO: Auto-generated Javadoc
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
	}
	
	@Override
	protected String[] getPropertiesToShow() {
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, SCALE };
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
		editors[3].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 
	}
	//Adding the decorator to show error message when field name same.
	@Override
	protected void setDecorator() {
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);	
		scaleDecorator = WidgetUtility.addDecorator(editors[3].getControl(),Messages.SCALEERROR);
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
