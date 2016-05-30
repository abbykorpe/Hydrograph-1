package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class GoAction extends Action {
	
	public GoAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("Go action");
		super.run();
	}
}
