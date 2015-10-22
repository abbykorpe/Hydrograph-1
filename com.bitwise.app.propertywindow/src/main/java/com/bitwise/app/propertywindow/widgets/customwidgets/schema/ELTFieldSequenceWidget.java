package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTTable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTTableViewer;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDetails;
import com.bitwise.app.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;
// TODO: Auto-generated Javadoc

/**
 * The Class ELTFieldSequenceWidget.
 * 
 * @author Bitwise
 */
//TODO : REMOVE THIS CLASS
public class ELTFieldSequenceWidget extends AbstractWidget {

	/**
	 * Instantiates a new ELT field sequence widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTFieldSequenceWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);

		setProperties(componentConfigrationProperty.getPropertyName(), componentConfigrationProperty.getPropertyValue());
	}

	private Table table;
	private List fieldSeduence = new ArrayList();
	public ControlDecoration fieldNameDecorator;
	public ControlDecoration scaleDecorator;
	private Object properties;
	private String propertyName;
	public TableViewer tableViewer;
	private ListenerHelper helper; 
	private LinkedHashMap<String, Object> property=new LinkedHashMap<>();


	// Table column names/properties
	public static final String FIELDNAME = Messages.FIELDNAME;
	public static final String DATEFORMAT = Messages.DATEFORMAT;
	public static final String DATATYPE = Messages.DATATYPE;
	public static final String SCALE = Messages.SCALE;
	public static final String[] PROPS = { FIELDNAME, DATATYPE, DATEFORMAT,
			SCALE };
	// Operational class label.
			final AbstractELTWidget fieldError = new ELTDefaultLable(Messages.FIELDNAMEERROR).lableWidth(95);
	public static String[] dataTypeList;

	// get the datatype list from property file.
	public static String[] getDataType() {
		if (dataTypeList != null)
			return dataTypeList;
		else {
			String schemaList = Messages.DATATYPELIST;
			dataTypeList = schemaList.split(",");
			return dataTypeList;
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		/*ListenerFactory listenerFactory = new ListenerFactory();
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		
		AbstractELTWidget eltTableViewer = new ELTTableViewer(new SchemaGridContentProvider(), new SchemaGridLabelProvider());
		eltSuDefaultSubgroupComposite.attachWidget(eltTableViewer);
		//eltTableViewer.getSWTWidgetControl().
		tableViewer = (TableViewer) eltTableViewer.getJfaceWidgetControl();
		tableViewer.setInput(fieldSeduence);
		// Set up the table
		ELTTable eltTable = new ELTTable(tableViewer);
		eltSuDefaultSubgroupComposite.attachWidget(eltTable); 
		table = (Table)eltTable.getSWTWidgetControl();
		//Create Table column 
		WidgetUtility.createTableColumns(table, PROPS);
		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			table.getColumn(i).pack(); 
			table.getColumn(i).setWidth(80);
			
		}
		
		CellEditor[] editors = GeneralGridWidgetBuilder.createCellEditorList(table,4);

		// Set the editors, cell modifier, and column properties
		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(new SchemaGridCellModifier(tableViewer));
		tableViewer.setCellEditors(editors); 
		//Adding the decorator to show error message when field name same.
		fieldNameDecorator =	WidgetUtility.addDecorator(editors[0].getControl(),Messages.FIELDNAMEERROR)	;
		scaleDecorator =	WidgetUtility.addDecorator(editors[3].getControl(),Messages.SCALEERROR)	;
		
		editors[0].setValidator(new ELTCellEditorFieldValidator(table, fieldSeduence, fieldNameDecorator,propertyDialogButtonBar));
		editors[3].setValidator(new ELTCellEditorIsNumericValidator(scaleDecorator,propertyDialogButtonBar)); 

		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite2 = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite2.createContainerWidget();
		// Create browse button.
		AbstractELTWidget addButton = new ELTDefaultButton("Add").buttonWidth(60);
		eltSuDefaultSubgroupComposite2.attachWidget(addButton);

				// Create new button, that use to create operational class
		AbstractELTWidget deleteButton = new ELTDefaultButton("Delete").buttonWidth(60);
		eltSuDefaultSubgroupComposite2.attachWidget(deleteButton);

				// Edit new button, that use to edit operational class
		AbstractELTWidget deleteAllButton = new ELTDefaultButton("Delete All").buttonWidth(60);
		eltSuDefaultSubgroupComposite2.attachWidget(deleteAllButton); 

		helper= new ListenerHelper("schemaGrid", new ELTGridDetails(fieldSeduence,tableViewer,(Label)fieldError.getSWTWidgetControl(),new GeneralGridWidgetBuilder()));
		try {
			eltTable.attachListener(listenerFactory.getListener("ELTGridMouseDoubleClickListener"),propertyDialogButtonBar, helper,table);
			eltTable.attachListener(listenerFactory.getListener("ELTGridMouseDownListener"),propertyDialogButtonBar, helper,editors[0].getControl());
			addButton.attachListener(listenerFactory.getListener("ELTGridAddSelectionListener"),propertyDialogButtonBar, helper,table);
			deleteButton.attachListener(listenerFactory.getListener("ELTGridDeleteSelectionListener"),propertyDialogButtonBar, helper,table);
			deleteAllButton.attachListener(listenerFactory.getListener("ELTGridDeleteAllSelectionListener"),propertyDialogButtonBar, helper,table); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
 
	@Override
	public LinkedHashMap<String,Object> getProperties() {
		property.put(propertyName, fieldSeduence); 
		return property; 
	}  
 
	
	/**
	 * Sets the properties.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param properties
	 *            the properties
	 */
	public void setProperties(String propertyName, Object properties) {
		/*this.properties =  properties;
		this.propertyName = propertyName;
		if(this.properties!=null)   
		{
			fieldSeduence =(List<SchemaGrid>) this.properties;
		tableViewer.setInput(fieldSeduence);
		helper=new ListenerHelper("schemaGrid", new ELTGridDetails(fieldSeduence,tableViewer,(Label)fieldError.getSWTWidgetControl(),new GeneralGridWidgetBuilder()));
		tableViewer.refresh();
		} */
	} 


}
