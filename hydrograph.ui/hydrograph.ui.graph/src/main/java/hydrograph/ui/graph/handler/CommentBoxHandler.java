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
package hydrograph.ui.graph.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.PlatformUI;

import hydrograph.ui.graph.command.CommentBoxCommand;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.CommentBox;
import hydrograph.ui.graph.model.Container;
/**
 * The Class CommentBoxHandler.
 * 
 * @author Bitwise
 * 
 */
public class CommentBoxHandler extends AbstractHandler{
	private Point oldLoc ;
	private Point newLoc ;
	List <CommentBox> list = new ArrayList();
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException{
		ELTGraphicalEditor editor = (ELTGraphicalEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Container container = editor.getContainer();
		
		if(container.getChildren().isEmpty() && !(list.isEmpty())){
			list.clear();
		}
		
		for(Object obj : container.getChildren()){
			if((obj instanceof CommentBox)){
				list.add((CommentBox) obj);
			}
			else{
			list.clear();
			}
		}
		
		if(list.isEmpty()){
		oldLoc = new Point(0,0);
		}
		else{
			oldLoc = (list.get(list.size()-1)).getLocation();	
		}
		
		newLoc = new Point(oldLoc.x+6 ,oldLoc.y+6);
		CommentBox comment = new CommentBox("Label");
		comment.setLocation(newLoc);
		CommentBoxCommand command = new CommentBoxCommand(comment,"Label",container);
		command.execute();
		return null;
	}
}
