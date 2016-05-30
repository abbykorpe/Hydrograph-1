package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.CSVDataViewer;
import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.InputBox;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

public class ReloadAction extends Action {
	private CSVDataViewer csvDataViewer;
	public ReloadAction(String menuItem, CSVDataViewer csvDataViewer) {
		super(menuItem);
		this.csvDataViewer = csvDataViewer;
	}
	public ReloadAction(String menuItem, DebugDataViewer debugDataViewer) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void run() {
		System.out.println("ReloadAction");
		InputBox inputBox = new InputBox(Display.getDefault().getActiveShell());
		inputBox.open();
		csvDataViewer.loadDataFile(inputBox.getDirectoryName(), inputBox.getFileName());
		super.run();
	}

}
