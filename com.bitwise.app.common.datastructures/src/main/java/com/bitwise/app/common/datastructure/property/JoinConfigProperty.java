package com.bitwise.app.common.datastructure.property;

public class JoinConfigProperty {

	private String port_index;
	private String join_key;
	private Integer join_type;
	
	public String getPort_index() {
		return port_index;
	}
	public void setPort_index(String port_index) {
		this.port_index = port_index;
	}
	public String getJoin_key() {
		return join_key;
	}
	public void setJoin_key(String join_key) {
		this.join_key = join_key;
	}
	public Integer getJoin_type() {
		return join_type;
	}
	public void setJoin_type(Integer join_type) {
		this.join_type = join_type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((join_key == null) ? 0 : join_key.hashCode());
		result = prime * result
				+ ((join_type == null) ? 0 : join_type.hashCode());
		result = prime * result
				+ ((port_index == null) ? 0 : port_index.hashCode());
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
		JoinConfigProperty other = (JoinConfigProperty) obj;
		if (join_key == null) {
			if (other.join_key != null)
				return false;
		} else if (!join_key.equals(other.join_key))
			return false;
		if (join_type == null) {
			if (other.join_type != null)
				return false;
		} else if (!join_type.equals(other.join_type))
			return false;
		if (port_index == null) {
			if (other.port_index != null)
				return false;
		} else if (!port_index.equals(other.port_index))
			return false;
		return true;
	}
	
	
}