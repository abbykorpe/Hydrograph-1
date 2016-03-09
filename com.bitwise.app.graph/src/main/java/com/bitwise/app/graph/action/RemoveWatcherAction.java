package com.bitwise.app.graph.action;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

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
	protected boolean calculateEnabled() {
		 
		return false;
	}

	@Override
	protected void init() {
		super.init();
		 setText("Remove Watch Point");
		 setId("deleteWatchPoint");
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
}
