package com.bitwise.app.graph.action;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Shell;
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
public class AddWatcherAction extends SelectionAction{

	private Long limitValue;
	private boolean watcherSelection;
	
	public AddWatcherAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
		 
		
	}

	@Override
	protected void init() {
		super.init();
		setText(Messages.ADD_WATCH_POINT_TEXT);
		setId(Constants.ADD_WATCH_POINT_ID);
		setEnabled(true);

	}

	private void limitValueGrid(){
		Shell parentShell = new Shell();
		LimitValueGrid customLimitValueGrid = new LimitValueGrid(parentShell);
		customLimitValueGrid.open();
		limitValue = customLimitValueGrid.getLimitValue();
		watcherSelection = customLimitValueGrid.isOkselection();
		 
	}

	private void addWatchPoint(List<Object> selectedObjects) {
	 

		for(Object obj:selectedObjects)
		{
			if(obj instanceof LinkEditPart)
			{
				Link link = (Link)((LinkEditPart)obj).getModel();
				link.getSource().addWatcherTerminal(link.getSourceTerminal(), limitValue);
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
								((PortEditPart)part).getPortFigure().changeWatchColor();
								((PortEditPart)part).getCastedModel().setWatched(true);
								((PortEditPart)part).getPortFigure().setWatched(true);
								((PortEditPart)part).getPortFigure().repaint();
							} 
						}
					}
				}
			} 
		}
		 
	}

	@Override
	protected boolean calculateEnabled() {
		return false;
	}

	@Override
	public void run() {
		super.run();
		limitValueGrid();

		List<Object> selectedObjects =getSelectedObjects();
		if(watcherSelection){
		addWatchPoint(selectedObjects);
		 
		}
	}
}
