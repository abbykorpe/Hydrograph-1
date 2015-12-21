package com.bitwise.app.propertywindow.handlers;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Display;

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
				// Do nothing
			}
		return null;
	}

}
