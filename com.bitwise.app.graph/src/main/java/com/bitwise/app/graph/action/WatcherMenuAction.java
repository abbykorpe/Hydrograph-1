package com.bitwise.app.graph.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
/**
 * @author Bitwise
 *
 */
public class WatcherMenuAction extends Action implements SelectionListener {

	private SelectionListener actionInstance;
	private IAction[] actions;
	
	public WatcherMenuAction(final IAction[] actions, String text, String toolTipText) {
		 super("", IAction.AS_DROP_DOWN_MENU);
		 this.actionInstance = this;
		 this.actions = actions; 
		 setText(text);
		 setToolTipText(toolTipText);
		 
		 setMenuCreator(new IMenuCreator() {
			@Override
			public Menu getMenu(Menu parent) {
				Menu menu = new Menu(parent);
			 
				for(int i=0; i<actions.length;i++){
				 MenuItem item = new MenuItem(menu, SWT.None);
				 item.setData(new Integer(i));
				 item.setText(actions[i].getText());
				 
				 item.addSelectionListener(actionInstance);
				}
				
				return menu;
			}
			
			@Override
			public Menu getMenu(Control parent) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
			}
		});
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		 
			actions[((Integer) (((MenuItem) (e.getSource())).getData())).intValue()].run();
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
			actions[0].run();
	}

	@Override
	public void run() {
		actions[0].run();
	}
}
