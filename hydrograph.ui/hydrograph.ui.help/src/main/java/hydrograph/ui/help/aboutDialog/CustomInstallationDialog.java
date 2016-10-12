package hydrograph.ui.help.aboutDialog;

import hydrograph.ui.datastructure.property.InstallationWindowDetails;
import hydrograph.ui.datastructure.property.JarInformationDetails;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.about.AboutPluginsPage;
import org.eclipse.ui.internal.about.InstallationDialog;
import org.eclipse.ui.services.IServiceLocator;

import com.thoughtworks.xstream.XStream;

public class CustomInstallationDialog extends InstallationDialog {

	
	
	private static IServiceLocator serviceLocator;
	private TabFolder tabFolder;
	private Composite composite;
	private TableViewer tableViewer;
	private Composite composite_1;
	private InstallationWindowDetails installationWindowDetails;
	File file = new File(
			"C:\\Users\\ashikah\\Git\\Hydrograph_Tool\\Thesis\\hydrograph.ui\\hydrograph.ui.help\\xml\\About_Window_Installation_Details.xml");
	AboutPluginsPage page;

	public CustomInstallationDialog(Shell shell) {
		super(shell, serviceLocator);
		page = new AboutPluginsPage();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		 composite = (Composite) super.createDialogArea(parent);
		this.tabFolder = (TabFolder) composite.getChildren()[0];
		
		composite.getShell().setMinimumSize(950, 800);
		TabItem tbtmLibraries = new TabItem(tabFolder, SWT.NONE);
		tbtmLibraries.setText("Libraries");

		composite_1 = new Composite(tabFolder, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		tbtmLibraries.setControl(composite_1);
		
		//ObjectToXMLGeneration.INSTANCE.objectToXMlConverter(file);
		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL
				| SWT.V_SCROLL);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		readFromXMLFile(file);
		createTableViewerColumns(tableViewer, "Name");
		createTableViewerColumns(tableViewer, "Version No");
		createTableViewerColumns(tableViewer, "Group Id");
		createTableViewerColumns(tableViewer, "Artifact Id");
		TableViewerColumn tableLicense=createTableViewerColumns(tableViewer, "License Info");
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(0);
		tblclmnItem.setResizable(false);
		tblclmnItem.setText("Path");
		
		
		tableViewer.setLabelProvider(new InstallationDetailsLabelProvider());
		tableViewer.setContentProvider(new InstallationDetailsContentProvider());
		tableViewer.setInput(installationWindowDetails.getJarInfromationDetails());
		tableViewer.refresh();
		
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				StructuredSelection selection=(StructuredSelection) tableViewer.getSelection();
				JarInformationDetails details=(JarInformationDetails) selection.getFirstElement();
				IPath iPath=new Path(details.getPath());
				try {
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(iPath.toFile().toURL());
				} catch (PartInitException | MalformedURLException e) {
					e.printStackTrace();
				};
				
			}
		});
		
		
		
		return composite;
	}

	
	

	/**
	 * Creates columns for the Schema Grid
	 * 
	 * @param tableViewer
	 */
	public TableViewerColumn createTableViewerColumns(TableViewer tableViewer, String columnName) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(180);
		tblclmnItem.setText(columnName);
			
		return tableViewerColumn;
	}

	public void readFromXMLFile(File file) {

		XStream xstream = new XStream();
		xstream.alias("InstallationWindowDetails", InstallationWindowDetails.class);
		xstream.alias("JarInformationDetail", JarInformationDetails.class);
		try {
			installationWindowDetails = (InstallationWindowDetails) xstream.fromXML(file);
		} catch (Exception e) {

		}

	}
	
}
