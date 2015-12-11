package com.bitwise.app.menus.importWizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.menus.messages.Messages;

public class ImportEngineXmlWizard extends Wizard implements IImportWizard {
	private static final Logger lOGGEER = LogFactory.INSTANCE.getLogger(ImportEngineXmlWizard.class);
	private ImportEngineXmlWizardPage mainPage;

	public ImportEngineXmlWizard() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		lOGGEER.debug("Import target XML : Creating files before finish");
		IFile file = mainPage.createNewFile();
        if (file == null)
            return false;
        return true;
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		lOGGEER.debug("Initializing Import Engine Wizard");
		setWindowTitle(Messages.IMPORT_WINDOW_TITLE_TEXT); 
		setNeedsProgressMonitor(true);
		mainPage = new ImportEngineXmlWizardPage(Messages.IMPORT_WINDOW_TITLE_TEXT,selection);
	}
	
	/* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
    	lOGGEER.debug("Adding pages to Import Engine Wizard");
        super.addPages(); 
        addPage(mainPage);        
    }

}
