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
import java.util.List;

import hydrograph.engine.assembly.entity.base.OperationAssemblyEntity;
import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.OutSocket;

public class UnionAllEntity extends OperationAssemblyEntity {

	private OutSocket outSocket;

	public OutSocket getOutSocket() {
		return outSocket;
	}

	public void setOutSocket(OutSocket outSocket) {
		this.outSocket = outSocket;
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

		StringBuilder str = new StringBuilder("Union all entity information\n");
		str.append(super.toString());

		str.append("Out socket(s): ");
		if (outSocket != null) {
			str.append(outSocket.toString());
		}
		return str.toString();
	}

	@Override
	public List<Operation> getOperationsList() {
		return new ArrayList<>();
	}
}