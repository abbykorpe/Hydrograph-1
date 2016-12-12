package hydrograph.ui.graph.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.CopyParticipant;
import org.slf4j.Logger;

import hydrograph.ui.graph.Messages;
import hydrograph.ui.logging.factory.LogFactory;

public class JobCopyParticipant extends CopyParticipant {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(JobCopyParticipant.class);
	private IFile modifiedResource;
	private static List<IFile> copiedFileList;
	private static String copyToPath;
	private static List<IFile> xmlFiles;
	

	@Override
	protected boolean initialize(Object element) {
		if (element instanceof IFile) {
			this.modifiedResource = (IFile) element;
			if (modifiedResource == null && StringUtils.isEmpty(modifiedResource.toString())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
			throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		
		copyToPath=getArguments().getDestination().toString();
		IWorkspaceRoot workSpaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workSpaceRoot.getProject(copyToPath.split("/")[1]);
		IFolder jobFolder = project.getFolder(copyToPath.substring(copyToPath.indexOf('/', 2)));
		xmlFiles=new ArrayList<>();
		for (IResource iResource : jobFolder.members()) {
			if (!(iResource instanceof IFolder)) {
				IFile iFile = (IFile) iResource;
				 if (iFile.getFileExtension().equalsIgnoreCase(Messages.XML_EXT)) {
					String fileName=iFile.getName();
					IFile xmlFile= jobFolder.getFile(fileName.replace(Messages.XMLEXTENSION,Messages.JOBEXTENSION));
					if(!xmlFile.exists())
					xmlFiles.add(jobFolder.getFile(fileName));
				}
			}
		}
		copiedFileList.add(modifiedResource);
		return null;
	}

	
	@Override
	public Change createPreChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return super.createPreChange(pm);
	}


	public static String getCopyToPath() {
		return copyToPath;
	}


	
	public static List<IFile> getCopiedFileList() {
		return copiedFileList;
	}

	public static void setCopiedFileList(List<IFile> copiedFileList) {
		JobCopyParticipant.copiedFileList = copiedFileList;
	}
	public static List<IFile> getXmlFiles() {
		return xmlFiles;
	}

	
}
