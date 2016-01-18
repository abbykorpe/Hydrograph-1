package com.bitwise.app.common.datastructure.property;

import org.slf4j.Logger;

import com.bitwise.app.cloneableinterface.IDataStructure;
import com.bitwise.app.common.util.LogFactory;


public class OperationField extends PropertyField implements IDataStructure{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(OperationField.class);
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
		OperationField operationField=null;
		try {
		operationField=this.getClass().newInstance();
		}
		catch (Exception e) {
			logger.debug("Unable to instantiate cloning object",e);
			}
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
