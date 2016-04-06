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

 
package hydrograph.ui.propertywindow.widgets.utility;

import hydrograph.ui.common.component.config.Operations;
import hydrograph.ui.common.component.config.TypeInfo;
import hydrograph.ui.common.datastructures.tooltip.TootlTipErrorMessage;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.OperationClassProperty;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.interfaces.IOperationClassDialog;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;


/**
 * The Class FilterOperationClassUtility.
 * 
 * @author Bitwise
 */
public class FilterOperationClassUtility  {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterOperationClassUtility.class);
	private static IJavaProject iJavaProject;
	private static Button createBtn;
	private static Button browseBtn;
	private static Button openBtn;
	private static Button btnCheckButton;
	private static String componentName;

	/**
	 * Creates the new class wizard.
	 * 
	 * @param fileNameTextBox
	 *            the file name
	 * @param widgetConfig 
	 */
	public static void createNewClassWizard(Text fileNameTextBox, WidgetConfig widgetConfig) {
		OpenNewClassWizardAction wizard = new OpenNewClassWizardAction();
		wizard.setOpenEditorOnFinish(false);
		final NewClassWizardPage page = new NewClassWizardPage();
		page.setSuperClass("java.lang.Object", true);
		page.setMethodStubSelection(false, false, true, true);
		List<String> interfaceList = new ArrayList<String>();
		OperationClassConfig operationClassConfig = (OperationClassConfig) widgetConfig;
		Operations operations = XMLConfigUtil.INSTANCE.getComponent(FilterOperationClassUtility.getComponentName()).getOperations();
		TypeInfo typeInfo=operations.getInterface();
		if (operationClassConfig.getComponentName().equalsIgnoreCase(typeInfo.getName()))
		{
			interfaceList.add(typeInfo.getClazz());
		}
		page.setSuperInterfaces(interfaceList, true);  
		wizard.setConfiguredWizardPage(page);
		if(OSValidator.isMac()){
			Display.getDefault().timerExec(0, new Runnable() {

				@Override
				public void run() {
					page.getControl().forceFocus();					
				}
			});
		}
		wizard.run();
		if (page.isPageComplete()) {
			if(!page.getPackageText().equalsIgnoreCase("")){
				fileNameTextBox.setText(page.getPackageText()+"."
						+ page.getTypeName());
			}else{
				fileNameTextBox.setText(page.getTypeName());
			}
		}

		fileNameTextBox.setData("path", "/" + page.getPackageFragmentRootText() + "/"
				+ page.getPackageText().replace(".", "/") + "/"
				+ page.getTypeName() + ".java");
	}

	/**
	 * Browse file.
	 * 
	 * @param filterExtension
	 *            the filter extension
	 * @param fileName
	 *            the file name
	 */
	public static void browseFile(String filterExtension, Text fileName) {
		ResourceFileSelectionDialog dialog = new ResourceFileSelectionDialog(
				"Project", "Select Java Class (.java)", new String[] { filterExtension });
		if (dialog.open() == IDialogConstants.OK_ID) {
			IResource resource = (IResource) dialog.getFirstResult();
			String filePath = resource.getRawLocation().toOSString();
			java.nio.file.Path path =Paths.get(filePath); 
			String classFile=path.getFileName().toString();
			String name = "";
			try { 
				BufferedReader r = new BufferedReader(new FileReader(filePath));
				String firstLine= r.readLine();
				if(firstLine.contains(Constants.PACKAGE)){
					name= firstLine.replaceFirst(Constants.PACKAGE, "").replace(";", "");
					if(!name.equalsIgnoreCase(""))
						name=name+"."+classFile.substring(0, classFile.lastIndexOf('.'));
					
				}else
					name=classFile.substring(0, classFile.lastIndexOf('.'));
				
			} catch (IOException e) { 
				logger.debug("Unable to read file " + filePath,e );
			}
			fileName.setText(name.trim());
			filePath = resource.getRawLocation().toOSString();
			fileName.setData("path", resource.getFullPath().toOSString());

		}
	} 

	/**
	 * Open file editor.
	 * 
	 * @param fileName
	 *            the file name
	 * @return true, if successful
	 */
	public static boolean openFileEditor(Text filePath,String pathFile) {
		try {
			String fileFullPath;
			String fileName;
			if(filePath!=null)
				fileName= (String) filePath.getData("path");
			else
				fileName=pathFile;

			File fileToOpen = new File(fileName);
			if(!fileToOpen.isFile())
			{
				Path path = new Path(fileName);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				fileFullPath = file.getRawLocation().toOSString();
			}
			else
				fileFullPath=fileName;
			File fileToEditor = new File(fileFullPath);
			if (fileToEditor.exists()) {
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(
						fileToEditor.toURI());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IDE.openEditorOnFileStore(page, fileStore);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	public static OperationClassProperty createOperationalClass(
			Composite composite,
			PropertyDialogButtonBar eltOperationClassDialogButtonBar,
			AbstractELTWidget combo, AbstractELTWidget isParameterCheckbox,
			AbstractELTWidget fileNameTextBox,
			TootlTipErrorMessage tootlTipErrorMessage,
			WidgetConfig widgetConfig,
			IOperationClassDialog eltOperationClassDialog,
			PropertyDialogButtonBar propertyDialogButtonBar, PropertyDialogButtonBar opeartionClassDialogButtonBar) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(composite);
		eltSuDefaultSubgroupComposite.createContainerWidget();
		eltSuDefaultSubgroupComposite.numberOfBasicWidgets(5);

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable(Messages.OPERATION_CALSS_LABEL);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		eltSuDefaultSubgroupComposite.attachWidget(combo);
		Combo comboOfOperaationClasses = (Combo) combo.getSWTWidgetControl();

		eltSuDefaultSubgroupComposite.attachWidget(fileNameTextBox);
		Text fileName = (Text) fileNameTextBox.getSWTWidgetControl();
		fileName.setSize(10, 100);

		AbstractELTWidget browseButton = new ELTDefaultButton(Messages.BROWSE_BUTTON_TEXT).buttonWidth(20);
		eltSuDefaultSubgroupComposite.attachWidget(browseButton);
		browseBtn=(Button)browseButton.getSWTWidgetControl();


		eltSuDefaultSubgroupComposite.attachWidget(isParameterCheckbox);


		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite2 = new ELTDefaultSubgroupComposite(composite);
		eltSuDefaultSubgroupComposite2.createContainerWidget();
		eltSuDefaultSubgroupComposite2.numberOfBasicWidgets(3);


		ELTDefaultButton emptyButton = new ELTDefaultButton("").buttonWidth(75);
		eltSuDefaultSubgroupComposite2.attachWidget(emptyButton);
		emptyButton.visible(false);

		// Create new button, that use to create operational class
		AbstractELTWidget createButton = new ELTDefaultButton(Messages.CREATE_NEW_OPEARTION_CLASS_LABEL);
		eltSuDefaultSubgroupComposite2.attachWidget(createButton); 
		createBtn=(Button)createButton.getSWTWidgetControl();

		// Edit new button, that use to edit operational class
		AbstractELTWidget openButton = new ELTDefaultButton(Messages.OPEN_BUTTON_LABEL);
		eltSuDefaultSubgroupComposite2.attachWidget(openButton); 
		openBtn=(Button)openButton.getSWTWidgetControl();

		btnCheckButton=(Button) isParameterCheckbox.getSWTWidgetControl();

		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.TOOLTIP_ERROR_MESSAGE, tootlTipErrorMessage);
		helper.put(HelperType.WIDGET_CONFIG, widgetConfig);
		helper.put(HelperType.OPERATION_CLASS_DIALOG_OK_CONTROL, eltOperationClassDialog);
		helper.put(HelperType.OPERATION_CLASS_DIALOG_APPLY_BUTTON, opeartionClassDialogButtonBar);
		helper.put(HelperType.PROPERTY_DIALOG_BUTTON_BAR, propertyDialogButtonBar);
		setIJavaProject();
		try { 						
			openButton.attachListener(ListenerFactory.Listners.OPEN_FILE_EDITOR.getListener(),eltOperationClassDialogButtonBar, helper,comboOfOperaationClasses,fileName);
			browseButton.attachListener(ListenerFactory.Listners.BROWSE_FILE_LISTNER.getListener(),eltOperationClassDialogButtonBar, helper,fileName);
			createButton.attachListener(ListenerFactory.Listners.CREATE_NEW_CLASS.getListener(),eltOperationClassDialogButtonBar, helper,comboOfOperaationClasses,fileName);
			combo.attachListener(ListenerFactory.Listners.COMBO_CHANGE.getListener(),eltOperationClassDialogButtonBar, helper,comboOfOperaationClasses,fileName,btnCheckButton);
			isParameterCheckbox.attachListener(ListenerFactory.Listners.ENABLE_BUTTON.getListener(),eltOperationClassDialogButtonBar, null,btnCheckButton,browseButton.getSWTWidgetControl(),createButton.getSWTWidgetControl(),openButton.getSWTWidgetControl());
		} catch (Exception e1) {
			e1.printStackTrace(); 
		} 
		OperationClassProperty operationClassProperty = new OperationClassProperty(comboOfOperaationClasses.getText(),fileName.getText(), btnCheckButton.getEnabled(),(String)fileName.getData("path"));
		return operationClassProperty;
	}

	private static void setIJavaProject() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if ((page.getActiveEditor().getEditorInput().getClass()).isAssignableFrom(FileEditorInput.class)) {
			IFileEditorInput input = (IFileEditorInput) page.getActiveEditor().getEditorInput();
			IFile file = input.getFile();
			IProject activeProject = file.getProject();
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(activeProject.getName());
			iJavaProject = JavaCore.create(project);
		}
	}

	public static IJavaProject getIJavaProject() {
		return iJavaProject;
	}


	public static void enableAndDisableButtons(boolean value,boolean checkboxValue) {
		if (checkboxValue==false) {
			createBtn.setEnabled(value);
			browseBtn.setEnabled(value);
			btnCheckButton.setEnabled(!value);
		}
		if (checkboxValue==true) {
			btnCheckButton.setEnabled(value);
			openBtn.setEnabled(!value);
			createBtn.setEnabled(!value);
			browseBtn.setEnabled(!value);
		}
	}
	public static void setComponentName(String name) {
		componentName = name;
	}

	public static String getComponentName() {
		return componentName;
	}

	public static boolean isCheckBoxSelected() {
		return btnCheckButton.getSelection();
	}
	public static void setOperationClassNameInTextBox(String operationName, Text textBox) {
		String operationClassName = null;
		Operations operations = XMLConfigUtil.INSTANCE.getComponent(FilterOperationClassUtility.getComponentName())
				.getOperations();
		List<TypeInfo> typeInfos = operations.getStdOperation();
		for (int i = 0; i < typeInfos.size(); i++) {
			if (typeInfos.get(i).getName().equalsIgnoreCase(operationName)) {
				operationClassName = typeInfos.get(i).getClazz();
				break;
			}
		}
		textBox.setText(operationClassName);;
	}


}
