package com.bitwise.app.graph.action;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.bitwise.app.common.util.ContributionItemManager;
import com.bitwise.app.graph.command.ComponentCopyCommand;
import com.bitwise.app.graph.command.ComponentCutCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.model.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class CutAction.
 * 
 * @author Bitwise
 */
public class CutAction extends SelectionAction{
	PasteAction pasteAction;
	
	/**
	 * Instantiates a new cut action.
	 * 
	 * @param part
	 *            the part
	 * @param action
	 *            the action
	 */
	public CutAction(IWorkbenchPart part, IAction action) {
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("Cut");
		setId(ActionFactory.CUT.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(false);
	}

	private Command createCutCommand(List<Object> selectedObjects) {
		ComponentCutCommand cutCommand =new ComponentCutCommand();
		if (selectedObjects == null || selectedObjects.isEmpty()) {
			return null;
		}
		Component node = null;
		Iterator<Object> it = selectedObjects.iterator();
		while (it.hasNext()) {
			Object ep = it.next();
			if (ep instanceof ComponentEditPart) {
				node = (Component) ((EditPart)ep).getModel();
			}
			if (!cutCommand.isCutNode(node)){
				return null;
			}
				
			cutCommand.addElement(node);
			}
		return cutCommand;
	}

	@Override
	protected boolean calculateEnabled() {
		Command cmd = createCutCommand(getSelectedObjects());
		if (cmd == null){
			ContributionItemManager.CUT.setEnable(false);			
			return false;
		}else{
			ContributionItemManager.CUT.setEnable(true);			
			return true;
		}

	}

	@Override
	public void run() {
		   execute(createCutCommand(getSelectedObjects()));
			pasteAction.setPasteCounter(1);
			pasteAction.update();
		}
   }
