package hydrograph.ui.propertywindow.widgets.customwidgets;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

public class DelimiterWidget extends TextBoxWithLabelWidget{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DelimiterWidget.class);
	
	public DelimiterWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps,
			PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		
	}

	protected void populateWidget(){
		logger.debug("Populating {} textbox", textBoxConfig.getName());
		String property = propertyValue;
		if(!StringUtils.isEmpty(property) ){
			textBox.setText(property);
			txtDecorator.hide();
		}
		else{
			textBox.setText("");
			txtDecorator.show();
		}
	}
}
