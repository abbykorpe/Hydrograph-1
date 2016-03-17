package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.xmlPropertiesContainer.XMLTextContainer;

/**
 * @author Bitwise
 * 
 * This class is used to launch property window which shows XML content of component
 *
 */
public class ELTXmlPropertiesContainer extends AbstractWidget{
	
	private LinkedHashMap<String, Object> property=new LinkedHashMap<>();
	 private String propertyName;
	private String xmlContent=null;
	
	/**
	 * @param componentConfigrationProperty
	 * @param componentMiscellaneousProperties
	 * @param propertyDialogButtonBar
	 */
	public ELTXmlPropertiesContainer(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,propertyDialogButtonBar);
		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.xmlContent=(String) componentConfigrationProperty.getPropertyValue();

	}
	
	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget#attachToPropertySubGroup(com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget)
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		 
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Properties");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		setPropertyHelpWidget((Control) eltDefaultLable.getSWTWidgetControl());
		
		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) {
				XMLTextContainer xmlTextContainer=new XMLTextContainer();
				if(xmlContent!=null)
					xmlTextContainer.setXmlText(xmlContent);
				xmlContent=xmlTextContainer.launchXMLTextContainerWindow();
			}
			
		});
	}


	@Override
	public LinkedHashMap<String, Object> getProperties() {
		 property.put(this.propertyName,xmlContent );
	return property;
	}
	


	
}
