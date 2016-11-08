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


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

import hydrograph.ui.graph.action.CommentBoxLabelEditManager;
import hydrograph.ui.graph.figure.CommentBoxFigure;
import hydrograph.ui.graph.model.CommentBox;
import hydrograph.ui.graph.policy.CommentBoxDirectEditPolicy;
/**
 * The Class CommentBoxEditPart.
 * 
 * @author Bitwise
 * 
 */
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
	
	@Override
	protected AccessibleEditPart getAccessibleEditPart(){
		if (acc == null)
			acc = createAccessible();
		return acc;
	}
	
	protected AccessibleEditPart createAccessible(){
		return new AccessibleGraphicalEditPart(){
			public void getValue(AccessibleControlEvent e) {
				e.result = getLabel().getLabelContents();
			}

			public void getName(AccessibleEvent e) {
				e.result = "Label";
			}
		};
	}

	@Override
	protected void createEditPolicies(){
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new CommentBoxDirectEditPolicy());
		//installEditPolicy(EditPolicy.COMPONENT_ROLE,new LabelEditPolicy()); 
	}

	@Override
	protected IFigure createFigure(){
		CommentBoxFigure label = new CommentBoxFigure();
		Point loc = getLabel().getLocation();
		Dimension size = getLabel().getSize();
		Rectangle r = new Rectangle(loc ,size);
		label.setBounds(r);
		if(label.getSize() != getLabel().getSize()){
			label.setSize(getLabel().getSize());
		}
		return label;
	}

	private CommentBox getLabel(){
		return (CommentBox)getModel();
	}

	public IFigure getCommentBoxFigure(){
		return (CommentBoxFigure)getFigure();
	}
	
	private void performDirectEdit(){
		new CommentBoxLabelEditManager(this,
				new CommentBoxCellEditorLocator((CommentBoxFigure)getFigure())).show();
	}

	/*private Point resetLocation(int x , int y){
		if ((x <= 135 && y <= -53) || (x <= 154 && y <= -30 )){
			x = 0;
			y = 0;
		}
		return new Point(x,y);
	}*/
	
	@Override
	public void performRequest(Request request){
		//if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt){
		String prop = evt.getPropertyName();
		if (StringUtils.equalsIgnoreCase(prop, "labelContents")){
			 refreshVisuals();
		  }
		else if (StringUtils.equalsIgnoreCase(prop, "Size") || StringUtils.equalsIgnoreCase(prop, "Location")){
			Point loc = getLabel().getLocation();
			Dimension size = getLabel().getSize();
			Rectangle r = new Rectangle(loc ,size);
			((GraphicalEditPart) getParent()).setLayoutConstraint(this,getFigure(),r);
			 refreshVisuals();
		}
	}
	
	@Override
	protected void refreshVisuals() {
		((CommentBoxFigure)getFigure()).setText(getLabel().getLabelContents());
	}

}
