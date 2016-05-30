package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class FilterAction extends Action {
	
	public FilterAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("filter action");
		super.run();
	}
}
