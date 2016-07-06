package hydrograph.ui.dataviewer.actions;

import java.util.ArrayList;

import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class ResetColumns extends Action{
	
	private DebugDataViewer debugDataViewer;
	private static final String LABEL = "Reset Columns";
	SelectColumnAction selectColumnAction;
	
	public ResetColumns(DebugDataViewer debugDataViewer){
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
	}
	
	@Override
	public void run() {
		System.out.println("Reset all columns");
		selectColumnAction = ((SelectColumnAction)debugDataViewer.getActionFactory().getAction(SelectColumnAction.class.getName()));
		//((ReloadAction)debugDataViewer.getActionFactory().getAction(ReloadAction.class.getName())).setIfFilterReset(true);
		selectColumnAction.diposeTable();
		selectColumnAction.setSelectedColumns(new ArrayList<String>());
		selectColumnAction .setAllColumns(new ArrayList<String>());
		debugDataViewer.getDataViewerAdapter().setColumnList(new ArrayList<String>(debugDataViewer.getDataViewerAdapter().getAllColumnsMap().keySet()));
		selectColumnAction.recreateViews();
	}
}
