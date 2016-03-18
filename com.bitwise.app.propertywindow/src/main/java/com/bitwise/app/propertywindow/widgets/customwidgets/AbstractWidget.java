package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.propertywindow.handlers.ShowHidePropertyHelpHandler;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.property.ELTComponenetProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialog;
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
	private List<String> operationFieldList;
	protected PropertyDialog propertyDialog;
	private Control propertyHelpWidget;
	
	protected Component getComponent() {
		return component;
	}

	/**
	 * 
	 * Set component name
	 * 
	 * @param component
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	public AbstractWidget() {
	
	}
		
	/**
	 *  get schema for internal propagation
	 *  
	 * @return {@link Schema}
	 */
	public Schema getSchemaForInternalPapogation() {
		return schemaForInternalPapogation;
	}

	/**
	 * Set schema for internal propagation
	 * 
	 * @param schemaForInternalPapogation
	 */
	public void setSchemaForInternalPapogation(Schema schemaForInternalPapogation) {
		this.schemaForInternalPapogation = schemaForInternalPapogation;
	}
	
	/**
	 * 
	 * Get operation field list
	 * 
	 * @return - list of operation fields
	 */
	public List<String> getOperationFieldList() {
		return operationFieldList;
	}

	/**
	 * 
	 * Set operation field list
	 * 
	 * @param operationFieldList
	 */
	public void setOperationFieldList(List<String> operationFieldList) {
		this.operationFieldList = operationFieldList;
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

	/**
	 * 
	 * get component properties
	 * 
	 */
	public abstract LinkedHashMap<String, Object> getProperties();
		
	public Text getFirstTextWidget(){
		return firstTextWidget;
	}

	/**
	 * 
	 * Get widget configurations 
	 * 
	 * @return {@link WidgetConfig}
	 */
	public WidgetConfig getWidgetConfig() {
		return widgetConfig;
	}

	/**
	 * 
	 * Set widget configurations 
	 * 
	 * @param widgetConfig
	 */
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		this.widgetConfig = widgetConfig;
	}
	
	/**
	 * 
	 * Set tooltip message
	 * 
	 * @param toolTipErrorMessage
	 */
	protected void setToolTipMessage(String toolTipErrorMessage){
		this.toolTipErrorMessage = toolTipErrorMessage;
	}
	
	/**
	 * 
	 * Get tooltip error message
	 * 
	 * @return
	 */
	public String getToolTipErrorMessage(){
		return toolTipErrorMessage ;
	}
	
	/**
	 * 
	 * Get property name
	 * 
	 * @return
	 */
	public String getPropertyName(){
		return componentConfigrationProperty.getPropertyName();
	}
	
	/**
	 * 
	 * get all component properties
	 * 
	 * @return
	 */
	public ELTComponenetProperties getEltComponenetProperties() {
		return allComponenetProperties;
	}

	/**
	 * 
	 * Set all component properties
	 * 
	 * @param eltComponenetProperties
	 */
	public void setEltComponenetProperties(
			ELTComponenetProperties eltComponenetProperties) {
		this.allComponenetProperties = eltComponenetProperties;
	}

	/**
	 * 
	 * Refresh internal schema
	 * 
	 */
	public void refresh(){
				
	}

	/**
	 * 
	 * Set property dialog
	 * 
	 * @param {@link PropertyDialog}
	 */
	public void setPropertyDialog(PropertyDialog propertyDialog) {
		this.propertyDialog = propertyDialog;
	}
	
	/**
	 * 
	 * Set the widget to which you want to attach property help
	 * 
	 * @param propertyHelpWidget
	 */
	protected void setPropertyHelpWidget(Control propertyHelpWidget){
		this.propertyHelpWidget = propertyHelpWidget;
	}
	
	/**
	 * 
	 * Attach help to property help widget
	 * 
	 */
	public void setPropertyHelp() {

		if (ShowHidePropertyHelpHandler.getInstance() != null
				&& getComponent().getPropertyHelpTextMap().get(componentConfigrationProperty.getPropertyName()) != null
				&& propertyHelpWidget != null
				&& ShowHidePropertyHelpHandler.getInstance().isShowHidePropertyHelpChecked()) {
			propertyHelpWidget.setToolTipText(component.getPropertyHelpTextMap()
					.get(componentConfigrationProperty.getPropertyName()).replace("\\n", "\n"));
			propertyHelpWidget.setCursor(new Cursor(propertyHelpWidget.getDisplay(), SWT.CURSOR_HELP));
		}

	}
	
}
