package com.bitwise.app.common.datastructure.property;



import com.bitwise.app.cloneableinterface.IDataStructure;


/**
 * The Class OperationClassProperty.
 * 
 * @author Bitwise
 */
public class OperationClassProperty implements IDataStructure{


	private String comboBoxValue;
	private String operationClassPath;
	private boolean isParameter;
	private String operationClassFullPath;
	
	/**
	 * Instantiates a new operation class property.
	 * 
	 * @param operationClassPath
	 *            the operation class path
	 * @param isParameter
	 *            the is parameter
	 */
	public OperationClassProperty(String comboBoxValue, String operationClassPath, boolean isParameter) {
		this.operationClassPath = operationClassPath;
		this.isParameter = isParameter;
		this.comboBoxValue = comboBoxValue;
	}
	public OperationClassProperty(String comboBoxValue,String operationClassPath, boolean isParameter,String operationClassFullPath) {
		this.comboBoxValue=comboBoxValue;
		this.operationClassPath = operationClassPath;
		this.isParameter = isParameter;
		this.operationClassFullPath=operationClassFullPath;
	}
	public String getComboBoxValue() {
		return comboBoxValue;
	}
	public String getOperationClassPath() {
		return operationClassPath;
	}
	public boolean isParameter() {
		return isParameter;
	}
		
	public String getOperationClassFullPath() {
		return operationClassFullPath;
	}
	public void setOperationClassFullPath(String operationClassFullPath) {
		this.operationClassFullPath = operationClassFullPath;
	}
	@Override
	public OperationClassProperty clone() {
		return new OperationClassProperty(comboBoxValue,operationClassPath,isParameter,operationClassFullPath);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isParameter ? 1231 : 1237);
		result = prime
				* result
				+ ((operationClassPath == null) ? 0 : operationClassPath
						.hashCode());
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
		OperationClassProperty other = (OperationClassProperty) obj;
		if (isParameter != other.isParameter)
			return false;
		if (operationClassPath == null) {
			if (other.operationClassPath != null)
				return false;
		} else if (!operationClassPath.equals(other.operationClassPath))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return operationClassPath;
	}
	
	
  
}
