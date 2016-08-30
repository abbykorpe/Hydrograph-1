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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cascading.cascade.Cascade;
import cascading.flow.Flow;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.assembly.entity.utils.StraightPullEntityUtils;
import hydrograph.engine.core.utilities.SocketUtilities;
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

class TrackComponentUtils {
	
	 /**
	 * Creates an object of type {@link TypeBaseInSocket} from component's fromComponentId,
	 *   outSocketId and outSocketType
	 * @param fromComponentId
	 * 					fromComponentId to set in {@link TypeBaseInSocket} object
	 * @param outSocketId
	 * 					outSocketId to set in {@link TypeBaseInSocket} object
	 * @param outSocketType
	 * 					outSocketType to set in {@link TypeBaseInSocket} object
	 * @return an object of type {@link TypeBaseInSocket}
	 */
	static TypeBaseInSocket getStraightPullInSocket(String fromComponentId, String outSocketId,
			String outSocketType) {
		TypeBaseInSocket baseInSocket = new TypeBaseInSocket();
		baseInSocket.setFromComponentId(fromComponentId);
		baseInSocket.setFromSocketId(outSocketId);
		baseInSocket.setFromSocketType(outSocketType);
		baseInSocket.setId("in0");
		return baseInSocket;
	}

	 /**
	 * Creates an object of type {@link TypeOperationsOutSocket} from id and inSocketId
	 * @param id
	 * 			id to set in in {@link TypeOperationsOutSocket} object
	 * @param inSocketId
	 * 				inSocketId to set in in {@link TypeOperationsOutSocket} object
	 * @return an object of type {@link TypeOperationsOutSocket}
	 */
	static TypeOperationsOutSocket getStraightPullOutSocket(String id, String inSocketId) {
		TypeOperationsOutSocket operationOutSocket = new TypeOperationsOutSocket();
		operationOutSocket.setId(id);
		TypeOutSocketAsInSocket typeOutSocketAsInSocket = new TypeOutSocketAsInSocket();
		typeOutSocketAsInSocket.setInSocketId(inSocketId);
		operationOutSocket.setCopyOfInsocket(typeOutSocketAsInSocket);
		return operationOutSocket;
	}

	 /**
	 * Generate UniqueComponentId for generated filter components
	 * @param compId
	 * 				compId to set
	 * @param socketId
	 * 				socketId to set
	 * @param typeBaseComponents
	 * 				- {@link TypeBaseComponent} to set
	 * @return the UniqueComponentId 
	 */
	static String generateUniqueComponentId(String compId, String socketId,
			List<TypeBaseComponent> typeBaseComponents) {
		String newComponentID = compId + "_" + socketId;
		for (int i = 0; i < typeBaseComponents.size(); i++) {
			if (newComponentID.equalsIgnoreCase(typeBaseComponents.get(i).getId())) {
				newComponentID += "_" + i;
			}
		}
		return newComponentID;
	}

	 /**
	 * Creates an object of type {@link TypeBaseComponent}
	 * @param jaxbGraph
	 * 				List of {@link TypeBaseComponent} object to set
	 * @param compId
	 * 				compId to set
	 * @param socketId
	 * 				socketId to set
	 * @return the {@link TypeBaseComponent} object
	 */
	static TypeBaseComponent getComponent(List<TypeBaseComponent> jaxbGraph, String compId,
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
	
	 /**
	 * Creates an object of type {@link Filter}
	 * @param trackContext
	 * 					- {@link TrackContext} to set
	 * @param jaxbObjectList
	 * 					- List of {@link TypeBaseComponent} object to set
	 * @param schemaFieldsMap
	 * 					 - Set of {@link SchemaField} object to set
	 * @return the object of type {@link Filter}
	 */
	static Filter generateFilterAfterEveryComponent(TrackContext trackContext, List<TypeBaseComponent> jaxbObjectList,
			Map<String, Set<SchemaField>> schemaFieldsMap) {
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

	 /**
	 * Creates a List of OutSocket object
	 * @param typeBaseComponent
	 * 					- {@link TypeBaseComponent} to set
	 * @return the List of {@link OutSocket}
	 */
	static List<OutSocket> getOutSocketListofComponent(TypeBaseComponent typeBaseComponent) {
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
	
	 /**
	 * Creates an object of type boolean to 
	 * @param cascade
	 * 				- {@link Cascade} to set
	 * @return the job running is local or not
	 */
	static boolean isLocalFlowExecution(Cascade cascade) {
		Flow<?> flow = cascade.getFlows().get(0);
		// PlatformInfo PlatformInfo = flow.getPlatformInfo();
		return flow.stepsAreLocal();
	}

}
