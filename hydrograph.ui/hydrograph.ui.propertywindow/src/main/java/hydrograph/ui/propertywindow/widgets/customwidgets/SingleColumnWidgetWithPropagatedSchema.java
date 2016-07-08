package hydrograph.ui.propertywindow.widgets.customwidgets;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.FieldDialogWithAddValue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

public class SingleColumnWidgetWithPropagatedSchema extends SingleColumnWidget {

	public SingleColumnWidgetWithPropagatedSchema(
			ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps,
			PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
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
		
		FieldDialogWithAddValue addValue=new FieldDialogWithAddValue(Display.getCurrent().getActiveShell(), propertyDialogButtonBar);
		addValue.open();
		
	
	}

}



