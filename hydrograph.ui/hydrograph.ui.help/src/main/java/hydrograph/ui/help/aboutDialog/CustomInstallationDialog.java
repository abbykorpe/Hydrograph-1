package hydrograph.ui.help.aboutDialog;

import org.eclipse.core.internal.registry.ConfigurationElementHandle;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.about.InstallationDialog;
import org.eclipse.ui.services.IServiceLocator;

public class CustomInstallationDialog extends InstallationDialog {

	private static IServiceLocator serviceLocator;
	TabFolder tabFolder;

	public CustomInstallationDialog(Shell shell) {
		super(shell, serviceLocator);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		 this.tabFolder = (TabFolder) composite.getChildren()[0];
			
		TabItem tbtmLibraries = new TabItem( tabFolder, SWT.NONE);
		tbtmLibraries.setText("Libraries");
	
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		tbtmLibraries.setControl(composite_1);
		
		TableViewer tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createTableViewerColumns(tableViewer,"Name");
		createTableViewerColumns(tableViewer,"Details");
		final TableViewerColumn tableViewerColumn=createTableViewerColumns(tableViewer,"Description");
		
		ConfigurationElementHandle object =(ConfigurationElementHandle) tabFolder.getItem(0).getData();
		

		return composite;
	}
	
	/**
	 * Creates columns for the Schema Grid
	 * @param tableViewer
	 */
	public TableViewerColumn createTableViewerColumns(TableViewer tableViewer, String columnName) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(100);
		tblclmnItem.setText(columnName);
		return tableViewerColumn;
	}
	
}
