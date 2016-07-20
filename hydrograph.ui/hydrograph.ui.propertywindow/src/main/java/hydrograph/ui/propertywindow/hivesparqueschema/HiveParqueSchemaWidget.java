package hydrograph.ui.propertywindow.hivesparqueschema;

import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.SchemaGridContentProvider;
import hydrograph.ui.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class HiveParqueSchemaWidget.
 * 
 * @author Bitwise
 */
public class HiveParqueSchemaWidget extends ELTSchemaGridWidget{

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
	public HiveParqueSchemaWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,propertyDialogButtonBar);
		this.gridRowType = "HiveParquet";
	}
	
	@Override
	protected Map<String, Integer> getPropertiesToShow() {
		Map<String, Integer> columns = new HashMap<>();
		columns.put(FIELDNAME, 0);
		columns.put(DATATYPE, 1);
		columns.put(PRECISION, 2);
		columns.put(SCALE, 3);
		columns.put(SCALE_TYPE, 4);
		columns.put(DATEFORMAT, 5);
		columns.put(FIELD_DESCRIPTION, 6);
		return columns;
		//return new String[]{ FIELDNAME, DATATYPE, PRECISION, SCALE, SCALE_TYPE, DATEFORMAT, FIELD_DESCRIPTION };
	}

	@Override
	protected HiveParqueSchemaWidgetBuilder getGridWidgetBuilder() {
		return HiveParqueSchemaWidgetBuilder.INSTANCE;
	}

	@Override
	protected SchemaGridContentProvider getContentProvider() {
		return new SchemaGridContentProvider();
	}

	@Override
	protected HiveParqueSchemaLabelProvider getLableProvider() {
		return new HiveParqueSchemaLabelProvider();
	}

	@Override
	protected HiveParqueSchemaCellModifier getCellModifier() {
		return new HiveParqueSchemaCellModifier(this,tableViewer);
	}

	@Override
	protected void addValidators() {
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, schemaGridRowList, fieldNameDecorator,isFieldNameAlphanumericDecorator,propertyDialogButtonBar));
	}

	@Override
	protected void setDecorator() {
		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);
		fieldNameDecorator.setMarginWidth(8);
		isFieldNameAlphanumericDecorator.setMarginWidth(8);
	}

	@Override
	public boolean isWidgetValid() {
		return applySchemaValidationRule();
	}

	@Override
	public void addModifyListener(Property property,
			ArrayList<AbstractWidget> widgetList) {
		attachListener();
	}

}