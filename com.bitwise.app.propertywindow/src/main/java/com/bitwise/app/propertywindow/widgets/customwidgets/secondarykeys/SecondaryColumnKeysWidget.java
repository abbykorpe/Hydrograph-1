package com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.EditButtonWithLabelConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
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
	private Logger logger = LogFactory.INSTANCE.getLogger(SecondaryColumnKeysWidget.class);
	private EditButtonWithLabelConfig buttonWithLabelConfig;
	private LinkedHashMap<String, Object> tempPropertyMap;

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

	public void setWidgetConfig(WidgetConfig widgetConfig) {
		buttonWithLabelConfig = (EditButtonWithLabelConfig) widgetConfig;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		ELTDefaultSubgroupComposite runtimeComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		runtimeComposite.createContainerWidget();
		shell = runtimeComposite.getContainerControl().getShell();

		ELTDefaultLable defaultLable1 = new ELTDefaultLable(buttonWithLabelConfig.getName());
		runtimeComposite.attachWidget(defaultLable1);
		setPropertyHelpWidget((Control) defaultLable1.getSWTWidgetControl());
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton("Edit");

		runtimeComposite.attachWidget(eltDefaultButton);

		try {
			eltDefaultButton.attachListener(ListenerFactory.Listners.RUNTIME_BUTTON_CLICK.getListener(),
					propertyDialogButtonBar, new ListenerHelper(this.getClass().getName(), this),
					eltDefaultButton.getSWTWidgetControl());

		} catch (Exception e1) {
          logger.error("Failed to attach listener",e1);
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
		
		SecondaryColumnKeysDialog secondaryColumnDialog = new SecondaryColumnKeysDialog(shell, propertyDialogButtonBar, buttonWithLabelConfig);
		if (getProperties().get(propertyName) == null) {
			setProperties(propertyName, new LinkedHashMap<String, String>());

		}
		secondaryColumnDialog.setSecondaryColumnsMap((LinkedHashMap<String, String>) getProperties()
				.get(propertyName));
		secondaryColumnDialog.setSourceFieldsFromPropagatedSchema(getPropagatedSchema());
		
		secondaryColumnDialog.open();
		
		setProperties(propertyName, secondaryColumnDialog.getSecondaryColumnsMap());

	}

	private List<String> getPropagatedSchema() {
		return SchemaPropagationHelper.INSTANCE.getFieldsForFilterWidget(getComponent()).get(
				Constants.INPUT_SOCKET_TYPE + 0);
	}

}
