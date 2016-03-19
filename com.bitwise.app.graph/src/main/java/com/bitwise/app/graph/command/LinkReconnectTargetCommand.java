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

import com.bitwise.app.common.component.config.PortInfo;
import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class LinkReconnectTargetCommand.
 */
public class LinkReconnectTargetCommand extends Command{
	
	Link link;
	private Component oldTarget;
	private Component newTarget;
	private String oldTargetTerminal;
	private String newTargetTerminal;
	private String componentName;
	private final Component oldSource;
	
	/**
	 * Instantiates a new link reconnect target command.
	 * 
	 * @param link
	 *            the link
	 */
	public LinkReconnectTargetCommand(Link link){
		if (link == null) {
			throw new IllegalArgumentException();
		}
		this.link=link;
		this.oldSource=link.getSource();
		this.oldTarget = link.getTarget();
		setLabel("Target Reconnection");
	}
	
	
	@Override
	public boolean canExecute(){
		List<PortSpecification> portspecification;
		if(newTarget != null){
			if(newTarget.equals(oldSource)){
				return false;
			}
			componentName = DynamicClassProcessor.INSTANCE
					.getClazzName(newTarget.getClass());

			portspecification=XMLConfigUtil.INSTANCE.getComponent(componentName).getPort().getPortSpecification();
			for (PortSpecification p:portspecification)
			{
				for(PortInfo portInfo:p.getPort()){
					String portName=p.getTypeOfPort().value()+portInfo.getSequenceOfPort();
					if(portName.equals(newTargetTerminal)){
						if(portInfo.isAllowMultipleLinks() ||
								!newTarget.isInputPortEngaged(newTargetTerminal)){
							
						}else{
							return false;
						}
					}
				}
			}
		}

		return true;
	}
	
	@Override
	public void execute(){
		if(newTarget != null){
			link.detachTarget();
			link.getTarget().freeInputPort(link.getTargetTerminal());
			
			link.setTarget(newTarget);
			link.setTargetTerminal(newTargetTerminal);
						
			oldTarget.freeInputPort(link.getTargetTerminal());
			oldTarget.disconnectInput(link);

			link.attachTarget();
			newTarget.engageInputPort(newTargetTerminal);
			
		}
	}
	
	public void setNewTarget(Component linkTarget) {
		if (linkTarget == null) {
			throw new IllegalArgumentException();
		}
		newTarget = linkTarget;
	}
	
	public void setNewTargetTerminal(String newTargetTerminal){
		this.newTargetTerminal=newTargetTerminal;
	}
	
	public void setOldTarget(Link w){
		oldTarget=w.getTarget();
		oldTargetTerminal=w.getTargetTerminal();
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public void undo(){
		
		newTarget=link.getTarget();
		newTargetTerminal=link.getTargetTerminal();
		newTarget.disconnectInput(link);
		newTarget.freeInputPort(link.getTargetTerminal());
		link.detachTarget();
		
		link.setTarget(oldTarget);
		link.setTargetTerminal(oldTargetTerminal);
		link.attachTarget();
		
	}

}
