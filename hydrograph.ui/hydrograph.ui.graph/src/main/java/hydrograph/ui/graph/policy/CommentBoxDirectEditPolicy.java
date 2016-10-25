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
package hydrograph.ui.graph.policy;



import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import hydrograph.ui.graph.command.CommentCommand;
import hydrograph.ui.graph.controller.CommentBoxEditPart;
import hydrograph.ui.graph.figure.StickyNoteFigure;
import hydrograph.ui.graph.model.CommentBox;


public class LabelDirectEditPolicy 
	extends DirectEditPolicy {

/**
 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
 */
protected Command getDirectEditCommand(DirectEditRequest edit) {
	String labelText = (String)edit.getCellEditor().getValue();
	CommentBoxEditPart label = (CommentBoxEditPart)getHost();
	CommentCommand command = new CommentCommand((CommentBox) (label.getModel()),labelText);
	return command;
}

/**
 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
 */
protected void showCurrentEditValue(DirectEditRequest request) {
	String value = (String)request.getCellEditor().getValue();
	((StickyNoteFigure)getHostFigure()).setText(value);
	//hack to prevent async layout from placing the cell editor twice.
	getHostFigure().getUpdateManager().performUpdate();
	
}

}
