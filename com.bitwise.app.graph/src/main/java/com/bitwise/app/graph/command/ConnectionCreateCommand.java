package com.bitwise.app.graph.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.commands.Command;

import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.custom.Filter;
import com.bitwise.app.graph.model.custom.Gather;
import com.bitwise.app.graph.model.custom.Input;
import com.bitwise.app.graph.model.custom.Output;
import com.bitwise.app.graph.model.custom.Replicate;
import com.bitwise.app.graph.processor.DynamicClassProcessor;

public class ConnectionCreateCommand extends Command{
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
	
	public ConnectionCreateCommand() {
		super("connection");
	}
	
	public ConnectionCreateCommand(Component source, int lineStyle) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel("connection creation");
		this.source = source;
		this.lineStyle = lineStyle;
	}

	public boolean canExecute() {
		
		// disallow source -> source connections
		if (source.equals(target)) {
			return false;
		}
		// return false, if the source -> target connection exists already
		for (Iterator<Link> iter = source.getSourceConnections().iterator(); iter
				.hasNext();) {
			Link conn = (Link) iter.next();
			
			if (conn.getTarget().equals(target)) {
				return false;
			}
		}
		
		//Out port restrictions


		String componentName = DynamicClassProcessor.INSTANCE
				.getClazzName(source.getClass());
		
		List<PortSpecification> portspecification=XMLConfigUtil.INSTANCE.getComponent(componentName).getPort().getPortSpecification();

		for (PortSpecification p:portspecification)
		{
			String portName=p.getTypeOfPort()+p.getSequenceOfPort();
			if(portName.equals(sourceTerminal)){
				if(p.isAllowMultipleLinks() || 
						//!(source.getOutputPortStatus(sourceTerminal)!=null && source.getOutputPortStatus(sourceTerminal).equals("connected"))){
					!source.hasOutputPort(sourceTerminal)){
					
				}else
					return false;
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
						//!(target.getInputPortStatus(targetTerminal)!=null && target.getInputPortStatus(targetTerminal).equals("connected")) ){
						!target.hasInputPort(targetTerminal)){
					
				}else
					return false;
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
			
			//source.setOutputPortStatus(sourceTerminal, "connected");
			source.addOutputPort(sourceTerminal);
			
			
				
		}
		if(target!=null){
			
			connection.setTarget(target);
			connection.setTargetTerminal(targetTerminal);
			connection.setLineStyle(Graphics.LINE_SOLID);
			connection.attachTarget();
			
			//target.setInputPortStatus(targetTerminal, "connected");
			target.addInputPort(targetTerminal);
			
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
		target = connection.getTarget();
		sourceTerminal = connection.getSourceTerminal();
		targetTerminal = connection.getTargetTerminal();

		connection.detachSource();
		connection.detachTarget();

		connection.setSource(oldSource);
		connection.setTarget(oldTarget);
		connection.setSourceTerminal(oldSourceTerminal);
		connection.setTargetTerminal(oldTargetTerminal);

		connection.attachSource();
		connection.attachTarget();

	}
}