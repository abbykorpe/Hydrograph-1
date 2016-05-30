package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class SelectAllAction extends Action{
	
	public SelectAllAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("SelectAllAction");
		super.run();
	}
}
