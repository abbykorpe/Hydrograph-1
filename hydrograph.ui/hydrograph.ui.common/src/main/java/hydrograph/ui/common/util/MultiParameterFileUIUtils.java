package hydrograph.ui.common.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class MultiParameterFileUIUtils {
		
	public static String getActiveProjectLocation() {
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart();
		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput()
				.getAdapter(IFile.class);
		IProject project = file.getProject();
		String activeProjectLocation = project.getLocation().toOSString();
		return activeProjectLocation;
	}
}
