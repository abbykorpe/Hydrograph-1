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

package hydrograph.ui.propertywindow.widgets.utility;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;

import java.util.List;


/**
 * The Class FilterOperationClassUtility.
 * 
 * @author Bitwise
 */
public class SchemaSyncUtility {


	/**
	 * Add and remove data from map fields those are not present in outer schema, use to sync outer schema with transform and aggregate internal fields.
	 *
	 * @param outSchema the out schema
	 * @param transformMapping the transform mapping
	 * @return the list
	 */
	public static List<NameValueProperty> filterCommonMapFields(List<NameValueProperty> outSchema, TransformMapping transformMapping) {
		List<NameValueProperty> mapNameValueProperties = transformMapping.getMapAndPassthroughField();
		for (NameValueProperty nameValueProperty : outSchema) {
			boolean isPresent=false;
	    	if(!mapNameValueProperties.contains(nameValueProperty))
	    	{
	    		for (MappingSheetRow mappingSheetRow : transformMapping.getMappingSheetRows()) {
    				FilterProperties tempFilterProperties = new FilterProperties();
    				tempFilterProperties.setPropertyname(nameValueProperty.getPropertyValue());
     				if(mappingSheetRow.getOutputList().contains(tempFilterProperties)){
    					isPresent=true;
    					break;    					
    				}
    			}
	    		if(!isPresent)
	    			mapNameValueProperties.add(nameValueProperty);
	    	}
	    	
	    }
		mapNameValueProperties.retainAll(outSchema);
	    return mapNameValueProperties;
	}
	
	/**
	 * Removes the op fields those removed from outer schema.
	 *
	 * @param outSchema the out schema
	 * @param mappingSheetRow the mapping sheet row
	 */
	public static void removeOpFields(List<FilterProperties> outSchema, List<MappingSheetRow> mappingSheetRow){
		for (MappingSheetRow mapSheetRow : mappingSheetRow) {
					mapSheetRow.getOutputList().retainAll(outSchema);
		}
	}
	
	/**
	 * Union filter.
	 *
	 * @param list1 the list1
	 * @param list2 the list2
	 * @return the list
	 */
	public static List<FilterProperties> unionFilter(List<FilterProperties> list1, List<FilterProperties> list2) {
	    for (FilterProperties filterProperties : list1) {
	    	if(!list2.contains(filterProperties))
	    		list2.add(filterProperties);
	    }
	    return list2;
	}
	
	public static boolean isSchemaSyncAllow(String componentName){
		return Constants.TRANSFORM.equalsIgnoreCase(componentName) || Constants.AGGREGATE.equalsIgnoreCase(componentName) || Constants.NORMALIZE.equalsIgnoreCase(componentName) || Constants.CUMULATE.equalsIgnoreCase(componentName);
	}

}
