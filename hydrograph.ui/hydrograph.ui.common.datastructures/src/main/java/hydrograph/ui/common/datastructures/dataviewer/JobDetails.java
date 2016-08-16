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
 * The Class JobDetails
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

	private boolean isRemote;

	/**
	 * Instantiates a new job details.
	 * 
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param basepath
	 *            the basepath
	 * @param uniqueJobID
	 *            the unique job ID
	 * @param componentID
	 *            the component ID
	 * @param componentSocketID
	 *            the component socket ID
	 * @param isRemote
	 *            the is remote
	 */
	public JobDetails(String host, String port, String username, String password, String basepath, String uniqueJobID,
			String componentID, String componentSocketID, boolean isRemote) {
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
	

	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}


	/**
	 * Gets the port.
	 * 
	 * @return the port
	 */
	public String getPort() {
		return port;
	}


	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * Gets the basepath.
	 * 
	 * @return the basepath
	 */
	public String getBasepath() {
		return basepath;
	}


	/**
	 * Gets the unique job ID.
	 * 
	 * @return the unique job ID
	 */
	public String getUniqueJobID() {
		return uniqueJobID;
	}


	/**
	 * Gets the component ID.
	 * 
	 * @return the component ID
	 */
	public String getComponentID() {
		return componentID;
	}


	/**
	 * Gets the component socket ID.
	 * 
	 * @return the component socket ID
	 */
	public String getComponentSocketID() {
		return componentSocketID;
	}


	/**
	 * Checks if is remote.
	 * 
	 * @return true, if is remote
	 */
	public boolean isRemote() {
		return isRemote;
	}

	@Override
	public String toString() {
		return "JobDetails [host=" + host + ", port=" + port + ", basepath=" + basepath + ", uniqueJobID="
				+ uniqueJobID + ", componentID=" + componentID + ", componentSocketID=" + componentSocketID
				+ ", isRemote=" + isRemote + "]";
	}
	
}
