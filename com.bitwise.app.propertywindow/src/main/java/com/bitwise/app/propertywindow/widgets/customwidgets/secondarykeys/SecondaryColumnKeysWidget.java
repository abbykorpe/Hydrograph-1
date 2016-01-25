package com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;

/**
 * The Class ELTRuntimePropertiesWidget.
 * 
 * @author Bitwise
 */
public class SecondaryColumnKeysWidget extends AbstractWidget {

	private LinkedHashMap<String, String> InstializeMap;
	private String propertyName;
	private Shell shell;
	private String componentName;

	/*
	 * public ELTRuntimePropertiesWidget() { super(); tempPropertyMap = new LinkedHashMap<String, Object>(); }
	 */

	/**
	 * Instantiates a new ELT runtime properties widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	@SuppressWarnings("unchecked")
	public SecondaryColumnKeysWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);

		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.InstializeMap = (LinkedHashMap<String, String>) componentConfigrationProperty.getPropertyValue();

		tempPropertyMap = new LinkedHashMap<String, Object>();
	}

	private LinkedHashMap<String, Object> tempPropertyMap;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		ELTDefaultSubgroupComposite runtimeComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		runtimeComposite.createContainerWidget();
		shell = runtimeComposite.getContainerControl().getShell();

		ELTDefaultLable defaultLable1 = new ELTDefaultLable("Secondary\nKeys");
		runtimeComposite.attachWidget(defaultLable1);

		ELTDefaultButton eltDefaultButton = new ELTDefaultButton("Edit");

		runtimeComposite.attachWidget(eltDefaultButton);

		try {
			eltDefaultButton.attachListener(ListenerFactory.Listners.RUNTIME_BUTTON_CLICK.getListener(),
					propertyDialogButtonBar, new ListenerHelper(this.getClass().getName(), this),
					eltDefaultButton.getSWTWidgetControl());

		} catch (Exception e1) {

			e1.printStackTrace();
		}

	}

	/**
	 * Sets the properties.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param properties
	 *            the properties
	 */
	public void setProperties(String propertyName, Object properties) {
		this.propertyName = propertyName;
		this.InstializeMap = (LinkedHashMap<String, String>) properties;
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {

		tempPropertyMap.put(this.propertyName, this.InstializeMap);
		return (tempPropertyMap);
	}

	/**
	 * New window launcher.
	 */
	public void newWindowLauncher() {
		SecondaryColumnKeysWidgetWizard secondaryColumnKeyWizard = new SecondaryColumnKeysWidgetWizard();
		secondaryColumnKeyWizard.setComponentName(componentName);
		if (getProperties().get(propertyName) == null) {
			setProperties(propertyName, new LinkedHashMap<String, String>());

		}
		secondaryColumnKeyWizard.setRuntimePropertyMap((LinkedHashMap<String, String>) getProperties()
				.get(propertyName));
		secondaryColumnKeyWizard.setSourceFieldsFromPropagatedSchema(getPropagatedSchema());
		setProperties(propertyName, secondaryColumnKeyWizard.launchRuntimeWindow(shell, propertyDialogButtonBar));

	}

	private List<String> getPropagatedSchema() {
		List<String> fieldNameList = new ArrayList<>();
		ComponentsOutputSchema outputSchema = null;
		for (Link link : getComponent().getTargetConnections()) {
			{
				outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
				if (outputSchema != null) {
					for (FixedWidthGridRow row : outputSchema.getFixedWidthGridRowsOutputFields())
						fieldNameList.add(row.getFieldName());
				}
			}
		}
		return fieldNameList;
	}

}
