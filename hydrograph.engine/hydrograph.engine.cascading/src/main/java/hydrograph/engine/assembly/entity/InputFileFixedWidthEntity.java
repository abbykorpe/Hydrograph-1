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

import hydrograph.engine.assembly.entity.base.IOAssemblyEntity;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;

import java.util.Arrays;
import java.util.List;

public class InputFileFixedWidthEntity extends IOAssemblyEntity {

	private String path;
	private boolean strict = true;
	private boolean safe = false;
	private String charset = "UTF-8";
	private List<OutSocket> outSocketList;
	private List<SchemaField> fieldsList;


	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
	}

	@Override
	public List<SchemaField> getFieldsList() {
		return fieldsList;
	}

	public void setFieldsList(List<SchemaField> fieldsList) {
		this.fieldsList = fieldsList;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getPath() {
		return path;
	}

	public boolean isStrict() {
		return strict;
	}

	public boolean isSafe() {
		return safe;
	}

	public String getCharset() {
		return charset;
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
		StringBuilder str = new StringBuilder("Input file fixed width entity info:\n");
		str.append(super.toString());
		str.append("Path: " + path);
		str.append(" | strict: " + strict);
		str.append(" | safe: " + safe);
		str.append(" | charset: " + charset);

		str.append("\nfields: ");
		if (fieldsList != null) {
			str.append(Arrays.toString(fieldsList.toArray()));
		}
		
		str.append("\nout socket(s): ");
		if (outSocketList != null) {
			str.append(Arrays.toString(outSocketList.toArray()));
		}
		return str.toString();
	}
}