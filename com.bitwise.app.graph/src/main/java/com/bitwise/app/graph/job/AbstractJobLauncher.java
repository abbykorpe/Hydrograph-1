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
import com.bitwise.app.logging.factory.LogFactory;

abstract public class AbstractJobLauncher {
	private static Logger logger = LogFactory.INSTANCE.getLogger(AbstractJobLauncher.class);
		
	abstract public void launchJob(String xmlPath, String paramFile, Job job, DefaultGEFCanvas gefCanvas);
		
	protected void enableLockedResources(final DefaultGEFCanvas gefCanvas) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				gefCanvas.enableRunningJobResource();
			}
		});
	}
	
	protected void refreshProject(DefaultGEFCanvas gefCanvas) {
		//IEditorPart iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IEditorPart iEditorPart = ((IEditorPart)gefCanvas);
		
		String projectName = ((IFileEditorInput) iEditorPart.getEditorInput()).getFile().getProject().getName();
		
		IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		try {
			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.error("Error while refreshing the project", e);
		}
	}
}
