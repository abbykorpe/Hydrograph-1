package com.bitwise.app.graph.action.subgraph;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.action.PasteAction;
import com.bitwise.app.graph.controller.ComponentEditPart;
/**
 * The Class SubGraphUpdateAction use to create sub graph.
 * 
 * @author Bitwise
 */
public class SubGraphUpdateAction extends SelectionAction{
	
	/** The paste action. */
	//TODO : remove pasteAction is not needed.
	PasteAction pasteAction;
	
	/** The ed component edit part. */
	ComponentEditPart componentEditPart;
	/**
	 * Instantiates a new cut action.
	 * 
	 * @param part
	 *            the part
	 * @param action
	 *            the action
	 */
	public SubGraphUpdateAction(IWorkbenchPart part, IAction action) {
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	@Override
	protected void init() {
		super.init();
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Constants.SUBGRAPH_UPDATE); 
		setId(Constants.SUBGRAPH_UPDATE);
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(false);
	}



	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		List<Object> selectedObjects =getSelectedObjects();
		if (selectedObjects != null && !selectedObjects.isEmpty()) {
			for(Object obj:selectedObjects)
			{
				if(obj instanceof ComponentEditPart)
				{
					if (Constants.SUBGRAPH_COMPONENT.equalsIgnoreCase(((ComponentEditPart) obj).getCastedModel().getComponentName())) 
						return true;
					
				}
			}
		}
		return false;
	}

	
	/* 
	 * Create sub graph
	 */
	@Override  
	public void run() { 
		
	}
   }
