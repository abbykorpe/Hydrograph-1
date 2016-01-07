package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.widgets.Text;

import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

/**
 * The Class ELTComponentBaseType.
 * 
 * @author Bitwise
 */
public class ELTComponentBaseType extends AbstractWidget{
	private ELTDefaultTextBox eltDefaultTextBox;
	
	/**
	 * Instantiates a new ELT component base type.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTComponentBaseType(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(subGroup.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		
		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Base Type");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		eltDefaultTextBox = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(100);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultTextBox);
		eltDefaultTextBox.setEnabled(false);
		
		populateWidget();
	}

	
	private void populateWidget() {
		String componentBaseType = (String) componentMiscellaneousProperties.getComponentMiscellaneousProperty("componentBaseType");
		((Text)eltDefaultTextBox.getSWTWidgetControl()).setText(componentBaseType);
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		return null;
	}
}
