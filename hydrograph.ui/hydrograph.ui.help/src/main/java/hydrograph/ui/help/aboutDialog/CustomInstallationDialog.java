/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hydrograph.ui.help.aboutDialog;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.osgi.framework.Bundle;
import org.slf4j.Logger;

import com.thoughtworks.xstream.XStream;

import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.InstallationWindowDetails;
import hydrograph.ui.datastructure.property.JarInformationDetails;
import hydrograph.ui.logging.factory.LogFactory;

/**
 * The Class CustomInstallationDialog. This class creates the tabFolder which
 * displays information for JarInformation.
 * 
 * @author Bitwise
 */
public class CustomInstallationDialog extends InstallationDialog {

	private static IServiceLocator serviceLocator;
	private TabFolder tabFolder;
	private Composite composite;
	private TableViewer tableViewer;
	private Composite composite_1;
	private URL fileUrl;
	private Logger logger = LogFactory.INSTANCE.getLogger(CustomInstallationDialog.class);
	private InstallationWindowDetails installationWindowDetails;
	private Bundle bundle = Platform.getBundle("hydrograph.ui.help");
	private IPath path = new Path("/xml/About_Window_Installation_Details.xml");
	private AboutPluginsPage page;

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
		
		URL url = FileLocator.find(bundle, path, null);
		try {
			fileUrl = FileLocator.toFileURL(url);
		} catch (IOException e2) {
			logger.error(e2.getMessage());
		}
		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		readFromXMLFile(fileUrl);
		createTableViewerColumns(tableViewer, "Name");
		createTableViewerColumns(tableViewer, "Version No");
		createTableViewerColumns(tableViewer, "Group Id");
		createTableViewerColumns(tableViewer, "Artifact Id");
		TableViewerColumn tableLicense = createTableViewerColumns(tableViewer, "License Info");

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(0);
		tblclmnItem.setResizable(false);
		tblclmnItem.setText("Path");

		tableViewer.setLabelProvider(new InstallationDetailsLabelProvider());
		tableViewer.setContentProvider(new InstallationDetailsContentProvider());
		tableViewer.setInput(installationWindowDetails.getJarInfromationDetails());
		tableLicense.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Object element = cell.getElement();
				if (element instanceof JarInformationDetails) {
					JarInformationDetails jarInfo = (JarInformationDetails) cell.getElement();

					/* make text look like a link */
					StyledString text = new StyledString();
					StyleRange myStyledRange = new StyleRange(0, jarInfo.getLicenseInfo().length(),
							Display.getCurrent().getSystemColor(SWT.COLOR_BLUE), null);
					myStyledRange.underline = true;
					text.append(jarInfo.getLicenseInfo(), StyledString.DECORATIONS_STYLER);
					cell.setText(text.toString());

					StyleRange[] range = { myStyledRange };
					cell.setStyleRanges(range);

					super.update(cell);

				}
			}
		});
		tableViewer.refresh();

		tableViewer.getControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
				JarInformationDetails details = (JarInformationDetails) selection.getFirstElement();
				IPath iPath = new Path(details.getPath());
				try {
					URL url = FileLocator.find(bundle, iPath, null);
					 URL fileUrlForPath = FileLocator.toFileURL(url);
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(fileUrlForPath);
				} catch (PartInitException | IOException e1) {
					logger.error(e1.getMessage());
				}
				;

			}
		});

		return composite;
	}

	/**
	 * Creates columns for the Table Viewer
	 * 
	 * @param tableViewer
	 * @return tableViewerColumn
	 */
	public TableViewerColumn createTableViewerColumns(TableViewer tableViewer, String columnName) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(180);
		tblclmnItem.setText(columnName);

		return tableViewerColumn;
	}

	/**
	 * Reads the XML file(About_Window_Installation_Details.xml) to display in
	 * Installation Window
	 * 
	 * @param file
	 * 
	 */
	public void readFromXMLFile(URL file) {

		XStream xstream = new XStream();
		xstream.alias("InstallationWindowDetails", InstallationWindowDetails.class);
		xstream.alias("JarInformationDetail", JarInformationDetails.class);

		installationWindowDetails = (InstallationWindowDetails) xstream.fromXML(file);

	}

}
