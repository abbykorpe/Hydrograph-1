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

 
package hydrograph.ui.propertywindow.widgets.customwidgets;


import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;



/**
 * The Class ELTFilePathWidget.
 * 
 * @author Bitwise
 */
public class ELTFilePathWidget extends AbstractWidget{
	
	private Text textBox;
	private Object properties;
	private String propertyName;
	private ControlDecoration txtDecorator;
	private ControlDecoration decorator;
	private Button button;
	private Properties jobProps;
	private Map<String, String> paramsMap;
	private Cursor cursur;
	private String finalParamPath;
	private static final String PARAMETER_NOT_FOUND = Messages.PARAMETER_NOT_FOUND;
	private Logger LOGGER = LogFactory.INSTANCE.getLogger(ELTFilePathWidget.class);
	/**
	 * Instantiates a new ELT file path widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTFilePathWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);

		this.properties =  componentConfigrationProperty.getPropertyValue();
		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.jobProps = new Properties();
	}
	

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("File Path");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		setPropertyHelpWidget((Control) eltDefaultLable.getSWTWidgetControl());
		
		AbstractELTWidget eltDefaultTextBox = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(200);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultTextBox);
		
		textBox = (Text) eltDefaultTextBox.getSWTWidgetControl();
		decorator=WidgetUtility.addDecorator(textBox, Messages.EMPTYFIELDMESSAGE);
		
		textBox.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(textBox.getText().isEmpty()){
					decorator.show();
					textBox.setBackground(new Color(Display.getDefault(), 255, 255, 204));
				}
				else{
					decorator.hide();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				decorator.hide();
				textBox.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			}
		});
		
		AbstractELTWidget eltDefaultButton = new ELTDefaultButton("...").buttonWidth(35);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		button=(Button)eltDefaultButton.getSWTWidgetControl();
		
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				decorator.hide();
				textBox.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
			
		
		txtDecorator = WidgetUtility.addDecorator(textBox, Messages.CHARACTERSET);
		txtDecorator.setMarginWidth(3);
		decorator.setMarginWidth(3);
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		helper.put(HelperType.CURRENT_COMPONENT, getComponent());

		try {
			eltDefaultTextBox.attachListener(ListenerFactory.Listners.EVENT_CHANGE.getListener(), propertyDialogButtonBar,  null,eltDefaultTextBox.getSWTWidgetControl());
			eltDefaultTextBox.attachListener(ListenerFactory.Listners.MODIFY.getListener(), propertyDialogButtonBar,  helper, eltDefaultTextBox.getSWTWidgetControl());
			if (StringUtils.equalsIgnoreCase(Constants.OUTPUT, getComponent().getCategory()))
				eltDefaultButton.attachListener(ListenerFactory.Listners.DIRECTORY_DIALOG_SELECTION.getListener(),
						propertyDialogButtonBar, helper, eltDefaultButton.getSWTWidgetControl(),
						eltDefaultTextBox.getSWTWidgetControl());
			else
				eltDefaultButton.attachListener(ListenerFactory.Listners.FILE_DIALOG_SELECTION.getListener(),
						propertyDialogButtonBar, helper, eltDefaultButton.getSWTWidgetControl(),
						eltDefaultTextBox.getSWTWidgetControl());
			//eltDefaultTextBox.attachListener(listenerFactory.getListener("ELTFocusOutListener"), propertyDialogButtonBar,  helper,eltDefaultTextBox.getSWTWidgetControl());
			} catch (Exception exception) {
			LOGGER.error("Exception occurred while attaching listeners to ELTFileWidget",exception);
		}
		/**
		 *parameter resolution at dev phase 
		 */
		loadProperties();
		cursur = container.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		
		populateWidget();
	}

	final MouseMoveListener listner = new MouseMoveListener() {
		
		@Override
		public void mouseMove(MouseEvent e) {
			String paramValue = getParamValue(textBox.getText());
		    finalParamPath = getParamFilePath(textBox.getText(), paramValue);
		    while(ParameterUtil.containsParameter(finalParamPath, '/')){
		    	paramValue = getParamValue(textBox.getToolTipText());
		    	finalParamPath = getParamFilePath(textBox.getToolTipText(), paramValue);
	    		}
			}
	};
	
	private void populateWidget(){		
		String property = (String)properties;
		if(StringUtils.isNotBlank(property)){
			textBox.setText(property);	
			decorator.hide();
			txtDecorator.hide();
			if(ParameterUtil.containsParameter(textBox.getText(),'/')){
				Color myColor = new Color(Display.getDefault(), 0, 0, 255);
				textBox.setForeground(myColor);	
				textBox.setCursor(cursur);
				textBox.addMouseMoveListener(listner);
					}
			else{
				textBox.removeMouseMoveListener(listner);
				textBox.setForeground(new Color(Display.getDefault(), 0, 0, 0));
				textBox.setCursor(null);
			}
		}
		else{
			textBox.setText("");
			decorator.show();
			//setToolTipMessage(toolTipErrorMessage)
		}
	}

	private void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
		if(decorator.isVisible())
			toolTipErrorMessage = decorator.getDescriptionText();
		
		if(txtDecorator.isVisible())
			toolTipErrorMessage = toolTipErrorMessage + "\n" + txtDecorator.getDescriptionText();
		
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName, textBox.getText());
		setToolTipErrorMessage();
		
		return property;
	}

	
	public boolean isWidgetValid() {
		 return validateAgainstValidationRule(textBox.getText());
	}


	public Text getTextBox() {
		return textBox;
	}


	@Override
	public void addModifyListener(final Property property, final ArrayList<AbstractWidget> widgetList) {
		textBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(ParameterUtil.containsParameter(textBox.getText(),'/')){
					Color myColor = new Color(Display.getDefault(), 0, 0, 255);
					textBox.setForeground(myColor);	
					textBox.setCursor(cursur);
					textBox.addMouseMoveListener(listner);
						}
				else{
					textBox.removeMouseMoveListener(listner);
					textBox.setForeground(new Color(Display.getDefault(), 0, 0, 0));
					textBox.setCursor(null);
				}
				showHideErrorSymbol(widgetList);
			}
		});
	}
	
	private void loadProperties(){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFileEditorInput input = (IFileEditorInput) page.getActiveEditor().getEditorInput();
		
		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		String activeProjectName = activeProject.getName();
		final File globalparamFilesPath = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()+"/"+activeProjectName+"/"+"globalparam");
		final File localParamFilePath = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()+"/"+activeProjectName+"/"+"param");
		File[] files = (File[]) ArrayUtils.addAll(listFilesForFolder(globalparamFilesPath), listFilesForFolder(localParamFilePath));
		List<File> paramNameList = Arrays.asList(files);
		getParamMap(paramNameList);
	}

   private void getParamMap(List<File> FileNameList){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFileEditorInput input = (IFileEditorInput) page.getActiveEditor().getEditorInput();
		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		String activeProjectName = activeProject.getName();
		
		String propFilePath = null;
		for(File propFileName : FileNameList){
			String fileName = propFileName.getName();
			if(StringUtils.contains(propFileName.toString(), "globalparam")){
				 propFilePath = "/" + activeProjectName +"/globalparam"+"/"+fileName;
			}
			else{
				 propFilePath =  "/" + activeProjectName +"/param"+"/"+fileName;
			}
			IPath propPath = new Path(propFilePath);
			IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(propPath);
			try {
				InputStream reader = iFile.getContents();
				jobProps.load(reader);

			} catch (CoreException | IOException e) {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",
						"Exception occured while loading build properties from file -\n" + e.getMessage());
			}
		
			Enumeration<?> e = jobProps.propertyNames();
			paramsMap = new HashMap<String, String>();
		    while (e.hasMoreElements()){
		        String param = (String) e.nextElement();
		        paramsMap.put(param, jobProps.getProperty(param));
		     }
	    }
		System.out.println(paramsMap);
	}
	
	 private String getParamValue(String value){
			if(jobProps != null && !jobProps.isEmpty() && StringUtils.isNotBlank(value)){
			String param = null;
			value = value.substring(value.indexOf("{") + 1).substring(0, value.substring(value.indexOf("{") + 1).indexOf("}"));
			for (Map.Entry<String, String> entry : paramsMap.entrySet()){
				param = entry.getKey();
			 if(StringUtils.equals(param, value)){
				 if(entry.getValue().endsWith("/")){
					 return entry.getValue();
				 }
				return entry.getValue()+"/";
	    			}
				} 
			}
			return PARAMETER_NOT_FOUND;
		}		
		
	 private String getParamFilePath(String extSchemaPath, String paramValue){
			String remainingString = "";
		    if(StringUtils.contains(paramValue, PARAMETER_NOT_FOUND) || ParameterUtil.isParameter(extSchemaPath)){
		    	textBox.setToolTipText(paramValue+remainingString);
		    }
		    else{
		    remainingString = extSchemaPath.substring(extSchemaPath.indexOf("}")+2, extSchemaPath.length());
		    textBox.setToolTipText(paramValue+remainingString);
		       }
			return paramValue+remainingString;
			}		
		
	 public File[]  listFilesForFolder(final File folder) {
			File[] listofFiles = folder.listFiles();
			
			return listofFiles;
			}				
}
