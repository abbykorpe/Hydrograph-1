package com.bitwise.app.graph.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.bitwise.app.graph.command.ComponentCreateCommand;
import com.bitwise.app.graph.command.ComponentSetConstraintCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;

/**
  * EditPolicy for the Figure used by this edit part. 
  * Creates the model components as and when requested(ex. Drag and drop from pallet)
  * Children of XYLayoutEditPolicy can be used in Figures with XYLayout. *
 */
public class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		//creates the component
		return new ComponentCreateCommand((Component) request.getNewObject(), //custom component
				(Container) getHost().getModel(),
				(Rectangle) getConstraintFor(request));
	}

	/**
	 * Creates the command which is used to move and/or resize a shape
	 */
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child instanceof ComponentEditPart && constraint instanceof Rectangle) {
			//return a command that can move and/or resize a Shape
			return new ComponentSetConstraintCommand((Component) child.getModel(),
					request, (Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child,
				constraint);
	}
	
	@Override 
	protected EditPolicy createChildEditPolicy(EditPart child) { 
		return new ComponentResizableEditPolicy();
		//return new NonResizableEditPolicy(); 
	} 
	
}
