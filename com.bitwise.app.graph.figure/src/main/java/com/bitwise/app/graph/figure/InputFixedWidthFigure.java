package com.bitwise.app.graph.figure;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.bitwise.app.common.component.config.PortSpecification;

public class InputFixedWidthFigure extends ComponentFigure{

	

	public InputFixedWidthFigure(List<PortSpecification> portSpecification, String canvasIconPath) {
		super(portSpecification, canvasIconPath);
 
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
 

	}

	public Rectangle getHandleBounds() {
		return getBounds().getCopy();
	}

}
