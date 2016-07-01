package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.filter.FilterConditions;
import hydrograph.ui.dataviewer.utilities.DataViewerUtility;
import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.apache.commons.lang.StringUtils;
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
		debugDataViewer.setLocalCondition("");
		debugDataViewer.setRemoteCondition("");
		debugDataViewer.setConditions(new FilterConditions());
		debugDataViewer.getDataViewerAdapter().setFilterCondition("");
		debugDataViewer.getActionFactory().getAction(ReloadAction.class.getName()).run();
		debugDataViewer.enableDisableFilter();
		
		//DataViewerUtility.INSTANCE.resetSort(debugDataViewer);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if(StringUtils.isEmpty(debugDataViewer.getLocalCondition()) && StringUtils.isEmpty(debugDataViewer.getRemoteCondition())){
			super.setEnabled(false);
		}
		else{
			super.setEnabled(true);
		}
	}
	
}
