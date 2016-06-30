package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class ClearFilter extends Action {

	private DebugDataViewer debugDataViewer;
	private static final String LABEL = "Clear Filter";
	
	public ClearFilter(DebugDataViewer debugDataViewer) {
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
	}
	
	@Override
	public void run() {
		
		System.out.println("CLEAR FILTER ");
		
		//DataViewerUtility.INSTANCE.resetSort(debugDataViewer);
	}
}
