package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class SelectAllAction extends Action{
	
	public SelectAllAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("SelectAllAction");
		super.run();
	}
}
