package com.bitwise.app.propertywindow.widgets.listeners;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

/**
 * The listener interface for receiving ELTEnableButton events. The class that is interested in processing a
 * ELTEnableButton event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTEnableButtonListener<code> method. When
 * the ELTEnableButton event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTEnableButtonEvent
 */
public class ELTEnableButtonListener implements IELTListener{

	@Override
	public int getListenerType() {
		 
		return SWT.Selection;
	}
	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helpers, Widget... widgets) {
		final Widget[] widgetList = widgets;
				 
		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (((Button)widgetList[0]).getSelection()) {
					((Button)widgetList[1]).setEnabled(false);
					((Button)widgetList[2]).setEnabled(false);
					((Button)widgetList[3]).setEnabled(false);
				} else {
					((Button)widgetList[1]).setEnabled(true);
					((Button)widgetList[2]).setEnabled(true);
					((Button)widgetList[3]).setEnabled(true);
				}
				}
		};
		return listener;
	}

	
}
