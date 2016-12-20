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

package hydrograph.ui.graph.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.DeleteParticipant;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.utility.ResourceChangeUtil;
import hydrograph.ui.project.structure.CustomMessages;

/**
 * JobDeleteParticipant- If any of the .job, .xml and .properties file is deleted in Project explorer, then the corresponding 
 * other files will also get deleted.
 * 
 *  Author: Bitwise
 * 
 */

public class JobDeleteParticipant extends DeleteParticipant{
	private IFile modifiedResource;
	private boolean flag;
	
	@Override
	protected boolean initialize(Object element) {
		this.modifiedResource = (IFile)element;
		IProject[] iProjects=ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject iProject:iProjects){
			if (modifiedResource.getParent()!=null && modifiedResource.getParent().getParent()!=null 
					&& StringUtils.equals(iProject.getName(), modifiedResource.getParent().getParent().getName())) {
				if (StringUtils.equalsIgnoreCase(Messages.PROPERTIES_EXT, modifiedResource.getFileExtension())) {
					return deleteCorrospondingXmlAndJobFileifUserDeletePropertyFile(iProject);
				}
				else if (StringUtils.equalsIgnoreCase(Messages.JOB_EXT, modifiedResource.getFileExtension())) {
					return deleteCorrospondingXmlAndPropertyFileifUserDeleteJobFile(iProject);
				}
				else if (StringUtils.equalsIgnoreCase(Messages.XML_EXT, modifiedResource.getFileExtension())) {
					return deleteCorrospondingJobAndPropertyFileifUserDeleteXmlFile(iProject);
				}
			}
		}
		
		return true;
	}

	private boolean deleteCorrospondingJobAndPropertyFileifUserDeleteXmlFile(IProject iProject) {
		if (StringUtils.equalsIgnoreCase(modifiedResource.getParent().getName(),CustomMessages.ProjectSupport_JOBS)) {
			IFile jobFileName = null;
			IFile propertyFileName = null;
			IFolder jobsFolder = iProject.getFolder(CustomMessages.ProjectSupport_JOBS);
			IFolder propertiesFolder = iProject.getFolder(Messages.PARAM);
			
			if (jobsFolder != null) {
				jobFileName = jobsFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.JOB_EXTENSION_FOR_IPATH).toFile().getName());
			}
			if (propertiesFolder != null) {
				propertyFileName = propertiesFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.PROPERTIES).toFile().getName());
			}
			String message=getErrorMessageIfUserDeleteXmlRelatedFiles(jobFileName,propertyFileName);
			showErrorMessage(jobFileName, propertyFileName, Messages.bind(message,modifiedResource.getName()));
		}
		else
		{
			flag=true;
		}
		return flag;
	}

	private boolean deleteCorrospondingXmlAndPropertyFileifUserDeleteJobFile(IProject iProject) {
		if (StringUtils.equalsIgnoreCase(modifiedResource.getParent().getName(),CustomMessages.ProjectSupport_JOBS)) {
			IFile xmlFileName = null;
			IFile propertyFileName = null;
			IFolder jobsFolder = iProject.getFolder(CustomMessages.ProjectSupport_JOBS);
			IFolder propertiesFolder = iProject.getFolder(Messages.PARAM);
			if (jobsFolder != null) {
				xmlFileName = jobsFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.XML_EXTENSION_FOR_IPATH).toFile().getName());
			}
			if (propertiesFolder != null) {
				propertyFileName = propertiesFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.PROPERTIES).toFile().getName());
			}
			String message=getErrorMessageIfUserDeleteJobRelatedFiles(propertyFileName,xmlFileName);
			showErrorMessage(xmlFileName, propertyFileName, Messages.bind(message,modifiedResource.getName()));
		}
		else
		{
			flag=true;
		}
		return flag;
	}

	private boolean deleteCorrospondingXmlAndJobFileifUserDeletePropertyFile(IProject iProject) {
		if (StringUtils.equalsIgnoreCase(modifiedResource.getParent().getName(),
				Messages.PARAM)) {
			IFile jobFileName = null;
			IFile xmlFileName = null;
			IFolder jobsFolder = iProject.getFolder(CustomMessages.ProjectSupport_JOBS);
			if (jobsFolder != null) {
				jobFileName = jobsFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.JOB_EXTENSION_FOR_IPATH).toFile().getName());
				xmlFileName = jobsFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.XML_EXTENSION_FOR_IPATH).toFile().getName());
			}
			String message=getErrorMessageIfUserDeletePropertyRelatedFiles(jobFileName,xmlFileName);
			showErrorMessage(jobFileName, xmlFileName, Messages.bind(message,modifiedResource.getName()));
		} 
		return flag;
	}
	
	private String getErrorMessageIfUserDeletePropertyRelatedFiles(IFile jobFileName, IFile xmlFileName) {
		String message = "";
		if (jobFileName != null && xmlFileName != null) {
			if ((jobFileName.exists()) && (!xmlFileName.exists())) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_PROPERTY_RELATED_JOB_RESOURCE;
			} else if (!jobFileName.exists() && xmlFileName.exists()) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_PROPERTY_RELATED_XML_RESOURCE;
			} else if (jobFileName.exists() && xmlFileName.exists()) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_PROPERTY_RELATED_RESOURCE;
			}
		}
		return message;
	}
	private String getErrorMessageIfUserDeleteJobRelatedFiles(IFile propertyFileName, IFile xmlFileName) {
		String message = "";
		if (propertyFileName != null && xmlFileName != null) {
			if ((propertyFileName.exists()) && (!xmlFileName.exists())) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_JOB_RELATED_PROPERTY_RESOURCE;
			} else if (!propertyFileName.exists() && xmlFileName.exists()) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_JOB_RELATED_XML_RESOURCE;
			} else if (propertyFileName.exists() && xmlFileName.exists()) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_JOB_RELATED_RESOURCE;
			}
		}
		return message;
	}
	private String getErrorMessageIfUserDeleteXmlRelatedFiles(IFile jobFileName, IFile propertyFileName) {
		String message = "";
		if (jobFileName != null && propertyFileName != null) {
			if ((jobFileName.exists()) && (!propertyFileName.exists())) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_XML_RELATED_JOB_RESOURCE;
			} else if (!jobFileName.exists() && propertyFileName.exists()) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_XML_RELATED__PROPERTY_RESOURCE;
			} else if (jobFileName.exists() && propertyFileName.exists()) {
				message = Messages.SHOW_ERROR_MESSAGE_ON_DELETING_XML_RELATED_RESOURCE;
			}
		}
		return message;
	}

	private void showErrorMessage(IFile fileName1, IFile fileName2, String errorMessage) {
		if((fileName1!=null && fileName1.exists()) || (fileName2!=null && fileName2.exists()))
		{
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
				int returnCode=openErrorMessageBox(errorMessage);
						if (returnCode == SWT.YES) {
							flag=true;
						}
				}
			});
		}
	}
	
	private int openErrorMessageBox(String message) {
		MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ERROR | SWT.YES | SWT.NO);
		messageBox.setText(Constants.ERROR);
		messageBox.setMessage(message);
		return messageBox.open();
	}

	@Override
	public String getName() {
		return "Job File Deleting Participant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		final HashMap<IFile,DeleteResourceChange> changes= new HashMap<IFile,DeleteResourceChange>();
		
		if(modifiedResource.getParent()!=null)
		{
			if (StringUtils.equalsIgnoreCase(modifiedResource.getParent().getName(), CustomMessages.ProjectSupport_JOBS)
					|| StringUtils.equalsIgnoreCase(modifiedResource.getParent().getName(),
							CustomMessages.ProjectSupport_PARAM)) {
				List<IResource> memberList = new ArrayList<IResource>(modifiedResource.getProject()
						.getFolder(CustomMessages.ProjectSupport_PARAM).members().length
						+ modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS).members().length);
				ResourceChangeUtil.addMembersToList(memberList,
						modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS));
				ResourceChangeUtil.addMembersToList(memberList,
						modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_PARAM));
				final String fileName = ResourceChangeUtil.removeExtension(modifiedResource.getName());
				for (IResource resource : memberList) {
					if (Pattern.matches(fileName + ".*", resource.getName())) {
						if (StringUtils.equalsIgnoreCase(Messages.XML_EXT, resource.getFileExtension())
								|| StringUtils.equalsIgnoreCase(Messages.PROPERTIES_EXT, resource.getFileExtension())
								|| StringUtils.equalsIgnoreCase(Messages.JOB_EXT, resource.getFileExtension()) 
								&& !(StringUtils.equalsIgnoreCase(modifiedResource.getName(), resource.getName()))) {
							getDeleteChanges(changes, resource);
						}
					}
				}
			} else {
				List<IResource> memberList = new ArrayList<IResource>(modifiedResource.getProject()
						.getFolder(modifiedResource.getParent().getName()).members().length);
				ResourceChangeUtil.addMembersToList(memberList,
						modifiedResource.getProject().getFolder(modifiedResource.getParent().getName()));
				final String fileName = ResourceChangeUtil.removeExtension(modifiedResource.getName());
				for (IResource resource : memberList) {
					if (Pattern.matches(fileName + ".*", resource.getName())) {
						if (StringUtils.equalsIgnoreCase(Messages.XML_EXT, resource.getFileExtension()) || 
							StringUtils.equalsIgnoreCase(Messages.JOB_EXT, resource.getFileExtension())
							&& !(StringUtils.equalsIgnoreCase(modifiedResource.getName(), resource.getName()))) {
							getDeleteChanges(changes, resource);
						}
					}
				}
			}
		}
	
		if (changes.isEmpty()) {
	        return null;
		}
		
		
		CompositeChange result= new CompositeChange("Delete Job Related Files"); 
	    for (Iterator<DeleteResourceChange> iter= changes.values().iterator(); iter.hasNext();) {
	        result.add((Change) iter.next());
	    }
		return result;
		
	}

	private void getDeleteChanges(final HashMap<IFile, DeleteResourceChange> changes, IResource resource) {
		DeleteResourceChange change = (DeleteResourceChange) changes.get((IFile)resource);
		if (change == null) {
			change= new DeleteResourceChange(resource.getFullPath(), true);
			changes.put((IFile)resource, change);
		}
	}

}
