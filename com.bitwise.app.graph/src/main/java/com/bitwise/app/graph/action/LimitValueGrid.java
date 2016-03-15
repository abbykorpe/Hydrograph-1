package com.bitwise.app.graph.action;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.datastructure.property.DebugProperty;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.runconfig.EmptyTextListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;


/**
 * @author Bitwise
 *
 */
public class LimitValueGrid extends Dialog {
	private Text textBox;
	protected long limitValue = 100L;
	private List<String> ITEMS = Arrays.asList(com.bitwise.app.graph.Messages.DEBUG_DEFAULT, com.bitwise.app.graph.Messages.DEBUG_CUSTOM, com.bitwise.app.graph.Messages.DEBUG_ALL);
	private DebugProperty debugProperty;
	private Combo combo;
	private boolean okselection;
	

	public boolean isOkselection() {
		return okselection;
	}

	public void setOkselection(boolean okselection) {
		this.okselection = okselection;
	}

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LimitValueGrid(Shell parentShell) {
		super(parentShell);
		debugProperty = new DebugProperty();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText(com.bitwise.app.graph.Messages.RECORD_LIMIT);
		Composite composite = new Composite(container, SWT.BORDER);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 363;
		gd_composite.heightHint = 118;
		composite.setLayoutData(gd_composite);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(32, 75, 78, 15);
		lblNewLabel.setText(com.bitwise.app.graph.Messages.RECORD_LIMIT);
		
		textBox = new Text(composite, SWT.BORDER);
		textBox.setBounds(132, 67, 181, 23);
		textBox.setEnabled(false);
		
		Label limitLabel = new Label(composite, SWT.NONE);
		limitLabel.setBounds(32, 26, 78, 15);
		limitLabel.setText(com.bitwise.app.graph.Messages.LIMIT_VALUE);
		EmptyTextListener emptyTextListener = new EmptyTextListener(com.bitwise.app.graph.Messages.RECORD_LIMIT);
		textBox.addModifyListener(emptyTextListener);
		final ControlDecoration txtDecorator = WidgetUtility.addDecorator(textBox, Messages.FIELDPHASE);
		txtDecorator.setMarginWidth(3);
		txtDecorator.hide();
		
		textBox.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				Text txt= (Text)event.widget;
				if(StringUtils.isNotBlank((txt.getText()))){
					txtDecorator.hide();
					limitValue =  Long.parseLong(txt.getText());
				}
			}
		});
		
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		final Listener listener = ListenerFactory.Listners.VERIFY_NUMERIC.getListener().getListener(null, helper, textBox);
		
		combo = new Combo(composite, SWT.READ_ONLY);
		combo.setBounds(132, 18, 181, 23);
		combo.setItems((String[])ITEMS.toArray());
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					String comboBox = ((Combo)e.widget).getText();
					debugProperty.setComboBoxIndex(ITEMS.indexOf(combo));
					if(com.bitwise.app.graph.Messages.DEBUG_CUSTOM.equalsIgnoreCase(comboBox)) {
						textBox.setEnabled(true);
						txtDecorator.show();
						textBox.addListener(SWT.Verify, listener);
						debugProperty.setLimit(textBox.getText()); 
					}else if(com.bitwise.app.graph.Messages.DEBUG_ALL.equalsIgnoreCase(comboBox)) {
						textBox.setEnabled(false);
						textBox.removeListener(SWT.Verify,listener);
						txtDecorator.hide();
						limitValue = -1L;
					}else{
						textBox.setEnabled(false);
                        textBox.removeListener(SWT.Verify,listener);
                        txtDecorator.hide();
					}
			}
		});
		
		Label defaultValueLabel = new Label(composite, SWT.READ_ONLY);
		defaultValueLabel.setBounds(132, 88, 181, 15);
		defaultValueLabel.setText(com.bitwise.app.graph.Messages.DEFAULT_LIMIT);
		
		return container;
	}

	
	public Long getLimitValue(){
		return this.limitValue;
	}
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		setOkselection(true);
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		// TODO Auto-generated method stub
		super.cancelPressed();
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(393, 228);
	}
  
}
