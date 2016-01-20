package com.bitwise.app.graph.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;



import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.OSValidator;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.joblogger.JobLogger;
import com.bitwise.app.parametergrid.dialog.ParameterGridDialog;
import com.bitwise.app.propertywindow.runconfig.RunConfigDialog;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

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
	public Object execute(ExecutionEvent event) {
		
		setBaseEnabled(false);
		DefaultGEFCanvas gefCanvas = getComponentCanvas();
		gefCanvas.disableRunningJobResource();
		
		
		
		if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
			try{
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().doSave(null);
			}catch(Exception e){
				logger.debug("Unable to save graph ", e);
				setBaseEnabled(true);
				gefCanvas.enableRunningJobResource();
			}
			
		}
		
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell());
		runConfigDialog.open();
		if(!runConfigDialog.proceedToRunGraph()){
			setBaseEnabled(true);
			gefCanvas.enableRunningJobResource();
			return null;
		}
		
		String clusterPassword = runConfigDialog.getClusterPassword()!=null ? runConfigDialog.getClusterPassword():"";

		ParameterGridDialog parameterGrid = new ParameterGridDialog(Display.getDefault().getActiveShell());
		parameterGrid.setVisibleParameterGridNote(false);
		parameterGrid.open();
		if(parameterGrid.canRunGraph() == false){
			logger.debug("Not running graph");
			setBaseEnabled(true);
			gefCanvas.enableRunningJobResource();
			return null;
		}
		logger.debug("property File :"+parameterGrid.getParameterFile());
		final JobLogger joblogger = new JobLogger(getComponentCanvas().getActiveProject(), getComponentCanvas().getJobName());
		joblogger.logJobStartInfo();
		joblogger.logSystemInformation();
		try {
			IEditorPart iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			final String projectName=((IFileEditorInput)iEditorPart.getEditorInput()).getFile().getProject().getName();
			String XML_PATH = "";
			if (iEditorPart != null) {
				XML_PATH = iEditorPart.getEditorInput().getToolTipText().replace(Messages.JOBEXTENSION, Messages.XMLEXTENSION);
				Process process=executeRunJob(XML_PATH,parameterGrid.getParameterFile(),clusterPassword);
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
							setBaseEnabled(true);
							IProject iProject=ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
							try {
								iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
							} catch (CoreException e) {
								logger.error("Error while refreshing the project",e);
							}
						}
						
					}).start();
					gefCanvas.enableRunningJobResource();
				} else
					WidgetUtility.errorMessage("Please open a graph to run.");
			
			
			
		} catch (Exception ex) {
			logger.error("Error in Run Job",ex);
		}

		//setBaseEnabled(true);
		//gefCanvas.enableRunningJobResource();
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
	private Process executeRunJob(String XML_PATH,String paramFile,String clusterPassword) throws IOException{
		String projectName = XML_PATH.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		String[] runCommand=new String[3];
		if (OSValidator.isWindows()) {
			logger.info("This is windows.");
			String[] command = {Messages.CMD,"/c",Messages.GRADLE_RUN + " " + Messages.XMLPATH + "=\""+ XML_PATH.split("/", 2)[1] + "\" "+ Messages.PARAM_FILE+"=\""+paramFile+"\" "+ Messages.CLUSTER_PASSWORD+"=\""+clusterPassword+"\""};
			runCommand=command;
		} else if (OSValidator.isMac()) {
			logger.debug("This is Mac.");
			String[] command = {"bash","-c",Messages.GRADLE_RUN + " " + Messages.XMLPATH + "=\""+ XML_PATH.split("/", 2)[1] + "\" "+ Messages.PARAM_FILE+"=\""+paramFile+"\" "+ Messages.CLUSTER_PASSWORD+"=\""+clusterPassword+"\""};
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
