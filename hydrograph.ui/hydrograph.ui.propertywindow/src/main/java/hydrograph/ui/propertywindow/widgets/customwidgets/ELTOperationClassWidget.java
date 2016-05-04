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

import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.OperationClassProperty;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.ELTOperationClassDialog;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;


/**
 * The Class ELTOperationClassWidget.
 * 
 * @author Bitwise
 */
public class ELTOperationClassWidget extends AbstractWidget {

	private String propertyName;
	private ArrayList<AbstractWidget> widgets;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>(); 
	private OperationClassProperty operationClassProperty;
	private ELTOperationClassDialog eltOperationClassDialog;
	private List<NameValueProperty> nameValuePropertyList;
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
		nameValuePropertyList=new ArrayList<>(); 
		this.operationClassProperty = (OperationClassProperty) componentConfigrationProperty.getPropertyValue();
		if(operationClassProperty == null){
			operationClassProperty = new OperationClassProperty(Messages.CUSTOM, "", false, "",nameValuePropertyList);
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
				OperationClassProperty oldOperationClassProperty=operationClassProperty.clone();
				eltOperationClassDialog = new ELTOperationClassDialog(
						runtimeComposite.getContainerControl().getShell(), propertyDialogButtonBar,
						operationClassProperty, widgetConfig, getComponent().getComponentName());
				eltOperationClassDialog.open();
				operationClassProperty.setComboBoxValue(eltOperationClassDialog.getOperationClassProperty().getComboBoxValue());
				operationClassProperty.setOperationClassPath(eltOperationClassDialog.getOperationClassProperty().getOperationClassPath());
				operationClassProperty.setOperationClassFullPath(eltOperationClassDialog.getOperationClassProperty().getOperationClassFullPath());
				operationClassProperty.setParameter(eltOperationClassDialog.getOperationClassProperty().isParameter());
				if (eltOperationClassDialog.isCancelPressed() && (!(eltOperationClassDialog.isApplyPressed()))) {
					operationClassProperty.setNameValuePropertyList(oldOperationClassProperty.getNameValuePropertyList());
				}
				setToolTipMessage(eltOperationClassDialog.getTootlTipErrorMessage());
				
				if(eltOperationClassDialog.isYesPressed()){
					propertyDialog.pressOK();
				}
				
				if(eltOperationClassDialog.isNoPressed()){
					propertyDialog.pressCancel();
				}
				showHideErrorSymbol(widgets);
				super.widgetSelected(e);
			}
			
		});
	
} 
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {		
		property.put(propertyName, operationClassProperty);
		return property;
	}

	@Override
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(operationClassProperty);
	}



	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;
	}

}
