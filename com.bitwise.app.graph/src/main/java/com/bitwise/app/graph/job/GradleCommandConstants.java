package com.bitwise.app.graph.job;
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
}
