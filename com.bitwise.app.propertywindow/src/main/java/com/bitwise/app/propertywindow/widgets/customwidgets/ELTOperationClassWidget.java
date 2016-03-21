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

 
package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.dialogs.ELTOperationClassDialog;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

/**
 * The Class ELTOperationClassWidget.
 * 
 * @author Bitwise
 */
public class ELTOperationClassWidget extends AbstractWidget {

	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>(); 
	private OperationClassProperty operationClassProperty;
	private ELTOperationClassDialog eltOperationClassDialog;

	/**
	 * Instantiates a new ELT operation class widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTOperationClassWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);

		this.operationClassProperty = (OperationClassProperty) componentConfigrationProperty.getPropertyValue();
		if(operationClassProperty == null){
			operationClassProperty = new OperationClassProperty(Messages.CUSTOM, "", false, "");
		}
		this.propertyName = componentConfigrationProperty.getPropertyName();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		final ELTDefaultSubgroupComposite runtimeComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		runtimeComposite.createContainerWidget();
		ELTDefaultLable defaultLable1 = new ELTDefaultLable(Messages.OPERATION_CALSS_LABEL); 
		runtimeComposite.attachWidget(defaultLable1);
		
		setPropertyHelpWidget((Control) defaultLable1.getSWTWidgetControl());
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(
				Messages.EDIT_BUTTON_LABEL).grabExcessHorizontalSpace(false);
		runtimeComposite.attachWidget(eltDefaultButton);
		
		setToolTipMessage(Messages.OperationClassBlank);
		((Button)eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				eltOperationClassDialog = new ELTOperationClassDialog(
						runtimeComposite.getContainerControl().getShell(), propertyDialogButtonBar,
						operationClassProperty.clone(), widgetConfig, getComponent().getComponentName());
				eltOperationClassDialog.open();
				if(!eltOperationClassDialog.getOperationClassProperty().equals(operationClassProperty)){
					operationClassProperty = eltOperationClassDialog.getOperationClassProperty();
					propertyDialogButtonBar.enableApplyButton(true);
				}
				setToolTipMessage(eltOperationClassDialog.getTootlTipErrorMessage());
				
				if(eltOperationClassDialog.isOKPressed()){
					propertyDialog.pressOK();
				}
				
				if(eltOperationClassDialog.isCancelPressed()){
					propertyDialog.pressCancel();
				}
				
				super.widgetSelected(e);
			}
			
		});
	
} 

	@Override
	public LinkedHashMap<String, Object> getProperties() {		
		property.put(propertyName, operationClassProperty);
		return property;
	}

}
