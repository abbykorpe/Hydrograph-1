package com.bitwise.app.propertywindow.widgets.customwidgets.lookupproperty;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.datastructure.property.LookupConfigProperty;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.dialogs.FieldDialog;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterPropertyWizard;

public class ELTLookupConfigGrid extends Dialog {
	
	private static final String IN1 = "in1";
	private static final String IN0 = "in0";
	private boolean isSelected;
	private Text drivenText;
	private Text lookupText;
	private boolean islookup;
	private Button[] radio = new Button[2];
	private LookupConfigProperty configProperty = new LookupConfigProperty();
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private ControlDecoration txtDecorator;
	private String drivenKeys,lookupKey;
	private Label driverEditLableAsButton,lookupEditLableAsButton;
	private Map<String,List<String>> propagatedFiledNames;
	private String editImageIconPath = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/editImage.png";
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param propertyDialogButtonBar2 
	 * @param lookupConfigProperty 
	 */
	public ELTLookupConfigGrid(Shell parentShell, PropertyDialogButtonBar propertyDialogButtonBar, LookupConfigProperty lookupConfigProperty) {
		super(parentShell);		
		setShellStyle(SWT.CLOSE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		configProperty = lookupConfigProperty;
		drivenKeys=configProperty.getDriverKey();
		lookupKey=configProperty.getLookupKey();
		this.propertyDialogButtonBar=propertyDialogButtonBar;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	public Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Lookup Configuration");
		container.setLayout(new FillLayout(SWT.VERTICAL));
		
		
		Composite composite = new Composite(container, SWT.BORDER);
		composite.setLayout(new RowLayout(SWT.VERTICAL));
		
		container.getShell().setText("Lookup Configuration");
		
		Label lblNewLabel = new Label(composite, SWT.CENTER);
		lblNewLabel.setLayoutData(new RowData(137, 21));
		lblNewLabel.setText("Lookup Configuration");
		
		Composite portComposite = new Composite(composite, SWT.BORDER);
		portComposite.setLayoutData(new RowData(436, 60));
		portComposite.setBounds(10, 10, 200, 100);
		
		labelWidget(portComposite, SWT.CENTER|SWT.READ_ONLY, new int[]{5, 5, 100, 20}, "Lookup Port");
	    radio[0] = buttonWidget(portComposite, SWT.RADIO, new int[]{105, 5, 90, 20}, IN0);
	    radio[1] = buttonWidget(portComposite, SWT.RADIO, new int[]{105, 25, 90, 20}, IN1); 
	    
	   
	    
	    for(int i=0; i<radio.length;i++){
	    radio[i].addSelectionListener(new SelectionAdapter() {
	    	@Override
			public void widgetSelected(SelectionEvent event) {
	    		Button button = (Button)event.widget;
	    		if(button.getText().equalsIgnoreCase("in1")){
	    			radio[0].setSelection(false);
	    			 radio[1].setSelection(true);	    		
	    			configProperty.setSelected(false);
	    		 
	    		}else{
	    			radio[1].setSelection(false);
	    			radio[0].setSelection(true);	    	 
	    			configProperty.setSelected(true);
	    		}
	    	}
		});
	    }	
		
		//---------------------------------------------------------------------
		Composite keyComposite = new Composite(composite, SWT.BORDER);
		keyComposite.setLayoutData(new RowData(436, 100));
		
		
		labelWidget(keyComposite, SWT.CENTER|SWT.READ_ONLY, new int[]{10, 10, 175, 15}, "Port Type");
		labelWidget(keyComposite, SWT.CENTER|SWT.READ_ONLY, new int[]{191, 10, 235, 15}, "Lookup Key(s)");
		 
		textBoxWidget(keyComposite, new int[]{10, 31, 175, 21}, "driver", false);
		textBoxWidget(keyComposite, new int[]{10, 58, 175, 21}, "lookup", false);
		

			 drivenText = textBoxWidget(keyComposite, new int[]{191, 31, 220, 21},"", false);
			 lookupText = textBoxWidget(keyComposite, new int[]{191, 58, 220, 21}, "", false);
			 drivenText.setBackground(new Color(null,255,255,255));
			 lookupText.setBackground(new Color(null,255,255,255));
			 labelWidget(keyComposite, SWT.CENTER|SWT.READ_ONLY, new int[]{10, 10, 175, 15}, "Port Type");
			 
			 driverEditLableAsButton= labelWidget(keyComposite, SWT.CENTER|SWT.READ_ONLY, new int[]{415, 28, 20, 20}, "Insert Image");
			 driverEditLableAsButton.setImage(new Image(null,editImageIconPath));
			 
			 driverEditLableAsButton.addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseDown(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					
					@Override
					public void mouseUp(MouseEvent e) {
						drivenKeys=launchDialogToSelectFields(drivenKeys,"in0");
						drivenText.setText(drivenKeys);
						
					}

				});
			 
			 lookupEditLableAsButton= labelWidget(keyComposite, SWT.CENTER|SWT.READ_ONLY, new int[]{415, 58, 20, 20}, "");
			 lookupEditLableAsButton.setImage(new Image(null,editImageIconPath));
			 
			 lookupEditLableAsButton.addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseDown(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseUp(MouseEvent e) {
						lookupKey=launchDialogToSelectFields(lookupKey,"in1");
						lookupText.setText(lookupKey);
						

					}

				});
			 
//		ListenerHelper helper = new ListenerHelper();
//		txtDecorator = WidgetUtility.addDecorator(drivenText, Messages.EMPTYFIELDMESSAGE);
//		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
//		drivenText.addListener(SWT.FocusIn, ListenerFactory.Listners.NORMAL_FOCUS_IN.getListener().getListener(propertyDialogButtonBar, helper, drivenText));
//		drivenText.addListener(SWT.FocusOut, ListenerFactory.Listners.NORMAL_FOCUS_OUT.getListener().getListener(propertyDialogButtonBar, helper, drivenText));
//		drivenText.addListener(SWT.Verify, ListenerFactory.Listners.VERIFY_TEXT.getListener().getListener(propertyDialogButtonBar, helper, drivenText));
//		drivenText.addListener(SWT.Modify, ListenerFactory.Listners.MODIFY.getListener().getListener(propertyDialogButtonBar, helper, drivenText));
		
//		drivenText.addModifyListener(new ModifyListener() {			
//			@Override
//			public void modifyText(ModifyEvent e) {
//				configProperty.setDriverKey(((Text)e.widget).getText());
//			}
//		});
		
//		ListenerHelper help = new ListenerHelper();
//		txtDecorator = WidgetUtility.addDecorator(lookupText, Messages.EMPTYFIELDMESSAGE);
//		help.put(HelperType.CONTROL_DECORATION, txtDecorator);
//		lookupText.addListener(SWT.FocusIn, ListenerFactory.Listners.NORMAL_FOCUS_IN.getListener().getListener(propertyDialogButtonBar, help, lookupText));
//		lookupText.addListener(SWT.FocusOut, ListenerFactory.Listners.NORMAL_FOCUS_OUT.getListener().getListener(propertyDialogButtonBar, help, lookupText));
//		lookupText.addListener(SWT.Verify, ListenerFactory.Listners.VERIFY_TEXT.getListener().getListener(propertyDialogButtonBar, help, lookupText));
//		lookupText.addListener(SWT.Modify, ListenerFactory.Listners.MODIFY.getListener().getListener(propertyDialogButtonBar, help, lookupText));
		
//		
//		lookupText.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent e) {
//				configProperty.setLookupKey(((Text)e.widget).getText());
//			}
//		});
		populateWidget();
		return container;
	}
	
	public void populateWidget(){
		if (StringUtils.isNotBlank(configProperty.getDriverKey())){
			drivenText.setText(configProperty.getDriverKey());
		}
		if (StringUtils.isNotBlank(configProperty.getLookupKey())){
			lookupText.setText(configProperty.getLookupKey());	
		}
		radio[0].setSelection(configProperty.isSelected() ? true : false);
		radio[1].setSelection(configProperty.isSelected() ? false : true);		
	}
		
	public Button buttonWidget(Composite parent, int style, int[] bounds, String value){
		Button button = new Button(parent, style);
			button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
			button.setText(value);
		
		return button;
	}

	public Text textBoxWidget(Composite parent, int[] bounds, String textValue, boolean value){
		Text text = new Text(parent, SWT.BORDER|SWT.READ_ONLY|SWT.CENTER);
		text.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		text.setText(textValue);
		text.setEditable(value);
		
		return text;
	}
	
	public Label labelWidget(Composite parent, int style, int[] bounds, String value){
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);
		
		return label;
	}
	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,	true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(470, 420);
	}

	public LookupConfigProperty getConfigProperty() {
		return configProperty;
	}

	public void setConfigProperty(LookupConfigProperty configProperty) {
		this.configProperty = configProperty;
	}

	public static void main(String[] args) {
		 ELTLookupConfigGrid eltLookupConfigGrid = new ELTLookupConfigGrid(new Shell(),null,new LookupConfigProperty());
		 eltLookupConfigGrid.open();
	}
	
	
	private String launchDialogToSelectFields(String availableValues,String socketId)
	{	
		FieldDialog fieldDialog = new FieldDialog(new Shell(),propertyDialogButtonBar);
		fieldDialog.setPropertyFromCommaSepratedString(availableValues);
		fieldDialog.setSourceFieldsFromPropagatedSchema(propagatedFiledNames.get(socketId));
		fieldDialog.open();
		return fieldDialog.getResultAsCommaSeprated();
		
	}

	public void setPropagatedFieldProperty(Map<String, List<String>> propagatedFiledNames)
	{
		this.propagatedFiledNames=propagatedFiledNames;
	}
	
	
@Override
protected void okPressed() {
	configProperty.setLookupKey(lookupKey);
	configProperty.setDriverKey(drivenKeys);
	super.okPressed();
}
}
 
