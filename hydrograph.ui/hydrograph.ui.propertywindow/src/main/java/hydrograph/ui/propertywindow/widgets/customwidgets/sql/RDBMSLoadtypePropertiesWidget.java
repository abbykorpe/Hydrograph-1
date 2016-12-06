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
package hydrograph.ui.propertywindow.widgets.customwidgets.sql;

import hydrograph.ui.datastructure.property.SQLLoadTypeProperty;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
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
/**
 * The class RDBMSLoadtypePropertiesWidget
 * 
 * @author Bitwise
 * 
 */
public class RDBMSLoadtypePropertiesWidget extends AbstractWidget{

	private List<AbstractWidget> widgets;
	private SQLLoadTypeProperty properties;
	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	
	
	public RDBMSLoadtypePropertiesWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);
		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.properties = (SQLLoadTypeProperty) componentConfigrationProperty.getPropertyValue();
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				subGroup.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Loadtype\nConfiguration");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		setPropertyHelpWidget((Control) eltDefaultLable.getSWTWidgetControl());
		
		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);

		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (properties == null) {
					properties = new SQLLoadTypeProperty();
					
				}
				
				RDBMSLoadTypeConfigGrid loadTypeConfigGrid = new RDBMSLoadTypeConfigGrid(((Button) eltDefaultButton
						.getSWTWidgetControl()).getShell(), propertyDialogButtonBar, properties);
				
				loadTypeConfigGrid.setPropagatedFieldProperty(SchemaPropagationHelper.INSTANCE
						.getFieldsForFilterWidget(getComponent()));
				loadTypeConfigGrid.open();
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
	public boolean isWidgetValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addModifyListener(Property property,
			ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;
		
	}

}
