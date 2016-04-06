/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hydrograph.ui.propertywindow.widgets.customwidgets;

import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.handlers.ShowHidePropertyHelpHandler;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.ELTComponenetProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialog;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;


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
	private String propertyHelpText;
	
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
	 * Set property help text
	 * 
	 * @param propertyHelpText
	 */
	public void setPropertyHelpText(String propertyHelpText) {
		this.propertyHelpText = propertyHelpText;
	}

	/**
	 * 
	 * Attach help to property help widget
	 * 
	 */
	public void setPropertyHelp() {

		if (ShowHidePropertyHelpHandler.getInstance() != null && propertyHelpText != null && propertyHelpWidget != null
				&& ShowHidePropertyHelpHandler.getInstance().isShowHidePropertyHelpChecked()) {
			propertyHelpWidget.setToolTipText(propertyHelpText);
			propertyHelpWidget.setCursor(new Cursor(propertyHelpWidget.getDisplay(), SWT.CURSOR_HELP));
		}

	}
	
	public boolean verifySchemaFile(){
		return true;
	}
	
}
