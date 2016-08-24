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

	public String getFromComponentId() {
		return fromComponentId;
	}

	public void setFromComponentId(String fromComponentId) {
		this.fromComponentId = fromComponentId;
	}

	public String getFromOutSocketId() {
		return fromOutSocketId;
	}

	public void setFromOutSocketId(String fromOutSocketId) {
		this.fromOutSocketId = fromOutSocketId;
	}

	public String getFromOutSocketType() {
		return fromOutSocketType;
	}

	public void setFromOutSocketType(String fromOutSocketType) {
		this.fromOutSocketType = fromOutSocketType;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

}
