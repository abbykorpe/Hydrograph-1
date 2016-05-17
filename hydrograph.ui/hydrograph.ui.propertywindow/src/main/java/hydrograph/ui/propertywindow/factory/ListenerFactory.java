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

import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.widgets.listeners.DelimiterFocusInListener;
import hydrograph.ui.propertywindow.widgets.listeners.DelimiterFocusOutListener;
import hydrograph.ui.propertywindow.widgets.listeners.DelimiterModify;
import hydrograph.ui.propertywindow.widgets.listeners.DirectoryDialogSelectionListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTBrowseFileListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTCheckFileExtensionListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTCreateNewClassListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTEmptyTextModifyListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTEnableButtonListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTEventChangeListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTFileDialogSelectionListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTFocusGainedListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTFocusOutListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTInputCountListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTModifyListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTNormalFocusOutListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTOpenFileEditorListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTRuntimeButtonClickListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTSchemaDialogSelectionListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTSelectionListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTVerifyComponentNameListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTVerifyNumbericListener;
import hydrograph.ui.propertywindow.widgets.listeners.ELTVerifySequenceFieldName;
import hydrograph.ui.propertywindow.widgets.listeners.ELTVerifyTextListener;
import hydrograph.ui.propertywindow.widgets.listeners.FocusInListener;
import hydrograph.ui.propertywindow.widgets.listeners.IELTListener;
import hydrograph.ui.propertywindow.widgets.listeners.JoinInputCountFocusOutListener;
import hydrograph.ui.propertywindow.widgets.listeners.OperationClassComboChangeListener;
import hydrograph.ui.propertywindow.widgets.listeners.VerifyNumbericOrParameterFocusInListener;
import hydrograph.ui.propertywindow.widgets.listeners.VerifyNumbericOrParameterFocusOutListener;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTGridAddSelectionListener;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTGridDeleteAllSelectionListener;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTGridDeleteSelectionListener;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTGridMouseDoubleClickListener;
import hydrograph.ui.propertywindow.widgets.listeners.grid.ELTGridMouseDownListener;
import hydrograph.ui.propertywindow.widgets.listeners.grid.transform.ELTTransformDeleteSelectionListener;

import org.slf4j.Logger;


/**
 * Factory class for widget listeners
 * @author Bitwise
 * Sep 18, 2015
 */
public class ListenerFactory {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ListenerFactory.class);
	
	
	public enum Listners{
		EVENT_CHANGE(ELTEventChangeListener.class),
		SELECTION(ELTSelectionListener.class),
		VERIFY_TEXT(ELTVerifyTextListener.class),
		VERIFY_COMPONENT_NAME(ELTVerifyComponentNameListener.class),
		RUNTIME_BUTTON_CLICK(ELTRuntimeButtonClickListener.class),
		FILE_DIALOG_SELECTION(ELTFileDialogSelectionListener.class),
		DIRECTORY_DIALOG_SELECTION(DirectoryDialogSelectionListener.class),
		SCHEMA_DIALOG_SELECTION(ELTSchemaDialogSelectionListener.class),
		FOCUS_OUT(ELTFocusOutListener.class),
		FOCUS_IN(ELTFocusGainedListener.class),
		EMPTY_TEXT_MODIFY(ELTEmptyTextModifyListener.class),
		CHECK_FILE_EXTENTION(ELTCheckFileExtensionListener.class),
		OPEN_FILE_EDITOR(ELTOpenFileEditorListener.class),
		CREATE_NEW_CLASS(ELTCreateNewClassListener.class),
		BROWSE_FILE_LISTNER(ELTBrowseFileListener.class),
		ENABLE_BUTTON(ELTEnableButtonListener.class),
		VERIFY_NUMERIC(ELTVerifyNumbericListener.class),
		GRID_MOUSE_DOUBLE_CLICK(ELTGridMouseDoubleClickListener.class),
		GRID_MOUSE_DOWN(ELTGridMouseDownListener.class),
		GRID_ADD_SELECTION(ELTGridAddSelectionListener.class),
		GRID_DELETE_SELECTION(ELTGridDeleteSelectionListener.class),
		GRID_DELETE_ALL(ELTGridDeleteAllSelectionListener.class),
		MODIFY(ELTModifyListener.class), 
		NORMAL_FOCUS_OUT(ELTNormalFocusOutListener.class),
		TRANSFORM_DELETE_SELECTION(ELTTransformDeleteSelectionListener.class),
		NORMAL_FOCUS_IN(FocusInListener.class),
		JOIN_INPUT_COUNT(ELTInputCountListener.class), 
		VERIFY_SEQUENCE_FIELD_NAME_EXISTS(ELTVerifySequenceFieldName.class),
		COMBO_CHANGE(OperationClassComboChangeListener.class),
		VERIFY_NUMERIC_OR_PARAMETER_FOCUS_IN(VerifyNumbericOrParameterFocusInListener.class),
		VERIFY_NUMERIC_OR_PARAMETER_FOCUS_OUT(VerifyNumbericOrParameterFocusOutListener.class),
		JOIN_INPUT_COUNT_FOCUS_OUT(JoinInputCountFocusOutListener.class),
		DELIMITER_FOCUS_IN(DelimiterFocusInListener.class),
		DELIMITER_FOCUS_OUT(DelimiterFocusOutListener.class),
		DELIMITER_MODIFY(DelimiterModify.class);
		
		
		Class<?> clazz = null;
		private Listners(Class<?> clazz) {
			this.clazz = clazz; 
		}
		public IELTListener getListener(){
			try {
				return (IELTListener) clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException exception) {
				logger.error("Failed to create listener for class : {}, {}", clazz.getName(), exception);
				throw new RuntimeException("Failed to instantiate the Listner " + clazz.getName());
			}
		}
	}
}

