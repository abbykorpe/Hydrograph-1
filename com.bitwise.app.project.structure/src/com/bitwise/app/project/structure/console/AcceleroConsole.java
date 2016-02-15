package com.bitwise.app.project.structure.console;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.internal.console.ConsoleView;

import com.bitwise.app.common.interfaces.console.IAcceleroConsole;
import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;

/**
 * 
 * Accelero's custom console view
 * 
 * @author Bitwise
 *
 */

public class AcceleroConsole extends ConsoleView implements IAcceleroConsole{

	private boolean consoleClosed;
	private static String DEFAULT_CONSOLE = "NewConsole";

	@Override
	public void partActivated(IWorkbenchPart part) {
		super.partActivated(part);
		consoleClosed = true;
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		super.partDeactivated(part);
		consoleClosed = false;
	}

	@Override
	protected void partVisible(IWorkbenchPart part) {
		super.partVisible(part);
		consoleClosed = false;
	}


	@Override
	public void partClosed(IWorkbenchPart part) {
		super.partClosed(part);
		consoleClosed = true;
	}

	@Override
	public boolean isConsoleClosed() {
		return consoleClosed;
	}

	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}

	private IConsole getConsole(String consoleName,IConsoleManager conMan){		
		IConsole[] existing = conMan.getConsoles();
		MessageConsole messageConsole=null;
		for (int i = 0; i < existing.length; i++) {			
			if (existing[i].getName().equals(consoleName)){
				messageConsole=(MessageConsole) existing[i];

				return messageConsole;
			}	
		}
		return null;
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		super.partOpened(part);

		if(getComponentCanvas()!=null){
			ConsolePlugin plugin = ConsolePlugin.getDefault();
			IConsoleManager conMan = plugin.getConsoleManager();

			String consoleName = getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();

			IConsole consoleToShow = getConsole(consoleName, conMan);	

			if(consoleToShow!=null){
				conMan.showConsoleView(consoleToShow);
			}else{
				addDummyConsole();
			}
		}
	}

	private void addDummyConsole(){
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();

		IConsole consoleToShow = getConsole(DEFAULT_CONSOLE, conMan);	

		if(consoleToShow == null){
			consoleToShow = createNewMessageConsole(DEFAULT_CONSOLE,conMan);
		}

		conMan.showConsoleView(consoleToShow);
	}


	private MessageConsole createNewMessageConsole(String consoleName,IConsoleManager conMan) {
		MessageConsole messageConsole;
		messageConsole = new MessageConsole(consoleName, null);
		conMan.addConsoles(new IConsole[] { messageConsole });
		return messageConsole;
	}
}
