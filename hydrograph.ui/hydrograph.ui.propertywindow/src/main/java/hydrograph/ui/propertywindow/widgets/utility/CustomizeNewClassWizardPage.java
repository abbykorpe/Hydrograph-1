package hydrograph.ui.propertywindow.widgets.utility;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

public class CustomizeNewClassWizardPage extends NewClassWizardPage {

	@Override
	protected void createContainerControls(Composite parent, int nColumns) {

		super.createContainerControls(parent, nColumns);
		Text text = (Text) parent.getChildren()[1];
		text.setEditable(false);
		IEditorInput editorInput = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor().getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
			IProject project = fileEditorInput.getFile().getProject();
			if (project != null) {
				IFolder srcFolder = project.getFolder("src/main/java");
				if (srcFolder != null && srcFolder.exists()) {
					text.setText(project.getName() + "/" + srcFolder.getProjectRelativePath().toString());
				}
			}
			Button button = (Button) parent.getChildren()[2];
			button.setEnabled(false);
		}
	}
}
