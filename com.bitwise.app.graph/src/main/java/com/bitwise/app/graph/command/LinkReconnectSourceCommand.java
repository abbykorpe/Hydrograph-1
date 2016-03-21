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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.PortInfo;
import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class LinkReconnectSourceCommand.
 */
public class LinkReconnectSourceCommand extends Command {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(LinkReconnectSourceCommand.class);
	
	private final Link link;

	private Component newSource;
	private String newSourceTerminal;
	private String oldSourceTerminal;
	private Component oldSource;
	private final  Component oldTarget;

	private String componentName;

	/**
	 * Instantiates a new link reconnect source command.
	 * 
	 * @param link
	 *            the link
	 */
	public LinkReconnectSourceCommand(Link link) {
		if (link == null) {
			throw new IllegalArgumentException();
		}
		this.link = link;
		this.oldSource = link.getSource();
		this.oldTarget = link.getTarget();
		setLabel("Source Reconnection");
	}

	@Override
	public boolean canExecute() {
		List<PortSpecification> portspecification;
		if (newSource != null)
			if (newSource.equals(oldTarget)) {
				return false;
			}

		// Out Port
		componentName = DynamicClassProcessor.INSTANCE.getClazzName(newSource
				.getClass());
		portspecification = XMLConfigUtil.INSTANCE.getComponent(componentName)
				.getPort().getPortSpecification();

		for (PortSpecification p : portspecification) {
			for(PortInfo portInfo:p.getPort()){
				String portName = p.getTypeOfPort().value() + portInfo.getSequenceOfPort();
				if (portName.equals(newSourceTerminal)) {
					if (portInfo.isAllowMultipleLinks()
							|| !newSource.isOutputPortEngaged(newSourceTerminal)) {

					} else{
						return false;
					}
				}
			}
			
		}
		return true;
	}

	@Override
	public void execute() {
		if (newSource != null) {
			link.detachSource();
			link.getSource().freeOutputPort(link.getSourceTerminal());
			
			link.setSource(newSource);
			link.setSourceTerminal(newSourceTerminal);
			
			oldSource.freeOutputPort(link.getSourceTerminal());
			oldSource.disconnectOutput(link);
			
			link.attachSource();
			newSource.engageOutputPort(newSourceTerminal);
			
			
		}

	}

	public void setNewSource(Component linkSource) {
		if (linkSource == null) {
			throw new IllegalArgumentException();
		}
		newSource = linkSource;
		
	}

	public void setNewSourceTerminal(String newSourceTerminal) {
		this.newSourceTerminal = newSourceTerminal;
	}
	
	public void setOldSource(Link w) {
		oldSource = w.getSource();
		oldSourceTerminal = w.getSourceTerminal();
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	
	@Override
	public void undo(){
	
		newSource=link.getSource();
		logger.debug("New source is :{}", newSource.getProperties().get("name"));
		newSourceTerminal=link.getSourceTerminal();
		newSource.disconnectOutput(link);
		newSource.freeOutputPort(link.getSourceTerminal());
		link.detachSource();
		
		link.setSource(oldSource);
		logger.debug("Old source is :{}", oldSource.getProperties().get("name"));
		link.setSourceTerminal(oldSourceTerminal);
		link.attachSource();
		
	}
}
