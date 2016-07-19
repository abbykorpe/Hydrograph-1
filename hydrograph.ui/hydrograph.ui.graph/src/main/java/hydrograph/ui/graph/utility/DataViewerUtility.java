package hydrograph.ui.graph.utility;

import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.graph.job.JobManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataViewerUtility {
	
	public static DataViewerUtility INSTANCE = new DataViewerUtility();
	
	private DataViewerUtility(){
		
	}
	
	public void closeDataViewerWindows() {
		List<DebugDataViewer> dataViewerList = new ArrayList<>(); 
		dataViewerList.addAll(JobManager.INSTANCE.getDataViewerMap().values());
				
		JobManager.INSTANCE.getDataViewerMap().clear();		
		Iterator<DebugDataViewer> iterator = dataViewerList.iterator();
		while(iterator.hasNext()){
			DebugDataViewer daDebugDataViewer = (DebugDataViewer) iterator.next();
			daDebugDataViewer.close();			
			iterator.remove();
		}
	}
}
