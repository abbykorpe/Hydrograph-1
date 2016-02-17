package com.bitwise.app.propertywindow.generaterecords.schema;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
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
public class ELTGenerateRecordsGridWidget extends ELTSchemaGridWidget{

	
	public ELTGenerateRecordsGridWidget(PropertyDialogButtonBar propertyDialogButtonBar) {
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
	public ELTGenerateRecordsGridWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
	}

	@Override
	protected String[] getPropertiesToShow() {
		return new String[]{ FIELDNAME, DATATYPE, DATEFORMAT, SCALE, LENGTH,RANGE_FROM,RANGE_TO,DEFAULT_VALUE };
	}
	
	@Override
	protected GenerateRecordsGridWidgetBuilder getGridWidgetBuilder() {
		return GenerateRecordsGridWidgetBuilder.INSTANCE;
	}
	
	protected GenerateRecordsGridContentProvider getContentProvider() {
		return new GenerateRecordsGridContentProvider();
	}
	
	protected GenerateRecordsGridLabelProvider getLableProvider() {
		return new GenerateRecordsGridLabelProvider();
	}
	
	protected GenerateRecordsGridCellModifier getCellModifier() {
		return new GenerateRecordsGridCellModifier(tableViewer);
	}

	@Override
	protected void addValidators() {
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
		editors[3].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 
		editors[4].setValidator(new ELTCellEditorIsNumericValidator(lengthDecorator,propertyDialogButtonBar));
//		editors[5].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, rangeFromDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
//		editors[6].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, rangeToDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
		
	}
	//Adding the decorator to show error message when field name same.
	@Override
	protected void setDecorator() {
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
		scaleDecorator = WidgetUtility.addDecorator(editors[3].getControl(),Messages.SCALEERROR);
		lengthDecorator = WidgetUtility.addDecorator(editors[4].getControl(),Messages.LENGTHERROR);
//		lengthDecorator = WidgetUtility.addDecorator(editors[5].getControl(),Messages.LENGTHERROR);
//		lengthDecorator = WidgetUtility.addDecorator(editors[6].getControl(),Messages.LENGTHERROR);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);	
	}

	
}