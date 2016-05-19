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

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.PathUtility;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.job.GradleCommandConstants;
import hydrograph.ui.graph.job.Job;

public class JobScpAndProcessUtility {

	public static final JobScpAndProcessUtility INSTANCE = new JobScpAndProcessUtility();
	
	
	private JobScpAndProcessUtility(){
		
	}
	
	public  String getLibararyScpCommand(Job job) {
		String command = GradleCommandConstants.GCMD_SCP_JAR + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword();
		return command;
	}

	public  String getJobXMLScpCommand(String xmlPath, String debugXmlPath, Job job) {
		String command=GradleCommandConstants.GCMD_SCP_JOB_XML + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_JOB_XML + xmlPath.split("/", 2)[1];
		
		if(!"".equalsIgnoreCase(debugXmlPath.trim()))
			command=command +GradleCommandConstants.GPARAM_JOB_DEBUG_XML + debugXmlPath.split("/", 2)[1];
		
		return command;
	}

	public  String getParameterFileScpCommand(String paramFile, Job job) {
		String command =GradleCommandConstants.GCMD_SCP_PARM_FILE + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_PARAM_FILE + "\""+ paramFile+"\"";
		return command;
	}
 
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
				+ job.getPassword() + GradleCommandConstants.GPARAM_MOVE_SCHEMA + externalSchema;
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
	public  String getCreateDirectoryCommand(Job job,String paramFile,String  xmlPath,String project,List<String> externalSchemaFiles) {
		xmlPath = getDirectoryPath(xmlPath);
		//Get comma separated files from list.
		String externalFiles=StringUtils.join(externalSchemaFiles, ",");
		externalSchemaFiles.clear();
		
		for (String schemaFile : externalFiles.split(",")) {
					schemaFile=getDirectoryPath(schemaFile);
			    	externalSchemaFiles.add(schemaFile);
			}
		
		externalFiles=StringUtils.join(externalSchemaFiles, ",");

		return  GradleCommandConstants.GCMD_CREATE_DIRECTORIES+ GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_JOB_XML + xmlPath +GradleCommandConstants.GPARAM_MOVE_PARAM_FILE +project+"/"+GradleCommandConstants.REMOTE_FIXED_DIRECTORY_PARAM + GradleCommandConstants.GPARAM_MOVE_SCHEMA_FILES + externalFiles + GradleCommandConstants.GPARAM_MOVE_JAR + project+"/"+GradleCommandConstants.REMOTE_FIXED_DIRECTORY_LIB ;
	}

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
}
