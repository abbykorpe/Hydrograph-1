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
package hydrograph.ui.graph.canvas.search;

import org.eclipse.jface.fieldassist.ContentProposal;

/**
 * ComponentContentProposal provides the component details
 * @author Bitwise
 *
 */
public class ComponentContentProposal extends ContentProposal {

	private ComponentDetails componentDetails;

	public ComponentContentProposal(ComponentDetails componentDetails) {
		super(componentDetails.getCategoryAndPalletteName(), componentDetails.getDescription());
		this.componentDetails = componentDetails;
	}

	/**
	 * Gets the component details
	 * @return
	 */
	public ComponentDetails getComponentDetails() {
		return componentDetails;
	}

	/**
	 * Sets the component details
	 * @param componentDetails
	 */
	public void setComponentDetails(ComponentDetails componentDetails) {
		this.componentDetails = componentDetails;
	}
}
