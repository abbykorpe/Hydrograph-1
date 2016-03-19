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

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving ELTSelectionTask events. The class that is interested in processing a
 * ELTSelectionTask event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTSelectionTaskListener<code> method. When
 * the ELTSelectionTask event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTSelectionTaskEvent
 */
public abstract class ELTSelectionTaskListener implements IELTListener {
	@Override
	public int getListenerType() {

		return SWT.Selection;
	}

	@Override
	public Listener getListener(
			final PropertyDialogButtonBar propertyDialogButtonBar,
			final ListenerHelper helper, final Widget... widgets) {

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				selectionListenerAction(propertyDialogButtonBar,helper, widgets);
			}
		};
		return listener;
	}
	
	/**
	 * Selection listener action.
	 * 
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 * @param helpers
	 *            the helpers
	 * @param widgets
	 *            the widgets
	 */
	public abstract void selectionListenerAction(PropertyDialogButtonBar propertyDialogButtonBar,
			final ListenerHelper helpers, Widget... widgets);
	
}
