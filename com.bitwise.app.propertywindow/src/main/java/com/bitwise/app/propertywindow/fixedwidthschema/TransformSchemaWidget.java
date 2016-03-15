package com.bitwise.app.propertywindow.fixedwidthschema;

import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;

public class TransformSchemaWidget extends ELTFixedWidget{

	public TransformSchemaWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
		
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		isTransformSchemaType=true;
		super.attachToPropertySubGroup(container);
		
	}
}
