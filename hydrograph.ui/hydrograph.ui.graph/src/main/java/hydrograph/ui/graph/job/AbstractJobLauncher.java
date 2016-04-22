/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package hydrograph.ui.graph.job;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.joblogger.JobLogger;
import hydrograph.ui.logging.factory.LogFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.slf4j.Logger;


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
	abstract public void launchJobInDebug(String xmlPath, String debugXmlPath, String basePath, String paramFile, Job job, DefaultGEFCanvas gefCanvas, String uniqueJobID);
	
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