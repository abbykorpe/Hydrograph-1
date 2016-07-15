package hydrograph.ui.propertywindow.widgets.customwidgets;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.InputHivePartitionColumn;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.hiveInput.dialog.FieldDialogWithAddValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

public class SingleColumnWidgetHiveInput extends SingleColumnWidget {
	
	private Map<String,List<InputHivePartitionColumn>> fieldMap;

	public SingleColumnWidgetHiveInput(
			ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps,
			PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
	}



	protected void intialize(ComponentConfigrationProperty componentConfigProp) {
		propertyName = componentConfigProp.getPropertyName();
		setProperties(componentConfigProp.getPropertyName(), componentConfigProp.getPropertyValue());
	}

	
	
	@Override
	protected List<String> getPropagatedSchema() {
		List<String> list = new ArrayList<String>();
		Schema schema = (Schema) getComponent().getProperties().get(
				Constants.SCHEMA_PROPERTY_NAME);
		if (schema != null && schema.getGridRow() != null) {
			List<GridRow> gridRows = schema.getGridRow();
			if (gridRows != null) {
				for (GridRow gridRow : gridRows) {
					list.add(gridRow.getFieldName());
				}
			}
		}
		return list;
	}

	@Override
	protected void onDoubleClick() {
		
		FieldDialogWithAddValue fieldDialog=new FieldDialogWithAddValue(Display.getCurrent().getActiveShell(), propertyDialogButtonBar);
		fieldDialog.setComponentName(gridConfig.getComponentName());
		if (getProperties().get(propertyName) == null) {
			setProperties(propertyName, new LinkedHashMap<>());
		}
		fieldDialog.setFieldDialogRuntimeProperties(fieldMap);
		fieldDialog.setSourceFieldsFromPropagatedSchema(getPropagatedSchema());
		fieldDialog.open();
		setProperties(propertyName,fieldDialog.getFieldDialogRuntimeProperties());
	
	}
	
	
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property = new LinkedHashMap<>();
		property.put(propertyName, fieldMap);
		return property;
	}
	
	
	private void setProperties(String propertyName, Object properties) {
		this.propertyName = propertyName;
		this.fieldMap = (Map<String,List<InputHivePartitionColumn>>)properties;

	}

}



