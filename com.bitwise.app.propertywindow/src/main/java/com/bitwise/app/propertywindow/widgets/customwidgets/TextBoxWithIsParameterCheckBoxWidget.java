package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCheckBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;

/**
 * TextBoxWithIsParameterCheckBoxWidget class creates a text-box with a check-box  
 * Text-Box accepts Alphanumeric text
 * Check-box used to format the text-box text into parameter format
 * 
 * @author Bitwise
 *
 */
public class TextBoxWithIsParameterCheckBoxWidget extends TextBoxWithLabelWidget {

	private PropertyDialogButtonBar propDialogButtonBar;

	public TextBoxWithIsParameterCheckBoxWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propDialogButtonBar = propDialogButtonBar;

	}

	protected void setToolTipErrorMessage() {
		super.setToolTipErrorMessage();
	}

	public LinkedHashMap<String, Object> getProperties() {
		return super.getProperties();
	}

	public void setWidgetConfig(WidgetConfig widgetConfig) {
		super.setWidgetConfig(widgetConfig);
	}

	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		super.attachToPropertySubGroup(container);

	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.customwidgets.TextBoxWithLabelWidget#populateWidget()
	 */
	@Override
	protected void populateWidget() {

		AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox(Constants.IS_PARAMETER).checkBoxLableWidth(100);
		lableAndTextBox.attachWidget(isParameterCheckbox);
		((Button) isParameterCheckbox.getSWTWidgetControl()).addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (textBox.getText() != null && ((Button) event.getSource()).getSelection()) {
					if (!StringUtils.isEmpty(textBox.getText().trim())) {
						textBox.setText("@{" + textBox.getText() + "}");
						textBox.setEnabled(false);
					}
				} else {
					if (textBox.getText() != null && !StringUtils.isEmpty(textBox.getText().trim()))
						textBox.setText(textBox.getText().replace("@{", "").replace("}", ""));
					textBox.setEnabled(true);
				}
				propDialogButtonBar.enableApplyButton(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		if (isParameter(this.propertyValue)) {
			((Button) isParameterCheckbox.getSWTWidgetControl()).setSelection(true);
			textBox.setEnabled(false);
		}

		super.populateWidget();
	}
}
