package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.LinkedHashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.dialogs.ELTOperationClassDialog;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

// TODO: Auto-generated Javadoc
/**
 * The Class ELTOperationClassWidget.
 * 
 * @author Bitwise
 */
public class ELTOperationClassWidget extends AbstractWidget {

	

	private Text fileName;
	private Object properties;
	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private Button btnCheckButton; 
	private OperationClassProperty operationClassProperty;
	private ELTOperationClassDialog eltOperationClassDialog;

	/**
	 * Instantiates a new ELT operation class widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTOperationClassWidget(
			ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);

		this.operationClassProperty = (OperationClassProperty) componentConfigrationProperty.getPropertyValue();
		if(operationClassProperty == null){
			operationClassProperty = new OperationClassProperty("", false);
		}
		this.propertyName = componentConfigrationProperty.getPropertyName();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {

		final ELTDefaultSubgroupComposite runtimeComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		runtimeComposite.createContainerWidget();
		ELTDefaultLable defaultLable1 = new ELTDefaultLable("Operation\nClass"); 
		runtimeComposite.attachWidget(defaultLable1);
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(
				"Edit").grabExcessHorizontalSpace(false);
		runtimeComposite.attachWidget(eltDefaultButton);

		//populateWidget();
		setToolTipMessage(Messages.OperationClassBlank);
		((Button)eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				eltOperationClassDialog = new ELTOperationClassDialog(runtimeComposite.getContainerControl().getShell(), propertyDialogButtonBar,operationClassProperty.clone());
				eltOperationClassDialog.setValidationStatus(validationStatus);
				eltOperationClassDialog.open();
				if(!eltOperationClassDialog.getOperationClassProperty().equals(operationClassProperty)){
					operationClassProperty = eltOperationClassDialog.getOperationClassProperty();
					propertyDialogButtonBar.enableApplyButton(true);
				}
				setToolTipMessage(eltOperationClassDialog.getTootlTipErrorMessage());
				super.widgetSelected(e);
			}
			
		});
	
} 

	@Override
	public LinkedHashMap<String, Object> getProperties() {		
		//operationClassProperty = eltOperationClassDialog.getOperationClassProperty();
		property.put(propertyName, operationClassProperty);
		
		return property;
	}

}
