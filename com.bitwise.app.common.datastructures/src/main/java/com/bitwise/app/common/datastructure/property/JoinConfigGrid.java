package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;
import com.bitwise.app.cloneableinterface.IDataStructure;



public class JoinConfigGrid implements IDataStructure {
	private List<JoinConfigProperty> joinConfigProperties;
	private List<JoinConfigProperty> clonedJoinConfigProperty;
	
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
	
	@Override
	public JoinConfigGrid clone()   {
		JoinConfigGrid joinConfigGrid=new JoinConfigGrid() ;
		clonedJoinConfigProperty=new ArrayList<>();
	    for(int i=0;i<joinConfigProperties.size();i++)
    	 {
		  clonedJoinConfigProperty.add(joinConfigProperties.get(i).clone());
    	 }		 
		joinConfigGrid.setJoinConfigProperties(clonedJoinConfigProperty);
		return joinConfigGrid;
		
	}
}
