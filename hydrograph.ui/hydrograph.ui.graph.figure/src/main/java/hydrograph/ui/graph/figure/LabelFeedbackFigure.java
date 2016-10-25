/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package hydrograph.ui.graph.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class LabelFeedbackFigure
	extends BentCornerFigure
{

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics graphics) {
	Rectangle rect = getBounds().getCopy();
	
	graphics.setXORMode(true);
	graphics.setForegroundColor(ColorConstants.white);
	graphics.setBackgroundColor(new Color(null, 31, 31, 31));
	
	graphics.translate(getLocation());
	
	PointList outline = new PointList();
	
	outline.addPoint(0, 0);
	outline.addPoint(rect.width - getCornerSize(), 0);
	outline.addPoint(rect.width - 1, getCornerSize());
	outline.addPoint(rect.width - 1, rect.height - 1);
	outline.addPoint(0, rect.height - 1);
	
	graphics.fillPolygon(outline); 
	
	// draw the inner outline
	PointList innerLine = new PointList();

	innerLine.addPoint(rect.width - getCornerSize() - 1, 0);
	innerLine.addPoint(rect.width - getCornerSize() - 1, getCornerSize());
	innerLine.addPoint(rect.width - 1, getCornerSize());
	innerLine.addPoint(rect.width - getCornerSize() - 1, 0);
	innerLine.addPoint(0, 0);
	innerLine.addPoint(0, rect.height - 1);
	innerLine.addPoint(rect.width - 1, rect.height - 1);
	innerLine.addPoint(rect.width - 1, getCornerSize());

	graphics.drawPolygon(innerLine);
	
	graphics.drawLine(rect.width - getCornerSize() - 1, 0, rect.width - 1, getCornerSize());
	
	graphics.translate(getLocation().getNegated());
}
}
