package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ImagePathConstant;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.fixedwidthschema.ELTFixedWidget;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTRadioButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTTable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTTableViewer;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTSchemaSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTSchemaTableComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDetails;
import com.bitwise.app.propertywindow.widgets.listeners.grid.GridChangeListener;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The Class ELTSchemaGridWidget.
 * 
 * @author Bitwise
 */
public abstract class ELTSchemaGridWidget extends AbstractWidget {

	private static Logger logger = LogFactory.INSTANCE.getLogger(ELTSchemaGridWidget.class);

	public static final String FIELDNAME = Messages.FIELDNAME;
	public static final String DATEFORMAT = Messages.DATEFORMAT;
	public static final String DATATYPE = Messages.DATATYPE;
	public static final String PRECISION = Messages.PRECISION;
	public static final String SCALE = Messages.SCALE;
	public static final String SCALE_TYPE = Messages.SCALE_TYPE;
	public static final String FIELD_DESCRIPTION = Messages.FIELD_DESCRIPTION;
	public static final String LENGTH = Messages.LENGTH;

	public static final String RANGE_FROM = "Range From";
	public static final String RANGE_TO = "Range To";

	public static final String DEFAULT_VALUE ="Default Value";

	protected String gridRowType;

	
	private CellEditor fieldNameEditor;

	

	protected ControlDecoration fieldNameDecorator;
	protected ControlDecoration isFieldNameAlphanumericDecorator;
	protected ControlDecoration scaleDecorator;
	protected ControlDecoration lengthDecorator;
	protected ControlDecoration rangeFromDecorator;
	protected ControlDecoration rangeToDecorator;
	protected TableViewer tableViewer;
	protected List<GridRow> schemaGridRowList = new ArrayList<GridRow>();
	protected CellEditor[] editors;
	protected Table table;

	protected GridWidgetCommonBuilder gridWidgetBuilder = getGridWidgetBuilder();
	protected final String[] PROPS = getPropertiesToShow();
	private boolean isExternal;
	private Object properties;
	private String propertyName;
	private ListenerHelper helper;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private ELTDefaultLable upButton, downButton, addButton, deleteButton, importButton;

	private Button browseButton;
	AbstractELTWidget internalSchema, externalSchema;
	private Text extSchemaPathText;

	private ControlDecoration txtDecorator, decorator;

	protected abstract String[] getPropertiesToShow();

	protected abstract GridWidgetCommonBuilder getGridWidgetBuilder();

	protected abstract IStructuredContentProvider getContentProvider();

	protected abstract ITableLabelProvider getLableProvider();

	protected abstract ICellModifier getCellModifier();

	/**
	 * Adds the validators.
	 */
	protected abstract void addValidators();

	/**
	 * Sets the decorator.
	 */
	protected abstract void setDecorator();

	public ELTSchemaGridWidget() {
	}

	/**
	 * Instantiates a new ELT schema grid widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configuration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTSchemaGridWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);

		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.properties = componentConfigrationProperty.getPropertyValue();
	}

	private List<String> getSchemaFields(List<GridRow> schemaGridRowList2) {
		List<String> schemaFields = new LinkedList<>();
		if (schemaGridRowList2 != null) {
			for (GridRow gridRow : schemaGridRowList2) {
				GridRow fixedWidthGridRow = (GridRow) gridRow;
				schemaFields.add(fixedWidthGridRow.getFieldName());
			}
		}
		return schemaFields;
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		Map<String, ComponentsOutputSchema> schemaMap = new LinkedHashMap<String, ComponentsOutputSchema>();
		ComponentsOutputSchema componentsOutputSchema = new ComponentsOutputSchema();
		if (getComponent().getProperties().get(Constants.SCHEMA_TO_PROPAGATE) != null) {

			ComponentsOutputSchema previousOutputSchema = ((Map<String, ComponentsOutputSchema>) getComponent()
					.getProperties().get(Constants.SCHEMA_TO_PROPAGATE)).get(Constants.FIXED_OUTSOCKET_ID);

			if (previousOutputSchema != null && !previousOutputSchema.getMapFields().isEmpty())
				componentsOutputSchema.getMapFields().putAll(previousOutputSchema.getMapFields());
			if (previousOutputSchema != null && !previousOutputSchema.getPassthroughFields().isEmpty())
				componentsOutputSchema.getPassthroughFields().addAll(previousOutputSchema.getPassthroughFields());
			if (previousOutputSchema != null && !previousOutputSchema.getPassthroughFieldsPortInfo().isEmpty())
				componentsOutputSchema.getPassthroughFieldsPortInfo().putAll(
						previousOutputSchema.getPassthroughFieldsPortInfo());
			if (previousOutputSchema != null && !previousOutputSchema.getMapFieldsPortInfo().isEmpty())
				componentsOutputSchema.getMapFieldsPortInfo().putAll(previousOutputSchema.getMapFieldsPortInfo());
		}
		List<GridRow> tempGrid = new ArrayList<>();
		List<String> oprationFieldList = getOperationFieldList();
		if (schemaGridRowList != null) {

			if (getSchemaForInternalPapogation() != null) {
				Schema internalSchema = getSchemaForInternalPapogation().clone();
				List<String> schemaFields = getSchemaFields(schemaGridRowList);
				for (GridRow internalSchemaRow : internalSchema.getGridRow()) {
					int index = 0;
					if (schemaFields.contains(internalSchemaRow.getFieldName())) {
						for (Object schemaGridRow : schemaGridRowList) {
							if (internalSchemaRow.getFieldName().equals(((GridRow) schemaGridRow).getFieldName())) {
								if (!oprationFieldList.contains(internalSchemaRow.getFieldName()))
									schemaGridRowList.set(index, internalSchemaRow.copy());
							}
							index++;
						}
					} else {
						schemaGridRowList.add(internalSchemaRow.copy());
					}
				}
			}
			if (!schemaGridRowList.isEmpty()) {
				for (GridRow gridRow : (List<GridRow>) schemaGridRowList) {
					tempGrid.add(gridRow.copy());
					componentsOutputSchema.addSchemaFields(gridRow);
				}
			}
		}

		Schema schema = new Schema();
		if (isExternal) {
			schema.setIsExternal(true);

			schema.setGridRow(new ArrayList());
			schema.setExternalSchemaPath(extSchemaPathText.getText());

		} else {
			schema.setIsExternal(false);
			schema.setGridRow(tempGrid);
			schema.setExternalSchemaPath("");
			schemaMap.put(Constants.FIXED_OUTSOCKET_ID, componentsOutputSchema);
			property.put(Constants.SCHEMA_TO_PROPAGATE, schemaMap);
		}

		property.put(propertyName, schema);
		SchemaPropagation.INSTANCE.continuousSchemaPropagation(getComponent(), schemaMap);//

		return property;
	}

	// Operational class label.
	AbstractELTWidget fieldError = new ELTDefaultLable(Messages.FIELDNAMEERROR).lableWidth(250);

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		createSchemaTypesSection(container.getContainerControl());
		createSchemaGridSection(container.getContainerControl());
		createExternalSchemaSection(container.getContainerControl());
		populateSchemaTypeWidget();
	}

	/**
	 * 
	 * returns propagated schema
	 * 
	 * @param {@link Link}
	 * @return {@link Schema}
	 */
	protected Schema getPropagatedSchema(Link link) {
		ComponentsOutputSchema componentsOutputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
		Schema schema = null;
		schema = new Schema();
		schema.setExternalSchemaPath("");
		schema.setIsExternal(false);
		schema.setGridRow(new ArrayList<GridRow>());
		if (componentsOutputSchema != null) {
			if (this.getClass().isAssignableFrom(ELTFixedWidget.class)) {
				for (FixedWidthGridRow gridRow : componentsOutputSchema.getFixedWidthGridRowsOutputFields()) {
					schema.getGridRow().add(gridRow);
				}
			} else if (this.getClass().equals(ELTGenericSchemaGridWidget.class)) {
				for (FixedWidthGridRow gridRow : componentsOutputSchema.getFixedWidthGridRowsOutputFields()) {
					schema.getGridRow().add(componentsOutputSchema.convertFixedWidthSchemaToSchemaGridRow(gridRow));
				}
			}
		}
		return schema;
	}

	protected void schemaFromConnectedLinks() {
		for (Link link : getComponent().getTargetConnections()) {
			this.properties = getPropagatedSchema(link);

		}
	}

	// Adds the browse button
	private void createExternalSchemaSection(Composite containerControl) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(containerControl);
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable(Messages.EXTERNAL_SCHEMA);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		AbstractELTWidget eltDefaultTextBox = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(200);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultTextBox);

		extSchemaPathText = (Text) eltDefaultTextBox.getSWTWidgetControl();
		extSchemaPathText.setToolTipText(Messages.CHARACTERSET);
		decorator = WidgetUtility.addDecorator(extSchemaPathText, Messages.EMPTYFIELDMESSAGE);
		decorator.hide();
		extSchemaPathText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (extSchemaPathText.getText().isEmpty()) {
					decorator.show();
					extSchemaPathText.setBackground(new Color(Display.getDefault(), 250, 250, 250));
				} else {
					decorator.hide();
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				decorator.hide();
				extSchemaPathText.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			}
		});

		AbstractELTWidget eltDefaultButton = new ELTDefaultButton(Messages.BROWSE_BUTTON).buttonWidth(35);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		browseButton = (Button) eltDefaultButton.getSWTWidgetControl();

		browseButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				decorator.hide();
				extSchemaPathText.setBackground(new Color(Display.getDefault(), 255, 255, 255));

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing to Do
			}

		});


		txtDecorator = WidgetUtility.addDecorator(extSchemaPathText, Messages.CHARACTERSET);
		txtDecorator.setMarginWidth(3);
		decorator.setMarginWidth(3);

		txtDecorator.hide();

		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);

		try {
			eltDefaultTextBox.attachListener(ListenerFactory.Listners.EVENT_CHANGE.getListener(),
					propertyDialogButtonBar, null, eltDefaultTextBox.getSWTWidgetControl());
			eltDefaultTextBox.attachListener(ListenerFactory.Listners.MODIFY.getListener(), propertyDialogButtonBar,
					helper, eltDefaultTextBox.getSWTWidgetControl());
			eltDefaultButton.attachListener(ListenerFactory.Listners.SCHEMA_DIALOG_SELECTION.getListener(),
					propertyDialogButtonBar, helper, eltDefaultButton.getSWTWidgetControl(),
					eltDefaultTextBox.getSWTWidgetControl());

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		populateWidgetExternalSchema();

	}

	// Adds the Radio buttons
	private void createSchemaTypesSection(Composite containerControl) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(containerControl);
		eltSuDefaultSubgroupComposite.createContainerWidget();
		eltSuDefaultSubgroupComposite.numberOfBasicWidgets(4);

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable(Messages.SCHEMA_TYPES);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		// Radio button listener
		internalSchema = new ELTRadioButton(Messages.INTERNAL_SCHEMA_TYPE);
		eltSuDefaultSubgroupComposite.attachWidget(internalSchema);
		((Button) internalSchema.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				propertyDialogButtonBar.enableApplyButton(true);
				toggleTextBox(false);
				//toggleTable(true);
				isExternal = false;
				decorator.hide();
				txtDecorator.hide();
			}
		});

		externalSchema = new ELTRadioButton(Messages.EXTERNAL_SCHEMA_TYPE);
		eltSuDefaultSubgroupComposite.attachWidget(externalSchema);
		((Button) externalSchema.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				propertyDialogButtonBar.enableApplyButton(true);
				toggleTextBox(true);
				//toggleTable(false);
				isExternal = true;

				if (extSchemaPathText.getText().isEmpty()) {
					decorator.show();
				} else {
					decorator.hide();
				}
			}
		});

		populateSchemaTypeWidget();
	}

	private void swap(int index1, int index2, String text1, String text2) {
		GridRow swap1 = null;
		GridRow swap2 = null;
		for (int i = 0; i < schemaGridRowList.size(); i++) {
			GridRow grid = (GridRow) schemaGridRowList.get(i);
			if (grid.getFieldName().equalsIgnoreCase(text1)) {
				swap1 = grid;
			}
			if (grid.getFieldName().equalsIgnoreCase(text2)) {
				swap2 = grid;
			}
		}

		schemaGridRowList.set(index2, swap1);
		schemaGridRowList.set(index1, swap2);
	}

	private void gridListener(CellEditor[] cellEditors) {

		GridChangeListener gridChangeListener = new GridChangeListener(cellEditors, propertyDialogButtonBar);
		gridChangeListener.attachCellChangeListener();
	}

	private void populateWidget() {
		if (this.properties != null) {
			Schema schema = (Schema) this.properties;

			if (!schema.getIsExternal()) {
				if (tableViewer != null) {
					schemaGridRowList = schema.getGridRow();
					tableViewer.setInput(schemaGridRowList);
					tableViewer.refresh();
					isExternal = false;
					toggleTextBox(true);
				}
			}
		}
	}

	private void populateWidgetExternalSchema() {
		if (this.properties != null) {
			Schema schema = (Schema) this.properties;
			if (schema.getIsExternal()) {
				if (extSchemaPathText != null) {
					extSchemaPathText.setText(schema.getExternalSchemaPath());
					schemaGridRowList = schema.getGridRow();
					tableViewer.setInput(schemaGridRowList);
					tableViewer.refresh();
					decorator.hide();
					isExternal = true;
					toggleTable(false);
					toggleTextBox(true);
				}
			}
		}
	}

	private void populateSchemaTypeWidget() {
		if (this.properties != null) {
			Schema schema = (Schema) this.properties;
			if (schema.getIsExternal()) {
				toggleRadioButton(true);
			} else {
				toggleRadioButton(false);
				toggleTextBox(false);
			}
		} else {
			toggleRadioButton(false);
			toggleTable(true);
			toggleTextBox(false);
		}
	}

	private void toggleTable(boolean enableInternalSchemaTable) {
		if (table != null) {
			table.setEnabled(enableInternalSchemaTable);
			/*
			 * addButton.setEnabled(enableInternalSchemaTable); deleteButton.setEnabled(enableInternalSchemaTable);
			 * upButton.setEnabled(enableInternalSchemaTable); downButton.setEnabled(enableInternalSchemaTable);
			 */
		}
	}

	private void toggleRadioButton(boolean enableExternalSchemaRadioButton) {
		((Button) externalSchema.getSWTWidgetControl()).setSelection(enableExternalSchemaRadioButton);
		((Button) internalSchema.getSWTWidgetControl()).setSelection(!enableExternalSchemaRadioButton);
	}

	private void toggleTextBox(boolean enableExternalSchemaTextBox) {
		if (extSchemaPathText != null && browseButton != null) {
			extSchemaPathText.setEnabled(enableExternalSchemaTextBox);
			browseButton.setEnabled(enableExternalSchemaTextBox);
		}
	}

	private ListenerHelper getListenerHelper() {
		if (helper == null) {
			helper = new ListenerHelper();
			if (this.properties != null) {
				Schema schema = (Schema) this.properties;
				schemaGridRowList = schema.getGridRow();
			}
			ELTGridDetails value = new ELTGridDetails(schemaGridRowList, tableViewer,
					(Label) fieldError.getSWTWidgetControl(), gridWidgetBuilder);
			helper.put(HelperType.SCHEMA_GRID, value);

		}
		return helper;
	}

	/**
	 * 
	 * Creates schema grid section
	 * 
	 * @param {@link Composite}
	 * @return {@link TableViewer}
	 */
	public TableViewer createSchemaGridSection(Composite container) {

		ELTSchemaSubgroupComposite buttonSubGroup = new ELTSchemaSubgroupComposite(container);
		
		buttonSubGroup.createContainerWidget();
		buttonSubGroup.numberOfBasicWidgets(5);
		

		importButton = new ELTDefaultLable("");
		importButton.lableWidth(25);
		buttonSubGroup.attachWidget(importButton);
		importButton.setImage(XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/import_schema.png");

		importButton.addMouseUpListener(new MouseListener() {
			int index = 0, index2 = 0;

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
//				extSchemaPathText.getText();
//				System.out.println("Path set: "+extSchemaPathText.getText());
//				File schemaFile = new File(extSchemaPathText.getText());
//				Fields fields;
//				ArrayList schemaGridRowListToImport = new ArrayList<>();
//				
//				try {
//					JAXBContext jaxbContext = JAXBContext.newInstance(Fields.class);
//					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//					fields = (Fields) jaxbUnmarshaller.unmarshal(schemaFile);
//					ArrayList<Field> fieldsList = (ArrayList<Field>) fields.getField();
//					
//					if(gridRowType.equals("Generic")){
//						schemaGridRowListToImport = new ArrayList<SchemaGrid>();
//						
//						for (Field temp : fieldsList) {
//							SchemaGrid gridRow = new SchemaGrid();
//							gridRow.setFieldName(temp.getName());
//							gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value()));
//							gridRow.setDateFormat(temp.getFormat());
//							gridRow.setPrecision(String.valueOf(temp.getPrecision()));
//							gridRow.setScale(String.valueOf(temp.getScale()));
//							//gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value()));	
//							gridRow.setDescription(temp.getDescription());
//							schemaGridRowListToImport.add(gridRow);
//						}
//						
//					}else if(gridRowType.equals("FixedWidth")){
//						schemaGridRowListToImport = new ArrayList<FixedWidthGridRow>();
//						
//						for (Field temp : fieldsList) {
//							FixedWidthGridRow gridRow = new FixedWidthGridRow();
//							gridRow.setFieldName(temp.getName());
//							gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value()));
//							gridRow.setDateFormat(temp.getFormat());
//							gridRow.setPrecision(String.valueOf(temp.getPrecision()));
//							gridRow.setScale(String.valueOf(temp.getScale()));
//							//gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value()));
//							gridRow.setDescription(temp.getDescription());
//							schemaGridRowListToImport.add(gridRow);
//						}
//					}
//					
//					
//				} catch (JAXBException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//				
//				tableViewer.setInput(schemaGridRowListToImport);
//				tableViewer.refresh();

			
				extSchemaPathText.getText();
				System.out.println("Path set: "+extSchemaPathText.getText());
				File schemaFile = new File(extSchemaPathText.getText());
				
				String content = null;
				try {
					content = new Scanner(schemaFile).useDelimiter("\\Z").next();
					System.out.println(content);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				//StringBuilder xmlStr = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				
				//xmlStr.append(content);
				//-------------------------------
				final String xmlStrtemp = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+
						"<data><employee><name>A</name>"
				        + "<title>Manager</title></employee>" 
				        + "<employee><name>B</name>"
				        + "<title>Developer</title></employee>"
				        + "</data>";
				//--------------------------------------------------------
				
		        DocumentBuilder db;
				try {
					db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					InputSource is = new InputSource();
					
			        /*is.setCharacterStream(new StringReader(xmlStrtemp));
			        Document doc;
			        doc = db.parse(is);
			        NodeList nodes = doc.getElementsByTagName("employee");
		         
		            for (int i = 0; i < nodes.getLength(); i++) {
		                Element element = (Element) nodes.item(i);

		                NodeList name = element.getElementsByTagName("name");
		                Element line = (Element) name.item(0);
		                System.out.println("Name: " + getCharacterDataFromElement(line));

		                NodeList title = element.getElementsByTagName("title");
		                line = (Element) title.item(0);
		                System.out.println("Title: " + getCharacterDataFromElement(line));
		              }*/
					
			        is.setCharacterStream(new StringReader(content));
			        Document doc;
			        doc = db.parse(is);
		            NodeList nodes = doc.getElementsByTagName("employee");
		            for (int i = 0; i < nodes.getLength(); i++) {
		                Element element = (Element) nodes.item(i);

		                NodeList name = element.getElementsByTagName("name");
		                Element line = (Element) name.item(0);
		                System.out.println("Name: " + getCharacterDataFromElement(line));

		                NodeList title = element.getElementsByTagName("title");
		                line = (Element) title.item(0);
		                System.out.println("Title: " + getCharacterDataFromElement(line));
		              }

		        } catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			
		        } catch (Exception exc) {  
		            exc.printStackTrace();  
		        } 
				
			}
		});
		
		downButton = new ELTDefaultLable("");
		downButton.lableWidth(25);
		buttonSubGroup.attachWidget(downButton);
		downButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEDOWN_BUTTON);

		downButton.addMouseUpListener(new MouseAdapter() {
			int index = 0, index2 = 0;

			@Override
			public void mouseUp(MouseEvent e) {
				
				propertyDialogButtonBar.enableApplyButton(true);
				index = table.getSelectionIndex();

				if (index < schemaGridRowList.size() - 1) {
					String text1 = tableViewer.getTable().getItem(index).getText(0);
					index2 = index + 1;
					String text2 = tableViewer.getTable().getItem(index2).getText(0);

					swap(index, index2, text1, text2);
					tableViewer.refresh();
					table.setSelection(index + 1);
				}
				
			}
		});

		upButton = new ELTDefaultLable("");
		upButton.lableWidth(25);
		buttonSubGroup.attachWidget(upButton);
		upButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEUP_BUTTON);
		upButton.addMouseUpListener(new MouseAdapter() {
			int index = 0, index2 = 0;

			@Override
			public void mouseUp(MouseEvent e) {
				propertyDialogButtonBar.enableApplyButton(true);
				index = table.getSelectionIndex();

				if (index > 0) {
					index2 = index - 1;
					String text1 = tableViewer.getTable().getItem(index).getText(0);
					String text2 = tableViewer.getTable().getItem(index2).getText(0);

					swap(index, index2, text1, text2);

					tableViewer.refresh();
					table.setSelection(index - 1);

				}
			}
		});

		deleteButton = new ELTDefaultLable("");
		deleteButton.lableWidth(25);
		buttonSubGroup.attachWidget(deleteButton);
		deleteButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DELETE_BUTTON);

		addButton = new ELTDefaultLable("");
		addButton.lableWidth(25);
		buttonSubGroup.attachWidget(addButton);
		addButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.ADD_BUTTON);

		ELTSchemaTableComposite gridSubGroup = new ELTSchemaTableComposite(container);
		gridSubGroup.createContainerWidget();

		AbstractELTWidget eltTableViewer = new ELTTableViewer(getContentProvider(), getLableProvider());
		gridSubGroup.attachWidget(eltTableViewer);

		tableViewer = (TableViewer) eltTableViewer.getJfaceWidgetControl();
		tableViewer.setInput(schemaGridRowList);
		// Set the editors, cell modifier, and column properties
		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(getCellModifier());
		ELTTable eltTable = new ELTTable(tableViewer);
		gridSubGroup.attachWidget(eltTable);
		table = (Table) eltTable.getSWTWidgetControl();
		// Create Table column
		WidgetUtility.createTableColumns(table, PROPS);
		// Set up the table
		for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
			table.getColumn(columnIndex).pack();
			table.getColumn(columnIndex).setWidth(94);
		}
		editors = gridWidgetBuilder.createCellEditorList(table, PROPS.length);
		tableViewer.setCellEditors(editors);

		// enables the tab functionality
		TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer),
				ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);

		// Adding the decorator to show error message when field name same.
		setDecorator();
		helper = getListenerHelper();
		addValidators();
		try {
			eltTable.attachListener(ListenerFactory.Listners.GRID_MOUSE_DOUBLE_CLICK.getListener(),
					propertyDialogButtonBar, helper, table);
			eltTable.attachListener(ListenerFactory.Listners.GRID_MOUSE_DOWN.getListener(), propertyDialogButtonBar,
					helper, editors[0].getControl());
			addButton.attachListener(ListenerFactory.Listners.GRID_ADD_SELECTION.getListener(),
					propertyDialogButtonBar, helper, table);
			deleteButton.attachListener(ListenerFactory.Listners.GRID_DELETE_SELECTION.getListener(),
					propertyDialogButtonBar, helper, table);

		} catch (Exception e) {
			logger.debug("Failed to attach listeners to table",e);
			throw new RuntimeException("Failed to attach listeners to table");
		}

		gridListener(editors);

		populateWidget();
		return tableViewer;
	}


	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	  }
	
	private String readFile(String path, Charset encoding){
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
	public List getSchemaGridRowList() {

		return schemaGridRowList;
	}

	public void setSchemaGridRowList(List<GridRow> schemaGridRowList) {
		this.schemaGridRowList = schemaGridRowList;
	}

	@Override
	public void refresh() {

		Schema schema = getSchemaForInternalPapogation();
		if (this.properties != null) {
			Schema originalSchema = (Schema) this.properties;
			List<GridRow> existingFields = getExitingSchemaFields(originalSchema);

			List<String> existingFieldNames = getExitingSchemaFieldNames(originalSchema);

			List<String> operationFieldList = getOperationFieldList();
			for (GridRow row : schema.getGridRow()) {
				if (existingFieldNames.contains(row.getFieldName().trim())) {
					if (existingFields.contains(row)) {
						for (int index = 0; index < originalSchema.getGridRow().size(); index++) {
							if (originalSchema.getGridRow().get(index).getFieldName().equals(row.getFieldName().trim())) {
								if (!operationFieldList.contains(row.getFieldName()))
									originalSchema.getGridRow().set(index, row.copy());
							}
						}
					}
				} else {
					originalSchema.getGridRow().add(row.copy());
				}
			}
			table.clearAll();
			if (!originalSchema.getIsExternal()) {
				if (tableViewer != null) {
					schemaGridRowList = originalSchema.getGridRow();
					tableViewer.setInput(schemaGridRowList);
					tableViewer.refresh();
					isExternal = false;
					toggleTextBox(true);
				}
			}

		} else {
			if (schema.getGridRow().size() != 0) {
				table.clearAll();
				if (!schema.getIsExternal()) {
					if (tableViewer != null) {
						schemaGridRowList = schema.getGridRow();
						tableViewer.setInput(schemaGridRowList);
						tableViewer.refresh();
						isExternal = false;
						toggleTextBox(true);
					}
				}
			}
		}
	}

	private List<String> getExitingSchemaFieldNames(Schema originalSchema) {
		List<String> list = new ArrayList<>();
		for (GridRow row : originalSchema.getGridRow()) {
			list.add(row.getFieldName());
		}
		return list;
	}

	private List<GridRow> getExitingSchemaFields(Schema originalSchema) {
		List<GridRow> list = new ArrayList<>();

		for (GridRow row : originalSchema.getGridRow()) {
			list.add((GridRow) row.copy());
		}
		return list;
	}
	
}
