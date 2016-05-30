package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class StopAction extends Action {
	
	public StopAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("Stop action");
		super.run();
	}

}
