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

 
package com.bitwise.app.graph.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.editor.ELTGraphicalEditorInput;

/**
 *	Handler class to create the graphical editor.
 */
public class NewGraphHandler extends AbstractHandler {
	private static int graphCounter = 1;
	private Logger logger = LogFactory.INSTANCE.getLogger(NewGraphHandler.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		logger.debug("Job " +graphCounter+ " Created");
		try {
			ELTGraphicalEditorInput input = new ELTGraphicalEditorInput("Job_"+ graphCounter++);
			page.openEditor(input, ELTGraphicalEditor.ID, false);
			//For selecting the created editor so it will trigger the event to activate and load the Palette
			IWorkbench workbench = PlatformUI.getWorkbench();
		    IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
		    if (activeWindow != null) {
		        final IWorkbenchPage activePage = activeWindow.getActivePage();
		        if (activePage != null) {
		            activePage.activate(activePage.findEditor(input));
		        }
		    }
		} catch (PartInitException e) {
          logger.error("Failed to open editor", e);			
		}
		return null;
	}
}
