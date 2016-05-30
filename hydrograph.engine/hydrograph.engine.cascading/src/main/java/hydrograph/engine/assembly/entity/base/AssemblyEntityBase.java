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

import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.SchemaField;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AssemblyEntityBase {

	private String componentId;
	private Properties runtimeProperties;
	private Integer phase;

	/**
	 * @return the componentId
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * @param componentID
	 *            the componentID to set
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * @return the runtimeProperties
	 */
	public Properties getRuntimeProperties() {
		return runtimeProperties;
	}

	/**
	 * @param runtimeProperties
	 *            the runtimeProperties to set
	 */
	public void setRuntimeProperties(Properties runtimeProperties) {
		this.runtimeProperties = runtimeProperties;
	}

	/**
	 * @return the phase
	 */
	public Integer getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(Integer phase) {
		this.phase = phase;
	}

	public List<SchemaField> getFieldsList() {
		return new ArrayList<>();
	}

	public List<Operation> getOperationsList() {
		return new ArrayList<>();
	}

	public boolean isOperationPresent() {
		return Boolean.FALSE;
	}

	@Override
	public String toString() {

		return "Component ID: " + componentId + " | phase: " + phase
				+ " | runtime properties: " + runtimeProperties + "\n";
	}
}
