package com.bitwise.app.menus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleConstants;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
/**
 *Creates Console Handler 
 * @author Bitwise
 *
 */
public class ConsoleHandler extends AbstractHandler implements IHandler {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ConsoleHandler.class);
	private static final String consoleView = "com.bitwise.app.project.structure.console.AcceleroConsole";
	
	/**
	 * open console view
	 * @param event
	 * @return Object
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(consoleView);
		} catch (PartInitException e) {
			logger.error("Failed to show view : ", e);
		}
		return null;
	}

}
