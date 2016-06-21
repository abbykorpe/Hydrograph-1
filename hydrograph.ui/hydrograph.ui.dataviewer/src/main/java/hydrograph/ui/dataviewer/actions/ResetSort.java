package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.utilities.DataViewerUtility;

import org.eclipse.jface.action.Action;

public class ResetSort extends Action {

	@Override
	public void run() {
		DataViewerUtility.INSTANCE.resetSort();
	}
}
