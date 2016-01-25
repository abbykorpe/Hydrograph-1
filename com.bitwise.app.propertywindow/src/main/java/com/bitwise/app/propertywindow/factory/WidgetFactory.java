package com.bitwise.app.propertywindow.factory;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.fixedwidthschema.ELTFixedWidget;
import com.bitwise.app.propertywindow.fixedwidthschema.ELTJoinFixedWidthSchemaWidget;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.DropDownWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTComponentBaseType;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTComponentNameWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTComponentType;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTFilePathWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTJoinMapWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTJoinWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTLookupConfigWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTLookupMapWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTOperationClassWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTRetentionlogicWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTXmlPropertiesContainer;
import com.bitwise.app.propertywindow.widgets.customwidgets.SingleColumnWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.TextBoxWithLabelWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.WidgetHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinproperty.ELTJoinPortCount;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.TransformWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty.ELTRuntimePropertiesWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.ELTGenericSchemaGridWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys.SecondaryColumnKeysWidget;

/**
 * Factory for creating Widgets
 * @author Bitwise
 * Sep 08, 2015
 * 
 */

public class WidgetFactory {
	public static final WidgetFactory INSTANCE = new WidgetFactory();
	private static final Logger logger = LogFactory.INSTANCE.getLogger(WidgetFactory.class);
	
	public enum Widgets{
		SCHEMA_WIDGET(ELTGenericSchemaGridWidget.class),
		FIXED_WIDGET(ELTFixedWidget.class),
		JOIN_FIXED_WIDTH_SCHEMA_WIDGET(ELTJoinFixedWidthSchemaWidget.class),
		FILE_PATH_WIDGET(ELTFilePathWidget.class),
		COMPONENT_NAME_WIDGET(ELTComponentNameWidget.class),
		
		COMPONENT_BASETYPE_WIDGET(ELTComponentBaseType.class),
		COMPONENT_TYPE_WIDGET(ELTComponentType.class),

		RETENTION_LOGIC_WIDGET(ELTRetentionlogicWidget.class),

		STRICT_CLASS_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getStrictWidgetConfig()),
		SAFE_PROPERTY_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getSafeWidgetConfig()),
		CHARACTER_SET_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getCharacterSetWidgetConfig()),
		HAS_HEADER_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getHasHeaderWidgetConfig()),

		DELIMETER_WIDGET(TextBoxWithLabelWidget.class, WidgetHelper.INSTANCE.getDelimiterWidgetConfig()),
		PHASE_WIDGET(TextBoxWithLabelWidget.class, WidgetHelper.INSTANCE.getPhaseWidgetConfig()),

		FILTER_PROPERTY_WIDGET(SingleColumnWidget.class, WidgetHelper.INSTANCE.getOperationFieldsConfig()),
		COLUMN_NAME_WIDGET(SingleColumnWidget.class, WidgetHelper.INSTANCE.getColumnNameConfig()),

		OPERATIONAL_CLASS_WIDGET(ELTOperationClassWidget.class, WidgetHelper.INSTANCE.getOperationClassForFilterWidgetConfig()),
		RUNTIME_PROPERTIES_WIDGET(ELTRuntimePropertiesWidget.class),
		SECONDARY_COLUMN_KEYS_WIDGET(SecondaryColumnKeysWidget.class),
		
		TRANSFORM_WIDGET(TransformWidget.class,WidgetHelper.INSTANCE.getOperationClassForTransformWidgetConfig()),
		AGGREGATE_WIDGET(TransformWidget.class,WidgetHelper.INSTANCE.getOperationClassForAggregateWidgetConfig()),
		XML_CONTENT_WIDGET(ELTXmlPropertiesContainer.class),
		INPUT_COUNT_WIDGET(ELTJoinPortCount.class,WidgetHelper.INSTANCE.getInputCountWidgetConfig()),
		JOIN_TYPE_WIDGET(ELTJoinWidget.class),
		JOIN_MAPPING_WIDGET(ELTJoinMapWidget.class),
		HASH_JOIN_WIDGET(ELTLookupConfigWidget.class),
		HASH_JOIN_MAPPING_WIDGET(ELTLookupMapWidget.class);
		
		private Class<?> clazz = null;
		private WidgetConfig widgetConfig = null;
		
		private Widgets(Class<?> clazz) {
			this.clazz = clazz;
		}
		
		private Widgets(Class<?> clazz, WidgetConfig widgetConfig) {
			this.clazz = clazz;
			this.widgetConfig = widgetConfig;
		}
		
		public Class<?> getClazz(){
			return this.clazz;
		}
		
		public WidgetConfig getWidgetConfig() {
			return widgetConfig;
		}
	}

	public AbstractWidget getWidget(String widgetName, ComponentConfigrationProperty componentConfigProperty, 
			ComponentMiscellaneousProperties componentMiscProperties, PropertyDialogButtonBar propertyDialogButtonBar){
		try {
			Widgets widget = Widgets.valueOf(widgetName);
			AbstractWidget abstractWidget = (AbstractWidget) widget.getClazz().getDeclaredConstructor(ComponentConfigrationProperty.class,
					ComponentMiscellaneousProperties.class,	PropertyDialogButtonBar.class).
					newInstance(componentConfigProperty, componentMiscProperties, propertyDialogButtonBar);
			abstractWidget.setWidgetConfig(widget.getWidgetConfig());
			return abstractWidget;
		
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
				InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			logger.error("Failed to create widget for class : {}, {}", widgetName, exception);
			throw new RuntimeException("Failed to instantiate the Listner {}" + widgetName);
		}
	}
}
