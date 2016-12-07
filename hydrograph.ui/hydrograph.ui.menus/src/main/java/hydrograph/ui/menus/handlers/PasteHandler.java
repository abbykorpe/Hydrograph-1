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

 
package hydrograph.ui.menus.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.ui.refactoring.reorg.PasteAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.statushandlers.StatusManager;
import org.slf4j.Logger;

import hydrograph.ui.engine.util.ConverterUtil;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.editor.JobCopyParticipant;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.menus.Activator;
import hydrograph.ui.menus.messages.Messages;



/**
 * The Class PasteHandler.
 * <p>
 * Handler to Paste component on canvas and Project Explorer 
 * 
 * @author Bitwise
 */
public class PasteHandler extends AbstractHandler implements IHandler {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(PasteHandler.class);
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<IFile> jobFiles = new ArrayList<>();
		List<IFile> xmlFiles = new ArrayList<>();
		List<IFile> pastedFileList = new ArrayList<>();
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if(part instanceof CommonNavigator){
			PasteAction action = new PasteAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite());
			action.run();
			writeContainerContentsToJobFile();
			IWorkspaceRoot workSpaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = workSpaceRoot.getProject(JobCopyParticipant.getCopyToPath().split("/")[1]);
			IFolder jobFolder = project.getFolder(
					JobCopyParticipant.getCopyToPath().substring(JobCopyParticipant.getCopyToPath().indexOf('/', 2)));
			IFolder paramFolder = project.getFolder(Messages.paramFolderName);
			try {
				createJobFileAndXmlFileList(jobFolder, jobFiles, xmlFiles);
				createPastedFileList(jobFiles, xmlFiles, pastedFileList);
				createXmlFilesForPastedJobFiles(pastedFileList);
				List<String> copiedPropertiesList = getCopiedPropertiesList();
				createPropertiesFilesForPastedFiles(paramFolder, pastedFileList, copiedPropertiesList);

			} catch (CoreException  coreException) {
				logger.warn("Error while copy paste jobFiles",coreException.getMessage() );
			}
			
		}
		
		else if(part instanceof ELTGraphicalEditor){
			IEditorPart editor = HandlerUtil.getActiveEditor(event);
			((ELTGraphicalEditor)editor).pasteSelection();
		}
		
		return null;
	}
	private void createPropertiesFilesForPastedFiles(IFolder paramFolder, List<IFile> pastedFileList,
			List<String> copiedPropertiesList) throws CoreException {
		for (int i = 0; i < copiedPropertiesList.size(); i++) {
			InputStream inputStream = paramFolder.getFile(copiedPropertiesList.get(i)).getContents();
			try {
				IFile file = paramFolder
						.getFile(pastedFileList.get(i).getName().replace(Messages.jobExtension, Messages.properties));
				file.create(inputStream, true, null);
			} catch (CoreException coreException) {
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Problem occured during creating property file.",
						coreException);
				StatusManager.getManager().handle(status, StatusManager.BLOCK);
				logger.error("Error while creating properties files for pasted files ::{}", coreException.getMessage());
				throw new RuntimeException("Property file already exists", coreException);
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException ioException) {
					logger.warn("Exception occured while closing stream");
				}
			}
		}
	}
	

	private List<String> getCopiedPropertiesList() {
		List<String> copiedPropertiesList = new ArrayList<>();
		HashMap<IFile, Container> copiedFilesMap = JobCopyParticipant.getCopiedFilesMap();
		if (!copiedFilesMap.isEmpty()) {
			Iterator iterator = copiedFilesMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry pair = (Map.Entry) iterator.next();
				IFile iFile = (IFile) pair.getKey();
				copiedPropertiesList.add((iFile.getName().replace(Messages.jobExtension,Messages.properties)));
			}
		}
		return copiedPropertiesList;
	}

	private void createXmlFilesForPastedJobFiles(List<IFile> pastedFileList) {
		for (IFile file : pastedFileList) {
			InputStream inputStream = null;
			try {
				inputStream = file.getContents();
				Container container = (Container) CanvasUtils.INSTANCE.fromXMLToObject(inputStream);
				IPath path = file.getFullPath().removeFileExtension().addFileExtension(Messages.xml);
				IFile xmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				ConverterUtil.INSTANCE.convertToXML(container, true, xmlFile, null);

			} catch (CoreException | InstantiationException | IllegalAccessException | InvocationTargetException
					| NoSuchMethodException exception) {

			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException ioException) {
					logger.warn("Exception occured while closing stream");
				}
			}
		}
	}

	private void createPastedFileList(List<IFile> jobFiles, List<IFile> xmlFiles, List<IFile> pastedFileList) {
		for (int i = 0; i < jobFiles.size(); i++) {
			int isXmlPresent=0;
			for (int j = i; j < xmlFiles.size(); j++) {
				if(jobFiles.get(i).getFullPath().removeFileExtension().lastSegment().equalsIgnoreCase(xmlFiles.get(j).getFullPath().removeFileExtension().lastSegment()))
				{
					isXmlPresent=1;
					break;
				}
			}
			if(isXmlPresent!=1)
			pastedFileList.add(jobFiles.get(i));
		}
	}

	private void createJobFileAndXmlFileList(IFolder jobFolder, List<IFile> jobFiles, List<IFile> xmlFiles)
			throws CoreException {
		for (IResource iResource : jobFolder.members()) {
			if (!(iResource instanceof IFolder)) {
				IFile iFile = (IFile) iResource;
				if (iFile.getFileExtension().equalsIgnoreCase(Messages.job)) {
					jobFiles.add(iFile);
				} else if (iFile.getFileExtension().equalsIgnoreCase(Messages.xml)) {
					xmlFiles.add(iFile);
				}
			}
		}
	}

	private void writeContainerContentsToJobFile() {
		HashMap<IFile, Container> copiedFiles = JobCopyParticipant.getCopiedFilesMap();
		if (!copiedFiles.isEmpty()) {
			Iterator iterator = copiedFiles.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry pair = (Map.Entry) iterator.next();
				Container container = (Container) pair.getValue();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					out.write(CanvasUtils.INSTANCE.fromObjectToXML(container).getBytes());
					IFile iFile = (IFile) pair.getKey();
					iFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, null);
				} catch (IOException | CoreException exception) {
					logger.error("Error while writing container contents to job files", exception);
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException ioException) {
							logger.warn("Exception occured while closing stream");
						}
					}
				}
			}
		}

	}

	

}
