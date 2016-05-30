package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class ClearAction extends Action{
	
	public ClearAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("ClearAction");
		super.run();
	}

}
