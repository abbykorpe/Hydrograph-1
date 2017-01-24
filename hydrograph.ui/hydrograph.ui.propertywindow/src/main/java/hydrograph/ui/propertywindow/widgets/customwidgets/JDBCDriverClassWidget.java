package hydrograph.ui.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

public class JDBCDriverClassWidget extends AbstractWidget{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(UpdateByKeysWidget.class);
	private String propertyName;
	private String propertyValue;
	private Text textBox;

	public JDBCDriverClassWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propertyName = componentConfigProp.getPropertyName();
		this.propertyValue =  String.valueOf(componentConfigProp.getPropertyValue());
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		logger.debug("Starting {} button creation");
		
		ELTDefaultSubgroupComposite jdbcDriverClassComposite = new ELTDefaultSubgroupComposite(subGroup.getContainerControl());
		jdbcDriverClassComposite.createContainerWidget();
		
		ELTDefaultLable defaultLable = new ELTDefaultLable("JDBC Driver \n Class");
		jdbcDriverClassComposite.attachWidget(defaultLable);
		setPropertyHelpWidget((Control) defaultLable.getSWTWidgetControl());
		
		ELTDefaultTextBox defaultTextBox = new ELTDefaultTextBox();
		jdbcDriverClassComposite.attachWidget(defaultTextBox);
		 textBox=(Text)defaultTextBox.getSWTWidgetControl();
		setPropertyHelpWidget((Control) defaultTextBox.getSWTWidgetControl());	
		
	}
	
	public void setTextValue(String text){
		textBox.setText(text);
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		return null;
	}

	@Override
	public boolean isWidgetValid() {
		return false;
	}

	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
		
	}

}
