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

public class UniqueSequenceEntity extends AssemblyEntityBase {

	private List<OutSocket> outSocketList;
	// private Map<String, InSocket> inSocketMap;
	private boolean operationPresent;
	private int numOperations;
	private List<Operation> operationList;

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
	 * @return the operationList
	 */
	public List<Operation> getOperationList() {
		return operationList;
	}

	/**
	 * @param operationList
	 *            the operationList to set
	 */
	public void setOperationList(List<Operation> operationList) {
		this.operationList = operationList;
		if (operationList != null) {
			operationPresent = true;
			numOperations = operationList.size();
		} else {
			operationPresent = false;
			numOperations = 0;
		}
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

	/*	*//**
	 * @return the inSocketMap
	 */
	/*
	 * public Map<String, InSocket> getInSocketMap() { return inSocketMap; }
	 *//**
	 * @param inSocketMap
	 *            the inSocketMap to set
	 */
	/*
	 * public void setInSocketMap(Map<String, InSocket> inSocketMap) {
	 * this.inSocketMap = inSocketMap; }
	 */

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
				"Unique sequence entity information\n");
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
