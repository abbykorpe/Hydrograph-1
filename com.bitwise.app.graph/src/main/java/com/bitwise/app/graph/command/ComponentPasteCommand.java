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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.Model;

/**
 * The Class ComponentPasteCommand.
 */
public class ComponentPasteCommand extends Command {
	private static final Logger log = LogFactory.INSTANCE.getLogger(ComponentPasteCommand.class);
	private int pasteCounter=0;
	private Map<Component,Component> list = new HashMap<>();

	@Override
	public boolean canExecute() {
		List bList = (ArrayList) Clipboard.getDefault().getContents();
		if (bList == null || bList.isEmpty())
			return false;
		Iterator it = bList.iterator();
		while (it.hasNext()) {
			Component node = (Component) it.next();
			if (isPastableNode(node)) {
				list.put(node, null);
			}
		}

		return true;
	}

	@Override
	public void execute() {
		if (!canExecute())
			return;
		Iterator<Component> it = list.keySet().iterator();
		while (it.hasNext()) {
       	Component node = it.next();                              
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
					Component clonedComponent =  node.clone();
					clonedComponent.setParent(((ELTGraphicalEditor) page.getActiveEditor()).getContainer());
					Point location = node.getLocation();
					int incrementedLocation = pasteCounter * 20;
					clonedComponent.setLocation(new Point(location.getCopy().x + incrementedLocation,
						location.getCopy().y + incrementedLocation));
					list.put(node,clonedComponent);
			
			} catch (CloneNotSupportedException e) {
				log.error("Object could not cloned", e);
				
			}
		}
		redo();
	}

	@Override
	public void redo() {
		Iterator<Component> it = list.values().iterator();
		while (it.hasNext()) {
			Component node = it.next();
			if (isPastableNode(node)) {
				node.getParent().addChild(node);
			}
		}
		
		pasteLinks();
		
	}

	private void pasteLinks() {
		for(Component originalNode:list.keySet()){
			if(!originalNode.getSourceConnections().isEmpty()){
				
				for(Link originlink: originalNode.getSourceConnections()){
					Component targetComponent = originlink.getTarget();
					Component newSource = list.get(originalNode);
					Component newtarget = list.get(targetComponent);					
					
					if(newSource!=null && newtarget!=null){
						Link link = new Link();
						link.setSourceTerminal(originlink.getSourceTerminal());
						link.setTargetTerminal(originlink.getTargetTerminal());
						link.setSource(newSource);
						link.setTarget(newtarget);
						newSource.connectOutput(link);
						newtarget.connectInput(link);
					}
				}
			}
		}
	}

	@Override
	public boolean canUndo() {
		return !(list.isEmpty());
	}

	@Override
	public void undo() {
		Iterator<Component> it = list.values().iterator();
		while (it.hasNext()) {
			Component node = it.next();
			if (isPastableNode(node)) {
				node.getParent().removeChild(node);
			}
		}
	}

	/**
	 * Checks if is pastable node.
	 * 
	 * @param node
	 *            the node
	 * @return true, if is pastable node
	 */
	public boolean isPastableNode(Model node) {
		if (node instanceof Component)
			return true;
		if (node instanceof Link)
			return true;
		return false;
	}

	/**
	 * 
	 * get paste counter
	 * 
	 * @return
	 */
	public int getPasteCounter() {
		return pasteCounter;
	}

	/**
	 * 
	 * Set paste counter
	 * 
	 * @param pasteCounter
	 */
	public void setPasteCounter(int pasteCounter) {
		this.pasteCounter = pasteCounter;
	}

}
