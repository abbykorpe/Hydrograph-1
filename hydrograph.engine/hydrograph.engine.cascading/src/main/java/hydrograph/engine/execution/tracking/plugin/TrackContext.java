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
package hydrograph.engine.execution.tracking.plugin;

/**TrackContext Class to send the component data to ExecutionTrackingplugin
 * @author saketm
 *
 */
public class TrackContext {
	private String fromComponentId;
	private String fromOutSocketId;
	private String fromOutSocketType;
	private String phase;

	/**
	 * @return the fromComponentId
	 */
	public String getFromComponentId() {
		return fromComponentId;
	}

	/**
	 * @param fromComponentId
	 * 					fromComponentId to set
	 */
	public void setFromComponentId(String fromComponentId) {
		this.fromComponentId = fromComponentId;
	}

	/**
	 * @return the fromOutSocketId
	 */
	public String getFromOutSocketId() {
		return fromOutSocketId;
	}

	/**
	 * @param fromOutSocketId
	 * 					fromOutSocketId to set
	 */
	public void setFromOutSocketId(String fromOutSocketId) {
		this.fromOutSocketId = fromOutSocketId;
	}

	/**
	 * @return the fromOutSocketType
	 */
	public String getFromOutSocketType() {
		return fromOutSocketType;
	}

	/**
	 * @param fromOutSocketType
	 * 					fromOutSocketType to set
	 */
	public void setFromOutSocketType(String fromOutSocketType) {
		this.fromOutSocketType = fromOutSocketType;
	}

	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 * 			phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

}
