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

package com.bitwise.app.parametergrid.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructures.parametergrid.FilePath;
import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.parametergrid.dialog.ParameterFileDialog;

/**
 * 
 * Handler to open parameter grid
 * 
 * @author Bitwise
 * 
 */
public class ParameterGridOpenHandler extends AbstractHandler {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ParameterGridOpenHandler.class);

	/**
	 * 
	 * Returns active editor as {@link DefaultGEFCanvas}
	 * 
	 * @return {@link DefaultGEFCanvas}
	 */
	private DefaultGEFCanvas getComponentCanvas() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		else
			return null;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (getComponentCanvas().getParameterFile() == null) {
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

			messageBox.setText("Error");
			messageBox.setMessage("Could not open parameter grid. \nPlease save the job file.");
			messageBox.open();

			logger.debug("Parameter file does not exist. Need to save job file");
			return null;
		}

		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart();
		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput()
				.getAdapter(IFile.class);
		IProject project = file.getProject();
		String activeProjectLocation = project.getLocation().toOSString();
		System.out.println("path: " + project.getLocation().toOSString());

		FileInputStream fin;
		List<FilePath> filepathList = new LinkedList<>();
		filepathList.add(new FilePath(getComponentCanvas().getJobName().replace("job", "properties"),
				getComponentCanvas().getParameterFile(), true, true));
		try {
			fin = new FileInputStream(activeProjectLocation + "\\project.metadata");
			ObjectInputStream ois = new ObjectInputStream(fin);
			filepathList.addAll((LinkedList<FilePath>) ois.readObject());
		} catch (Exception exception) {
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_WARNING | SWT.OK);

			messageBox.setText("Warning");
			messageBox.setMessage("Unable to read project.metadata file, this might be a new project");
			messageBox.open();

			logger.debug("Parameter file does not exist. Need to save job file", exception);
		}

		ParameterFileDialog testDialog = new ParameterFileDialog(new Shell(), activeProjectLocation);
		testDialog.setParameterFiles(filepathList);
		testDialog.open();

		
		return null;
	}
}
