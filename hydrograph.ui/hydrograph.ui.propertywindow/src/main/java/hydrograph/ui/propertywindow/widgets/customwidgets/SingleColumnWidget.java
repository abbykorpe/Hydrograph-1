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

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.SingleColumnGridConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.dialogs.FieldDialog;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
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
import org.eclipse.swt.widgets.Shell;


public class SingleColumnWidget extends AbstractWidget {

	protected String propertyName;
	private List<String> set;
	protected SingleColumnGridConfig gridConfig = null;
	private ArrayList<AbstractWidget> widgets;

	public SingleColumnWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {

		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		propertyName = componentConfigProp.getPropertyName();
		setProperties(componentConfigProp.getPropertyName(), componentConfigProp.getPropertyValue());
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		ELTDefaultSubgroupComposite defaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		defaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget defaultLable = new ELTDefaultLable(gridConfig.getLabelName());
		defaultSubgroupComposite.attachWidget(defaultLable);
		setPropertyHelpWidget((Control) defaultLable.getSWTWidgetControl());
		
		
		AbstractELTWidget defaultButton = new ELTDefaultButton(Constants.EDIT);
		defaultSubgroupComposite.attachWidget(defaultButton);
		Button button = (Button) defaultButton.getSWTWidgetControl();
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				onDoubleClick();
			}

			
		});

	}

	
	protected void onDoubleClick() {
		FieldDialog fieldDialog = new FieldDialog(new Shell(), propertyDialogButtonBar);
		fieldDialog.setComponentName(gridConfig.getComponentName());
		if (getProperties().get(propertyName) == null) {
			setProperties(propertyName, new ArrayList<String>());
		}
		fieldDialog.setRuntimePropertySet(new ArrayList<String>(set));
		fieldDialog.setSourceFieldsFromPropagatedSchema(getPropagatedSchema());
		fieldDialog.open();

		setProperties(propertyName, fieldDialog.getFieldNameList());
        showHideErrorSymbol(widgets);
	} 
	
	
	
	private void setProperties(String propertyName, Object properties) {
		this.propertyName = propertyName;
		this.set = (List<String>) properties;

	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property = new LinkedHashMap<>();
		property.put(propertyName, this.set);
		return property;
	}

	@Override
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		gridConfig = (SingleColumnGridConfig) widgetConfig;
	}

	protected List<String> getPropagatedSchema() {
		return SchemaPropagationHelper.INSTANCE.getFieldsForFilterWidget(getComponent()).get(
				Constants.INPUT_SOCKET_TYPE + 0);
	}

	@Override
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(getProperties().get(propertyName));
	}

	

	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;
		
	}
}
