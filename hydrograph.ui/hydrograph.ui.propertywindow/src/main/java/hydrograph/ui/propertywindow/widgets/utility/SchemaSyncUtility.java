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
import hydrograph.ui.datastructure.property.JoinMappingGrid;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.datastructure.property.LookupMappingGrid;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.graph.model.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * The Class FilterOperationClassUtility.
 * 
 * @author Bitwise
 */
public class SchemaSyncUtility {

	public static final String OPERATION = "operation";
	public static final String LOOKUP_MAP = "hash_join_map";
	public static final String JOIN_MAP = "join_mapping";

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
	
	/**
	 * Returns if schema sync is allowed for the component name passed as parameter.
	 *
	 * @param componentName
	 * @return boolean value if schema sync is allowed
	 */
	public boolean isSchemaSyncAllow(String componentName){
		return Constants.TRANSFORM.equalsIgnoreCase(componentName)
				|| Constants.AGGREGATE.equalsIgnoreCase(componentName) 
				|| Constants.NORMALIZE.equalsIgnoreCase(componentName) 
				|| Constants.CUMULATE.equalsIgnoreCase(componentName)
				|| Constants.LOOKUP.equalsIgnoreCase(componentName)
				|| Constants.JOIN.equalsIgnoreCase(componentName);
	}

	/**
	 * Push the schema from schema tab to Mapping in General tab
	 *
	 * @param component
	 * @param schemaGridRowList
	 */
	public void pushSchemaToMapping( Component component, List<GridRow> schemaGridRowList) {
		if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.AGGREGATE.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.NORMALIZE.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.CUMULATE.equalsIgnoreCase(component.getComponentName()) ){
				pushSchemaToTransformMapping(component, schemaGridRowList);
		}else if(Constants.LOOKUP.equalsIgnoreCase(component.getComponentName())){
			pushSchemaToLookupMapping( component, schemaGridRowList);
		}else if(Constants.JOIN.equalsIgnoreCase(component.getComponentName())){
			pushSchemaToJoinMapping( component, schemaGridRowList);
		}
	}
	
	/**
	 * Push the schema from schema tab to Mapping in General tab
	 *
	 * @param component
	 * @param schemaGridRowList
	 */
	public boolean isSyncRequired( Component component, List<GridRow> schemaGridRowList) {
		if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.AGGREGATE.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.NORMALIZE.equalsIgnoreCase(component.getComponentName()) ||
		   Constants.CUMULATE.equalsIgnoreCase(component.getComponentName()) ){
				//pushSchemaToTransformMapping(component, schemaGridRowList);
			return true;
		}else if(Constants.LOOKUP.equalsIgnoreCase(component.getComponentName())){
			//pushSchemaToLookupMapping( component, schemaGridRowList);
			return true;
		}else if(Constants.JOIN.equalsIgnoreCase(component.getComponentName())){
			return isSyncRequiredInJoin(component, schemaGridRowList);
		}else{
			return false;
		}
	}
	
	private boolean isSyncRequiredInJoin(Component component,
			List<GridRow> schemaGridRowList) {
		JoinMappingGrid joinMappingGrid = (JoinMappingGrid) component.getProperties().get(JOIN_MAP);
		
		List<String> schemaFieldList = getSchemaFieldList(schemaGridRowList);
		List<String> joinOutputFieldList = getOutputFieldsFromJoinMapping(joinMappingGrid);
		
		if(schemaFieldList == null && joinOutputFieldList == null){
			return false;
		}
		
		if(schemaFieldList.size()!=joinOutputFieldList.size()){
			return true;
		}
		
		for(int index=0;index<schemaFieldList.size();index++){
			if(!StringUtils.equals(schemaFieldList.get(index), joinOutputFieldList.get(index))){
				return true;
			}
		}
		return false;
	}

	/**
	 * Push the schema from schema tab to Mapping in General tab for Lookup component
	 *
	 * @param component
	 * @param schemaGridRowList
	 */
	public void pushSchemaToJoinMapping( Component component,
			List<GridRow> schemaGridRowList) {		
		JoinMappingGrid joinMappingGrid = (JoinMappingGrid) component.getProperties().get(JOIN_MAP);
		
		List<LookupMapProperty> mappingTableItemListCopy=new LinkedList<>();
		mappingTableItemListCopy.addAll(joinMappingGrid.getLookupMapProperties());
		joinMappingGrid.getLookupMapProperties().clear();
		
		
		List<String> schemaFieldList = getSchemaFieldList(schemaGridRowList);
		if(schemaFieldList.size() == 0){
			return;
		}
		
		for(String fieldName:schemaFieldList){
			LookupMapProperty row = getMappingTableItem(mappingTableItemListCopy,fieldName);
			if(row!=null){
				joinMappingGrid.getLookupMapProperties().add(row);
			}else{
				row=new LookupMapProperty();
				row.setSource_Field("");
				row.setOutput_Field(fieldName);
				joinMappingGrid.getLookupMapProperties().add(row);
			}
		}
	}
	
	private LookupMapProperty getMappingTableItem(List<LookupMapProperty> mappingTableItemListClone, String fieldName) {
		for(LookupMapProperty row:mappingTableItemListClone){
			if(StringUtils.equals(fieldName, row.getOutput_Field())){
				return row;
			}
		}
		return null;
	}

	public List<String> getSchemaFieldList(List<GridRow> schemaGridRowList) {
		List<String> schemaFieldList = new LinkedList<>();
		
		for(GridRow gridRow: schemaGridRowList){
			schemaFieldList.add(gridRow.getFieldName());
		}
		return schemaFieldList;
	}
	

	/**
	 * Push the schema from schema tab to Mapping in General tab for Lookup component
	 *
	 * @param component
	 * @param schemaGridRowList
	 */
	public void pushSchemaToLookupMapping( Component component,
			List<GridRow> schemaGridRowList) {
		LookupMappingGrid lookupMappingGrid = (LookupMappingGrid) component.getProperties().get(LOOKUP_MAP);
		List<String> lookupMapOutputs = getOutputFieldsFromLookupMapping(lookupMappingGrid);
		List<LookupMapProperty> outputFieldsFromSchema = getComponentSchemaAsLookupMapProperty(schemaGridRowList);
		List<LookupMapProperty> outputFieldsFromSchemaToRetain = getOutputFieldsFromSchemaToRetain(schemaGridRowList, lookupMappingGrid.getLookupMapProperties());
		
		lookupMappingGrid.setLookupMapProperties(outputFieldsFromSchemaToRetain);
		for (LookupMapProperty l : outputFieldsFromSchema){
			if(!lookupMapOutputs.contains(l.getOutput_Field())){
				lookupMappingGrid.getLookupMapProperties().add(l);
			}
		}
	}
	
	/**
	 * Pull the schema from schema tab to Mapping in General tab for Join component
	 *
	 * @param schema
	 * @param component
	 * @return The list of schema grid rows to be shown in mapping.
	 */
	public List<LookupMapProperty> pullJoinSchemaInMapping(Schema schema, Component component) {
		JoinMappingGrid joinMappingGrid = (JoinMappingGrid) component.getProperties().get(JOIN_MAP);
		List<String> lookupMapOutputs = getOutputFieldsFromJoinMapping(joinMappingGrid);
		
		List<LookupMapProperty> outputFieldsFromSchema = getComponentSchemaAsLookupMapProperty(schema.getGridRow());
		
		List<LookupMapProperty> outputFieldsFromSchemaToRetain = getOutputFieldsFromSchemaToRetain(schema.getGridRow(), joinMappingGrid.getLookupMapProperties());
		
		joinMappingGrid.getLookupMapProperties().retainAll(outputFieldsFromSchemaToRetain);
		
		for (LookupMapProperty l : outputFieldsFromSchema){
			if(!lookupMapOutputs.contains(l.getOutput_Field())){
				joinMappingGrid.getLookupMapProperties().add(l);
			}
		}
		return joinMappingGrid.getLookupMapProperties();
	}
	
	
	/**
	 * Pull the schema from schema tab to Mapping in General tab for Lookup component
	 *
	 * @param schema
	 * @param component
	 * @return The list of schema grid rows to be shown in mapping.
	 */
	public List<LookupMapProperty> pullLookupSchemaInMapping(Schema schema, Component component) {
		LookupMappingGrid lookupMappingGrid = (LookupMappingGrid) component.getProperties().get(LOOKUP_MAP);
		List<String> lookupMapOutputs = getOutputFieldsFromLookupMapping(lookupMappingGrid);
		
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

	private List<String> getOutputFieldsFromLookupMapping(
			LookupMappingGrid lookupMappingGrid) {
		List<String> lookupMapOutputs = new ArrayList<>();
		for (LookupMapProperty l : lookupMappingGrid.getLookupMapProperties()) {
			lookupMapOutputs.add(l.getOutput_Field());
		}
		return lookupMapOutputs;
	}
	
	private List<String> getOutputFieldsFromJoinMapping(
			JoinMappingGrid joinMappingGrid) {
		List<String> lookupMapOutputs = new ArrayList<>();
		for (LookupMapProperty l : joinMappingGrid.getLookupMapProperties()) {
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
