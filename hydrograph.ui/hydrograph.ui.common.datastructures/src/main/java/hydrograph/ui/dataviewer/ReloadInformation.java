package hydrograph.ui.dataviewer;

public class ReloadInformation {

	String host;
	String port;
	String username;
	String password;
	String basepath;
		
	String uniqueJobID;
	String componentID;
	String componentSocketID;
	
	boolean isLocalJob;

	public ReloadInformation() {
		// TODO Auto-generated constructor stub
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
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

	public String getBasepath() {
		return basepath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	public String getUniqueJobID() {
		return uniqueJobID;
	}

	public void setUniqueJobID(String uniqueJobID) {
		this.uniqueJobID = uniqueJobID;
	}

	public String getComponentID() {
		return componentID;
	}

	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}

	public String getComponentSocketID() {
		return componentSocketID;
	}

	public void setComponentSocketID(String componentSocketID) {
		this.componentSocketID = componentSocketID;
	}

	public boolean getIsLocalJob() {
		return isLocalJob;
	}

	public void setIsLocalJob(boolean isLocalJob) {
		this.isLocalJob = isLocalJob;
	}

	@Override
	public String toString() {
		return "ReloadInformation [host=" + host + ", port=" + port + ", username=" + username + ", password=" + password
				+ ", basepath=" + basepath + ", uniqueJobID=" + uniqueJobID + ", componentID=" + componentID
				+ ", componentSocketID=" + componentSocketID + ", isLocalJob=" + isLocalJob + "]";
	}
	
}
