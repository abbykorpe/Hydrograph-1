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
import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.OutSocket;

public class FilterEntity extends OperationAssemblyEntity {

	private List<OutSocket> outSocketList;
	private int numOperations;
	private boolean operationPresent;
	private Operation operation;

	// private List<String[]> passThroughFieldsList;

	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
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

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
		if (operation != null) {
			operationPresent = true;
			numOperations = 1; // FilterEntity has provision for just 1
								// operation
		}
		else{
			operationPresent = false;
			numOperations = 0;
		}
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return this.operation;
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
		StringBuilder str = new StringBuilder("Filter entity information\n");
		str.append(super.toString());

		if (operationPresent) {
			str.append(numOperations
					+ " operation(s) present, Operation info: ");
			if (operation != null) {
				str.append(operation.toString());
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

	@Override
	public List<Operation> getOperationsList() {
		return new ArrayList<>();
	}}
