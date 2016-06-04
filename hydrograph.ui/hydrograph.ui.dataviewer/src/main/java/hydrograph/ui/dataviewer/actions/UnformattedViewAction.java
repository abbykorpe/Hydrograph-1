package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class UnformattedViewAction extends Action {
	private DebugDataViewer debugDataViewer;
	
	public UnformattedViewAction(String menuItem,
			DebugDataViewer debugDataViewer) {
		super(menuItem);
		this.debugDataViewer = debugDataViewer;
	}
	@Override
	public void run() {
		debugDataViewer.createUnformattedViewTabItem();
		super.run();
	}
}
