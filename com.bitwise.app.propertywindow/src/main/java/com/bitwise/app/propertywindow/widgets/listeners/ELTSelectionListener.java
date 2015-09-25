package com.bitwise.app.propertywindow.widgets.listeners;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class ELTSelectionListener implements IELTListener {

	ControlDecoration txtDecorator;

	@Override
	public int getListenerType() {

		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper,
			Widget... widgets) {
		final Widget[] widgetList = widgets;

		/*
		 * for(Widget widget: widgets){ widgetList.add(widget); }
		 */
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.getObject();
		}

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (((Combo) widgetList[0]).getText().equals("Parameter")) {
					((Text) widgetList[1]).setVisible(true);
					((Text) widgetList[1]).setFocus();

					txtDecorator.hide();
				} else {
					((Text) widgetList[1]).setVisible(false);
					txtDecorator.hide();
				}
				propertyDialogButtonBar.enableApplyButton(true);
			}
		};
		return listener;
	}

}
