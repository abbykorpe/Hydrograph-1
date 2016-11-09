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

import org.eclipse.gef.commands.Command;

import hydrograph.ui.graph.model.CommentBox;

/**
 * implementation of CommentBoxCommand
 *
 *@author BITWISE
 */
public class CommentCommand extends Command
{
	private String newName, oldName;
	private CommentBox comment;
	/**
	 * Instantiates a new CommentBoxCommand.
	 * 
	 * @param comment
	 *            the comment
	 * @param label
	 *            the label
	 */
	public CommentCommand(CommentBox comment, String label){
		this.comment = comment;
		if (label != null)
			newName = label;
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
