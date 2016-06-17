package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.utilities.DataViewerUtility;
import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class ResetSort extends Action {
	private DebugDataViewer debugDataViewer;
	private DataViewerUtility dataViewerUtility;
	public ResetSort(DebugDataViewer debugDataViewer) {
		this.debugDataViewer = debugDataViewer;
		dataViewerUtility = new DataViewerUtility(debugDataViewer);
	}
	
	@Override
	public void run() {
		dataViewerUtility.resetSort();
	}
}
