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
package hydrograph.engine.core.schemapropagation;

import hydrograph.engine.core.component.entity.elements.MapField;
import hydrograph.engine.core.component.entity.elements.OperationField;
import hydrograph.engine.core.component.entity.elements.OutSocket;
import hydrograph.engine.core.component.entity.elements.PassThroughField;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.core.component.entity.utils.InputEntityUtils;
import hydrograph.engine.core.component.entity.utils.OperationEntityUtils;
import hydrograph.engine.core.component.entity.utils.OutputEntityUtils;
import hydrograph.engine.core.utilities.SocketUtilities;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseRecord;
import hydrograph.engine.jaxb.commontypes.TypeInputComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOutputComponent;
import hydrograph.engine.jaxb.commontypes.TypeStraightPullComponent;
import hydrograph.engine.jaxb.commontypes.TypeStraightPullOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeTransformExpression;
import hydrograph.engine.jaxb.commontypes.TypeTransformOperation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OperationHandler implements Serializable {

	private TypeBaseComponent baseComponent;
	private Map<String, Set<SchemaField>> schemaFields;

	public OperationHandler(TypeBaseComponent baseComponent, Map<String, Set<SchemaField>> schemaFields) {
		this.baseComponent = baseComponent;
		this.schemaFields = schemaFields;
	}

	public Map<String, Set<SchemaField>> getOperation() {

		Map<String, Set<SchemaField>> newSchemaFields = new LinkedHashMap<String, Set<SchemaField>>();
		List<OutSocket> operationList = OperationEntityUtils
				.extractOutSocketList(((TypeOperationsComponent) baseComponent).getOutSocket());
		for (OutSocket outSocket : operationList) {
			HashSet<SchemaField> schemaFieldList = new LinkedHashSet<SchemaField>();
			schemaFieldList.addAll(getPassThroughFields(outSocket, baseComponent));
			schemaFieldList.addAll(getMapFields(outSocket, baseComponent));
			schemaFieldList.addAll(getOperationFields(outSocket, baseComponent));

			schemaFieldList.addAll(getCopyOfInsocket(outSocket, baseComponent));
			newSchemaFields.put(baseComponent.getId() + "_" + outSocket.getSocketId(), schemaFieldList);
		}
		return newSchemaFields;

	}

	private Set<SchemaField> getCopyOfInsocket(OutSocket outSocket, TypeBaseComponent baseComponent2) {
		Set<SchemaField> schemaFieldsList = new LinkedHashSet<SchemaField>();
		List<TypeBaseInSocket> baseInSocketList = ((TypeOperationsComponent) baseComponent).getInSocket();
		if (outSocket.getCopyOfInSocketId() != null && !outSocket.getCopyOfInSocketId().equals(""))
			for (TypeBaseInSocket baseInSockets : baseInSocketList) {
				if (baseInSockets.getId().equalsIgnoreCase(outSocket.getCopyOfInSocketId())) {
					schemaFieldsList.addAll(schemaFields
							.get(baseInSockets.getFromComponentId() + "_" + baseInSockets.getFromSocketId()));
				}
			}
		return schemaFieldsList;
	}

	private Set<SchemaField> getOperationFields(OutSocket outSocket, TypeBaseComponent baseComponent) {
		Map<String, HashSet<SchemaField>> schemaFieldMap = getOperationOutputFields(baseComponent);
		Set<SchemaField> schemaFieldList = new LinkedHashSet<SchemaField>();
		for (OperationField operationFields : outSocket.getOperationFieldList()) {
			schemaFieldList.add(
					getSchemaField(schemaFieldMap.get(operationFields.getOperationId()), operationFields.getName()));
		}
		return schemaFieldList;
	}

	private Map<String, HashSet<SchemaField>> getOperationOutputFields(TypeBaseComponent baseComponent) {
		List<Object> operationList = ((TypeOperationsComponent) baseComponent).getOperationOrExpression();
		Map<String, HashSet<SchemaField>> newSchemaFieldMap = new LinkedHashMap<String, HashSet<SchemaField>>();
		HashSet<SchemaField> newSchemaFieldList = new LinkedHashSet<SchemaField>();
		for (Object transformOperation : operationList) {
			if (transformOperation instanceof TypeTransformOperation) {
				List<Object> outputFieldList = new ArrayList<Object>(
						((TypeTransformOperation) transformOperation).getOutputFields() != null
								? ((TypeTransformOperation) transformOperation).getOutputFields().getField()
								: new ArrayList<Object>());
				newSchemaFieldList.addAll(InputEntityUtils.extractInputFields(outputFieldList));
				newSchemaFieldMap.put(((TypeTransformOperation) transformOperation).getId(), newSchemaFieldList);
			} else {
				List<Object> outputFieldList = new ArrayList<Object>();
				if (((TypeTransformExpression) transformOperation).getOutputFields() != null)
					outputFieldList.add(((TypeTransformExpression) transformOperation).getOutputFields().getField());
				newSchemaFieldList.addAll(InputEntityUtils.extractInputFields(outputFieldList));
				newSchemaFieldMap.put(((TypeTransformExpression) transformOperation).getId(), newSchemaFieldList);
			}
		}
		return newSchemaFieldMap;
	}

	private Set<SchemaField> getMapFields(OutSocket outSocket, TypeBaseComponent baseComponent) {
		Set<SchemaField> mapFieldsList = new LinkedHashSet<SchemaField>();
		for (MapField mapField : outSocket.getMapFieldsList()) {
			mapFieldsList.add(generateMapFields(baseComponent, mapField));
		}
		return mapFieldsList;
	}

	private SchemaField generateMapFields(TypeBaseComponent baseComponent, MapField mapField) {
		List<? extends TypeBaseInSocket> inSocketList = SocketUtilities.getInSocketList(baseComponent);

		for (TypeBaseInSocket inSocket : inSocketList) {
			if (inSocket.getId().equalsIgnoreCase(mapField.getInSocketId())) {
				SchemaField schemaField = getSchemaField(
						schemaFields.get(inSocket.getFromComponentId() + "_" + inSocket.getFromSocketId()),
						mapField.getSourceName());
				schemaField.setFieldName(mapField.getName());
				return schemaField;
			}
		}

		throw new RuntimeException("wrong insocket id in map Fields");

	}

	private Set<SchemaField> getPassThroughFields(OutSocket outSocket, TypeBaseComponent baseComponent) {
		Set<SchemaField> schemaFieldList = new LinkedHashSet<SchemaField>();

		for (PassThroughField passthroughFields : outSocket.getPassThroughFieldsList()) {
			schemaFieldList.addAll(generatePassthroughFields(baseComponent, passthroughFields));
		}
		return schemaFieldList;
	}

	private Set<SchemaField> generatePassthroughFields(TypeBaseComponent baseComponent,
			PassThroughField passthroughFields) {
		Set<SchemaField> passThroughFieldsList = new LinkedHashSet<SchemaField>();
		List<? extends TypeBaseInSocket> inSocketList = SocketUtilities.getInSocketList(baseComponent);
		for (TypeBaseInSocket inSocket : inSocketList) {
			if (inSocket.getId().equalsIgnoreCase(passthroughFields.getInSocketId())) {
				if (passthroughFields.getName().equals("*"))
					return schemaFields.get(inSocket.getFromComponentId() + "_" + inSocket.getFromSocketId());
				else {
					passThroughFieldsList.add(getSchemaField(
							schemaFields.get(inSocket.getFromComponentId() + "_" + inSocket.getFromSocketId()),
							passthroughFields.getName()));
					return passThroughFieldsList;
				}

			}
		}
		throw new RuntimeException("wrong insocket id in passthrough fields");
	}

	private SchemaField getSchemaField(Set<SchemaField> schemaFieldList, String fieldName) {
		for (SchemaField schemaField : schemaFieldList) {
			if (schemaField.getFieldName().equalsIgnoreCase(fieldName)) {
				return schemaField.clone();
			}
		}
		return null;
	}

	private static Set<SchemaField> inputComponentSchemaFields(TypeBaseComponent baseComponent) {
		List<Object> jaxbInFields = ((TypeInputComponent) baseComponent).getOutSocket().get(0).getSchema()
				.getFieldOrRecordOrIncludeExternalSchema();
		return new LinkedHashSet<SchemaField>(InputEntityUtils.extractInputFields(jaxbInFields));
	}

	private static Set<SchemaField> outputComponentSchemaFields(TypeBaseComponent baseComponent) {
		TypeBaseRecord inputSchema = ((TypeOutputComponent) baseComponent).getInSocket().get(0).getSchema();
		List<Object> jaxbInFields = inputSchema != null ? inputSchema.getFieldOrRecordOrIncludeExternalSchema()
				: new ArrayList<Object>();
		return new HashSet<SchemaField>(OutputEntityUtils.extractOutputFields(jaxbInFields));
	}

	public Map<String, Set<SchemaField>> getStraightPullSchemaFields() {
		Map<String, Set<SchemaField>> tempSchemaFieldsMap = new LinkedHashMap<String, Set<SchemaField>>();
		List<TypeBaseInSocket> baseInSocketList = ((TypeStraightPullComponent) baseComponent).getInSocket();
		List<TypeStraightPullOutSocket> straightPullOutSocketList = ((TypeStraightPullComponent) baseComponent)
				.getOutSocket();
		for (TypeStraightPullOutSocket straightPullOutSocket : straightPullOutSocketList) {
			for (TypeBaseInSocket baseInSockets : baseInSocketList) {
				if (baseInSockets.getId().equalsIgnoreCase(straightPullOutSocket.getCopyOfInsocket().getInSocketId())) {
					tempSchemaFieldsMap.put(baseComponent.getId() + "_" + straightPullOutSocket.getId(), schemaFields
							.get(baseInSockets.getFromComponentId() + "_" + baseInSockets.getFromSocketId()));
				}
			}
		}
		return tempSchemaFieldsMap;

	}

	public Map<String, Set<SchemaField>> getInputFields() {
		Map<String, Set<SchemaField>> tempSchemaFieldsMap = new LinkedHashMap<String, Set<SchemaField>>();
		String outSocketId = ((TypeInputComponent) baseComponent).getOutSocket().get(0).getId();
		tempSchemaFieldsMap.put(baseComponent.getId() + "_" + outSocketId, inputComponentSchemaFields(baseComponent));
		return tempSchemaFieldsMap;
	}

	public Map<String, Set<SchemaField>> getOutputFields() {
		Map<String, Set<SchemaField>> tempSchemaFieldsMap = new LinkedHashMap<String, Set<SchemaField>>();
		String inSocketId = ((TypeOutputComponent) baseComponent).getInSocket().get(0).getId();
		tempSchemaFieldsMap.put(baseComponent.getId() + "_" + inSocketId, outputComponentSchemaFields(baseComponent));
		return tempSchemaFieldsMap;
	}

}
