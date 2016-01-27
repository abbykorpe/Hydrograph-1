package com.bitwise.app.propertywindow.widgets.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty.ELTRuntimePropertiesWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty.SubGraphPropertiesWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys.SecondaryColumnKeysWidget;

/**
 * The listener interface for receiving ELTRuntimeButtonClick events. The class that is interested in processing a
 * ELTRuntimeButtonClick event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTRuntimeButtonClickListener<code> method. When
 * the ELTRuntimeButtonClick event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTRuntimeButtonClickEvent
 */
public class ELTRuntimeButtonClickListener implements IELTListener {

	@Override
	public int getListenerType() {
		return SWT.Selection;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, final ListenerHelper helpers, Widget... widgets) {
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection) {
					//((ELTRuntimePropertiesWidget)helpers.object).newWindowLauncher();
					if(helpers.object instanceof ELTRuntimePropertiesWidget)
						((ELTRuntimePropertiesWidget)helpers.object).newWindowLauncher();
					else if(helpers.object instanceof SecondaryColumnKeysWidget) 
						((SecondaryColumnKeysWidget)helpers.object).newWindowLauncher();
					else if(helpers.object instanceof SubGraphPropertiesWidget) 
						((SubGraphPropertiesWidget)helpers.object).newWindowLauncher();

				}

			}
		};

		return listener;
	}
}
