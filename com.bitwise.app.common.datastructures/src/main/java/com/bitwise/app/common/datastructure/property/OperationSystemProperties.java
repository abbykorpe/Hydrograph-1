/********************************************************************************
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
 ******************************************************************************/

 
package com.bitwise.app.common.datastructure.property;

public class OperationSystemProperties extends PropertyField{
	private boolean isChecked;
	private String opSysValue;
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getOpSysValue() {
		return opSysValue;
	}
	public void setOpSysValue(String opSysValue) {
		this.opSysValue = opSysValue;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OperationSystemProperties [isChecked=");
		builder.append(isChecked);
		builder.append(", opSysValue=");
		builder.append(opSysValue);
		builder.append("]");
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((opSysValue == null) ? 0 : opSysValue.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationSystemProperties other = (OperationSystemProperties) obj;
		if (opSysValue == null) {
			if (other.opSysValue != null)
				return false;
		} else if (!opSysValue.equals(other.opSysValue))
			return false;
		return true;
	}

	
}
