package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.SingleColumnGridConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.dialogs.FieldDialog;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterPropertyWizard;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

public class SingleColumnWidget extends AbstractWidget {

	private String propertyName;
	private List<String> set;
	private Shell shell;
	private SingleColumnGridConfig gridConfig = null;

	public SingleColumnWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {

		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		propertyName = componentConfigProp.getPropertyName();
		setProperties(componentConfigProp.getPropertyName(), componentConfigProp.getPropertyValue());
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		shell = eltSuDefaultSubgroupComposite.getContainerControl().getShell();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable(gridConfig.getLabelName());
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		Button button = (Button) eltDefaultButton.getSWTWidgetControl();
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				FieldDialog fieldDialog = new FieldDialog(new Shell(),propertyDialogButtonBar);
				fieldDialog.setComponentName(gridConfig.getComponentName());
				if (getProperties().get(propertyName) == null) {
					setProperties(propertyName, new ArrayList<String>());
				}
				fieldDialog.setRuntimePropertySet((List<String>) getProperties().get(propertyName));
				fieldDialog.setSourceFieldsFromPropagatedSchema(getPropagatedSchema());
				fieldDialog.open();
				
				setProperties(propertyName, fieldDialog.getFieldNameList());
				
			}
		});

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

	public void setWidgetConfig(WidgetConfig widgetConfig) {
		gridConfig = (SingleColumnGridConfig) widgetConfig;
	}

	private List<String> getPropagatedSchema() {
			return SchemaPropagationHelper.INSTANCE.getFieldsForFilterWidget(getComponent()).get(
				Constants.INPUT_SOCKET_TYPE + 0);
	}
}
