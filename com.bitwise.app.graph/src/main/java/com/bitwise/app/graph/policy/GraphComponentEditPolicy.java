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

 
package com.bitwise.app.graph.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.bitwise.app.graph.command.ComponentDeleteCommand;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;

/**
 * The Class GraphComponentEditPolicy.
 */
public class GraphComponentEditPolicy extends ComponentEditPolicy {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(
	 * org.eclipse.gef.requests.GroupRequest)
	 */
//	@Override
//	protected Command createDeleteCommand(GroupRequest deleteRequest) {
//		Object parent = getHost().getParent().getModel();
//		Object child = getHost().getModel();
//		if (parent instanceof Container && child instanceof Component) {
//			return new ComponentDeleteCommand((Container) parent,
//					(Component) child);
//		}
//		return super.createDeleteCommand(deleteRequest);
//	}

}
