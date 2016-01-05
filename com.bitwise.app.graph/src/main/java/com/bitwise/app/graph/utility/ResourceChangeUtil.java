package com.bitwise.app.graph.utility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.project.structure.CustomMessages;

public class ResourceChangeUtil {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ResourceChangeUtil.class);
	
	private ResourceChangeUtil() {
		
	}
	
	public static void addMembersToList(List<IResource> memberList, IFolder folder) {
		try {
			for(IResource resource: folder.members()) {
				if(resource instanceof IFile) {
					memberList.add(resource);
				}
				else {
					addMembersToList(memberList, (IFolder)resource);
				}
			}
		} catch (CoreException e) {
			logger.debug("Unable to add members to list " , e);
		}
	}
	
	public static boolean isGeneratedFile(String fileName,IProject project) {
		List<IResource> memberList= new ArrayList<>(); 
		String jobFileName = removeExtension(fileName)+".job";
		
		addMembersToList(memberList, (IFolder)project.getFolder(CustomMessages.ProjectSupport_JOBS));
		for(IResource file:memberList) {
			if(jobFileName.equals(file.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static String removeExtension(String fileName) {
		return fileName.substring(0,fileName.lastIndexOf("."));
	}
}
