package com.bitwise.app.menus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;

/**
 *Creates Outline Handler 
 * @author Bitwise
 *
 */
public class OutlineHandler extends AbstractHandler implements IHandler {
	private Logger logger=LogFactory.INSTANCE.getLogger(ConsoleHandler.class);
	/**
	 * open Outline view
	 * @param event
	 * @return Object
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.showView("org.eclipse.ui.views.ContentOutline");
		} catch (PartInitException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
