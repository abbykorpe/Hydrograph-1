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
package hydrograph.ui.propertywindow.widgets.dialogs.lookup;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.datastructure.property.LookupMappingGrid;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.join.support.JoinMappingEditingSupport;
import hydrograph.ui.propertywindow.widgets.dialogs.join.utils.JoinMapDialogConstants;
import hydrograph.ui.propertywindow.widgets.utility.SchemaSyncUtility;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * 
 * Lookup mapping dialog
 * 
 * @author Bitwise
 *
 */
public class LookupMapDialog extends Dialog {
	private Component component;
	private LookupMappingGrid lookupMappingGrid;
	private List<List<FilterProperties>> inputPorts;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private TableViewer mappingTableViewer;
	private List<LookupMapProperty> mappingTableItemList;
	private LookupMappingGrid oldLookupMappingGrid;
	private LookupMappingGrid newLookupMappingGrid;
	private List<String> allInputFields;
	private Button btnPull;
	private Button btnDown;
	private Button btnUp;
	private Button btnDelete;
	private Button btnAdd;
	
	private static final String FIELD_TOOLTIP_MESSAGE_NO_SUCH_INPUT_FIELD = "No such input field";
	private static final String FIELD_TOOLTIP_MESSAGE_FIELD_CANT_BE_EMPTY = "Field can not be empty";
	private static final String FIELD_TOOLTIP_MESSAGE_DUPLICATE_FIELDS = "Duplicate field";
	private static final String MAPPING_TABLE_ITEM_DELIMILATOR="#";
	private static final String IN0_PREFIX = "in0.";
	private static final String IN1_PREFIX = "in1.";
	private static final String IN0_HEADER = "Input Fields(in0)";
	private static final String IN1_HEADER = "Input Fields(in1)";
	
	private static final String PULL_BUTTON_TEXT="Pull";
	private static final String ADD_BUTTON_TEXT="Add";
	private static final String DELETE_BUTTON_TEXT="Delete";
	private static final String UP_BUTTON_TEXT="Up";
	private static final String DOWN_BUTTON_TEXT="Down";
	
	private static final String PULL_TOOLTIP = "Pull schema";
	private static final String ADD_TOOLTIP = "Add field";
	private static final String DELETE_TOOLTIP = "Delete field";
	private static final String UP_TOOLTIP = "Move field up";
	private static final String DOWN_TOOLTIP = "Move field down";
	
	private static final String DIALOG_TITLE="Lookup Mapping Dialog";
	private Table in1Table;
	private Table in0Table;
	
	/**
	 * Create the Lookup mapping dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */@Deprecated
	public LookupMapDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL
				| SWT.RESIZE);
	}

	public LookupMapDialog(Shell parentShell, Component component, LookupMappingGrid lookupPropertyGrid,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL
				| SWT.RESIZE);
		this.lookupMappingGrid = lookupPropertyGrid;
		this.component=component;
		inputPorts = new ArrayList<>();

		this.propertyDialogButtonBar = propertyDialogButtonBar;
		allInputFields = new LinkedList<>();		
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.getShell().setText(DIALOG_TITLE);
		
		SashForm composite = new SashForm(container, SWT.SMOOTH);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createInputFieldSection(composite);

		creatFieldMappingSection(composite);
		composite.setWeights(new int[] {267, 618});

		populateLookupMapDialog();
		return container;
	}


	private void creatFieldMappingSection(Composite composite) {

		Composite mappingComposite = new Composite(composite, SWT.NONE);
		GridLayout gl_mappingComposite = new GridLayout(1, false);
		gl_mappingComposite.verticalSpacing = 0;
		gl_mappingComposite.marginWidth = 0;
		gl_mappingComposite.marginHeight = 0;
		gl_mappingComposite.horizontalSpacing = 0;
		mappingComposite.setLayout(gl_mappingComposite);
		mappingComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		mappingComposite.setBounds(0, 0, 64, 64);

		createButtonSectionInFieldMappingSection(mappingComposite);

		createMappingTableInFieldMappingSection(mappingComposite);
	}

	private void createMappingTableInFieldMappingSection(Composite composite_2) {
		Composite mappingTableComposite = new Composite(composite_2, SWT.NONE);
		GridLayout gl_mappingTableComposite = new GridLayout(1, false);
		gl_mappingTableComposite.verticalSpacing = 0;
		gl_mappingTableComposite.marginWidth = 0;
		gl_mappingTableComposite.marginHeight = 0;
		gl_mappingTableComposite.horizontalSpacing = 0;
		mappingTableComposite.setLayout(gl_mappingTableComposite);
		mappingTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		ScrolledComposite mappingScrolledComposite = new ScrolledComposite(
				mappingTableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		mappingScrolledComposite.setTouchEnabled(true);
		mappingScrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		mappingScrolledComposite.setExpandHorizontal(true);
		mappingScrolledComposite.setExpandVertical(true);

		Composite mappingTableComposite2 = new Composite(mappingScrolledComposite, SWT.NONE);
		GridLayout gl_mappingTableComposite2 = new GridLayout(1, false);
		gl_mappingTableComposite2.verticalSpacing = 0;
		gl_mappingTableComposite2.marginWidth = 0;
		gl_mappingTableComposite2.marginHeight = 0;
		gl_mappingTableComposite2.horizontalSpacing = 0;
		mappingTableComposite2.setLayout(gl_mappingTableComposite2);

		Table table = createMappingTable(mappingTableComposite2);

		addTabFunctionalityInMappingTable();
		
		createInputFieldColumnInMappingTable();
		
		createOutputFieldColumnInMappingTable();

		setTableLayoutToMappingTable(table);

		mappingTableViewer.setInput(mappingTableItemList);

		attachDropFunctionalityToMappingTable();

		mappingScrolledComposite.setContent(mappingTableComposite2);
		mappingScrolledComposite.setMinSize(mappingTableComposite2.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	private void setTableLayoutToMappingTable(Table table) {
		TableColumnLayout layout = new TableColumnLayout();
		mappingTableViewer.getControl().getParent().setLayout(layout);

		for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
			table.getColumn(columnIndex).pack();
		}

		for (int i = 0; i < mappingTableViewer.getTable().getColumnCount(); i++) {
			layout.setColumnData(mappingTableViewer.getTable().getColumn(i),
					new ColumnWeightData(1));
		}
	}

	private void addTabFunctionalityInMappingTable() {
		TableViewerEditor.create(mappingTableViewer,
				new ColumnViewerEditorActivationStrategy(mappingTableViewer),
				ColumnViewerEditor.KEYBOARD_ACTIVATION
						| ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL);
	}

	private Table createMappingTable(Composite composite_6) {
		mappingTableViewer = new TableViewer(composite_6, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		Table table = mappingTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.widthHint = 374;
		table.setLayoutData(gd_table);
		mappingTableViewer.setContentProvider(new ArrayContentProvider());
		ColumnViewerToolTipSupport.enableFor(mappingTableViewer);
		return table;
	}

	private void attachDropFunctionalityToMappingTable() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		DropTarget target = new DropTarget(mappingTableViewer.getTable(),
				operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}

				String[] dropData = ((String) event.data).split(Pattern
						.quote(MAPPING_TABLE_ITEM_DELIMILATOR));
				for (String data : dropData) {
					LookupMapProperty mappingTableItem = new LookupMapProperty();
					mappingTableItem.setSource_Field(data);
					mappingTableItem.setOutput_Field(data.split("\\.")[1]);
					mappingTableItemList.add(mappingTableItem);

					mappingTableViewer.refresh();
				}
				refreshButtonStatus();
			}
		});
	}

	private void createOutputFieldColumnInMappingTable() {
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				mappingTableViewer, SWT.NONE);
		TableColumn tblclmnPropertyValue = tableViewerColumn_1.getColumn();
		tblclmnPropertyValue.setWidth(148);
		tblclmnPropertyValue.setText(JoinMapDialogConstants.OUTPUT_FIELD);
		tableViewerColumn_1.setEditingSupport(new JoinMappingEditingSupport(
				mappingTableViewer, JoinMapDialogConstants.OUTPUT_FIELD));
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {

			String tooltipText;

			private List<String> getOutputFieldList(){
				List<String> outputFieldList = new LinkedList<>();
				for(LookupMapProperty lookupMapProperty : mappingTableItemList){
					outputFieldList.add(lookupMapProperty.getOutput_Field());
				}
				return outputFieldList;
			}
			
			@Override
			public String getToolTipText(Object element) {
				tooltipText = null;
				
				int occurrences = Collections.frequency(getOutputFieldList(),
						((LookupMapProperty)element).getOutput_Field());
				if (occurrences > 1) {
					tooltipText = FIELD_TOOLTIP_MESSAGE_DUPLICATE_FIELDS;
				}

				LookupMapProperty lookupMapProperty = (LookupMapProperty) element;
				if (StringUtils.isBlank(lookupMapProperty.getSource_Field()))
					tooltipText = FIELD_TOOLTIP_MESSAGE_FIELD_CANT_BE_EMPTY;

				return tooltipText;
			}

			@Override
			public Color getForeground(Object element) {				
				int occurrences = Collections.frequency(getOutputFieldList(),
						((LookupMapProperty)element).getOutput_Field());
				if (occurrences > 1) {
					return new Color(null, 255, 0, 0);
				} else {
					return super.getForeground(element);
				}

			}

			@Override
			public Color getBackground(Object element) {
				LookupMapProperty lookupMapProperty = (LookupMapProperty) element;

				if (StringUtils.isBlank(lookupMapProperty.getOutput_Field()))
					return new Color(Display.getDefault(), 0xFF, 0xDD, 0xDD);
				else
					return super.getBackground(element);
			}

			@Override
			public String getText(Object element) {
				LookupMapProperty lookupMapProperty = (LookupMapProperty) element;
				
				if(ParameterUtil.isParameter(lookupMapProperty.getOutput_Field()))
					lookupMapProperty.setSource_Field(lookupMapProperty.getOutput_Field());
				
				return lookupMapProperty.getOutput_Field();
			}
		});
	}

	private void createInputFieldColumnInMappingTable() {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				mappingTableViewer, SWT.NONE);
		TableColumn tblclmnPropertyName = tableViewerColumn.getColumn();
		tblclmnPropertyName.setWidth(169);
		tblclmnPropertyName.setText(JoinMapDialogConstants.INPUT_FIELD);
		tableViewerColumn.setEditingSupport(new JoinMappingEditingSupport(
				mappingTableViewer, JoinMapDialogConstants.INPUT_FIELD));

		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			String tooltipText;

			@Override
			public String getToolTipText(Object element) {
				tooltipText = null;
				LookupMapProperty lookupMapProperty = (LookupMapProperty) element;
				if (!allInputFields.contains(lookupMapProperty
						.getSource_Field()) && !ParameterUtil.isParameter(lookupMapProperty.getSource_Field())) {
					tooltipText = FIELD_TOOLTIP_MESSAGE_NO_SUCH_INPUT_FIELD;
				}

				if (StringUtils.isBlank(lookupMapProperty.getSource_Field()))
					tooltipText = FIELD_TOOLTIP_MESSAGE_FIELD_CANT_BE_EMPTY;
				
				return tooltipText;
			}

			@Override
			public Color getForeground(Object element) {
				LookupMapProperty lookupMapProperty = (LookupMapProperty) element;
				if (!allInputFields.contains(lookupMapProperty
						.getSource_Field()) && !ParameterUtil.isParameter(lookupMapProperty.getSource_Field())) {
					return new Color(null, 255, 0, 0);
				} else {
					return super.getForeground(element);
				}

			}

			@Override
			public Color getBackground(Object element) {
				LookupMapProperty lookupMapProperty = (LookupMapProperty) element;

				if (StringUtils.isBlank(lookupMapProperty.getSource_Field()))
					return new Color(Display.getDefault(), 0xFF, 0xDD, 0xDD);
				else
					return super.getBackground(element);
			}

			@Override
			public String getText(Object element) {
				LookupMapProperty lookupMapProperty = (LookupMapProperty) element;
				if(ParameterUtil.isParameter(lookupMapProperty.getSource_Field()))
					lookupMapProperty.setOutput_Field(lookupMapProperty.getSource_Field());
				
				return lookupMapProperty.getSource_Field();
			}

		});
	}

	private void createButtonSectionInFieldMappingSection(Composite composite) {

		Composite mappingHeaderComposite = new Composite(composite, SWT.NONE);
		mappingHeaderComposite.setTouchEnabled(true);
		mappingHeaderComposite.setLayout(new GridLayout(2, false));
		GridData gd_mappingHeaderComposite = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_mappingHeaderComposite.heightHint = 40;
		mappingHeaderComposite.setLayoutData(gd_mappingHeaderComposite);

		Composite outputMappingComposite = new Composite(mappingHeaderComposite, SWT.NONE);
		GridLayout gl_outputMappingComposite = new GridLayout(1, false);
		gl_outputMappingComposite.verticalSpacing = 0;
		gl_outputMappingComposite.marginWidth = 0;
		gl_outputMappingComposite.marginHeight = 0;
		outputMappingComposite.setLayout(gl_outputMappingComposite);
		outputMappingComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				true, 1, 1));

		Label lblMappingView = new Label(outputMappingComposite, SWT.NONE);
		lblMappingView.setText("Output Mapping");

		Composite outputMappingButtonsComposite = new Composite(mappingHeaderComposite, SWT.NONE);
		GridLayout gl_outputMappingButtonsComposite = new GridLayout(5, false);
		gl_outputMappingButtonsComposite.verticalSpacing = 0;
		gl_outputMappingButtonsComposite.marginWidth = 0;
		gl_outputMappingButtonsComposite.marginHeight = 0;
		outputMappingButtonsComposite.setLayout(gl_outputMappingButtonsComposite);
		outputMappingButtonsComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				true, 1, 1));
		
		createPullButton(outputMappingButtonsComposite);

		createAddButton(outputMappingButtonsComposite);

		createDeleteButton(outputMappingButtonsComposite);

		createUpButton(outputMappingButtonsComposite);

		createDownButton(outputMappingButtonsComposite);
	}

	private void createPullButton(Composite composite_11) {
		btnPull = new Button(composite_11, SWT.NONE);
		btnPull.setText(PULL_BUTTON_TEXT);
		btnPull.setToolTipText(PULL_TOOLTIP);
		
		btnPull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog dialog = new MessageDialog(new Shell(), Constants.SYNC_CONFIRM, null, Constants.SYNC_OUTPUT_FIELDS_CONFIRM_MESSAGE, MessageDialog.QUESTION, new String[] {"Ok", "Cancel" }, 0);
				int dialogResult =dialog.open();
				List<LookupMapProperty> pulledLookupMapProperties = null;
				if(dialogResult == 0){
					//syncTransformFieldsWithSchema();
					Schema schema = (Schema) component.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
					pulledLookupMapProperties = SchemaSyncUtility.INSTANCE.pullLookupSchemaInMapping(schema, component);
				}
				mappingTableViewer.setInput(pulledLookupMapProperties);
				mappingTableItemList = pulledLookupMapProperties;
				mappingTableViewer.refresh(); 
				component.setLatestChangesInSchema(false);
				refreshButtonStatus();
			}
		});
	}
	
	private void createDownButton(Composite composite_11) {
		btnDown = new Button(composite_11, SWT.NONE);
		btnDown.setText(DOWN_BUTTON_TEXT);
		btnDown.setToolTipText(DOWN_TOOLTIP);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = mappingTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int i = indexes.length - 1; i > -1; i--) {

					if (indexes[i] < mappingTableItemList.size() - 1) {
						Collections.swap(
								(List<LookupMapProperty>) mappingTableItemList,
								indexes[i], indexes[i] + 1);
						mappingTableViewer.refresh();

					}
				}
				refreshButtonStatus();
			}
		});
	}

	private void createUpButton(Composite composite_11) {
		btnUp = new Button(composite_11, SWT.NONE);
		btnUp.setText(UP_BUTTON_TEXT);
		btnUp.setToolTipText(UP_TOOLTIP);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = mappingTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int index : indexes) {

					if (index > 0) {
						Collections.swap(
								(List<LookupMapProperty>) mappingTableItemList,
								index, index - 1);
						mappingTableViewer.refresh();
					}
				}
				refreshButtonStatus();
			}
		});
	}

	private void createDeleteButton(Composite composite_11) {
		btnDelete = new Button(composite_11, SWT.NONE);
		btnDelete.setText(DELETE_BUTTON_TEXT);
		btnDelete.setToolTipText(DELETE_TOOLTIP);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = mappingTableViewer.getTable();
				int selectionIndex = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				if (selectionIndex == -1) {
					WidgetUtility.errorMessage("Select Rows to delete");
				} else {
					table.remove(indexs);
					int itemsRemoved=0;
					for (int index : indexs) {
						mappingTableItemList.remove(index-itemsRemoved);
						if(index-itemsRemoved-1 != -1){
							table.setSelection(index-itemsRemoved-1);
						}else{
							table.setSelection(0);
						}
						itemsRemoved++;
					}
					mappingTableViewer.refresh();
				}
				component.setLatestChangesInSchema(false);
				refreshButtonStatus();
			}
		});
	}

	private void createAddButton(Composite composite_11) {
		btnAdd = new Button(composite_11, SWT.NONE);
		btnAdd.setText(ADD_BUTTON_TEXT);
		btnAdd.setToolTipText(ADD_TOOLTIP);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LookupMapProperty lookupMapProperty = new LookupMapProperty();
				lookupMapProperty.setOutput_Field("");
				lookupMapProperty.setSource_Field("");
				mappingTableItemList.add(lookupMapProperty);
				mappingTableViewer.refresh();
				mappingTableViewer.editElement(lookupMapProperty, 0);
				component.setLatestChangesInSchema(false);
				refreshButtonStatus();
			}
		});
	}

	private Composite createOuterMostComposite(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		return composite;
	}

	private void createInputFieldSection(Composite composite) {
		
		Composite inputComposite = new Composite(composite, SWT.NONE);
		GridLayout gl_inputComposite = new GridLayout(1, false);
		gl_inputComposite.horizontalSpacing = 0;
		gl_inputComposite.verticalSpacing = 0;
		gl_inputComposite.marginWidth = 0;
		gl_inputComposite.marginHeight = 0;
		inputComposite.setLayout(gl_inputComposite);
		GridData gd_inputComposite = new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1);
		gd_inputComposite.widthHint = 269;
		inputComposite.setLayoutData(gd_inputComposite);
		inputComposite.setBounds(0, 0, 64, 64);

		ScrolledComposite inputScrolledComposite = new ScrolledComposite(
				inputComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		inputScrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		inputScrolledComposite.setExpandHorizontal(true);
		inputScrolledComposite.setExpandVertical(true);

		final SashForm inputComposite2 = new SashForm(inputScrolledComposite, SWT.VERTICAL);
		inputComposite2.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				in0Table.getColumn(0).setWidth(inputComposite2.getSize().x-5);
				in1Table.getColumn(0).setWidth(inputComposite2.getSize().x-5);
			}
		});
		inputComposite2.setLayout(new GridLayout(1, false));
		
		addIn0InputFields(inputComposite2);
		
		addIn1InputFields(inputComposite2);

		inputScrolledComposite.setContent(inputComposite2);
		inputScrolledComposite.setMinSize(inputComposite2.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	private void addIn0InputFields(Composite inputComposite2) {
		List<FilterProperties> inputIn0PortFieldList = null;
		if (lookupMappingGrid != null) {
			if (lookupMappingGrid.getLookupInputProperties() != null
					&& !lookupMappingGrid.getLookupInputProperties()
					.isEmpty()) {

				inputIn0PortFieldList = lookupMappingGrid
						.getLookupInputProperties().get(0);

			} else {
				inputIn0PortFieldList = new ArrayList<>();
			}
		}
		if (inputPorts != null) {
			inputPorts.add(inputIn0PortFieldList);

			for (FilterProperties inputField : inputIn0PortFieldList) {
				allInputFields.add(IN0_PREFIX
						+ inputField.getPropertyname());
			}

		}
		TableViewer in0TableViewer = new TableViewer(inputComposite2, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		in0Table = in0TableViewer.getTable();
		in0Table.setLinesVisible(true);
		in0Table.setHeaderVisible(true);
		in0Table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		in0TableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn in0TableViewerColumn = new TableViewerColumn(
				in0TableViewer, SWT.NONE);
		TableColumn in0TblclmnInputFields = in0TableViewerColumn.getColumn();
		in0TblclmnInputFields.setWidth(230);
		in0TblclmnInputFields.setText(IN0_HEADER);

		in0TableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				FilterProperties tableField = (FilterProperties) element;
				return tableField.getPropertyname();
			}
		});
		in0TableViewer.setInput(inputIn0PortFieldList);
		
		Transfer[] in0Types = new Transfer[] { TextTransfer.getInstance() };
		final String in0PortLabel = IN0_PREFIX;
		int in0Operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		// final Table table = (Table) sourceControl;
		DragSource in0Source = new DragSource(in0Table, in0Operations);
		in0Source.setTransfer(in0Types);
		in0Source.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				// Set the data to be the first selected item's text
				event.data = addDelimeter(in0PortLabel, in0Table.getSelection());
			}
		});
	}

	private void addIn1InputFields(Composite inputComposite2) {
		List<FilterProperties> inputIn1PortFieldList = null;
		if (lookupMappingGrid != null) {
			if (lookupMappingGrid.getLookupInputProperties() != null
					&& !lookupMappingGrid.getLookupInputProperties()
					.isEmpty()) {

				inputIn1PortFieldList = lookupMappingGrid
						.getLookupInputProperties().get(1);

			} else {
				inputIn1PortFieldList = new ArrayList<>();
			}
		}
		
		if (inputPorts != null) {
			inputPorts.add(inputIn1PortFieldList);

			for (FilterProperties inputField : inputIn1PortFieldList) {
				allInputFields.add(IN1_PREFIX
						+ inputField.getPropertyname());
			}

		}
		
		TableViewer in1TableViewer = new TableViewer(inputComposite2, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		in1Table = in1TableViewer.getTable();
		in1Table.setHeaderVisible(true);
		in1Table.setLinesVisible(true);
		in1Table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		in1TableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn in1TableViewerColumn = new TableViewerColumn(
				in1TableViewer, SWT.NONE);
		TableColumn in1TblclmnInputFields = in1TableViewerColumn.getColumn();
		in1TblclmnInputFields.setWidth(225);
		in1TblclmnInputFields.setText(IN1_HEADER);

		in1TableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				FilterProperties tableField = (FilterProperties) element;
				return tableField.getPropertyname();
			}
		});
		
		in1TableViewer.setInput(inputIn1PortFieldList);
		Transfer[] in1Types = new Transfer[] { TextTransfer.getInstance() };
		final String in1PortLabel = IN1_PREFIX;
		int in1Operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		// final Table table = (Table) sourceControl;
		DragSource in1Source = new DragSource(in1Table, in1Operations);
		in1Source.setTransfer(in1Types);
		in1Source.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				// Set the data to be the first selected item's text
				event.data = addDelimeter(in1PortLabel, in1Table.getSelection());
			}
		});
	}
	

	private String addDelimeter(String portLabel, TableItem[] selectedTableItems) {
		StringBuffer buffer = new StringBuffer();
		for (TableItem tableItem : selectedTableItems) {
			buffer.append(portLabel + tableItem.getText() + MAPPING_TABLE_ITEM_DELIMILATOR);
		}
		return buffer.toString();
	}
	

	/**
	 * Create contents of the button bar.
	 * 
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

	@Override
	protected void okPressed() {
		lookupMappingGrid.setLookupInputProperties(inputPorts);
		lookupMappingGrid.setLookupMapProperties(mappingTableItemList);
		
		populateCurrentItemsOfTable();

		if (!oldLookupMappingGrid.equals(newLookupMappingGrid)) {
			propertyDialogButtonBar.enableApplyButton(true);
		}
		super.okPressed();
	}

	private void populateLookupMapDialog() {

		if (lookupMappingGrid.getLookupMapProperties() != null
				&& !lookupMappingGrid.getLookupMapProperties().isEmpty()) {
			mappingTableItemList = lookupMappingGrid.getLookupMapProperties();
		} else {
			mappingTableItemList = new ArrayList<>();
		}

		mappingTableViewer.setInput(mappingTableItemList);

		mappingTableViewer.refresh();

		populatePreviousItemsOfTable();
		refreshButtonStatus();

	}

	private void populatePreviousItemsOfTable() {
		oldLookupMappingGrid = lookupMappingGrid.clone();
	}

	private void populateCurrentItemsOfTable() {
		newLookupMappingGrid = lookupMappingGrid.clone();
	}
	
	
	private void refreshButtonStatus(){
		if(mappingTableItemList.size()>=1){
			btnDelete.setEnabled(true);
		}
		
		if(mappingTableItemList.size()>1){
			btnDown.setEnabled(true);
			btnUp.setEnabled(true);
		}
		if(mappingTableItemList.size() == 0){
			btnDelete.setEnabled(false);
			btnDown.setEnabled(false);
			btnUp.setEnabled(false);
		}
		
	}
}
