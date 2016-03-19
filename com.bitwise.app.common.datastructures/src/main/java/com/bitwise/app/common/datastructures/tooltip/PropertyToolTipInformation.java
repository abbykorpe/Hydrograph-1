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

 
package com.bitwise.app.common.datastructures.tooltip;

public class PropertyToolTipInformation {
	private String propertyName;
	private Object propertyValue;
	private String showAsTooltip;
	private String tooltipDataType;
	private String errorMessage=null;
	
	public PropertyToolTipInformation(String propertyName, String showAsTooltip,
			String tooltipDataType) {
		super();
		this.propertyName = propertyName;
		this.showAsTooltip = showAsTooltip;
		this.tooltipDataType = tooltipDataType;
	}

	public boolean isShowAsTooltip() {
		return Boolean.parseBoolean(showAsTooltip);
	}

	public String getTooltipDataType() {
		return tooltipDataType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result
				+ ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result
				+ ((propertyValue == null) ? 0 : propertyValue.hashCode());
		result = prime * result
				+ ((showAsTooltip == null) ? 0 : showAsTooltip.hashCode());
		result = prime * result
				+ ((tooltipDataType == null) ? 0 : tooltipDataType.hashCode());
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
		PropertyToolTipInformation other = (PropertyToolTipInformation) obj;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (propertyValue == null) {
			if (other.propertyValue != null)
				return false;
		} else if (!propertyValue.equals(other.propertyValue))
			return false;
		if (showAsTooltip == null) {
			if (other.showAsTooltip != null)
				return false;
		} else if (!showAsTooltip.equals(other.showAsTooltip))
			return false;
		if (tooltipDataType == null) {
			if (other.tooltipDataType != null)
				return false;
		} else if (!tooltipDataType.equals(other.tooltipDataType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyToolTipInformation [propertyName=" + propertyName
				+ ", propertyValue=" + propertyValue + ", showAsTooltip="
				+ showAsTooltip + ", tooltipDataType=" + tooltipDataType
				+ ", errorMessage=" + errorMessage + "]";
	}

	
}
