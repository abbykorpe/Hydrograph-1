package hydrograph.ui.dataviewer.preferances;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ViewDataPreferenceHandler extends AbstractHandler{
	
	private static final String viewData = "hydrograph.ui.graph.preferences.PreferencePage";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewData);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
			   
		return null;
	}

}
