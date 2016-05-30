package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class CloseAction extends Action {
	
	public CloseAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("CloseAction");
		super.run();
	}

}
