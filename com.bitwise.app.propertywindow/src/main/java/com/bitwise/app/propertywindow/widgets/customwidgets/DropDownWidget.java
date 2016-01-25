package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.datastructures.ComboBoxParameter;
import com.bitwise.app.propertywindow.factory.ListenerFactory.Listners;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.DropDownConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCombo;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.IELTListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * Widget for showing Drop-down in property window.
 * 
 * @author Bitwise
 */
public class DropDownWidget extends AbstractWidget{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(DropDownWidget.class);
	
	private Combo combo;
	private Text text;
	private LinkedHashMap<String, Object> property=new LinkedHashMap<>();
	private String propertyName;
	private String properties;
	private ComboBoxParameter comboBoxParameter=new ComboBoxParameter();
	private ControlDecoration txtDecorator;
	private DropDownConfig dropDownConfig;
	
	/**
	 * Instantiates a new ELT safe widget.
	 * 
	 * @param componentConfigProp
	 *            the component configuration property
	 * @param componentMiscProps
	 *            the component miscellaneous properties
	 * @param propDialogButtonBar
	 *            the property dialog button bar
	 */
	public DropDownWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);

		this.propertyName = componentConfigProp.getPropertyName();
		this.properties =  (String) componentConfigProp.getPropertyValue(); 
		
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		
		AbstractELTWidget defaultLabel = new ELTDefaultLable(dropDownConfig.getName());
		eltSuDefaultSubgroupComposite.attachWidget(defaultLabel);
		
		AbstractELTWidget defaultCombo = new ELTDefaultCombo().defaultText(convertToArray(dropDownConfig.getItems()));
		eltSuDefaultSubgroupComposite.attachWidget(defaultCombo);
		combo=(Combo)defaultCombo.getSWTWidgetControl();
		combo.select(0);
		
		ELTDefaultTextBox eltDefaultTextBox = new ELTDefaultTextBox().grabExcessHorizontalSpace(true);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultTextBox);
		eltDefaultTextBox.visibility(false);
		text=(Text)eltDefaultTextBox.getSWTWidgetControl();
		
		txtDecorator = WidgetUtility.addDecorator(text, Messages.bind(Messages.EMPTY_FIELD, dropDownConfig.getName()));
		
		
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		
		
		try {
			for (Listners listenerNameConstant : dropDownConfig.getDropDownListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				defaultCombo.attachListener(listener,propertyDialogButtonBar, helper,defaultCombo.getSWTWidgetControl(),
						eltDefaultTextBox.getSWTWidgetControl());
			}
			for (Listners listenerNameConstant : dropDownConfig.getTextBoxListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				eltDefaultTextBox.attachListener(listener, propertyDialogButtonBar, helper,eltDefaultTextBox.getSWTWidgetControl());
			}
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", dropDownConfig.getName(), exception);
		}
		
		 populateWidget();
	}

	private void populateWidget(){	
		if(StringUtils.isNotBlank(properties)){
			if(dropDownConfig.getItems().contains(properties)){
				int indexOf = dropDownConfig.getItems().indexOf(properties);
				combo.select(indexOf);
			}
			else{
				combo.select(dropDownConfig.getItems().size() - 1);
				text.setVisible(true);
				if (StringUtils.isNotEmpty(properties)){
					text.setText(properties);
					txtDecorator.hide();
				}
				else{
					text.setText("");
					txtDecorator.show();
				}
			}
		}
	}

	private void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
		if(txtDecorator.isVisible())
			toolTipErrorMessage = txtDecorator.getDescriptionText();
						
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		if( combo.getText().equalsIgnoreCase(Constants.PARAMETER)){
			comboBoxParameter.setOption(text.getText());
			comboBoxParameter.setOptionValue(text.getText());
		}else{
			comboBoxParameter.setOption(combo.getText());
		}
		property.put(propertyName,comboBoxParameter.getOption());
		setToolTipErrorMessage();
		return property;
	}

	@Override
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		this.dropDownConfig = (DropDownConfig) widgetConfig;
	}
	
	private String[] convertToArray(List<String> items) {
		String[] stringItemsList = new String[items.size()];
		int index = 0;
		for (String item : items) {
			stringItemsList[index++] = item;
		}
		return stringItemsList;
	}
}
