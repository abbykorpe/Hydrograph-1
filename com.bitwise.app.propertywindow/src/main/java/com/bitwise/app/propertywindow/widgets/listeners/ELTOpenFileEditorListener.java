package com.bitwise.app.propertywindow.widgets.listeners;


import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.Operations;
import com.bitwise.app.common.component.config.TypeInfo;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The listener interface for receiving ELTOpenFileEditor events. The class that is interested in processing a
 * ELTOpenFileEditor event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTOpenFileEditorListener<code> method. When
 * the ELTOpenFileEditor event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTOpenFileEditorEvent
 */
public class ELTOpenFileEditorListener implements IELTListener{
	IJavaProject javaProject;
	private static final String INFORMATION = "Information";
	private Logger logger=LogFactory.INSTANCE.getLogger(XMLConfigUtil.class);
	@Override
	public int getListenerType() {
		
		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helpers, Widget... widgets) {
		final Widget[] widgetList = widgets;
		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				
				String comboValue = ((Combo) widgetList[0]).getText();
				if (comboValue.equalsIgnoreCase("Custom")) {
					if (((Text) widgetList[1]).getText().startsWith("$")) {
						((Text) widgetList[1]).setText(Messages.path);
					}
					boolean flag = FilterOperationClassUtility.openFileEditor(((Text) widgetList[1]), null);
					if (!flag) {
						WidgetUtility.errorMessage("File Not Found");
					}
				} else {
					openInbuiltOperationClass(comboValue, widgetList[1], propertyDialogButtonBar);
				}
			}
		};
		return listener;
	}

	private void openInbuiltOperationClass(String operationName, Widget widgetList,
			PropertyDialogButtonBar propertyDialogButtonBar) {
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
		propertyDialogButtonBar.enableApplyButton(true);
		javaProject = FilterOperationClassUtility.getIJavaProject();
		if (javaProject != null) {
			try {
				IType findType = javaProject.findType(operationClassName);
				JavaUI.openInEditor(findType);
			} catch (JavaModelException | PartInitException e) {
				logger.error(e.getMessage(),e);
			}
		}
		else
		{
			WidgetUtility.errorMessage(Messages.SAVE_JOB_MESSAGE);
		}
	}

	
}
