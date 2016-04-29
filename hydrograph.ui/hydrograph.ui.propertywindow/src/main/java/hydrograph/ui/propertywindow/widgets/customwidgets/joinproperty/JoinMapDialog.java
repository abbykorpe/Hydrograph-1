package hydrograph.ui.propertywindow.widgets.customwidgets.joinproperty;

import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.JoinMappingGrid;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTJoinWidget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class JoinMapDialog extends Dialog {
	private Table table;
	private Table table_1;
	private JoinMappingGrid joinMappingGrid;
	private List<List<FilterProperties>> inputPorts = new ArrayList<>();
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private int inputPortValue = ELTJoinWidget.value;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public JoinMapDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL | SWT.RESIZE);
	}
	
	public JoinMapDialog(Shell parentShell,JoinMappingGrid joinPropertyGrid, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL | SWT.RESIZE);
		this.joinMappingGrid = joinPropertyGrid;
		this.propertyDialogButtonBar=propertyDialogButtonBar;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = createOuterMostComposite(container);
		
		createInputFieldExpandBarSection(composite);
		
		creatFieldMappingSection(composite);
		
		creatCopyInputToOutputFieldSection(composite);
		
		createNoteSection(container);

		return container;
	}

	private void createNoteSection(Composite container) {
		Composite composite_9 = new Composite(container, SWT.NONE);
		composite_9.setLayout(new GridLayout(2, false));
		GridData gd_composite_9 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_9.heightHint = 29;
		composite_9.setLayoutData(gd_composite_9);
		
		Label lblNote = new Label(composite_9, SWT.NONE);
		lblNote.setText("Note: ");
		
		Label lblThisIsTest = new Label(composite_9, SWT.NONE);
		lblThisIsTest.setText("This is test not an you can change it later");
	}

	private void creatCopyInputToOutputFieldSection(Composite composite) {
		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.verticalSpacing = 0;
		gl_composite_3.marginWidth = 0;
		gl_composite_3.marginHeight = 0;
		gl_composite_3.horizontalSpacing = 0;
		composite_3.setLayout(gl_composite_3);
		GridData gd_composite_3 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_composite_3.widthHint = 121;
		composite_3.setLayoutData(gd_composite_3);
		composite_3.setBounds(0, 0, 64, 64);
		
		Composite composite_8 = new Composite(composite_3, SWT.BORDER);
		GridLayout gl_composite_8 = new GridLayout(1, false);
		gl_composite_8.verticalSpacing = 0;
		gl_composite_8.marginWidth = 0;
		gl_composite_8.horizontalSpacing = 0;
		gl_composite_8.marginHeight = 0;
		composite_8.setLayout(gl_composite_8);
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ScrolledComposite scrolledComposite_2 = new ScrolledComposite(composite_8, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite_2.setExpandHorizontal(true);
		scrolledComposite_2.setExpandVertical(true);
		
		Composite composite_12 = new Composite(scrolledComposite_2, SWT.NONE);
		composite_12.setLayout(new GridLayout(1, false));
		
		Button btnRadioButton = new Button(composite_12, SWT.RADIO);
		btnRadioButton.setText("Radio Button");
		
		Button btnRadioButton_1 = new Button(composite_12, SWT.RADIO);
		btnRadioButton_1.setText("Radio Button");
		
		Button btnRadioButton_2 = new Button(composite_12, SWT.RADIO);
		btnRadioButton_2.setText("Radio Button");
		scrolledComposite_2.setContent(composite_12);
		scrolledComposite_2.setMinSize(composite_12.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void creatFieldMappingSection(Composite composite) {
		Composite composite_2 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.verticalSpacing = 0;
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		gl_composite_2.horizontalSpacing = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_2.setBounds(0, 0, 64, 64);
		
		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayout(new GridLayout(2, false));
		GridData gd_composite_4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_4.heightHint = 40;
		composite_4.setLayoutData(gd_composite_4);
		
		Composite composite_10 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_composite_10 = new GridLayout(1, false);
		gl_composite_10.verticalSpacing = 0;
		gl_composite_10.marginWidth = 0;
		gl_composite_10.marginHeight = 0;
		composite_10.setLayout(gl_composite_10);
		composite_10.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		
		Label lblMappingView = new Label(composite_10, SWT.NONE);
		lblMappingView.setText("Output Mapping");
		
		Composite composite_11 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_composite_11 = new GridLayout(4, false);
		gl_composite_11.verticalSpacing = 0;
		gl_composite_11.marginWidth = 0;
		gl_composite_11.marginHeight = 0;
		composite_11.setLayout(gl_composite_11);
		composite_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1));
		
		Button btnAdd = new Button(composite_11, SWT.NONE);
		btnAdd.setText("Add");
		
		Button btnDelete = new Button(composite_11, SWT.NONE);
		btnDelete.setText("Delete");
		
		Button btnUp = new Button(composite_11, SWT.NONE);
		btnUp.setText("Up");
		
		Button btnDown = new Button(composite_11, SWT.NONE);
		btnDown.setText("Down");
		
		Composite composite_5 = new Composite(composite_2, SWT.NONE);
		GridLayout gl_composite_5 = new GridLayout(1, false);
		gl_composite_5.verticalSpacing = 0;
		gl_composite_5.marginWidth = 0;
		gl_composite_5.marginHeight = 0;
		gl_composite_5.horizontalSpacing = 0;
		composite_5.setLayout(gl_composite_5);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite_5, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite_6 = new Composite(scrolledComposite, SWT.NONE);
		GridLayout gl_composite_6 = new GridLayout(1, false);
		gl_composite_6.verticalSpacing = 0;
		gl_composite_6.marginWidth = 0;
		gl_composite_6.marginHeight = 0;
		gl_composite_6.horizontalSpacing = 0;
		composite_6.setLayout(gl_composite_6);
		
		TableViewer tableViewer = new TableViewer(composite_6, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.widthHint = 374;
		table.setLayoutData(gd_table);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnPropertyName = tableViewerColumn.getColumn();
		tblclmnPropertyName.setWidth(169);
		tblclmnPropertyName.setText("Property Name");
		
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnPropertyValue = tableViewerColumn_1.getColumn();
		tblclmnPropertyValue.setWidth(148);
		tblclmnPropertyValue.setText("Property Value");
		
		TableColumnLayout layout = new TableColumnLayout();
		tableViewer.getControl().getParent().setLayout(layout);

		for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
			table.getColumn(columnIndex).pack();
		}
		
		for (int i = 0; i < tableViewer.getTable().getColumnCount(); i++) {
			layout.setColumnData(tableViewer.getTable().getColumn(i),
					new ColumnWeightData(1));
		}
		
		
		scrolledComposite.setContent(composite_6);
		scrolledComposite.setMinSize(composite_6.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private Composite createOuterMostComposite(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return composite;
	}

	private void createInputFieldExpandBarSection(Composite composite) {
		Composite composite_1 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.horizontalSpacing = 0;
		gl_composite_1.verticalSpacing = 0;
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_composite_1.widthHint = 269;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setBounds(0, 0, 64, 64);
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(composite_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		
		Composite composite_7 = new Composite(scrolledComposite_1, SWT.NONE);
		composite_7.setLayout(new GridLayout(1, false));
		
		ExpandBar expandBar = new ExpandBar(composite_7, SWT.V_SCROLL);
		expandBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		populateInputFieldExpandBarSection(expandBar);
				
		expandBar.getItem(0).setExpanded(true);
		
		scrolledComposite_1.setContent(composite_7);
		scrolledComposite_1.setMinSize(composite_7.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void populateInputFieldExpandBarSection(ExpandBar expandBar) {
		List<FilterProperties> inputPortFieldList = null;

		for (int i = 0; i < inputPortValue; i++) {
			if (joinMappingGrid != null) {
				if (joinMappingGrid.getLookupInputProperties() != null
						&& !joinMappingGrid.getLookupInputProperties()
								.isEmpty()) {
					if (i < joinMappingGrid.getLookupInputProperties().size())
						inputPortFieldList = joinMappingGrid
								.getLookupInputProperties().get(i);
					else
						inputPortFieldList = new ArrayList<>();
				} else {
					inputPortFieldList = new ArrayList<>();
				}
			}
			if (inputPorts != null) {
				inputPorts.add(inputPortFieldList);
			}
			addExpandItem(expandBar, inputPortFieldList, i);
		}
	}

	private void addExpandItem(ExpandBar expandBar, List<FilterProperties> inputPortFieldList,int portNumber) {
		ExpandItem xpndtmItem = new ExpandItem(expandBar, SWT.NONE);
		xpndtmItem.setText("Port in" + portNumber);
		
		Composite composite_13 = new Composite(expandBar, SWT.NONE);
		xpndtmItem.setControl(composite_13);
		composite_13.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer_1 = new TableViewer(composite_13, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table_1 = tableViewer_1.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer_1.setContentProvider(new ArrayContentProvider());
		
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnInputFields = tableViewerColumn_2.getColumn();
		tblclmnInputFields.setWidth(229);
		tblclmnInputFields.setText("Input Fields");
		
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				FilterProperties tableField = (FilterProperties) element;
				return tableField.getPropertyname();
			}
		});
		
		tableViewer_1.setInput(inputPortFieldList);
		xpndtmItem.setHeight(150);
		
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(922, 558);
	}
}
