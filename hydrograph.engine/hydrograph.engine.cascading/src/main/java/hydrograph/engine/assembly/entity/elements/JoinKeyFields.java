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
/**
 * 
 */
package hydrograph.engine.assembly.entity.elements;

import java.util.Arrays;

import cascading.tuple.Fields;

/**
 * @author kirana
 *
 */
public class JoinKeyFields {

	private String inSocketId;
	private boolean recordRequired;
	private String[] fields;

	/**
	 * @param sourceName
	 * @param name
	 * @param inSocketId
	 */
	public JoinKeyFields(String inSocketId, boolean recordRequired, String[] fields) {
		this.inSocketId = inSocketId;
		this.recordRequired = recordRequired;
		this.fields = fields != null ? fields.clone() : null;
	}

	public Fields getFields() {
		return new Fields(fields);
	}

	public boolean isRecordRequired() {
		return recordRequired;
	}

	public String getInSocketId() {
		return inSocketId;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("In socket id: " + inSocketId);
		str.append(" | record required (inner): " + recordRequired);
		str.append(" | key fields: " + Arrays.toString(fields));

		return str.toString();
	}
}