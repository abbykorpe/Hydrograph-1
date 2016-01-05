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

import com.bitwise.app.graph.command.ComponentCopyCommand;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.model.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class CopyAction.
 */
public class CopyAction extends SelectionAction {
	PasteAction pasteAction;
	
	/**
	 * Instantiates a new copy action.
	 * 
	 * @param part
	 *            the part
	 * @param action
	 *            the action
	 */
	public CopyAction(IWorkbenchPart part, IAction action) {
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("Copy");
		setId(ActionFactory.COPY.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setEnabled(false);
	}

	private Command createCopyCommand(List<Object> selectedObjects) {
		ComponentCopyCommand copyCommand =new ComponentCopyCommand();
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
			if (copyCommand.isCopyableNode(node)){
				copyCommand.addElement(node);
			}
		}
		return copyCommand;
	}

	@Override
	protected boolean calculateEnabled() {
		Command cmd = createCopyCommand(getSelectedObjects());
		if (cmd == null)
		return false;
		return true;
	}

	@Override
	public void run() {
		Command cmd = createCopyCommand(getSelectedObjects());
		if (cmd != null && cmd.canExecute()) {
			cmd.execute();
			pasteAction.setPasteCounter(1);
			pasteAction.update();
		}
	}

}
