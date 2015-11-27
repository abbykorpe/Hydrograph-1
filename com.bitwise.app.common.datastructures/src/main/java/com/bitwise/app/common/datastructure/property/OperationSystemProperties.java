package com.bitwise.app.common.datastructure.property;

public class OperationSystemProperties extends PropertyField{
	private boolean isChecked;
	private String opSysValue;
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getOpSysValue() {
		return opSysValue;
	}
	public void setOpSysValue(String opSysValue) {
		this.opSysValue = opSysValue;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OperationSystemProperties [isChecked=");
		builder.append(isChecked);
		builder.append(", opSysValue=");
		builder.append(opSysValue);
		builder.append("]");
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((opSysValue == null) ? 0 : opSysValue.hashCode());
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
		OperationSystemProperties other = (OperationSystemProperties) obj;
		if (opSysValue == null) {
			if (other.opSysValue != null)
				return false;
		} else if (!opSysValue.equals(other.opSysValue))
			return false;
		return true;
	}

	
}
