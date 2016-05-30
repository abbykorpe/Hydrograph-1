package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.CSVDataViewer;
import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class ViewDataGridMenuCreator implements IMenuCreator{

	CSVDataViewer csvDataViewer;

	public ViewDataGridMenuCreator(CSVDataViewer csvDataViewer) {
		this.csvDataViewer = csvDataViewer;
	}

	public ViewDataGridMenuCreator(DebugDataViewer debugDataViewer) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dispose() {
	}

	@Override
	public Menu getMenu(Control parent) {
		Menu menu=new Menu(parent);
		ActionContributionItem gridViewMenuitem=new ActionContributionItem(new GridViewAction("Grid View",csvDataViewer));
		ActionContributionItem horizontalViewMenuItem=new ActionContributionItem(new HorizontalViewAction("Horizontal View"));
		ActionContributionItem unformattedViewMenuItem=new ActionContributionItem(new UnformattedViewAction("Formatted View",csvDataViewer));
		gridViewMenuitem.fill(menu, 0);
		horizontalViewMenuItem.fill(menu, 1);
		unformattedViewMenuItem.fill(menu,2);
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		return parent;
	}


}