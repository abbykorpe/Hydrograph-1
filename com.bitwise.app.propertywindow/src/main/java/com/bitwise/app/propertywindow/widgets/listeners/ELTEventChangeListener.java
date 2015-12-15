package com.bitwise.app.propertywindow.widgets.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.utils.WordUtils;

/**
 * 
 * @author Bitwise Sep 22, 2015
 * 
 */

public class ELTEventChangeListener implements IELTListener {

	@Override
	public int getListenerType() {
		return SWT.CHANGED;
	}

	@Override
	public Listener getListener(
			final PropertyDialogButtonBar propertyDialogButtonBar,
			final ListenerHelper helpers, Widget... widgets) {
		final Widget[] wigetList = widgets;

		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.CHANGED) {
					propertyDialogButtonBar.enableApplyButton(true);
				}
			}
		};
		return listener;
	}

}
