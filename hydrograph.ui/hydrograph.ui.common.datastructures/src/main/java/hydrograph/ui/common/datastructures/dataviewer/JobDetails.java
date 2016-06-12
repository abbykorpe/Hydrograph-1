/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hydrograph.ui.common.datastructures.dataviewer;

/**
 * 
 * Data structure to hold debug service client information
 * 
 * @author Bitwise
 *
 */
public class JobDetails {
	private String host;
	private String port;
	private String username;
	private String password;
	private String basepath;

	private String uniqueJobID;
	private String componentID;
	private String componentSocketID;

	boolean isRemote;

	public JobDetails(String host, String port, String username, String password, String basepath,
			String uniqueJobID, String componentID, String componentSocketID, boolean isRemote) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.basepath = basepath;
		this.uniqueJobID = uniqueJobID;
		this.componentID = componentID;
		this.componentSocketID = componentSocketID;
		this.isRemote = isRemote;
	}
	

	public String getHost() {
		return host;
	}


	public String getPort() {
		return port;
	}


	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	public String getBasepath() {
		return basepath;
	}


	public String getUniqueJobID() {
		return uniqueJobID;
	}


	public String getComponentID() {
		return componentID;
	}


	public String getComponentSocketID() {
		return componentSocketID;
	}


	public boolean isRemote() {
		return isRemote;
	}


	@Override
	public String toString() {
		return "DebugServiceClientInformation [host=" + host + ", port=" + port + ", username=" + username + ", basepath="
				+ basepath + ", uniqueJobID=" + uniqueJobID + ", componentID=" + componentID + ", componentSocketID="
				+ componentSocketID + ", isRemote=" + isRemote + "]";
	}
	
}
