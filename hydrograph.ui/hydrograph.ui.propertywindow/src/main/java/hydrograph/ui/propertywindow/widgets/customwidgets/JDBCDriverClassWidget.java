package hydrograph.ui.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.JDBCDriverClassWidgetDatastructure;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.datastructures.ComboBoxParameter;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory.Listners;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.DropDownConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultCombo;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.IELTListener;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

public class JDBCDriverClassWidget extends AbstractWidget{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(UpdateByKeysWidget.class);
	private String propertyName;
	private JDBCDriverClassWidgetDatastructure propertyValue;
	private Text jdbcDriverClassTextBox;
	private DropDownConfig dropDownConfig;
	private Combo combo;
	private LinkedHashMap<String, Object> property=new LinkedHashMap<>();
	private ComboBoxParameter comboBoxParameter=new ComboBoxParameter();
	private List<AbstractWidget> widgetList;
	private Text parameterTextBox;
	private ControlDecoration paramterTextBoxDecorator;
	private ControlDecoration jdbcDriverClassTextBoxDecorator;
	private LinkedHashMap<String, String> map;
	public JDBCDriverClassWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propertyName = componentConfigProp.getPropertyName();
		this.propertyValue =  (JDBCDriverClassWidgetDatastructure)componentConfigProp.getPropertyValue();
		if(propertyValue==null){
			propertyValue=new JDBCDriverClassWidgetDatastructure();
			propertyValue.setJdbcDriverClassValue(Constants.THIN);
		}
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		logger.debug("Starting {} button creation");
		
		ELTDefaultSubgroupComposite jdbcDriverClassComposite = new ELTDefaultSubgroupComposite(subGroup.getContainerControl());
		jdbcDriverClassComposite.createContainerWidget();
		
		AbstractELTWidget defaultLabel = new ELTDefaultLable(dropDownConfig.getName());
		jdbcDriverClassComposite.attachWidget(defaultLabel);
		setPropertyHelpWidget((Control) defaultLabel.getSWTWidgetControl());
		
		AbstractELTWidget defaultCombo = new ELTDefaultCombo().defaultText(convertToArray(dropDownConfig.getItems()));
		jdbcDriverClassComposite.attachWidget(defaultCombo);
		combo=(Combo)defaultCombo.getSWTWidgetControl();
		combo.select(0);
		
		ELTDefaultTextBox eltDefaultTextBox = new ELTDefaultTextBox().grabExcessHorizontalSpace(true);
		jdbcDriverClassComposite.attachWidget(eltDefaultTextBox);
		eltDefaultTextBox.visibility(false);
		parameterTextBox=(Text)eltDefaultTextBox.getSWTWidgetControl();
		
		
		ELTDefaultLable driverClassLable = new ELTDefaultLable(Messages.LABEL_JDBC_DRIVER_CLASS);
		jdbcDriverClassComposite.attachWidget(driverClassLable);
		setPropertyHelpWidget((Control) driverClassLable.getSWTWidgetControl());
		
		ELTDefaultTextBox textBoxWidget = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(120);
		jdbcDriverClassComposite.attachWidget(textBoxWidget);
		jdbcDriverClassTextBox=(Text)textBoxWidget.getSWTWidgetControl();
		jdbcDriverClassTextBoxDecorator = WidgetUtility.addDecorator(jdbcDriverClassTextBox, Messages.bind(Messages.EMPTY_FIELD, Messages.LABEL_JDBC_DRIVER_CLASS));
		jdbcDriverClassTextBoxDecorator.setMarginWidth(3);
		jdbcDriverClassTextBoxDecorator.hide();
		ListenerHelper helper1 = new ListenerHelper();
		helper1.put(HelperType.CONTROL_DECORATION, jdbcDriverClassTextBoxDecorator);

		try {
			textBoxWidget.attachListener(ListenerFactory.Listners.MODIFY.getListener(), propertyDialogButtonBar,
					helper1, textBoxWidget.getSWTWidgetControl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		setPropertyHelpWidget((Control) textBoxWidget.getSWTWidgetControl());	
		paramterTextBoxDecorator = WidgetUtility.addDecorator(parameterTextBox, Messages.bind(Messages.EMPTY_FIELD, dropDownConfig.getName()));
		
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, paramterTextBoxDecorator);
		
		
		try {
			for (Listners listenerNameConstant : dropDownConfig.getDropDownListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				defaultCombo.attachListener(listener,propertyDialogButtonBar, helper,defaultCombo.getSWTWidgetControl(),
						eltDefaultTextBox.getSWTWidgetControl());
			}
			for (Listners listenerNameConstant : dropDownConfig.getTextBoxListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				eltDefaultTextBox.attachListener(listener, propertyDialogButtonBar, helper,eltDefaultTextBox.getSWTWidgetControl());
			}
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", dropDownConfig.getName(), exception);
		}
		
		addComboSelectionListner();
		 populateWidget();
		
	}
	
	
	
	
	private void populateWidget(){	
		if(propertyValue.isParameter()){
			parameterTextBox.setVisible(true);
			parameterTextBox.setText(propertyValue.getDataBaseValue());
			combo.select(dropDownConfig.getItems().indexOf(Constants.PARAMETER));
		}else{
			if(dropDownConfig.getItems().contains(propertyValue.getDataBaseValue())){
				int indexOf = dropDownConfig.getItems().indexOf(propertyValue.getDataBaseValue());
				combo.select(indexOf);
			}
		}
			jdbcDriverClassTextBox.setText(propertyValue.getJdbcDriverClassValue());
	}
	
	private boolean addComboSelectionListner() {
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				showHideErrorSymbol(widgetList);
				String str = ((Combo)event.getSource()).getText();
				if(dropDownConfig.getItems().contains(str)){
					jdbcDriverClassTextBoxDecorator.hide();
					jdbcDriverClassTextBox.setBackground(new Color(Display.getDefault(), 255, 255, 255));
					if(str.equalsIgnoreCase(Constants.ORACLE)){
						jdbcDriverClassTextBox.setText(Constants.THIN);
					}else if(str.equalsIgnoreCase(Constants.REDSHIFT)){
						jdbcDriverClassTextBox.setText(Constants.REDSHIFT_DRIVER_CLASS);
					}else if(str.equalsIgnoreCase(Constants.MYSQL)){
						jdbcDriverClassTextBox.setText(Constants.MYSQL_DRIVER_CLASS);
					}else if(str.equalsIgnoreCase(Constants.TERADATA)){
						jdbcDriverClassTextBox.setText(Constants.TERADATA_DRIVER_CLASS);
					}else if(str.equalsIgnoreCase(Constants.PARAMETER)){
						jdbcDriverClassTextBox.setText("");
						jdbcDriverClassTextBox.setBackground(new Color(Display.getDefault(), 255, 255, 204));
						jdbcDriverClassTextBoxDecorator.setMarginWidth(3);
						jdbcDriverClassTextBoxDecorator.show();
					}
				}
			}
			
		});
		return true;
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		JDBCDriverClassWidgetDatastructure datastructure = new JDBCDriverClassWidgetDatastructure();
		LinkedHashMap<String, Object> propertymap = new LinkedHashMap<>();
		if( combo.getText().equalsIgnoreCase(Constants.PARAMETER)){
			/*comboBoxParameter.setOption(text.getText());
			comboBoxParameter.setOptionValue(text.getText());*/
			datastructure.setParameter(true);
			datastructure.setJdbcDriverClassValue(jdbcDriverClassTextBox.getText());
			datastructure.setDataBaseValue(parameterTextBox.getText());
		}else{
			datastructure.setDataBaseValue(combo.getText());
			datastructure.setJdbcDriverClassValue(jdbcDriverClassTextBox.getText());
		}
		propertymap.put(this.propertyName, datastructure);
		setToolTipErrorMessage();
		return propertymap;
	}

	@Override
	public boolean isWidgetValid() {
		return true;
//		if (propertyValue != null && StringUtils.isNotBlank(propertyValue.getDataBaseValue())
//				&& StringUtils.isNotBlank(propertyValue.getJdbcDriverClassValue())) {
//			return true;
//		}
//		return false;
	}
	
	private void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
		if(paramterTextBoxDecorator.isVisible())
			toolTipErrorMessage = paramterTextBoxDecorator.getDescriptionText();
						
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
		this.widgetList = widgetList;
		   parameterTextBox.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
				 showHideErrorSymbol(widgetList);
				}
			});
			
	}
	
	private String[] convertToArray(List<String> items) {
		String[] stringItemsList = new String[items.size()];
		int index = 0;
		for (String item : items) {
			stringItemsList[index++] = item;
		}
		return stringItemsList;
	}
	
	
	@Override
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		this.dropDownConfig = (DropDownConfig) widgetConfig;
	}
	
}
