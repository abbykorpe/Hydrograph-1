package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.common.datastructure.property.JoinMappingGrid;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.joinproperty.JoinMapGrid;

public class ELTJoinMapWidget extends AbstractWidget{

	public static int value;
	private Object properties;
	private String propertyName;
	private JoinMappingGrid joinPropertyGrid;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	
	public ELTJoinMapWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);
		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.properties =  componentConfigrationProperty.getPropertyValue();
				
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(subGroup.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		
		LinkedHashMap<String, Object> map = allComponenetProperties.getComponentConfigurationProperties();
		for(String key : map.keySet()){
			if(key.equalsIgnoreCase("input_count")){
				String data=(String)map.get(key);
				if(Integer.parseInt(data)>=2){
					value = Integer.parseInt(data);
				}else{
					value = 2;
				}
			}
		}
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Component\n Mapping");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		
		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) {
				JoinMapGrid joinMapGrid = new JoinMapGrid(((Button) eltDefaultButton.getSWTWidgetControl()).getShell(), joinPropertyGrid);
				joinMapGrid.open();
				joinPropertyGrid = joinMapGrid.getJoinPropertyGrid();
			}
			
		});
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, joinPropertyGrid);
		
		return property;
	}

}
