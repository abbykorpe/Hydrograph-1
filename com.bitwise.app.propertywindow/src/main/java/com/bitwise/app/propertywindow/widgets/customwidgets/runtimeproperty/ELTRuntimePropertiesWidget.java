package com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.RuntimeConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;

/**
 * Creates the Property window for Runtime Properties
 * 
 * @author Bitwise
 */
public class ELTRuntimePropertiesWidget extends AbstractWidget {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTRuntimePropertiesWidget.class);
	private Map<String, String> initialMap;
	private String propertyName;
	private Shell shell;
	private RuntimeConfig runtimeConfig;
	
	/**
	 * Instantiates a new ELT runtime properties widget.
	 * 
	 * @param componentConfigProp
	 *            the component configration property
	 * @param componentMiscProps
	 *            the component miscellaneous properties
	 * @param propDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTRuntimePropertiesWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);

		this.propertyName = componentConfigProp.getPropertyName();
		this.initialMap = (Map<String, String>) componentConfigProp.getPropertyValue();
		
		//since this window does all the validation 
		//we can assume that it is valid always
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		ELTDefaultSubgroupComposite runtimeComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		runtimeComposite.createContainerWidget();
		shell = runtimeComposite.getContainerControl().getShell();
		runtimeConfig = (RuntimeConfig) widgetConfig;
		
		ELTDefaultLable defaultLable1 = new ELTDefaultLable(runtimeConfig.getLabel()); 
		runtimeComposite.attachWidget(defaultLable1);
		setPropertyHelpWidget((Control) defaultLable1.getSWTWidgetControl());
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(Constants.EDIT);
		
		runtimeComposite.attachWidget(eltDefaultButton);

		try {
			eltDefaultButton.attachListener(ListenerFactory.Listners.RUNTIME_BUTTON_CLICK.getListener(),
					propertyDialogButtonBar, new ListenerHelper(this.getClass().getName(), this), eltDefaultButton.getSWTWidgetControl());

		} catch (Exception exception) {
			logger.error("Error occured while attaching listener to Runtime Properties window", exception);
		}
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> tempPropertyMap = new LinkedHashMap<>();
		tempPropertyMap.put(this.propertyName, this.initialMap);
		return tempPropertyMap;
	}

	/**
	 * New window launcher.
	 */
	public void newWindowLauncher() {
		if (getProperties().get(propertyName) == null) {
			initialMap = new HashMap<String, String>();
		}
		
		RuntimePropertyDialog runtimePropertyDialog = new RuntimePropertyDialog(shell,propertyDialogButtonBar,runtimeConfig.getWindowLabel());
		runtimePropertyDialog.setRuntimeProperties((Map<String, String>) getProperties().get(propertyName));
		runtimePropertyDialog.open();
		initialMap = runtimePropertyDialog.getRuntimeProperties();
	}
}
