package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.CSVDataViewer;
import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.Action;

public class UnformattedViewAction extends Action {
	private CSVDataViewer csvDataViewer;
	public UnformattedViewAction(String menuItem, CSVDataViewer csvDataViewer) {
		super(menuItem);
		this.csvDataViewer = csvDataViewer;
	}
	public UnformattedViewAction(String menuItem,
			DebugDataViewer debugDataViewer) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void run() {
		System.out.println("UnformattedViewAction");
		csvDataViewer.switchToFormattedTextView();
		super.run();
	}
}
