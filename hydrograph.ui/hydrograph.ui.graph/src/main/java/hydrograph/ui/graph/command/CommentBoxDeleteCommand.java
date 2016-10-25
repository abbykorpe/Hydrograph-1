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
package hydrograph.ui.graph.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.CommentBox;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Model;

public class CommentBoxDeleteCommand extends Command{
	
	private List<CommentBox> selectedComponents;
	private Set<CommentBox> deleteComponents;
	private final Container parent;
	private boolean wasRemoved;
	
	
	public CommentBoxDeleteCommand() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();	
		this.parent=(((ELTGraphicalEditor) page.getActiveEditor()).getContainer());
		selectedComponents = new ArrayList<CommentBox>();
		deleteComponents = new LinkedHashSet<>();
	}

	public void addComponentToDelete(CommentBox node){
		selectedComponents.add(node);
	}
	
	public boolean hasComponentToDelete(){
		return !selectedComponents.isEmpty();
	}
	
	@Override
	public boolean canExecute() {
		if (selectedComponents == null || selectedComponents.isEmpty())
			return false;

		Iterator<CommentBox> it = selectedComponents.iterator();
		while (it.hasNext()) {
			CommentBox node = (CommentBox) it.next();
			if (isDeletableNode(node)) {
				deleteComponents.add(node);
			}
		}
		return true;
	}
	
	private boolean isDeletableNode(Model node) {
		if (node instanceof CommentBox)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean canUndo() {
		return wasRemoved;
	}

	@Override
	public void execute() {
		redo();
	}
	
	@Override
	public void redo() {
		Iterator<CommentBox> it = deleteComponents.iterator();
		while(it.hasNext()){
			CommentBox deleteComp=(CommentBox)it.next();
			wasRemoved = parent.removeChild(deleteComp);
		}

	}
	@Override
	public void undo() {
		Iterator<CommentBox> it = deleteComponents.iterator();
		while(it.hasNext()){
			CommentBox restoreComp=(CommentBox)it.next();
			parent.addChild(restoreComp);
		}

	}
}
