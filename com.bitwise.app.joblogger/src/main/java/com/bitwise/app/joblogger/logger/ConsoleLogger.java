package com.bitwise.app.joblogger.logger;

import java.io.IOException;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;

/**
 * 
 * Class to create Console logger
 * 
 * @author Bitwise
 *
 */
public class ConsoleLogger extends AbstractJobLogger{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ConsoleLogger.class);
	
	private final String CONSOLE_NAME="Gradle Console";
	private MessageConsoleStream messageConsoleStream;
	
	public ConsoleLogger(String projectName, String jobName) {
		super(projectName, jobName);
		initConsoleStream();
		logger.debug("Created console logger stream");
	}
	
	@Override
	public void log(String message) {
		messageConsoleStream.println(getLogStamp() + message);
		logger.debug("logged message on console - message - {}" , message );
	}
	
	/**
	 * Find console using name if exist or create new.
	 *
	 * @param name            the console name
	 * @return console
	 */
	private void initConsoleStream() {
		MessageConsole messageConsole = getMessageConsole();
		
		messageConsoleStream=messageConsole.newMessageStream();
		logger.debug("Created message console stream");
	}

	/**
	 * get message console object
	 * 
	 * @param messageConsole
	 * @return
	 */
	private MessageConsole getMessageConsole() {
		IConsoleManager conMan = getConsoleManager();	
		MessageConsole messageConsole = getConsoleFromExistingConsoles(conMan);		
		if(messageConsole == null){
			messageConsole = createNewMessageConsole(conMan);
			logger.debug("No existing console found, created new one");
		}
		return messageConsole;
	}

	/**
	 * 
	 * returns gradle console if it is registered with console manager exist
	 * 
	 * @param conMan - console manager
	 * @return
	 */
	private MessageConsole getConsoleFromExistingConsoles(IConsoleManager conMan) {
		IConsole[] existing = getExistingConsoles(conMan);		
		MessageConsole messageConsole = getExistingMessageConsole(existing);
		return messageConsole;
	}

	
	/**
	 * 
	 * Creates new message console and register it with console manager
	 * 
	 * @param conMan
	 * @return
	 */
	private MessageConsole createNewMessageConsole(IConsoleManager conMan) {
		MessageConsole messageConsole;
		messageConsole = new MessageConsole(CONSOLE_NAME, null);
		conMan.addConsoles(new IConsole[] { messageConsole });
		logger.debug("Created message console");
		return messageConsole;
	}

	/**
	 * 
	 * returns Gradle console if it is registered with console manager 
	 * 
	 * @param existing
	 * @return - MessageConsole
	 */
	private MessageConsole getExistingMessageConsole(IConsole[] existing) {
		MessageConsole messageConsole=null;
		for (int i = 0; i < existing.length; i++) {
			if (CONSOLE_NAME.equals(existing[i].getName())){
				messageConsole=(MessageConsole) existing[i];
				logger.debug("We have an message console");
				break;
			}	
		}
		return messageConsole;
	}

	
	/**
	 * 
	 * get all consoles registered with default console manager
	 * 
	 * @param conMan
	 * @return - IConsole[]
	 */
	private IConsole[] getExistingConsoles(IConsoleManager conMan) {
		IConsole[] existing = conMan.getConsoles();
		logger.debug("Retrived existing consoles. Number of console - {}" , existing.length);
		return existing;
	}

	/**
	 * 
	 *  get default console manager
	 * 
	 * @return ConsoleManager
	 */
	private IConsoleManager getConsoleManager() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		logger.debug("Retived Console plugin object");
		IConsoleManager conMan = plugin.getConsoleManager();
		logger.debug("Retrived Console manager");
		return conMan;
	}

	@Override
	public void close() {
		try {
			messageConsoleStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void logWithNoTimeStamp(String message) {
		messageConsoleStream.println(message);
		
	}	
}
