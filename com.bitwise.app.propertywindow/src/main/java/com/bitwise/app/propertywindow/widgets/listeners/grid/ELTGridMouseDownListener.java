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

 
package com.bitwise.app.propertywindow.widgets.listeners.grid;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ELTMouseDownListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The listener interface for receiving ELTGridMouseDown events. The class that is interested in processing a
 * ELTGridMouseDown event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTGridMouseDownListener<code> method. When
 * the ELTGridMouseDown event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTGridMouseDownEvent
 */
public class ELTGridMouseDownListener extends ELTMouseDownListener{
	public ControlDecoration fieldNameDecorator;
	@Override
	public void mouseDownAction(PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helpers, Widget... widgets){
		fieldNameDecorator =	WidgetUtility.addDecorator((Control)widgets[0],Messages.FIELDNAMEERROR)	;
		fieldNameDecorator.hide();
	}

}
