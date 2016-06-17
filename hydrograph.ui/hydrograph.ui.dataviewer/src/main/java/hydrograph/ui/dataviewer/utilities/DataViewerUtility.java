package hydrograph.ui.dataviewer.utilities;

import hydrograph.ui.dataviewer.window.DebugDataViewer;

public class DataViewerUtility {
	DebugDataViewer debugDataViewer;

	public DataViewerUtility(DebugDataViewer debugDataViewer) {
		this.debugDataViewer = debugDataViewer;
	}
	
	public void resetSort(){
		debugDataViewer.getRecentlySortedColumn().setImage(null);
		debugDataViewer.getDataViewLoader().updateDataViewLists();
		debugDataViewer.getDataViewLoader().reloadloadViews();
	}
}
