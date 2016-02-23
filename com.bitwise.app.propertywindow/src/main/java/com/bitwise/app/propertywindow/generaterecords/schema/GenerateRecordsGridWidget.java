package com.bitwise.app.propertywindow.generaterecords.schema;

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
		this.gridRowType = Messages.GENERATE_RECORD_GRIDROW;
	}

	/**
	 * Instantiates a new GenerateRecordsGridWidget.
	 *
	 */
	public GenerateRecordsGridWidget(ComponentConfigrationProperty componentConfigurationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigurationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
		this.gridRowType = Messages.GENERATE_RECORD_GRIDROW;
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
		editors[8].setValidator(new ELTCellEditorIsNumericValidator(rangeFromDecorator, propertyDialogButtonBar));
		editors[9].setValidator(new ELTCellEditorIsNumericValidator(rangeToDecorator, propertyDialogButtonBar));

	}

	
	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget#setDecorator()
	 */
	@Override
	protected void setDecorator() {
		
//		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
//		scaleDecorator = WidgetUtility.addDecorator(editors[4].getControl(),Messages.SCALEERROR);
//		lengthDecorator = WidgetUtility.addDecorator(editors[7].getControl(),Messages.LENGTHERROR);
//		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);
//		fieldNameDecorator.setMarginWidth(8);
//		scaleDecorator.setMarginWidth(8);
//		lengthDecorator.setMarginWidth(8);
//		isFieldNameAlphanumericDecorator.setMarginWidth(8);
		
		//----------
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(), Messages.FIELDNAMEERROR);
		scaleDecorator = WidgetUtility.addDecorator(editors[4].getControl(), Messages.SCALEERROR);
		lengthDecorator = WidgetUtility.addDecorator(editors[7].getControl(), Messages.LENGTHERROR);
		rangeFromDecorator = WidgetUtility.addDecorator(editors[8].getControl(), Messages.LENGTHERROR);
		rangeToDecorator = WidgetUtility.addDecorator(editors[9].getControl(), Messages.LENGTHERROR);
		isFieldNameAlphanumericDecorator = WidgetUtility.addDecorator(editors[0].getControl(),
				Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);
	}

}