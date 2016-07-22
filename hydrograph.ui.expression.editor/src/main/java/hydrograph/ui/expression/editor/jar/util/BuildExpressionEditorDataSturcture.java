package hydrograph.ui.expression.editor.jar.util;

import hydrograph.ui.expression.editor.message.CustomMessageBox;
import hydrograph.ui.expression.editor.repo.ClassRepo;
import hydrograph.ui.logging.factory.LogFactory;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

public class BuildExpressionEditorDataSturcture {

	public static final BuildExpressionEditorDataSturcture INSTANCE = new BuildExpressionEditorDataSturcture();
	private Logger LOGGER = LogFactory.INSTANCE.getLogger(BuildExpressionEditorDataSturcture.class);

	public void createClassRepo(String jarFileName,String Package) {
		ClassRepo.INSTANCE.flusRepo();
		try {
			IPackageFragmentRoot iPackageFragmentRoot = getIPackageFragment(jarFileName);
			if (iPackageFragmentRoot != null) {
				IPackageFragment fragment = iPackageFragmentRoot.getPackageFragment(Package);
				if (fragment != null) {
					for (IClassFile element : fragment.getClassFiles()) {
						ClassRepo.INSTANCE.addClass(element, false);
					}
				} else {
					new CustomMessageBox(SWT.ERROR, Package + " Package not found in jar "
							+ iPackageFragmentRoot.getElementName(), "ERROR").open();
				}
			}
		} catch (JavaModelException e) {
			LOGGER.error("Error occured while loading class from jar", e);
		}
	}

	public void loadUserDefinedClassesInClassRepo(String jarFileName,String Package) {
		IPackageFragmentRoot iPackageFragmentRoot = getIPackageFragment(jarFileName);
		try {
			if (iPackageFragmentRoot != null) {
				IPackageFragment fragment = iPackageFragmentRoot.getPackageFragment(Package);
				if (fragment != null) {
					for (IClassFile element : fragment.getClassFiles()) {
						ClassRepo.INSTANCE.addClass(element, true);
					}
				} else {
					LOGGER.warn("Package not found in jar " + iPackageFragmentRoot.getElementName(), "ERROR");
				}
			}
		} catch (JavaModelException e) {
			LOGGER.error("Error occured while loading class from jar", e);
		}
	}
	
	private IPackageFragmentRoot getIPackageFragment(String jarFileName) {
		IProject iProject = getCurrentProject();
		try {
			IPackageFragmentRoot[] fragmentRoot = JavaCore.create(iProject).getAllPackageFragmentRoots();
			for (IPackageFragmentRoot iPackageFragmentRoot : fragmentRoot) {
				if (StringUtils.contains(iPackageFragmentRoot.getElementName(),jarFileName))
					return iPackageFragmentRoot;
			}
		} catch (JavaModelException javaModelException) {
			LOGGER.error("Error occurred while loading engines-transform jar",javaModelException);
		}
		new CustomMessageBox(SWT.ERROR, "Error occurred while loading "+jarFileName+" file", "ERROR").open();
		return null;
	}

	public IProject getCurrentProject() {
		String path=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput().getToolTipText();
		IPath iPath=new Path(path);
		IProject[] iProject = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject project:iProject){
			if(StringUtils.equals(iPath.segment(0), project.getName())){
				return project;
			}
		}
		return null;
	}

}
