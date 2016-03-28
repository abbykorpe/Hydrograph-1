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

 
package com.bitwise.app.engine.converter;

import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseRecord;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeInputComponent;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;

public abstract class InputConverter extends Converter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputConverter.class);
	
	@Override
	public void prepareForXML() {
		super.prepareForXML();
		((TypeInputComponent)baseComponent).getOutSocket().addAll(getInOutSocket());
	
		
	}

	/**
	 * Returs the {@link List} of classes of type {@link TypeInputOutSocket}
	 * @return {@link List}
	 * @throws SchemaException
	 */
	protected abstract List<TypeInputOutSocket> getInOutSocket();

	
	/** Converts String value to {@link TypeBaseRecord}
	 * @return {@link TypeBaseRecord}
	 * @throws SchemaException
	 */
	protected TypeBaseRecord getSchema(){
		LOGGER.debug("Genrating TypeBaseRecord data for {}", properties.get(Constants.PARAM_NAME));
		TypeBaseRecord typeBaseRecord = new TypeBaseRecord();
		Schema schema=  (Schema) properties.get(PropertyNameConstants.SCHEMA.value());
		if(schema!=null){
		if(schema.getIsExternal()){
			TypeExternalSchema typeExternalSchema=new TypeExternalSchema();
			typeExternalSchema.setUri(schema.getExternalSchemaPath());
			typeBaseRecord.setName("External");
			typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().add(typeExternalSchema);
		}else{
			typeBaseRecord.setName("Internal");
			typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().addAll(getFieldOrRecord(schema.getGridRow()));	
		}}else
			typeBaseRecord.setName("Internal");
		return typeBaseRecord;
	}

	/**
	 * Prepare the Fields/Records for shcema
	 * @param list 
	 * @return {@link List}
	 *
	 */
	protected abstract List<TypeBaseField> getFieldOrRecord(List<GridRow> list);

	
	
}

