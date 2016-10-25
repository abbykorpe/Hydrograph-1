/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
