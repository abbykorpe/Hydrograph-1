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
import hydrograph.ui.propertywindow.factory.ListenerFactory.Listners;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.TextBoxWithLableConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.IELTListener;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;


/**
 * Widget for showing text box in property window.
 * 
 * @author Bitwise
 */
public class TextBoxWithLabelWidget extends AbstractWidget{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(TextBoxWithLabelWidget.class);
	protected Text textBox;
	protected String propertyValue;
	protected String propertyName;
	protected ControlDecoration txtDecorator;
	protected TextBoxWithLableConfig textBoxConfig;
	protected ELTDefaultSubgroupComposite lableAndTextBox;
	private Properties jobProps;
	private Map<String, String> paramsMap;
	private Cursor cursur;
	private String finalParamPath;
	private static final String PARAMETER_NOT_FOUND = Messages.PARAMETER_NOT_FOUND;
	/**
	 * Instantiates a new text box widget with provided configurations
	 * 
	 * @param componentConfigProp
	 *            the component configration property
	 * @param componentMiscProps
	 *            the component miscellaneous properties
	 * @param propDialogButtonBar
	 *            the property dialog button bar
	 */
	public TextBoxWithLabelWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);
		this.propertyName = componentConfigProp.getPropertyName();
		this.propertyValue =  String.valueOf(componentConfigProp.getPropertyValue());
		this.jobProps = new Properties();
	}

	protected void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
				
		if(txtDecorator.isVisible())
			toolTipErrorMessage = txtDecorator.getDescriptionText();
		
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName, textBox.getText());
		setToolTipErrorMessage();
		return property;
	}
	
	public void setWidgetConfig(WidgetConfig widgetConfig) {
		textBoxConfig = (TextBoxWithLableConfig) widgetConfig;
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		logger.debug("Starting {} textbox creation", textBoxConfig.getName());
		lableAndTextBox = new ELTDefaultSubgroupComposite(container.getContainerControl());
		lableAndTextBox.createContainerWidget();
		
		AbstractELTWidget label = new ELTDefaultLable(textBoxConfig.getName() + " ");
		lableAndTextBox.attachWidget(label);
		setPropertyHelpWidget((Control) label.getSWTWidgetControl());
		
		AbstractELTWidget textBoxWidget = new ELTDefaultTextBox().
				grabExcessHorizontalSpace(textBoxConfig.getGrabExcessSpace()).textBoxWidth(textBoxConfig.getwidgetWidth());
		lableAndTextBox.attachWidget(textBoxWidget);
		
		textBox = (Text) textBoxWidget.getSWTWidgetControl();
		txtDecorator = WidgetUtility.addDecorator(textBox, Messages.bind(Messages.EMPTY_FIELD, textBoxConfig.getName()));
		txtDecorator.setMarginWidth(3);
		
		attachListeners(textBoxWidget);
		
		 /**
		 *parameter resolution at dev phase 
		 */
		loadProperties();
		cursur = container.getContainerControl().getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		
		populateWidget();
		logger.debug("Finished {} textbox creation", textBoxConfig.getName());
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
	
	protected void attachListeners(AbstractELTWidget textBoxWidget) {
		ListenerHelper helper = prepareListenerHelper();
		try {
			for (Listners listenerNameConstant : textBoxConfig.getListeners()) {
				IELTListener listener = listenerNameConstant.getListener();
				textBoxWidget.attachListener(listener, propertyDialogButtonBar, helper, textBoxWidget.getSWTWidgetControl());
			}
		} catch (Exception exception) {
			logger.error("Failed in attaching listeners to {}, {}", textBoxConfig.getName(), exception);
		}
	}

	protected ListenerHelper prepareListenerHelper() {
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		helper.put(HelperType.CURRENT_COMPONENT, getComponent());
		helper.put(HelperType.CHARACTER_LIMIT, textBoxConfig.getCharacterLimit());
		return helper;
	}
	
	protected void populateWidget(){
		logger.debug("Populating {} textbox", textBoxConfig.getName());
		String property = propertyValue;
		if(StringUtils.isNotBlank(property) ){
			textBox.setText(property);
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
			txtDecorator.show();
		}
	}
	
	protected boolean isParameter(String input) {
		if (input != null) {
			Matcher matchs = Pattern.compile(Constants.PARAMETER_REGEX).matcher(input);
			if (matchs.matches()) {
				return true;
			}
		}
		return false;
	}

	


	
	@Override
	public boolean isWidgetValid() {
	  return validateAgainstValidationRule(textBox.getText());
	}

	

	@Override
	public void addModifyListener(final Property property,  final ArrayList<AbstractWidget> widgetList) {
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
