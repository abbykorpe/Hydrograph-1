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
package hydrograph.engine.cascading.avro.scheme;

import cascading.tuple.Fields;

public class AvroDescriptor {

	private Fields inputFields;
	private Class<?>[] fieldDataTypes;
	private int[] fieldScale;
	private int[] fieldPrecision;

	public AvroDescriptor(Fields inputFields, Class<?>[] fieldDataTypes) {
		this(inputFields, fieldDataTypes, new int[fieldDataTypes.length],
				new int[fieldDataTypes.length]);
	}

	public AvroDescriptor(Fields inputFields, Class<?>[] fieldDataTypes,
			int[] fieldPrecision, int[] fieldScale) {
		this.inputFields = inputFields;
		this.fieldDataTypes = fieldDataTypes;
		this.fieldScale = fieldScale;
		this.fieldPrecision = fieldPrecision;
	}

	public Fields getInputFields() {
		return inputFields;
	}

	public Class<?>[] getFieldDataTypes() {
		return fieldDataTypes;
	}

	public int[] getFieldScale() {
		return fieldScale;
	}

	public void setFieldScale(int[] fieldScale) {
		this.fieldScale = fieldScale;
	}

	public void setFieldPrecision(int[] fieldPrecision) {
		this.fieldPrecision = fieldPrecision;
	}

	public int[] getFieldPrecision() {
		return fieldPrecision;
	}

}
