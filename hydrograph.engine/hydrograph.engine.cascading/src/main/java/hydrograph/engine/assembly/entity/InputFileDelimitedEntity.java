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

import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;

import java.util.Arrays;
import java.util.List;

import cascading.flow.FlowDef;

public class InputFileDelimitedEntity extends AssemblyEntityBase {

	private String path;
	private String quote = null;
	private boolean hasHeader = false;
	private FlowDef flowdef;
	private String delimiter;
	private boolean safe = false;
	private boolean strict = true;
	private String charset = "UTF-8";
	private List<OutSocket> outSocketList;
	private List<SchemaField> fieldsList;

	public InputFileDelimitedEntity() {
	}

	public List<SchemaField> getFieldsList() {
		return fieldsList;
	}

	public void setFieldsList(List<SchemaField> fieldsList) {
		this.fieldsList = fieldsList;
	}

	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	public FlowDef getFlowdef() {
		return flowdef;
	}

	public void setFlowdef(FlowDef flowdef) {
		this.flowdef = flowdef;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public boolean isSafe() {
		return safe;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
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
		StringBuilder str = new StringBuilder("Input file delimited entity info:\n");
		str.append(super.toString());
		str.append("Path: " + path);
		str.append(" | quote: " + quote);
		str.append(" | has header: " + hasHeader);
		str.append(" | delimiter: " + delimiter);
		str.append(" | safe: " + safe);
		str.append(" | strict: " + strict);
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
