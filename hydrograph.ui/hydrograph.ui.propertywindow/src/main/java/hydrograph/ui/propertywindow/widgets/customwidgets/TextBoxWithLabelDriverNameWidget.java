package hydrograph.ui.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory.Listners;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.utils.Utils;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.TextBoxWithLableConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.IELTListener;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

public class TextBoxWithLabelDriverNameWidget extends AbstractWidget {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(TextBoxWithLabelWidget.class);
	protected Text textBox;
	protected String propertyValue;
	protected String propertyName;
	protected ControlDecoration txtDecorator;
	protected TextBoxWithLableConfig textBoxConfig;
	protected ELTDefaultSubgroupComposite lableAndTextBox;
	private Cursor cursor;
	/**
	 * Instantiates a new text box widget with provided configurations
	 * 
	 * @param componentConfigProp
	 *            the component configration property
	 * @param componentMiscProps
	 *            the component miscellaneous properties
	 * @param propDialogButtonBar
	 *            the property dialog button bar
	 */
	public TextBoxWithLabelDriverNameWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propertyName = componentConfigProp.getPropertyName();
		this.propertyValue =  String.valueOf(componentConfigProp.getPropertyValue());
	}

	protected void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
				
		if(txtDecorator.isVisible())
			toolTipErrorMessage = txtDecorator.getDescriptionText();
		
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName, textBox.getText());
		setToolTipErrorMessage();
		return property;
	}
	
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		textBoxConfig = (TextBoxWithLableConfig) widgetConfig;
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		logger.debug("Starting {} textbox creation", textBoxConfig.getName());
		lableAndTextBox = new ELTDefaultSubgroupComposite(container.getContainerControl());
		lableAndTextBox.createContainerWidget();
		
		AbstractELTWidget label = new ELTDefaultLable(textBoxConfig.getName() + " ");
		lableAndTextBox.attachWidget(label);
		setPropertyHelpWidget((Control) label.getSWTWidgetControl());
		
		AbstractELTWidget textBoxWidget = new ELTDefaultTextBox().
				grabExcessHorizontalSpace(textBoxConfig.getGrabExcessSpace()).textBoxWidth(textBoxConfig.getwidgetWidth());
		
		lableAndTextBox.attachWidget(textBoxWidget);
		
		textBox = (Text) textBoxWidget.getSWTWidgetControl();
		textBox.setEnabled(false);
		txtDecorator = WidgetUtility.addDecorator(textBox, Messages.bind(Messages.EMPTY_FIELD, textBoxConfig.getName()));
		txtDecorator.setMarginWidth(3);
		
		attachListeners(textBoxWidget);
		
		 /**
		 *parameter resolution at dev phase 
		 */
		Utils.INSTANCE.loadProperties();
		cursor = container.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		
		populateWidget();
		logger.debug("Finished {} textbox creation", textBoxConfig.getName());
	}

	protected void attachListeners(AbstractELTWidget textBoxWidget) {
		ListenerHelper helper = prepareListenerHelper();
		try {
			for (Listners listenerNameConstant : textBoxConfig.getListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				textBoxWidget.attachListener(listener, propertyDialogButtonBar, helper, textBoxWidget.getSWTWidgetControl());
			}
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", textBoxConfig.getName(), exception);
		}
	}

	protected ListenerHelper prepareListenerHelper() {
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		helper.put(HelperType.CURRENT_COMPONENT, getComponent());
		helper.put(HelperType.CHARACTER_LIMIT, textBoxConfig.getCharacterLimit());
		return helper;
	}
	
	protected void populateWidget(){
		logger.debug("Populating {} textbox", textBoxConfig.getName());
		String property = propertyValue;
		if(StringUtils.isNotBlank(property) ){
			textBox.setText(property);
			txtDecorator.hide();
			Utils.INSTANCE.addMouseMoveListener(textBox, cursor);
		}
		else{
			textBox.setText("");
			txtDecorator.show();
		}
	}
	
	protected boolean isParameter(String input) {
		if (input != null) {
			Matcher matchs = Pattern.compile(Constants.PARAMETER_REGEX).matcher(input);
			if (matchs.matches()) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean isWidgetValid() {
	  return validateAgainstValidationRule(textBox.getText());
	}

	@Override
	public void addModifyListener(final Property property,  final ArrayList<AbstractWidget> widgetList) {
       textBox.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
			 Utils.INSTANCE.addMouseMoveListener(textBox, cursor);
			 showHideErrorSymbol(widgetList);
			}
		});
		
	}
}
