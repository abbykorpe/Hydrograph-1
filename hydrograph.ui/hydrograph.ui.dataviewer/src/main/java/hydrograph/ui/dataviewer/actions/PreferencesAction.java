package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class PreferencesAction extends Action {
	
	public PreferencesAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("PreferencesAction");
		super.run();
	}
}
