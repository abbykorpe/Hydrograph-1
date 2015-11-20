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
		port.setLocation(portPoint);
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
		}
		else if(type.equalsIgnoreCase("out")){
			xLocation=width-28;
		}
		yLocation=portOffset*sequence - 4 + margin;
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
		componentFigure.translateToAbsolute(newPortLoc);
		//componentFigure.translateToParent(portPoint);
		//componentFigure.translateFromParent(portPoint);
		getFigure().setLocation(newPortLoc);
		/*super.refreshVisuals();*/
	}
	public PortFigure getPortFigure() {
		return (PortFigure) getFigure();
	}

}
