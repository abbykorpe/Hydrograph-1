package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.cloneableinterface.CloneObject;
import com.bitwise.app.common.util.LogFactory;


public class JoinConfigGrid implements CloneObject {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(JoinConfigGrid.class);
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
		JoinConfigGrid joinConfigGrid=null;
		clonedJoinConfigProperty=new ArrayList<>();
		try {
			joinConfigGrid=this.getClass().newInstance();
		} catch (Exception e) {
			logger.debug("Unable to instantiate cloning object",e);
		}
		 for(int i=0;i<joinConfigProperties.size();i++)
    	 {
			 clonedJoinConfigProperty.add(joinConfigProperties.get(i).clone());
    	 }		 
		joinConfigGrid.setJoinConfigProperties(clonedJoinConfigProperty);
		return joinConfigGrid;
		
	}
}
