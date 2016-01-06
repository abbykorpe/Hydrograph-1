package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinproperty.ELTJoinConfigGrid;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

/**
 * @author 
 *
 */
public class ELTJoinWidget extends AbstractWidget{
	
	public static int value;
	private Object properties;
	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>(); 
	
	//private JoinMappingGrid lookupPropertyGrid;
	private List<JoinConfigProperty> configProperty;
	
	public ELTJoinWidget(
			ComponentConfigrationProperty componentConfigProp, ComponentMiscellaneousProperties componentMiscProps,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propertyDialogButtonBar);
		this.properties =  (List<JoinConfigProperty>)componentConfigrationProperty.getPropertyValue();
		if(properties == null){
			configProperty = new ArrayList<>();
		}
		else{
			configProperty = (List<JoinConfigProperty>) properties;
		}
		this.propertyName = componentConfigProp.getPropertyName();
		
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
			if(key.equalsIgnoreCase("inPortCount")){
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
				ELTJoinConfigGrid eltJoinConfigGrid = new ELTJoinConfigGrid(((Button) eltDefaultButton.getSWTWidgetControl()).getShell(), configProperty, validationStatus);
				eltJoinConfigGrid.open();
			}
			
		});
	}


	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, configProperty);
		return property;
	}


	
}
