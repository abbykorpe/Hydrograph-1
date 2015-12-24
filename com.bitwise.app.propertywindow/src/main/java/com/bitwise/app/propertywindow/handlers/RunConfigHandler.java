package com.bitwise.app.propertywindow.handlers;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.propertywindow.runconfig.RunConfigDialog;

/**
 * The Class Component.
 * 
 * @author Bitwise
 */

public class RunConfigHandler extends AbstractHandler implements IHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		 
		 RunConfigDialog runConfig = new RunConfigDialog(Display.getDefault().getActiveShell());
			try{
				runConfig.open();
			}catch(Exception e){
				MessageDialog.openError(new Shell(), "Error", "Exception occured while opening run configuration -\n"+e.getMessage());
			}
		return null;
	}

}
