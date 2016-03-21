/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
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
