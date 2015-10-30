package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ELTVerifyComponentNameListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

// TODO: Auto-generated Javadoc
/**
 * The Class ELTComponentNameWidget.
 * 
 * @author Bitwise
 */
public class ELTComponentNameWidget extends AbstractWidget {
	private static final String COMPONENT_NAMES = "componentNames";

	private static final String NAME = "Name";

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTComponentNameWidget.class);

	private String newName = "newName";
	private String oldName = "oldName";
	private Text text;
	private String propertyName;
	
	private ELTVerifyComponentNameListener listener;

	/**
	 * Instantiates a new ELT component name widget.
	 * 
	 * @param componentConfigurationProperty
	 *            the component configuration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTComponentNameWidget(ComponentConfigrationProperty componentConfigurationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigurationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);

		this.propertyName = componentConfigurationProperty.getPropertyName();
		this.oldName = (String) componentConfigurationProperty.getPropertyValue();
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		ELTDefaultSubgroupComposite eltDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		eltDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable(NAME);
		eltDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		AbstractELTWidget eltDefaultTextBox = new ELTDefaultTextBox().defaultText("Hello")
				.grabExcessHorizontalSpace(true).textBoxWidth(200);
		eltDefaultSubgroupComposite.attachWidget(eltDefaultTextBox);

		text = (Text) eltDefaultTextBox.getSWTWidgetControl();
		
		text.setTextLimit(15);
		ListenerHelper listenerHelper = new ListenerHelper();
		listenerHelper.put(HelperType.VALIDATION_STATUS, validationStatus);
		try {
			listener = (ELTVerifyComponentNameListener)ListenerFactory.Listners.VERIFY_COMPONENT_NAME.getListener();
			listener.setNames((ArrayList<String>) super.componentMiscellaneousProperties.getComponentMiscellaneousProperty(COMPONENT_NAMES));
			eltDefaultTextBox.attachListener(listener,
					propertyDialogButtonBar,  listenerHelper,eltDefaultTextBox.getSWTWidgetControl());
		/*	eltDefaultTextBox.attachListener(listenerFactory.getListener("MyCustomWidgetTextChange"),
					propertyDialogButtonBar,  null,eltDefaultTextBox.getSWTWidgetControl());*/
		} catch (Exception exception) {
			logger.error("Exception occured", exception);
		}
		
		populateWidget();

	}


	private void populateWidget() {
		listener.setOldName(oldName);
		text.setText(oldName);

	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		newName = text.getText().trim();
		LinkedHashMap<String, Object> property = new LinkedHashMap<>();
		if (newName != null && newName != "" && isUniqueCompName(newName)) {
			property.put(propertyName, newName);
			//super.names.remove(oldName);
			((ArrayList<String>) super.componentMiscellaneousProperties.getComponentMiscellaneousProperty(COMPONENT_NAMES)).remove(oldName);			
			//super.names.add(newName);
			((ArrayList<String>) super.componentMiscellaneousProperties.getComponentMiscellaneousProperty(COMPONENT_NAMES)).add(newName);
			oldName = newName;
		} else {
			// old name already should be there in the names arraylist
			property.put(propertyName, oldName);
		}

		return property;
	}

	/*@Override
	public void setComponentName(String componentName) {
		// TODO Auto-generated method stub

	}*/

	private boolean isUniqueCompName(String componentName) {
		componentName = componentName.trim();
		boolean result = true;

		for (String cname : ((ArrayList<String>) super.componentMiscellaneousProperties.getComponentMiscellaneousProperty(COMPONENT_NAMES))) {
			if (cname.equalsIgnoreCase(componentName)) {
				result = false;
				break;
			}
		}
		logger.debug("result: {}", result);

		return result;
	}

}
