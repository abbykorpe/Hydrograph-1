package hydrograph.ui.propertywindow.widgets.customwidgets;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.slf4j.Logger;

import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.utils.Utils;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;

public class PortWidget extends TextBoxWithLabelWidget{

	private PropertyDialogButtonBar propDialogButtonBar;
	private Cursor cursor;
	private AbstractELTContainerWidget container;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(PortWidget.class);
	
	public PortWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propDialogButtonBar = propDialogButtonBar;
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		this.container=container;
		super.attachToPropertySubGroup(container);
		
	}
	
	@Override
	protected void populateWidget() {
		Utils.INSTANCE.loadProperties();
		cursor = container.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		
		logger.trace("Populating {} textbox", textBoxConfig.getName());
		String property = propertyValue;
		if(StringUtils.isNotBlank(property) ){
			textBox.setText(property);
			Utils.INSTANCE.addMouseMoveListener(textBox, cursor);
		}
		else{
			textBox.setText("");
			txtDecorator.show();
		}
	}
}
