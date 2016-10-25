package hydrograph.ui.graph.command;

import org.eclipse.gef.commands.Command;

import hydrograph.ui.graph.model.CommentBox;


public class CommentCommand extends Command
{
	private String newName, oldName;
	private CommentBox comment;

	public CommentCommand(CommentBox comment, String s){
		this.comment = comment;
		if (s != null)
			newName = s;
		else
			newName = ""; 
		
	}
	
	@Override
	public void execute(){
		oldName = comment.getLabelContents();
		redo();
	}
	
	@Override
	public void undo(){
		comment.setLabelContents(oldName);
	}
	
	@Override
	public void redo(){
		comment.setLabelContents(newName);
	}
}
