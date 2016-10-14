package hydrograph.ui.validators.utils;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.slf4j.Logger;

import hydrograph.ui.logging.factory.LogFactory;



/**
 * utility class for validators.
 * 
 * @author Bitwise
 *
 */
public class ValidatorUtility {

private static final Logger logger = LogFactory.INSTANCE.getLogger(ValidatorUtility.class);
public static final ValidatorUtility INSTANCE = new ValidatorUtility();

/**
 * This method checks if java file is present under source folder or not.
 * @param filePath java file path. 
 * @return true if file is present otherwise false.
 */
public boolean isClassFilePresentOnBuildPath(String filePath)
{
	if(filePath.contains("."))
	{	
	String packageName=filePath.substring(0, filePath.lastIndexOf('.'));
	String JavaFileName=filePath.substring(filePath.lastIndexOf('.')+1);
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
       IProject project = null;    
	    if(page.getActiveEditor()!=null)
	    {	
	    	
		IFileEditorInput input = (IFileEditorInput) page.getActiveEditor().getEditorInput();
		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(activeProject.getName());
	    }
	    else
	    {
	    	 ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    
	   		 ISelection selection = selectionService.getSelection();    

	   		        if(selection instanceof IStructuredSelection) {    
	   		            Object element = ((IStructuredSelection)selection).getFirstElement();    
	   		           
	   		                project= ((IResource)element).getProject();    
	     }
	    }
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragmentRoot[] ipackageFragmentRootList=null;
		try {
			ipackageFragmentRootList = javaProject.getPackageFragmentRoots();
		} catch (JavaModelException e) {
			logger.error("Unable to get jars which are on build path of project " ,e );
		}
		for(IPackageFragmentRoot tempIpackageFragmentRoot:ipackageFragmentRootList)
		{
			if(!tempIpackageFragmentRoot.getElementName().contains("-sources"))
			{		
			IPackageFragment packageFragment=tempIpackageFragmentRoot.getPackageFragment(packageName);
			if(!packageFragment.exists())
			continue;
			else
			{
				if(packageFragment.getCompilationUnit(JavaFileName+".java").exists()
						||packageFragment.getClassFile(JavaFileName+".class").exists()
						)
				return true;
			}	
			}
		} 
	   }
		return false;
}
}
