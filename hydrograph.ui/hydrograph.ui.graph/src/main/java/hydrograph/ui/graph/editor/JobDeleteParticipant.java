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

import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.utility.ResourceChangeUtil;
import hydrograph.ui.project.structure.CustomMessages;

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
import org.eclipse.ltk.core.refactoring.participants.DeleteParticipant;
import org.eclipse.ltk.core.refactoring.resource.DeleteResourceChange;

/**
 * JobDeleteParticipant- If any of the .job, .xml and .properties file is deleted in Project explorer, then the corresponding 
 * other files will also get deleted.
 * 
 *  Author: Bitwise
 * 
 */

public class JobDeleteParticipant extends DeleteParticipant{
	private IFile modifiedResource;
	
	@Override
	protected boolean initialize(Object element) {
		this.modifiedResource = (IFile)element;
		return true;
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
		
		List<IResource> memberList = 
				new ArrayList<IResource>(modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_PARAM).members().length
										+modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS).members().length);
		ResourceChangeUtil.addMembersToList(memberList, modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_JOBS));
		ResourceChangeUtil.addMembersToList(memberList, modifiedResource.getProject().getFolder(CustomMessages.ProjectSupport_PARAM));
		
		final String fileName = ResourceChangeUtil.removeExtension(modifiedResource.getName());
		
		for(IResource resource:memberList) {
			if(Pattern.matches(fileName+".*", resource.getName())) {
				if((Messages.XML_EXT.equals(resource.getFileExtension())
						|| Messages.PROPERTIES_EXT.equals(resource.getFileExtension())
						|| Messages.JOB_EXT.equals(resource.getFileExtension()))
						&&!(modifiedResource.getName().equals(resource.getName()))) {
					
					
					DeleteResourceChange change = (DeleteResourceChange) changes.get((IFile)resource);
					if (change == null) {
						change= new DeleteResourceChange(resource.getFullPath(), true);
						changes.put((IFile)resource, change);
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

}
