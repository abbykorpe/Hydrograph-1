package hydrograph.ui.dataviewer.preferances;



import hydrograph.ui.dataviewer.Activator;
import hydrograph.ui.dataviewer.constants.PreferenceConstants;

import java.io.File;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;


public class PreferencePageViewData extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	
	//private static final Logger logger = LogFactory.INSTANCE.getLogger(PreferencePageViewData.class);

	private DirectoryFieldEditor tempPathFieldEditor;
	private DirectoryFieldEditor defaultPathFieldEditor;
	private IntegerFieldEditor memoryFieldEditor;
	private IntegerFieldEditor recordLimitFieldEditor;
	
	private StringFieldEditor stringFieldEditor;
	
	public PreferencePageViewData() {
		super(GRID);
		setPreferenceStore(PlatformUI.getWorkbench().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		tempPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.TEMPPATH, "&Temp Path", 
				getFieldEditorParent());
		//tempPathFieldEditor.setEmptyStringAllowed(false);
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		tempPathFieldEditor.setFilterPath(new File(""+path));
		addField(tempPathFieldEditor);
		
		defaultPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.DEFAULTPATH, "&Export Default Path", 
				getFieldEditorParent());
		//defaultPathFieldEditor.setEmptyStringAllowed(false);
		defaultPathFieldEditor.setFilterPath(new File(""+path));
		addField(defaultPathFieldEditor);
		
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new RowLayout());
		composite.setBounds(200, 0, 300, 150);
		
		/*Label labl = new Label(composite, SWT.BORDER);
		labl.setText("     ");
		labl.setEnabled(false);
		labl.setVisible(false);
		labl.setBounds(0, 0, 100, 16);*/
		
		Composite composite1 = new Composite(parent, SWT.None);
		composite1.setLayout(new RowLayout());
		composite1.setBounds(0, 4, 300, 80);
		
		Text lbl = new Text(composite1, SWT.None|SWT.READ_ONLY);
		lbl.setText("Memory size should be define in MB");
		lbl.setEnabled(false);
		
		
		Label lb= new Label(parent, SWT.None);
		lb.setText("          ");
		
		
		memoryFieldEditor = new IntegerFieldEditor(PreferenceConstants.MEMORYSIZE, "&Memory Size", getFieldEditorParent());
		memoryFieldEditor.setEmptyStringAllowed(false);
		memoryFieldEditor.setErrorMessage("Memory Size value should be an integer");
		addField(memoryFieldEditor);
		
		recordLimitFieldEditor = new IntegerFieldEditor(PreferenceConstants.RECORDSLIMIT, "&Record Limit", getFieldEditorParent());
		recordLimitFieldEditor.setEmptyStringAllowed(false);
		recordLimitFieldEditor.setErrorMessage("Record Limit value should be an integer");
		addField(recordLimitFieldEditor);
		
		
		stringFieldEditor = new StringFieldEditor(PreferenceConstants.FILENAME, "&File Name", getFieldEditorParent());
		stringFieldEditor.setEmptyStringAllowed(false);
		//stringFieldEditor.setErrorMessage("Record Limit value should be an integer");
		addField(stringFieldEditor);
	}
	
	
	@Override
	protected void checkState() {
		super.checkState();
		/*if(isValid())
			return;
		
		if(memoryFieldEditor.getStringValue() != null){
			setErrorMessage("Must have define memory size");
			setValid(true);
		}else{
			setErrorMessage(null);
			setValid(false);
		}*/
	}
	

	@Override
	public void init(IWorkbench workbench) {
		
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setDefault(PreferenceConstants.MEMORYSIZE, "100");
		preferenceStore.setDefault(PreferenceConstants.RECORDSLIMIT, "100");
		setPreferenceStore(preferenceStore);
		
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page to View Data");
		
	}

	@Override
	protected void initialize() {
		super.initialize();
		IWorkspace iWorkspace = ResourcesPlugin.getWorkspace();
		IProject iProjects = iWorkspace.getRoot().getProject();
	}
	
	@Override
	protected void performApply() {
		super.performApply();
		memoryFieldEditor.store();
		recordLimitFieldEditor.store();
	}
	
	@Override
	protected void performDefaults() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		memoryFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.MEMORYSIZE));
		recordLimitFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.RECORDSLIMIT));
	}

	public  IntegerFieldEditor getMemoryFieldEditor() {
		return memoryFieldEditor;
	}
	
	public DirectoryFieldEditor getDefaultPathFieldEditor() {
		return defaultPathFieldEditor;
	}
	
	public DirectoryFieldEditor getTempPathFieldEditor() {
		return tempPathFieldEditor;
	}
	public IntegerFieldEditor getRecordLimitFieldEditor() {
		return recordLimitFieldEditor;
	}
	

	@Override
	public boolean performOk() {
		String s = getMemoryFieldEditor().getStringValue();
		
		tempPathFieldEditor.store();
		defaultPathFieldEditor.store();
		memoryFieldEditor.store();
		recordLimitFieldEditor.store();
		
		return super.performOk();
	}
	
}
