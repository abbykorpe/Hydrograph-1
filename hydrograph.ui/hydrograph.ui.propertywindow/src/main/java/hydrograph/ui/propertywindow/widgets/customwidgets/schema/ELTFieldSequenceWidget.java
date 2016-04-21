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

import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTTable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTTableViewer;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTCellEditorIsNumericValidator;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTGridDetails;
import hydrograph.ui.propertywindow.widgets.listeners.grid.schema.ELTCellEditorFieldValidator;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;


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
 
	private void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
		if(fieldNameDecorator.isVisible())
			toolTipErrorMessage = fieldNameDecorator.getDescriptionText();
		
		if(scaleDecorator.isVisible())
			toolTipErrorMessage = toolTipErrorMessage + "\n" + scaleDecorator.getDescriptionText();
		
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public LinkedHashMap<String,Object> getProperties() {
		property.put(propertyName, fieldSeduence); 
		setToolTipErrorMessage();
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
