package hydrograph.ui.dataviewer.actions;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.eclipse.jface.action.Action;

import hydrograph.ui.dataviewer.DebugDataViewer;

public class ActionFactory {
	private DebugDataViewer debugDataViewer;
	private HashMap<String,Action> actionMap;
	
	
	public ActionFactory(){
		createAllActions();
	}
	
	public ActionFactory(DebugDataViewer debugDataViewer) {
		super();
		this.debugDataViewer = debugDataViewer;
		actionMap = new LinkedHashMap<>();
		createAllActions();
	}
	
	private void createAllActions(){
		ExportAction exportAction=new ExportAction("Export",debugDataViewer);
		FilterAction filterAction =new FilterAction("Filter",debugDataViewer);
		SelectAllAction selectAllAction=new SelectAllAction("Select All",debugDataViewer);
		CopyAction copyAction=new CopyAction("Copy",debugDataViewer);
		FindAction findAction=new FindAction("Find",debugDataViewer);		
		GridViewAction gridViewAction =new GridViewAction("Grid View", debugDataViewer);
		HorizontalViewAction horizontalViewAction = new HorizontalViewAction("Horizontal View",debugDataViewer);
		UnformattedViewAction unformattedViewAction = new UnformattedViewAction("Unformatted Text View", debugDataViewer);
		FormattedViewAction formattedViewAction = new FormattedViewAction("Formatted Text View", debugDataViewer);
		ReloadAction reloadAction = new ReloadAction("Reload", debugDataViewer);
		PreferencesAction preferencesAction= new PreferencesAction("Preferences",debugDataViewer);
		
		actionMap.put("ExportAction", exportAction);
		actionMap.put("FilterAction", filterAction);
		actionMap.put("SelectAllAction", selectAllAction);
		actionMap.put("CopyAction", copyAction);
		actionMap.put("FindAction", findAction);
		actionMap.put("GridViewAction", gridViewAction);
		actionMap.put("HorizontalViewAction", horizontalViewAction);
		actionMap.put("UnformattedViewAction", unformattedViewAction);
		actionMap.put("FormattedViewAction", formattedViewAction);
		actionMap.put("ReloadAction", reloadAction);
		actionMap.put("PreferencesAction", preferencesAction);
		
	}
	
	public Action getAction(String action){
		return actionMap.get(action);
	}
	
	
}
