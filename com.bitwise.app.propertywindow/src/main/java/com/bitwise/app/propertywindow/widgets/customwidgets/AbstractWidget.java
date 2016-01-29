package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.property.ELTComponenetProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;

/**
 * 
 * @author Bitwise 
 * Sep 08, 2015
 * 
 */

public abstract class AbstractWidget {
	protected ComponentConfigrationProperty componentConfigrationProperty;
	protected ComponentMiscellaneousProperties componentMiscellaneousProperties;
	protected PropertyDialogButtonBar propertyDialogButtonBar;
	protected Text firstTextWidget=null;
	protected WidgetConfig widgetConfig;
	protected ELTComponenetProperties allComponenetProperties;
	private String toolTipErrorMessage =  null;
	private Component component;
	private Schema schemaForInternalPapogation;
	protected Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public AbstractWidget() {
	
	}
	
	
	public Schema getSchemaForInternalPapogation() {
		return schemaForInternalPapogation;
	}

	public void setSchemaForInternalPapogation(Schema schemaForInternalPapogation) {
		this.schemaForInternalPapogation = schemaForInternalPapogation;
	}

	/**
	 * Instantiates a new abstract widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public AbstractWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		
		this.componentConfigrationProperty = componentConfigrationProperty;
		this.componentMiscellaneousProperties = componentMiscellaneousProperties;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
	}

	/**
	 * Attach to property sub group.
	 * 
	 * @param subGroup
	 *            the sub group
	 */
	public abstract void attachToPropertySubGroup(AbstractELTContainerWidget subGroup);

	public abstract LinkedHashMap<String, Object> getProperties();
	
	public Text getFirstTextWidget(){
		return firstTextWidget;
	}

	public WidgetConfig getWidgetConfig() {
		return widgetConfig;
	}

	public void setWidgetConfig(WidgetConfig widgetConfig) {
		this.widgetConfig = widgetConfig;
	}
	
	protected void setToolTipMessage(String toolTipErrorMessage){
		this.toolTipErrorMessage = toolTipErrorMessage;
	}
	
	public String getToolTipErrorMessage(){
		return toolTipErrorMessage ;
	}
	
	public String getPropertyName(){
		return componentConfigrationProperty.getPropertyName();
	}
	
	public ELTComponenetProperties getEltComponenetProperties() {
		return allComponenetProperties;
	}

	public void setEltComponenetProperties(
			ELTComponenetProperties eltComponenetProperties) {
		this.allComponenetProperties = eltComponenetProperties;
	}

	public void refresh(){
				
	}
}
