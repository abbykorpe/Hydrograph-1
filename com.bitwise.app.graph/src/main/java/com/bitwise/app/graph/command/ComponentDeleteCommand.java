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

 
package com.bitwise.app.graph.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;

// TODO: Auto-generated Javadoc
/**
 * The Class ComponentDeleteCommand.
 */
public class ComponentDeleteCommand extends Command {
	private final Component child;
	private final Container parent;
	private boolean wasRemoved;
	private final List<Link> sourceConnections = new ArrayList<Link>();
	private final List<Link> targetConnections = new ArrayList<Link>();

	/**
	 * Instantiates a new component delete command.
	 * 
	 * @param parent
	 *            the parent
	 * @param child
	 *            the child
	 */
	public ComponentDeleteCommand(Container parent, Component child) {
		this.parent = parent;
		this.child = child;
	}

	@Override
	public boolean canUndo() {
		return wasRemoved;
	}

	@Override
	public void execute() {
		redo();
	}

	@Override
	public void redo() {
		deleteConnections(child);
		wasRemoved = parent.removeChild(child);
	}

	@Override
	public void undo() {
		parent.addChild(child);
		restoreConnections();
	}
	
	private void deleteConnections(Component component) {
		
		sourceConnections.addAll(component.getSourceConnections());
		for (int i = 0; i < sourceConnections.size(); i++) {
			Link link = sourceConnections.get(i);
			link.detachSource();
			link.detachTarget();
			if(link.getSource()!=null)
				link.getSource().freeOutputPort(link.getSourceTerminal());
			if(link.getTarget()!=null)
				link.getTarget().freeInputPort(link.getTargetTerminal());
		}
		
		targetConnections.addAll(component.getTargetConnections());
		for (int i = 0; i < targetConnections.size(); i++) {
			Link link = targetConnections.get(i);
			link.detachSource();
			link.detachTarget();
			if(link.getSource()!=null)
				link.getSource().freeOutputPort(link.getSourceTerminal());
			if(link.getTarget()!=null)
				link.getTarget().freeInputPort(link.getTargetTerminal());
		}
	}
	
	private void restoreConnections() {
		for (int i = 0; i < sourceConnections.size(); i++) {
			Link link = sourceConnections.get(i);
			link.attachSource();
			link.getSource().engageOutputPort(link.getSourceTerminal());
			link.attachTarget();
			link.getTarget().engageInputPort(link.getTargetTerminal());
		}
		sourceConnections.clear();
		for (int i = 0; i < targetConnections.size(); i++) {
			Link link = targetConnections.get(i);
			link.attachSource();
			link.getSource().engageOutputPort(link.getSourceTerminal());
			link.attachTarget();
			link.getTarget().engageInputPort(link.getTargetTerminal());
		}
		targetConnections.clear();
	}

}
