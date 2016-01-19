package com.bitwise.app.common.datastructure.property;

import com.bitwise.app.cloneableinterface.IDataStructure;


	
public class LookupMapProperty implements IDataStructure {
	
	private String Source_Field;
	private String Output_Field;
	
	public String getSource_Field() {
		return Source_Field;
	}
	public void setSource_Field(String source_Field) {
		Source_Field = source_Field;
	}
	public String getOutput_Field() {
		return Output_Field;
	}
	public void setOutput_Field(String output_Field) {
		Output_Field = output_Field;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Output_Field == null) ? 0 : Output_Field.hashCode());
		result = prime * result
				+ ((Source_Field == null) ? 0 : Source_Field.hashCode());
		return result;
	}
	
	@Override
	public LookupMapProperty clone() 
	{
		LookupMapProperty lookupMapProperty=new LookupMapProperty();	
		lookupMapProperty.setOutput_Field(getOutput_Field());
		lookupMapProperty.setSource_Field(getSource_Field());
		return lookupMapProperty;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LookupMapProperty other = (LookupMapProperty) obj;
		if (Output_Field == null) {
			if (other.Output_Field != null)
				return false;
		} else if (!Output_Field.equals(other.Output_Field))
			return false;
		if (Source_Field == null) {
			if (other.Source_Field != null)
				return false;
		} else if (!Source_Field.equals(other.Source_Field))
			return false;
		return true;
	}
	
	
}
