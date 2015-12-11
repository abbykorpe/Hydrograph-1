package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;


// TODO: Auto-generated Javadoc
/**
 * The Class ELTFilePathWidget.
 * 
 * @author Bitwise
 */
public class ELTFilePathWidget extends AbstractWidget{
	
	private Text textBox;
	private Object properties;
	private String propertyName;
	private ControlDecoration txtDecorator;
	private ControlDecoration decorator;
	private Button button;
	

	/**
	 * Instantiates a new ELT file path widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTFilePathWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);

		this.properties =  componentConfigrationProperty.getPropertyValue();
		this.propertyName = componentConfigrationProperty.getPropertyName();
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(container.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("File Path");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		AbstractELTWidget eltDefaultTextBox = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(200);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultTextBox);
		
		textBox = (Text) eltDefaultTextBox.getSWTWidgetControl();
		decorator=WidgetUtility.addDecorator(textBox, Messages.EMPTYFIELDMESSAGE);
		
		textBox.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(textBox.getText().isEmpty()){
					decorator.show();
					textBox.setBackground(new Color(Display.getDefault(), 255, 255, 204));
				}
				else{
					decorator.hide();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				decorator.hide();
				textBox.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			}
		});
		
		AbstractELTWidget eltDefaultButton = new ELTDefaultButton("...").buttonWidth(35);
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		button=(Button)eltDefaultButton.getSWTWidgetControl();
		
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				decorator.hide();
				textBox.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
			
		
		txtDecorator = WidgetUtility.addDecorator(textBox, Messages.CHARACTERSET);
		
		ListenerHelper helper = new ListenerHelper();
		helper.put(HelperType.CONTROL_DECORATION, txtDecorator);
		helper.put(HelperType.VALIDATION_STATUS, validationStatus);

		try {
			eltDefaultTextBox.attachListener(ListenerFactory.Listners.EVENT_CHANGE.getListener(), propertyDialogButtonBar,  null,eltDefaultTextBox.getSWTWidgetControl());
			eltDefaultTextBox.attachListener(ListenerFactory.Listners.MODIFY.getListener(), propertyDialogButtonBar,  helper, eltDefaultTextBox.getSWTWidgetControl());
			eltDefaultButton.attachListener(ListenerFactory.Listners.FILE_DIALOG_SELECTION.getListener(), propertyDialogButtonBar, helper, eltDefaultButton.getSWTWidgetControl(),eltDefaultTextBox.getSWTWidgetControl());
			//eltDefaultTextBox.attachListener(listenerFactory.getListener("ELTFocusOutListener"), propertyDialogButtonBar,  helper,eltDefaultTextBox.getSWTWidgetControl());
			} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		populateWidget();
	}

	private void populateWidget(){		
		String property = (String)properties;
		if(StringUtils.isNotBlank(property)){
			textBox.setText(property);	
			decorator.hide();
			txtDecorator.hide();
		}
		else{
			textBox.setText("");
			decorator.show();
			//setToolTipMessage(toolTipErrorMessage)
		}
	}

	private void setToolTipErrorMessage(){
		String toolTipErrorMessage = null;
		if(decorator.isVisible())
			toolTipErrorMessage = decorator.getDescriptionText();
		
		if(txtDecorator.isVisible())
			toolTipErrorMessage = toolTipErrorMessage + "\n" + txtDecorator.getDescriptionText();
		
		setToolTipMessage(toolTipErrorMessage);
	}
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> property=new LinkedHashMap<>();
		property.put(propertyName, textBox.getText());
		setToolTipErrorMessage();
		
		return property;
	}

	/*@Override
	public void setComponentName(String componentName) {
	//
		
	}*/
}
