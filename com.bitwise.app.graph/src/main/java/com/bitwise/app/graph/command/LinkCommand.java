package com.bitwise.app.graph.command;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.commands.Command;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.processor.DynamicClassProcessor;

public class LinkCommand extends Command{
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(LinkReconnectCommand.class);
	
	/** The connection instance. */
	private Link connection;
	/** The desired line style for the connection (dashed or solid). */
	private int lineStyle;


	private Component source, target;
	protected String sourceTerminal, targetTerminal;

	protected Component oldSource;
	protected String oldSourceTerminal;
	protected Component oldTarget;
	protected String oldTargetTerminal;
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

	public LinkCommand(Component source, int lineStyle) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel("connection creation");
		this.source = source;
		this.lineStyle = lineStyle;
	}

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
				String portName=p.getTypeOfPort()+p.getSequenceOfPort();
				if(portName.equals(sourceTerminal)){
					if(p.isAllowMultipleLinks() || 
							!source.hasOutputPort(sourceTerminal)){
						
					}else{
						
						return false;
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
				String portName=p.getTypeOfPort()+p.getSequenceOfPort();
				if(portName.equals(targetTerminal)){
					if(p.isAllowMultipleLinks() ||
							!target.hasInputPort(targetTerminal)){
						
					}else{
						
						return false;
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
	public void execute() {

		if(source!=null){

			connection.setSource(source);
			connection.setSourceTerminal(sourceTerminal);
			connection.setLineStyle(Graphics.LINE_SOLID);
			connection.attachSource();

			source.addOutputPort(sourceTerminal);
			
		}
		if(target!=null){

			connection.setTarget(target);
			connection.setTargetTerminal(targetTerminal);
			connection.setLineStyle(Graphics.LINE_SOLID);
			connection.attachTarget();

			target.addInputPort(targetTerminal);
			
		}
		
		//Delete the link
		if (source == null && target == null) {
			connection.detachSource();
			connection.detachTarget();
			connection.getSource().removeOutputPort(connection.getSourceTerminal());
			connection.getTarget().removeInputPort(connection.getTargetTerminal());
			connection.setTarget(null);
			connection.setSource(null);
		}
	}

	public void setTarget(Component target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}

	public void setSource(Component newSource) {
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
		oldSource = w.getSource();
		oldTarget = w.getTarget();
		oldSourceTerminal = w.getSourceTerminal();
		oldTargetTerminal = w.getTargetTerminal();
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

		connection.setSource(oldSource);
		logger.debug("Link comd Old Name :{}",oldSource.getProperties().get("name"));
		connection.setTarget(oldTarget);
		connection.setSourceTerminal(oldSourceTerminal);
		connection.setTargetTerminal(oldTargetTerminal);

		connection.attachSource();
		connection.attachTarget();

	}
}