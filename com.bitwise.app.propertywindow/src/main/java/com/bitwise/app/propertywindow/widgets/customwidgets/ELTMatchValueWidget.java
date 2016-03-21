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

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import com.bitwise.app.common.datastructure.property.MatchValueProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTRadioButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

/**
 * @author Bitwise
 *
 */
public class ELTMatchValueWidget extends AbstractWidget {

	private final String propertyName;
	private final LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private Object properties;
	private String[] buttonText = new String[] { Constants.FIRST,
			Constants.LAST, Constants.ALL };
	private Button[] buttons = new Button[buttonText.length];
	private MatchValueProperty matchValue;

	public ELTMatchValueWidget(
			ComponentConfigrationProperty componentConfigrationProp,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProp, componentMiscellaneousProperties,
				propertyDialogButtonBar);
		this.propertyName = componentConfigrationProperty.getPropertyName();
		//this.properties = componentConfigrationProperty.getPropertyValue();
		if (componentConfigrationProperty.getPropertyValue() == null) {
			matchValue = new MatchValueProperty();
		} else {
			matchValue = (MatchValueProperty) componentConfigrationProperty
					.getPropertyValue();
		}
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();
		eltSuDefaultSubgroupComposite
		.numberOfBasicWidgets(buttonText.length + 1);

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable(Constants.MATCH);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		setPropertyHelpWidget((Control) eltDefaultLable.getSWTWidgetControl());
		
		SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button button = ((Button) event.widget);

				matchValue.setMatchValue(button.getText());
				matchValue.setRadioButtonSelected(true);
				propertyDialogButtonBar.enableApplyButton(true);
			}
		};

		for (int i = 0; i < buttonText.length; i++) {
			ELTRadioButton eltRadioButton = new ELTRadioButton(buttonText[i]);
			eltSuDefaultSubgroupComposite.attachWidget(eltRadioButton);
			buttons[i] = ((Button) eltRadioButton.getSWTWidgetControl());
			((Button) eltRadioButton.getSWTWidgetControl())
			.addSelectionListener(selectionListener);
		}
		buttons[0].setSelection(true);

		populateWidget();
	}


	public void populateWidget() {
		for (int i = 1; i < buttons.length; i++) {
			if (StringUtils.isNotBlank(matchValue.getMatchValue())) {
				if (matchValue.getMatchValue().equalsIgnoreCase(
						buttons[i].getText())) {
					buttons[i].setSelection(true);
					buttons[0].setSelection(false);
				}
			} else {
				buttons[0].setSelection(true);
			}
		}
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, matchValue);
		return property;
	}

}
