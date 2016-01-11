package com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Shell;

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
	
	private TreeMap<String, String> InstializeMap;
	private String propertyName;
	private Shell shell;
	private String componentName;

	/*public ELTRuntimePropertiesWidget() {
		super();
		tempPropertyMap = new LinkedHashMap<String, Object>();
	}*/
	
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
	public SecondaryColumnKeysWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);

		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.InstializeMap = (TreeMap<String, String>) componentConfigrationProperty.getPropertyValue();
		
		tempPropertyMap = new LinkedHashMap<String, Object>();
	}
	

	private LinkedHashMap<String, Object> tempPropertyMap;

	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		ELTDefaultSubgroupComposite runtimeComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		runtimeComposite.createContainerWidget();
		shell = runtimeComposite.getContainerControl().getShell();

		
		ELTDefaultLable defaultLable1 = new ELTDefaultLable("Secondary\nKeys"); 
		runtimeComposite.attachWidget(defaultLable1);
		
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(
				"Edit");
		
		runtimeComposite.attachWidget(eltDefaultButton);

		try {
			eltDefaultButton.attachListener(ListenerFactory.Listners.RUNTIME_BUTTON_CLICK.getListener(),
					propertyDialogButtonBar, new ListenerHelper(this.getClass()
							.getName(), this), eltDefaultButton
							.getSWTWidgetControl());

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
		this.InstializeMap = (TreeMap<String, String>) properties;
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
		SecondaryColumnKeysWidgetWizard runTimeWizardObj = new SecondaryColumnKeysWidgetWizard();
		runTimeWizardObj.setComponentName(componentName);
		if (getProperties().get(propertyName) == null) {
			setProperties(propertyName, new TreeMap<String, String>());

		}

		runTimeWizardObj
				.setRuntimePropertyMap((TreeMap<String, String>) getProperties()
						.get(propertyName));
		setProperties(propertyName, runTimeWizardObj.launchRuntimeWindow(shell,propertyDialogButtonBar));

	}

}
