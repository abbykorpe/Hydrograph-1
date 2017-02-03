/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package hydrograph.ui.propertywindow.widgets.listeners;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Widget;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

/**
 * The class OverWriteWidgetSelectionListener listens the OverWrite Combo box
 * selection. It attaches the Listener to Overwrite selection. Basically, it
 * facilitates the pop up message when user selects True option for overwrite.
 * 
 * 
 * @see IELTListener
 */

public class OverWriteWidgetSelectionListener implements IELTListener {
	private static final String INFORMATION = "Information";

	@Override
	public int getListenerType() {

		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper,
			Widget... widgets) {
		final Widget[] widgetList = widgets;

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (StringUtils.equalsIgnoreCase(((Combo) widgetList[0]).getText(), String.valueOf(Boolean.TRUE))) {
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(),
							SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText(INFORMATION);
					messageBox.setMessage("All files at given location will be overwritten.");
					messageBox.open();
				}
			}
		};
		return listener;
	}
}
