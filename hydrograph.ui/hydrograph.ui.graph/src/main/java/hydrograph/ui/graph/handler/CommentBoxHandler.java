package hydrograph.ui.graph.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import hydrograph.ui.graph.command.CommentBoxCommand;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.CommentBox;
import hydrograph.ui.graph.model.Container;

public class CommentBoxHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Container container = editor.getContainer();
		CommentBox comment = new CommentBox("Label");
		CommentBoxCommand command = new CommentBoxCommand(comment,"Label",container);
		command.execute();
		return null;
	}

}
