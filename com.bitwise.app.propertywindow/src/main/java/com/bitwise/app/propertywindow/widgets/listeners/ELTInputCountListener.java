package com.bitwise.app.propertywindow.widgets.listeners;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

public class ELTInputCountListener implements IELTListener{

	private ControlDecoration txtDecorator;
	private ValidationStatus validationStatus; 
	@Override
	public int getListenerType() {
		return SWT.Modify;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helper, Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.get(HelperType.CONTROL_DECORATION);
			validationStatus = (ValidationStatus) helper.get(HelperType.VALIDATION_STATUS);
		}

		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				String string =((Text) widgetList[0]).getText().trim();	
				if(StringUtils.isNotEmpty(string)){
					if(event.type == SWT.Modify){
						if(Integer.parseInt(string) < 2 || Integer.parseInt(string) > 25 ){
							txtDecorator.setDescriptionText(Messages.PORT_VALUE);
							txtDecorator.show();
							propertyDialogButtonBar.enableOKButton(false);
							propertyDialogButtonBar.enableApplyButton(false);
							setValidationStatus(false);
						}else
						{
							txtDecorator.hide();
							propertyDialogButtonBar.enableOKButton(true);
							setValidationStatus(true);
						}
					}
				}

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
