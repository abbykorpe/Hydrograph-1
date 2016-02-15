package com.bitwise.app.propertywindow.widgets.listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

/**
 * Listener for text box which will either accept numeric value or parameter.
 * if the value is parameter, then it will remove @{} in order to edit the value(focus in). 
 */
public class VerifyNumbericOrParameterFocusInListener implements IELTListener{

	@Override
	public int getListenerType() {
		return SWT.FocusIn;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper,  Widget... widgets) {
		final Widget[] widgetList = widgets;
			Listener listener=new Listener() {
				@Override
				public void handleEvent(Event event) {
					String string=((Text) widgetList[0]).getText().trim();
					Matcher matchs=Pattern.compile("[\\d]*").matcher(string);
					if(!matchs.matches() && StringUtils.isNotBlank(string)){
						((Text) widgetList[0]).setText(string.replace("@{", "").replace("}", ""));
					}
				}
			};
		return listener;
	}
}