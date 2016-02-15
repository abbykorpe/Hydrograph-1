package com.bitwise.app.propertywindow.widgets.listeners;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;

/**
 * The listener interface for receiving ELTCreateNewClass events. The class that is interested in processing a
 * ELTCreateNewClass event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTCreateNewClassListener<code> method. When
 * the ELTCreateNewClass event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTCreateNewClassEvent
 */
public class ELTCreateNewClassListener implements IELTListener{
	
	private WidgetConfig widgetConfig;
	@Override
	public int getListenerType() {
		
		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helpers, Widget... widgets) {
		final Widget[] widgetList = widgets;
			
		if (helpers != null) {
			widgetConfig = (WidgetConfig) helpers.get(HelperType.WIDGET_CONFIG);
		}
		
		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				String comboValue = ((Combo) widgetList[0]).getText();
				if (comboValue.equals(Messages.CUSTOM)) {
					FilterOperationClassUtility.createNewClassWizard((Text) widgetList[1], widgetConfig);
					propertyDialogButtonBar.enableApplyButton(true);
				}
			}
		};
		return listener;
	}
	

	
}
