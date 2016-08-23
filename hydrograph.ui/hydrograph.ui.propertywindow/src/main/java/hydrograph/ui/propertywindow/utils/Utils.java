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

package hydrograph.ui.propertywindow.utils;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.JoinMappingGrid;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.datastructure.property.LookupMappingGrid;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Utility class
 * 
 * @author Bitwise
 *
 */
public class Utils {
	
	public static final String OPERATION = "operation";
	public static final String LOOKUP_MAP = "hash_join_map";
	public static final String JOIN_MAP = "join_mapping";
	
	public static Utils INSTANCE = new Utils();
	
	private Utils(){
		
	}
	
	/**
	 * 
	 * Checks if component schema is sync with mapping of the component
	 * 
	 * @param componentName
	 * @param componentProperties
	 * @return true if schema is in sync with mapping of the component otherwise false
	 */
	public boolean isMappingAndSchemaAreInSync(String componentName,Map<String, Object> componentProperties) {
		List<String> outputFieldList = getOutputFieldList(componentName,componentProperties);
		
		Schema schema = (Schema) componentProperties.get(Constants.SCHEMA_PROPERTY_NAME);
		
		if(schema == null && outputFieldList == null){
			return true;
		}
		
		if(schema == null || outputFieldList == null){
			return false;
		}
		
		List<String> schemaFieldList = getSchemaFieldList(schema.getGridRow());		
		
		if(schemaFieldList.size()!=outputFieldList.size()){
			return false;
		}
		
		for(int index=0;index<schemaFieldList.size();index++){
			if(!StringUtils.equals(schemaFieldList.get(index), outputFieldList.get(index))){
				return false;
			}
		}
		return true;
	}
	
	private List<String> getSchemaFieldList(List<GridRow> schemaGridRowList) {
		List<String> schemaFieldList = new LinkedList<>();
		
		for(GridRow gridRow: schemaGridRowList){
			schemaFieldList.add(gridRow.getFieldName());
		}
		return schemaFieldList;
	}
	
	private List<String> getOutputFieldList(String componentName,Map<String, Object> componentProperties) {
		List<String> outputFieldList=null;
		if(StringUtils.equalsIgnoreCase(componentName,Constants.JOIN)){
			JoinMappingGrid joinMappingGrid = (JoinMappingGrid) componentProperties.get(JOIN_MAP);
			if(joinMappingGrid == null){
				return null;
			}
			outputFieldList = getOutputFieldsFromJoinMapping(joinMappingGrid);
		}else if(StringUtils.equalsIgnoreCase(componentName,Constants.LOOKUP)){
			LookupMappingGrid lookupMappingGrid = (LookupMappingGrid) componentProperties.get(LOOKUP_MAP);
			if(lookupMappingGrid == null){
				return null;
			}
			outputFieldList = getOutputFieldsFromLookupMapping(lookupMappingGrid);
		}else if(StringUtils.equalsIgnoreCase(Constants.TRANSFORM, componentName) ||
				   StringUtils.equalsIgnoreCase(Constants.AGGREGATE, componentName) ||
				   StringUtils.equalsIgnoreCase(Constants.NORMALIZE, componentName) ||
				   StringUtils.equalsIgnoreCase(Constants.CUMULATE, componentName)){
			TransformMapping transformMapping = (TransformMapping) componentProperties.get(OPERATION);
			if(transformMapping == null){
				return null;
			}
			outputFieldList = getOutputFieldsFromTransformMapping(transformMapping.getOutputFieldList());
		}
		return outputFieldList;
	}
	
	private List<String> getOutputFieldsFromJoinMapping(
			JoinMappingGrid joinMappingGrid) {
		List<String> lookupMapOutputs = new ArrayList<>();
		for (LookupMapProperty l : joinMappingGrid.getLookupMapProperties()) {
			lookupMapOutputs.add(l.getOutput_Field());
		}
		return lookupMapOutputs;
	}
	
	private List<String> getOutputFieldsFromLookupMapping(
			LookupMappingGrid lookupMappingGrid) {
		List<String> lookupMapOutputs = new ArrayList<>();
		for (LookupMapProperty l : lookupMappingGrid.getLookupMapProperties()) {
			lookupMapOutputs.add(l.getOutput_Field());
		}
		return lookupMapOutputs;
	}
	
	private List<String> getOutputFieldsFromTransformMapping(
			List<FilterProperties> outputFieldList) {
		List<String> outputFields = new ArrayList<>();
		for (FilterProperties fileFilterProperty : outputFieldList) {
			outputFields.add(fileFilterProperty.getPropertyname());
		}
		return outputFields;
	}

}
