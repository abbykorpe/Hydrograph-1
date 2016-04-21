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

 
package hydrograph.ui.datastructure.property;

import hydrograph.ui.common.cloneableinterface.IDataStructure;

import java.util.ArrayList;
import java.util.List;


public class JoinMappingGrid implements IDataStructure{
	
	private Boolean isSelected;
	private String buttonText;
	private List<LookupMapProperty> lookupMapProperties; //right side grid
	private List<LookupMapProperty> clonedLookupMapProperties;
	private List<List<FilterProperties>> lookupInputProperties;   //join left side
	private List<FilterProperties> clonedInnerLookupInputProperties;
	private List<List<FilterProperties>> clonedLookupInputProperties;
	
	public JoinMappingGrid() {
		lookupMapProperties = new ArrayList<>();
		lookupInputProperties = new ArrayList<>();
		isSelected = Boolean.FALSE;
	}	
	
	public String getButtonText() {
		return buttonText;
	}
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	public Boolean isSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	public List<List<FilterProperties>> getLookupInputProperties() {
		return lookupInputProperties;
	}
	public void setLookupInputProperties(List<List<FilterProperties>> lookupInputProperties) {
		this.lookupInputProperties = lookupInputProperties;
	}
	public List<LookupMapProperty> getLookupMapProperties() {
		return lookupMapProperties;
	}
	public void setLookupMapProperties(List<LookupMapProperty> lookupMapProperties) {
		this.lookupMapProperties = lookupMapProperties;
	}
	
	@Override
	public JoinMappingGrid clone() 
	{
		
		clonedLookupMapProperties=new ArrayList<>();
		clonedLookupInputProperties=new ArrayList<>();
		JoinMappingGrid joinMappingGrid=new JoinMappingGrid();
		for(int i=0;i<lookupMapProperties.size();i++)
		{
			clonedLookupMapProperties.add(lookupMapProperties.get(i).clone());
			
		}	
		
		for(int i=0;i<lookupInputProperties.size();i++)
		{
			clonedInnerLookupInputProperties=new ArrayList<>();
			for(int j=0;j<lookupInputProperties.get(i).size();j++)
			{
				clonedInnerLookupInputProperties.add(lookupInputProperties.get(i).get(j).clone());
			}
			clonedLookupInputProperties.add(clonedInnerLookupInputProperties);
		
		}
	
		joinMappingGrid.setButtonText(getButtonText());
		joinMappingGrid.setIsSelected(isSelected());
		joinMappingGrid.setLookupInputProperties(clonedLookupInputProperties);
		joinMappingGrid.setLookupMapProperties(clonedLookupMapProperties);
		return joinMappingGrid;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JoinMappingGrid [lookupMapProperties=");
		builder.append(lookupMapProperties);
		builder.append(", lookupInputProperties=");
		builder.append(lookupInputProperties);
		builder.append("]");
		return builder.toString();
	}
}
