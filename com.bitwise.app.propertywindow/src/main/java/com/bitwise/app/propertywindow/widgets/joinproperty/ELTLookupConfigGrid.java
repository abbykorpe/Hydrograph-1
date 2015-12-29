package com.bitwise.app.propertywindow.widgets.joinproperty;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ELTVerifyTextListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class ELTLookupConfigGrid extends Dialog {
	
	private static final String IN1 = "in1";
	private static final String IN0 = "in0";
	private Text drivenText;
	private Text lookupText;
	private boolean islookup;
	private Button[] radio = new Button[2];
	private LookupConfigProperty configProperty = new LookupConfigProperty();
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private ControlDecoration txtDecorator;
 
	 
	 
	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param lookupConfigProperty 
	 */
	public ELTLookupConfigGrid(Shell parentShell, LookupConfigProperty lookupConfigProperty) {
		super(parentShell);		
		setShellStyle(SWT.CLOSE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		configProperty = lookupConfigProperty;
	 
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	public Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
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
	    radio[0].setSelection(true);
	    radio[1] = buttonWidget(portComposite, SWT.RADIO, new int[]{105, 25, 90, 20}, IN1); 
	    
	   
	    
	    for(int i=0; i<radio.length;i++){
	    radio[i].addSelectionListener(new SelectionAdapter() {
	    	@Override
			public void widgetSelected(SelectionEvent event) {
	    		Button button = (Button)event.widget;
	    		if(configProperty.isSelected()){
	    			radio[0].setSelection(false);
	    			radio[1].setSelection(true);
	    		 
	    		}else{
	    			radio[1].setSelection(false);
	    			radio[0].setSelection(true);
	    		 
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
		

			 drivenText = textBoxWidget(keyComposite, new int[]{191, 31, 235, 21},"", true);
			 lookupText = textBoxWidget(keyComposite, new int[]{191, 58, 235, 21}, "", true);
		
		ListenerHelper helper = new ListenerHelper();
		txtDecorator = WidgetUtility.addDecorator(drivenText, Messages.EMPTYFIELDMESSAGE);
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		ELTVerifyTextListener list = new ELTVerifyTextListener();
		drivenText.addListener(SWT.Verify, list.getListener(propertyDialogButtonBar, helper, drivenText));
		
		drivenText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				configProperty.setDriverKey(((Text)e.widget).getText());
			}
		});
		
		ListenerHelper help = new ListenerHelper();
		help.put(HelperType.CONTROL_DECORATION, WidgetUtility.addDecorator(lookupText, Messages.EMPTYFIELDMESSAGE));
		ELTVerifyTextListener listener = new ELTVerifyTextListener();
		lookupText.addListener(SWT.Verify, listener.getListener(propertyDialogButtonBar, help, lookupText));
		lookupText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				configProperty.setLookupKey(((Text)e.widget).getText());
			}
		});
		populateWidget();
		return container;
	}
	
	public void populateWidget(){
		if (configProperty.getDriverKey() != null)
			drivenText.setText(configProperty.getDriverKey());
		if (configProperty.getLookupKey() != null)
			lookupText.setText(configProperty.getLookupKey());
		
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

	
	
}
 
