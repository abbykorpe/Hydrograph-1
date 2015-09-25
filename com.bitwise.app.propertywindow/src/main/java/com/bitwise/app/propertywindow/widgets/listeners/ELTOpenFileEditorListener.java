package com.bitwise.app.propertywindow.widgets.listeners;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;

public class ELTOpenFileEditorListener implements IELTListener{

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
				if (((Text)widgetList[0]).getText().startsWith("$")) {
					((Text)widgetList[0]).setText(Messages.path);
				} 
				boolean flag = FilterOperationClassUtility.openFileEditor(((Text)widgetList[0]).getText());
				if (!flag) {
					FilterOperationClassUtility.errorMessage("File Not Found"); 
				} else {
					//shell.close(); 
				}
			}
		};
		return listener;
	}

	
}