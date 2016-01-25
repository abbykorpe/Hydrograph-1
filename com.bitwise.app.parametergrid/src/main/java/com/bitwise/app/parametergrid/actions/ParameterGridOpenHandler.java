package com.bitwise.app.parametergrid.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.parametergrid.dialog.ParameterGridDialog;
/**
 * 
 * Handler to open parameter grid
 * 
 * @author Bitwise
 *
 */
public class ParameterGridOpenHandler extends AbstractHandler{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ParameterGridOpenHandler.class);
	
	/**
	 * 
	 * Returns active editor as {@link DefaultGEFCanvas}
	 * 
	 * @return {@link DefaultGEFCanvas}
	 */
	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(getComponentCanvas().getParameterFile() == null ){
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );

			messageBox.setText("Error");
			messageBox.setMessage("Could not open parameter grid. \nPlease save the job file.");
			messageBox.open();
			
			logger.debug("Parameter file does not exist. Need to save job file");
			return null;
		}
		
		ParameterGridDialog parameterGrid = new ParameterGridDialog(Display.getDefault().getActiveShell());
		logger.debug("Created Parameter grid dialog instance");
			try{
				parameterGrid.open();
				logger.debug("Opened parameter grid");
			}catch(Exception e){
				logger.debug("Unable to open parameter grid " , e);
			}
			
		return null;
	}
}
