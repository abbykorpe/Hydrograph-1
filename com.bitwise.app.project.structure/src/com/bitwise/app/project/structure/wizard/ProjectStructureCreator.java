package com.bitwise.app.project.structure.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.events.BuildCommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.project.structure.Activator;
import com.bitwise.app.project.structure.CustomMessages;
import com.bitwise.app.project.structure.natures.ProjectNature;

/**
 * Class to create the Custom Project Structure.
 *
 */
public class ProjectStructureCreator {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ProjectStructureCreator.class);

	public static final String [] paths = {CustomMessages.ProjectSupport_Settings,CustomMessages.ProjectSupport_JOBS,CustomMessages.ProjectSupport_SRC,  
		CustomMessages.ProjectSupport_SCRIPTS,CustomMessages.ProjectSupport_PARAM,CustomMessages.ProjectSupport_SCHEMA,CustomMessages.ProjectSupport_LIB};

	public static final ProjectStructureCreator INSTANCE = new ProjectStructureCreator();

	private ProjectStructureCreator(){
	}

	/**
	 * Creates the custom project structure at the specified location and name
	 * The Project will have below natures:<br>
	 * <b>Java</b> and Custom nature as per <b>ETL</b> project
	 * @param projectName Project name
	 * @param location Where should the project be saved
	 * @return
	 */
	public IProject createProject(String projectName, URI location){

		if(projectName == null || projectName.trim().length() <= 0)
			throw new InvalidProjectNameException();
		IProject project = null;
		try {
			project = createBaseProject(projectName, location);
			if(project!=null)
			{
				addNature(project);
				addToProjectStructure(project, paths);
				IJavaProject javaProject = JavaCore.create(project);

				IFolder binFolder = project.getFolder(CustomMessages.ProjectSupport_BIN);
				javaProject.setOutputLocation(binFolder.getFullPath(), null);

				List<IClasspathEntry> entries = addJavaLibrariesInClassPath();

				IFolder libFolder = project.getFolder(CustomMessages.ProjectSupport_LIB);

				//add libs to project class path 
				String installLocation = Platform.getInstallLocation().getURL().getPath();

				copyExternalLibAndAddToClassPath(installLocation + CustomMessages.ProjectSupport_LIB, libFolder, entries);

				copyBuildFile(installLocation + CustomMessages.ProjectSupport_CONFIG_FOLDER + "/" + CustomMessages.ProjectSupport_GRADLE+"/build", 
						project);
				
				copyBuildFile(installLocation + CustomMessages.ProjectSupport_CONFIG_FOLDER + "/" + CustomMessages.ProjectSupport_GRADLE+"/properties", 
						project);

				javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);

				//set source folder entry in classpath
				javaProject.setRawClasspath(setSourceFolderInClassPath(project, javaProject), null);
			}
		} catch (CoreException e) {
			logger.debug("Failed to create Project with parameters as projectName : {} location : {}, exception : {}", 
					new Object[]{projectName, location, e});
			project = null;
		}

		return project;
	} 

	private void copyBuildFile(String source, IProject project) throws CoreException {
		File sourceFileLocation = new File(source);
		File[] listFiles = sourceFileLocation.listFiles();
		if(listFiles != null){
			for(File sourceFile : listFiles){
				IFile destinationFile = project.getFile(sourceFile.getName());
				try {
					destinationFile.create(new FileInputStream(sourceFile), true, null);
				} catch (FileNotFoundException | CoreException exception) {
					logger.debug("Copy build file operation failed");
					throw new CoreException(new MultiStatus(Activator.PLUGIN_ID, 100, "Copy build file operation failed", exception));
				}
			}
		}
	}

	/**
	 * Sets the <b>src</b> folder as the source folder in project
	 * @param project
	 * @param javaProject
	 * @return IClasspathEntry[]
	 * @throws JavaModelException
	 */
	private IClasspathEntry[] setSourceFolderInClassPath(IProject project,	IJavaProject javaProject) throws JavaModelException {
		IFolder sourceFolder = project.getFolder(CustomMessages.ProjectSupport_SRC); //$NON-NLS-1$
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
		return newEntries;
	}

	/**
	 * Adds java libraries to the classpath
	 * @return List &lt; IClasspathEntry &gt;
	 */
	private List<IClasspathEntry> addJavaLibrariesInClassPath() {
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		for (LibraryLocation element : locations) {
			entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}
		return entries;
	}

	/**
	 * Creates the specified folder if it does not exists
	 * @param folder
	 * @throws CoreException
	 */
	private void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}

	/**
	 * Create a folder structure as specified in the parameters
	 * @param newProject 
	 * @param paths contains the names of the folder that need to be created.
	 * @throws CoreException
	 */
	private void addToProjectStructure(IProject newProject, String[] paths) throws CoreException {
		for (String path : paths) {
			IFolder etcFolders = newProject.getFolder(path);
			createFolder(etcFolders);
		}
	}

	private void addNature(IProject project) throws CoreException{
		if(!project.hasNature(ProjectNature.NATURE_ID)){
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 2];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = ProjectNature.NATURE_ID;
			newNatures[prevNatures.length + 1] = JavaCore.NATURE_ID;

			// validate the natures
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IStatus status = workspace.validateNatureSet(newNatures); 
			BuildCommand buildCommand= new BuildCommand();
			buildCommand.setName("org.eclipse.jdt.core.javabuilder");
			BuildCommand[] iCommand = {buildCommand};
			description.setBuildSpec(iCommand); 
			// only apply new nature, if the status is ok
			if (status.getCode() == IStatus.OK) {
				description.setNatureIds(newNatures);
				project.setDescription(description, null);
			}
			logger.debug("Project nature added"); 
		}
	}

	/**
	 * Creates the base structure of the project under specified location and name
	 * @param projectName
	 * @param location
	 * @return
	 * @throws CoreException 
	 */
	private IProject createBaseProject(String projectName, URI location) throws CoreException {
		IProject newProject=null;
		File file;
		String errorMessage="Project already exists in Workspace";
		if(location==null)
		{
			file=new File(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/"+projectName);
			if(file.exists())
				newProject = checkIfProjectAlreadyExists(newProject,errorMessage);
			else
				newProject = createTheProjectAtSpecifiedLocation(projectName,location);
		}
		else
		{
			URI newLocation=URI.create(location+"/"+projectName);
			file=new File(newLocation);
			if(file.exists())
				newProject = checkIfProjectAlreadyExists(newProject,errorMessage);
			else
				newProject=	createTheProjectAtSpecifiedLocation(projectName, newLocation);
		}
		return newProject;
	}
	private IProject createTheProjectAtSpecifiedLocation(String projectName,
			URI location) throws CoreException {
		IProject newProject;
		newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if(!newProject.exists()){
			IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());
			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				desc.setLocationURI(null);
			}
			else{
				desc.setLocationURI(location);
			}

			try {
				newProject.create(desc, null);
				if (!newProject.isOpen()) {
					newProject.open(null);
				}
				logger.debug("Project base structure created"); 
			} catch (CoreException exception) {
				logger.debug("Project base structure creation failed");
				throw exception;
			}
		}
		return newProject;
	}

	private IProject checkIfProjectAlreadyExists(IProject newProject,String errorMessage) {
		MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
		messageBox.setText("Error");
		messageBox.setMessage(errorMessage);
		if(messageBox.open()==SWT.OK){
			newProject=null;
		}
		return newProject;
	}

	/*
	 * Copy External jar to project lib directory
	 * @param source path
	 * @param target path
	 * @return list of added files path
	 */
	private void copyExternalLibAndAddToClassPath(String source,IFolder destinationFolder, List<IClasspathEntry> entries) throws CoreException{
		File sourceFileLocation = new File(source);
		File[] listFiles = sourceFileLocation.listFiles();
		if(listFiles != null){
			for(File sourceFile : listFiles){
				IFile destinationFile = destinationFolder.getFile(sourceFile.getName());
				try {
					destinationFile.create(new FileInputStream(sourceFile), true, null);
					entries.add(JavaCore.newLibraryEntry(new Path(destinationFile.getLocation().toOSString()), null, null));
				} catch (FileNotFoundException | CoreException exception) {
					logger.debug("Copy external library files operation failed", exception);
					throw new CoreException(new MultiStatus(Activator.PLUGIN_ID, 101, "Copy external library files operation failed", exception));
				}
			}
		}
	}

	/**
	 * The Class InvalidProjectNameException.
	 * 
	 * @author Bitwise
	 */
	public class InvalidProjectNameException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6194407190206545086L;
	}
}
