package com.bitwise.app.graph.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.OSValidator;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.joblogger.JobLogger;
import com.bitwise.app.parametergrid.dialog.ParameterGridDialog;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;
import com.bitwise.app.propertywindow.runconfig.*;

/**
 * Handler use to run the job using gradle command.
 * 
 * @author Bitwise
 * @version 1.0
 * @since 2015-10-27
 */
public class RunJobHandler extends AbstractHandler {

	/** The logger. */
	private Logger logger = LogFactory.INSTANCE.getLogger(RunJobHandler.class);

	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}
	
	private boolean isDirtyEditor(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().isDirty();
	}
	
	
	/*
	 * 
	 * Execute command to run the job.
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().doSave(null);
		}
		
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell());
		runConfigDialog.open();
		if(!runConfigDialog.proceedToRunGraph()){
			return null;
		}
		
		ParameterGridDialog parameterGrid = new ParameterGridDialog(Display.getDefault().getActiveShell());
		parameterGrid.setVisibleParameterGridNote(false);
		parameterGrid.open();
		if(parameterGrid.canRunGraph() == false){
			logger.debug("Not running graph");
			return null;
		}
		logger.debug("property File :"+parameterGrid.getParameterFile());
		final JobLogger joblogger = new JobLogger(getComponentCanvas().getActiveProject(), getComponentCanvas().getJobName());
		joblogger.logJobStartInfo();
		joblogger.logSystemInformation();
		try {
			IEditorPart iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			String XML_PATH = "";
			if (iEditorPart != null) {
				XML_PATH = iEditorPart.getEditorInput().getToolTipText().replace(Messages.JOBEXTENSION, Messages.XMLEXTENSION);
				Process process=executeRunJob(XML_PATH,parameterGrid.getParameterFile());
				final InputStream stream = process.getInputStream();

				/**
					 * Read the log in new thread and redirect it to product
					 * console.
					 */
					new Thread(new Runnable() {
						public void run() {
							BufferedReader reader = null;
							try {
								reader = new BufferedReader(
										new InputStreamReader(stream));
								String line = null;
								while ((line = reader.readLine()) != null) {
									joblogger.logMessage(line);
								}
							} catch (Exception e) {
								logger.info("Error occured while reading run job log.");
							} finally {
								if (reader != null) {
									try {
										reader.close();
									} catch (IOException e) {
										logger.error("Ignore the exception", e);
									}
								}
							}
							joblogger.logJobEndInfo();
							joblogger.close();
						}
						
					}).start();
				} else
					WidgetUtility.errorMessage("Please open a graph to run.");
			
			
			
		} catch (Exception ex) {
			logger.error("Error in Run Job",ex);
		}

		return null;
	}

	/**
	 * Find console using name if exist or create new.
	 *
	 * @param name            the console name
	 * @return console
	 */
	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++) {
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		}
		// no console found, so create a new one
		MessageConsole newConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { newConsole });
		return newConsole;
	}
	
	
	
	/**
	 * Execute run job.
	 *
	 * @param XML_PATH the xml path that contain xml file name to run.
	 * @return the process
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Process executeRunJob(String XML_PATH,String paramFile) throws IOException{
		String projectName = XML_PATH.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		String[] runCommand=new String[3];
		if (OSValidator.isWindows()) {
			logger.info("This is windows.");
			String[] command = {Messages.CMD,"/c",Messages.GRADLE_RUN + " " + Messages.XMLPATH + "=\""+ XML_PATH.split("/", 2)[1] + "\" "+ Messages.PARAM_FILE+"=\""+paramFile+"\""};
			runCommand=command;
		} else if (OSValidator.isMac()) {
			logger.debug("This is Mac.");
			String[] command = {"/usr/bin/bash","-c",Messages.GRADLE_RUN + " " + Messages.XMLPATH + "="+ XML_PATH.split("/", 2)[1] + " "+ Messages.PARAM_FILE+"="+paramFile};
			runCommand=command;
		} else if (OSValidator.isUnix()) {
			logger.debug("This is Unix or Linux");
		} else if (OSValidator.isSolaris()) {
			logger.debug("This is Solaris");
		} else {
			logger.debug("Your OS is not support!!");
		}
		
		ProcessBuilder pb = new ProcessBuilder(runCommand);
		pb.directory(new File(project.getLocation().toOSString()));
		pb.redirectErrorStream(true);
		Process process = pb.start();
		return process;
	}
	
	/**
	 * Return list of param files.
	 * 
	 * @param project
	 * @return String  list of properties files to resolve xml param values.
	 */
	 String getListOfPropertyFiles(IProject project){
		StringBuffer sb = new StringBuffer();
		File folder = new File(project.getFolder(Messages.PARAM).getLocation().toString());
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  sb.append(Messages.PARAM+"/"+listOfFiles[i].getName()+",");
		      }  
		    }
		    if(sb.toString().isEmpty())
		    	return "";
		    else
		    	return sb.substring(0, sb.length()-1);
	} 
	
}
