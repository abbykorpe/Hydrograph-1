package com.bitwise.app.propertywindow.widgets.listeners;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.common.component.config.Operations;
import com.bitwise.app.common.component.config.TypeInfo;
import com.bitwise.app.common.util.XMLConfigUtil;
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
				if (comboValue.equalsIgnoreCase(Messages.CUSTOM)) {
					((Text) widgetList[1]).setText("");
					((Text)widgetList[1]).setEnabled(true);
					FilterOperationClassUtility.enableAndDisableButtons(true);
				} else {
					setOperationClassNameInTextBox(comboValue, widgetList[1]);
					((Text)widgetList[1]).setEnabled(false);
					FilterOperationClassUtility.enableAndDisableButtons(false);
				}
			}
		};
		return listener;
	}
	private void setOperationClassNameInTextBox(String operationName, Widget widget) {
		String operationClassName = null;
		Operations operations = XMLConfigUtil.INSTANCE.getComponent(FilterOperationClassUtility.getComponentName())
				.getOperations();
		List<TypeInfo> typeInfos = operations.getStdOperation();
		for (int i = 0; i < typeInfos.size(); i++) {
			if (typeInfos.get(i).getName().equalsIgnoreCase(operationName)) {
				operationClassName = typeInfos.get(i).getClazz();
				break;
			}
		}
		((Text) widget).setText(operationClassName);
		
	}
	
	

}
