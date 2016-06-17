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
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.datastructure.property.LookupMappingGrid;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.graph.model.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class FilterOperationClassUtility.
 * 
 * @author Bitwise
 */
public class SchemaSyncUtility {

	public static final String OPERATION = "operation";
	public static final String LOOKUP_MAP = "hash_join_map";

	public static SchemaSyncUtility INSTANCE= new SchemaSyncUtility();
	
	private SchemaSyncUtility(){
		
	}
	
	/**
	 * Add and remove data from map fields those are not present in outer schema, use to sync outer schema with transform and aggregate internal fields.
	 *
	 * @param outSchema the out schema
	 * @param transformMapping the transform mapping
	 * @return the list
	 */
	public List<NameValueProperty> filterCommonMapFields(List<NameValueProperty> outSchema, TransformMapping transformMapping) {
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
	public void removeOpFields(List<FilterProperties> outSchema, List<MappingSheetRow> mappingSheetRow){
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
	public List<FilterProperties> unionFilter(List<FilterProperties> list1, List<FilterProperties> list2) {
	    for (FilterProperties filterProperties : list1) {
	    	if(!list2.contains(filterProperties))
	    		list2.add(filterProperties);
	    }
	    return list2;
	}
	
	public boolean isSchemaSyncAllow(String componentName){
		return Constants.TRANSFORM.equalsIgnoreCase(componentName)
				|| Constants.AGGREGATE.equalsIgnoreCase(componentName) 
				|| Constants.NORMALIZE.equalsIgnoreCase(componentName) 
				|| Constants.CUMULATE.equalsIgnoreCase(componentName)
				|| Constants.LOOKUP.equalsIgnoreCase(componentName)
				|| Constants.JOIN.equalsIgnoreCase(componentName);
	}

	
	public void pushSchemaToMapping( Component component, List<GridRow> schemaGridRowList) {
		if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.AGGREGATE.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.NORMALIZE.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.CUMULATE.equalsIgnoreCase(component.getComponentName()) ){
				pushSchemaToTransformMapping(component, schemaGridRowList);
		}else if(Constants.LOOKUP.equalsIgnoreCase(component.getComponentName())){
			pushSchemaToLookupMapping( component, schemaGridRowList);
		}else if(Constants.JOIN.equalsIgnoreCase(component.getComponentName())){
			
		}
	}

	public void pushSchemaToLookupMapping( Component component,
			List<GridRow> schemaGridRowList) {
		LookupMappingGrid lookupMappingGrid = (LookupMappingGrid) component.getProperties().get(LOOKUP_MAP);
		List<String> lookupMapOutputs = getOutputFieldsFromMapping(lookupMappingGrid);
		List<LookupMapProperty> outputFieldsFromSchema = getComponentSchemaAsLookupMapProperty(schemaGridRowList);
		List<LookupMapProperty> outputFieldsFromSchemaToRetain = getOutputFieldsFromSchemaToRetain(schemaGridRowList, lookupMappingGrid.getLookupMapProperties());
		
		lookupMappingGrid.setLookupMapProperties(outputFieldsFromSchemaToRetain);
		for (LookupMapProperty l : outputFieldsFromSchema){
			if(!lookupMapOutputs.contains(l.getOutput_Field())){
				lookupMappingGrid.getLookupMapProperties().add(l);
			}
		}
	}
	
	public List<LookupMapProperty> pullLookupSchemaInMapping(Schema schema, Component component) {
		LookupMappingGrid lookupMappingGrid = (LookupMappingGrid) component.getProperties().get(LOOKUP_MAP);
		List<String> lookupMapOutputs = getOutputFieldsFromMapping(lookupMappingGrid);
		
		List<LookupMapProperty> outputFieldsFromSchema = getComponentSchemaAsLookupMapProperty(schema.getGridRow());
		
		List<LookupMapProperty> outputFieldsFromSchemaToRetain = getOutputFieldsFromSchemaToRetain(schema.getGridRow(), lookupMappingGrid.getLookupMapProperties());
		
		lookupMappingGrid.getLookupMapProperties().retainAll(outputFieldsFromSchemaToRetain);
		
		for (LookupMapProperty l : outputFieldsFromSchema){
			if(!lookupMapOutputs.contains(l.getOutput_Field())){
				lookupMappingGrid.getLookupMapProperties().add(l);
			}
		}
		return lookupMappingGrid.getLookupMapProperties();
	}

	private List<String> getOutputFieldsFromMapping(
			LookupMappingGrid lookupMappingGrid) {
		List<String> lookupMapOutputs = new ArrayList<>();
		for (LookupMapProperty l : lookupMappingGrid.getLookupMapProperties()) {
			lookupMapOutputs.add(l.getOutput_Field());
		}
		return lookupMapOutputs;
	}

	private void pushSchemaToTransformMapping(
			Component component, List<GridRow> schemaGridRowList) {
		TransformMapping transformMapping= (TransformMapping) component.getProperties().get(OPERATION);
		List<FilterProperties> filterProperties = convertSchemaToFilterProperty(schemaGridRowList);
		SchemaSyncUtility.INSTANCE.removeOpFields(filterProperties, transformMapping.getMappingSheetRows());
		List<NameValueProperty> outputFileds =getComponentSchemaAsProperty(schemaGridRowList);
		SchemaSyncUtility.INSTANCE.filterCommonMapFields(outputFileds, transformMapping);
	}
	
	public List<FilterProperties> convertSchemaToFilterProperty(List<GridRow> schemaGridRowList){
		List<FilterProperties> outputFileds = new ArrayList<>();
			for (GridRow gridRow : schemaGridRowList) {
				FilterProperties filterProperty = new FilterProperties();
				filterProperty.setPropertyname(gridRow.getFieldName());
				outputFileds.add(filterProperty);
			}
		return outputFileds;
	}
	
	public List<LookupMapProperty> getComponentSchemaAsLookupMapProperty(List<GridRow> schemaGridRowList){
		List<LookupMapProperty> outputFields = new ArrayList<>();
			for (GridRow gridRow : schemaGridRowList) {
				LookupMapProperty lookupMapProperty = new LookupMapProperty();
				lookupMapProperty.setSource_Field("");
				lookupMapProperty.setOutput_Field(gridRow.getFieldName());
				outputFields.add(lookupMapProperty);
			}
		return outputFields;
	}
	
	public List<LookupMapProperty> getOutputFieldsFromSchemaToRetain(List<GridRow> schemaGridRowList, List<LookupMapProperty> list){
		List<LookupMapProperty> outputFieldsToRetain = new ArrayList<>();
		for (LookupMapProperty l : list) {
			for(GridRow gridRow : schemaGridRowList){
				if(l.getOutput_Field().equals(gridRow.getFieldName())){
					outputFieldsToRetain.add(l);
					break;
				}
			}

		}
		return outputFieldsToRetain;
	}
	
	public List<NameValueProperty> getComponentSchemaAsProperty(List<GridRow> schemaGridRowList){
		List<NameValueProperty> outputFileds = new ArrayList<>();
			for (GridRow gridRow : schemaGridRowList) {
				NameValueProperty nameValueProperty = new NameValueProperty();
				nameValueProperty.setPropertyName("");
				nameValueProperty.setPropertyValue(gridRow.getFieldName());
				outputFileds.add(nameValueProperty);
			}
		return outputFileds;
	}
}
