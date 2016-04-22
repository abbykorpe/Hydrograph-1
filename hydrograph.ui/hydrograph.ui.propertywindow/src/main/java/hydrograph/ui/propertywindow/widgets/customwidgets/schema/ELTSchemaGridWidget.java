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

 
package hydrograph.ui.propertywindow.widgets.customwidgets.schema;

import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GenerateRecordSchemaGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.fixedwidthschema.ELTFixedWidget;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTRadioButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTTable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTTableViewer;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTSchemaSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTSchemaTableComposite;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTGridDetails;
import hydrograph.ui.propertywindow.widgets.listeners.grid.GridChangeListener;
import hydrograph.ui.propertywindow.widgets.utility.GridComparator;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import hydrograph.ui.datastructure.property.MixedSchemeGridRow;
import hydrograph.ui.propertywindow.filemixedschema.ELTMixedSchemeWidget;
import org.slf4j.Logger;


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
	public static final String DELIMITER = Messages.DELIMITER;

	public static final String RANGE_FROM = Messages.RANGE_FROM;
	public static final String RANGE_TO = Messages.RANGE_TO;
	public static final String DEFAULT_VALUE =Messages.DEFAULT_VALUE;

	protected boolean transformSchemaType=false;

	protected String gridRowType;

	protected ControlDecoration fieldNameDecorator;
	protected ControlDecoration isFieldNameAlphanumericDecorator;
	protected ControlDecoration scaleDecorator;
	protected ControlDecoration lengthDecorator;
	protected ControlDecoration delimiterDecorator;
	protected ControlDecoration rangeFromDecorator;
	protected ControlDecoration rangeToDecorator;
	protected TableViewer tableViewer;
	protected List<GridRow> schemaGridRowList = new ArrayList<GridRow>();
	protected CellEditor[] editors;
	protected Table table;

	protected GridWidgetCommonBuilder gridWidgetBuilder = getGridWidgetBuilder();
	protected final String[] PROPS = getPropertiesToShow();
	private boolean external;
	private Object properties;
	private String propertyName;
	private ListenerHelper helper;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();


	private ELTDefaultLable upButton, downButton, addButton, deleteButton;

	private Button browseButton, importButton, exportButton;
	
	private MenuItem copyMenuItem, pasteMenuItem;

	AbstractELTWidget internalSchema, externalSchema;
	private Text extSchemaPathText;
	public final static String SCHEMA_CONFIG_XSD_PATH = Platform.getInstallLocation().getURL().getPath() + Messages.SCHEMA_CONFIG_XSD_PATH;

	private ControlDecoration txtDecorator, decorator;

	private String addButtonTooltip = Messages.ADD_SCHEMA_TOOLTIP;
	private String removeButtonTooltip = Messages.DELETE_SCHEMA_TOOLTIP;
	private String upButtonTooltip = Messages.MOVE_SCHEMA_UP_TOOLTIP;
	private String downButtonTooltip = Messages.MOVE_SCHEMA_DOWN_TOOLTIP;
	
	List<GridRow> copiedGridRows=new ArrayList<GridRow>();

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
	//protected abstract void setDecorator(ListenerHelper helper);
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
				if (fixedWidthGridRow != null) {
					schemaFields.add(fixedWidthGridRow.getFieldName());
				}
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

		if (schemaGridRowList != null ) {
			if(!Constants.TRANSFORM.equalsIgnoreCase(getComponent().getComponentName()) && !Constants.AGGREGATE.equalsIgnoreCase(getComponent().getComponentName()))
			if(getSchemaForInternalPapogation()!=null){

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
					if (gridRow != null) {
						tempGrid.add(gridRow.copy());
						componentsOutputSchema.addSchemaFields(gridRow);
					}
				}
			}
		}

		if (schemaGridRowList != null && schemaGridRowList.size()>0 && !isSchemaInSync()){
			MessageDialog dialog = new MessageDialog(new Shell(), Constants.SYNC_WARNING, null, 
					 Constants.SCHEMA_NOT_SYNC_MESSAGE, MessageDialog.CONFIRM, 
	                new String[] { Messages.SYNC_NOW, Messages.LATER }, 0);
			int dialogResult =dialog.open();
			if(dialogResult == 0){
				syncSchemaFromTransform();
				if (!schemaGridRowList.isEmpty()) {
					for (GridRow gridRow : (List<GridRow>) schemaGridRowList) {
						if (gridRow != null) {
							tempGrid.add(gridRow.copy());
							componentsOutputSchema.addSchemaFields(gridRow);
						}
					}
				}
			}
			
		}
		Schema schema = new Schema();
		schema.setGridRow(tempGrid);
		if (external) {
			schema.setIsExternal(true);
			schema.setExternalSchemaPath(extSchemaPathText.getText());

		} else {
			schema.setIsExternal(false);
			schema.setExternalSchemaPath("");
			toggleSchema(false);

			//			schemaMap.put(Constants.FIXED_OUTSOCKET_ID, componentsOutputSchema);
			//			property.put(Constants.SCHEMA_TO_PROPAGATE,schemaMap);
		}

		schemaMap.put(Constants.FIXED_OUTSOCKET_ID, componentsOutputSchema);
		property.put(Constants.SCHEMA_TO_PROPAGATE,schemaMap);

		property.put(propertyName, schema);
		SchemaPropagation.INSTANCE.continuousSchemaPropagation(getComponent(), schemaMap);//

		return property;
	}
	
	@Override
	public boolean verifySchemaFile(){
		boolean verifiedSchema=true;
		if(external){
			verifiedSchema=verifyExtSchemaSync(extSchemaPathText.getText(), schemaGridRowList);
		}
		return verifiedSchema;
	}

	private boolean verifyExtSchemaSync(String extSchemaPath, List<GridRow> schemaInGrid) {
		List<GridRow> schemasFromFile = new ArrayList<GridRow>();
		File schemaFile = new File(extSchemaPath);
		InputStream xml, xsd;
		Fields fields;
		boolean verifiedSchema = true;
		try {
			xml = new FileInputStream(extSchemaPath);
			xsd = new FileInputStream(SCHEMA_CONFIG_XSD_PATH);

			if(validateXML(xml, xsd)){
				JAXBContext jaxbContext;
				try {
					jaxbContext = JAXBContext.newInstance(hydrograph.ui.common.schema.Schema.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					
					hydrograph.ui.common.schema.Schema schema= 
							(hydrograph.ui.common.schema.Schema) jaxbUnmarshaller.unmarshal(schemaFile);
					fields = schema.getFields();
					ArrayList<Field> fieldsList = (ArrayList<Field>) fields.getField();
					GridRow gridRow = null;
					
					if(Messages.GENERIC_GRID_ROW.equals(gridRowType)){

						for (Field temp : fieldsList) {
							gridRow = new BasicSchemaGridRow();
							populateCommonFields(gridRow, temp);
							schemasFromFile.add(gridRow);
						}	
						
					}else if(Messages.FIXEDWIDTH_GRID_ROW.equals(gridRowType)){

						for (Field temp : fieldsList) {
							gridRow = new FixedWidthGridRow();
							populateCommonFields(gridRow, temp);

							if(temp.getLength()!=null)
								((FixedWidthGridRow) gridRow).setLength(String.valueOf(temp.getLength()));
							else
								((FixedWidthGridRow) gridRow).setLength("");
							schemasFromFile.add(gridRow);
						}
					}else if(Messages.MIXEDSCHEME_GRID_ROW.equals(gridRowType)){

						for (Field temp : fieldsList) {
							gridRow = new MixedSchemeGridRow();
							populateCommonFields(gridRow, temp);
							if(temp.getLength()!=null)
								((MixedSchemeGridRow) gridRow).setLength(String.valueOf(temp.getLength()));
							else
								((MixedSchemeGridRow) gridRow).setLength("");
							((MixedSchemeGridRow) gridRow).setDelimiter(temp.getDelimiter());
							schemasFromFile.add(gridRow);
						}
					}else if(Messages.GENERATE_RECORD_GRID_ROW.equals(gridRowType)){

						for (Field temp : fieldsList) {
							gridRow = new GenerateRecordSchemaGridRow();
							populateCommonFields(gridRow, temp);

							if(temp.getLength()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setLength(String.valueOf(temp.getLength()));
							else
								((GenerateRecordSchemaGridRow) gridRow).setLength("");

							if(temp.getDefault()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setDefaultValue((String.valueOf(temp.getDefault())));
							else
								((GenerateRecordSchemaGridRow) gridRow).setDefaultValue((String.valueOf("")));

							if(temp.getRangeFrom()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setRangeFrom(String.valueOf(temp.getRangeFrom()));
							else
								((GenerateRecordSchemaGridRow) gridRow).setRangeFrom("");

							if(temp.getRangeFrom()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setRangeTo(String.valueOf(temp.getRangeTo()));
							else
								((GenerateRecordSchemaGridRow) gridRow).setRangeTo("");
							
							schemasFromFile.add(gridRow);
							
						}
						
					}
				} catch (JAXBException e) {
					logger.error(Messages.EXPORTED_SCHEMA_SYNC_ERROR, e);
				}
				
			}
			if(!equalLists(schemaInGrid, schemasFromFile,new GridComparator())){
				verifiedSchema=false;
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Information", Messages.EXPORTED_SCHEMA_NOT_IN_SYNC);
			}
			
		} catch (FileNotFoundException e) {
			logger.error(Messages.EXPORTED_SCHEMA_SYNC_ERROR, e);
		}
		return verifiedSchema;

	}

	private boolean validateXML(InputStream xml, InputStream xsd){
		try
		{
			SchemaFactory factory = 
					SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			javax.xml.validation.Schema schema = factory.newSchema(new StreamSource(xsd));
			Validator validator = schema.newValidator();
			
			validator.validate(new StreamSource(xml));
			return true;
		}
		catch(Exception ex)
		{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", Messages.IMPORT_XML_FORMAT_ERROR + "-\n" + ex.getMessage());
			logger.error(Messages.IMPORT_XML_FORMAT_ERROR);
			return false;
		}
	}
	
	private void populateCommonFields(GridRow gridRow, Field temp) {
		gridRow.setFieldName(temp.getName());
		gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value()));
		gridRow.setDataTypeValue(GridWidgetCommonBuilder.getDataTypeValue()[GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value())]);
		
		if(temp.getFormat()!=null)
			gridRow.setDateFormat(temp.getFormat());
		else
			gridRow.setDateFormat("");
		
		if(temp.getPrecision()!=null)
			gridRow.setPrecision(String.valueOf(temp.getPrecision()));
		else
			gridRow.setPrecision("");

		if(temp.getScale()!=null)
			gridRow.setScale(String.valueOf(temp.getScale()));
		else
			gridRow.setScale("");

		if(temp.getScaleType()!=null){
			gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value()));	
			gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value())]);
		}else{
			gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(Messages.SCALE_TYPE_NONE));
			gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]);
		}
		if(temp.getDescription()!=null)
			gridRow.setDescription(temp.getDescription());
		else
			gridRow.setDescription("");
	}
	
	public  boolean equalLists(List<GridRow> one, List<GridRow> two, GridComparator gridComparator ){     
	    if (one == null && two == null){
	        return true;
	    }

	    if((one == null && two != null) 
	      || one != null && two == null
	      || one.size() != two.size()){
	        return false;
	    }
	    Iterator<GridRow> firstList = one.iterator();
	    Iterator<GridRow> secondList = two.iterator();
	    while (firstList.hasNext()) {
	    	GridRow t1 = firstList.next();
	    	GridRow t2 = secondList.next();
	        if (gridComparator.compare(t1, t2) != 0) {
	            // as soon as a difference is found, stop looping
	            return false;
	        }
	    }
	    return true;
	}
	
	// Operational class label.
	AbstractELTWidget fieldError = new ELTDefaultLable(Messages.FIELDNAMEERROR).lableWidth(250);

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		if(transformSchemaType){
			createSchemaGridSection(container.getContainerControl(), 340, 360);
			if(StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.TRANSFORM)|| StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.AGGREGATE))
				createPullSchemaFromTransform(container.getContainerControl());
		}
		else{
			createSchemaTypesSection(container.getContainerControl());
			createSchemaGridSection(container.getContainerControl(), 250, 360);
			createExternalSchemaSection(container.getContainerControl());
		}
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
			if( this.getClass().isAssignableFrom(ELTMixedSchemeWidget.class))
				for (FixedWidthGridRow gridRow : componentsOutputSchema.getFixedWidthGridRowsOutputFields()) {
					schema.getGridRow().add(componentsOutputSchema.convertFixedWidthSchemaToMixedSchemaGridRow(gridRow));
				}
			else if (this.getClass().isAssignableFrom(ELTFixedWidget.class)) {
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
		Schema currentSchema = (Schema) this.properties;
		if ((currentSchema != null && !currentSchema.getIsExternal())
				|| (currentSchema == null && StringUtils.equalsIgnoreCase(getComponent().getCategory(),
						Constants.OUTPUT)))
			for (Link link : getComponent().getTargetConnections()) {
				this.properties = getPropagatedSchema(link);
			}
	}
	
	
	
	
	// Adds the browse button
	private void createPullSchemaFromTransform(Composite containerControl) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(containerControl);
		eltSuDefaultSubgroupComposite.createContainerWidget();
		eltSuDefaultSubgroupComposite.numberOfBasicWidgets(2);
		ELTDefaultButton btnPull = new ELTDefaultButton(Messages.PULL_FROM_TRANSFORM);
		btnPull.buttonWidth(150);
		eltSuDefaultSubgroupComposite.attachWidget(btnPull);
		((Button)btnPull.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				syncSchemaFromTransform();
			}
		});
	

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

		addImportExportButtons(containerControl);

		populateWidgetExternalSchema();

	}

	private void addImportExportButtons(Composite containerControl) {
		ELTDefaultSubgroupComposite importExportComposite = new ELTDefaultSubgroupComposite(containerControl);
		importExportComposite.createContainerWidget();
		importExportComposite.numberOfBasicWidgets(2);

		AbstractELTWidget importButtonWidget = new ELTDefaultButton(Messages.IMPORT_XML).buttonWidth(100);
		importExportComposite.attachWidget(importButtonWidget);
		importButton = (Button) importButtonWidget.getSWTWidgetControl();

		importButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				extSchemaPathText.getText();
				File schemaFile = new File(extSchemaPathText.getText());

				List<GridRow> schemaGridRowListToImport = new ArrayList<GridRow>();;

				tableViewer.setInput(schemaGridRowListToImport);
				tableViewer.refresh();

				GridRowLoader gridRowLoader = new GridRowLoader(gridRowType, schemaFile);

				schemaGridRowListToImport = gridRowLoader.importGridRowsFromXML(helper);

				if(schemaGridRowListToImport!=null){

					tableViewer.setInput(schemaGridRowList);
					tableViewer.refresh();
					enableDisableButtons(schemaGridRowListToImport.size());
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Information", Messages.IMPORTED_SCHEMA);
				}
				
			}
		});

		AbstractELTWidget exportButtonWidget = new ELTDefaultButton(Messages.EXPORT_XML).buttonWidth(100).grabExcessHorizontalSpace(false);
		importExportComposite.attachWidget(exportButtonWidget);
		exportButton = (Button) exportButtonWidget.getSWTWidgetControl();

		//Add listener
		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				extSchemaPathText.getText();
				File schemaFile = (File) new File(extSchemaPathText.getText());

				GridRowLoader gridRowLoader = new GridRowLoader(gridRowType, schemaFile);
				gridRowLoader.exportXMLfromGridRows((ArrayList<GridRow>) schemaGridRowList);
			}
		});
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
				toggleSchema(false);
				external = false;
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
				toggleSchema(true);
				external = true;

				if (extSchemaPathText.getText().isEmpty()) {
					decorator.show();
				} else {
					decorator.hide();
				}
			}
		});

		populateSchemaTypeWidget();
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
					external = false;
					toggleSchema(false);
				}
			}
			enableDisableButtons(schemaGridRowList.size());
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
					external = true;
					toggleSchema(true);
				}
			} else {
				toggleSchema(false);
			}
			enableDisableButtons(schemaGridRowList.size());
		}
	}

	private void populateSchemaTypeWidget() {
		if (this.properties != null) {
			Schema schema = (Schema) this.properties;
			if (schema.getIsExternal()) {
				toggleSchemaChoice(true);
				toggleSchema(true);
			} else {
				toggleSchemaChoice(false);
				toggleSchema(false);
			}
		} else {
			toggleSchemaChoice(false);
			toggleSchema(false);
		}
	}


	private void toggleSchemaChoice(boolean enableExternalSchemaRadioButton) {
		if(externalSchema!=null){
			((Button) externalSchema.getSWTWidgetControl()).setSelection(enableExternalSchemaRadioButton);
			((Button) internalSchema.getSWTWidgetControl()).setSelection(!enableExternalSchemaRadioButton);
		}
	}

	private void toggleSchema(boolean enableExtSchema) {
		if (extSchemaPathText != null && browseButton != null) {
			extSchemaPathText.setEnabled(enableExtSchema);
			browseButton.setEnabled(enableExtSchema);
			importButton.setEnabled(enableExtSchema);
			exportButton.setEnabled(enableExtSchema);
		}
	}

	public ListenerHelper getListenerHelper(boolean isTab) {
		if (helper == null && !isTab) {
			helper = new ListenerHelper();
			if (this.properties != null) {
				Schema schema = (Schema) this.properties;
				schemaGridRowList = schema.getGridRow();
			}
				ELTGridDetails value = new ELTGridDetails(schemaGridRowList, tableViewer,(Label) fieldError.getSWTWidgetControl(), gridWidgetBuilder);
				helper.put(HelperType.SCHEMA_GRID, value);
		}
		else{
			if(Constants.TRANSFORM.equalsIgnoreCase(getComponent().getComponentName()) || Constants.AGGREGATE.equalsIgnoreCase(getComponent().getComponentName()))
			{	Map<String, ComponentsOutputSchema> previousOutputSchema = ((Map<String, ComponentsOutputSchema>) getComponent().getProperties().get(Constants.SCHEMA_TO_PROPAGATE));
				if(previousOutputSchema!=null){
				for (String schemaKey : previousOutputSchema.keySet()) {
					ComponentsOutputSchema componentsOutputSchema = previousOutputSchema.get(schemaKey);
					for (BasicSchemaGridRow basicSchemaGridRow : componentsOutputSchema.getSchemaGridOutputFields() ){
						if(!schemaGridRowList.contains(basicSchemaGridRow))
							schemaGridRowList.add(basicSchemaGridRow);
					}
				}
			}
			tableViewer.refresh();
			}
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
	public TableViewer createSchemaGridSection(Composite container, int height,
			int width) {

		ELTSchemaSubgroupComposite buttonSubGroup = new ELTSchemaSubgroupComposite(
				container);

		buttonSubGroup.createContainerWidget();

		buttonSubGroup.numberOfBasicWidgets(4);

		addAddButton(buttonSubGroup);
		addDeleteButton(buttonSubGroup);
		addUpButton(buttonSubGroup);
		addDownButton(buttonSubGroup);

		ELTSchemaTableComposite gridSubGroup = new ELTSchemaTableComposite(
				container);
		gridSubGroup.createContainerWidget();

		ColumnLayoutData cld_composite = new ColumnLayoutData();
		cld_composite.heightHint = 260;
		cld_composite.widthHint = 400;
		gridSubGroup.getContainerControl().setLayoutData(cld_composite);

		Composite composite = new Composite(gridSubGroup.getContainerControl(),
				SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1);
		
		composite.setLayoutData(gd_composite);

		ScrolledComposite scrolledComposite = new ScrolledComposite(composite,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setShowFocusedControl(true);

		Composite composite_2 = new Composite(scrolledComposite, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		gl_composite_2.horizontalSpacing = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		AbstractELTWidget eltTableViewer = new ELTTableViewer(
				getContentProvider(), getLableProvider());
		eltTableViewer.attachWidget(composite_2);

		tableViewer = (TableViewer) eltTableViewer.getJfaceWidgetControl();
		tableViewer.setInput(schemaGridRowList);

		addGridRowsCopyPasteContextMenu();

		// Set the editors, cell modifier, and column properties
		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(getCellModifier());
		ELTTable eltTable = new ELTTable(tableViewer, height, width);
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
		TableViewerEditor.create(tableViewer,
				new ColumnViewerEditorActivationStrategy(tableViewer),
				ColumnViewerEditor.KEYBOARD_ACTIVATION
						| ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL);

		TableColumnLayout layout = new TableColumnLayout();
		tableViewer.getControl().getParent().setLayout(layout);

		for (int i = 0; i < tableViewer.getTable().getColumnCount(); i++) {
			layout.setColumnData(tableViewer.getTable().getColumn(i),
					new ColumnWeightData(1));
		}
		
		helper = getListenerHelper(false);
		
		// Adding the decorator to show error message when field name same.
		setDecorator();

		addValidators();
		try {
			eltTable.attachListener(
					ListenerFactory.Listners.GRID_MOUSE_DOUBLE_CLICK
							.getListener(), propertyDialogButtonBar, helper,
					table, deleteButton.getSWTWidgetControl(), upButton
							.getSWTWidgetControl(), downButton
							.getSWTWidgetControl());
			eltTable.attachListener(
					ListenerFactory.Listners.GRID_MOUSE_DOWN.getListener(),
					propertyDialogButtonBar, helper, editors[0].getControl());
			addButton.attachListener(
					ListenerFactory.Listners.GRID_ADD_SELECTION.getListener(),
					propertyDialogButtonBar, helper, table,
					deleteButton.getSWTWidgetControl(),
					upButton.getSWTWidgetControl(),
					downButton.getSWTWidgetControl());
			deleteButton.attachListener(
					ListenerFactory.Listners.GRID_DELETE_SELECTION
							.getListener(), propertyDialogButtonBar, helper,
					table, deleteButton.getSWTWidgetControl(), upButton
							.getSWTWidgetControl(), downButton
							.getSWTWidgetControl());

		} catch (Exception e) {
			logger.error(Messages.ATTACH_LISTENER_ERROR, e);
			throw new RuntimeException(Messages.ATTACH_LISTENER_ERROR);
		}

		gridListener(editors);
		upButton.setEnabled(false);
		downButton.setEnabled(false);
		deleteButton.setEnabled(false);

		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(
				tableViewer, new FocusCellOwnerDrawHighlighter(tableViewer));
		ColumnViewerEditorActivationStrategy activationSupport = new ColumnViewerEditorActivationStrategy(
				tableViewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION) {
					EventObject source = event.sourceEvent;
					if (source instanceof MouseEvent
							&& ((MouseEvent) source).button == 3)
						return false;
				}
				return super.isEditorActivationEvent(event)
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.F2);
			}
		};
		TableViewerEditor.create(tableViewer, focusCellManager,
				activationSupport, ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		populateWidget();
		scrolledComposite.setContent(composite_2);
		scrolledComposite.setMinSize(composite_2.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		return tableViewer;
	}

	private void addGridRowsCopyPasteContextMenu() {
		Menu menu = new Menu(tableViewer.getControl());
	
		copyMenuItem = new MenuItem(menu, SWT.PUSH);
		copyMenuItem.setText(Messages.COPY_MENU_TEXT);
		copyMenuItem.setAccelerator(SWT.CTRL + 'C');
		copyMenuItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.trace("Copying gridRows");
				copiedGridRows.clear();
				for (TableItem tableItem:tableViewer.getTable().getSelection()){
					copiedGridRows.add((GridRow) tableItem.getData());
					logger.trace("Copied", ((GridRow) tableItem.getData()).getFieldName());
				}
				pasteMenuItem.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		pasteMenuItem = new MenuItem(menu, SWT.PUSH);
		pasteMenuItem.setText(Messages.PASTE_MENU_TEXT);
		pasteMenuItem.setAccelerator(SWT.CTRL + 'V');
		pasteMenuItem.setEnabled(!copiedGridRows.isEmpty());
		
		pasteMenuItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.trace("Pasting gridRows");
				ELTGridDetails eltGridDetails = (ELTGridDetails)helper.get(HelperType.SCHEMA_GRID);
				for (GridRow copiedRow:copiedGridRows){
					logger.trace("Pasted",copiedRow.getFieldName());
					GridRow pasteGrid = copiedRow.copy();

					int copyCount =0;	
					do{
						pasteGrid.setFieldName(copiedRow.getFieldName() + Messages.COPY_GRID_SUFFIX + copyCount++);
					}while(eltGridDetails.getGrids().contains(pasteGrid));
					
					eltGridDetails.getGrids().add(pasteGrid);
				}
				tableViewer.setInput(eltGridDetails.getGrids());
				tableViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		tableViewer.getTable().setMenu(menu);
	}

	private void addAddButton(ELTSchemaSubgroupComposite buttonSubGroup) {
		addButton = new ELTDefaultLable("");
		addButton.lableWidth(25);
		buttonSubGroup.attachWidget(addButton);
		addButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.ADD_BUTTON);
		addButton.setToolTipText(addButtonTooltip);
	}

	private void addDeleteButton(ELTSchemaSubgroupComposite buttonSubGroup) {
		deleteButton = new ELTDefaultLable("");
		deleteButton.lableWidth(25);
		buttonSubGroup.attachWidget(deleteButton);
		deleteButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DELETE_BUTTON);
		deleteButton.setToolTipText(removeButtonTooltip);
	}

	private void addUpButton(ELTSchemaSubgroupComposite buttonSubGroup) {
		upButton = new ELTDefaultLable("");
		upButton.lableWidth(25);
		buttonSubGroup.attachWidget(upButton);
		upButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEUP_BUTTON);
		upButton.setToolTipText(upButtonTooltip);
		upButton.addMouseUpListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				propertyDialogButtonBar.enableApplyButton(true);
				int[] indexes=table.getSelectionIndices();
				for(int index :indexes)
				{

					if (index > 0) {
						Collections.swap((List)schemaGridRowList,index ,index-1);
						tableViewer.refresh();

					}
				}
			}
		});
	}

	private void addDownButton(ELTSchemaSubgroupComposite buttonSubGroup) {
		downButton = new ELTDefaultLable("");
		downButton.lableWidth(25);
		buttonSubGroup.attachWidget(downButton);

		downButton.setImage(XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEDOWN_BUTTON);
		downButton.setToolTipText(downButtonTooltip);


		downButton.addMouseUpListener(new MouseAdapter() {


			@Override
			public void mouseUp(MouseEvent e) {

				propertyDialogButtonBar.enableApplyButton(true);
				int[] indexes = table.getSelectionIndices();
				for (int i = indexes.length - 1; i > -1; i--) {

					if (indexes[i] < schemaGridRowList.size() - 1) {
						Collections.swap((List)schemaGridRowList,indexes[i] ,indexes[i]+1);
						tableViewer.refresh();

					}
				}

			}
		});
	}




	public List<GridRow> getSchemaGridRowList() {
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
				if (row != null) {
					if (existingFieldNames.contains(row.getFieldName().trim())) {
						if (existingFields.contains(row)) {
							for (int index = 0; index < originalSchema.getGridRow().size(); index++) {
								if (originalSchema.getGridRow().get(index).getFieldName().equals(row.getFieldName().trim())) {
									if (!operationFieldList.contains(row.getFieldName()))
										originalSchema.getGridRow().set(index, row.copy());
								}
							}
						}
					} 
					else if(!StringUtils.equalsIgnoreCase(Constants.TRANSFORM, getComponent().getComponentName()) && !StringUtils.equalsIgnoreCase(Constants.AGGREGATE, getComponent().getComponentName()))
							originalSchema.getGridRow().add(row.copy());					
				}
			}
			table.clearAll();


			if (tableViewer != null) {
				schemaGridRowList = originalSchema.getGridRow();
				tableViewer.setInput(schemaGridRowList);
				tableViewer.refresh();
				toggleSchema(true);
			}
			if (!originalSchema.getIsExternal()) {
				external = false;
				toggleSchema(false);
			}

		} else {
			if(!StringUtils.equalsIgnoreCase(Constants.TRANSFORM, getComponent().getComponentName()) && !StringUtils.equalsIgnoreCase(Constants.AGGREGATE, getComponent().getComponentName()))
			{			
			if (schema.getGridRow().size() != 0) {
				table.clearAll();
				if (!schema.getIsExternal()) {
					if (tableViewer != null) {
						schemaGridRowList = schema.getGridRow();
						tableViewer.setInput(schemaGridRowList);
						tableViewer.refresh();
						external = false;
						toggleSchema(false);
					}
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

	public boolean isTransformSchemaType() {
		return transformSchemaType;
	}

	public void setTransformSchemaType(boolean isTransformSchemaType) {
		this.transformSchemaType = isTransformSchemaType;
	}
	
	public void enableDisableButtons(int size) {
		if (size >= 1) {
			deleteButton.setEnabled(true);
		} else {
			deleteButton.setEnabled(false);
		}
		if (size >= 2) {
			upButton.setEnabled(true);
			downButton.setEnabled(true);
		} else {
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		}
	}

	public boolean isExternal() {
		return external;
	}
	public int getSizeOfTableViewer()
	{
		return schemaGridRowList.size();
	}

private void syncSchemaFromTransform(){
	Schema schema =getSchemaForInternalPapogation();
	for (GridRow gridRow : schema.getGridRow()) {
		if(!schemaGridRowList.contains(gridRow)){
			schemaGridRowList.add(gridRow);
		}else
		{
			schemaGridRowList.set(schemaGridRowList.indexOf(gridRow), gridRow);
		}
	}
	ELTGridDetails eLTDetails= (ELTGridDetails) helper.get(HelperType.SCHEMA_GRID);
	eLTDetails.setGrids(schemaGridRowList);
	tableViewer.setInput(schemaGridRowList);
	tableViewer.refresh();

}
private boolean isSchemaInSync(){
	Schema schema =getSchemaForInternalPapogation();
	for (GridRow gridRow : schema.getGridRow()) {
		if(!schemaGridRowList.contains(gridRow)){
			return false;
		}
	}
	return true;
}
	
}