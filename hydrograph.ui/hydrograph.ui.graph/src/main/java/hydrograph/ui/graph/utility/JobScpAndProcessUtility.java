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

package hydrograph.ui.graph.utility;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.GradleCommandConstants;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
/**
 * 
 * JobScpAndProcessUtility use to create gradle command and process builder.
 * 
 * @author Bitwise
 *
 */
public class JobScpAndProcessUtility {

	private static Logger logger = LogFactory.INSTANCE.getLogger(JobScpAndProcessUtility.class);
	public static final JobScpAndProcessUtility INSTANCE = new JobScpAndProcessUtility();
	
	private JobScpAndProcessUtility(){
		
	}
	/**
	 * Gradle command to SCP project jar at remote server.
	 * @param job
	 * @return command
	 */
	public  String getLibararyScpCommand(Job job) {
		String command = GradleCommandConstants.GCMD_SCP_JAR + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword();
		return command;
	}
	/**
	 * Gradle command to SCP job xml at remote server.
	 * @param xmlPath
	 * @param debugXmlPath
	 * @param job
	 * @return command
	 */
	public  String getJobXMLScpCommand(String xmlPath, String debugXmlPath, Job job) {
		String command=GradleCommandConstants.GCMD_SCP_JOB_XML + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_JOB_XML + xmlPath.split("/", 2)[1];
		
		if(!"".equalsIgnoreCase(debugXmlPath.trim()))
			command=command +GradleCommandConstants.GPARAM_JOB_DEBUG_XML + debugXmlPath.split("/", 2)[1];
		
		return command;
	}
	/**
	 * Gradle command to SCP param file at remote server.
	 * @param paramFile
	 * @param job
	 * @return command
	 */
	public  String getParameterFileScpCommand(String paramFile, Job job) {
		String command =GradleCommandConstants.GCMD_SCP_PARM_FILE + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_PARAM_FILE + "\""+ paramFile+"\"";
		return command;
	}
	/**
	 * Gradle command to execute job on remote server.
	 * @param xmlPath
	 * @param debugXmlPath
	 * @param paramFile
	 * @param job
	 * @return command
	 */
	public  String getExecututeJobCommand(String xmlPath,String debugXmlPath, String paramFile, Job job) {
		String command =GradleCommandConstants.GCMD_EXECUTE_REMOTE_JOB + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_PARAM_FILE +"\""+ paramFile+"\""
				+ GradleCommandConstants.GPARAM_JOB_XML + xmlPath.split("/", 2)[1];
		
		if(!"".equalsIgnoreCase(debugXmlPath.trim())){
				command= command + GradleCommandConstants.GPARAM_JOB_DEBUG_XML + debugXmlPath.split("/", 2)[1] + GradleCommandConstants.GPARAM_JOB_BASE_PATH 
				+ job.getBasePath() + GradleCommandConstants.GPARAM_UNIQUE_JOB_ID +job.getUniqueJobId();
		}
		
		return command;
	}
	
	/**
	 * Give command that move all external schema file to remote server on created directory same as project relative path.  
	 * @param externalSchemaFiles
	 * @param job
	 * @return String Command to scp external schema files.
	 */
	public String getSchemaScpCommand(List<String> externalSchemaFiles,Job job) {
			String externalSchema =  StringUtils.join(externalSchemaFiles, ",");
			return  GradleCommandConstants.GCMD_SCP_SCHEMA_FILES + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_MOVE_SCHEMA +"\""+externalSchema+"\"";
	}
	
	
	/**
	 * 
	 * @param externalSchemaFiles
	 * @param job
	 * @return
	 */
	public String getSubjobScpCommand(List<String> subJobList,Job job) {
			String subJobFiles =  StringUtils.join(subJobList, ",");
			return  GradleCommandConstants.GCMD_SCP_SUBJOB_FILES + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_MOVE_SUBJOB +"\""+subJobFiles+"\"";
	}
	


	/**
	 * 
	 * return directory creation command to create directory structure on remote server needed to run job and move required files.  
	 * @param job
	 * @param paramFile 
	 * @param xmlPath
	 * @param project
	 * @param externalSchemaFiles
	 * @return String (Command)
	 */
	public  String getCreateDirectoryCommand(Job job,String paramFile,String  xmlPath,String project,List<String> externalSchemaFiles,List<String> subJobList) {
		xmlPath = getDirectoryPath(xmlPath);
		//Get comma separated files from list.
		String schemaFiles="";
		String subJobFiles="";
		if(!externalSchemaFiles.isEmpty())
			schemaFiles = getCommaSeparatedDirectories(externalSchemaFiles);
		
		if(!subJobList.isEmpty()) 
			subJobFiles = getCommaSeparatedDirectories(subJobList);
		
		return  GradleCommandConstants.GCMD_CREATE_DIRECTORIES+ GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_JOB_XML + xmlPath +GradleCommandConstants.GPARAM_MOVE_PARAM_FILE +project+"/"+GradleCommandConstants.REMOTE_FIXED_DIRECTORY_PARAM + GradleCommandConstants.GPARAM_MOVE_SCHEMA_FILES + schemaFiles + GradleCommandConstants.GPARAM_MOVE_SUBJOB_FILES + subJobFiles + GradleCommandConstants.GPARAM_MOVE_JAR + project+"/"+GradleCommandConstants.REMOTE_FIXED_DIRECTORY_LIB ;
	}

	/**
	 * 
	 * Create process builder as per operating system.
	 * @param project
	 * @param gradleCommand
	 * @return process builder
	 */
	public  ProcessBuilder getProcess(IProject project, String gradleCommand) {
		String[] runCommand = new String[3];
		if (OSValidator.isWindows()) {
			String[] command = { Messages.CMD, "/c", gradleCommand };
			runCommand = command;

		} else if (OSValidator.isMac()) {
			String[] command = { Messages.SHELL, "-c", gradleCommand };
			runCommand = command;
		}

		ProcessBuilder processBuilder = new ProcessBuilder(runCommand);
		processBuilder.directory(new File(project.getLocation().toOSString()));
		processBuilder.redirectErrorStream(true);
		return processBuilder;

	} 

	/**
	 * Remove file name and return directory path.
	 * @param path
	 * @return
	 */
	public  String getDirectoryPath(String path){
		if(path.length() > 0 )
		{
		    int endIndex = path.lastIndexOf("/");
		    if (endIndex != -1)  
		    {
		    	path = path.substring(0, endIndex);
		    	return path;
		    }
		
		}
		return "";
	}
	
	/**
	 * Create directory comma separated string from list.
	 * 
	 * @param fileList
	 * @return
	 */
	private String getCommaSeparatedDirectories(List<String> fileList ){
		List<String> directories = new ArrayList<>();
		for (String schemaFile : fileList) {
					schemaFile=getDirectoryPath(schemaFile);
					directories.add(schemaFile);
			}
		
		String files=StringUtils.join(directories, ",");
		return files;
	}
	
	/**
	 * Collect all external schema files from active editor, also check for subjob and nested subjob. 
	 * @return list of external schema files.
	 */
	public List<String> getExternalSchemaList() {
		List<String> externalSchemaPathList=new ArrayList<>();	
		ELTGraphicalEditor editor = (ELTGraphicalEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor != null && editor instanceof ELTGraphicalEditor) {
			GraphicalViewer graphicalViewer = (GraphicalViewer) ((GraphicalEditor) editor)
					.getAdapter(GraphicalViewer.class);
			for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); ite.hasNext();) {
				EditPart editPart = (EditPart) ite.next();
				if (editPart instanceof ComponentEditPart) {
					Component component = ((ComponentEditPart) editPart).getCastedModel();
					Schema  schema = (Schema) component.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
					if(schema!=null && schema.getIsExternal()){
						externalSchemaPathList.add(schema.getExternalSchemaPath());
					}
					if(Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
						  String subJobPath=(String) component.getProperties().get(Constants.PATH_PROPERTY_NAME);
						  checkSubJobForExternalSchema(externalSchemaPathList, subJobPath); 
						}
				}
			}
		}
		return externalSchemaPathList;
	}
	
	/**
	 * Collect all subjob file from active editor, also check for nested subjob. 
	 * @return list of subjob.
	 */
	public List<String> getSubJobList() {
		ArrayList<String> subJobList=new ArrayList<>();
		ELTGraphicalEditor editor = (ELTGraphicalEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor != null && editor instanceof ELTGraphicalEditor) {
			GraphicalViewer graphicalViewer = (GraphicalViewer) ((GraphicalEditor) editor)
					.getAdapter(GraphicalViewer.class);
			for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); ite.hasNext();) {
				EditPart editPart = (EditPart) ite.next();
				if (editPart instanceof ComponentEditPart) {
					Component component = ((ComponentEditPart) editPart).getCastedModel();
					if(Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
					  String subJobPath=(String) component.getProperties().get(Constants.PATH_PROPERTY_NAME);
					  subJobList.add(subJobPath);
					  checkNestedSubJob(subJobList, subJobPath);
					}
				}
			}
		}
		return subJobList;
	}

	/**
	 * Check for subjob container and nested subjob.
	 * @param subJobList
	 * @param subJobPath
	 */
	private void checkNestedSubJob(List<String> subJobList,String subJobPath) {
			Object obj=null;
			try {
				obj = CanvasUtils.INSTANCE.fromXMLToObject(new FileInputStream(new File(JobManager.getAbsolutePathFromFile(new Path(subJobPath)))));
			} catch (FileNotFoundException e) {
				logger.error("subjob xml not found "+e);
			}
			if(obj!=null && obj instanceof Container){
			  Container container = (Container) obj;
			  for (Component component : container.getChildren()) {
					if(Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
						  String subJob=(String) component.getProperties().get(Constants.PATH_PROPERTY_NAME);
						  subJobList.add(subJob);
						  checkNestedSubJob(subJobList, subJob);
					}
			}
		  }
	}
	
	/**
	 * Check nested subjob to collect external schema files.
	 * @param externalSchemaPathList
	 * @param subJobPath
	 */
	private void checkSubJobForExternalSchema(List<String> externalSchemaPathList,String subJobPath) {
		Object obj=null;
		try {
			obj = CanvasUtils.INSTANCE.fromXMLToObject(new FileInputStream(new File(JobManager.getAbsolutePathFromFile(new Path(subJobPath)))));
		} catch (FileNotFoundException e) {
			logger.error("subjob xml not found "+e);
		}
		if(obj!=null && obj instanceof Container){
		  Container container = (Container) obj;
		  for (Component component : container.getChildren()) {
			  	Schema  schema = (Schema) component.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
				if(schema!=null && schema.getIsExternal()){
					externalSchemaPathList.add(schema.getExternalSchemaPath());
				}
				if(Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
					  String subJob=(String) component.getProperties().get(Constants.PATH_PROPERTY_NAME);
					  checkSubJobForExternalSchema(externalSchemaPathList, subJob);
				}
		  	}
		}
	}
	
}
