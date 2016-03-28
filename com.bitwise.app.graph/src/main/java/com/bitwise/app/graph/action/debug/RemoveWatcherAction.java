/********************************************************************************
 - * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 - * Licensed under the Apache License, Version 2.0 (the "License");
 - * you may not use this file except in compliance with the License.
 - * You may obtain a copy of the License at
 - * http://www.apache.org/licenses/LICENSE-2.0
 - * Unless required by applicable law or agreed to in writing, software
 - * distributed under the License is distributed on an "AS IS" BASIS,
 - * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 - * See the License for the specific language governing permissions and
 - * limitations under the License.
 - ******************************************************************************/

package com.bitwise.app.graph.action.debug;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.LinkEditPart;
import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
/**
 * @author Bitwise
 *
 */
public class RemoveWatcherAction extends SelectionAction{

	
	public RemoveWatcherAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}


	@Override
	protected void init() {
		super.init();
		 setText(Messages.REMOVE_WATCH_POINT_TEXT);
		 setId(Constants.REMOVE_WATCH_POINT_ID);
		 setEnabled(false);
	}
	

	private void removeWatchPoint(List<Object> selectedObjects)  {
		 
		for(Object obj:selectedObjects)
		{
			if(obj instanceof LinkEditPart)
			{
				Link link = (Link)((LinkEditPart)obj).getModel();
				link.getSource().removeWatcherTerminal(link.getSourceTerminal());
				changePortColor(link.getSource(), link.getSourceTerminal());
			}	
		}
	}
	
	private void changePortColor(Component selectedComponent, String portName){

		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();)
		{
			EditPart editPart = (EditPart) iterator.next();
			if(editPart instanceof ComponentEditPart) 
			{
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				if(comp.equals(selectedComponent)){
					List<PortEditPart> portEditParts = editPart.getChildren();
					for(AbstractGraphicalEditPart part:portEditParts)
					{
						if(part instanceof PortEditPart){
							if(((PortEditPart)part).getCastedModel().getTerminal().equals(portName)){
								((PortEditPart)part).getPortFigure().removeWatchColor();
								((PortEditPart)part).getPortFigure().setWatched(false);
								((PortEditPart)part).getCastedModel().setWatched(false);
							} 
						}
					}
				}
			} 
		}
	}
	
	@Override
	public void run() {
	 
		super.run();
		List<Object> selectedObjects =getSelectedObjects();
		 
		removeWatchPoint(selectedObjects);
	}

	@Override
	protected boolean calculateEnabled() {
		List<Object> selectedObject = getSelectedObjects();
		if(!selectedObject.isEmpty()){
			for(Object obj : getSelectedObjects()){
				if(obj instanceof LinkEditPart)	{
					return true;
				}
			}
		}
		return false;
	}
}
