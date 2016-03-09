package com.bitwise.app.graph.handler;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.LinkEditPart;
import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;

public class RemoveDebugHandler extends AbstractHandler{

	
	public RemoveDebugHandler() {
		setBaseEnabled(false);
		 
		
	}
	
	
	/*private void removeWatchPoint(List<Object> selectedObjects)  {
		 
		for(Object obj:selectedObjects)
		{
			if(obj instanceof LinkEditPart)
			{
				Link link = (Link)((LinkEditPart)obj).getModel();
				link.getSource().clearWatcherMap();
				changePortColor(link.getSource(), link.getSourceTerminal());
			}	
		}
		
		
	}*/
	
	/*private void changePortColor(Component selectedComponent, String portName){

		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();)
		{
			EditPart editPart = (EditPart) iterator.next();
			if(editPart instanceof ComponentEditPart) 
			{
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				comp.clearWatcherMap();
				 
				
			} 
		}
	}*/
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();)
		{
			EditPart editPart = (EditPart) iterator.next();
			if(editPart instanceof ComponentEditPart) 
			{
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				 	comp.clearWatcherMap();
					List<PortEditPart> portEditParts = editPart.getChildren();
					for(AbstractGraphicalEditPart part:portEditParts)
					{
						if(part instanceof PortEditPart){
							((PortEditPart)part).getPortFigure().deSelectPort();
					}
				}
			} 
		}
		return null;
	}

	public void setRemoveDebugEnable(boolean enable){
		setBaseEnabled(enable);
	}
}
