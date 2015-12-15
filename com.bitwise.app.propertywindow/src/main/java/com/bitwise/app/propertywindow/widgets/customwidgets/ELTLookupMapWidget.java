package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.joinproperty.ELTLookupConfigGrid;
import com.bitwise.app.propertywindow.widgets.joinproperty.ELTLookupMapWizard;

public class ELTLookupMapWidget extends AbstractWidget{

	public ELTLookupMapWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar){
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(subGroup.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Lookup\n Mapping");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		
		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button)eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			 @Override
				public void widgetSelected(SelectionEvent e) {
				 ELTLookupMapWizard grid = new ELTLookupMapWizard( ((Button)eltDefaultButton.getSWTWidgetControl()).getShell() );
				 	grid.open();

			 }
		});
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
