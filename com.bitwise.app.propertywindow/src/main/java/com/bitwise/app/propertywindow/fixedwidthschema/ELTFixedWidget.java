package com.bitwise.app.propertywindow.fixedwidthschema;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
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
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, SCALE, LENGTH };
	}
	
	@Override
	protected FixedWidthGridWidgetBuilder getGridWidgetBuilder() {
		return FixedWidthGridWidgetBuilder.INSTANCE;
	}
	
	protected FixedWidthGridContentProvider getContentProvider() {
		return new FixedWidthGridContentProvider();
	}
	
	protected FixedWidthGridLabelProvider getLableProvider() {
		return new FixedWidthGridLabelProvider();
	}
	
	protected FixedWidthGridCellModifier getCellModifier() {
		return new FixedWidthGridCellModifier(tableViewer);
	}

	@Override
	protected void addValidators() {
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,propertyDialogButtonBar));
		editors[3].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 
		editors[4].setValidator(new ELTCellEditorIsNumericValidator(lengthDecorator,propertyDialogButtonBar)); 
	}
	//Adding the decorator to show error message when field name same.
	@Override
	protected void setDecorator() {
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
		scaleDecorator = WidgetUtility.addDecorator(editors[3].getControl(),Messages.SCALEERROR);
		lengthDecorator = WidgetUtility.addDecorator(editors[4].getControl(),Messages.LENGTHERROR);
	}
	
}