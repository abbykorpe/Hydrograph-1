package hydrograph.ui.graph.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.CopyParticipant;
import org.slf4j.Logger;

import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.utils.GenerateUniqueJobIdUtil;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.logging.factory.LogFactory;

public class JobCopyParticipant extends CopyParticipant {

	private Logger logger = LogFactory.INSTANCE.getLogger(JobCopyParticipant.class);
	private IFile modifiedResource;
	private static HashMap<IFile,Container> copiedFilesMap;
	private static String copyToPath;
	

	@Override
	protected boolean initialize(Object element) {
		this.modifiedResource = (IFile) element;
		InputStream contents = null;
	    try {
	    	contents = modifiedResource.getContents();
	    	Container container = (Container) CanvasUtils.INSTANCE.fromXMLToObject(contents);
			copiedFilesMap.put(modifiedResource,container);
		} catch (CoreException coreException) {
			logger.error("Error while getting contents from ifile", coreException);
		}
	    finally {
	    	try {
				contents.close();
			} catch (IOException ioException) {
				logger.warn("Exception occured while closing stream");
			}
		}
		if (modifiedResource == null && StringUtils.isEmpty(modifiedResource.toString())) {
			return false;
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
		
		this.copyToPath=getArguments().getDestination().toString();
		InputStream contents = modifiedResource.getContents();
		Container container = (Container) CanvasUtils.INSTANCE.fromXMLToObject(contents);
		String uniqueJobId;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			uniqueJobId = GenerateUniqueJobIdUtil.INSTANCE.generateUniqueJobId();
			container.setUniqueJobId(uniqueJobId);
			out.write(CanvasUtils.INSTANCE.fromObjectToXML(container).getBytes());
			modifiedResource.setContents(new ByteArrayInputStream(out.toByteArray()),true, false, null);
			
		} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			logger.error("Exception occured while generating new Unique JobId",noSuchAlgorithmException);

		} catch (IOException ioException) {
			logger.error("Exception occured while converting xml into object",ioException);
		}

		finally {
			try {
				contents.close();
				out.close();
			} catch (IOException e) {
				logger.warn("Exception occured while closing stream");
			}
		}
		return null;
	}

	
	@Override
	public Change createPreChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return super.createPreChange(pm);
	}
	public static void setCopiedFilesMap(HashMap<IFile, Container> copiedFilesMap) {
		JobCopyParticipant.copiedFilesMap = copiedFilesMap;
	}

	public static String getCopyToPath() {
		return copyToPath;
	}

	public static HashMap<IFile, Container> getCopiedFilesMap() {
		return copiedFilesMap;
	}
	
}
