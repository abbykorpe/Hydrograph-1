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
		PortFigure port = null;
		
		Color borderColor = ELTColorConstants.componentBorder;
		Point portPoint = null;
		int height = componentFigure.getHeight();
		int margin = componentFigure.getComponentLabelMargin();
		port =  new PortFigure(borderColor, getCastedModel().getPortType(), getCastedModel().getSequence(), getCastedModel().getNumberOfPortsOfThisType(),getCastedModel().getNameOfPort(),getCastedModel().getLabelOfPort());	
		
		//Calling getNameOfPort() method form Port Model
		String toolTipText = getCastedModel().getNameOfPort();
		port.getToolTipFigure().setMessage(toolTipText);
		
		portPoint = getPortLocation(getCastedModel().getNumberOfPortsOfThisType(), getCastedModel().getPortType(),
				getCastedModel().getSequence(), height, margin);
		
		Point  tmpPoint = new Point(componentFigure.getLocation().x+portPoint.x , componentFigure.getLocation().y+portPoint.y);
		componentFigure.translateToAbsolute(tmpPoint);
		port.setLocation(tmpPoint);
		componentFigure.setAnchors(port.getAnchor());
		return port;
	}

	

	private Point getPortLocation(int totalPortsOfThisType, String type, int sequence, int height, int margin) {

		Point p = null ;
		int width = 100;
		int portOffsetFactor = totalPortsOfThisType+1;
		int portOffset=height/portOffsetFactor;
		int xLocation=0, yLocation=0;

		if(type.equalsIgnoreCase("in")){
			xLocation=0;
			yLocation=portOffset*sequence - 4 + margin;
		}
		else if(type.equalsIgnoreCase("out")){
			xLocation=width-27;
			yLocation=portOffset*sequence - 4 + margin;
		}else if (type.equalsIgnoreCase("unused")){
			xLocation = 28;
			yLocation=height + ELTFigureConstants.componentOneLineLabelMargin- 4 - 3;
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
		int height = componentFigure.getHeight();
		int margin = componentFigure.getComponentLabelMargin(); 
		Point portPoint = getPortLocation(getCastedModel().getNumberOfPortsOfThisType(), getCastedModel().getPortType(),
				getCastedModel().getSequence(), height, margin);
		
		Point newPortLoc = new Point(portPoint.x+componentLocation.x, portPoint.y+componentLocation.y);
		
		System.out.println("newPortLoc: "+newPortLoc.x +", "+newPortLoc.y);
		componentFigure.translateToAbsolute(newPortLoc);
		
		getFigure().setLocation(newPortLoc);
	}
	public PortFigure getPortFigure() {
		return (PortFigure) getFigure();
	}

}
