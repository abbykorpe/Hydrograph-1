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

package hydrograph.ui.engine.ui.util;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Bitwise
 * 
 *         This class is used to propagate schema after importing target XML.
 * 
 */
public class ImportedSchemaPropagation {

	public static final ImportedSchemaPropagation INSTANCE = new ImportedSchemaPropagation();

	private ImportedSchemaPropagation() {
	}

	/**
	 * Initiates schema propagation after importing target XML.
	 * 
	 * @param container
	 * @return
	 */
	public void initiateSchemaPropagationAfterImport(Container container) {
		for (Component component : container.getChildren()) {
			@SuppressWarnings("unchecked")
			Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) component
					.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
			if (schemaMap != null && !StringUtils.equalsIgnoreCase(component.getCategory(), Constants.TRANSFORM))
				SchemaPropagation.INSTANCE.continuousSchemaPropagation(component, schemaMap);
		}
		schemaPropagationForTransformCategory(container);
		removeTempraryProperties(container);
	}
	
	
	// This method propagates schema from transform components.
	private void schemaPropagationForTransformCategory(Container container) {
		for (Component component : container.getChildren()) {
			if (StringUtils.equalsIgnoreCase(component.getCategory(), Constants.TRANSFORM)) {
				Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) component
						.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
				if (schemaMap != null) {
					SchemaPropagation.INSTANCE.continuousSchemaPropagation(component, schemaMap);
					addSchemaForTransformComponents(component);
				}
			}
		}
	}

	//This method creates rows for schema tab of transform components from propagated schema.
	private void addSchemaForTransformComponents(Component component) {
		Schema schema = null;
		if (component != null && component.getProperties().get(Constants.SCHEMA_TO_PROPAGATE) != null) {
			Map<String, ComponentsOutputSchema> componentOutputSchemaMap = (Map<String, ComponentsOutputSchema>) component
					.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
			if (componentOutputSchemaMap != null && componentOutputSchemaMap.get(Constants.FIXED_OUTSOCKET_ID) != null) {
				schema = new Schema();
				schema.getGridRow().addAll(
						componentOutputSchemaMap.get(Constants.FIXED_OUTSOCKET_ID).getBasicGridRowsOutputFields());
				arrangeSchemaFieldsAndAddToComponentsSchema(schema,component);
			}
		}
	}

	//This method arranges the schema grid rows of schema tab as arranged in the target XML.
	private void arrangeSchemaFieldsAndAddToComponentsSchema(Schema schema, Component component) {
		Schema arrangedSchema=null;
		List<String> schemaFieldSequnce=null;
		if (schema != null && component != null) {
			arrangedSchema=new Schema();			
			if (component.getProperties().get(Constants.SCHEMA_FILED_SEQUENCE) != null) {
				schemaFieldSequnce = (List<String>) component.getProperties().get(Constants.SCHEMA_FILED_SEQUENCE);
				for(String fieldName:schemaFieldSequnce){
					if(schema.getGridRow(fieldName)!=null)
					arrangedSchema.getGridRow().add(schema.getGridRow(fieldName));
				}
				component.getProperties().put(Constants.SCHEMA_PROPERTY_NAME, arrangedSchema);
			} else 
				component.getProperties().put(Constants.SCHEMA_PROPERTY_NAME, schema);
		}
	}

	//This method removes temporary properties from components.
	private void removeTempraryProperties(Container container){
		for(Component component:container.getChildren()){
			component.getProperties().remove(Constants.SCHEMA_FILED_SEQUENCE);
		}
	}
	
}
