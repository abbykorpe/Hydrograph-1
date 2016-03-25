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

 
package com.bitwise.app.engine.converter.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.datastructure.property.mapping.TransformMapping;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Transform;

public class TransformComponentConverter extends TransformConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(TransformComponentConverter.class);
	private TransformMapping atMapping;
	private List<FixedWidthGridRow> fixedWidthGridRows;
	ConverterHelper converterHelper;

	public TransformComponentConverter(Component component) {
		super();
		this.baseComponent = new Transform();
		this.component = component;
		this.properties = component.getProperties();
		atMapping = (TransformMapping) properties.get(Constants.PARAM_OPERATION);
		converterHelper = new ConverterHelper(component);
		initFixedWidthGridRows();
	}

	
	private void initFixedWidthGridRows(){
		fixedWidthGridRows = new LinkedList<>();
		Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) properties
				.get(Constants.SCHEMA_TO_PROPAGATE);
		if (schemaMap != null && schemaMap.get(Constants.FIXED_OUTSOCKET_ID) != null) {
			ComponentsOutputSchema componentsOutputSchema = schemaMap.get(Constants.FIXED_OUTSOCKET_ID);
			List<FixedWidthGridRow> gridRows = componentsOutputSchema.getFixedWidthGridRowsOutputFields();			
			for(FixedWidthGridRow row : gridRows){
				fixedWidthGridRows.add((FixedWidthGridRow) row.copy());
			}
		}
			
	}
	
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Transform transform = (Transform) baseComponent;
		transform.getOperation().addAll(getOperations());
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		return converterHelper.getOperations(atMapping,fixedWidthGridRows);
	}

	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		return converterHelper.getOutSocket(atMapping,fixedWidthGridRows);
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		return converterHelper.getInSocket();
	}
}
