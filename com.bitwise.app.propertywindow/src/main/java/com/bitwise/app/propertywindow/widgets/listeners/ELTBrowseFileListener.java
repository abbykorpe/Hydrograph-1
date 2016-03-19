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


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;

/**
 * The listener interface for receiving ELTBrowseFile events. The class that is interested in processing a ELTBrowseFile
 * event implements this interface, and the object created with that class is registered with a component using the
 * component's <code>addELTBrowseFileListener<code> method. When
 * the ELTBrowseFile event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTBrowseFileEvent
 */
public class ELTBrowseFileListener implements IELTListener{
	
	@Override
	public int getListenerType() {
		
		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helpers, Widget... widgets) {
		final Widget[] widgetList = widgets;
				
		if (helpers != null) {
		}
		
		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
					FilterOperationClassUtility.browseFile("java",(Text)widgetList[0]);
				propertyDialogButtonBar.enableApplyButton(true);
				}
		};
		return listener;
	}

	
}
