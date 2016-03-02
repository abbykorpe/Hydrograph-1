package com.bitwise.app.graph.job;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.joblogger.JobLogger;
import com.bitwise.app.logging.factory.LogFactory;

/**
 * 
 * The abstract class to launch job
 * 
 * @author Bitwise
 *
 */
abstract public class AbstractJobLauncher {
	private static Logger logger = LogFactory.INSTANCE.getLogger(AbstractJobLauncher.class);
	
	/**
	 * 
	 * Launch job
	 * 
	 * @param xmlPath
	 * @param paramFile
	 * @param job
	 * @param gefCanvas
	 */
	abstract public void launchJob(String xmlPath, String paramFile, Job job, DefaultGEFCanvas gefCanvas);
	
	/**
	 * Enables locked resouces..like job canvas
	 * 
	 * @param {@link DefaultGEFCanvas}
	 */
	protected void enableLockedResources(final DefaultGEFCanvas gefCanvas) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				gefCanvas.enableRunningJobResource();
			}
		});
	}
	
	/**
	 * 
	 * Refresh project directory corresponding to given {@link DefaultGEFCanvas}
	 * 
	 * @param gefCanvas
	 */
	protected void refreshProject(DefaultGEFCanvas gefCanvas) {
		IEditorPart iEditorPart = ((IEditorPart)gefCanvas);		
		String projectName = ((IFileEditorInput) iEditorPart.getEditorInput()).getFile().getProject().getName();		
		IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		try {
			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.error("Error while refreshing the project", e);
		}
	}
	
	/**
	 * 
	 * Initialize job logger
	 * 
	 * @param gefCanvas
	 * @return
	 */
	protected JobLogger initJobLogger(DefaultGEFCanvas gefCanvas,boolean logSystemInfo,boolean logJobStartInfo) {
		final JobLogger joblogger = new JobLogger(gefCanvas.getActiveProject(), gefCanvas.getJobName());
		if(logJobStartInfo)
			joblogger.logJobStartInfo();
		
		if(logSystemInfo)
			joblogger.logSystemInformation();
		
		return joblogger;
	}
}
