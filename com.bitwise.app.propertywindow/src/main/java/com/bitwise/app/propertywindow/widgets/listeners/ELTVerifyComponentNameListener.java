package com.bitwise.app.propertywindow.widgets.listeners;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

/**
 * The listener interface for receiving ELTVerifyComponentName events. The class that is interested in processing a
 * ELTVerifyComponentName event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTVerifyComponentNameListener<code> method. When
 * the ELTVerifyComponentName event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTVerifyComponentNameEvent
 */
public class ELTVerifyComponentNameListener implements IELTListener {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTVerifyComponentNameListener.class);
	private ArrayList<String> names;
	private String oldName;
	
	private ControlDecoration txtDecorator;
	
	@Override
	public int getListenerType() {
		return SWT.Verify;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers,  Widget... widgets) {
		Widget[] widgetList = widgets;
		final Text text = (Text) widgetList[0];
		
		if(helpers != null){
			if (helpers != null) {
				txtDecorator = (ControlDecoration) helpers.get(HelperType.CONTROL_DECORATION);
				txtDecorator.hide();
			}
		}
		

		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (e.type == SWT.Verify) {
					logger.debug("<<<<<<<<<<"+e.text.toString()+">>>>>>>>>>>");
					String currentText = ((Text) e.widget).getText();
					String newName = (currentText.substring(0, e.start) + e.text + currentText.substring(e.end)).trim();
					Matcher matchName = Pattern.compile("[\\w+]*").matcher(newName.replaceAll("[\\W&&[\\ \\.\\-]]*", ""));
					logger.debug("new text: {}", newName);
					if (newName == null || newName.equals("")) {
						// e.doit=false;
						text.setBackground(new Color(Display.getDefault(), 255, 255, 204));
						text.setToolTipText(Messages.FIELD_LABEL_ERROR);
						propertyDialogButtonBar.enableOKButton(false);
						propertyDialogButtonBar.enableApplyButton(false);
						txtDecorator.setDescriptionText(Messages.FIELD_LABEL_ERROR);
						txtDecorator.show();
					}else if(!matchName.matches()){
						text.setToolTipText(Messages.INVALID_CHARACTERS);
						txtDecorator.setDescriptionText(Messages.INVALID_CHARACTERS);
						txtDecorator.show();
						e.doit=false;
					} else if(!newName.equalsIgnoreCase(oldName) && !isUniqueCompName(newName)) {
						text.setBackground(new Color(Display.getDefault(),255,255,204));
						text.setToolTipText(Messages.FIELD_LABEL_ERROR);
						propertyDialogButtonBar.enableOKButton(false);
						propertyDialogButtonBar.enableApplyButton(false);
						txtDecorator.setDescriptionText(Messages.FIELD_LABEL_ERROR);
						txtDecorator.show();
					}
					else{
						text.setBackground(null);
						text.setToolTipText("");
						text.setMessage("");
						propertyDialogButtonBar.enableOKButton(true);
						propertyDialogButtonBar.enableApplyButton(true);
						txtDecorator.hide();
					}
				}
			}
		};
		return listener;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	public void setNames(ArrayList<String> names) {
		this.names = names;
	}
	
	private boolean isUniqueCompName(String componentName) {
		componentName = componentName.trim();
		boolean result = true;

		for (String cname : names) {
			if (cname.equalsIgnoreCase(componentName)) {
				result = false;
				break;
			}
		}
		logger.debug("result: {}", result);
		return result;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
}
