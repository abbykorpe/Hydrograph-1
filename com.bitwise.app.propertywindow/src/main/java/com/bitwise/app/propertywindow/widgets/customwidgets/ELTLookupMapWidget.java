package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.joinproperty.ELTLookupMapWizard;

public class ELTLookupMapWidget extends AbstractWidget{

	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private ELTLookupMapWizard lookupMapWizard;
	private LookupMappingGrid lookupPropertyGrid;
	
	public ELTLookupMapWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propertyDialogButtonBar){
		super(componentConfigProp, componentMiscProps, propertyDialogButtonBar);
		if(componentConfigProp.getPropertyValue() == null){
			lookupPropertyGrid = new LookupMappingGrid();
		}
		else{
			lookupPropertyGrid = (LookupMappingGrid) componentConfigProp.getPropertyValue();
		}
		this.propertyName = componentConfigProp.getPropertyName();
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
				 lookupMapWizard = new ELTLookupMapWizard(((Button)eltDefaultButton.getSWTWidgetControl()).getShell(), lookupPropertyGrid);
				 lookupMapWizard.open();
				 lookupMapWizard.getLookupPropertyGrid();
				 propertyDialogButtonBar.enableApplyButton(true);

			 }
		});
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, lookupPropertyGrid);
		
		return property;
	}

}
