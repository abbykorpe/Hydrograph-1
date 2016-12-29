package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ExpandBar;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;

class Factory {
	 
public static final Factory INSTANCE= new Factory();

AbstractExpressionComposite getComposite(ExpandBar parent,MappingSheetRow mappingSheetRow,Component component,WidgetConfig widgetConfig )
{
	if(Constants.NORMALIZE.equalsIgnoreCase(component.getComponentName()))
	{
		return new NormalizeExpressionComposite(parent, SWT.None, mappingSheetRow, component, widgetConfig);
	}
	else if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()))
	{
	return new TransformExpressionComposite(parent, SWT.None, mappingSheetRow, component, widgetConfig);
	}
	else if(Constants.AGGREGATE.equalsIgnoreCase(component.getComponentName())||
			Constants.CUMULATE.equalsIgnoreCase(component.getComponentName()))
	{
	return new AggregateCumulateExpressionComposite(parent, SWT.None, mappingSheetRow, component, widgetConfig);
	}
	
	return null;
}

}
