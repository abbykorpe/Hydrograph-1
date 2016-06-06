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

import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.jaxb.commontypes.FieldDataTypes;
import hydrograph.engine.jaxb.commontypes.ScaleTypeList;
import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeBaseRecord;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;

import java.util.Set;

import javax.xml.namespace.QName;

public class JaxbSchemaFieldConverter {

	private JaxbSchemaFieldConverter() {
	}

	public static TypeOutputInSocket convertToJaxb(
			Set<SchemaField> schemaFieldList) {
		TypeOutputInSocket typeOutputInSocket = new TypeOutputInSocket();
		TypeBaseRecord record = new TypeBaseRecord();
		for (SchemaField schemaField : schemaFieldList) {
			TypeBaseField typeBaseField = new TypeBaseField();
			typeBaseField.setName(schemaField.getFieldName());
			setFieldScale(schemaField, typeBaseField);
			setFieldPrecision(schemaField, typeBaseField);
			// setFieldScaleType(schemaField, typeBaseField);
			setFieldFormat(schemaField, typeBaseField);
			 typeBaseField.setType(FieldDataTypes.fromValue(schemaField
			 .getFieldDataType()));
			//typeBaseField.setType(FieldDataTypes.JAVA_LANG_STRING);
			record.getFieldOrRecordOrIncludeExternalSchema().add(typeBaseField);
		}
		typeOutputInSocket.setSchema(record);
		return typeOutputInSocket;
	}

	public static void setFieldScale(SchemaField schemaField,
			TypeBaseField typeBaseField) {
		if (schemaField.getFieldScale() != 0)
			typeBaseField.setScale(schemaField.getFieldScale());
	}

	public static void setFieldPrecision(SchemaField schemaField,
			TypeBaseField typeBaseField) {
		if (schemaField.getFieldPrecision() != 0)
			typeBaseField.setPrecision(schemaField.getFieldPrecision());
	}

	public static void setFieldScaleType(SchemaField schemaField,
			TypeBaseField typeBaseField) {
		if (schemaField.getFieldScaleType().equals(ScaleTypeList.EXPLICIT))
			typeBaseField.setScaleType(ScaleTypeList.EXPLICIT);
		else
			typeBaseField.setScaleType(ScaleTypeList.IMPLICIT);
	}

	public static void setFieldFormat(SchemaField schemaField,
			TypeBaseField typeBaseField) {
		if (schemaField.getFieldFormat() != null) {
			typeBaseField.setFormat(schemaField.getFieldFormat());
		}
	}

	public static void setFieldLength(SchemaField schemaField,
			TypeBaseField typeBaseField) {
		QName qname = new QName("length");
		if (schemaField.getFieldLength() != 0)
			typeBaseField.getOtherAttributes().put(qname,
					String.valueOf(schemaField.getFieldLength()));
		else
			typeBaseField.getOtherAttributes().put(qname, String.valueOf(0));

	}
}
