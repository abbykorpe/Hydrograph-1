package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.widgets.Text;

import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;

// TODO: Auto-generated Javadoc
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
	protected ValidationStatus validationStatus = new ValidationStatus();
	protected Text firstTextWidget=null;
	protected WidgetConfig widgetConfig;
	private String toolTipErrorMessage =  null;
	
	public AbstractWidget() {
	
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
	
	/**
	 * Indicates if all the mandatory data is available and valid in the property window. 
	 */
	public class ValidationStatus{
		private Boolean isValid = false;
		
		public void setIsValid(Boolean isValid) {
			this.isValid = isValid;
		}
		
		public boolean getIsValid() {
			return isValid;
		}
	}
	
	public boolean isDataValid() {
		return validationStatus.isValid;
	}
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

}
