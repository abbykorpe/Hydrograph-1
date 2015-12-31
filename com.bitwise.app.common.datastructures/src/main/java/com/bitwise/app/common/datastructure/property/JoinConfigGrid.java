package com.bitwise.app.common.datastructure.property;

import java.util.List;

public class JoinConfigGrid {
	private List<JoinConfigProperty> joinConfigProperties;
	
	public List<JoinConfigProperty> getJoinConfigProperties() {
		return joinConfigProperties;
	}
	public void setJoinConfigProperties(
			List<JoinConfigProperty> joinConfigProperties) {
		this.joinConfigProperties = joinConfigProperties;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JoinConfigGrid [joinConfigProperties=");
		builder.append(joinConfigProperties);
		builder.append("]");
		return builder.toString();
	}
}
