package hydrograph.ui.dataviewer.actions;

import java.util.ArrayList;

import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class ResetColumnsAction extends Action{
	
	private DebugDataViewer debugDataViewer;
	private static final String LABEL = "Reset Columns";
	SelectColumnAction selectColumnAction;
	
	public ResetColumnsAction(DebugDataViewer debugDataViewer){
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
	}
	
	@Override
	public void run() {
		selectColumnAction = ((SelectColumnAction)debugDataViewer.getActionFactory().getAction(SelectColumnAction.class.getName()));
		selectColumnAction.diposeTable();
		selectColumnAction.setSelectedColumns(new ArrayList<String>());
		selectColumnAction .setAllColumns(new ArrayList<String>());
		debugDataViewer.getDataViewerAdapter().setColumnList(new ArrayList<String>(debugDataViewer.getDataViewerAdapter().getAllColumnsMap().keySet()));
		selectColumnAction.recreateViews();
	}
}
