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

 
package hydrograph.ui.graph.policy;

import hydrograph.ui.graph.command.LinkCommand;
import hydrograph.ui.graph.model.Link;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;


/**
 * The Class LinkEditPolicy.
 */
public class LinkEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {
	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		LinkCommand c = new LinkCommand();
		c.setConnection((Link) getHost().getModel());
		return c;
	}
}

