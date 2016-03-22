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

 
package com.bitwise.app.propertywindow.widgets.customwidgets;

/**
 * TextBoxWithIsParameterCheckBoxWidget class creates a text-box with a check-box Text-Box accepts Alphanumeric text
 * Check-box used to format the text-box text into parameter format
 * 
 * @author Bitwise
 * 
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCheckBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;

public class TextBoxWithIsParameterCheckBoxWidget extends TextBoxWithLabelWidget {

	private PropertyDialogButtonBar propDialogButtonBar;
	private String lastValue;
	private List<String> availableFieldList = new ArrayList<>();

	public TextBoxWithIsParameterCheckBoxWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propDialogButtonBar = propDialogButtonBar;

	}

	private void loadAvailableFields() {
		for (Link link : getComponent().getTargetConnections())
			availableFieldList = SchemaPropagationHelper.INSTANCE.getInputFieldListForLink(link);
	}

	/* *
	 * This method used to set tool-tip of text-box.
	 */
	protected void setToolTipErrorMessage() {
		super.setToolTipErrorMessage();
	}

	public LinkedHashMap<String, Object> getProperties() {
		loadNewFieldAndPropagate(textBox.getText());
		return super.getProperties();
	}

	/*
	 * This method sets the configuration of widget.
	 */
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		super.setWidgetConfig(widgetConfig);
	}

	/*
	 * This method attaches widget to property sub group.
	 */
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		super.attachToPropertySubGroup(container);
	}

	/* *
	 * This method populates the widget. *
	 */
	@Override
	protected void populateWidget() {
		loadAvailableFields();
		final AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox(Constants.IS_PARAMETER)
				.checkBoxLableWidth(100);
		lableAndTextBox.attachWidget(isParameterCheckbox);

		textBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (StringUtils.isBlank(textBox.getText().trim())) {

					((Button) isParameterCheckbox.getSWTWidgetControl()).setEnabled(false);
				} else
					((Button) isParameterCheckbox.getSWTWidgetControl()).setEnabled(true);

				if (isFieldNameExists(textBox.getText())) {
					Point lastCursorPoint = textBox.getSelection();
					String currentValue = textBox.getText();
					if(StringUtils.isNotBlank(lastValue))
					textBox.setText(lastValue);
					textBox.setSelection(lastCursorPoint);
					txtDecorator.setDescriptionText(currentValue + " - already exists");
					txtDecorator.show();
					textBox.setToolTipText(Messages.bind(Messages.EMPTY_FIELD, textBoxConfig.getName()));
				}

				lastValue = textBox.getText();
			}
		});

		((Button) isParameterCheckbox.getSWTWidgetControl()).addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String parameterText = Constants.PARAMETER_SUFFIX + textBox.getText() + Constants.PARAMETER_PREFIX;
				if (StringUtils.isNotBlank(textBox.getText()) && ((Button) event.getSource()).getSelection()) {
					if (!isFieldNameExists(parameterText)) {
						textBox.setText(parameterText);
						textBox.setEnabled(false);
						propDialogButtonBar.enableApplyButton(true);
					} else {
						((Button) event.getSource()).setSelection(false);
						txtDecorator.setDescriptionText(parameterText + " - already exists");
						txtDecorator.show();
						textBox.setToolTipText(Messages.bind(Messages.EMPTY_FIELD, textBoxConfig.getName()));
					}

				} else {
					if (StringUtils.isNotBlank(textBox.getText())) {
						textBox.setText(textBox.getText().replace(Constants.PARAMETER_SUFFIX, "")
								.replace(Constants.PARAMETER_PREFIX, ""));
						propDialogButtonBar.enableApplyButton(true);
					}
					textBox.setEnabled(true);
				}

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

	private boolean isFieldNameExists(String newfieldName) {
		if (availableFieldList.contains(StringUtils.lowerCase(newfieldName)))
			return true;
		else
			return false;
	}

	private void loadNewFieldAndPropagate(String fieldName) {
		Map<String, ComponentsOutputSchema> schemaMap = new LinkedHashMap<String, ComponentsOutputSchema>();
		ComponentsOutputSchema newComponentsOutputSchema = new ComponentsOutputSchema();
		ComponentsOutputSchema sourceOutputSchema = null;
		for (Link link : getComponent().getTargetConnections())
			sourceOutputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
		if (sourceOutputSchema != null) {
			newComponentsOutputSchema.copySchemaFromOther(sourceOutputSchema);
		}
		if (StringUtils.isNotBlank(textBox.getText()))
			newComponentsOutputSchema.getFixedWidthGridRowsOutputFields().add(createSchemaForNewField(fieldName));
		schemaMap.put(Constants.FIXED_OUTSOCKET_ID, newComponentsOutputSchema);
		SchemaPropagation.INSTANCE.continuousSchemaPropagation(getComponent(), schemaMap);
	}

	private FixedWidthGridRow createSchemaForNewField(String fieldName) {
		FixedWidthGridRow fixedWidthGridRow = SchemaPropagationHelper.INSTANCE.createFixedWidthGridRow(fieldName);
		fixedWidthGridRow.setDataType(1);
		fixedWidthGridRow.setDataTypeValue(Integer.class.getCanonicalName());
		return fixedWidthGridRow;
	}

}
