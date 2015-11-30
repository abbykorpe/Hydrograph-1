package com.bitwise.app.propertywindow.widgets.listeners;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

/**
 * The listener interface for receiving ELTModify events. The class that is interested in processing a ELTModify event
 * implements this interface, and the object created with that class is registered with a component using the
 * component's <code>addELTModifyListener<code> method. When
 * the ELTModify event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTModifyEvent
 */
public class ELTModifyListener implements IELTListener{
	private ControlDecoration txtDecorator;
	private ValidationStatus validationStatus;
	
	@Override
	public int getListenerType() {
		return SWT.Modify;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper, Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.get(HelperType.CONTROL_DECORATION);
			validationStatus = (ValidationStatus) helper.get(HelperType.VALIDATION_STATUS); 
		}
		Listener listener=new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				String string=((Text)widgetList[0]).getText().trim();
				if(event.type==SWT.Modify){
					if(StringUtils.isBlank(string)){
						//txtDecorator.setDescriptionText(Messages.EMPTYFIELDMESSAGE);
						txtDecorator.show();
						((Text) widgetList[0]).setToolTipText(txtDecorator.getDescriptionText());
						((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 204));
						setValidationStatus(false);
					}else{
						//txtDecorator.setDescriptionText(Messages.EMPTYFIELDMESSAGE);
						txtDecorator.hide();
						((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));
						setValidationStatus(true);
					}
					
				}else{
					//txtDecorator.setDescriptionText(Messages.EMPTYFIELDMESSAGE);
					txtDecorator.hide();
					((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));
					setValidationStatus(true);
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
