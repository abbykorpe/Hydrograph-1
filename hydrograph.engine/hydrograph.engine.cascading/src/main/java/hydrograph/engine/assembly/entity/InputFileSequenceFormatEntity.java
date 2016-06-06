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

import java.util.List;

import cascading.flow.FlowDef;
import hydrograph.engine.assembly.entity.base.IOAssemblyEntity;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;

public class InputFileSequenceFormatEntity extends IOAssemblyEntity {

	private String path;
	private FlowDef flowdef;
	private List<OutSocket> outSocketList;
	private List<SchemaField> fieldsList;

	public InputFileSequenceFormatEntity() {
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


	public FlowDef getFlowdef() {
		return flowdef;
	}

	public void setFlowdef(FlowDef flowdef) {
		this.flowdef = flowdef;
	}

}
