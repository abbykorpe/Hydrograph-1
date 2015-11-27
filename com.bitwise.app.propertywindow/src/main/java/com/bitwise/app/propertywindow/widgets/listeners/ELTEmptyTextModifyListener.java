package com.bitwise.app.propertywindow.widgets.listeners;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.common.datastructures.tooltip.TootlTipErrorMessage;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The listener interface for receiving ELTEmptyTextModify events. The class that is interested in processing a
 * ELTEmptyTextModify event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTEmptyTextModifyListener<code> method. When
 * the ELTEmptyTextModify event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTEmptyTextModifyEvent
 */
public class ELTEmptyTextModifyListener implements IELTListener {
	private ValidationStatus validationStatus;
	private TootlTipErrorMessage tootlTipErrorMessage;

	
	private ControlDecoration fieldNameDecorator;
	private ControlDecoration fieldNameMustJava;
	@Override
	public int getListenerType() {

		return SWT.Modify;
	}

	@Override
	public Listener getListener(
			PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers, Widget... widgets) {
		
		if (helpers != null) {
			validationStatus = (ValidationStatus) helpers.get(HelperType.VALIDATION_STATUS); 
			tootlTipErrorMessage = (TootlTipErrorMessage)helpers.get(HelperType.TOOLTIP_ERROR_MESSAGE); 
		}
		
		final Widget[] widgetList = widgets;
		fieldNameDecorator = WidgetUtility
				.addDecorator((Text) widgetList[0],
						Messages.OperationClassBlank);
		fieldNameMustJava = WidgetUtility.addDecorator(
				(Text) widgetList[0], Messages.INVALID_FILE);
		
		tootlTipErrorMessage.setErrorMessage(Messages.OperationClassBlank);
		//validationStatus.setIsValid(true);
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (!((Button) widgetList[2]).getSelection()) {
					if (((Text) widgetList[0]).getText().trim().isEmpty()) {
						fieldNameDecorator.show();
						fieldNameMustJava.hide();
						((Button) widgetList[1]).setEnabled(false);
						((Text) widgetList[0]).setBackground(new Color(Display
								.getDefault(), 255, 255, 204));
						validationStatus.setIsValid(false);
					} else {
						fieldNameDecorator.hide();
						fieldNameMustJava.hide();
						((Button) widgetList[1]).setEnabled(true);
						validationStatus.setIsValid(true);
					} 
					if (!WidgetUtility.isFileExtention(
							(((Text) widgetList[0]).getText()).trim(), ".java")
							&& !(((Text) widgetList[0]).getText().trim()
									.isEmpty())) {
						fieldNameMustJava.show();
						fieldNameDecorator.hide();
						((Text) widgetList[0]).setBackground(new Color(Display
								.getDefault(), 255, 255, 204));
						validationStatus.setIsValid(false);
					} else {
						((Text) widgetList[0]).setBackground(new Color(Display
								.getDefault(), 255, 255, 255));
						fieldNameMustJava.hide();
						validationStatus.setIsValid(true);
					}
				}
				else
				{
					fieldNameDecorator.hide();
					fieldNameMustJava.hide();
					validationStatus.setIsValid(true);
				}
				setToolTipErrorMessage(fieldNameDecorator,fieldNameMustJava);
			}

		};
		//setToolTipErrorMessage(fieldNameDecorator,fieldNameMustJava);
		return listener;
	}
	
	private void setToolTipErrorMessage(ControlDecoration fieldNameDecorator, ControlDecoration fieldNameMustJava){
		String errmsg=null;
		if(fieldNameMustJava.isVisible())
			errmsg = fieldNameMustJava.getDescriptionText();
		
		if(fieldNameDecorator.isVisible())
			errmsg = errmsg + "\n" + fieldNameDecorator.getDescriptionText();
		
		tootlTipErrorMessage.setErrorMessage(errmsg);
		
	}
}
