package com.bitwise.app.menus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;

/**
 *Creates Palette Handler 
 * @author Bitwise
 *
 */

public class PaletteHandler extends AbstractHandler implements IHandler {
	private Logger logger=LogFactory.INSTANCE.getLogger(ConsoleHandler.class);
	/**
	 * open Palette view
	 * @param event
	 * @return Object
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.showView("org.eclipse.gef.ui.palette_view");
		} catch (PartInitException e) {
			logger.error(e.getMessage());
		}
		return null;
	}  

}
