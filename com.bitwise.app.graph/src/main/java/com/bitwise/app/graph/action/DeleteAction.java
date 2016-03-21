package com.bitwise.app.graph.action;


import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.bitwise.app.graph.command.ComponentDeleteCommand;
import com.bitwise.app.graph.command.LinkCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.LinkEditPart;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.Model;


/**
 * @author Bitwise The Class DeleteAction.
 */
public class DeleteAction extends SelectionAction {

	/**
	 * Instantiates a new Delete action.
	 * 
	 * @param part
	 *            the part
	 */
	public DeleteAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();

		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("Delete");
		setId(ActionFactory.DELETE.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		setEnabled(false);
	}

	private Command createDeleteCommand(List<Object> selectedObjects) {
		ComponentDeleteCommand componentDeleteCommand =new ComponentDeleteCommand();
		LinkCommand linkCommand = new LinkCommand();
		
		if (selectedObjects == null || selectedObjects.isEmpty()) {
			return null;
		}
		Model node = null;
		Iterator<Object> it = selectedObjects.iterator();
		boolean deleteComponent=false;
		boolean deleteLink=false;
		for(Object obj:selectedObjects)
		{
			if(obj instanceof ComponentEditPart)
			{
				deleteComponent=true;
				break;
			}	
		}
		if(deleteComponent)
		{	
			for(Object obj:selectedObjects)
			{
				if (obj instanceof ComponentEditPart) {
					node = (Component) ((EditPart)obj).getModel();
					componentDeleteCommand.addChildToDelete((Component)node);
				}
			}
			return componentDeleteCommand;
		}
		//------------------------------------------
		for(Object obj:selectedObjects)
		{
			if(obj instanceof LinkEditPart)
			{
				deleteLink=true;
				break;
			}	
		}
		if(deleteLink)
		{	
			for(Object obj:selectedObjects)
			{
				if (obj instanceof LinkEditPart) {
					node = (Link) ((EditPart)obj).getModel();
					linkCommand.setConnection((Link)node);
				}
			}
			return linkCommand;
		}
		else 
			return null;	
	}

	@Override
	protected boolean calculateEnabled() {
		Command cmd = createDeleteCommand(getSelectedObjects());
		if (cmd == null){
			ContributionItemManager.DELETE.setEnable(false);
			System.out.println("Disabled delete button888888888888888888");
			return false;
		}else{
			ContributionItemManager.DELETE.setEnable(true);
			System.out.println("Enabled delete button888888888888888888");
			return true;
		}
	}

	@Override
	public void run() {
		Command cmd = createDeleteCommand(getSelectedObjects());
		if (cmd != null && cmd.canExecute()) {
			execute(cmd);
			//cmd.execute();
		}
	}

}

