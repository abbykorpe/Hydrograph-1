package hydrograph.ui.graph.action.debug;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ViewDataActionMenu extends Action implements SelectionListener{

	private SelectionListener actionInstance;
	private IAction[] actions;
	private boolean hideDisabled;
	
	public ViewDataActionMenu(final IAction[] actions, String text, String toolTipText, boolean hideDisabledActions) {
		 super("", IAction.AS_DROP_DOWN_MENU);
		 this.actionInstance = this;
		 this.actions = actions; 
		 setText(text);
		 setToolTipText(toolTipText);
		 this.hideDisabled = hideDisabledActions;
		 
		 setMenuCreator(new IMenuCreator() {
			@Override
			public Menu getMenu(Menu parent) {
				Menu menu = new Menu(parent);
			 
				for(int i=0; i<actions.length;i++){
				 MenuItem item = new MenuItem(menu, SWT.None);
				 if (actions[i] == null || !actions[i].isEnabled() && hideDisabled)
                 {	                    
                 	item.setText(actions[i].getText());
                 	item.setEnabled(false);
                 	continue;
            	 	}
				 
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
	
	 public int getActiveOperationCount()
     {
         int operationCount = 0;
         for (int i = 0; i < actions.length; i++)
             operationCount += actions[i] != null && actions[i].isEnabled() ? 1 : 0;
  
         return operationCount;
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
