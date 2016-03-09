package com.bitwise.app.propertywindow.widgets.listeners;


import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.statushandlers.StatusManager;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.Operations;
import com.bitwise.app.common.component.config.TypeInfo;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.Activator;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.interfaces.IOperationClassDialog;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
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
	private Logger logger=LogFactory.INSTANCE.getLogger(XMLConfigUtil.class);
	@Override
	public int getListenerType() {
		
		return SWT.Selection;
	}

	@Override
	public Listener getListener(
			final PropertyDialogButtonBar propertyDialogButtonBar,
			final ListenerHelper helpers, Widget... widgets) {
		final Widget[] widgetList = widgets;

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {

				String comboValue = ((Combo) widgetList[0]).getText();
				if (comboValue.equalsIgnoreCase(Messages.CUSTOM)) {

					if (helpers
							.get(HelperType.OPERATION_CLASS_DIALOG_OK_CONTROL) != null) {
						IOperationClassDialog operationClassDialog = (IOperationClassDialog) helpers
								.get(HelperType.OPERATION_CLASS_DIALOG_OK_CONTROL);
						if (needToShowMessageBox(helpers)) {
							MessageBox messageBox = new MessageBox(new Shell(),
									SWT.ICON_WARNING | SWT.YES | SWT.NO
											| SWT.CANCEL);
							messageBox.setText("Save Changes");
							messageBox
									.setMessage("Do you want to save the changes?");
							int retCode = messageBox.open();
							if (retCode == SWT.YES) {
								saveChangesAndOpenOpeartionClassInEditor(
										widgetList, operationClassDialog);
							} else if (retCode == SWT.NO) {
								discardChangesAndOpenOpeartionClassInEditor(
										widgetList, operationClassDialog);
							}
						} else {
							saveChangesAndOpenOpeartionClassInEditor(
									widgetList, operationClassDialog);
						}
					}

				} else {
					if (helpers
							.get(HelperType.OPERATION_CLASS_DIALOG_OK_CONTROL) != null) {
						IOperationClassDialog operationClassDialog = (IOperationClassDialog) helpers
								.get(HelperType.OPERATION_CLASS_DIALOG_OK_CONTROL);

						MessageBox messageBox = new MessageBox(new Shell(),
								SWT.ICON_WARNING | SWT.YES | SWT.NO
										| SWT.CANCEL);
						messageBox.setText(Messages.INFORMATION);
						messageBox
								.setMessage(Messages.OPEARTION_CLASS_OPEN_BUTTON_MESSAGE);
						int retCode = messageBox.open();
						if (retCode == SWT.YES) {
							openInbuiltOperationClass(comboValue,
									propertyDialogButtonBar);
							operationClassDialog.pressOK();
						} else if (retCode == SWT.NO) {
							openInbuiltOperationClass(comboValue,
									propertyDialogButtonBar);
							operationClassDialog.pressCancel();
						}

					}
				}
			}
		};
		return listener;
	}

	private void discardChangesAndOpenOpeartionClassInEditor(
			final Widget[] widgetList,
			IOperationClassDialog operationClassDialog) {
		boolean flag = FilterOperationClassUtility.openFileEditor(
				((Text) widgetList[1]), null);
		if (!flag) {
			WidgetUtility.errorMessage("File Not Found");
		} else {
			operationClassDialog.pressCancel();
		}
	}

	private void saveChangesAndOpenOpeartionClassInEditor(
			final Widget[] widgetList,
			IOperationClassDialog operationClassDialog) {
		boolean flag = FilterOperationClassUtility.openFileEditor(
				((Text) widgetList[1]), null);
		if (!flag) {
			WidgetUtility.errorMessage("File Not Found");
		} else {
			operationClassDialog.pressOK();
		}
	}
	
	private void openInbuiltOperationClass(String operationName, PropertyDialogButtonBar propertyDialogButtonBar) {
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
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,Messages.CLASS_NOT_EXIST,null);
				StatusManager.getManager().handle(status, StatusManager.BLOCK);
				logger.error(e.getMessage(), e);
			}
		} else {
			WidgetUtility.errorMessage(Messages.SAVE_JOB_MESSAGE);
		}
	}
	
	private boolean needToShowMessageBox(ListenerHelper helpers) {
		PropertyDialogButtonBar opeartionClassDialogButtonBar = (PropertyDialogButtonBar) helpers
				.get(HelperType.OPERATION_CLASS_DIALOG_APPLY_BUTTON);
		PropertyDialogButtonBar propertyDialogButtonBar = (PropertyDialogButtonBar) helpers
				.get(HelperType.PROPERTY_DIALOG_BUTTON_BAR);

		if (opeartionClassDialogButtonBar.isApplyEnabled()
				|| propertyDialogButtonBar.isApplyEnabled()) {
			return true;
		} else {
			return false;
		}
	}
	
}
