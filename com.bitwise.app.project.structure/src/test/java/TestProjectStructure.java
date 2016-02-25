package test.java;
import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.junit.Assert;
import org.junit.Test;

import com.bitwise.app.project.structure.natures.ProjectNature;
import com.bitwise.app.project.structure.wizard.ProjectStructureCreator;
 
 
// TODO: Auto-generated Javadoc
/**
 * The Class TestProjectStructure.
 * 
 * @author Bitwise
 */
public class TestProjectStructure {
    
	/**
	 * Test create project with empty name arg.
	 */
    @SuppressWarnings("nls")
    @Test(expected = ProjectStructureCreator.InvalidProjectNameException.class)
    public void testCreateProjectWithEmptyNameArg() {
        ProjectStructureCreator.INSTANCE.createProject("", null);
    }
 
	/**
	 * Test create project with null name arg.
	 */
    @Test(expected = ProjectStructureCreator.InvalidProjectNameException.class)
    public void testCreateProjectWithNullNameArg() {
        ProjectStructureCreator.INSTANCE.createProject(null, null);
    }
    
	/**
	 * Test create project with good args.
	 * 
	 * @throws CoreException
	 *             the core exception
	 */
    @SuppressWarnings("nls")
    @Test
    public void testCreateProjectWithGoodArgs() throws CoreException {
        // This is the default workspace for this plug-in
        String workspaceFilePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
        String projectName = "delete-me";
        String projectPath = workspaceFilePath + File.separator + projectName;
 
        assertProjectDotFileAndStructureAndNatureExist(projectPath, projectName, null);
    }
 
    
    @SuppressWarnings("nls")
    private void assertProjectDotFileAndStructureAndNatureExist(String projectPath, String name, URI location) 
    		throws CoreException {
        IProject project = ProjectStructureCreator.INSTANCE.createProject(name, location);
        String projectFilePath = projectPath + File.separator + ".project";
 
        Assert.assertNotNull(project);
        assertFileExists(projectFilePath);
        assertNatureIn(project);
        assertFolderStructureIn(projectPath);
        project.delete(true, null);
    }
 
    @SuppressWarnings("nls")
    private void assertFolderStructureIn(String projectPath) {
        for (String path : ProjectStructureCreator.paths) {
            File file = new File(projectPath + File.separator + path);
            if (!file.exists()) {
                Assert.fail("Folder structure " + path + " does not exist.");
            }
        }
    }
 
    private void assertNatureIn(IProject project) throws CoreException {
        IProjectDescription descriptions = project.getDescription();
        String[] natureIds = descriptions.getNatureIds();
        if (natureIds.length != 2) {
            Assert.fail("Not all natures found in project."); //$NON-NLS-1$
        }
 
        if (!natureIds[0].equals(ProjectNature.NATURE_ID) ||
        		!natureIds[1].equals(JavaCore.NATURE_ID)) {
            Assert.fail("Required project natures not found in project."); //$NON-NLS-1$
        }
    }
 
    private void assertFileExists(String projectFilePath) {
        File file = new File(projectFilePath);
 
        if (!file.exists()) {
            Assert.fail("File " + projectFilePath + " does not exist."); //$NON-NLS-1$//$NON-NLS-2$
        }
    }
}