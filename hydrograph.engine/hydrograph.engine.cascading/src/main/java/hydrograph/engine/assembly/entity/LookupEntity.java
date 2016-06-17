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
import hydrograph.engine.assembly.entity.elements.JoinKeyFields;
import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.OutSocket;

public class LookupEntity extends OperationAssemblyEntity {

	private List<OutSocket> outSocketList;
	private List<JoinKeyFields> joinKeyFields;
	private String match;

	/**
	 * @return the joinKeyFields
	 */
	public List<JoinKeyFields> getKeyFields() {
		return joinKeyFields;
	}

	/**
	 * @param joinKeyFields
	 *            the joinKeyFields to set
	 */
	public void setKeyFields(List<JoinKeyFields> joinKeyFields) {
		this.joinKeyFields = joinKeyFields;
	}

	public List<OutSocket> getOutSocketList() {
		return outSocketList;
	}

	public void setOutSocketList(List<OutSocket> outSocketList) {
		this.outSocketList = outSocketList;
	}

	public int getAllKeyFieldSize() {
		int i = 0;
		for (JoinKeyFields keyFields2 : joinKeyFields) {
			i = i + keyFields2.getFields().size();
		}

		return i;
	}

	public boolean[] getAllJoinTypes() {
		boolean[] joinTypes = new boolean[joinKeyFields.size()];
		int i = 0;
		for (JoinKeyFields keyFields2 : joinKeyFields) {
			joinTypes[i] = keyFields2.isRecordRequired();
			i++;
		}
		return joinTypes;
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

		StringBuilder str = new StringBuilder("Hash join entity information\n");
		str.append(super.toString());

		str.append("\nkey fields: ");
		if (joinKeyFields != null) {
			str.append(Arrays.toString(joinKeyFields.toArray()));
		}

		str.append("\nout socket: ");
		if (outSocketList != null) {
			str.append(Arrays.toString(outSocketList.toArray()));
		}

		return str.toString();
	}

	public void setMatch(String match) {
		this.match = match; 
		
	}

	public String getMatch() {
		return match;
	}

	@Override
	public List<Operation> getOperationsList() {
		return new ArrayList<>();
	}
}
