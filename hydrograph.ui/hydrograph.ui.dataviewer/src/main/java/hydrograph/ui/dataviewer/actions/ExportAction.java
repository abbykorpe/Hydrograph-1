package hydrograph.ui.dataviewer.actions;

import org.eclipse.jface.action.Action;

public class ExportAction extends Action {
	
	public ExportAction(String menuItem) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("export action");
		super.run();
	}
}
