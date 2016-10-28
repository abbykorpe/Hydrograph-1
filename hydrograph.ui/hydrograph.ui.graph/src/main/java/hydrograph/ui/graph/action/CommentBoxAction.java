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

 
package hydrograph.ui.graph.action;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.graph.command.CommentBoxCommand;
import hydrograph.ui.graph.controller.ContainerEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.CommentBox;
import hydrograph.ui.graph.model.Container;
/**
 * The Class Action use to create Comment Box
 * 
 * @author Bitwise
 */
public class CommentBoxAction extends SelectionAction{
	
private PasteAction pasteAction;
private org.eclipse.draw2d.geometry.Point location;
	
	public CommentBoxAction(IWorkbenchPart part, IAction action){
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	@Override
	protected boolean calculateEnabled() {
		if(getSelectedObjects()!=null && getSelectedObjects().size()==1 && getSelectedObjects().get(0) instanceof ContainerEditPart)
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	@Override
	protected void init() {
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		super.init();
		setText("Comment Box"); 
		setId("Add");
		setHoverImageDescriptor(getImageDisDescriptor());
		setImageDescriptor(getImageDisDescriptor());
		setDisabledImageDescriptor(getImageDisDescriptor());
		setEnabled(false);
	}
		
	private Command createLabelCommand(boolean increaseCounter){
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(editor != null){
		Container container = editor.getContainer();
		
		org.eclipse.draw2d.geometry.Point point = editor.location;
		CommentBox label = new CommentBox("Label");
		label.setSize(new Dimension(300, 60));
		label.setLocation(point);
		CommentBoxCommand command = new CommentBoxCommand(label,"Label",container);
		return command;
		}
		return null;
	}
	private ImageDescriptor getImageDisDescriptor() {
		ImageDescriptor imageDescriptor = new ImageDescriptor() {

			@Override
			public ImageData getImageData() {
				return new ImageData(XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH+"/icons/comment-box-icon.png");
			}
		};
		return imageDescriptor;
	}
	
	@Override
	public void run() {
		execute(createLabelCommand(true));
	}
}
