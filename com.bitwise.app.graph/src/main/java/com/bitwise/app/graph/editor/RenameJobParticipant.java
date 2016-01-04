package com.bitwise.app.graph.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceChange;

import com.bitwise.app.graph.utility.ResourceChangeUtil;
import com.bitwise.app.project.structure.CustomMessages;

public class RenameJobParticipant extends RenameParticipant {
	private IFile modifiedResource;
	
	@Override
	protected boolean initialize(Object element) {
		this.modifiedResource = (IFile)element;
		return true;
	}

	@Override
	public String getName() {
		return "Job File Renaming Participant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		String newName = getArguments().getNewName();
		String newExt = newName.substring(newName.lastIndexOf(".")+1);
		
		if("job".equals(modifiedResource.getFileExtension())) {
			if(!("job".equals(newExt))) {
				return RefactoringStatus.createFatalErrorStatus("Changing extension of job file is not allowed");
			}
		}
		else if("job".equals(newExt)) {
			return RefactoringStatus.createFatalErrorStatus("Changing extension to .job not allowed." +
					"Please create a new job file.");
		}
		else if(CustomMessages.ProjectSupport_JOBS.equals(modifiedResource.getFullPath().segment(1))
				&& !newExt.matches("job|xml")) {
			return RefactoringStatus.createFatalErrorStatus("Only .job and .xml files can be stored in this folder" );
		}
		else if(modifiedResource.getFileExtension().matches("xml|properties")
				|| (newExt.matches("xml|properties"))){
			if(ResourceChangeUtil.isGeneratedFile(modifiedResource.getName()
											,modifiedResource.getProject())) {
				return RefactoringStatus.createFatalErrorStatus(
						".xml or .properties file cannot be renamed. " +
						"Rename the .job file to rename the xml and properties file");
				
			}
			else if(ResourceChangeUtil.isGeneratedFile(newName,modifiedResource.getProject())) {
				return RefactoringStatus.createFatalErrorStatus("Generated file with same name exists.Please choose a different name");
			}
			else if(("properties".equals(modifiedResource.getFileExtension()) 
					|| "properties".equals(newExt))
					&& !modifiedResource.getFullPath().segment(1).equals(CustomMessages.ProjectSupport_PARAM)) {
				return RefactoringStatus.createFatalErrorStatus("properties file can only be saved in param folder.");
			}
		}
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		final HashMap<IFile,RenameResourceChange> changes= new HashMap<IFile,RenameResourceChange>();
		final String newName = ResourceChangeUtil.removeExtension(getArguments().getNewName());
	
		List<IResource> memberList = 
				new ArrayList<IResource>(modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_PARAM).members().length
										+modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS).members().length);
		ResourceChangeUtil.addMembersToList(memberList, modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS));
		ResourceChangeUtil.addMembersToList(memberList, modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_PARAM));
		final String fileName = ResourceChangeUtil.removeExtension(modifiedResource.getName());
		for(IResource resource:memberList) {
			if(Pattern.matches(fileName+".*", resource.getName())) {
				if(("xml".equals(resource.getFileExtension())
						|| "properties".equals(resource.getFileExtension())
						|| "job".equals(resource.getFileExtension()))
						&&!(modifiedResource.getName().equals(resource.getName()))) {
					RenameResourceChange change= (RenameResourceChange) changes.get((IFile)resource);
					if (change == null) {
						change= new RenameResourceChange(resource.getFullPath(), newName+"."+resource.getFileExtension());
						changes.put((IFile)resource, change);
					}
				}
			}
		}
				
		if (changes.isEmpty()) {
	        return null;
		}
		
		CompositeChange result= new CompositeChange("Rename Job Related Files"); 
	    for (Iterator<RenameResourceChange> iter= changes.values().iterator(); iter.hasNext();) {
	        result.add((Change) iter.next());
	    }
		return result;
	}

}