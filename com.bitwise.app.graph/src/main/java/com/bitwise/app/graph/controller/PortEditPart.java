package com.bitwise.app.graph.controller;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.ConnectionDragCreationTool;
import org.eclipse.swt.graphics.Color;

import com.bitwise.app.graph.figure.ComponentFigure;
import com.bitwise.app.graph.figure.ELTColorConstants;
import com.bitwise.app.graph.figure.ELTFigureConstants;
import com.bitwise.app.graph.figure.PortFigure;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Port;
/**
 * The Class PortEditPart.
 * 
 * @author Bitwise
 */
public class PortEditPart extends AbstractGraphicalEditPart {
	public Port getCastedModel() {
		return (Port) getModel();
	}

	@Override
	protected IFigure createFigure() {
		
		ComponentFigure componentFigure = ((ComponentEditPart) getParent()).getComponentFigure();
		Component component = ((ComponentEditPart) getParent()).getCastedModel();
		PortFigure port = null;
		
		Color borderColor = ELTColorConstants.COMPONENT_BORDER;
		Point portPoint = null;
		
		int height = component.getSize().height-componentFigure.getComponentLabelMargin();
		int width = component.getSize().width;
		
		
		int margin = componentFigure.getComponentLabelMargin();
		port =  new PortFigure(borderColor, getCastedModel().getPortType(), getCastedModel().getSequence(), getCastedModel().getNumberOfPortsOfThisType(),getCastedModel().getNameOfPort(),getCastedModel().getLabelOfPort());	
		
		String toolTipText = getCastedModel().getNameOfPort();
		port.getToolTipFigure().setMessage(toolTipText);
		
		portPoint = getPortLocation(getCastedModel().getNumberOfPortsOfThisType(), getCastedModel().getPortType(),
				getCastedModel().getSequence(), height, width, margin);
		
		Point  tmpPoint = new Point(componentFigure.getLocation().x+portPoint.x , componentFigure.getLocation().y+portPoint.y);
		port.setLocation(tmpPoint);
		componentFigure.setAnchors(port.getAnchor());
		return port;
	}

	

	private Point getPortLocation(int totalPortsOfThisType, String type, int sequence, int height, int width, int margin){

		Point p = null ;
		int portOffsetFactor = totalPortsOfThisType+1;
		int portHeightOffset=height/portOffsetFactor;
		int portWidthOffset=width/portOffsetFactor;
		int xLocation=0, yLocation=0;

		if(type.equalsIgnoreCase("in")){
			xLocation=0;
			yLocation=portHeightOffset*(sequence+1) - 4 + margin;
		}else if(type.equalsIgnoreCase("out")){
			xLocation=width-27;
			yLocation=portHeightOffset*(sequence+1) - 4 + margin;
		}else if (type.equalsIgnoreCase("unused")){
			if(totalPortsOfThisType == 1){
				xLocation = 43;
				
			}else if(totalPortsOfThisType > 1){
				xLocation = portWidthOffset*(sequence+1) - 6;
			}
			yLocation=height  + margin - 8 - 8;
			
		}
		p=new Point(xLocation, yLocation);
		return p;
	}
	
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public DragTracker getDragTracker(Request request) {
		getViewer().select(this);
		return new ConnectionDragCreationTool();
	}
	
	public void adjustPortFigure(Point componentLocation) { 
		
		ComponentFigure componentFigure = ((ComponentEditPart) getParent()).getComponentFigure();
		Component component = ((ComponentEditPart) getParent()).getCastedModel();
		
		int height = component.getSize().height-componentFigure.getComponentLabelMargin();
		int width = component.getSize().width;
		
		int margin = componentFigure.getComponentLabelMargin(); 
		Point portPoint = getPortLocation(getCastedModel().getNumberOfPortsOfThisType(), getCastedModel().getPortType(),
				getCastedModel().getSequence(), height, width, margin);
		
		Point newPortLoc = new Point(portPoint.x+componentLocation.x, portPoint.y+componentLocation.y);
				
		getFigure().setLocation(newPortLoc);
	}
	public PortFigure getPortFigure() {
		return (PortFigure) getFigure();
	}
	

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		((PortFigure)getFigure()).setLabelOfPort(getCastedModel().getLabelOfPort());
		getFigure().repaint();
	}

}
