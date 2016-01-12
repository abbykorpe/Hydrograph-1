package com.bitwise.app.propertywindow.runconfig;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class EmptyTextListener implements ModifyListener {

	private ControlDecoration errorDecorator;
	private Button okButton;

	
	public EmptyTextListener(Button button) {
		this.okButton = button;
		
	}

	@Override
	public void modifyText(ModifyEvent event) {
		String txt= ((Text)event.getSource()).getText();
		
		if (StringUtils.isEmpty(txt)) {
			errorDecorator = WidgetUtility.addDecorator((Text)event.getSource(),Messages.EMPTY_FIELD);
			errorDecorator.show();
			okButton.setEnabled(false);
		} else {
			if(errorDecorator!=null)
				errorDecorator.hide();
			okButton.setEnabled(true);
		}

	}

}
