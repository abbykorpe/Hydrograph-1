package hydrograph.ui.dataviewer.preferances;

import hydrograph.ui.dataviewer.Activator;
import hydrograph.ui.dataviewer.constants.PreferenceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;


public class PreferencePageViewData extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	
	private DirectoryFieldEditor tempPathFieldEditor;
	private DirectoryFieldEditor defaultPathFieldEditor;
	private IntegerFieldEditor memoryFieldEditor;
	private IntegerFieldEditor recordLimitFieldEditor;
	
	private StringFieldEditor stringFieldEditor;
	private StringFieldEditor delimiter;
	private StringFieldEditor quoteCharactor;
	private BooleanFieldEditor includeHeaders;
	public final static String PREFERENCE_FILE_PATH = Platform.getInstallLocation().getURL().getPath() + "config/Preferences/preferences.properties";
	private static final String DELIMITER="Delimiter";
	private static final String QUOTE_CHARACTOR="Quote Charactor";
	private static final String INCLUDE_HEADERS="Include Headers";
	private static final String DELIMITER_PROPERTY="delimiter";
	private static final String QUOTE_CHARACTOR_PROPERTY="quoteCharactor";
	private static final String INCLUDE_HEADERS_PROPERTY="includeHeader";
	private static final String WARNING="Warning";
	private static final String ERROR_MESSAGE="Exported file might not open in Excel due to change in default delimiter and quote character.";
	private static final String DEFAULT_DELIMITER=",";
	private static final String DEFAULT_QUOTE_CHARACTOR="\"";
	
	public PreferencePageViewData() {
		super(GRID);
		setPreferenceStore(PlatformUI.getWorkbench().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		tempPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.TEMPPATH, "&Temp Path", 
				getFieldEditorParent());
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		tempPathFieldEditor.setFilterPath(new File(""+path));
		addField(tempPathFieldEditor);
		
		defaultPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.DEFAULTPATH, "&Export Default Path", 
				getFieldEditorParent());
		defaultPathFieldEditor.setFilterPath(new File(""+path));
		addField(defaultPathFieldEditor);
		
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new RowLayout());
		composite.setBounds(200, 0, 300, 150);
				
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
		addField(stringFieldEditor);
		Composite composite3 = new Composite(parent, SWT.None);
		composite3.setLayout(new RowLayout());
		
		Composite composite2 = new Composite(parent, SWT.None);
		composite2.setLayout(new RowLayout());
		
		Label lbl2 = new Label(composite3, SWT.None);
		lbl2.setText("Export Data Preferences");
		
		
		
		Label lb2= new Label(parent, SWT.None);
		lb2.setText("          ");

		delimiter=new StringFieldEditor(PreferenceConstants.DELIMITER,DELIMITER, -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE,getFieldEditorParent());
		
		addField(delimiter);
		quoteCharactor=new StringFieldEditor(PreferenceConstants.QUOTE_CHARACTOR,QUOTE_CHARACTOR, -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
		addField(quoteCharactor);
		includeHeaders=new BooleanFieldEditor(PreferenceConstants.INCLUDE_HEADER,INCLUDE_HEADERS, BooleanFieldEditor.DEFAULT, getFieldEditorParent());
		addField(includeHeaders);
	}
	
	
	@Override
	protected void checkState() {
		super.checkState();
	}
	

	@Override
	public void init(IWorkbench workbench) {
		
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		try {
			readExportDataPreferencesFromPropertyFile(preferenceStore);
			preferenceStore.setDefault(PreferenceConstants.MEMORYSIZE, "100");
			preferenceStore.setDefault(PreferenceConstants.RECORDSLIMIT, "100");
			setPreferenceStore(preferenceStore);
			setPreferenceStore(Activator.getDefault().getPreferenceStore());
			setDescription("A demonstration of a preference page to View Data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void readExportDataPreferencesFromPropertyFile(IPreferenceStore preferenceStore)
			throws FileNotFoundException, IOException {
		FileInputStream fileInput = new FileInputStream(new File(PREFERENCE_FILE_PATH));
		Properties properties = new Properties();
		properties.load(fileInput);
		fileInput.close();
		Enumeration enumeration = properties.keys();
		while (enumeration.hasMoreElements()) {
			String propertyKey = (String) enumeration.nextElement();
			if(propertyKey.equalsIgnoreCase(DELIMITER_PROPERTY))
			{
				preferenceStore.setDefault(PreferenceConstants.DELIMITER,properties.getProperty(propertyKey));
			}
			else if(propertyKey.equalsIgnoreCase(QUOTE_CHARACTOR_PROPERTY))
			{
				preferenceStore.setDefault(PreferenceConstants.QUOTE_CHARACTOR,properties.getProperty(propertyKey));
			}
			else if(propertyKey.equalsIgnoreCase(INCLUDE_HEADERS_PROPERTY))
			{
				preferenceStore.setDefault(PreferenceConstants.INCLUDE_HEADER, properties.getProperty(propertyKey));
			}
		}
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
		delimiter.store();
		quoteCharactor.store();
		includeHeaders.store();
	}
	
	@Override
	protected void performDefaults() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		memoryFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.MEMORYSIZE));
		recordLimitFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.RECORDSLIMIT));
		delimiter.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.DELIMITER));
		quoteCharactor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.QUOTE_CHARACTOR));
		includeHeaders.loadDefault();
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
		if (!delimiter.getStringValue().equalsIgnoreCase(DEFAULT_DELIMITER) && !quoteCharactor.getStringValue().equalsIgnoreCase(DEFAULT_QUOTE_CHARACTOR)) {
			MessageBox messageBox = new MessageBox(new Shell(), SWT.OK |SWT.CANCEL | SWT.ICON_WARNING);
			messageBox.setText(WARNING);
			messageBox.setMessage(ERROR_MESSAGE);
			int response=messageBox.open();
			if(response==SWT.OK)
			{
				storePreferences();
				return super.performOk();
			}
			else
			{
				return false;
			}
		}
		else
		{
			storePreferences();
			return super.performOk();
		}
		
	}
	private void storePreferences() {
		String s = getMemoryFieldEditor().getStringValue();
		tempPathFieldEditor.store();
		defaultPathFieldEditor.store();
		memoryFieldEditor.store();
		recordLimitFieldEditor.store();
		delimiter.store();
		quoteCharactor.store();
		includeHeaders.store();
	}
	
}
