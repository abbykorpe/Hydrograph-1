package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class CopyAction extends Action {
	
	public CopyAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("CopyAction");
		super.run();
	}

}
