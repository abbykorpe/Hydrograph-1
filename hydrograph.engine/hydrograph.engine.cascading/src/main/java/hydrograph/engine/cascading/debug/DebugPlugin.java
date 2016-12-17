/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.cascading.debug;

import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.core.core.HydrographDebugInfo;
import hydrograph.engine.core.flowmanipulation.FlowManipulationContext;
import hydrograph.engine.core.flowmanipulation.ManipulatorListener;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DebugPlugin implements ManipulatorListener {

	private Map<String, Set<SchemaField>> schemaFieldsMap;

	@Override
	public List<TypeBaseComponent> execute(
			FlowManipulationContext manipulationContext) {
		List<TypeBaseComponent> typeBaseComponents = manipulationContext
				.getJaxbMainGraph();
		schemaFieldsMap = manipulationContext.getSchemaFieldMap();
		List<DebugPoint> debugGraphList = DebugUtils
				.extractDebugPoints(HydrographDebugInfo.DebugChecker
						.getViewData(manipulationContext.getJaxbDebugGraph()));
		for (DebugPoint debug : debugGraphList) {
			typeBaseComponents = createComponent(debug, typeBaseComponents,
					manipulationContext);
		}
		return typeBaseComponents;
	}

	private List<TypeBaseComponent> createComponent(DebugPoint debug,
			List<TypeBaseComponent> mainGraphList,
			FlowManipulationContext manipulationContext) {

		for (TypeBaseComponent baseComponent : mainGraphList) {
			if (debug.getFromComponentId().equalsIgnoreCase(
					baseComponent.getId())) {
				TypeBaseComponent clone = generateReplicateComponent(
						baseComponent, mainGraphList, debug);
//				TypeBaseComponent limit = generateLimitComponent(baseComponent,
//						mainGraphList, debug.getLimit(), clone);
				generateOutputTextComponent(baseComponent, mainGraphList,
						debug, schemaFieldsMap, clone,
						manipulationContext.getJobId(),
						manipulationContext.getBasePath());
				return mainGraphList;
			}
		}

		throw new RuntimeException("debug fromComponent_id not matched : "
				+ debug.getFromComponentId());
	}

	private TypeBaseComponent generateOutputTextComponent(
			TypeBaseComponent baseComponent,
			List<TypeBaseComponent> componentList, DebugPoint debug,
			Map<String, Set<SchemaField>> schemaFieldsMap,
			TypeBaseComponent component, String jobId, String basePath) {

		DebugContext debugContext = new DebugContext();
		debugContext.setBasePath(basePath);
		debugContext.setJobId(jobId);
		debugContext.setPreviousComponentId(component.getId());
		debugContext.setFromComponentId(debug.getFromComponentId());
		debugContext.setFromOutSocketId(debug.getOutSocketId());
		debugContext.setBatch(baseComponent.getBatch());
		debugContext.setSchemaFieldsMap(schemaFieldsMap);
		debugContext.setTypeBaseComponents(componentList);
		return ComponentBuilder.TEXT_OUTPUT_COMPONENT.create(debugContext);
	}

	private TypeBaseComponent generateOutputAvroComponent(
			TypeBaseComponent baseComponent,
			List<TypeBaseComponent> componentList, DebugPoint debug,
			Map<String, Set<SchemaField>> schemaFieldsMap,
			TypeBaseComponent component, String jobId, String basePath) {

		DebugContext debugContext = new DebugContext();
		debugContext.setBasePath(basePath);
		debugContext.setJobId(jobId);
		debugContext.setPreviousComponentId(component.getId());
		debugContext.setFromComponentId(debug.getFromComponentId());
		debugContext.setFromOutSocketId(debug.getOutSocketId());
		debugContext.setBatch(baseComponent.getBatch());
		debugContext.setSchemaFieldsMap(schemaFieldsMap);
		debugContext.setTypeBaseComponents(componentList);
		return ComponentBuilder.AVRO_OUTPUT_COMPONENT.create(debugContext);
	}

	private TypeBaseComponent generateLimitComponent(
			TypeBaseComponent baseComponent,
			List<TypeBaseComponent> componentList, long limit,
			TypeBaseComponent component) {

		DebugContext debugContext = new DebugContext();
		debugContext.setPreviousComponentId(component.getId());
		debugContext.setFromComponentId(component.getId());
		debugContext.setFromOutSocketId("out1");
		debugContext.setBatch(baseComponent.getBatch());
		debugContext.setTypeBaseComponents(componentList);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limitvalue", limit);
		debugContext.setParams(params);
		return ComponentBuilder.LIMIT_COMPONENT.create(debugContext);
	}

	private TypeBaseComponent generateReplicateComponent(
			TypeBaseComponent baseComponent,
			List<TypeBaseComponent> componentList, DebugPoint debug) {

		DebugContext debugContext = new DebugContext();
		debugContext.setFromComponentId(debug.getFromComponentId());
		debugContext.setFromOutSocketId(debug.getOutSocketId());
		debugContext.setBatch(baseComponent.getBatch());
		debugContext.setTypeBaseComponents(componentList);
		return ComponentBuilder.REPLICATE_COMPONENT.create(debugContext);
	}

}
