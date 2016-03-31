package com.bitwise.app.parametergrid.dialog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.common.datastructures.parametergrid.FilePath;

public class AppLaunch extends ApplicationWindow {

	/**
	 * Create the application window.
	 */
	public AppLaunch() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
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
		{
			Button btnNewButton = new Button(container, SWT.NONE);
			btnNewButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					/*List<FilePath> filepathList = new LinkedList<>();
					filepathList.add(new FilePath("param1.properties","C:\\Users\\shrirangk\\Desktop\\Paramfiles\\param1.properties",true,false));
					filepathList.add(new FilePath("param2.properties","C:\\Users\\shrirangk\\Desktop\\Paramfiles\\param2.properties",false,false));
					filepathList.add(new FilePath("param3.properties","C:\\Users\\shrirangk\\Desktop\\Paramfiles\\param3.properties",false,true));
					filepathList.add(new FilePath("param4.properties","C:\\Users\\shrirangk\\Desktop\\Paramfiles\\param4.properties",false,false));
					filepathList.add(new FilePath("param5.properties","C:\\Users\\shrirangk\\Desktop\\Paramfiles\\param5.properties",false,true));*/
					
					FileInputStream fin;
					List<FilePath> filepathList = new LinkedList<>();
					try {
						fin = new FileInputStream("C:\\Users\\shrirangk\\Desktop\\Paramfiles\\param.meta");
						ObjectInputStream ois = new ObjectInputStream(fin);
						filepathList = (LinkedList<FilePath>) ois.readObject();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException xe) {
						// TODO Auto-generated catch block
						xe.printStackTrace();
					} catch (ClassNotFoundException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					
					
					ParameterFileDialog testDialog = new ParameterFileDialog(new Shell());
					testDialog.setParameterFiles(filepathList);
					testDialog.open();
				}
			});
			btnNewButton.setBounds(74, 53, 75, 25);
			btnNewButton.setText("New Button");
		}

		return container;
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
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
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
			AppLaunch window = new AppLaunch();
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
