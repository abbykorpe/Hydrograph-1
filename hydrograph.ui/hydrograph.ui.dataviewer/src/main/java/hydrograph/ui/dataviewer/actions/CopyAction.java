package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class CopyAction extends Action {
	
	public CopyAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("CopyAction");
		super.run();
	}

}
