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


package hydrograph.ui.validators.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.mapping.ErrorObject;
import hydrograph.ui.datastructure.property.mapping.InputField;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;

public class TransformMappingValidationRule implements IValidator{
	private String errorMessage;
	@Override
	public boolean validateMap(Object object, String propertyName) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		if(propertyMap != null && !propertyMap.isEmpty()){ 
			return validate(propertyMap.get(propertyName), propertyName);
		}
		return false;
	}

	@Override
	public boolean validate(Object object, String propertyName) {
		TransformMapping transformMapping=(TransformMapping) object;
		
		if(transformMapping==null)
		{
			errorMessage = propertyName + " is mandatory";
			return false;
		}	
		List<MappingSheetRow> mappingSheetRows=transformMapping.getMappingSheetRows();
		List<NameValueProperty>  mapOrPassthroughfields = transformMapping.getMapAndPassthroughField();
		
		if((mappingSheetRows==null || mappingSheetRows.isEmpty()) && (mapOrPassthroughfields==null || mapOrPassthroughfields.isEmpty() ) )
		{
	    errorMessage = propertyName + "Output field(s) is mandatory";		 	
		return false;
		}
		
		Set<FilterProperties>set=null;
		if(mappingSheetRows!=null && !mappingSheetRows.isEmpty())
		{
			for(MappingSheetRow mappingSheetRow:mappingSheetRows)
			{
				if(StringUtils.isBlank(mappingSheetRow.getOperationClassPath()))
				{
					 errorMessage = propertyName + "Operation class is blank in"+" "+mappingSheetRow.getOperationID();		
					 return false;
				}
				if(mappingSheetRow.getOutputList().isEmpty())
				{
					 errorMessage = propertyName + "Operation field(s) are empty";		
					 return false;
				}
			    for(NameValueProperty nameValueProperty :mappingSheetRow.getNameValueProperty())
			    {
			    	if(StringUtils.isBlank(nameValueProperty.getPropertyValue()))
			    	{
			    		 errorMessage = propertyName + "Property value is Blank";		
						 return false;
			    	}	
			    }	
			   
			}
			for(MappingSheetRow mappingSheetRow:mappingSheetRows)
			{
				   set = new HashSet<FilterProperties>(mappingSheetRow.getInputFields());
				   if(set.size() < mappingSheetRow.getInputFields().size())
				   {
					 errorMessage = propertyName + "Duplicate field(s) exists in" +" "+mappingSheetRow.getOperationID();		
					 return false;
				   }
			}
			
		}
		
		for (NameValueProperty nameValueProperty : mapOrPassthroughfields) {
			if(!transformMapping.getInputFields().contains(new InputField(nameValueProperty.getPropertyName(), new ErrorObject(false, "")))){
				 errorMessage = propertyName + " Input field not present.";		
				 return false;
			}
		}
		
		List<FilterProperties> filterProperties = new ArrayList<>();

		for (NameValueProperty nameValue : transformMapping.getMapAndPassthroughField()) {
			FilterProperties filterProperty = new FilterProperties();
			filterProperty.setPropertyname(nameValue.getPropertyValue());
			filterProperties.add(filterProperty);
		}
		
		List<FilterProperties> operationOutputFieldList=new ArrayList<>();
		for( MappingSheetRow mappingSheetRow : mappingSheetRows)
		{
			operationOutputFieldList.addAll(mappingSheetRow.getOutputList());
		}
		 set = new HashSet<FilterProperties>(filterProperties);
		 set.addAll(operationOutputFieldList);
		
		if((set.size()<(operationOutputFieldList.size()+filterProperties.size()))) 
		{
			 errorMessage = propertyName + "Duplicate field(s) exists in OutputFields";		
			 return false;
			
		}	
		return true;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
