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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

/***
 * The class to test the connection for different DB components
 * @author Bitwise
 *
 */
public class DatabaseTestConnectionWidget extends AbstractWidget{
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(DatabaseTestConnectionWidget.class);
	private Map<String, String> initialMap;
	private String propertyName;
	protected ControlDecoration buttonDecorator;
	private Button testConnectionButton;
	private ArrayList<AbstractWidget> widgets;
	private static final String DEFAULT_PORTNO = "8004";
	private static final String PORT_NO = "portNo";
	private static final String HOST = "host";
	private static final String PLUGIN_ID = "hydrograph.ui.dataviewer";
	
	public DatabaseTestConnectionWidget(
			ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps,
			PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);

		this.propertyName = componentConfigProp.getPropertyName();
		if(initialMap==null){
			this.initialMap = new LinkedHashMap<String, String>();
		}
		
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		logger.debug("Starting {} button creation");
		ELTDefaultSubgroupComposite testConnectionComposite = new ELTDefaultSubgroupComposite(subGroup.getContainerControl());
		testConnectionComposite.createContainerWidget();
		

		ELTDefaultLable defaultLable1 = new ELTDefaultLable("");
		testConnectionComposite.attachWidget(defaultLable1);
		setPropertyHelpWidget((Control) defaultLable1.getSWTWidgetControl());
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(Messages.TEST_CONNECTION);

		testConnectionComposite.attachWidget(eltDefaultButton);
		 testConnectionButton=(Button)eltDefaultButton.getSWTWidgetControl();
		testConnectionButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,false,false,0,0));

		buttonDecorator = WidgetUtility.addDecorator(
				(Control) eltDefaultButton.getSWTWidgetControl(),
				Messages.bind(Messages.EmptyValueNotification,Messages.TEST_CONNECTION));
		if (OSValidator.isMac()) {
			buttonDecorator.setMarginWidth(-2);
		}
		else{
			buttonDecorator.setMarginWidth(3);
		}
		
		attachButtonListner(testConnectionButton);
		setDecoratorsVisibility();
		
	}

	/**
	 * Attaches selection listener on TestConnection button
	 * @param testConnectionButton
	 */
	private void attachButtonListner(Button testConnectionButton) {
		
		testConnectionButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO
				String host = Platform.getPreferencesService().getString(PLUGIN_ID, HOST, "", null);
				String port_no = Platform.getPreferencesService().getString(PLUGIN_ID, PORT_NO, DEFAULT_PORTNO, null);
				
				if (null != host && StringUtils.isNotBlank(host)) {
					
					if (getDatabaseConnectionDetails()) {
					
					}
				}
			}
		});
		
	}
	
	/**
	 * Provides the value for all the DB details
	 * @return 
	 */
	private boolean getDatabaseConnectionDetails() {
		
		//TODO
		
		String oracleDatabaseName = "";
		String oracleHostName = "";
		String oraclePortNo = "";
		String oracleJdbcName = "";
		String oracleSchemaName = "";
		String oracleUserName = "";
		String oraclePassword = "";
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
			}else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_SCHEMA_WIDGET_NAME)) {
				oracleSchemaName = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_SCHEMA_WIDGET_NAME);
			}else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_USER_NAME_WIDGET_NAME)) {
				oracleUserName = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_USER_NAME_WIDGET_NAME);
			}else if (textAbtractWgt.getProperty().getPropertyName()
					.equalsIgnoreCase(Constants.ORACLE_PASSWORD_WIDGET_NAME)) {
				oraclePassword = (String) textAbtractWgt.getProperties().get(Constants.ORACLE_PASSWORD_WIDGET_NAME);
			}

		}

		if (StringUtils.isNotEmpty(oracleDatabaseName) && StringUtils.isNotEmpty(oracleHostName)
				&& StringUtils.isNotEmpty(oracleJdbcName) && StringUtils.isNotEmpty(oraclePortNo) && StringUtils.isNotEmpty(oracleSchemaName)
						&& StringUtils.isNotEmpty(oracleUserName) && StringUtils.isNotEmpty(oraclePassword)) {

			return true;
		}

		return false;
	}
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property = new LinkedHashMap<>();
		property.put(propertyName, this.initialMap);

		setToolTipErrorMessage();
		return property;
	}
	

	@Override
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(initialMap);
	}

	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
			widgets = widgetList;
		}
		
	/**
	 * Sets the tool tip error message
	 */
	protected void setToolTipErrorMessage() {
		String toolTipErrorMessage = null;

		if (buttonDecorator.isVisible())
			toolTipErrorMessage = buttonDecorator.getDescriptionText();

		setToolTipMessage(toolTipErrorMessage);
	}
	
	/**
	 * Show or hides the decorator
	 */
	protected void setDecoratorsVisibility() {

		if (!isWidgetValid()) {
			buttonDecorator.show();
		} else {
          buttonDecorator.hide();
		}

	}
	

}
