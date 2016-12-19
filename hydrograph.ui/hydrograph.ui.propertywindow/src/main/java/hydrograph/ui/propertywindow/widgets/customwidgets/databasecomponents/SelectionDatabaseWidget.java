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
package hydrograph.ui.propertywindow.widgets.customwidgets.databasecomponents;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import hydrograph.ui.common.datastructures.property.database.DatabaseParameterType;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.DatabaseSelectionConfig;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory.Listners;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.utils.Utils;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.TextBoxWithLableConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.metastore.DatabaseTableSchema;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTRadioButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTSubGroupCompositeWithStack;
import hydrograph.ui.propertywindow.widgets.listeners.IELTListener;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

/**
 * SelectionDatabaseWidget provides 
 * @author Bitwise
 *
 */
public class SelectionDatabaseWidget extends AbstractWidget {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(LoadTypeConfigurationWidget.class);
	private String propertyName;
	private DatabaseSelectionConfig databaseSelectionConfig;
	private ELTRadioButton tableNameRadioButton;
	private ELTRadioButton sqlQueryRadioButton;
	private TextBoxWithLableConfig textBoxConfig;
	private ControlDecoration tableNameDecorator;
	private ControlDecoration sqlQueryDecorator;
	private ELTSubGroupCompositeWithStack tableComposite;
	private ELTSubGroupCompositeWithStack sqlQueryComposite;
	private Text sqlQueryTextBox;
	private ArrayList<AbstractWidget> widgets;
	private Text textBoxTableName;
	private ELTDefaultLable selectLable;
	private ModifyListener textboxSQLQueryModifyListner;
	private ModifyListener textboxTableNameModifyListner;
	private static final String ERROR = "ERR";
	private static final String INFO = "INF";
	private Cursor cursor;
	private String sqlQueryStatement;
	private Text sqlQueryCountertextbox;
	private ModifyListener sqlQueryCounterModifyListner;
	private static final String ORACLE = "Oracle";
	private static final String REDSHIFT = "RedShift";
	private static final String MYSQL = "Mysql";
	

	public SelectionDatabaseWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propertyDialogButtonBar) {

		super(componentConfigProp, componentMiscProps, propertyDialogButtonBar);
		this.propertyName = componentConfigProp.getPropertyName();
		this.databaseSelectionConfig = (DatabaseSelectionConfig) componentConfigProp.getPropertyValue();
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		final ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				subGroup.getContainerControl());

		eltSuDefaultSubgroupComposite.createContainerWidget();
		eltSuDefaultSubgroupComposite.numberOfBasicWidgets(6);

		selectLable = new ELTDefaultLable(Messages.DATABASE_SELECT);
		eltSuDefaultSubgroupComposite.attachWidget(selectLable);

		tableNameRadioButton = new ELTRadioButton(Messages.DATABASE_TABLE_NAME);
		eltSuDefaultSubgroupComposite.attachWidget(tableNameRadioButton);
		propertyDialogButtonBar.enableApplyButton(true);

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		sqlQueryRadioButton = new ELTRadioButton(Messages.DATABASE_SQL_QUERY);
		eltSuDefaultSubgroupComposite.attachWidget(sqlQueryRadioButton);

		final ELTSubGroupCompositeWithStack selectionComposite = new ELTSubGroupCompositeWithStack(
				subGroup.getContainerControl());
		final StackLayout layout = new StackLayout();
		selectionComposite.createStackContainerWidget(layout);

		createTableNameComposite(selectionComposite);

		createSQLQueryComposite(selectionComposite);

		if (null != databaseSelectionConfig) {
			if (databaseSelectionConfig.isTableName()) {

				layout.topControl = tableComposite.getContainerControl();
				((Button) tableNameRadioButton.getSWTWidgetControl()).setSelection(true);

			} else {

				layout.topControl = sqlQueryComposite.getContainerControl();
				((Button) sqlQueryRadioButton.getSWTWidgetControl()).setSelection(true);
			}
			eltSuDefaultSubgroupComposite.getContainerControl().layout();

		} else {

			layout.topControl = tableComposite.getContainerControl();
			eltSuDefaultSubgroupComposite.getContainerControl().layout();
			((Button) tableNameRadioButton.getSWTWidgetControl()).setSelection(true);

		}
		attachTableButtonListner(selectionComposite, layout);

		attachSQLQueryListner(selectionComposite, layout);
		populateWidget();
	}

	/**
	 * 
	 * @param selectionComposite
	 * @param layout
	 */
	private void attachSQLQueryListner(final ELTSubGroupCompositeWithStack selectionComposite,
			final StackLayout layout) {
		final Button sqlRadioBtn = (Button) sqlQueryRadioButton.getSWTWidgetControl();
		sqlRadioBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (sqlRadioBtn.getSelection()) {
					unRegisterTableOrSQLQueryTextListner();
					layout.topControl = sqlQueryComposite.getContainerControl();
					selectionComposite.getContainerControl().layout();
					if (databaseSelectionConfig != null) {
						databaseSelectionConfig.setTableName(false);
						databaseSelectionConfig.setSqlQuery(sqlQueryTextBox.getText());
						databaseSelectionConfig.setSqlQueryCounter(sqlQueryCountertextbox.getText());
						populateWidget();
					}
					showHideErrorSymbol(widgets);
					propertyDialogButtonBar.enableApplyButton(true);
				}
			}
		});
	}

	/**
	 * 
	 * @param selectionComposite
	 * @param layout
	 */
	private void attachTableButtonListner(final ELTSubGroupCompositeWithStack selectionComposite,
			final StackLayout layout) {
		final Button tableRadioBtn = (Button) tableNameRadioButton.getSWTWidgetControl();
		tableRadioBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableRadioBtn.getSelection()) {
					unRegisterTableOrSQLQueryTextListner();
					layout.topControl = tableComposite.getContainerControl();
					selectionComposite.getContainerControl().layout();
					if (databaseSelectionConfig != null) {
						databaseSelectionConfig.setTableName(true);
						databaseSelectionConfig.setTableName(textBoxTableName.getText());
						populateWidget();
					}
					showHideErrorSymbol(widgets);
					propertyDialogButtonBar.enableApplyButton(true);
				}
			}
		});
	}

	/**
	 * Creates the stack layout composite for SQLQuery 
	 * @param selectionComposite
	 */
	private void createSQLQueryComposite(ELTSubGroupCompositeWithStack selectionComposite) {

		Utils.INSTANCE.loadProperties();
		cursor = selectionComposite.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		sqlQueryComposite = new ELTSubGroupCompositeWithStack(selectionComposite.getContainerControl());
		sqlQueryComposite.createContainerWidget();

		sqlQueryComposite.numberOfBasicWidgets(3);

		createWidgetlabel(Messages.SQL_QUERY, sqlQueryComposite);
		AbstractELTWidget sqlQueryWgt = createWidgetTextbox(Messages.SQL_QUERY, sqlQueryComposite);
		sqlQueryDecorator = attachDecoratorToTextbox(Messages.SQL_QUERY, sqlQueryWgt, sqlQueryDecorator);
		sqlQueryTextBox = (Text) sqlQueryWgt.getSWTWidgetControl();
		attachListeners(sqlQueryWgt);

		ELTDefaultButton sqlQueryButtonWgt = new ELTDefaultButton("...");
		sqlQueryButtonWgt.buttonWidth(25);
		sqlQueryButtonWgt.buttonHeight(20);
		sqlQueryButtonWgt.grabExcessHorizontalSpace(false);
		sqlQueryComposite.attachWidget(sqlQueryButtonWgt);
		Button buttonAlignment = ((Button) sqlQueryButtonWgt.getSWTWidgetControl());
		GridData data = (GridData) buttonAlignment.getLayoutData();
		data.verticalIndent = 5;
		sqlQuerySelectionListner(sqlQueryButtonWgt);

		createWidgetlabel(Messages.SQL_QUERY_COUNTER, sqlQueryComposite);
		AbstractELTWidget sqlQueryCounterWgt = createWidgetTextbox(Messages.SQL_QUERY_COUNTER, sqlQueryComposite);
		sqlQueryCountertextbox = (Text) sqlQueryCounterWgt.getSWTWidgetControl();
		attachListeners(sqlQueryCounterWgt);

		ELTDefaultButton sqlQueryCounterButtonWgt = new ELTDefaultButton("...");
		sqlQueryCounterButtonWgt.buttonWidth(25);
		sqlQueryCounterButtonWgt.buttonHeight(20);
		sqlQueryCounterButtonWgt.grabExcessHorizontalSpace(false);
		sqlQueryComposite.attachWidget(sqlQueryCounterButtonWgt);
		Button sqlQueryCounterButton = ((Button) sqlQueryCounterButtonWgt.getSWTWidgetControl());
		GridData sqlQueryCounterData = (GridData) sqlQueryCounterButton.getLayoutData();
		sqlQueryCounterData.verticalIndent = 5;
		sqlQueryCounterSelectionListner(sqlQueryCounterButtonWgt);

	}

	/**
	 * Opens the SQL Query Counter Dialog
	 * @param sqlQueryCounterButtonWgt
	 */
	private void sqlQueryCounterSelectionListner(ELTDefaultButton sqlQueryCounterButtonWgt) {
		
		((Button) sqlQueryCounterButtonWgt.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			private String sqlQueryCounterStatement;

			@Override
			public void widgetSelected(SelectionEvent e) {
				SQLQueryStatementDialog sqlQueryStatementDialog = new SQLQueryStatementDialog(
						Display.getCurrent().getActiveShell());
				sqlQueryStatementDialog.open();
				sqlQueryCounterStatement = sqlQueryStatementDialog.getStyleTextSqlQuery();
				sqlQueryCountertextbox.setText(sqlQueryCounterStatement);
			}

		});
	}

	/**
	 * Opens the SQL Query Statement Dialog
	 * @param sqlQueryButtonWgt
	 */
	private void sqlQuerySelectionListner(ELTDefaultButton sqlQueryButtonWgt) {

		((Button) sqlQueryButtonWgt.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				SQLQueryStatementDialog sqlQueryStatementDialog = new SQLQueryStatementDialog(
						Display.getCurrent().getActiveShell());
				sqlQueryStatementDialog.open();
				sqlQueryStatement = sqlQueryStatementDialog.getStyleTextSqlQuery();
				sqlQueryTextBox.setText(sqlQueryStatement);
			}

		});
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {

		DatabaseSelectionConfig databaseSelectionConfig = new DatabaseSelectionConfig();
		if (((Button) tableNameRadioButton.getSWTWidgetControl()).getSelection()) {
			databaseSelectionConfig.setTableName(true);
			databaseSelectionConfig.setTableName(textBoxTableName.getText());

		} else {
			databaseSelectionConfig.setTableName(false);
			databaseSelectionConfig.setSqlQuery(sqlQueryTextBox.getText());
			databaseSelectionConfig.setSqlQueryCounter(sqlQueryCountertextbox.getText());
		}

		LinkedHashMap<String, Object> property = new LinkedHashMap<>();
		property.put(propertyName, databaseSelectionConfig);

		setToolTipErrorMessage();
		return property;
	}

	/**
	 * Unregisters all the modify listeners on TextBoxes
	 */
	protected void unRegisterTableOrSQLQueryTextListner() {
		if (((Button) tableNameRadioButton.getSWTWidgetControl()).getSelection()) {

			sqlQueryTextBox.removeModifyListener(textboxSQLQueryModifyListner);
			sqlQueryCountertextbox.removeModifyListener(sqlQueryCounterModifyListner);
			registerTextBoxListner(true);

		} else {

			textBoxTableName.removeModifyListener(textboxTableNameModifyListner);
			registerTextBoxListner(false);

		}

	}
	
	/**
	 * Registers all the modify listeners on TextBoxes
	 * @param isTableNameRadioButton
	 */
	private void registerTextBoxListner(boolean isTableNameRadioButton) {

		if (isTableNameRadioButton) {
			textBoxTableName.addModifyListener(textboxTableNameModifyListner);
		} else {
			sqlQueryTextBox.addModifyListener(textboxSQLQueryModifyListner);
			sqlQueryCountertextbox.addModifyListener(sqlQueryCounterModifyListner);
		}

	}

	/**
	 * Sets the data structure used for TextBoxes
	 */
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		textBoxConfig = (TextBoxWithLableConfig) widgetConfig;
	}

	/**
	 * Sets the tool tip error message
	 */
	protected void setToolTipErrorMessage() {

		String toolTipErrorMessage = null;

		if (sqlQueryDecorator.isVisible()) {
			toolTipErrorMessage = sqlQueryDecorator.getDescriptionText();
			setToolTipMessage(toolTipErrorMessage);
		}

		if (tableNameDecorator.isVisible()) {
			toolTipErrorMessage = tableNameDecorator.getDescriptionText();
			setToolTipMessage(toolTipErrorMessage);
		}

	}

	@Override
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(databaseSelectionConfig);
	}

	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
		widgets = widgetList;

		textboxSQLQueryModifyListner = attachTextModifyListner(widgetList);
		textboxTableNameModifyListner = attachTextModifyListner(widgetList);
		sqlQueryCounterModifyListner = attachTextModifyListner(widgetList);

		sqlQueryTextBox.addModifyListener(textboxSQLQueryModifyListner);
		textBoxTableName.addModifyListener(textboxTableNameModifyListner);
		sqlQueryCountertextbox.addModifyListener(sqlQueryCounterModifyListner);

	}
	
	/**
	 * Applies multiple listeners to textBoxes 
	 * @param widgetList
	 * @return
	 */
	private ModifyListener attachTextModifyListner(final ArrayList<AbstractWidget> widgetList) {
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				showHideErrorSymbol(widgetList);
			}
		};
	}
	
	/**
	 * Creates the stack layout composite for Table option
	 * @param eltSuDefaultSubgroupComposite
	 */
	private void createTableNameComposite(ELTSubGroupCompositeWithStack eltSuDefaultSubgroupComposite) {

		Utils.INSTANCE.loadProperties();
		cursor = eltSuDefaultSubgroupComposite.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		tableComposite = new ELTSubGroupCompositeWithStack(eltSuDefaultSubgroupComposite.getContainerControl());
		tableComposite.createContainerWidget();

		tableComposite.numberOfBasicWidgets(2);
		createWidgetlabel(Messages.LABEL_TABLE_NAME, tableComposite);
		AbstractELTWidget tableNameWgt = createWidgetTextbox(Messages.LABEL_TABLE_NAME, tableComposite);
		tableNameDecorator = attachDecoratorToTextbox(Messages.LABEL_TABLE_NAME, tableNameWgt, tableNameDecorator);
		textBoxTableName = (Text) tableNameWgt.getSWTWidgetControl();

		attachListeners(tableNameWgt);

		createWidgetlabel(Messages.EXTRACT_FROM_METASTORE, tableComposite);
		ELTDefaultButton editButton = new ELTDefaultButton(Messages.EXTRACT);
		tableComposite.attachWidget(editButton);

		Button button = (Button) editButton.getSWTWidgetControl();
		GridData data = new GridData(SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		data.widthHint = 92;
		data.horizontalIndent = 15;
		button.setLayoutData(data);

		button.addSelectionListener(attachExtractButtonSelectionListner());

	}

	/**
	 * Provides all the DB details
	 */
	private void getDatabaseConnectionDetails() {
		String oracleDatabaseName = "";
		String oracleHostName = "";
		String oraclePortNo= "";
		String oracleJdbcName= "";
		String oracleSchemaName= "";
		String oracleUserName= "";
		String oraclePassword= "";
		String databaseType= "";
		
		for (AbstractWidget textAbtractWgt : widgets) {

			if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_DATABASE_WIDGET_NAME)) {
				oracleDatabaseName = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_DATABASE_WIDGET_NAME);
			} else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_HOST_WIDGET_NAME)) {
				oracleHostName = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_HOST_WIDGET_NAME);
			} else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_PORT_WIDGET_NAME)) {
				oraclePortNo = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_PORT_WIDGET_NAME);
			} else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_JDBC_DRIVER_WIDGET_NAME)) {
				oracleJdbcName = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_JDBC_DRIVER_WIDGET_NAME);
			} else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_SCHEMA_WIDGET_NAME)) {
				oracleSchemaName = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_SCHEMA_WIDGET_NAME);
			} else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_USER_NAME_WIDGET_NAME)) {
				oracleUserName = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_USER_NAME_WIDGET_NAME);
			} else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_PASSWORD_WIDGET_NAME)) {
				oraclePassword = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_PASSWORD_WIDGET_NAME);
			}
			
			databaseType = getComponentType();
			DatabaseParameterType parameterType = new DatabaseParameterType.DatabaseBuilder(databaseType, oracleHostName, 
					oraclePortNo, oracleUserName, oraclePassword).jdbcName(oracleJdbcName).schemaName(oracleSchemaName)
					.databaseName(oracleDatabaseName).build();
			
			DataBaseUtility.getInstance().getDatabaseParams().clear();
			DataBaseUtility.getInstance().addDatabaseParams(parameterType);
		}

	}
	
	private String getComponentType(){
		String databaseType= "";
		if(StringUtils.equalsIgnoreCase(getComponent().getType(), ORACLE)){
			databaseType=ORACLE;
		}else if(StringUtils.equalsIgnoreCase(getComponent().getType(), REDSHIFT)){
			databaseType=REDSHIFT;
		}else if(StringUtils.equalsIgnoreCase(getComponent().getType(), MYSQL)){
			databaseType = MYSQL;
		}
		
		return databaseType;
	}
	
	private void validateDatabaseParams(){
		List<String> oracleDatabaseValues = new ArrayList<String>();
		getDatabaseConnectionDetails();
		
			LinkedHashMap<String, Object> property = getProperties();
			databaseSelectionConfig = (DatabaseSelectionConfig) property.get(propertyName);
			
			if (((Button) tableNameRadioButton.getSWTWidgetControl()).getSelection()) {
				oracleDatabaseValues.add(databaseSelectionConfig.getTableName());
			}else{
				createMessageDialog(Messages.METASTORE_FORMAT_ERROR_FOR_SQL_QUERY, INFO).open();
			}
			if (oracleDatabaseValues != null && oracleDatabaseValues.size() > 0) {
				extractOracleMetaStoreDetails(oracleDatabaseValues);
			}
	}

	private void validateDatabaseFields(DatabaseParameterType parameterType){
		if (StringUtils.isEmpty(parameterType.getDatabaseName()) && StringUtils.isEmpty(parameterType.getHostName())
				&& StringUtils.isEmpty(parameterType.getJdbcName()) && StringUtils.isEmpty(parameterType.getPortNo())
				&& StringUtils.isEmpty(parameterType.getSchemaName()) && StringUtils.isEmpty(parameterType.getUserName())
				&& StringUtils.isEmpty(parameterType.getPassword())) {
			createMessageDialog(Messages.METASTORE_FORMAT_ERROR, ERROR).open();
		}
	}
	
	/**
	 * Selection listener on Extract MetaStore button
	 * @return
	 */
	private SelectionAdapter attachExtractButtonSelectionListner() {
		
		SelectionAdapter adapter = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				validateDatabaseParams();
				}
			};

		return adapter;
	}
	
	/**
	 * Extracts the details from MetaStore for DB components
	 * @param oracleDatabaseValues
	 * @param host
	 * @param port_no
	 */
	private void extractOracleMetaStoreDetails(List<String> oracleDatabaseValues) {

		DatabaseParameterType parameterType =  DataBaseUtility.getInstance().getDatabaseParams().get(0);
		validateDatabaseFields(parameterType);
		
		DatabaseTableSchema databaseTableSchema = DataBaseUtility.getInstance()
				.extractDatabaseDetails(oracleDatabaseValues, parameterType);
		/*try {
			
			//TODO
			ObjectMapper mapper = new ObjectMapper();
			String input = oracleDatabaseName + SEPARATOR + oracleHostName + SEPARATOR + oracleJdbcName + SEPARATOR
					+ oraclePassword + SEPARATOR + oraclePortNo + SEPARATOR + oracleUserName + SEPARATOR+ oracleSchemaName;
			
			
			jsonResponse = DebugServiceClient.INSTANCE.readMetaStoreDb(input, host, port_no, oracleDatabaseValues);
			DatabaseTableSchema databaseTableSchema = mapper.readValue(jsonResponse,
					DatabaseTableSchema.class);

		} catch (NumberFormatException | HttpException | MalformedURLException exp) {
			logger.error("Json to object Mapping issue ", exp);
		} catch (IOException ex) {
			logger.error("Json to object Mapping issue ", ex.getMessage());
		}*/
		
		//TODO This functionality will used in the future for fetching the json response for RedShift,Oracle and SQL Component
		/*if(null != oracleTableSchema){
			if(databaseType.equalsIgnoreCase(oracleTableSchema.getDatabaseType())){
				
				for (AbstractWidget abstractWgt : widgets) {
	
					if (abstractWgt.getProperty().getPropertyName()
							.equalsIgnoreCase(Constants.ORACLE_DATABASE_WIDGET_NAME)&& null != oracleTableSchema.getSid()) {
						
						abstractWgt.refresh(oracleTableSchema.getSid());
				
					}else if (abstractWgt.getProperty().getPropertyName().equalsIgnoreCase(Constants.SCHEMA_PROPERTY_NAME)) {
						
						abstractWgt.refresh(getComponentSchema(oracleTableSchema));
						
					}else if (abstractWgt.getProperty().getPropertyName().equalsIgnoreCase(Constants.PARTITION_KEYS_WIDGET_NAME)
							&& null != oracleTableSchema.getPartitionKeys()) {
	
						List<String> keys = new ArrayList<>(Arrays.asList(oracleTableSchema.getPartitionKeys().split(",")));
						
						List<Object> temp = new ArrayList<>();
						temp.add(keys);
						temp.add(getComponentSchema(oracleTableSchema));
						
						abstractWgt.refresh(temp);
						
					} else if (abstractWgt.getProperty().getPropertyName().equalsIgnoreCase(Constants.EXTERNAL_TABLE_PATH_WIDGET_NAME)
							&& null != oracleTableSchema.getExternalTableLocation()) {
						
						abstractWgt.refresh(oracleTableSchema.getExternalTableLocation());
					}
					
					
				}
	
			createMessageDialog(Messages.METASTORE_IMPORT_SUCCESS,INFO).open();
			propertyDialogButtonBar.enableApplyButton(true);
		
		}else{
			createMessageDialog(Messages.INVALID_DB_ERROR,ERROR).open();
		 }
	} else {
		if(StringUtils.isNotBlank(jsonResponse)){
			createMessageDialog(jsonResponse,ERROR).open();
		}else{
			createMessageDialog("Invalid Host Name:" +host,ERROR).open();
		}
}*/
		}
	

	/**
	 * Create the message dialog
	 * @param errorMessage
	 * @return
	 */
	public MessageBox createMessageDialog(String errorMessage, String messageType) {

		MessageBox messageBox = null;
		if ("INF".equalsIgnoreCase(messageType)) {
			messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
			messageBox.setText("Information");

		} else {
			messageBox = new MessageBox(new Shell(), SWT.ERROR | SWT.OK);
			messageBox.setText("Error");
		}

		messageBox.setMessage(errorMessage);
		return messageBox;
	}

	/**
	 * Validates the feild values
	 * @param value
	 * @return
	 */
	private boolean validateField(String value) {
		if (null != value && StringUtils.isNotBlank(value)) {
			return true;
		}
		return false;
	}

	/**
	 * Populates the data in the textBoxes 
	 */
	private void populateWidget() {

		if (null != databaseSelectionConfig) {

			if (databaseSelectionConfig.isTableName()) {
				((Button) tableNameRadioButton.getSWTWidgetControl()).setSelection(true);

				if (validateField(databaseSelectionConfig.getTableName())) {
					tableNameDecorator.hide();
					textBoxTableName.setText(databaseSelectionConfig.getTableName());

				} else {
					tableNameDecorator.show();
				}

			} else {

				((Button) sqlQueryRadioButton.getSWTWidgetControl()).setSelection(true);
				if (validateField(databaseSelectionConfig.getSqlQuery())) {
					sqlQueryDecorator.hide();
					sqlQueryTextBox.setText(databaseSelectionConfig.getSqlQuery());
				} else {
					sqlQueryDecorator.show();
				}

				if (validateField(databaseSelectionConfig.getSqlQueryCounter())) {
					sqlQueryCountertextbox.setText(databaseSelectionConfig.getSqlQueryCounter());

				} 
					 
			}

		} else {
			tableNameDecorator.show();
			sqlQueryDecorator.show();
		}

	}

	/**
	 * Attach listener to textBox widgets
	 * @param textBoxWidget
	 * @param txtDecorator
	 */
	protected void attachListeners(AbstractELTWidget textBoxWidget, ControlDecoration txtDecorator) {
		ListenerHelper helper = prepareListenerHelper(txtDecorator);
		try {
			for (Listners listenerNameConstant : textBoxConfig.getListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				textBoxWidget.attachListener(listener, propertyDialogButtonBar, helper,
						textBoxWidget.getSWTWidgetControl());
			}
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", textBoxConfig.getName(), exception);
		}
	}

	/**
	 * Prepares listener helper 
	 * @param txtDecorator
	 * @return
	 */
	protected ListenerHelper prepareListenerHelper(ControlDecoration txtDecorator) {
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		helper.put(HelperType.CURRENT_COMPONENT, getComponent());
		helper.put(HelperType.CHARACTER_LIMIT, textBoxConfig.getCharacterLimit());
		return helper;
	}
	
	/**
	 * Attach event change listener on TextBoxes
	 * @param textBoxWidget
	 */
	protected void attachListeners(AbstractELTWidget textBoxWidget) {
		try {

			textBoxWidget.attachListener(Listners.EVENT_CHANGE.getListener(), propertyDialogButtonBar, null,
					textBoxWidget.getSWTWidgetControl());
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", textBoxConfig.getName(), exception);
		}
	}
	
	/**
	 * Create Label on Stack layout composite
	 * @param labelName
	 * @param compositeWithStack
	 * @return
	 */
	private AbstractELTWidget createWidgetlabel(String labelName, ELTSubGroupCompositeWithStack compositeWithStack) {
		ELTDefaultLable label = new ELTDefaultLable(labelName).lableWidth(80);
		compositeWithStack.attachWidget(label);
		Label labelAlignment = ((Label) label.getSWTWidgetControl());
		GridData data = (GridData) labelAlignment.getLayoutData();
		data.verticalIndent = 5;
		setPropertyHelpWidget((Control) label.getSWTWidgetControl());

		return label;
	}
	
	/**
	 * Create TextBoxes on Stack layout composite
	 * @param labelName
	 * @param compositeWithStack
	 * @return
	 */
	private AbstractELTWidget createWidgetTextbox(String labelName, ELTSubGroupCompositeWithStack compositeWithStack) {

		AbstractELTWidget textboxWgt = new ELTDefaultTextBox()
				.grabExcessHorizontalSpace(textBoxConfig.getGrabExcessSpace());
		compositeWithStack.attachWidget(textboxWgt);
		Text textbox = ((Text) textboxWgt.getSWTWidgetControl());

		GridData data = (GridData) textbox.getLayoutData();
		data.horizontalIndent = 16;
		data.verticalIndent = 5;
		data.widthHint = 260;
		return textboxWgt;
	}
	
	/**
	 * Attach decorators to the TextBoxes 
	 * @param labelName
	 * @param textboxWgt
	 * @param txtDecorator
	 * @return
	 */
	private ControlDecoration attachDecoratorToTextbox(String labelName, AbstractELTWidget textboxWgt,
			ControlDecoration txtDecorator) {

		txtDecorator = WidgetUtility.addDecorator((Text) textboxWgt.getSWTWidgetControl(),
				Messages.bind(Messages.EMPTY_FIELD, labelName));
		txtDecorator.setMarginWidth(3);
		attachListeners(textboxWgt, txtDecorator);

		return txtDecorator;
	}
}
