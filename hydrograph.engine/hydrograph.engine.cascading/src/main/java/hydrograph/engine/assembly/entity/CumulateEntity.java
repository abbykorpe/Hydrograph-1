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

import hydrograph.engine.assembly.entity.base.OperationAssemblyEntity;
import hydrograph.engine.assembly.entity.elements.KeyField;
import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.OutSocket;

public class CumulateEntity extends OperationAssemblyEntity {

	private KeyField[] keyFields;
	private KeyField[] secondaryKeyFields;
	private boolean operationPresent;
	private int numOperations;
	private List<Operation> operationList;
	private List<OutSocket> outSocketList;

	/**
	 * @return keyFields
	 */
	public KeyField[] getKeyFields() {
		return keyFields != null ? keyFields : null;
	}

	/**
	 * @param keyFields
	 *            the keyFields to set
	 */
	public void setKeyFields(KeyField[] keyFields) {
		this.keyFields = keyFields;
	}

	/**
	 * @return secondaryKeyFields
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
	 * @return the numOperations
	 */
	public int getNumOperations() {
		return numOperations;
	}

	/**
	 * @param numOperations
	 *            the numOperations to set
	 */
	public void setNumOperations(int numOperations) {
		this.numOperations = numOperations;
	}

	/**
	 * @return the operationPresent
	 */
	public boolean isOperationPresent() {
		return operationPresent;
	}

	/**
	 * @param operationPresent
	 *            the operationPresent to set
	 */
	public void setOperationPresent(boolean operationPresent) {
		this.operationPresent = operationPresent;
	}

	public void setOperationsList(List<Operation> operationList) {
		this.operationList = operationList;
		if (operationList != null) {
			operationPresent = true;
			numOperations = operationList.size();
		} else {
			operationPresent = false;
			numOperations = 0;
		}
	}

	public List<Operation> getOperationsList() {
		return this.operationList;
	}

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
	 * Returns a string with the values for all the members of this entity
	 * object.
	 * <p>
	 * Use cautiously as this is a very heavy operation.
	 * 
	 * @see hydrograph.engine.assembly.entity.base.AssemblyEntityBase#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Cumulate entity information\n");
		str.append(super.toString());

		str.append("Key fields: ");
		if (keyFields != null) {
			str.append(Arrays.toString(keyFields));
		}

		str.append("\nSecondary key fields: ");
		if (secondaryKeyFields != null) {
			str.append(Arrays.toString(secondaryKeyFields));
		}

		if (operationPresent) {
			str.append("\n" + numOperations
					+ " operation(s) present, Operation info: ");
			if (operationList != null) {
				str.append(Arrays.toString(operationList.toArray()));
			}
		} else {
			str.append("Operation not present\n");
		}

		str.append("\nOut socket(s): ");
		if (outSocketList != null) {
			str.append(Arrays.toString(outSocketList.toArray()));
		}
		return str.toString();
	}
}