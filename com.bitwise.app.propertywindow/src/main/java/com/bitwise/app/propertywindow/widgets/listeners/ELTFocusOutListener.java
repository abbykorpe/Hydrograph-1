package com.bitwise.app.propertywindow.widgets.listeners;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class ELTFocusOutListener implements IELTListener {

	ControlDecoration txtDecorator;

	@Override
	public int getListenerType() {
		return SWT.FocusOut;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper,
			Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.getObject();
			
		}

		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				String charSet = ((Text) widgetList[0]).getText().trim();
				System.out.println("charSet: " + charSet);
				if (event.type == SWT.FocusOut) {

					if (charSet == null || charSet == "") {
						txtDecorator.setDescriptionText(Messages.EMPTYFIELDMESSAGE);
						txtDecorator.show();
						((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 204));
						((Text) widgetList[0]).setToolTipText(Messages.EMPTYFIELDMESSAGE);
					} else {
						txtDecorator.hide();
						((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));
					}

				} else {
					txtDecorator.hide();
					((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));

				}

			}
		};

		return listener;
	}

}