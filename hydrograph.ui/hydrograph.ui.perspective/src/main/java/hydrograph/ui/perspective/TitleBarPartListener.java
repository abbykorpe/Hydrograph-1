package hydrograph.ui.perspective;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

public class TitleBarPartListener implements IPartListener{
	
	private String windowTitlePrefix = "ELT Development - ";
	private String windowTitleSuffix = " - Hydrograph";
	private String windowTitleDefault = "ELT Development - Hydrograph";

	@Override
	public void partActivated(IWorkbenchPart part) {
		Shell shell= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		IEditorReference[] editorReference= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		
		if(part instanceof EditorPart){
			if(shell != null){
			shell.setText(windowTitlePrefix+((EditorPart)part).getTitleToolTip()+windowTitleSuffix);
			}
		}else{
			if(editorReference!=null && editorReference.length==0){
				shell.setText(windowTitleDefault);
			} 
		}
	}	


	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		
		
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		
		
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	
		
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		
		
	}

}
