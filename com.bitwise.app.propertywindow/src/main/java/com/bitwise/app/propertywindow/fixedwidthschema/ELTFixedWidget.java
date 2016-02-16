package com.bitwise.app.propertywindow.fixedwidthschema;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.SchemaGridContentProvider;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import com.bitwise.app.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class ELTFixedWidget.
 * 
 * @author Bitwise
 */
public class ELTFixedWidget extends ELTSchemaGridWidget{

	
	public ELTFixedWidget(PropertyDialogButtonBar propertyDialogButtonBar) {
		this.propertyDialogButtonBar=propertyDialogButtonBar;
		}
	/**
	 * Instantiates a new ELT fixed widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTFixedWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
	}

	@Override
	protected String[] getPropertiesToShow() {
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, PRECISION, SCALE, SCALE_TYPE, FIELD_DESCRIPTION, LENGTH };
	}
	
	@Override
	protected FixedWidthGridWidgetBuilder getGridWidgetBuilder() {
		return FixedWidthGridWidgetBuilder.INSTANCE;
	}
	
	protected SchemaGridContentProvider getContentProvider() {
		return new SchemaGridContentProvider();
	}
	
	protected FixedWidthGridLabelProvider getLableProvider() {
		return new FixedWidthGridLabelProvider();
	}
	
	protected FixedWidthGridCellModifier getCellModifier() {
		return new FixedWidthGridCellModifier(tableViewer);
	}

	@Override
	protected void addValidators() {
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
		editors[4].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 
		editors[7].setValidator(new ELTCellEditorIsNumericValidator(lengthDecorator,propertyDialogButtonBar)); 
	}
	//Adding the decorator to show error message when field name same.
	@Override
	protected void setDecorator() {
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
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
		if (!ELTJoinFixedWidthSchemaWidget.class.isAssignableFrom(this.getClass()))
			schemaFromConnectedLinks();
		super.attachToPropertySubGroup(container);
	}
}