package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class GridViewAction extends Action {
	private DebugDataViewer debugDataViewer;
	public GridViewAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
		this.debugDataViewer = debugDataViewer;
	}
	@Override
	public void run() {
		debugDataViewer.createGridViewTabItem();
		super.run();
	}
}
