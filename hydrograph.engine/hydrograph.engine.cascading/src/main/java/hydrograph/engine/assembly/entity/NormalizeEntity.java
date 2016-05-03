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
import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.OutSocket;

import java.util.Arrays;
import java.util.List;

public class NormalizeEntity extends AssemblyEntityBase {

	private List<Operation> operationList;
	private List<OutSocket> outSocketList;
	private boolean operationPresent;
	private int numOperations;

	public List<Operation> getOperationsList() {
		return operationList;
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

	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
	}

	public boolean isOperationPresent() {
		return operationPresent;
	}

	public void setOperationPresent(boolean operationPresent) {
		this.operationPresent = operationPresent;
	}

	public int getNumOperations() {
		return numOperations;
	}

	public void setNumOperations(int numOperations) {
		this.numOperations = numOperations;
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

		StringBuilder str = new StringBuilder("Normalize entity information\n");
		str.append(super.toString());

		if (operationPresent) {
			str.append(numOperations
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