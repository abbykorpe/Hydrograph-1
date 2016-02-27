package com.bitwise.app.graph.job;

/**
 * The job class. The class is a data structure of basic job entity
 * @author Bitwise
 *
 */
public class Job {
	
	private String localJobID;
	private Process localJobProcess;
	private String remoteJobProcessID;
	private String consoleName;
	private String canvasName;
	private String host;
	private String username;
	private String password;
	private String jobProjectDirectory;
	
	public Job(String localJobID,String consoleName, String canvasName) {
		this.localJobID = localJobID;
		this.consoleName = consoleName;
		this.canvasName = canvasName;
	}

	public String getLocalJobID() {
		return localJobID;
	}

	public void setLocalJobID(String localJobID) {
		this.localJobID = localJobID;
	}

	public Process getLocalJobProcess() {
		return localJobProcess;
	}

	public void setLocalJobProcess(Process localJobProcess) {
		this.localJobProcess = localJobProcess;
	}

	public String getRemoteJobProcessID() {
		return remoteJobProcessID;
	}

	public void setRemoteJobProcessID(String remoteJobProcessID) {
		this.remoteJobProcessID = remoteJobProcessID;
	}

	public String getConsoleName() {
		return consoleName;
	}

	public void setConsoleName(String consoleName) {
		this.consoleName = consoleName;
	}

	public String getCanvasName() {
		return canvasName;
	}

	public void setCanvasName(String canvasName) {
		this.canvasName = canvasName;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getJobProjectDirectory() {
		return jobProjectDirectory;
	}

	public void setJobProjectDirectory(String jobProjectDirectory) {
		this.jobProjectDirectory = jobProjectDirectory;
	}

	@Override
	public String toString() {
		return "Job [localJobID=" + localJobID + ", localJobProcess=" + localJobProcess + ", remoteJobProcessID="
				+ remoteJobProcessID + ", consoleName=" + consoleName + ", canvasName=" + canvasName + ", host=" + host
				+ ", username=" + username + ", password=" + password + ", jobProjectDirectory=" + jobProjectDirectory
				+ "]";
	}	
}
