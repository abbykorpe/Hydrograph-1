/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
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
