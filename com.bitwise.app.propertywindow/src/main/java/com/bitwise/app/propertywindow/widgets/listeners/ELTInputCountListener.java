package com.bitwise.app.propertywindow.widgets.listeners;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

public class ELTInputCountListener implements IELTListener{
	
	private ControlDecoration txtDecorator;

	@Override
	public int getListenerType() {
		return SWT.Modify;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helper, Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.get(HelperType.CONTROL_DECORATION);
		}

		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				String string=((Text) widgetList[0]).getText().trim();
				
				if(event.type == SWT.Modify){
					if((Integer.parseInt(string)) < 2){
					
						txtDecorator.setDescriptionText(Messages.PORT_VALUE);
						txtDecorator.show();
						propertyDialogButtonBar.enableOKButton(false);
						propertyDialogButtonBar.enableApplyButton(false);
					}else
					{
						txtDecorator.hide();
						propertyDialogButtonBar.enableOKButton(true);
						propertyDialogButtonBar.enableApplyButton(true);
					}
				}
				
			}
		};
	return listener;

	}

}
