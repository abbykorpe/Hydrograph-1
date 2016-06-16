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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hydrograph.engine.assembly.entity.base.OperationAssemblyEntity;
import hydrograph.engine.assembly.entity.elements.KeyField;
import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.OutSocket;

public class SortEntity extends OperationAssemblyEntity {
	private KeyField[] keyFields;
	private KeyField[] secondaryKeyFields;
	private List<OutSocket> outSocketList;

	/**
	 * @return the keyFields
	 */
	public KeyField[] getKeyFields() {
		return keyFields;
	}

	/**
	 * @param keyFields
	 *            the keyFields to set
	 */
	public void setKeyFields(KeyField[] keyFields) {
		this.keyFields = keyFields;
	}

	/**
	 * @return the secondaryKeyFields
	 */
	public KeyField[] getSecondaryKeyFields() {
		return secondaryKeyFields;
	}

	/**
	 * @param secondaryKeyFields
	 *            the secondaryKeyFields to set
	 */
	public void setSecondaryKeyFields(KeyField[] secondaryKeyFields) {
		this.secondaryKeyFields = secondaryKeyFields;
	}

	/**
	 * @return the outSocketList
	 */
	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	/**
	 * @param outSocketList the outSocketList to set
	 */
	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
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

		StringBuilder str = new StringBuilder("Aggregate entity information\n");
		str.append(super.toString());

		str.append("Key fields: ");
		if (keyFields != null) {
			str.append(Arrays.toString(keyFields));
		}

		str.append("\nSecondary key fields: ");
		if (secondaryKeyFields != null) {
			str.append(Arrays.toString(secondaryKeyFields));
		}

		str.append("\nOut socket(s): ");
		if (outSocketList != null) {
			str.append(Arrays.toString(outSocketList.toArray()));
		}
		return str.toString();
	}

	@Override
	public List<Operation> getOperationsList() {
		return new ArrayList<>();
	}
}