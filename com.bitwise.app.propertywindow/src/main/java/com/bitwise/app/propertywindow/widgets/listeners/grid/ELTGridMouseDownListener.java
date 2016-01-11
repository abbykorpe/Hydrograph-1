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
