package com.bitwise.app.common.datastructure.property;



public class JoinConfigProperty implements Cloneable{
	
	private String portIndex;
	private String joinKey;
	private Integer recordRequired;
	
	public JoinConfigProperty() {
		portIndex = "";
		joinKey = "";
		recordRequired = 0;
	}
	
	public JoinConfigProperty(String portIndex,String joinKey, Integer joinType) {
		this.portIndex =portIndex;
		this.joinKey=joinKey;
		this.recordRequired = joinType;
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

	
	public Integer getRecordRequired() {
		return recordRequired;
	}

	public void setRecordRequired(Integer recordRequired) {
		this.recordRequired = recordRequired;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((joinKey == null) ? 0 : joinKey.hashCode());
		result = prime * result
				+ ((recordRequired == null) ? 0 : recordRequired.hashCode());
		result = prime * result
				+ ((portIndex == null) ? 0 : portIndex.hashCode());
		return result;
	}
	
	@Override
	public JoinConfigProperty clone()
	{  
		JoinConfigProperty joinConfigProperty=new JoinConfigProperty() ;
		joinConfigProperty.setJoinKey(getJoinKey());
		joinConfigProperty.setRecordRequired(getRecordRequired());
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
		if (recordRequired == null) {
			if (other.recordRequired != null)
				return false;
		} else if (!recordRequired.equals(other.recordRequired))
			return false;
		if (portIndex == null) {
			if (other.portIndex != null)
				return false;
		} else if (!portIndex.equals(other.portIndex))
			return false;
		return true;
	}
	
	
}