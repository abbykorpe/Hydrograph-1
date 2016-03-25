package com.bitwise.app.parametergrid.dialog.models;

public class ParameterWithFilePath extends Parameter{
	FilePath filePath;
	
	public ParameterWithFilePath(String parameterName, String parameterValue,FilePath filePath) {
		super(parameterName, parameterValue);
		this.filePath = filePath;
	}

	public FilePath getFilePath() {
		return filePath;
	}

	public void setFilePath(FilePath filePath) {
		this.filePath = filePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterWithFilePath other = (ParameterWithFilePath) obj;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return filePath + "\n" + super.toString();
	}	
}
