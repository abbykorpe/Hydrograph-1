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
	public static final String GCMD_EXECUTE_REMOTE_JOB="gradle executeRemoteJob ";
	public static final String GCMD_EXECUTE_LOCAL_JOB="gradle executeLocalJob ";
	public static final String GCMD_KILL_REMOTE_JOB="gradle killRemoteJob ";
	
	public static final String GPARAM_USERNAME=" -Pusername=";
	public static final String GPARAM_PASSWORD=" -Ppassword=";
	public static final String GPARAM_HOST=" -Phost=";
	public static final String GPARAM_JOB_XML=" -PjobXML=";
	public static final String GPARAM_PARAM_FILE=" -PparameterFile=";
	public static final String GPARAM_LOCAL_JOB=" -Plocaljob=true";
	public static final String GPARAM_REMOTE_PROCESSID = " -Pjobprocessid=";
}
