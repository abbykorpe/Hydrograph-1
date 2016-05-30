package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class HorizontalViewAction extends Action{
	
	public HorizontalViewAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("HorizontalViewAction");
		super.run();
	}
}
