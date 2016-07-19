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

package hydrograph.ui.dataviewer.datastructures;

/**
 * 
 * Status message to be displayed in status bar
 * 
 * @author Bitwise
 *
 */
public class StatusMessage {
	private int returnCode;
	private String returnMessage="";
	
	public StatusMessage(int returnCode, String returnMessage) {
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}

	public StatusMessage(int returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * 
	 * Get return code
	 * 
	 * @return return code
	 */
	public int getReturnCode() {
		return returnCode;
	}

	/**
	 * 
	 * Get status Message
	 * 
	 * @return status message 
	 */
	public String getStatusMessage() {
		return returnMessage;
	}

	@Override
	public String toString() {
		return "StatusMessage [returnCode=" + returnCode + ", returnMessage=" + returnMessage + "]";
	}
}
