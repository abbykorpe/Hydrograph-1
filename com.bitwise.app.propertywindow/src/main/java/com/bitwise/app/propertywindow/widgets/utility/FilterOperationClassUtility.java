package com.bitwise.app.propertywindow.widgets.utility;

import java.io.File;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCheckBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
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
	private static String filePath = "";

	/**
	 * Creates the new class wizard.
	 * 
	 * @param fileName
	 *            the file name
	 */
	public static void createNewClassWizard(Text fileName) {
		OpenNewClassWizardAction wizard = new OpenNewClassWizardAction();
		wizard.setOpenEditorOnFinish(false);
		NewClassWizardPage page = new NewClassWizardPage();
		page.setSuperClass("java.lang.Object", true);
		page.setMethodStubSelection(false, false, true, true);
		List<String> interfaceList = new ArrayList<String>();
		interfaceList.add("com.bitwiseglobal.bhse.userfunctions.base.FilterBase");
		page.setSuperInterfaces(interfaceList, true);  
		wizard.setConfiguredWizardPage(page);
		wizard.run();
		if (page.isPageComplete())
			fileName.setText("/" + page.getPackageFragmentRootText() + "/"
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
			filePath = resource.getRawLocation().toOSString();
			String arg = resource.getFullPath().toOSString();
			fileName.setText(arg);
		}
	} 

	/**
	 * Open file editor.
	 * 
	 * @param fileName
	 *            the file name
	 * @return true, if successful
	 */
	public static boolean openFileEditor(String fileName) {
		try {
			File fileToOpen = new File(fileName);
			if(!fileToOpen.isFile())
			{
			Path path = new Path(fileName);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			filePath = file.getRawLocation().toOSString();
			}
			else
			filePath=fileName;
			File fileToEditor = new File(filePath);
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

public static void createOperationalClass(Composite composite,
											PropertyDialogButtonBar eltOperationClassDialogButtonBar,Text fileName
											,Button btnCheckButton,ValidationStatus validationStatus ){
	
	ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(composite);
	eltSuDefaultSubgroupComposite.createContainerWidget();
	eltSuDefaultSubgroupComposite.numberOfBasicWidgets(4);
	
	AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Operation\nClass");
	eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
	
	AbstractELTWidget fileNameText = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(150);
	eltSuDefaultSubgroupComposite.attachWidget(fileNameText);
	
	fileName = (Text) fileNameText.getSWTWidgetControl();
	
	AbstractELTWidget browseButton = new ELTDefaultButton("...").buttonWidth(20);
	eltSuDefaultSubgroupComposite.attachWidget(browseButton);
	
	AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox("Is Parameter").checkBoxLableWidth(100);
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
	
	btnCheckButton=(Button) isParameterCheckbox.getSWTWidgetControl();
	
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.VALIDATION_STATUS, validationStatus);
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
	
}
	
	
}
