package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.preferances.ViewDataPreferences;
import hydrograph.ui.dataviewer.preferances.ViewDataPreferencesDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

public class PreferencesAction extends Action {
	private ViewDataPreferences viewDataPreferences;
	private DebugDataViewer debugDataViewer;
	public PreferencesAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
		this.debugDataViewer = debugDataViewer;
	
	}
	@Override
	public void run() {
		viewDataPreferences=debugDataViewer.getViewDataPreferences();
		ViewDataPreferencesDialog viewDataPreferencesDialog=new ViewDataPreferencesDialog(new Shell());
		viewDataPreferencesDialog.setViewDataPreferences(viewDataPreferences);
		viewDataPreferencesDialog.open();
	}
}
