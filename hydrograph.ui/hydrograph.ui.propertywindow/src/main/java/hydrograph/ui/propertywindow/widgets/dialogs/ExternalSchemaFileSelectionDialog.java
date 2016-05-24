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


package hydrograph.ui.propertywindow.widgets.dialogs;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.utility.Extensions;
import hydrograph.ui.propertywindow.widgets.utility.FilterOperationClassUtility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.slf4j.Logger;


/**
 * @author Bitwise
 */
public class ExternalSchemaFileSelectionDialog extends ElementTreeSelectionDialog {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ExternalSchemaFileSelectionDialog.class);
	
    private String[] extensions;
    private AbstractELTWidget eltDefaultText;
    private FilterOperationClassUtility filterOperationClassUtility ;
    
    private static ITreeContentProvider contentProvider = new ITreeContentProvider() {
        public Object[] getChildren(Object element) {
            if (element instanceof IContainer) {
                try {
                    return ((IContainer) element).members();
                }
                catch (CoreException e) {
                }
            }
            return null;
        }

        public Object getParent(Object element) {
            return ((IResource) element).getParent();
        }

        public boolean hasChildren(Object element) {
            return element instanceof IContainer;
        }

        public Object[] getElements(Object input) {
            return (Object[]) input;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        } 
    };

    private static final IStatus OK = new Status(IStatus.OK, "0", 0, "", null);
    private static final IStatus ERROR = new Status(IStatus.ERROR, "1", 1, "", null);

    /*
     * Validator
     */
    private ISelectionStatusValidator validator = new ISelectionStatusValidator() {
        public IStatus validate(Object[] selection) {
        	if(selection.length == 1 && selection[0] instanceof IFile)
        	{
        		((Text)eltDefaultText.getSWTWidgetControl()).setText(((IFile) selection[0]).getName());
        	}
        	if(extensions.length!=0 && Extensions.SCHEMA.toString().equalsIgnoreCase(extensions[0])){
        	 if(selection.length == 1 && selection[0] instanceof IFolder)
        	 {
        		 return OK;
        	 }
        	}
        	
            return selection.length == 1 && selection[0] instanceof IFile
                    && checkExtension(((IFile) selection[0]).getFileExtension()) ? OK : ERROR;
        }
    };

	/**
	 * Instantiates a new resource file selection dialog.
	 * 
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @param type
	 *            the type
	 */
    public ExternalSchemaFileSelectionDialog(String title, String message, String[] type,FilterOperationClassUtility filterOperationClassUtility) {
        this(Display.getDefault().getActiveShell(), WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(),
                contentProvider);
        this.extensions = type;
        
        setTitle(title);
        setMessage(message);
        setInput(computeInput());
        setValidator(validator);
        this.filterOperationClassUtility=filterOperationClassUtility;
    }

	/**
	 * Instantiates a new resource file selection dialog.
	 * 
	 * @param parent
	 *            the parent
	 * @param labelProvider
	 *            the label provider
	 * @param contentProvider
	 *            the content provider
	 */
    public ExternalSchemaFileSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
    } 

    /*
     * Show projects
     */
    private Object[] computeInput() {
        /*
         * Refresh projects tree.
         */
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (int i = 0; i < projects.length; i++) {
            try {
                projects[i].refreshLocal(IResource.DEPTH_INFINITE, null);
            } catch (CoreException e) {
            	logger.debug("Unable to refresh local file");
            }
        }

        try {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_ONE, null);
        } catch (CoreException e) {
        }
        List<IProject> openProjects = new ArrayList<IProject>(projects.length);
        for (int i = 0; i < projects.length; i++) {
            if (projects[i].isOpen()) {
                openProjects.add(projects[i]);
            }
        } 
        return openProjects.toArray();
    }

    /*
     * Check file extension
     */
    private boolean checkExtension(String name) {
        if (name.equals("*")) {
            return true;
        }

        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].equals(name)) {
                return true;
            }
        } 
        return false;
    } 
    @Override
    protected Control createDialogArea(Composite parent) {
    	// TODO Auto-generated method stub
    	Composite composite=super.createDialogArea(parent).getParent();
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(composite);
		eltSuDefaultSubgroupComposite.createContainerWidget();
		eltSuDefaultSubgroupComposite.numberOfBasicWidgets(2);

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("      "+ Messages.FILE_NAME +"  :");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		eltDefaultText= new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(280);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultText);
    	return composite;
    }


@Override
protected void okPressed() {
	// TODO Auto-generated method stub
	filterOperationClassUtility.setFileNameTextBoxValue(((Text)eltDefaultText.getSWTWidgetControl()).getText());
	super.okPressed();
}


}