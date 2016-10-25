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
package hydrograph.ui.graph.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import hydrograph.ui.graph.model.CommentBox;

public class SetConstraintCommand
	extends org.eclipse.gef.commands.Command
{

	private Point newPos;
	private Dimension newSize;
	private Point oldPos;
	private Dimension oldSize;
	private CommentBox label;

	public void execute() {
		oldSize = label.getSize();
		oldPos  = label.getLocation();
		redo();
	}
	
	public String getLabel() {
		if (oldSize.equals(newSize))
			return "size";
		return "location";
	}
	
	public void redo() {
		label.setSize(newSize);
		label.setLocation(newPos);
	}
	
	public void setLocation(Rectangle r) {
		setLocation(r.getLocation());
		setSize(r.getSize());
	}
	
	public void setLocation(Point p) {
		newPos = p;
	}
	
	public void setPart(CommentBox part) {
		this.label = part;
	}
	
	public void setSize(Dimension p) {
		newSize = p;
	}
	
	public void undo() {
		label.setSize(oldSize);
		label.setLocation(oldPos);
	}
	
	}
