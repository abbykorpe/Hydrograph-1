package com.bitwise.app.propertywindow.widgets.utility;

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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructures.tooltip.TootlTipErrorMessage;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

// TODO: Auto-generated Javadoc
/**
 * The Class FilterOperationClassUtility.
 * 
 * @author Bitwise
 */
public class FilterOperationClassUtility {
	/**
	 * Creates the new class wizard.
	 * 
	 * @param fileName
	 *            the file name
	 * @param widgetConfig 
	 */
	public static void createNewClassWizard(Text fileName, WidgetConfig widgetConfig) {
		OpenNewClassWizardAction wizard = new OpenNewClassWizardAction();
		wizard.setOpenEditorOnFinish(false);
		NewClassWizardPage page = new NewClassWizardPage();
		page.setSuperClass("java.lang.Object", true);
		page.setMethodStubSelection(false, false, true, true);
		List<String> interfaceList = new ArrayList<String>();
		OperationClassConfig operationClassConfig = (OperationClassConfig) widgetConfig;
		
		if (operationClassConfig.getComponentName().equalsIgnoreCase(Constants.FILTER))
			interfaceList.add(Messages.FILTER_TEMPLATE);
		else if (operationClassConfig.getComponentName().equalsIgnoreCase(Constants.AGGREGATE))
			interfaceList.add(Messages.AGGREGATE_TEMPLATE);
		else if (operationClassConfig.getComponentName().equalsIgnoreCase(Constants.TRANSFORM))
			interfaceList.add(Messages.TRANSFORM_TEMPLATE);
		
		page.setSuperInterfaces(interfaceList, true);  
		wizard.setConfiguredWizardPage(page);
		wizard.run();
		if (page.isPageComplete()) 
			fileName.setText(page.getPackageText()+"."
					+ page.getTypeName());
			fileName.setData("path", "/" + page.getPackageFragmentRootText() + "/"
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
				String arg= r.readLine();
				name= arg.replace("package", "").replace(";", ""); 
				if(!name.equalsIgnoreCase(""))
				name=name+"."+classFile.substring(0, classFile.lastIndexOf('.'));
				else
					name=classFile.substring(0, classFile.lastIndexOf('.'));
			} catch (IOException e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
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

public static OperationClassProperty createOperationalClass(Composite composite,
											PropertyDialogButtonBar eltOperationClassDialogButtonBar,AbstractELTWidget fileNameText
											,AbstractELTWidget isParameterCheckbox,ValidationStatus validationStatus,TootlTipErrorMessage tootlTipErrorMessage, WidgetConfig widgetConfig ){
	
	ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(composite);
	eltSuDefaultSubgroupComposite.createContainerWidget();
	eltSuDefaultSubgroupComposite.numberOfBasicWidgets(4);
	
	AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Operation\nClass");
	eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
	
	eltSuDefaultSubgroupComposite.attachWidget(fileNameText);
	
	Text fileName = (Text) fileNameText.getSWTWidgetControl();
	
	AbstractELTWidget browseButton = new ELTDefaultButton("...").buttonWidth(20);
	eltSuDefaultSubgroupComposite.attachWidget(browseButton);
	
	eltSuDefaultSubgroupComposite.attachWidget(isParameterCheckbox);
	
	
	ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite2 = new ELTDefaultSubgroupComposite(composite);
	eltSuDefaultSubgroupComposite2.createContainerWidget();
	eltSuDefaultSubgroupComposite2.numberOfBasicWidgets(3);
	
	
	ELTDefaultButton emptyButton = new ELTDefaultButton("").buttonWidth(75);
	eltSuDefaultSubgroupComposite2.attachWidget(emptyButton);
	emptyButton.visible(false);
			
	// Create new button, that use to create operational class
	AbstractELTWidget createButton = new ELTDefaultButton("Create New");
	eltSuDefaultSubgroupComposite2.attachWidget(createButton); 

	// Edit new button, that use to edit operational class
	AbstractELTWidget editButton = new ELTDefaultButton("Edit");
	eltSuDefaultSubgroupComposite2.attachWidget(editButton); 
	
	Button btnCheckButton=(Button) isParameterCheckbox.getSWTWidgetControl();
	
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.VALIDATION_STATUS, validationStatus);
		helper.put(HelperType.TOOLTIP_ERROR_MESSAGE, tootlTipErrorMessage);
		helper.put(HelperType.WIDGET_CONFIG, widgetConfig);
	try { 						
		
		fileNameText.attachListener(ListenerFactory.Listners.EVENT_CHANGE.getListener(),eltOperationClassDialogButtonBar, null,fileName);
		editButton.attachListener(ListenerFactory.Listners.OPEN_FILE_EDITOR.getListener(),eltOperationClassDialogButtonBar, null,fileName);
		browseButton.attachListener(ListenerFactory.Listners.BROWSE_FILE_LISTNER.getListener(),eltOperationClassDialogButtonBar, helper,fileName);
		createButton.attachListener(ListenerFactory.Listners.CREATE_NEW_CLASS.getListener(),eltOperationClassDialogButtonBar, helper,fileName);
		fileNameText.attachListener(ListenerFactory.Listners.EMPTY_TEXT_MODIFY.getListener(),eltOperationClassDialogButtonBar, helper,fileName,editButton.getSWTWidgetControl(),isParameterCheckbox.getSWTWidgetControl());
		isParameterCheckbox.attachListener(ListenerFactory.Listners.ENABLE_BUTTON.getListener(),eltOperationClassDialogButtonBar, null,btnCheckButton,browseButton.getSWTWidgetControl(),createButton.getSWTWidgetControl());
		} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace(); 
	} 
	OperationClassProperty operationClassProperty = new OperationClassProperty(fileName.getText(), btnCheckButton.getEnabled(),(String)fileName.getData("path"));
	return operationClassProperty;
}
	
	
}
