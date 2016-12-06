package hydrograph.ui.propertywindow.widgets.customwidgets.oracle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.PreferenceConstants;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
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
import hydrograph.ui.propertywindow.widgets.customwidgets.metastore.HiveTableSchema;
import hydrograph.ui.propertywindow.widgets.customwidgets.metastore.OracleTableSchema;
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

public class ELTSelectionDatabaseWidget extends AbstractWidget {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTLoadTypeConfigurationWidget.class);
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
	private static final String PLUGIN_ID = "hydrograph.ui.dataviewer";
	private Cursor cursor;
	private String sqlQueryStatement;
	private static final String SEPARATOR = "|";
	private ControlDecoration sqlQueryCounterDecorator;
	private Text sqlQueryCountertextbox;
	private ModifyListener sqlQueryCounterModifyListner;
	private String oracleDatabaseName;
	private String oracleHostName;
	private String oraclePortNo;
	private String oracleJdbcName;
	private String oracleSchemaName;
	private String oracleUserName;
	private String oraclePassword;
	private String databaseType;
	private OracleTableSchema oracleTableSchema;
	private static final String ORACLE = "Oracle";
	private static final String REDSHIFT = "RedShift";

	public ELTSelectionDatabaseWidget(ComponentConfigrationProperty componentConfigProp,
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

		selectLable = new ELTDefaultLable("Select");
		eltSuDefaultSubgroupComposite.attachWidget(selectLable);

		tableNameRadioButton = new ELTRadioButton("TableName");
		eltSuDefaultSubgroupComposite.attachWidget(tableNameRadioButton);
		propertyDialogButtonBar.enableApplyButton(true);

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		sqlQueryRadioButton = new ELTRadioButton("SQL Query");
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

	private void createSQLQueryComposite(ELTSubGroupCompositeWithStack selectionComposite) {

		Utils.INSTANCE.loadProperties();
		cursor = selectionComposite.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		sqlQueryComposite = new ELTSubGroupCompositeWithStack(selectionComposite.getContainerControl());
		sqlQueryComposite.createContainerWidget();

		sqlQueryComposite.numberOfBasicWidgets(3);

		createWidgetlabel("SQLQuery", sqlQueryComposite);
		AbstractELTWidget sqlQueryWgt = createWidgetTextbox("SQLQuery", sqlQueryComposite);
		sqlQueryDecorator = attachDecoratorToTextbox("SQLQuery", sqlQueryWgt, sqlQueryDecorator);
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
		openSQLQueryStatement(sqlQueryButtonWgt);

		createWidgetlabel("Query Counter", sqlQueryComposite);
		AbstractELTWidget sqlQueryCounterWgt = createWidgetTextbox("Query Counter", sqlQueryComposite);
		/*
		 * sqlQueryCounterDecorator = attachDecoratorToTextbox("Query Counter",
		 * sqlQueryCounterWgt, sqlQueryCounterDecorator);
		 */
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
		openSQLQueryCounterStatement(sqlQueryCounterButtonWgt);

	}

	private void openSQLQueryCounterStatement(ELTDefaultButton sqlQueryCounterButtonWgt) {

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

	private void openSQLQueryStatement(ELTDefaultButton sqlQueryButtonWgt) {

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

	private void registerTextBoxListner(boolean isTableNameRadioButton) {

		if (isTableNameRadioButton) {
			textBoxTableName.addModifyListener(textboxTableNameModifyListner);
		} else {
			sqlQueryTextBox.addModifyListener(textboxSQLQueryModifyListner);
			sqlQueryCountertextbox.addModifyListener(sqlQueryCounterModifyListner);
		}

	}

	public void setWidgetConfig(WidgetConfig widgetConfig) {
		textBoxConfig = (TextBoxWithLableConfig) widgetConfig;
	}

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

		/*
		 * if (sqlQueryCounterDecorator.isVisible()) { toolTipErrorMessage =
		 * sqlQueryCounterDecorator.getDescriptionText();
		 * setToolTipMessage(toolTipErrorMessage); }
		 */

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

	private ModifyListener attachTextModifyListner(final ArrayList<AbstractWidget> widgetList) {
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				Utils.INSTANCE.addMouseMoveListener(sqlQueryTextBox, cursor);
				Utils.INSTANCE.addMouseMoveListener(textBoxTableName, cursor);
				Utils.INSTANCE.addMouseMoveListener(sqlQueryCountertextbox, cursor);
				showHideErrorSymbol(widgetList);

			}
		};
	}

	private void createTableNameComposite(ELTSubGroupCompositeWithStack eltSuDefaultSubgroupComposite) {

		Utils.INSTANCE.loadProperties();
		cursor = eltSuDefaultSubgroupComposite.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		tableComposite = new ELTSubGroupCompositeWithStack(eltSuDefaultSubgroupComposite.getContainerControl());
		tableComposite.createContainerWidget();

		tableComposite.numberOfBasicWidgets(2);
		createWidgetlabel("Table Name", tableComposite);
		AbstractELTWidget tableNameWgt = createWidgetTextbox("TableName", tableComposite);
		tableNameDecorator = attachDecoratorToTextbox("TableName", tableNameWgt, tableNameDecorator);
		textBoxTableName = (Text) tableNameWgt.getSWTWidgetControl();

		attachListeners(tableNameWgt);

		createWidgetlabel("Import from \n Metastore", tableComposite);
		ELTDefaultButton editButton = new ELTDefaultButton(Messages.EXTRACT);
		tableComposite.attachWidget(editButton);

		Button button = (Button) editButton.getSWTWidgetControl();
		GridData data = new GridData(SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		data.widthHint = 92;
		data.horizontalIndent = 15;
		button.setLayoutData(data);

		button.addSelectionListener(attachButtonSelectionListner());

	}

	/**
	 * 
	 */
	private boolean getOracleTableDetailsFromWidgets() {

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
			
			if(ORACLE.equalsIgnoreCase(getComponent().getType())){
				databaseType=ORACLE;
			}else{
				databaseType=REDSHIFT;
			}
		}

		if (StringUtils.isNotEmpty(oracleDatabaseName) && StringUtils.isNotEmpty(oracleHostName)
				&& StringUtils.isNotEmpty(oracleJdbcName) && StringUtils.isNotEmpty(oraclePortNo)
				&& StringUtils.isNotEmpty(oracleSchemaName) && StringUtils.isNotEmpty(oracleUserName)
				&& StringUtils.isNotEmpty(oraclePassword)) {

			return true;
		}

		return false;
	}

	private SelectionAdapter attachButtonSelectionListner() {

		SelectionAdapter adapter = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String host = Platform.getPreferencesService().getString(PLUGIN_ID, PreferenceConstants.REMOTE_HOST, "",
						null);
				String port_no = Platform.getPreferencesService().getString(PLUGIN_ID,
						PreferenceConstants.REMOTE_PORT_NO, PreferenceConstants.DEFAULT_PORT_NO, null);

				if (null != host && StringUtils.isNotBlank(host)) {

					if (getOracleTableDetailsFromWidgets()) {
						List<String> oracleDatabaseValues = new ArrayList<String>();
						LinkedHashMap<String, Object> property = getProperties();
						databaseSelectionConfig = (DatabaseSelectionConfig) property.get(propertyName);
						if (((Button) tableNameRadioButton.getSWTWidgetControl()).getSelection()) {
							oracleDatabaseValues.add(databaseSelectionConfig.getTableName());
						}else{
							createMessageDialog(Messages.METASTORE_FORMAT_ERROR_FOR_SQL_QUERY, INFO).open();
						}
						if (oracleDatabaseValues != null && oracleDatabaseValues.size() > 0) {
							extractOracleMetaStoreDetails(oracleDatabaseValues, host, port_no);
						}
					} else {
						createMessageDialog(Messages.METASTORE_FORMAT_ERROR, ERROR).open();
					}

				} else {
					createMessageDialog(Messages.HOST_NAME_BLANK_ERROR, ERROR).open();
				}

			}

		};

		return adapter;
	}

	private void extractOracleMetaStoreDetails(List<String> oracleDatabaseValues, String host, String port_no) {

		String jsonResponse = "";

		try {
			if (StringUtils.isEmpty(oracleSchemaName) || StringUtils.isBlank(oracleSchemaName)) {
				oracleSchemaName = "";
			}
			ObjectMapper mapper = new ObjectMapper();
			String input = oracleDatabaseName + SEPARATOR + oracleHostName + SEPARATOR + oracleJdbcName + SEPARATOR
					+ oraclePassword + SEPARATOR + oraclePortNo + SEPARATOR + oracleUserName + SEPARATOR
					+ oracleSchemaName;
			jsonResponse = DebugServiceClient.INSTANCE.readMetaStoreDb(input, host, port_no, oracleDatabaseValues);
			oracleTableSchema = mapper.readValue(jsonResponse,
					OracleTableSchema.class);

		} catch (NumberFormatException | HttpException | MalformedURLException exp) {
			logger.error("Json to object Mapping issue ", exp);
		} catch (IOException ex) {
			logger.error("Json to object Mapping issue ", ex.getMessage());
		}
		
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
	 * 
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

	private boolean validateField(String value) {
		if (null != value && StringUtils.isNotBlank(value)) {
			return true;
		}
		return false;
	}

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
					// sqlQueryCounterDecorator.hide();
					sqlQueryCountertextbox.setText(databaseSelectionConfig.getSqlQueryCounter());

				} /*
					 * else { sqlQueryCounterDecorator.show(); }
					 */
			}

		} else {
			tableNameDecorator.show();
			sqlQueryDecorator.show();
			// sqlQueryCounterDecorator.show();
		}

	}

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

	protected ListenerHelper prepareListenerHelper(ControlDecoration txtDecorator) {
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		helper.put(HelperType.CURRENT_COMPONENT, getComponent());
		helper.put(HelperType.CHARACTER_LIMIT, textBoxConfig.getCharacterLimit());
		return helper;
	}

	protected void attachListeners(AbstractELTWidget textBoxWidget) {
		try {

			textBoxWidget.attachListener(Listners.EVENT_CHANGE.getListener(), propertyDialogButtonBar, null,
					textBoxWidget.getSWTWidgetControl());
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", textBoxConfig.getName(), exception);
		}
	}

	private AbstractELTWidget createWidgetlabel(String labelName, ELTSubGroupCompositeWithStack compositeWithStack) {
		ELTDefaultLable label = new ELTDefaultLable(labelName).lableWidth(80);
		compositeWithStack.attachWidget(label);
		Label labelAlignment = ((Label) label.getSWTWidgetControl());
		GridData data = (GridData) labelAlignment.getLayoutData();
		data.verticalIndent = 5;
		setPropertyHelpWidget((Control) label.getSWTWidgetControl());

		return label;
	}

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

	private ControlDecoration attachDecoratorToTextbox(String labelName, AbstractELTWidget textboxWgt,
			ControlDecoration txtDecorator) {

		txtDecorator = WidgetUtility.addDecorator((Text) textboxWgt.getSWTWidgetControl(),
				Messages.bind(Messages.EMPTY_FIELD, labelName));
		txtDecorator.setMarginWidth(3);
		attachListeners(textboxWgt, txtDecorator);

		return txtDecorator;
	}
}
