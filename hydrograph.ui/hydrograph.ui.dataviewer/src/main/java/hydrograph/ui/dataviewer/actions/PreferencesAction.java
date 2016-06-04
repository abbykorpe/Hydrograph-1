package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class PreferencesAction extends Action {
	
	public PreferencesAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("PreferencesAction");
		super.run();
	}
}
