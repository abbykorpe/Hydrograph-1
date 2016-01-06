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
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

public class ELTInputCountListener implements IELTListener{
	
	private ControlDecoration txtDecorator;
	private int value;
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
				//((Text) widgetList[0]).setTextLimit(2);
				String string =((Text) widgetList[0]).getText().trim();	
				char[] chars = string.toCharArray();
					 for(int i=0;i<chars.length;i++){
						 if(event.type == SWT.Verify){
						 if(chars.length > 2){
							 txtDecorator.setDescriptionText(Messages.PORT_VALUE);
								txtDecorator.show();
								propertyDialogButtonBar.enableOKButton(false);
								propertyDialogButtonBar.enableApplyButton(false);  
						 }
						 }else{
							 txtDecorator.hide();
								propertyDialogButtonBar.enableOKButton(true);
								propertyDialogButtonBar.enableApplyButton(true);
						 }
					 }
			
				
				
				if(StringUtils.isNotEmpty(string)){
					
					}
					if(event.type == SWT.Modify){
						if(Integer.parseInt(string) < 2 || Integer.parseInt(string) > 25 ){
						txtDecorator.setDescriptionText(Messages.PORT_VALUE);
						txtDecorator.show();
						propertyDialogButtonBar.enableOKButton(false);
						propertyDialogButtonBar.enableApplyButton(false);
					}else
					{
						txtDecorator.hide();
						propertyDialogButtonBar.enableOKButton(true);
						propertyDialogButtonBar.enableApplyButton(true);
						value = Integer.parseInt(string);
					}
				}
				
			}
		};
	return listener;

	}

}
