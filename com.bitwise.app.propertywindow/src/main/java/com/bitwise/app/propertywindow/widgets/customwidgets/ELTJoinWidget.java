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
import com.bitwise.app.propertywindow.widgets.joinproperty.ELTJoinConfigGrid;




/**
 * @author vibhort
 *
 */
public class ELTJoinWidget extends AbstractWidget{
	
	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>(); 
	
	
	public ELTJoinWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);
		this.propertyName = componentConfigrationProperty.getPropertyName();
	}
	
	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget#attachToPropertySubGroup(com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget)
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		 
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Join\nConfiguration");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		
		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) {
				ELTJoinConfigGrid configGrid = new ELTJoinConfigGrid(((Button) eltDefaultButton.getSWTWidgetControl()).getShell());
				configGrid.open();
			}
			
		});
	}


	@Override
	public LinkedHashMap<String, Object> getProperties() {
		return property;
	}


	
}
