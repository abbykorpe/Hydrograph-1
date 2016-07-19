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

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.graph.model.Component;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;


public class RemoveDebugHandler extends AbstractHandler{

	public RemoveDebugHandler() {
		setBaseEnabled(false);
		RunStopButtonCommunicator.Removewatcher.setHandler(this);
	}
	
	private DefaultGEFCanvas getComponentCanvas() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		else
			return null;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();){
			EditPart editPart = (EditPart) iterator.next();
			if(editPart instanceof ComponentEditPart){
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				comp.clearWatcherMap();
				setRemoveWatcherEnabled(false);
			} else if(editPart instanceof PortEditPart){
				((PortEditPart)editPart).getPortFigure().removeWatchColor();
				((PortEditPart)editPart).getPortFigure().setWatched(false);
				((PortEditPart)editPart).getCastedModel().setWatched(false);
			}
		}
		
		return null;
		
	}

	/**
	 * 
	 * Enable/Disable removeWatcher button in toolBar
	 * 
	 * @param enable
	 */
	public void setRemoveWatcherEnabled(boolean enable) {
		setBaseEnabled(enable);
	}
	
}
