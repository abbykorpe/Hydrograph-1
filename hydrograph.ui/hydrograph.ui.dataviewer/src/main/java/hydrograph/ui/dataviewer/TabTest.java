package hydrograph.ui.dataviewer;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TabTest extends ApplicationWindow {
	private CTabFolder tabFolder;
	/**
	 * Create the application window,
	 */
	public TabTest() {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		{
			tabFolder = new CTabFolder(container, SWT.BORDER | SWT.CLOSE);
			tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
			{
				createTabItem1(tabFolder);
			}
			{
				createTabItem2(tabFolder);
			}
			{
				createTabItem3(tabFolder);
			}
		}
		{
			Composite composite = new Composite(container, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			{
				Button btnItem = new Button(composite, SWT.NONE);
				btnItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						createTabItem1(tabFolder);
					}
				});
				btnItem.setText("Item1");
			}
			{
				Button btnItem_1 = new Button(composite, SWT.NONE);
				btnItem_1.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						createTabItem2(tabFolder);
					}
				});
				btnItem_1.setText("Item2");
			}
			{
				Button btnItem_2 = new Button(composite, SWT.NONE);
				btnItem_2.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						createTabItem3(tabFolder);
					}
				});
				btnItem_2.setText("Item3");
			}
		}

		return container;
	}

	private void createTabItem3(CTabFolder tabFolder) {
		CTabItem tbtmItem_2 = new CTabItem(tabFolder, SWT.NONE);
		tbtmItem_2.setText("Item3");
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmItem_2.setControl(composite);
			composite.setLayout(new GridLayout(6, false));
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			{
				Label lblItem_2 = new Label(composite, SWT.NONE);
				lblItem_2.setText("Item3");
			}
		}
	}

	private void createTabItem2(CTabFolder tabFolder) {
		CTabItem tbtmItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tbtmItem_1.setText("Item2");
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmItem_1.setControl(composite);
			composite.setLayout(new GridLayout(3, false));
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			{
				Label lblItem_1 = new Label(composite, SWT.NONE);
				lblItem_1.setText("Item2");
			}
		}
	}

	private void createTabItem1(CTabFolder tabFolder) {
		CTabItem tbtmItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmItem.setText("Item1");
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmItem.setControl(composite);
			composite.setLayout(new GridLayout(5, false));
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			{
				Label lblItem = new Label(composite, SWT.NONE);
				lblItem.setText("Item1");
			}
		}
		tbtmItem.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Tab disposed");
			}
		});
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the coolbar manager.
	 * @return the coolbar manager
	 */
	@Override
	protected CoolBarManager createCoolBarManager(int style) {
		CoolBarManager coolBarManager = new CoolBarManager(style);
		return coolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			TabTest window = new TabTest();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
