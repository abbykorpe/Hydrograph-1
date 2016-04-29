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

import hydrograph.ui.datastructure.property.LookupConfigProperty;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import hydrograph.ui.propertywindow.widgets.customwidgets.lookupproperty.ELTLookupConfigGrid;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;


public class ELTLookupConfigWidget extends AbstractWidget {
    
	private ArrayList<AbstractWidget> widgets;
	private LookupConfigProperty properties;
	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();

	public ELTLookupConfigWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.properties = (LookupConfigProperty) componentConfigrationProperty.getPropertyValue();
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Lookup\nConfiguration");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		setPropertyHelpWidget((Control) eltDefaultLable.getSWTWidgetControl());
		
		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);

		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (properties == null) {
					properties = new LookupConfigProperty();
					properties.setDriverKey("");
					properties.setLookupKey("");
					properties.isSelected();
				}
				ELTLookupConfigGrid eltLookupConfigGrid = new ELTLookupConfigGrid(((Button) eltDefaultButton
						.getSWTWidgetControl()).getShell(), propertyDialogButtonBar, properties);
				eltLookupConfigGrid.setPropagatedFieldProperty(SchemaPropagationHelper.INSTANCE
						.getFieldsForFilterWidget(getComponent()));
				eltLookupConfigGrid.open();
				showHideErrorSymbol(widgets);
			}
		});
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, properties);
		return property;
	}

	@Override
	public boolean applyValidationRule() {
		LookupConfigProperty lookupConfigProperty = (LookupConfigProperty)properties;
		return validateAgainstValidationRule(lookupConfigProperty);
	}

	

	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;
	}

}
