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

 
package com.bitwise.app.parametergrid.dialog.models;

import java.io.Serializable;

public class FilePath  implements Serializable{
	private String fileName;
	private String path;
	private boolean jobSpecificFile;
	private boolean checked;
	
	public FilePath(){
		
	}
	
	public FilePath(String fileName, String path, boolean jobSpecificFile, boolean checked) {
		super();
		this.fileName = fileName;
		this.path = path;
		this.jobSpecificFile = jobSpecificFile;
		this.checked = checked;
	}

	public String getFilePathViewString(){
		return this.fileName + " - " + this.path;
	}
	
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}
	
	public boolean isJobSpecificFile() {
		return jobSpecificFile;
	}

	public void setJobSpecificFile(boolean jobSpecificFile) {
		this.jobSpecificFile = jobSpecificFile;
	}

	public boolean isChecked() {
		return checked;
	}

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
		FilePath other = (FilePath) obj;
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
		FilePath filePath = new FilePath(this.fileName,this.path,this.jobSpecificFile,this.checked);
		return filePath;
	}
	
	@Override
	public String toString() {
		return fileName + "\n" + path ;
	}	
}
