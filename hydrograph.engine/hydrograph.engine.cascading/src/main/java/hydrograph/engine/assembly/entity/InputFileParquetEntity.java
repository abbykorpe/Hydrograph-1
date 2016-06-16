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
package hydrograph.engine.assembly.entity;

import java.util.Arrays;
import java.util.List;

import hydrograph.engine.assembly.entity.base.IOAssemblyEntity;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;

public class InputFileParquetEntity extends IOAssemblyEntity {

	private String path;
	private List<OutSocket> outSocketList;
	private List<SchemaField> schemaFieldsList;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
	}

	public List<SchemaField> getFieldsList() {
		return schemaFieldsList;
	}

	public void setFieldsList(List<SchemaField> schemaFieldsList) {
		this.schemaFieldsList = schemaFieldsList;
	}

	/**
	 * Returns a string with the values for all the members of this entity
	 * object.
	 * <p>
	 * Use cautiously as this is a very heavy operation.
	 * 
	 * @see hydrograph.engine.assembly.entity.base.AssemblyEntityBase#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(
				"Input file parquet entity info:\n");
		str.append(super.toString());
		str.append("Path: " + path);

		str.append("\nfields: ");
		if (schemaFieldsList != null) {
			str.append(Arrays.toString(schemaFieldsList.toArray()));
		}

		str.append("\nout socket(s): ");
		if (outSocketList != null) {
			str.append(Arrays.toString(outSocketList.toArray()));
		}
		return str.toString();
	}

}