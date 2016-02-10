package com.bitwise.app.graph.job;

/**
 * The job class 
 * @author Bitwise
 *
 */
public class Job {
	private String localJobId;
	private Process localProcess;
	private String remoteJobId;
	private String consoleName;
	private String canvasName;
	
	public Job(String localJobId, Process localProcessId, String remoteJobId,
			String consoleName, String canvasName) {
		super();
		this.localJobId = localJobId;
		this.localProcess = localProcessId;
		this.remoteJobId = remoteJobId;
		this.consoleName = consoleName;
		this.canvasName = canvasName;
	}

	

	public String getLocalJobId() {
		return localJobId;
	}



	public void setLocalJobId(String localJobId) {
		this.localJobId = localJobId;
	}



	public Process getLocalProcessId() {
		return localProcess;
	}



	public void setLocalProcessId(Process localProcess) {
		this.localProcess = localProcess;
	}



	public String getRemoteJobId() {
		return remoteJobId;
	}



	public void setRemoteJobId(String remoteJobId) {
		this.remoteJobId = remoteJobId;
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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((canvasName == null) ? 0 : canvasName.hashCode());
		result = prime * result
				+ ((consoleName == null) ? 0 : consoleName.hashCode());
		result = prime * result
				+ ((localJobId == null) ? 0 : localJobId.hashCode());
		result = prime * result
				+ ((localProcess == null) ? 0 : localProcess.hashCode());
		result = prime * result
				+ ((remoteJobId == null) ? 0 : remoteJobId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (canvasName == null) {
			if (other.canvasName != null)
				return false;
		} else if (!canvasName.equals(other.canvasName))
			return false;
		if (consoleName == null) {
			if (other.consoleName != null)
				return false;
		} else if (!consoleName.equals(other.consoleName))
			return false;
		if (localJobId == null) {
			if (other.localJobId != null)
				return false;
		} else if (!localJobId.equals(other.localJobId))
			return false;
		if (localProcess == null) {
			if (other.localProcess != null)
				return false;
		} else if (!localProcess.equals(other.localProcess))
			return false;
		if (remoteJobId == null) {
			if (other.remoteJobId != null)
				return false;
		} else if (!remoteJobId.equals(other.remoteJobId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Job [localJobId=" + localJobId + ", localProcessId="
				+ localProcess + ", remoteJobId=" + remoteJobId
				+ ", consoleName=" + consoleName + ", canvasName=" + canvasName
				+ "]";
	}
}
