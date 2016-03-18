package com.bitwise.app.propertywindow.widgets.listeners;


import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

/**
 * The listener interface for receiving ELTFileDialogSelection events. The class that is interested in processing a
 * ELTFileDialogSelection event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTFileDialogSelectionListener<code> method. When
 * the ELTFileDialogSelection event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTFileDialogSelectionEvent
 */
public class ELTFileDialogSelectionListener implements IELTListener{

	private Logger LOGGER = LogFactory.INSTANCE.getLogger(ELTFileDialogSelectionListener.class);
	Shell shell;
	private ControlDecoration txtDecorator;
	private Component currentComponent;
	private String[] filterFileExtensions={Constants.ADD_ALL_FIELDS_SYMBOL+Constants.JOB_EXTENSION};
	@Override
	public int getListenerType() {
		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers,
			final Widget... widgets) {
		final Button button = ((Button) widgets[0]);
		button.getShell();
		if (helpers != null) {
			txtDecorator = (ControlDecoration) helpers.get(HelperType.CONTROL_DECORATION);
			currentComponent = (Component) helpers.get(HelperType.CURRENT_COMPONENT);
		}

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection) {
					File file = null;
					String path = null;
					FileDialog filedialog = new FileDialog(button.getShell(), SWT.None);
					filedialog.setFileName(((Text) widgets[1]).getText());

					if (StringUtils.equals(Constants.SUBGRAPH_COMPONENT, currentComponent.getComponentName())) {
						filedialog.setFilterExtensions(filterFileExtensions);
						path = filedialog.open();
						if (StringUtils.isNotEmpty(path)) {
							if (isFileExistsOnLocalFileSystem(new Path(path), ((Text) widgets[1]),propertyDialogButtonBar)) {
								file = new File(path);
								((Text) widgets[1]).setText(file.getAbsolutePath());
								propertyDialogButtonBar.enableApplyButton(true);
								txtDecorator.hide();
							}
						}
					} else {
						path = filedialog.open();
						file = new File(path);
						if (StringUtils.isNotEmpty(path)) {
							((Text) widgets[1]).setText(file.getAbsolutePath());
							propertyDialogButtonBar.enableApplyButton(true);
							txtDecorator.hide();
						}
					}
				}
			}
		};
		return listener;
	}
	
	private boolean isFileExistsOnLocalFileSystem(IPath jobFilePath, Text textBox, PropertyDialogButtonBar propertyDialogButtonBar) {
		jobFilePath=jobFilePath.removeFileExtension().addFileExtension(Constants.XML_EXTENSION_FOR_IPATH);
		try {
			if (ResourcesPlugin.getWorkspace().getRoot().getFile(jobFilePath).exists())
				return true;
			else if (jobFilePath.toFile().exists())
				return true;
		} catch (Exception exception) {
			LOGGER.error("Error occured while cheking file on local file system", exception);
		}
		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.YES
				| SWT.NO);
		messageBox.setMessage(jobFilePath.lastSegment()+Messages.FILE_DOES_NOT_EXISTS);
		messageBox.setText(jobFilePath.toString() +Messages.NOT_EXISTS);
		int response = messageBox.open();
		if (response == SWT.YES) {
			jobFilePath=jobFilePath.removeFileExtension().addFileExtension(Constants.JOB_EXTENSION_FOR_IPATH);
			textBox.setText(jobFilePath.toString());
			propertyDialogButtonBar.enableApplyButton(true);
		}else
			textBox.setText("");
		return false;
	}

}
