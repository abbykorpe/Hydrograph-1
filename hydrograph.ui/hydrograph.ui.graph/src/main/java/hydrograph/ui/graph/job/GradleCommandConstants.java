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
/**
 * Provides constants to build gradle command
 * 
 * @author Bitwise
 *
 */
public class GradleCommandConstants {
	
	public static final String GCMD_SCP_JAR="gradle scpJarFiles ";
	public static final String GCMD_SCP_PARM_FILE="gradle scpParameterFile ";
	public static final String GCMD_SCP_JOB_XML="gradle scpJobXML ";
	public static final String GCMD_SCP_DEBUG_JOB_XML="gradle scpDebugJobXML ";
	public static final String GCMD_EXECUTE_REMOTE_JOB="gradle executeRemoteJob ";
	public static final String GCMD_EXECUTE_LOCAL_JOB="gradle executeLocalJob ";
	public static final String GCMD_EXECUTE_DEBUG_LOCAL_JOB="gradle executeDebugLocal ";
	public static final String GCMD_EXECUTE_DEBUG_REMOTE_JOB="gradle executeDebugRemoteJob ";
	public static final String GCMD_KILL_REMOTE_JOB="gradle killRemoteJob ";
	public static final String GCMD_CREATE_DIRECTORIES="gradle createDirectories ";
	public static final String GCMD_SCP_SCHEMA_FILES="gradle scpSchemaFiles ";
	public static final String GCMD_SCP_SUBJOB_FILES="gradle scpSubJobFiles ";
	
	public static final String GPARAM_USERNAME=" -Pusername=";
	public static final String GPARAM_PASSWORD=" -Ppassword=";
	public static final String GPARAM_HOST=" -Phost=";
	public static final String GPARAM_JOB_XML=" -PjobXML=";
	public static final String GPARAM_PARAM_FILE=" -PparameterFile=";
	public static final String GPARAM_LOCAL_JOB=" -Plocaljob=true";
	public static final String GPARAM_REMOTE_PROCESSID = " -Pjobprocessid=";
	public static final String GPARAM_JOB_DEBUG_XML=" -PdebugJobXML=";
	public static final String GPARAM_JOB_BASE_PATH=" -PbasePath=";
	public static final String GPARAM_UNIQUE_JOB_ID=" -PjobId=";
	public static final String GPARAM_MOVE_PARAM_FILE=" -PmoveParameterFile=";
	public static final String GPARAM_MOVE_SCHEMA_FILES=" -PmoveExternalSchemaFiles=";
	public static final String GPARAM_MOVE_SUBJOB_FILES=" -PmoveSubJobFiles=";
	public static final String GPARAM_MOVE_JAR=" -PmoveJar=";
	public static final String GPARAM_MOVE_SCHEMA=" -PexternalSchemaFiles=";
	public static final String GPARAM_MOVE_SUBJOB=" -PsubJobFiles=";
	
	public static final String GPARAM_IS_EXECUTION_TRACKING_ON=" -PisExecutionTracking=";
	
	public static final String REMOTE_FIXED_DIRECTORY_PARAM="param";
	public static final String REMOTE_FIXED_DIRECTORY_LIB="lib";
	public static final String DAEMON_ENABLE=" -Dorg.gradle.daemon=true";
	public static final String KILL_GRADLE_DAEMON="gradle --stop";
	
	
}
