package hydrograph.ui.graph.command;


import hydrograph.ui.graph.model.CommentBox;
import hydrograph.ui.graph.model.Container;

import org.eclipse.gef.commands.Command;


public class CommentBoxCommand extends Command
{
	private  Container parent;
	private String newName, oldName;
	private CommentBox comment;

	public CommentBoxCommand(CommentBox comment, String s, Container parent){
		this.comment = comment;
		if (s != null)
			newName = s;
		else
			newName = ""; 
		
		this.parent = parent;
	}
	
	@Override
	public void execute(){
		oldName = comment.getLabelContents();
		redo();
	}
	
	@Override
	public void undo(){
		comment.setLabelContents(oldName);
		parent.removeChild(comment);
	}
	
	@Override
	public void redo(){
		parent.addChild(comment);
		comment.setLabelContents(newName);
	}
}
