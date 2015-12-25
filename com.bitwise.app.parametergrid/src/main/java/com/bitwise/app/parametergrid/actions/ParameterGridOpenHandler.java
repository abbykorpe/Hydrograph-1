package com.bitwise.app.parametergrid.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.parametergrid.dialog.ParameterGridDialog;

public class ParameterGridOpenHandler extends AbstractHandler{
	
	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}
	
	private boolean isDirtyEditor(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().isDirty();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );

			messageBox.setText("Error");
			messageBox.setMessage("Could not open parameter grid. \nPlease save the job file.");
			messageBox.open();
			return null;
		}
		
		ParameterGridDialog parameterGrid = new ParameterGridDialog(Display.getDefault().getActiveShell());
			try{
				parameterGrid.open();
			}catch(Exception e){
				// Do nothing
			}
			
		return null;
	}

}
