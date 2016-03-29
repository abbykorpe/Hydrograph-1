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

 
package com.bitwise.app.propertywindow.propertydialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.slf4j.Logger;

import com.bitwise.app.common.util.ComponentCacheUtil;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ImagePathConstant;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.constants.ELTProperties;
import com.bitwise.app.propertywindow.messagebox.ConfirmCancelMessageBox;
import com.bitwise.app.propertywindow.property.ELTComponenetProperties;
import com.bitwise.app.propertywindow.property.Property;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.interfaces.IOperationClassDialog;
import com.bitwise.app.validators.impl.IValidator;


/**
 * 
 * @author Bitwise
 * Sep 07, 2015
 * 
 */
public class PropertyDialog extends Dialog implements IOperationClassDialog{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(PropertyDialog.class);
	private Composite container;
	private final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Property>>> propertyTree;
	private PropertyDialogBuilder propertyDialogBuilder;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private final String componentName;
	private Button applyButton;
	private boolean propertyChanged=false;	
	private final ELTComponenetProperties componentProperties;
	private   final String DIALOG_FONT_DATA = "DIALOG_FONT_NAME"; //$NON-NLS-1$
	private   final String DIALOG_WIDTH = "DIALOG_WIDTH"; //$NON-NLS-1$
	private   final String DIALOG_HEIGHT = "DIALOG_HEIGHT";
	
	private boolean isPropertyWindowValid;
	
	private Map<String,String> toolTipErrorMessages;
	
	private Component component;
	
	private boolean isCancelButtonPressed = false;
	
	private boolean closeDialog;
	
	private boolean okPressed;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param propertyTree 
	 * @param component 
	 * @param ComponentProperties 
	 */
	public PropertyDialog(LinkedHashMap<String, LinkedHashMap<String, ArrayList<Property>>> propertyTree,ELTComponenetProperties eltComponenetProperties,Map<String, String> toolTipErrorMessages, Component component) {		
		super(Display.getCurrent().getActiveShell());
		this.propertyTree = propertyTree;
		this.componentProperties = eltComponenetProperties;
		componentName = (String) this.componentProperties.getComponentConfigurationProperty(ELTProperties.NAME_PROPERTY.propertyName());
		this.component = component;
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL | SWT.RESIZE);
		/**
		 * 	Initialize it with true, if any one of the property is invalid then mark this status as false
		 */
		isPropertyWindowValid = true;
		
		this.toolTipErrorMessages = toolTipErrorMessages;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		createPropertyDialogContainer(parent);
		propertyDialogButtonBar = new PropertyDialogButtonBar(container);

		propertyDialogBuilder = new PropertyDialogBuilder(container,propertyTree,componentProperties,propertyDialogButtonBar,component,this);
		propertyDialogBuilder.buildPropertyWindow();

		return container;
	}

	private void createPropertyDialogContainer(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		setPropertyDialogContainerLayout();
		setPropertyDialogTitle();
	}

	private void setPropertyDialogContainerLayout() {
		ColumnLayout cl_container = new ColumnLayout();
		cl_container.maxNumColumns = 1;
		container.setLayout(cl_container);
	}

	private void setPropertyDialogTitle() {
		container.getShell().setText(componentName + " - Properties");
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createOKButton(parent);
		Button cancelButton = createCancelButton(parent);
		createApplyButton(parent);		
		attachPropertyDialogButtonBarToEatchWidgetOnPropertyWindow(okButton,
				cancelButton);
	}

	private void attachPropertyDialogButtonBarToEatchWidgetOnPropertyWindow(
			Button okButton, Button cancelButton) {
		propertyDialogButtonBar.setPropertyDialogButtonBar(okButton, applyButton, cancelButton);
	}

	private void createApplyButton(Composite parent) {
		applyButton = createButton(parent, IDialogConstants.NO_ID,
				"Apply", false);
		disableApplyButton();
	}
	

	@Override
	protected void buttonPressed(int buttonId) {
		// If Apply Button pressed(3 is index of apply button);
		if(buttonId == 3){
			applyButtonAction();
		}
		updateComponentValidityStatus();
		super.buttonPressed(buttonId);
	}

	private void updateComponentValidityStatus() {
		String statusString = null;
		if(isPropertyWindowValid){
			statusString = "VALID";
		}
		else{
			statusString = "ERROR";
		}
		componentProperties.getComponentConfigurationProperties().put("validityStatus", statusString);
	}

	private void applyButtonAction() {
		boolean windowValidityStaus = Boolean.TRUE;
		for(AbstractWidget customWidget : propertyDialogBuilder.getELTWidgetList()){
			if(customWidget.getProperties() != null){
				if(customWidget.getProperties() != null){
					windowValidityStaus = validateWidget(windowValidityStaus, customWidget);
					savePropertiesInComponentModel(customWidget);
				}
			}
		}
		isPropertyWindowValid = windowValidityStaus;
		updateComponentValidityStatus();
		
		propertyChanged=true;
		disableApplyButton();
	}

	private void savePropertiesInComponentModel(AbstractWidget eltWidget) {
		LinkedHashMap<String, Object> tempPropert = eltWidget.getProperties();
		LinkedHashMap<String, Object> componentConfigurationProperties = componentProperties.getComponentConfigurationProperties();
		for(String propName : tempPropert.keySet()){
			componentConfigurationProperties.put(propName, tempPropert.get(propName));
		}
	}
	
	private void disableApplyButton() {
		applyButton.setEnabled(false);
	}

	private Button createCancelButton(Composite parent) {
		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		return cancelButton;
	}

	private Button createOKButton(Composite parent) {
		Button okButton=createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		return okButton;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		Point result = getDefaultSize();

	    // Check the dialog settings for a stored size.
	    if((getDialogBoundsStrategy() & DIALOG_PERSISTSIZE) != 0)
	    {
	      IDialogSettings settings = getDialogBoundsSettings();

	      if(settings != null)
	      {
	 
	        boolean useStoredBounds = true;
	        String previousDialogFontData = settings.get(DIALOG_FONT_DATA);
 
	        if(previousDialogFontData != null && previousDialogFontData.length() > 0)
	        {
	          FontData[] fontDatas = JFaceResources.getDialogFont().getFontData();

	          if(fontDatas.length > 0)
	          {
	            String currentDialogFontData = fontDatas[0].toString();
	            useStoredBounds = currentDialogFontData.equalsIgnoreCase(previousDialogFontData);
	          }
	        }

	        if(useStoredBounds)
	        {
	          try
	          {
	            // Get the stored width and height.
	            int width = settings.getInt(DIALOG_WIDTH);

	            if(width != DIALOG_DEFAULT_BOUNDS)
	            {
	              result.x = width;
	            }

	            int height = settings.getInt(DIALOG_HEIGHT);

	            if(height != DIALOG_DEFAULT_BOUNDS)
	            {
	              result.y = height;
	            }
	          }
	          catch(NumberFormatException e)
	          {
	          }
	        }
	      }
	    }
 
	    return result;
	  }
	
	 protected Point getDefaultSize()
	  {
	    return getShell().computeSize(450, 560, true);
	  }
	
	@Override
	protected void okPressed() {
		boolean windowValidityStaus = Boolean.TRUE;
		for(AbstractWidget customWidget : propertyDialogBuilder.getELTWidgetList()){
			if(customWidget.getProperties() != null){
				windowValidityStaus = validateWidget(windowValidityStaus, customWidget);
				savePropertiesInComponentModel(customWidget);
			}
		}
		if(applyButton.isEnabled())
			propertyChanged=true;
		
		isPropertyWindowValid = windowValidityStaus;
		updateComponentValidityStatus();
		
		okPressed=true;
		super.okPressed();
	}

	
	/**
	 * 
	 * press ok button
	 * 
	 */
	public void pressOK(){
		okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		boolean windowValidityStaus = Boolean.TRUE;
		for (AbstractWidget customWidget : propertyDialogBuilder.getELTWidgetList()) {
			if (customWidget.getProperties() != null) {
				windowValidityStaus = validateWidget(windowValidityStaus, customWidget);
			}
		}
		isPropertyWindowValid = windowValidityStaus;
		updateComponentValidityStatus();
		if (applyButton.isEnabled()) {
			if (!isCancelButtonPressed) {
				ConfirmCancelMessageBox confirmCancelMessageBox = new ConfirmCancelMessageBox(container);
				MessageBox confirmCancleMessagebox = confirmCancelMessageBox.getMessageBox();

				if (confirmCancleMessagebox.open() == SWT.OK) {
					closeDialog = super.close();
				}
			} else {
				closeDialog = super.close();
			}
		} else {
			closeDialog = super.close();
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);		
		String imagePath = null;
		//TODO Please uncomment below code before build.
		try{
			imagePath = XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.APP_ICON;
			Image shellImage = new Image(newShell.getDisplay(), imagePath);
			newShell.setImage(shellImage);
		}catch(Exception e){
			logger.debug("Unable to access image" , e);
		}
	}


	/**
	 * 
	 * returns true if user made changes in property window
	 * 
	 * @return boolean
	 */
	public boolean isPropertyChanged(){
		return propertyChanged;
	}

	
	private boolean validateWidget(Boolean windowValidityStaus, AbstractWidget customWidget) {
		List<String> validators = ComponentCacheUtil.INSTANCE.getValidatorsForProperty(
				(String) this.componentProperties.getComponentMiscellaneousProperty(Constants.COMPONENT_ORIGINAL_NAME), 
				customWidget.getPropertyName());
		
		IValidator validator = null;
		for (String validatorName : validators) {
			try {
				validator = (IValidator) Class.forName(Constants.VALIDATOR_PACKAGE_PREFIX + validatorName).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.error("Failed to create validator", e);
				throw new RuntimeException("Failed to create validator", e);
			}
			boolean status = validator.validateMap(customWidget.getProperties(), customWidget.getPropertyName());
			//NOTE : here if any of the property is not valid then whole component is not valid 
			if(status == false){
				windowValidityStaus = Boolean.FALSE;
				logger.debug("{} is not valid", customWidget.getPropertyName());
			}
			appendErrorMessage(customWidget, validator);
		}
		return windowValidityStaus;
	}

	private void appendErrorMessage(AbstractWidget customWidget, IValidator validator) {
		if(toolTipErrorMessages.containsKey(customWidget.getPropertyName())){
			String errorMessage = toolTipErrorMessages.get(customWidget.getPropertyName());
			errorMessage = errorMessage + "\n" + validator.getErrorMessage(); 
		}else{
			toolTipErrorMessages.put(customWidget.getPropertyName(), validator.getErrorMessage());
		}
	}

	@Override
	public void pressCancel() {
		isCancelButtonPressed = true;
		cancelPressed();
	}

	@Override
	public boolean close() {
		if(!okPressed){
			cancelPressed();			
			return closeDialog;
		}else{
			return super.close();
		}		
	}
}
