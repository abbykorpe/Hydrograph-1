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
