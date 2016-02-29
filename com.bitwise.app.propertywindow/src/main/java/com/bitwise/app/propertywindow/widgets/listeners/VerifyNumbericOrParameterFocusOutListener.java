package com.bitwise.app.propertywindow.widgets.listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

/**
 * Listener for text box which will either accept numeric value or parameter.
 * if the value is parameter, then it will add @{} after the focus is out.
 */
public class VerifyNumbericOrParameterFocusOutListener implements IELTListener{

	@Override
	public int getListenerType() {
		return SWT.FocusOut;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper,  Widget... widgets) {
		final Widget[] widgetList = widgets;
			Listener listener=new Listener() {
				@Override
				public void handleEvent(Event event) {
					String string=((Text) widgetList[0]).getText().trim();
					Matcher matcher=Pattern.compile("[\\d]*").matcher(string);
					if(!matcher.matches() && StringUtils.isNotBlank(string)){
						((Text) widgetList[0]).setText(string.replace("@{", "").replace("}", ""));
						((Text) widgetList[0]).setText("@{"+((Text) widgetList[0]).getText()+"}");
						((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));
					}
				}
			};
		return listener;
	}
}