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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.Model;
import com.bitwise.app.graph.model.Port;


/**
 * The Class ComponentDeleteCommand.
 */
public class ComponentDeleteCommand extends Command {
	
	private List<Component> selectedComponents;
	private Set<Component> deleteComponents;
	private final Container parent;
	private boolean wasRemoved;
	private final List<Link> sourceConnections;
	private final List<Link> targetConnections;

	/**
	 * Instantiates a new component delete command.
	 * 
	 */
	public ComponentDeleteCommand() {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();	
		this.parent=(((ELTGraphicalEditor) page.getActiveEditor()).getContainer());
		selectedComponents = new ArrayList<Component>();
		deleteComponents = new LinkedHashSet<>();
		sourceConnections = new ArrayList<Link>();
		targetConnections = new ArrayList<Link>();

	}

	/**
	 * 
	 * Add component to delete in selected component list
	 * 
	 * @param node
	 */
	public void addComponentToDelete(Component node){
		selectedComponents.add(node);
	}

	/**
	 * 
	 * returns false if selected component list is empty
	 * 
	 * @return boolean
	 */
	public boolean hasComponentToDelete(){
		return !selectedComponents.isEmpty();
	}

	@Override
	public boolean canExecute() {
		if (selectedComponents == null || selectedComponents.isEmpty())
			return false;

		Iterator<Component> it = selectedComponents.iterator();
		while (it.hasNext()) {
			Component node = (Component) it.next();
			if (isDeletableNode(node)) {
				deleteComponents.add(node);
			}
		}
		return true;

	}

	private boolean isDeletableNode(Model node) {
		if (node instanceof Component)
			return true;
		else
			return false;
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
		Iterator<Component> it = deleteComponents.iterator();
		while(it.hasNext()){
			Component deleteComp=(Component)it.next();
			deleteConnections(deleteComp);
			wasRemoved = parent.removeChild(deleteComp);
		}

	}

	@Override
	public void undo() {
		Iterator<Component> it = deleteComponents.iterator();
		while(it.hasNext()){
			Component restoreComp=(Component)it.next();
			parent.addChild(restoreComp);
			restoreConnections();
		}

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
			
			Port sourcePort = link.getSource().getPort(link.getSourceTerminal());
			if(sourcePort.isWatched()){
				removeWatch(sourcePort, link.getSource());
			}
			
			link.detachSource();
			link.detachTarget();
			if(link.getSource()!=null)
				link.getSource().freeOutputPort(link.getSourceTerminal());
			if(link.getTarget()!=null)
				link.getTarget().freeInputPort(link.getTargetTerminal());
		}
	}

	private void removeWatch(Port sourcePort, Component sourceComponent){
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Object objectEditPart : graphicalViewer.getEditPartRegistry().values()) 
		{
			EditPart editPart = (EditPart) objectEditPart;
			if(editPart instanceof ComponentEditPart) 
			{
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				if(comp.equals(sourceComponent)){
					List<PortEditPart> portEditParts = editPart.getChildren();
					for(AbstractGraphicalEditPart part:portEditParts)
					{
						if(part instanceof PortEditPart){
							if(((PortEditPart)part).getCastedModel().getTerminal().equals(sourcePort.getTerminal())){
								((PortEditPart)part).getPortFigure().removeWatchColor();
								((PortEditPart)part).getPortFigure().setWatched(false);
							} 
						}
					}
				}
			} 
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
