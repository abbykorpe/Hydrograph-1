package com.bitwise.app.propertywindow.widgets.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;

public class OperationClassComboChangeListener implements IELTListener{
	@Override
	public int getListenerType() {
		
		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers,
			Widget... widgets) {
		final Widget[] widgetList = widgets;

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				String comboValue = ((Combo) widgetList[0]).getText();
				propertyDialogButtonBar.enableApplyButton(true);
				if (Messages.CUSTOM.equalsIgnoreCase(comboValue) && !FilterOperationClassUtility.isCheckBoxSelected()) {
					((Text) widgetList[1]).setText("");
					((Text) widgetList[1]).setEnabled(true);
					FilterOperationClassUtility.enableAndDisableButtons(true, false);
				} else {
					if(FilterOperationClassUtility.isCheckBoxSelected())
					{
						MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
						messageBox.setText(Messages.ERROR);
						messageBox.setMessage(Messages.CHECKBOX_DISABLE_MESSAGE);
						if (messageBox.open() == SWT.OK) {
							((Text) widgetList[1]).setText("");
						} 
					}
					else
					{
						FilterOperationClassUtility.setOperationClassNameInTextBox(comboValue, (Text)widgetList[1]);
						((Text) widgetList[1]).setEnabled(false);
						FilterOperationClassUtility.enableAndDisableButtons(false, false);
					}
				}
			}
		};
		return listener;
	}

}
