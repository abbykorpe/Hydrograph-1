package com.bitwise.app.common.datastructure.property;



import com.bitwise.app.cloneableinterface.IDataStructure;



public class OperationField extends PropertyField implements IDataStructure{
	
	private String name;
	
	public OperationField() {
		
	}
	
	public OperationField(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public OperationField clone() 
	{  
		OperationField operationField=new OperationField();
		operationField.setName(getName());
		return operationField;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationField other = (OperationField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
