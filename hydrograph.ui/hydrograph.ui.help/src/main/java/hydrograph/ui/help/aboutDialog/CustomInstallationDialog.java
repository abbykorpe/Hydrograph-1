package hydrograph.ui.help.aboutDialog;

import hydrograph.ui.datastructure.property.InstallationWindowDetails;
import hydrograph.ui.datastructure.property.JarInformationDetails;

import java.io.File;

import org.eclipse.core.internal.registry.ConfigurationElementHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.about.InstallationPage;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.about.AboutPluginsPage;
import org.eclipse.ui.internal.about.InstallationDialog;
import org.eclipse.ui.services.IServiceLocator;

import com.thoughtworks.xstream.XStream;

public class CustomInstallationDialog extends InstallationDialog {

	
	
	private final static int MORE_ID = IDialogConstants.CLIENT_ID + 1;
	private final static int COLUMNS_ID = MORE_ID + 2;
	private static IServiceLocator serviceLocator;
	TabFolder tabFolder;
	Composite composite;
	InstallationWindowDetails installationWindowDetails;
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

		TabItem tbtmLibraries = new TabItem(tabFolder, SWT.NONE);
		tbtmLibraries.setText("Libraries");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		tbtmLibraries.setControl(composite_1);
		
		// ObjectToXMLGeneration.INSTANCE.objectToXMlConverter(file);
		TableViewer tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL
				| SWT.V_SCROLL);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		readFromXMLFile(file);
		createTableViewerColumns(tableViewer, "Name");
		createTableViewerColumns(tableViewer, "Version No");
		createTableViewerColumns(tableViewer, "Generic Id");
		createTableViewerColumns(tableViewer, "Artifact Id");
		tableViewer.setLabelProvider(new InstallationDetailsLabelProvider());
		tableViewer.setContentProvider(new InstallationDetailsContentProvider());
		tableViewer.setInput(installationWindowDetails.getJarInfromationDetails());
		tableViewer.refresh();
		ConfigurationElementHandle object = (ConfigurationElementHandle) tabFolder.getItem(0).getData();

		addListenerToLibrariesTab(tbtmLibraries);
		
		return composite;
	}

	private void addListenerToLibrariesTab(final TabItem tbtmLibraries) {
		final TabFolder folder=tbtmLibraries.getParent();
		tbtmLibraries.getParent().addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
			TabItem item=folder.getItem(3);
				//SashForm form=(SashForm) comp1.getChildren()[0];
				//Composite composite=(Composite) form.getChildren()[0];
			Composite comp1=(Composite)((Composite)((Composite)composite.getParent().getChildren()[1]).getChildren()[0]).getChildren()[0].getParent();
			
				if(folder.getSelectionIndex()==3){
					// increment the number of columns in the button bar
					
					((GridLayout) comp1.getLayout()).numColumns=((GridLayout) comp1.getLayout()).numColumns+3;
					comp1.setBackground(new Color(null,200,0,0));
					Composite compositeaa=new Composite(comp1, SWT.BORDER);
					compositeaa.setBackground(new Color(null, 0,0,200));
					Button button = new Button(comp1, SWT.PUSH);
					button.setText("LLLLLLLL");
					button.setFont(JFaceResources.getDialogFont());
					button.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent event) {
							buttonPressed(((Integer) event.widget.getData()).intValue());
						}
					});
					
					comp1.getChildren()[3].dispose();
					
					setButtonLayoutData(button);
					comp1.getChildren();
					comp1.redraw();
					System.out.println();
					
			}
				
				if(folder.getSelectionIndex()==1){
					TabItem item2=folder.getItem(1);
					
					for(Control control:comp1.getChildren()){
						if(control instanceof Button){
							Button button=(Button) control;
							
						}
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}

	/**
	 * Creates columns for the Schema Grid
	 * 
	 * @param tableViewer
	 */
	public TableViewerColumn createTableViewerColumns(TableViewer tableViewer, String columnName) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(100);
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

	@Override
	protected Control createButtonBar(Composite parent) {
		return super.createButtonBar(parent);

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		
	}
 
	@Override
	protected void createButtons(InstallationPage page) {
		
		super.createButtons(page);
	}
	
	public void createPageButtons(Composite parent) {
		
		
		Button moreInfo = createButton(parent, MORE_ID, WorkbenchMessages.AboutPluginsDialog_moreInfo, false);
		moreInfo.setEnabled(false);

		Button columns = createButton(parent, COLUMNS_ID, WorkbenchMessages.AboutPluginsDialog_columns, false);

	}
@Override
public int open() {
	// TODO Auto-generated method stub
	return super.open();
}
}
