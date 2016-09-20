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

 
package hydrograph.ui.propertywindow.factory;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.filemixedschema.ELTMixedSchemeWidget;
import hydrograph.ui.propertywindow.fixedwidthschema.ELTFixedWidget;
import hydrograph.ui.propertywindow.fixedwidthschema.TransformSchemaWidget;
import hydrograph.ui.propertywindow.generaterecords.schema.GenerateRecordsGridWidget;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.DelimiterWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.DropDownWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTBrowseWorkspaceWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTComponentBaseType;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTComponentNameWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTComponentType;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTFilePathWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTJoinMapWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTJoinWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTLookupConfigWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTLookupMapWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTMatchValueWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTOperationClassWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTRetentionLogicWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTXmlPropertiesContainer;
import hydrograph.ui.propertywindow.widgets.customwidgets.HiveInputSingleColumnWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.HiveOutputSingleColumnWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.SingleColumnWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.TextBoxWithIsParameterCheckBoxWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.TextBoxWithLabelWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.TextBoxWithLabelWidgetWithoutAnyValidation;
import hydrograph.ui.propertywindow.widgets.customwidgets.WidgetHelper;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.joinproperty.ELTJoinPortCount;
import hydrograph.ui.propertywindow.widgets.customwidgets.metastore.ELTExtractMetaStoreDataWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.operational.TransformWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.runtimeproperty.ELTRuntimePropertiesWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTGenericSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.secondarykeys.SecondaryColumnKeysWidget;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;


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
		MIXED_SCHEME(ELTMixedSchemeWidget.class),		
		TRANSFORM_SCHEMA_WIDGET(TransformSchemaWidget.class),
		GENERATE_RECORDS_SCHEMA_WIDGET(GenerateRecordsGridWidget.class),
		FILE_PATH_WIDGET(ELTFilePathWidget.class),
		BROWSE_WORKSPACE_WIDGET(ELTBrowseWorkspaceWidget.class),
		COMPONENT_NAME_WIDGET(ELTComponentNameWidget.class),
		
		COMPONENT_BASETYPE_WIDGET(ELTComponentBaseType.class),
		COMPONENT_TYPE_WIDGET(ELTComponentType.class),

		RETENTION_LOGIC_WIDGET(ELTRetentionLogicWidget.class),

		STRICT_CLASS_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getStrictWidgetConfig()),
		SAFE_PROPERTY_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getSafeWidgetConfig()),
		CHARACTER_SET_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getCharacterSetWidgetConfig()),
		HAS_HEADER_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getHasHeaderWidgetConfig()),
		OVERWRITE_WIDGET(DropDownWidget.class, WidgetHelper.INSTANCE.getOverWriteWidgetConfig()),

		TEXTBOX_WITH_IS_PARAMETER_CHECKBOX_WIDGET(TextBoxWithIsParameterCheckBoxWidget.class,WidgetHelper.INSTANCE.getSequenceFieldWidgetConfig()),
		DELIMETER_WIDGET(DelimiterWidget.class, WidgetHelper.INSTANCE.getDelimiterWidgetConfig()),
		BATCH_WIDGET(TextBoxWithLabelWidget.class, WidgetHelper.INSTANCE.getBatchWidgetConfig()),
		QUOTE_WIDGET(TextBoxWithLabelWidgetWithoutAnyValidation.class, WidgetHelper.INSTANCE.getQuoteWidgetConfig()),
		DATABASE_NAME_WIDGET(TextBoxWithLabelWidget.class, WidgetHelper.INSTANCE.getDatabaseNameWidgetConfig()),
		TABLE_NAME_WIDGET(TextBoxWithLabelWidget.class, WidgetHelper.INSTANCE.getTableNameWidgetConfig()),
		EXTERNAL_TABLE_PATH_WIDGET(TextBoxWithLabelWidgetWithoutAnyValidation.class, WidgetHelper.INSTANCE.getExternalTablePathWidgetConfig()),
		
		NO_OF_RECORDS_WIDGET(TextBoxWithLabelWidget.class, WidgetHelper.INSTANCE.getNoOfRecordsWidgetConfig()),
		COUNT_WIDGET (TextBoxWithLabelWidget.class, WidgetHelper.INSTANCE.getCountWidgetConfig ()),
		FILTER_PROPERTY_WIDGET(SingleColumnWidget.class, WidgetHelper.INSTANCE.getOperationFieldsConfig()),
		COLUMN_NAME_WIDGET(SingleColumnWidget.class, WidgetHelper.INSTANCE.getColumnNameConfig()),
		PARTITION_KEYS_WIDGET(HiveOutputSingleColumnWidget.class, WidgetHelper.INSTANCE.getPartitionKeysConfig()),
		
		PARTITION_KEYS_WIDGET_INPUT_HIVE(HiveInputSingleColumnWidget.class, WidgetHelper.INSTANCE.getPartitionKeysConfigInputHive()),
		OPERATIONAL_CLASS_WIDGET(ELTOperationClassWidget.class, WidgetHelper.INSTANCE.getOperationClassForFilterWidgetConfig()),
		RUNTIME_PROPERTIES_WIDGET(ELTRuntimePropertiesWidget.class,WidgetHelper.INSTANCE.getRunTimeWidgetConfig(Constants.RUNTIME_PROPERTY_LABEL,Constants.RUNTIME_PROPERTIES_WINDOW_LABEL)),
		SUBJOB_PROPERTIES_WIDGET(ELTRuntimePropertiesWidget.class,WidgetHelper.INSTANCE.getRunTimeWidgetConfig(Constants.SUBJOB_PROPERTY_LABEL,Constants.SUBJOB_WINDOW_LABEL)),
		PRIMARY_COLUMN_KEYS_WIDGET(SecondaryColumnKeysWidget.class, WidgetHelper.INSTANCE.getPrimaryKeyWidgetConfig()),
		SECONDARY_COLUMN_KEYS_WIDGET(SecondaryColumnKeysWidget.class, WidgetHelper.INSTANCE.getSecondaryKeyWidgetConfig()),
		
		TRANSFORM_WIDGET(TransformWidget.class,WidgetHelper.INSTANCE.getOperationClassForTransformWidgetConfig(Constants.TRANSFORM,Constants.TRANSFORM_DISPLAYNAME, Constants.TRANSFORM_WINDOW_TITLE)),
		AGGREGATE_WIDGET(TransformWidget.class,WidgetHelper.INSTANCE.getOperationClassForTransformWidgetConfig(Constants.AGGREGATE,Constants.AGGREGATE_DISPLAYNAME, Constants.AGGREGATE_WINDOW_TITLE)),
		CUMULATE_WIDGET(TransformWidget.class,WidgetHelper.INSTANCE.getOperationClassForTransformWidgetConfig(Constants.CUMULATE, Constants.CUMULATE_DISPLAYNAME, Constants.CUMULATE_WINDOW_TITLE)),
		NORMALIZE_WIDGET(TransformWidget.class,WidgetHelper.INSTANCE.getOperationClassForTransformWidgetConfig(Constants.NORMALIZE,Constants.NORMALIZE_DISPLAYNAME, Constants.NORMALIZE_WINDOW_TITLE)),		
		
		XML_CONTENT_WIDGET(ELTXmlPropertiesContainer.class),
		
		JOIN_INPUT_COUNT_WIDGET(ELTJoinPortCount.class,WidgetHelper.INSTANCE.getInputCountWidgetConfig(Messages.LABEL_INPUT_COUNT,2)),
		INPUT_COUNT_WIDGET(ELTJoinPortCount.class,WidgetHelper.INSTANCE.getInputCountWidgetConfig(Messages.LABEL_INPUT_COUNT,1)),
		OUTPUT_COUNT_WIDGET(ELTJoinPortCount.class,WidgetHelper.INSTANCE.getInputCountWidgetConfig(Messages.LABEL_OUTPUT_COUNT,1)),
		
		JOIN_TYPE_WIDGET(ELTJoinWidget.class),
		JOIN_MAPPING_WIDGET(ELTJoinMapWidget.class),
		HASH_JOIN_WIDGET(ELTLookupConfigWidget.class),
		HASH_JOIN_MAPPING_WIDGET(ELTLookupMapWidget.class),
		MATCH_PROPERTY_WIDGET(ELTMatchValueWidget.class),
		EXTRACT_METASTORE_DATA_WIDGET(ELTExtractMetaStoreDataWidget.class);
		
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
