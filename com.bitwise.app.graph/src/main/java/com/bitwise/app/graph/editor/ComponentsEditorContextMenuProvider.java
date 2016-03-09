package com.bitwise.app.graph.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.action.WatcherMenuAction;
import com.bitwise.app.graph.action.subgraph.SubMenuAction;

// TODO: Auto-generated Javadoc
/**
 * The Class ComponentsEditorContextMenuProvider.
 */
public class ComponentsEditorContextMenuProvider extends ContextMenuProvider {
	/** The editor's action registry. */
	private ActionRegistry actionRegistry;
	
	/**
	 * Instantiate a new menu context provider for the specified EditPartViewer
	 * and ActionRegistry.
	 * @param viewer the editor's graphical viewer
	 * @param registry the editor's action registry
	 * @throws IllegalArgumentException if registry is <tt>null</tt>.
	 */
	public ComponentsEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		if (registry == null) {
			throw new IllegalArgumentException();
		}
		actionRegistry = registry;
	}
	
	/**
	 * Called when the context menu is about to show. Actions, whose state is
	 * enabled, will appear in the context menu.
	 */
	@Override
	public void buildContextMenu(IMenuManager menu) {
		// Add standard action groups to the menu
		GEFActionConstants.addStandardActionGroups(menu);
		
		IAction[] actions = new IAction[3];
		actions[0] = getAction(Constants.SUBGRAPH_CREATE);
	    actions[1] = getAction(Constants.SUBGRAPH_OPEN);
	    actions[2] = getAction(Constants.SUBGRAPH_UPDATE);
	    SubMenuAction subGraphMenu=new SubMenuAction( actions, Constants.SUBGRAPH_ACTION, Constants.SUBGRAPH_ACTION_ToolTip,true);	// Add actions to the menu
		IAction[] watcherAction = new IAction[2];
         watcherAction[0] = getAction("watchPointWithDefault");
         watcherAction[1] = getAction("deleteWatchPoint");
         WatcherMenuAction watcherMenu = new WatcherMenuAction(watcherAction, "Watch Point", "Add watch"); //Add Debug Actions
       
         
         IAction actionWatchRecords = getAction("watchRecord");
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, // target group id
				getAction(ActionFactory.UNDO.getId())); // action to add
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));
	    menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.DELETE.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.SAVE.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, getAction(ActionFactory.CUT.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, getAction(ActionFactory.COPY.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, getAction(ActionFactory.PASTE.getId()));
	    menu.appendToGroup(GEFActionConstants.GROUP_REST, subGraphMenu);
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, watcherMenu);
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, actionWatchRecords);
		 
		if(subGraphMenu.getActiveOperationCount()==0)
	    subGraphMenu.setEnabled(false);
		watcherMenu.setEnabled(false);

		
	}
	private IAction getAction(String actionId) {
		return actionRegistry.getAction(actionId);
	}
}
