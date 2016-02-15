package com.bitwise.app.propertywindow.factory;

import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.widgets.listeners.ELTBrowseFileListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTCheckFileExtensionListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTCreateNewClassListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTEmptyTextModifyListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTEnableButtonListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTEventChangeListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTFileDialogSelectionListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTFocusGainedListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTFocusOutListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTInputCountListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTModifyListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTNormalFocusOutListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTOpenFileEditorListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTRuntimeButtonClickListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTSchemaDialogSelectionListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTSelectionListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTVerifyComponentNameListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTVerifyNumbericListener;
import com.bitwise.app.propertywindow.widgets.listeners.ELTVerifySequenceFieldName;
import com.bitwise.app.propertywindow.widgets.listeners.ELTVerifyTextListener;
import com.bitwise.app.propertywindow.widgets.listeners.FocusInListener;
import com.bitwise.app.propertywindow.widgets.listeners.IELTListener;
import com.bitwise.app.propertywindow.widgets.listeners.OperationClassComboChangeListener;
import com.bitwise.app.propertywindow.widgets.listeners.VerifyNumbericOrParameterFocusInListener;
import com.bitwise.app.propertywindow.widgets.listeners.VerifyNumbericOrParameterFocusOutListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridAddSelectionListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDeleteAllSelectionListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDeleteSelectionListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridMouseDoubleClickListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridMouseDownListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.transform.ELTTransformDeleteSelectionListener;

/**
 * Factory class for widget listeners
 * @author Bitwise
 * Sep 18, 2015
 */
public class ListenerFactory {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ListenerFactory.class);
	
	/**
	 * The Enum Listners.
	 * 
	 * @author Bitwise
	 */
	public enum Listners{
		EVENT_CHANGE(ELTEventChangeListener.class),
		SELECTION(ELTSelectionListener.class),
		VERIFY_TEXT(ELTVerifyTextListener.class),
		VERIFY_COMPONENT_NAME(ELTVerifyComponentNameListener.class),
		RUNTIME_BUTTON_CLICK(ELTRuntimeButtonClickListener.class),
		FILE_DIALOG_SELECTION(ELTFileDialogSelectionListener.class),
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
		VERIFY_NUMERIC_OR_PARAMETER_FOCUS_OUT(VerifyNumbericOrParameterFocusOutListener.class);
		
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

