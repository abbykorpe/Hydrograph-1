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
