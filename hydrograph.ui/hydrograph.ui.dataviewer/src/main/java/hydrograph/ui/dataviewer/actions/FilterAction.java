package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class FilterAction extends Action {
	
	public FilterAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("filter action");
		super.run();
	}
}
