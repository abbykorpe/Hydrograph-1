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
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helpers, Widget... widgets) {
		final Widget[] widgetList = widgets;
				
		if (helpers != null) {
		}
		
		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
					FilterOperationClassUtility.browseFile("java",(Text)widgetList[0]);
				}
		};
		return listener;
	}

	
}
