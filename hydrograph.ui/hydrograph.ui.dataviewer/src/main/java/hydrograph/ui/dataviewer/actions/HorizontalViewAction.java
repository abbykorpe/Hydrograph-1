package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class HorizontalViewAction extends Action{
	
	public HorizontalViewAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
	}
	@Override
	public void run() {
		System.out.println("HorizontalViewAction");
		super.run();
	}
}
