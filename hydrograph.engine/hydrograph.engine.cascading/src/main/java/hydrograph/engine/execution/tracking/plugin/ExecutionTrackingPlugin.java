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
package hydrograph.engine.execution.tracking.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.assembly.entity.utils.StraightPullEntityUtils;
import hydrograph.engine.core.utilities.SocketUtilities;
import hydrograph.engine.flow.utils.FlowManipulationContext;
import hydrograph.engine.flow.utils.ManipulatorListener;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeCommandComponent;
import hydrograph.engine.jaxb.commontypes.TypeInputComponent;
import hydrograph.engine.jaxb.commontypes.TypeInputField;
import hydrograph.engine.jaxb.commontypes.TypeOperationInputFields;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutSocketAsInSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutputComponent;
import hydrograph.engine.jaxb.commontypes.TypeStraightPullComponent;
import hydrograph.engine.jaxb.commontypes.TypeTransformOperation;
import hydrograph.engine.jaxb.operationstypes.Filter;

public class ExecutionTrackingPlugin implements ManipulatorListener {
	List<TypeBaseComponent> jaxbObjectList = new ArrayList<TypeBaseComponent>();
	Map<String, Set<SchemaField>> schemaFieldsMap;
	
	/* 
	 * @see hydrograph.engine.flow.utils.ManipulatorListener#execute(hydrograph.engine.flow.utils.FlowManipulationContext)
	 */
	@Override
	public List<TypeBaseComponent> execute(FlowManipulationContext manipulationContext) {
		// TODO Auto-generated method stub
		TrackContext trackContext;
		List<TypeBaseComponent> orginalComponentList = manipulationContext.getJaxbMainGraph();
		jaxbObjectList.addAll(orginalComponentList);
		schemaFieldsMap = manipulationContext.getSchemaFieldMap();

		for (Iterator<TypeBaseComponent> iterator = orginalComponentList.iterator(); iterator.hasNext();) {
			TypeBaseComponent typeBaseComponent = (TypeBaseComponent) iterator.next();
			List<OutSocket> outSocketList = getOutSocketListofComponent(typeBaseComponent);
			for (OutSocket outSocket : outSocketList) {
				trackContext = new TrackContext();
				trackContext.setFromComponentId(typeBaseComponent.getId());
				trackContext.setPhase(typeBaseComponent.getPhase());
				trackContext.setFromOutSocketId(outSocket.getSocketId());
				trackContext.setFromOutSocketType(outSocket.getSocketType());
				Filter newFilter = generateFilterAfterEveryComponent(trackContext);

				// add Filter to existing component
				TypeBaseComponent component = TrackComponentUtils.getComponent(jaxbObjectList,
						trackContext.getFromComponentId(), trackContext.getFromOutSocketId());
				SocketUtilities.updateComponentInSocket(component, trackContext.getFromComponentId(),
						trackContext.getFromOutSocketId(), newFilter.getId(), "out0");

				jaxbObjectList.add(newFilter);
			}
		}
		
		return jaxbObjectList;
	}

	private Filter generateFilterAfterEveryComponent(TrackContext trackContext) {
		Filter filter = new Filter();
		TypeTransformOperation filterOperation = new TypeTransformOperation();

		Set<SchemaField> schemaFields = schemaFieldsMap
				.get(trackContext.getFromComponentId() + "_" + trackContext.getFromOutSocketId());

		TypeOperationInputFields typeOperationInputFields = new TypeOperationInputFields();
		TypeInputField typeInputField = new TypeInputField();
		typeInputField.setInSocketId(trackContext.getFromOutSocketId());
		typeInputField.setName(schemaFields.iterator().next().getFieldName());
		typeOperationInputFields.getField().add(typeInputField);

		filterOperation.setInputFields(typeOperationInputFields);
		filterOperation.setClazz(Counter.class.getCanonicalName());
		filter.setId(TrackComponentUtils.generateUniqueComponentId(trackContext.getFromComponentId(),
				"generatedHydrographFilter", jaxbObjectList));
		filter.setPhase(trackContext.getPhase());
		filter.getInSocket().add(TrackComponentUtils.getStraightPullInSocket(trackContext.getFromComponentId(),
				trackContext.getFromOutSocketId(), trackContext.getFromOutSocketType()));

		filter.getOutSocket().add(TrackComponentUtils.getStraightPullOutSocket("out0", "in0"));
		filter.getOperation().add(filterOperation);
		return filter;
	}

	private List<OutSocket> getOutSocketListofComponent(TypeBaseComponent typeBaseComponent) {
		// TODO Auto-generated method stub
		if (typeBaseComponent instanceof TypeInputComponent) {
			TypeInputComponent typeInputComponent = (TypeInputComponent) typeBaseComponent;
			return InputEntityUtils.extractOutSocket(typeInputComponent.getOutSocket());
		} else if (typeBaseComponent instanceof TypeOutputComponent) {
			return Collections.emptyList();
		} else if (typeBaseComponent instanceof TypeStraightPullComponent) {
			TypeStraightPullComponent typeStraightPullComponent = (TypeStraightPullComponent) typeBaseComponent;
			return StraightPullEntityUtils.extractOutSocketList(typeStraightPullComponent.getOutSocket());
		} else if (typeBaseComponent instanceof TypeOperationsComponent) {
			TypeOperationsComponent typeOperationsComponent = (TypeOperationsComponent) typeBaseComponent;
			return OperationEntityUtils.extractOutSocketList(typeOperationsComponent.getOutSocket());
		} else if (typeBaseComponent instanceof TypeCommandComponent) {
			return Collections.emptyList();
		}
		return Collections.emptyList();
	}

	public static class TrackComponentUtils {
		public static TypeBaseInSocket getStraightPullInSocket(String formComponentId, String outSocketId,
				String outSocketType) {
			TypeBaseInSocket baseInSocket = new TypeBaseInSocket();
			baseInSocket.setFromComponentId(formComponentId);
			baseInSocket.setFromSocketId(outSocketId);
			baseInSocket.setFromSocketType(outSocketType);
			baseInSocket.setId("in0");
			return baseInSocket;
		}

		private static TypeOperationsOutSocket getStraightPullOutSocket(String id, String inSocketId) {
			TypeOperationsOutSocket operationOutSocket = new TypeOperationsOutSocket();
			operationOutSocket.setId(id);
			TypeOutSocketAsInSocket typeOutSocketAsInSocket = new TypeOutSocketAsInSocket();
			typeOutSocketAsInSocket.setInSocketId(inSocketId);
			operationOutSocket.setCopyOfInsocket(typeOutSocketAsInSocket);
			return operationOutSocket;
		}

		public static String generateUniqueComponentId(String compId, String socketId,
				List<TypeBaseComponent> typeBaseComponents) {
			String newComponentID = compId + "_" + socketId;
			for (int i = 0; i < typeBaseComponents.size(); i++) {
				if (newComponentID.equalsIgnoreCase(typeBaseComponents.get(i).getId())) {
					newComponentID += "_" + i;
				}
			}
			return newComponentID;
		}

		public static TypeBaseComponent getComponent(List<TypeBaseComponent> jaxbGraph, String compId,
				String socketId) {
			for (TypeBaseComponent component : jaxbGraph) {
				for (TypeBaseInSocket inSocket : SocketUtilities.getInSocketList(component)) {
					if (inSocket.getFromComponentId().equalsIgnoreCase(compId)
							&& inSocket.getFromSocketId().equalsIgnoreCase(socketId)) {
						return component;
					}
				}
			}
			throw new RuntimeException("debug FromComponent id: " + compId + " or Socket id: " + socketId
					+ " are not properly configured");
		}

	}
}
