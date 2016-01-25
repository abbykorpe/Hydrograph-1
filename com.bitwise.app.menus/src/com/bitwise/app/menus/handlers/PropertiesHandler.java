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
 *Creates Properties Handler 
 * @author Bitwise
 *
 */
public class PropertiesHandler extends AbstractHandler implements IHandler {
	private Logger logger=LogFactory.INSTANCE.getLogger(ConsoleHandler.class);
	/**
	 * open Properties view
	 * @param event
	 * @return Object
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.showView(IPageLayout.ID_PROP_SHEET);
		} catch (PartInitException e) {
			logger.error(e.getMessage());
		}
		return null;
	}  

}
