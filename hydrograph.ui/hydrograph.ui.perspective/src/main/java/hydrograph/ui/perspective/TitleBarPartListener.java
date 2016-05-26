package hydrograph.ui.perspective;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

public class TitleBarPartListener implements IPartListener{
	
	private String windowTitlePrefix="ELT Development - ";
	private String windowTitleSuffix=" - Hydrograph";
	private String windowTitleDefault="ELT Development - Hydrograph";

	@Override
	public void partActivated(IWorkbenchPart part) {
		if(part instanceof EditorPart){
			
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText(windowTitlePrefix+((EditorPart)part).getTitleToolTip()+windowTitleSuffix);
		}else
			if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences()!=null
					&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences().length==0)
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText(windowTitleDefault);
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
