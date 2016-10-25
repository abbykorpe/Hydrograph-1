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

import hydrograph.ui.graph.command.CommentBoxCommand;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.CommentBox;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.PlatformUI;


public class LogicLabelEditPolicy 
	extends LogicElementEditPolicy 
{

/*public Command getCommand(Request request) {
	if (NativeDropRequest.ID.equals(request.getType()))
		return getDropTextCommand((NativeDropRequest)request);
	return super.getCommand(request);
}

protected Command getDropTextCommand(NativeDropRequest request) {
	ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	Container container=editor.getContainer();
	LogicLabelCommand command = new LogicLabelCommand((LogicLabel)getHost().getModel(), (String)request.getData(), container);
	return command;
}

public EditPart getTargetEditPart(Request request) {
	if (NativeDropRequest.ID.equals(request.getType()))
		return getHost();
	return super.getTargetEditPart(request);*/
}


