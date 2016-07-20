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
package hydrograph.engine.assembly.entity.base;

import java.util.List;

import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;

public abstract class InputOutputEntityBase extends AssemblyEntityBase {
	private List<OutSocket> outSocketList;
	private List<SchemaField> schemaFieldsList;

	/**
	 * @return the outSocketList
	 */
	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	/**
	 * @param outSocketList
	 *            the outSocketList to set
	 */
	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
	}

	/**
	 * @return the schemaFieldsList
	 */
	public List<SchemaField> getFieldsList() {
		return schemaFieldsList;
	}

	/**
	 * @param schemaFieldsList
	 *            the schemaFieldsList to set
	 */
	public void setFieldsList(List<SchemaField> schemaFieldsList) {
		this.schemaFieldsList = schemaFieldsList;
	}
}