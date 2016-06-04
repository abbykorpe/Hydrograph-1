package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class FormattedViewAction extends Action {
	
	private DebugDataViewer debugDataViewer;
	public FormattedViewAction(String menuItem,
			DebugDataViewer debugDataViewer) {
		super(menuItem);
		this.debugDataViewer = debugDataViewer;
	}
	@Override
	public void run() {
		debugDataViewer.createFormatedViewTabItem();
		super.run();
	}
}
