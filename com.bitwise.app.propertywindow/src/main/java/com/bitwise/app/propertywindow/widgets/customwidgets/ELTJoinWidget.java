package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.common.datastructure.property.LookupConfigProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
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
	
	public static int value;
	private JoinConfigProperty properties;
	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>(); 
	private LookupMappingGrid lookupPropertyGrid;
	
	
	public ELTJoinWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);
		if(lookupPropertyGrid == null){
			lookupPropertyGrid = new LookupMappingGrid();
		}
		this.propertyName = componentConfigrationProperty.getPropertyName();
		
		//this.properties =  (JoinConfigProperty)componentConfigrationProperty.getPropertyValue();
	}
	
	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget#attachToPropertySubGroup(com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget)
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
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
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Join\nConfiguration");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		
		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(properties == null){
					 properties=new JoinConfigProperty();
					 properties.setJoin_key("");
				 }
				ELTJoinConfigGrid eltJoinConfigGrid = new ELTJoinConfigGrid(((Button) eltDefaultButton.getSWTWidgetControl()).getShell(), lookupPropertyGrid);
				eltJoinConfigGrid.open();
				lookupPropertyGrid = eltJoinConfigGrid.getJoinPropertyGrid();
			}
			
		});
	}


	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, lookupPropertyGrid);
		return property;
	}


	
}
