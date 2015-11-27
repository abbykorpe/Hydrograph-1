package com.bitwise.app.menus.handlers;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.figure.PortFigure;
/**
 *Creates Show Hide Port Labels Handler 
 * @author Bitwise
 *
 */
public class ShowHidePortLabelsHandler extends AbstractHandler implements IHandler,IElementUpdater {
	private UIElement element;
	/**
	 * show and hide port labels of components
	 * @param event
	 * @return Object
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if(editor instanceof ELTGraphicalEditor)
		{
			GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
			for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); 
					ite.hasNext();)
			{
				EditPart editPart = (EditPart) ite.next();
				if(editPart instanceof PortEditPart) 
				{
					PortFigure portFigure=((PortEditPart)editPart).getPortFigure();
					portFigure.setToggleValue(!portFigure.getToggleValue());
					element.setChecked(portFigure.getToggleValue());
					portFigure.repaint();
				}
			}
		}
		return null;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		this.element=element;
	}

}
