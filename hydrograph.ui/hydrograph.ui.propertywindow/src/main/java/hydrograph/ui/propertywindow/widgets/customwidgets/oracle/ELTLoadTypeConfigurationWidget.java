package hydrograph.ui.propertywindow.widgets.customwidgets.oracle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.RuntimeConfig;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

public class ELTLoadTypeConfigurationWidget extends AbstractWidget {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTLoadTypeConfigurationWidget.class);
	private Shell shell;
	private RuntimeConfig runtimeConfig;
	private List<AbstractWidget> widgets;
	protected ControlDecoration buttonDecorator;
	private String propertyName;
	List<String> schemaFeilds;
	LinkedHashMap<String, Object> tempPropertyMap;

	private Map<String, String> initialMap;
	
	/**
	 * Instantiates a new ELTLoadTypeConfiguration widget.
	 * 
	 * @param componentConfigProp
	 *            the component configration property
	 * @param componentMiscProps
	 *            the component miscellaneous properties
	 * @param propDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTLoadTypeConfigurationWidget(
			ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps,
			PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);

		this.propertyName = componentConfigProp.getPropertyName();
		this.initialMap = (Map<String, String>) componentConfigProp
				.getPropertyValue();
if(initialMap==null)
	this.initialMap = new LinkedHashMap<String, String>();
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {

		ELTDefaultSubgroupComposite loadConfigurationComposite = new ELTDefaultSubgroupComposite(
				subGroup.getContainerControl());
		loadConfigurationComposite.createContainerWidget();
		shell = loadConfigurationComposite.getContainerControl().getShell();
		runtimeConfig = (RuntimeConfig) widgetConfig;

		ELTDefaultLable defaultLable1 = new ELTDefaultLable(
				runtimeConfig.getLabel());
		loadConfigurationComposite.attachWidget(defaultLable1);
		setPropertyHelpWidget((Control) defaultLable1.getSWTWidgetControl());

		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(Constants.EDIT);

		loadConfigurationComposite.attachWidget(eltDefaultButton);

		buttonDecorator = WidgetUtility.addDecorator(
				(Control) eltDefaultButton.getSWTWidgetControl(),
				Messages.bind(Messages.EmptyValueNotification, runtimeConfig.getLabel()));
		if (OSValidator.isMac()) {
			buttonDecorator.setMarginWidth(-2);
		}
		else{
			buttonDecorator.setMarginWidth(3);
		}
		setDecoratorsVisibility();
		

		try {
			eltDefaultButton
					.attachListener(
							ListenerFactory.Listners.RUNTIME_BUTTON_CLICK
									.getListener(),
							propertyDialogButtonBar,
							new ListenerHelper(this.getClass().getName(), this),
							eltDefaultButton.getSWTWidgetControl());

		} catch (Exception exception) {
			logger.error(
					"Error occured while attaching listener to Runtime Properties window",
					exception);
		}
	}
	
	/**
	 * New window launcher.
	 */
	public void newWindowLauncher() {
		schemaFeilds=getPropagatedSchema();
		
		initialMap=new LinkedHashMap<>(initialMap); 
		LoadTypeConfigurationDialog loadTypeConfigurationPropertyDialog = new LoadTypeConfigurationDialog(
			shell, propertyDialogButtonBar, runtimeConfig.getWindowLabel(), schemaFeilds,initialMap);
	
		loadTypeConfigurationPropertyDialog.open();
		if (loadTypeConfigurationPropertyDialog.isOkPressed()) {

			initialMap = loadTypeConfigurationPropertyDialog.getSelectedPropertyValue();
			
			showHideErrorSymbol(widgets);
		}
		setDecoratorsVisibility();

	}
	
	protected List<String> getPropagatedSchema() {
		List<String> list = new ArrayList<String>();
		Schema schema = (Schema) getComponent().getProperties().get(
				Constants.SCHEMA_PROPERTY_NAME);
		if (schema != null && schema.getGridRow() != null) {
			List<GridRow> gridRows = schema.getGridRow();
			if (gridRows != null) {
				for (GridRow gridRow : gridRows) {
					list.add(gridRow.getFieldName());
				}
			}
		}
		return list;
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		tempPropertyMap = new LinkedHashMap<>();
		tempPropertyMap.put(this.propertyName, this.initialMap);
		setToolTipErrorMessage();
		return tempPropertyMap;
	}

	@Override
	public boolean isWidgetValid() {
		 return validateAgainstValidationRule(initialMap);
	}

	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
			widgets = widgetList;
		
	}
	
	protected void setToolTipErrorMessage() {
		String toolTipErrorMessage = null;

		if (buttonDecorator.isVisible())
			toolTipErrorMessage = buttonDecorator.getDescriptionText();

		setToolTipMessage(toolTipErrorMessage);
	}
	
	protected void setDecoratorsVisibility() {

		if (!isWidgetValid()) {
			buttonDecorator.show();
		} else {
          buttonDecorator.hide();
		}

	}
	

}
