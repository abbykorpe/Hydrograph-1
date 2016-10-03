package hydrograph.ui.help.aboutDialog;

import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class InstallationDetailsContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// Do nothing
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// Do nothing
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List) inputElement).toArray();
	}
}
