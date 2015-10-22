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
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

/**
 * The listener interface for receiving ELTSelection events. The class that is interested in processing a ELTSelection
 * event implements this interface, and the object created with that class is registered with a component using the
 * component's <code>addELTSelectionListener<code> method. When
 * the ELTSelection event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTSelectionEvent
 */
public class ELTSelectionListener implements IELTListener {
	private ControlDecoration txtDecorator;
	private ValidationStatus validationStatus;
	
	@Override
	public int getListenerType() {

		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper, Widget... widgets) {
		final Widget[] widgetList = widgets;

		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.get(HelperType.CONTROL_DECORATION);
			validationStatus = (ValidationStatus) helper.get(HelperType.VALIDATION_STATUS);
			//since this is dropdown, so set it to true as it is valid
			setValidationStatus(true);
		}

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (((Combo) widgetList[0]).getText().equals("Parameter")) {
					((Text) widgetList[1]).setVisible(true);
					((Text) widgetList[1]).setFocus();
					txtDecorator.hide();
					setValidationStatus(false);
				} else {
					((Text) widgetList[1]).setVisible(false);
					txtDecorator.hide();
					setValidationStatus(true);
				}
				propertyDialogButtonBar.enableApplyButton(true);
			}
		};
		return listener;
	}
	
	private void setValidationStatus(boolean status) {
		if(validationStatus != null){
			validationStatus.setIsValid(status);
		}
	}
}
