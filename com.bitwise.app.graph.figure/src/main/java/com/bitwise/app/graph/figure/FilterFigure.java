package com.bitwise.app.graph.figure;


import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import com.bitwise.app.common.component.config.PortSpecification;

public class FilterFigure extends ComponentFigure
implements HandleBounds{

	
	public FilterFigure(List<PortSpecification> portSpecification)
	{
		super(portSpecification);	
	}
	

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		Rectangle r = getBounds().getCopy();
		
		graphics.translate(r.getLocation());
		Rectangle q = new Rectangle(4, 4, r.width-8, r.height-8);
		graphics.fillRoundRectangle(q, 5, 5);

		drawLable(r, graphics);
	}

	@Override
	public Rectangle getHandleBounds() {

		return getBounds().getCopy();
	}

}
