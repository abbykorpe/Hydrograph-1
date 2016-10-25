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
package hydrograph.ui.graph.controller;


import hydrograph.ui.graph.action.LogicLabelEditManager;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.figure.StickyNoteFigure;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import hydrograph.ui.graph.model.CommentBox;
import hydrograph.ui.graph.policy.LabelDirectEditPolicy;
import hydrograph.ui.graph.policy.LogicLabelEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.ui.PlatformUI;

public class CommentBoxEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener

{
	private AccessibleEditPart acc;
	
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((CommentBox) getModel()).addPropertyChangeListener(this);
		}
	}
	
	@Override
	public void deactivate(){
		if (!isActive())
			return;
		super.deactivate();
		((CommentBox) getModel()).removePropertyChangeListener(this);
	}
	
	protected AccessibleEditPart getAccessibleEditPart(){
		if (acc == null)
			acc = createAccessible();
		return acc;
	}
	
	protected AccessibleEditPart createAccessible(){
		return new AccessibleGraphicalEditPart(){
			public void getValue(AccessibleControlEvent e) {
				e.result = getLogicLabel().getLabelContents();
			}

			public void getName(AccessibleEvent e) {
				e.result = "Label";
			}
		};
	}

	@Override
	protected void createEditPolicies(){
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new LabelDirectEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new LogicLabelEditPolicy()); 
	}

	@Override
	protected IFigure createFigure(){
		StickyNoteFigure label = new StickyNoteFigure();
		label.setSize(268, 56);
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		org.eclipse.swt.graphics.Point point = editor.getCursorPosition();
		Point newLocation = resetLocation(new Point(point));
		label.setLocation(newLocation);
		if(label.getSize() != getLogicLabel().getSize()){
			label.setSize(getLogicLabel().getSize());
		}
		return label;
	}

	private CommentBox getLogicLabel(){
		return (CommentBox)getModel();
	}

	private void performDirectEdit(){
		new LogicLabelEditManager(this,
				new LabelCellEditorLocator((StickyNoteFigure)getFigure())).show();
	}

	private Point resetLocation(Point newLocation){
		if (newLocation.x < 146 || newLocation.y < 0){
			newLocation.x = 0;
			newLocation.y = 0;
		}
		return newLocation;
	}
	
	@Override
	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt){
		String prop = evt.getPropertyName();
		if (prop.equalsIgnoreCase("labelContents")){
			 refreshVisuals();
		  }
		else if (prop.equalsIgnoreCase("Size") || prop.equalsIgnoreCase("Location")){
			Point loc = getLogicLabel().getLocation();
			Dimension size = getLogicLabel().getSize();
			Rectangle r = new Rectangle(loc ,size);
			((GraphicalEditPart) getParent()).setLayoutConstraint(this,getFigure(),r);
			 refreshVisuals();
		}
	}
	
	@Override
	protected void refreshVisuals() {
		((StickyNoteFigure)getFigure()).setText(getLogicLabel().getLabelContents());
	}

}
