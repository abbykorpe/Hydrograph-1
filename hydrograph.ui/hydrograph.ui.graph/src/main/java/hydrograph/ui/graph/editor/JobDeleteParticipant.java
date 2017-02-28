/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

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
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.utility.ResourceChangeUtil;
import hydrograph.ui.logging.factory.LogFactory;
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
	private IFile jobFileName = null;
	private IFile xmlFileName = null;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(JobDeleteParticipant.class);
	
	@Override
	protected boolean initialize(Object element) {
		this.modifiedResource = (IFile)element;
		IProject[] iProjects=ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject iProject:iProjects){
			if(StringUtils.equals(iProject.getName(),modifiedResource.getFullPath().segment(0))) {
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
		if (StringUtils.equalsIgnoreCase(modifiedResource.getProjectRelativePath().segment(0),
				CustomMessages.ProjectSupport_JOBS)) {
			IFile propertyFileName = null;
			IFolder jobsFolder = iProject.getFolder(CustomMessages.ProjectSupport_JOBS);
			IFolder propertiesFolder = iProject.getFolder(Messages.PARAM);

			if (jobsFolder != null) {
				setJobFile(jobsFolder);
			}
			if (propertiesFolder != null) {
				propertyFileName = propertiesFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.PROPERTIES).toFile().getName());
			}
			String message = getErrorMessageIfUserDeleteXmlRelatedFiles(jobFileName, propertyFileName);
			showErrorMessage(jobFileName, propertyFileName, Messages.bind(message, modifiedResource.getName()));
		} else {
			flag = true;
		}
		return flag;
	}

	private boolean deleteCorrospondingXmlAndPropertyFileifUserDeleteJobFile(IProject iProject) {
		if (StringUtils.equalsIgnoreCase(modifiedResource.getProjectRelativePath().segment(0),
				CustomMessages.ProjectSupport_JOBS)) {
			IFile propertyFileName = null;
			IFolder jobsFolder = iProject.getFolder(CustomMessages.ProjectSupport_JOBS);
			IFolder propertiesFolder = iProject.getFolder(Messages.PARAM);
			if (jobsFolder != null) {
				setXmlFile(jobsFolder);
			}
			if (propertiesFolder != null) {
				propertyFileName = propertiesFolder.getFile(modifiedResource.getFullPath().removeFileExtension()
						.addFileExtension(Constants.PROPERTIES).toFile().getName());
			}
			String message = getErrorMessageIfUserDeleteJobRelatedFiles(propertyFileName, xmlFileName);
			showErrorMessage(xmlFileName, propertyFileName, Messages.bind(message, modifiedResource.getName()));
		} else {
			flag = true;
		}
		return flag;
	}

	private boolean deleteCorrospondingXmlAndJobFileifUserDeletePropertyFile(IProject iProject) {
		if (StringUtils.equalsIgnoreCase(modifiedResource.getParent().getName(), Messages.PARAM)) {
			IFolder jobsFolder = iProject.getFolder(CustomMessages.ProjectSupport_JOBS);
			if (jobsFolder != null) {
				setJobFileAndXmlFile(jobsFolder);
			}
			String message = getErrorMessageIfUserDeletePropertyRelatedFiles(jobFileName, xmlFileName);
			showErrorMessage(jobFileName, xmlFileName, Messages.bind(message, modifiedResource.getName()));
		}
		return flag;
	}
	
	private void setJobFileAndXmlFile(IFolder jobsFolder) {
		try {
			IResource[] members = jobsFolder.members();
			for (IResource jobFolderMember : members) {
				String file = jobFolderMember.getFullPath().lastSegment();
				if (file.equals(
						modifiedResource.getName().replace(Constants.PROPERTIES_EXTENSION, Constants.JOB_EXTENSION))) {
					jobFileName = jobsFolder.getFile(modifiedResource.getName().replace(Constants.PROPERTIES_EXTENSION,
							Constants.JOB_EXTENSION));
				} else if (file.equals(
						modifiedResource.getName().replace(Constants.PROPERTIES_EXTENSION, Constants.XML_EXTENSION))) {
					xmlFileName = jobsFolder.getFile(modifiedResource.getName().replace(Constants.PROPERTIES_EXTENSION,
							Constants.XML_EXTENSION));
				} else if (jobFolderMember instanceof IFolder) {
					setJobFileAndXmlFile((IFolder) jobFolderMember);
				}
			}
		} catch (CoreException coreException) {
			logger.error("Error while setting job file and xml file for dependent deletion", coreException);
		}
	}

	private void setXmlFile(IFolder jobsFolder) {
		try {
			IResource[] members = jobsFolder.members();
			for (IResource jobFolderMember : members) {
				String file = jobFolderMember.getFullPath().lastSegment();
				if (file.equals(modifiedResource.getName().replace(Constants.JOB_EXTENSION, Constants.XML_EXTENSION))) {
					xmlFileName = jobsFolder.getFile(
							modifiedResource.getName().replace(Constants.JOB_EXTENSION, Constants.XML_EXTENSION));
				} else if (jobFolderMember instanceof IFolder) {
					setXmlFile((IFolder) jobFolderMember);
				}
			}
		} catch (CoreException coreException) {
			logger.error("Error while setting xml file for dependent deletion", coreException);
		}
	}
	
	private void setJobFile(IFolder jobsFolder) {
		try {
			IResource[] members = jobsFolder.members();
			for (IResource jobFolderMember : members) {
				String file = jobFolderMember.getFullPath().lastSegment();
				if (file.equals(modifiedResource.getName().replace(Constants.XML_EXTENSION, Constants.JOB_EXTENSION))) {
					jobFileName = jobsFolder.getFile(
							modifiedResource.getName().replace(Constants.XML_EXTENSION, Constants.JOB_EXTENSION));
				} else if (jobFolderMember instanceof IFolder) {
					setJobFile((IFolder) jobFolderMember);
				}
			}
		} catch (CoreException coreException) {
			logger.error("Error while setting job file for dependent deletion", coreException);
		}
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
		if ((fileName1 != null && fileName1.exists()) || (fileName2 != null && fileName2.exists())) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					int returnCode = openErrorMessageBox(errorMessage);
					if (returnCode == SWT.YES) {
						flag = true;
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
			List<IResource> iResources=new ArrayList<>();
			if (StringUtils.equalsIgnoreCase(modifiedResource.getProjectRelativePath().segment(0), CustomMessages.ProjectSupport_JOBS)
					|| StringUtils.equalsIgnoreCase(modifiedResource.getParent().getName(),
							CustomMessages.ProjectSupport_PARAM)) {
				List<IResource> memberList = new ArrayList<IResource>(modifiedResource.getProject()
						.getFolder(CustomMessages.ProjectSupport_PARAM).members().length
						+ getJobsFolderMembers(iResources,modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS).members()).size());
				ResourceChangeUtil.addMembersToList(memberList,
						modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS));
				ResourceChangeUtil.addMembersToList(memberList,
						modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_PARAM));
				final String fileName = ResourceChangeUtil.removeExtension(modifiedResource.getName());
				for (IResource resource : memberList) {
					if (Pattern.matches(fileName + Constants.EXTENSION, resource.getName())) {
						if ((StringUtils.equalsIgnoreCase(Messages.XML_EXT, resource.getFileExtension())
								|| StringUtils.equalsIgnoreCase(Messages.PROPERTIES_EXT, resource.getFileExtension())
								|| StringUtils.equalsIgnoreCase(Messages.JOB_EXT, resource.getFileExtension()))
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
	
	private List<IResource> getJobsFolderMembers(List<IResource> iResourcesList, IResource[] iResources)
			throws CoreException {
		for (IResource iResource : iResources) {
			if (iResource instanceof IFolder) {
				getJobsFolderMembers(iResourcesList, ((IFolder) iResource).members());
			} else {
				iResourcesList.add(iResource);
			}
		}
		return iResourcesList;
	}

	private void getDeleteChanges(final HashMap<IFile, DeleteResourceChange> changes, IResource resource) {
		DeleteResourceChange change = (DeleteResourceChange) changes.get((IFile)resource);
		if (change == null) {
			change= new DeleteResourceChange(resource.getFullPath(), true,true);
			changes.put((IFile)resource, change);
		}
	}

}
