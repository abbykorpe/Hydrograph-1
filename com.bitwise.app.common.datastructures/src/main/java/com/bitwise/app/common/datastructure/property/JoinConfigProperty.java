package com.bitwise.app.common.datastructure.property;



public class JoinConfigProperty implements Cloneable{
	
	private String portIndex;
	private String joinKey;
	private Integer joinType;
	private String paramValue;
	
	public JoinConfigProperty() {
		portIndex = "";
		joinKey = "";
		joinType = 0;
		paramValue = "";
	}
	
	
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getPortIndex() {
		return portIndex;
	}
	public void setPortIndex(String portIndex) {
		this.portIndex = portIndex;
	}
	public String getJoinKey() {
		return joinKey;
	}
	public void setJoinKey(String joinKey) {
		this.joinKey = joinKey;
	}
	public Integer getJoinType() {
		return joinType;
	}
	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((joinKey == null) ? 0 : joinKey.hashCode());
		result = prime * result
				+ ((joinType == null) ? 0 : joinType.hashCode());
		result = prime * result
				+ ((portIndex == null) ? 0 : portIndex.hashCode());
		return result;
	}
	
	@Override
	public JoinConfigProperty clone()
	{  
		JoinConfigProperty joinConfigProperty=new JoinConfigProperty() ;
		joinConfigProperty.setJoinKey(getJoinKey());
		joinConfigProperty.setJoinType(getJoinType());
		joinConfigProperty.setParamValue(getParamValue());
		joinConfigProperty.setPortIndex(getPortIndex());
		return joinConfigProperty;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JoinConfigProperty other = (JoinConfigProperty) obj;
		if (joinKey == null) {
			if (other.joinKey != null)
				return false;
		} else if (!joinKey.equals(other.joinKey))
			return false;
		if (joinType == null) {
			if (other.joinType != null)
				return false;
		} else if (!joinType.equals(other.joinType))
			return false;
		if (portIndex == null) {
			if (other.portIndex != null)
				return false;
		} else if (!portIndex.equals(other.portIndex))
			return false;
		return true;
	}
	
	
}