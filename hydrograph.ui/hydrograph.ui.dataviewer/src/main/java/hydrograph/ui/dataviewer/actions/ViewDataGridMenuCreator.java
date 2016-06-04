package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.DebugDataViewer;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class ViewDataGridMenuCreator implements IMenuCreator{

	ActionFactory actionFactory;

	public ViewDataGridMenuCreator(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}

	@Override
	public void dispose() {
	}

	@Override
	public Menu getMenu(Control parent) {
		Menu menu=new Menu(parent);
		
		/*ActionContributionItem gridViewMenuitem=new ActionContributionItem(new GridViewAction("Grid View",debugDataViewer));
		ActionContributionItem horizontalViewMenuItem=new ActionContributionItem(new HorizontalViewAction("Horizontal View",debugDataViewer));
		ActionContributionItem formattedViewMenuItem=new ActionContributionItem(new FormattedViewAction("Formatted View",debugDataViewer));
		ActionContributionItem unformattedViewMenuItem=new ActionContributionItem(new UnformattedViewAction("Unformatted View",debugDataViewer));*/
		
		ActionContributionItem gridViewMenuitem=new ActionContributionItem(actionFactory.getAction("GridViewAction"));
		//ActionContributionItem horizontalViewMenuItem=new ActionContributionItem(actionFactory.getAction("HorizontalViewAction"));
		ActionContributionItem formattedViewMenuItem=new ActionContributionItem(actionFactory.getAction("FormattedViewAction"));
		ActionContributionItem unformattedViewMenuItem=new ActionContributionItem(actionFactory.getAction("UnformattedViewAction"));
		
		gridViewMenuitem.fill(menu, 0);
		/*horizontalViewMenuItem.fill(menu, 1);
		formattedViewMenuItem.fill(menu,2);
		unformattedViewMenuItem.fill(menu,3);*/
		formattedViewMenuItem.fill(menu,1);
		unformattedViewMenuItem.fill(menu,2);
		
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		return parent;
	}


}