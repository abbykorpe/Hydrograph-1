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

package hydrograph.ui.expression.editor.launcher;

import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.Messages;
import hydrograph.ui.expression.editor.PathConstant;
import hydrograph.ui.expression.editor.buttons.ValidateExpressionToolButton;
import hydrograph.ui.expression.editor.dialogs.ExpressionEditorDialog;
import hydrograph.ui.expression.editor.jar.util.BuildExpressionEditorDataSturcture;
import hydrograph.ui.expression.editor.message.CustomMessageBox;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;

public class LaunchExpressionEditor {
	private Logger LOGGER = LogFactory.INSTANCE.getLogger(LaunchExpressionEditor.class);
	private IClasspathEntry[] oldClasspathEntry=null;
	
	public void launchExpressionEditor(ExpressionEditorData expressionEditorData){
		LOGGER.debug("Initiating creation of Expression Editor");
		if (intialize()) {
			BuildExpressionEditorDataSturcture.INSTANCE
					.createClassRepo(Constants.JAR_FILE_NAME, Constants.PACKAGE_NAME);
			ExpressionEditorDialog expressionEditorDialog = new ExpressionEditorDialog(Display.getCurrent()
					.getActiveShell(), expressionEditorData);
			int returnCode = expressionEditorDialog.open();
			if (returnCode == 0) {
				saveProperty(expressionEditorData, expressionEditorDialog.getExpressionText());
			}
		}
		try {
			cleanUp();
		} catch (JavaModelException e) {
			LOGGER.error("Exception occurred while reverting project build path.",e);
		}
	}
	
	private boolean intialize(){
		try{
		IPath tempSrcFolder=createTemprarySourceFolder();
		if(tempSrcFolder!=null){
			addClassPathEntry(tempSrcFolder);
		}}
		catch (Exception e) {
			LOGGER.error("Exception occurred while initializing expression editor",e);
			new CustomMessageBox(SWT.ERROR, Messages.CANNOT_OPEN_EDITOR, "ERROR");
			return false;
		}
		return true;
	}

	private void addClassPathEntry(IPath tempSrcFolder) throws JavaModelException {
		IJavaProject javaProject=JavaCore.create(BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject());
		oldClasspathEntry=javaProject.getRawClasspath();
		IClasspathEntry[] newClasspathEntry=new IClasspathEntry[oldClasspathEntry.length+1];
		for(int index=0;index<newClasspathEntry.length-1;index++){
			if(oldClasspathEntry[index].getPath().equals(tempSrcFolder))
				return ;
			newClasspathEntry[index]=javaProject.getRawClasspath()[index];
		}
		newClasspathEntry[newClasspathEntry.length-1]=JavaCore.newSourceEntry(tempSrcFolder);
		javaProject.setRawClasspath(newClasspathEntry, new NullProgressMonitor());
	}

	private void saveProperty(ExpressionEditorData expressionEditorData, String expressionText) {
		expressionEditorData.setExpression(ValidateExpressionToolButton.getExpressionText(expressionText));
		expressionEditorData.getfieldsUsedInExpression().clear();
		expressionEditorData.getfieldsUsedInExpression().addAll(new ArrayList<>(expressionEditorData.getSelectedInputFieldsForExpression().keySet()));
	}
	
	private IPath createTemprarySourceFolder(){
		IPath tempSrcFolder=new Path(PathConstant.TEMP_BUILD_PATH_SETTINGS_FOLDER);
		IFolder  folder=BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject().getFolder(tempSrcFolder);
		try {
		if(!folder.exists()){
				folder.create(true, true,null);
			}
		} catch (CoreException e) {
			LOGGER.error("CoreException occurred while creating temporary source folder",e);
			return null;
		}
		LOGGER.debug("Created temporary build path at "+PathConstant.TEMP_BUILD_PATH_SETTINGS_FOLDER);
		return folder.getFullPath();
	}
	
	private void cleanUp() throws JavaModelException{
//		JavaCore.create(BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject()).setRawClasspath(oldClasspathEntry, new NullProgressMonitor());
		System.gc();
		removeTemprarySourceFolder();
	}
	
	private void removeTemprarySourceFolder(){
		IPath tempSrcFolder=new Path(PathConstant.TEMP_BUILD_PATH_SETTINGS_FOLDER);
		IFolder  folder=BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject().getFolder(tempSrcFolder);
		try {
		if(folder.exists()){
				folder.delete(true, new NullProgressMonitor());
			}
		} catch (CoreException e) {
			LOGGER.error("CoreException occurred while removing temporary source folder",e);
		}
		LOGGER.debug("Removed temporary build path at "+PathConstant.TEMP_BUILD_PATH_SETTINGS_FOLDER);
	}
}
