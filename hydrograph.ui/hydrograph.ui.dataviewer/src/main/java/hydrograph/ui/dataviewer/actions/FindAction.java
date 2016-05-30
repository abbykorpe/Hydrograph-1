package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.CSVDataViewer;
import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.find.FindBox;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FindAction extends Action{
	private CSVDataViewer csvDataViewer;
	public FindAction(String menuItem, CSVDataViewer csvDataViewer) {
		super(menuItem);
		this.csvDataViewer = csvDataViewer;
	}
	public FindAction(String menuItem, DebugDataViewer debugDataViewer) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void run() {
		System.out.println("FindAction");
		FindBox findBox = new FindBox(Display.getDefault().getActiveShell(),csvDataViewer);
		findBox.open();
		//super.run();
	}

}
