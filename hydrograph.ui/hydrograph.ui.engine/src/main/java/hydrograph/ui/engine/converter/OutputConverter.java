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

 
package hydrograph.ui.engine.converter;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.impl.OutputFileDelimitedConverter;
import hydrograph.ui.engine.exceptions.SchemaException;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeBaseRecord;
import hydrograph.engine.jaxb.commontypes.TypeExternalSchema;
import hydrograph.engine.jaxb.commontypes.TypeOutputComponent;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;

public abstract class OutputConverter extends Converter {

	public OutputConverter(Component comp) {
		super(comp);
	}

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputFileDelimitedConverter.class);

	@Override
	public void prepareForXML() {
		super.prepareForXML();
		((TypeOutputComponent) baseComponent).getInSocket().addAll(getOutInSocket());
	}

	/**
	 * Returs the {@link List} of classes of type {@link TypeOutputInSocket}
	 * 
	 * @return {@link TypeOutputInSocket}
	 * @throws SchemaException
	 */
	protected abstract List<TypeOutputInSocket> getOutInSocket();

	/**
	 * Converts String value to {@link TypeBaseRecord}
	 * 
	 * @return {@link TypeBaseRecord}
	 * @throws SchemaException
	 */
	protected TypeBaseRecord getSchema() {
		logger.debug("Genrating TypeBaseRecord data for {}", properties.get(Constants.PARAM_NAME));
		TypeBaseRecord typeBaseRecord = new TypeBaseRecord();
		Schema schema = (Schema) properties.get(PropertyNameConstants.SCHEMA.value());
		if (schema != null) {
			if ( schema.getIsExternal()) {
				TypeExternalSchema typeExternalSchema = new TypeExternalSchema();
				typeExternalSchema.setUri(schema.getExternalSchemaPath());
				typeBaseRecord.setName(Constants.EXTERNAL_SCHEMA);
				typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().add(typeExternalSchema);
			} else
				typeBaseRecord.setName(Constants.INTERNAL_SCHEMA);
		}else
			typeBaseRecord.setName(Constants.INTERNAL_SCHEMA);
		
		if (fetchPropagatedSchema() != null && schema!=null && !schema.getIsExternal())
			typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().addAll(getFieldOrRecord(fetchPropagatedSchema()));

		return typeBaseRecord;
	}

	protected ComponentsOutputSchema fetchPropagatedSchema() {
		ComponentsOutputSchema componentsOutputSchema = null;
		if(!StringUtils.equals(Constants.OUTPUT_SUBJOB, component.getComponentName())){
		Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) properties
				.get(Constants.SCHEMA_TO_PROPAGATE);
		if (schemaMap != null && schemaMap.get(Constants.FIXED_OUTSOCKET_ID) != null)
			componentsOutputSchema = schemaMap.get(Constants.FIXED_OUTSOCKET_ID);
		}
		return componentsOutputSchema;
	}

	/**
	 * Prepare the Fields/Records for shcema
	 * 
	 * @param componentsOutputSchema
	 * @return {@link List}
	 * 
	 */
	protected abstract List<TypeBaseField> getFieldOrRecord(ComponentsOutputSchema componentsOutputSchema);
}
