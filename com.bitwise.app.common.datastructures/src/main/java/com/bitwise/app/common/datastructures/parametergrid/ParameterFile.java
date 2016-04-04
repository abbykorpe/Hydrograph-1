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
 
package com.bitwise.app.common.datastructures.parametergrid;

import java.io.Serializable;

/**
 * 
 * The class to hold parameter file with its metadata
 * 
 * @author Bitwise
 *
 */
public class ParameterFile  implements Serializable{
	private static final long serialVersionUID = 5403262912433893757L;
	private String fileName;
	private String path;
	private boolean jobSpecificFile;
	private boolean checked;
		
	public ParameterFile(String fileName, String path, boolean jobSpecificFile, boolean checked) {
		super();
		this.fileName = fileName;
		this.path = path;
		this.jobSpecificFile = jobSpecificFile;
		this.checked = checked;
	}

	/**
	 * 
	 * Returns string to show in parameter file path grid
	 * 
	 * @return String
	 */
	public String getFilePathViewString(){
		return this.fileName + " - " + this.path;
	}
	
	/**
	 * 
	 * Returns name of parameter file
	 * 
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * 
	 * Set name of parameter file
	 * 
	 * @param String
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 
	 * Get parameter file path
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 
	 * Set parameter file path
	 * 
	 * @param String
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 
	 * Returns true if it is job specific parameter file
	 * 
	 * @return
	 */
	public boolean isJobSpecificFile() {
		return jobSpecificFile;
	}

	/**
	 * 
	 * Set parameter file type
	 * True - if it is job specific file
	 * False - if it is normal parameter file
	 * 
	 * @param jobSpecificFile
	 */
	public void setJobSpecificFile(boolean jobSpecificFile) {
		this.jobSpecificFile = jobSpecificFile;
	}

	/**
	 * 
	 * Returns true if the file is checked for execution
	 * 
	 * @return boolean
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * 
	 * Check file for execution
	 * 
	 * @param boolean
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (checked ? 1231 : 1237);
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + (jobSpecificFile ? 1231 : 1237);
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		ParameterFile other = (ParameterFile) obj;
		if (checked != other.checked)
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (jobSpecificFile != other.jobSpecificFile)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		ParameterFile filePath = new ParameterFile(this.fileName,this.path,this.jobSpecificFile,this.checked);
		return filePath;
	}
	
	@Override
	public String toString() {
		return fileName + "\n" + path ;
	}	
}
