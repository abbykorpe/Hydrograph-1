package com.bitwise.app.propertywindow.widgets.listeners;


import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

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

	Shell shell;
	private ControlDecoration txtDecorator;
	
	@Override
	public int getListenerType() {
		return SWT.Selection;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers, final Widget... widgets) {
		final Button button = ((Button)widgets[0]);
		button.getShell();
		if(helpers != null){
			txtDecorator = (ControlDecoration) helpers.get(HelperType.CONTROL_DECORATION);
		}
		
		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(event.type==SWT.Selection){
					FileDialog filedialog=new FileDialog(button.getShell(),SWT.None);
					String path=filedialog.open();
					if(StringUtils.isNotEmpty(path)){
						File file= new File(path);
						((Text)widgets[1]).setText(file.getAbsolutePath());
						propertyDialogButtonBar.enableApplyButton(true);
						txtDecorator.hide();
					} 
				}
			}
		};
		return listener;
	}
}
