package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.HashSet;
import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterPropertyWizard;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

public class ELTColumnWidget extends AbstractWidget{
	
	private String propertyName;
	private HashSet<String> set;
	private Shell shell;

	public ELTColumnWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);
		
		this.propertyName = componentConfigrationProperty.getPropertyName();
		setProperties(componentConfigrationProperty.getPropertyName(), componentConfigrationProperty.getPropertyValue());
		validationStatus.setIsValid(true);
		
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		shell =eltSuDefaultSubgroupComposite.getContainerControl().getShell();
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Column\n Name");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		Button button=(Button)eltDefaultButton.getSWTWidgetControl();
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ELTFilterPropertyWizard filterWizardObj=new ELTFilterPropertyWizard();
				filterWizardObj.setComponentName("Column Name ");
				if(getProperties().get(propertyName)==null){
					setProperties(propertyName, new HashSet<String>());
				}
					filterWizardObj.setRuntimePropertySet((HashSet<String>) getProperties().get(propertyName));
					setProperties(propertyName,filterWizardObj.launchRuntimeWindow(shell, propertyDialogButtonBar));
			
			}
		});
		
	}
	private void setProperties(String propertyName, Object properties) {
		this.propertyName = propertyName;
		this.set = (HashSet<String>) properties;

	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName,this.set);
		return property;
	}

}
