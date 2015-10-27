package com.bitwise.app.propertywindow.propertydialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.constants.ELTProperties;
import com.bitwise.app.propertywindow.messagebox.ConfirmCancelMessageBox;
import com.bitwise.app.propertywindow.property.ELTComponenetProperties;
import com.bitwise.app.propertywindow.property.Property;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;


// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Sep 07, 2015
 * 
 */

public class PropertyDialog extends Dialog {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(PropertyDialog.class);
	private Composite container;
	private final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Property>>> propertyTree;
	private PropertyDialogBuilder propertyDialogBuilder;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private final String componentName;
	private Button applyButton;
	private boolean propertyChanged=false;	
	private final ELTComponenetProperties eltComponenetProperties;
	private   final String DIALOG_FONT_DATA = "DIALOG_FONT_NAME"; //$NON-NLS-1$
	private   final String DIALOG_WIDTH = "DIALOG_WIDTH"; //$NON-NLS-1$
	private   final String DIALOG_HEIGHT = "DIALOG_HEIGHT";
	
	private boolean isPropertyWindowValid;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param propertyTree 
	 * @param ComponentProperties 
	 */
	public PropertyDialog(Shell parentShell, LinkedHashMap<String, LinkedHashMap<String, ArrayList<Property>>> propertyTree,ELTComponenetProperties eltComponenetProperties) {		
		super(parentShell);
		this.propertyTree = propertyTree;
		this.eltComponenetProperties = eltComponenetProperties;
		componentName = (String) this.eltComponenetProperties.getComponentConfigurationProperty(ELTProperties.NAME_PROPERTY.propertyName());

		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL);
		/**
		 * 	Initialize it with true, if any one of the property is invalid then mark this status as false
		 */
		isPropertyWindowValid = true;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		createPropertyDialogContainer(parent);
		propertyDialogButtonBar = new PropertyDialogButtonBar(container);

		propertyDialogBuilder = new PropertyDialogBuilder(container,propertyTree,eltComponenetProperties,propertyDialogButtonBar);
		propertyDialogBuilder.buildPropertyWindow();

		return container;
	}

	private void createPropertyDialogContainer(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		setPropertyDialogContainerLayout();
		//setPropertyDialogSize();
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

	/*private void setPropertyDialogSize() {
		Monitor primary = container.getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = container.getBounds();

		int x = bounds.x + (bounds.width - rect.width+5) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		container.getShell().setMinimumSize(x, y);
	}
*/
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
		eltComponenetProperties.getComponentConfigurationProperties().put("validityStatus", statusString);
	}

	private void applyButtonAction() {
		for(AbstractWidget customWidget : propertyDialogBuilder.getELTWidgetList()){
			if(customWidget.getProperties() != null){
				if(Boolean.FALSE.equals(customWidget.isDataValid())){
					isPropertyWindowValid = false;
					logger.debug("{} is not valid ", customWidget);
				}
				savePropertiesInComponentModel(customWidget);
			}
		}
		propertyChanged=true;
		disableApplyButton();
	}

	private void savePropertiesInComponentModel(AbstractWidget eltWidget) {
		LinkedHashMap<String, Object> tempPropert = eltWidget.getProperties();
		LinkedHashMap<String, Object> componentConfigurationProperties = eltComponenetProperties.getComponentConfigurationProperties();
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
	    return getShell().computeSize(400, 500, true);
	  }
	
	@Override
	protected void okPressed() {
		
		for(AbstractWidget customWidget : propertyDialogBuilder.getELTWidgetList()){
			if(customWidget.getProperties() != null){
				if(Boolean.FALSE.equals(customWidget.isDataValid())){
					logger.debug("{} is not valid ", customWidget);
					isPropertyWindowValid = false;
				}
				savePropertiesInComponentModel(customWidget);	
			}
		}
		if(applyButton.isEnabled())
			propertyChanged=true;
		
		updateComponentValidityStatus();
		super.okPressed();
	}

	@Override
	protected void cancelPressed(){
		for(AbstractWidget customWidget : propertyDialogBuilder.getELTWidgetList()){
			if(Boolean.FALSE.equals(customWidget.isDataValid())){
				logger.debug("{} is not valid ", customWidget);
				isPropertyWindowValid = false;
				break;
			}
		}
		updateComponentValidityStatus();
		if(applyButton.isEnabled()){
			ConfirmCancelMessageBox confirmCancelMessageBox = new ConfirmCancelMessageBox(container);
			MessageBox confirmCancleMessagebox = confirmCancelMessageBox.getMessageBox();

			if(confirmCancleMessagebox.open() == SWT.OK){
				super.close();
			}
		}else{
			super.close();
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);		
		String imagePath = null;
		//TODO Please uncomment below code before build.
		try{
			imagePath = XMLConfigUtil.CONFIG_FILES_PATH + "/icons/property_window_icon.png" ;  
			Image shellImage = new Image(newShell.getDisplay(), imagePath);
			newShell.setImage(shellImage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public boolean isPropertyChanged(){
		return propertyChanged;
	}

}
