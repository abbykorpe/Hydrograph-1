package hydrograph.ui.dataviewer.preferencepage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import hydrograph.ui.common.swt.customwidget.HydroGroup;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.PreferenceConstants;
import hydrograph.ui.dataviewer.Activator;
import hydrograph.ui.dataviewer.constants.Messages;

public class ServicesPreference extends PreferencePage implements IWorkbenchPreferencePage {
	
	private Composite grpServiceDetailsCmposite;
	private IntegerFieldEditor localPortNo;
	private IntegerFieldEditor remotePortNo;
	private List<FieldEditor> editorList;
	private BooleanFieldEditor useRemoteConfigBooleanFieldEditor;
	private StringFieldEditor remoteHostFieldEditor;
	private Button useRemoteConfigbutton;
	private static final String DEFAULT_LOCAL_PORT = "8004";
	private static final String DEFAULT_REMOTE_PORT = "8004";
	private static final boolean DEFAULT_USE_REMOTE_CONFIGURATION_CHECK = false;
	
	public ServicesPreference() {
		super();
		setPreferenceStore(PlatformUI.getWorkbench().getPreferenceStore());
	}
	
	
	
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore preferenceStore =PlatformUI.getWorkbench().getPreferenceStore();
		//IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setDefault(PreferenceConstants.LOCAL_PORT_NO, DEFAULT_LOCAL_PORT);
		preferenceStore.setDefault(PreferenceConstants.REMOTE_PORT_NO, DEFAULT_REMOTE_PORT);
		preferenceStore.setDefault(PreferenceConstants.USE_REMOTE_CONFIGURATION, DEFAULT_USE_REMOTE_CONFIGURATION_CHECK);
		setPreferenceStore(preferenceStore);
	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		final Composite parentComposite = new Composite(parent, SWT.None);
		parentComposite.setToolTipText("Export Data");
		GridData parentCompositeData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		parentCompositeData.grabExcessHorizontalSpace = true;
		
		parentCompositeData.grabExcessVerticalSpace = true;
		parentComposite.setLayout(new GridLayout(1, false));
		parentComposite.setLayoutData(parentCompositeData);
		
		HydroGroup grpServiceDetails = new HydroGroup(parentComposite, SWT.NONE);
		grpServiceDetails.setHydroGroupText("Port and Remote Host Details");
		
		GridLayout gl_grpServiceDetails = new GridLayout(1, false);
		GridData gd_grpServiceDetailsData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		grpServiceDetails.setLayout(new GridLayout(1,false));
		grpServiceDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpServiceDetails.getHydroGroupClientArea().setLayout(gl_grpServiceDetails);
		grpServiceDetails.getHydroGroupClientArea().setLayoutData(gd_grpServiceDetailsData);
		
		grpServiceDetailsCmposite = new Composite(grpServiceDetails.getHydroGroupClientArea(), SWT.NONE);
		GridData serviceGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		grpServiceDetailsCmposite.setLayout(new GridLayout(1,false));
		grpServiceDetailsCmposite.setLayoutData(serviceGridData);

		localPortNo = new IntegerFieldEditor(PreferenceConstants.LOCAL_PORT_NO, Messages.LOCAL_PORT_NO_LABEL, grpServiceDetailsCmposite, 5);
		localPortNo.getTextControl(grpServiceDetailsCmposite).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String value = ((Text)event.getSource()).getText();
				validatePortField(value,localPortNo,Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		localPortNo.getTextControl(grpServiceDetailsCmposite).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
				String value = ((Text)event.getSource()).getText();
				validatePortField(value,localPortNo,Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		localPortNo.setPreferenceStore(getPreferenceStore());
		localPortNo.load();
		
		remotePortNo = new IntegerFieldEditor(PreferenceConstants.REMOTE_PORT_NO, Messages.REMOTE_PORT_NO_LABEL, grpServiceDetailsCmposite, 5);
		remotePortNo.getTextControl(grpServiceDetailsCmposite).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String value = ((Text)event.getSource()).getText();
				validatePortField(value,remotePortNo,Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		remotePortNo.getTextControl(grpServiceDetailsCmposite).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
				String value = ((Text)event.getSource()).getText();
				validatePortField(value,remotePortNo,Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		remotePortNo.setPreferenceStore(getPreferenceStore());
		remotePortNo.load();
		
		new Label(grpServiceDetailsCmposite, SWT.None).setText(Messages.OVERRIDE_REMOTE_HOST_LABEL);
		Composite headerRemoteComposite = new Composite(grpServiceDetailsCmposite, SWT.None);
		useRemoteConfigBooleanFieldEditor = new BooleanFieldEditor(PreferenceConstants.USE_REMOTE_CONFIGURATION, "", SWT.DEFAULT, headerRemoteComposite);
		useRemoteConfigbutton = (Button) useRemoteConfigBooleanFieldEditor.getDescriptionControl(headerRemoteComposite);
		getPreferenceStore().setDefault(PreferenceConstants.USE_REMOTE_CONFIGURATION, false);
		useRemoteConfigBooleanFieldEditor.setPreferenceStore(getPreferenceStore());
		useRemoteConfigBooleanFieldEditor.load();
		
		addListenerToRemoteConfigBooleanEditor(headerRemoteComposite);
		
		
		remoteHostFieldEditor = new StringFieldEditor(PreferenceConstants.REMOTE_HOST, Messages.REMOTE_HOST_NAME_LABEL, grpServiceDetailsCmposite);
		remoteHostFieldEditor.getTextControl(grpServiceDetailsCmposite).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validateRemoteHost();
			}
		});
		remoteHostFieldEditor.setPreferenceStore(getPreferenceStore());
		remoteHostFieldEditor.load();
		remoteHostFieldEditor.setErrorMessage(null);
		
		addFields(localPortNo);
		addFields(remotePortNo);
		addFields(remoteHostFieldEditor);
		
		return null;
	}
	
	@Override
	public boolean performOk() { 
		localPortNo.getStringValue();
		localPortNo.store();
		remotePortNo.store();
		remoteHostFieldEditor.store();
		useRemoteConfigBooleanFieldEditor.store();
		return super.performOk();
	}

	@Override
	protected void performApply() {
		localPortNo.store();
		remotePortNo.store();
		useRemoteConfigBooleanFieldEditor.store();
		remoteHostFieldEditor.store();
	}
	@Override
	protected void performDefaults() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		localPortNo.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.LOCAL_PORT_NO));
		remotePortNo.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.REMOTE_PORT_NO));
		useRemoteConfigBooleanFieldEditor.loadDefault();
		super.performDefaults();
}
	
	private void validatePortField(String value, IntegerFieldEditor editor, String message){
		if(StringUtils.isBlank(value) || !value.matches(Constants.PORT_VALIDATION_REGEX)){
			showErrorMessage(editor, message,false);
		}else{
			showErrorMessage(editor, null,true);
			checkState();
		}
	}
	private void showErrorMessage(IntegerFieldEditor editor, String message,boolean validState) {
		setErrorMessage(message);
		editor.setErrorMessage(message);
		setValid(validState);
	}

	
	private void checkState() {
		if(editorList != null){
			int size = editorList.size();
			for(int i=0; i<size; i++){
				FieldEditor fieldEditor = editorList.get(i);
				if(StringUtils.isNotBlank(((StringFieldEditor)fieldEditor).getErrorMessage())){
					setErrorMessage(((StringFieldEditor)fieldEditor).getErrorMessage());
					setValid(false);
					break;
				}else{
					setValid(true);
				}
			}
		}
	}
	

	private void addFields(FieldEditor editor){
		if (editorList == null) {
			editorList = new ArrayList<>();
		}
		editorList.add(editor);
	}
	
	private void addListenerToRemoteConfigBooleanEditor(Composite headerRemoteComposite) {
		useRemoteConfigbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validateRemoteHost();
			}
		});
	}
	private void validateRemoteHost() {
		if(useRemoteConfigbutton.getSelection() && StringUtils.isEmpty(remoteHostFieldEditor.getStringValue())){
			remoteHostFieldEditor.setErrorMessage(Messages.BLANK_REMOTE_HOST_NAME_ERROR);
			checkState();
		}else{
			setErrorMessage(null);
			remoteHostFieldEditor.setErrorMessage("");
			setValid(true);
			checkState();;
		}
	}
	
	@Override
	public IPreferenceStore getPreferenceStore() {
		return PlatformUI.getPreferenceStore();
	}
}
