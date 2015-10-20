package com.bitwise.app.graph.figure;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import com.bitwise.app.common.component.config.PortSpecification;

public class DedupFigure extends ComponentFigure implements HandleBounds{

	

	public DedupFigure(List<PortSpecification> portSpecification, String canvasIconPath) {
		super(portSpecification, canvasIconPath);
 
	}
	

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

	}

	@Override
	public Rectangle getHandleBounds() {

		return getBounds().getCopy();
	}

}
