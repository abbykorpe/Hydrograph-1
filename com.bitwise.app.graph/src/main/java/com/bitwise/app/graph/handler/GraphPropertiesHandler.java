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

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.utility.SubGraphUtility;
import com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty.RuntimePropertyDialog;

/**
 * Handles the execution of graph level runtime properties. 
 * 
 * @author Bitwise
 *
 */
public class GraphPropertiesHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		RuntimePropertyDialog dialog = new RuntimePropertyDialog(Display.getCurrent().getActiveShell(), null,
				getCurrentGraphName()+" - Graph Properties");
		dialog.setRuntimeProperties(getCurrentGarphInstance().getGraphRuntimeProperties());
		if(dialog.open()==0 && dialog.isOkPressedAfterUpdate()) 
			SubGraphUtility.getCurrentEditor().setDirty(true);
		return null;
	}

	private  String getCurrentGraphName()
	{
		String graphName= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput().getName();
		graphName = StringUtils.remove(graphName, Constants.JOB_EXTENSION);
		graphName = StringUtils.abbreviate(graphName, 20);
		return graphName;
	}
	
	private Container getCurrentGarphInstance()
	{
		return SubGraphUtility.getCurrentEditor().getContainer();
	}
	
	
}
