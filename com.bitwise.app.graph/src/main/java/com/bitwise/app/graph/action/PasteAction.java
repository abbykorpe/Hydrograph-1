package com.bitwise.app.graph.action;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.bitwise.app.graph.command.ComponentPasteCommand;

// TODO: Auto-generated Javadoc
/**
 * The Class PasteAction.
 */
public class PasteAction extends SelectionAction {
	private int pasteCounter;
	
	/**
	 * Instantiates a new paste action.
	 * 
	 * @param part
	 *            the part
	 */
	public PasteAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	protected void init() {
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("Paste");
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(false);
	}

	private Command createPasteCommand() {
		ComponentPasteCommand command = new ComponentPasteCommand();
		command.setPasteCounter(pasteCounter++);
		return command;
	}

	@Override
	protected boolean calculateEnabled() {
		Command command = createPasteCommand();
		boolean status = command != null && command.canExecute();
		ContributionItemManager.PASTE.setEnable(status);	
		return status;
	}

	@Override
	public void run() {
	   execute(createPasteCommand());
	}
   
	public void setPasteCounter(int pasteCounter) {
		this.pasteCounter = pasteCounter;
	}
}
