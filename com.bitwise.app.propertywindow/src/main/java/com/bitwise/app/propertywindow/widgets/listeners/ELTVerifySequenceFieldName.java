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

 
package com.bitwise.app.propertywindow.widgets.listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

/**
 * Verify Text format of Field Names
 * 
 * @author Bitwise
 *
 */
public class ELTVerifySequenceFieldName implements IELTListener {
	private ControlDecoration txtDecorator;
	Logger LOGGER = LogFactory.INSTANCE.getLogger(ELTVerifySequenceFieldName.class);

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.listeners.IELTListener#getListenerType()
	 */
	@Override
	public int getListenerType() {
		return SWT.Verify;
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.listeners.IELTListener#getListener(com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar, com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper, org.eclipse.swt.widgets.Widget[])
	 */
	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper,
			Widget... widgets) {
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.get(HelperType.CONTROL_DECORATION);
		}
		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				String string = event.text;
				Matcher matchs = Pattern.compile(Constants.REGEX).matcher(string);
				LOGGER.debug("Verifying text format");
				if (!matchs.matches()) {
					txtDecorator.setDescriptionText(Messages.CHARACTERSET);
					txtDecorator.show();
					event.doit = false;

				} else
					txtDecorator.hide();

			}
		};
		return listener;
	}
}
