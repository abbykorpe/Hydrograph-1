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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.PortInfo;
import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.Port;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwise.app.logging.factory.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class LinkCommand.
 */
public class LinkCommand extends Command{
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(LinkReconnectSourceCommand.class);
	
	/** The connection instance. */
	private Link connection;
	private Component source, target;
	private String sourceTerminal, targetTerminal;

	/**
	 * Instantiate a command that can create a connection between two shapes.
	 * @param source the source endpoint (a non-null Shape instance)
	 * @param lineStyle the desired line style. See Connection#setLineStyle(int) for details
	 * @throws IllegalArgumentException if source is null
	 * @see Link#setLineStyle(int)
	 */

	public LinkCommand() {
		super("connection");
	}

	/**
	 * Instantiates a new link command.
	 * 
	 * @param source
	 *            the source
	 * @param lineStyle
	 *            the line style
	 */
	public LinkCommand(Component source, int lineStyle) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel("Connection");
		this.source = source;
	}

	@Override
	public boolean canExecute() {
		String componentName;
		List<PortSpecification> portspecification;

		if(source!=null){
			//disallow the link to itself
			if (source.equals(target)) {
				return false;
			}

			//Out port restrictions
			componentName = DynamicClassProcessor.INSTANCE
					.getClazzName(source.getClass());

			portspecification=XMLConfigUtil.INSTANCE.getComponent(componentName).getPort().getPortSpecification();

			for (PortSpecification p:portspecification)
			{
				for(PortInfo portInfo:p.getPort()){
					String portName=p.getTypeOfPort().value()+portInfo.getSequenceOfPort();
					if(portName.equals(sourceTerminal)){
						if(portInfo.isAllowMultipleLinks() || 
								!source.isOutputPortEngaged(sourceTerminal)){
							
						}else{
							
							return false;
						}
					}
				}
				
			}

		}	

		//In port restrictions
		if(target!=null){
			componentName = DynamicClassProcessor.INSTANCE
					.getClazzName(target.getClass());

			portspecification=XMLConfigUtil.INSTANCE.getComponent(componentName).getPort().getPortSpecification();
			for (PortSpecification p:portspecification)
			{
				for(PortInfo portInfo:p.getPort()){
					String portName=p.getTypeOfPort().value()+portInfo.getSequenceOfPort();
					if(portName.equals(targetTerminal)){
						if(portInfo.isAllowMultipleLinks() ||
								!target.isInputPortEngaged(targetTerminal)){
							
						}else{
							
							return false;
						}
					}
				}
				
			}
		}


		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {

		if(source!=null){

			connection.setSource(source);
			connection.setSourceTerminal(sourceTerminal);
			connection.setLineStyle(Graphics.LINE_SOLID);
			connection.attachSource();

			source.engageOutputPort(sourceTerminal);
			
		}
		if(target!=null){

			connection.setTarget(target);
			connection.setTargetTerminal(targetTerminal);
			connection.setLineStyle(Graphics.LINE_SOLID);
			connection.attachTarget();

			target.engageInputPort(targetTerminal);
			
		}
		
		//Delete the link
		if (source == null && target == null) {
			
			
			Port sourcePort = connection.getSource().getPort(connection.getSourceTerminal());
			if(sourcePort.isWatched()){
				removeWatch(sourcePort, connection.getSource());
			}
			connection.detachSource();
			connection.detachTarget();
			connection.getSource().freeOutputPort(connection.getSourceTerminal());
			connection.getTarget().freeInputPort(connection.getTargetTerminal());
			connection.setTarget(null);
			connection.setSource(null);
			
		}
	}
	
	private void removeWatch(Port sourcePort, Component sourceComponent){
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();)
		{
			EditPart editPart = (EditPart) iterator.next();
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

	public void setTarget(Component target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}

	public void setSource(Component newSource) {
		if (newSource == null) {
			throw new IllegalArgumentException();
		}
		source = newSource;
	}

	public void setSourceTerminal(String newSourceTerminal) {
		sourceTerminal = newSourceTerminal;
	}

	public void setTargetTerminal(String newTargetTerminal) {
		targetTerminal = newTargetTerminal;
	}


	public void setConnection(Link w) {

		connection = w;
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public void undo() {
		source = connection.getSource();
		logger.debug("New Name :{}",source.getProperties().get("name"));
		target = connection.getTarget();
		sourceTerminal = connection.getSourceTerminal();
		targetTerminal = connection.getTargetTerminal();

		connection.detachSource();
		connection.detachTarget();
		
		source.freeOutputPort(connection.getSourceTerminal());
		target.freeInputPort(connection.getTargetTerminal());

		

	}
}