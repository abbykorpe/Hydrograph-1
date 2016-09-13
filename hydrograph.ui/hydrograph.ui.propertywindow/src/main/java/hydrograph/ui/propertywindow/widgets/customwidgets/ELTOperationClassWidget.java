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

 
package hydrograph.ui.propertywindow.widgets.customwidgets;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.OperationClassProperty;
import hydrograph.ui.expression.editor.launcher.LaunchExpressionEditor;
import hydrograph.ui.expression.editor.util.FieldDataTypeMap;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.ELTOperationClassDialog;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTRadioButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;


/**
 * The Class ELTOperationClassWidget.
 * 
 * @author Bitwise
 */
public class ELTOperationClassWidget extends AbstractWidget {

	private String propertyName;
	private ArrayList<AbstractWidget> widgets;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>(); 
	private OperationClassProperty operationClassProperty;
	private ELTOperationClassDialog eltOperationClassDialog;
	private List<NameValueProperty> nameValuePropertyList;
	private ELTRadioButton operationRadioButton;
	private ELTRadioButton expressionRadioButton;
	
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
		nameValuePropertyList=new ArrayList<>(); 
		this.operationClassProperty = (OperationClassProperty) componentConfigrationProperty.getPropertyValue();
		if(operationClassProperty == null){
			operationClassProperty = new OperationClassProperty(Messages.CUSTOM, "", false, "",nameValuePropertyList,null,null);
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
		runtimeComposite.numberOfBasicWidgets(3);
		operationClassProperty.getExpressionEditorData().setComponentName(getComponent().getComponentName());
		operationRadioButton = new ELTRadioButton(Messages.OPERATION_CALSS_LABEL);
		runtimeComposite.attachWidget(operationRadioButton);
		operationRadioButton.visible(true);
		setPropertyHelpWidget((Control) operationRadioButton.getSWTWidgetControl());
		addSelectionListenerOnOperation();
		
		ELTDefaultLable defaultLable1 = new ELTDefaultLable(""); 
		runtimeComposite.attachWidget(defaultLable1);
		
		ELTDefaultLable defaultLable2 = new ELTDefaultLable(""); 
		runtimeComposite.attachWidget(defaultLable2);
		
		if(OSValidator.isMac()){
			expressionRadioButton = new ELTRadioButton(Messages.MAC_EXPRESSION_EDITIOR_LABEL);
		}else {
		expressionRadioButton = new ELTRadioButton(Messages.WINDOWS_EXPRESSION_EDITIOR_LABEL);
		}
		runtimeComposite.attachWidget(expressionRadioButton);
		expressionRadioButton.visible(true);
		setPropertyHelpWidget((Control) expressionRadioButton.getSWTWidgetControl());
		addSelectionListenerOnExpression();
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(
				Messages.EDIT_BUTTON_LABEL).grabExcessHorizontalSpace(false);
		runtimeComposite.attachWidget(eltDefaultButton);
		
		
		initialize();
		
		setToolTipMessage(Messages.OperationClassBlank);
		((Button)eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button) expressionRadioButton.getSWTWidgetControl()).getSelection()){
					List<FixedWidthGridRow> inputFieldSchema=getInputSchema();
					operationClassProperty.setExpression(true);
					operationClassProperty.getExpressionEditorData().getSelectedInputFieldsForExpression().clear();
					operationClassProperty.getExpressionEditorData().getSelectedInputFieldsForExpression()
							.putAll(FieldDataTypeMap.INSTANCE.createFieldDataTypeMap(null,inputFieldSchema));
					LaunchExpressionEditor launchExpressionEditor=new LaunchExpressionEditor();
					String oldExpression=operationClassProperty.getExpressionEditorData().getExpression();
					launchExpressionEditor.launchExpressionEditor(operationClassProperty.getExpressionEditorData(),inputFieldSchema);
					if(!StringUtils.equals(operationClassProperty.getExpressionEditorData().getExpression(), oldExpression)){
						propertyDialogButtonBar.enableApplyButton(true);
					}
						
				}else{
				OperationClassProperty oldOperationClassProperty=operationClassProperty.clone();
				eltOperationClassDialog = new ELTOperationClassDialog(
						runtimeComposite.getContainerControl().getShell(), propertyDialogButtonBar,
						operationClassProperty, widgetConfig, getComponent().getComponentName());
				eltOperationClassDialog.open();
				operationClassProperty.setComboBoxValue(eltOperationClassDialog.getOperationClassProperty().getComboBoxValue());
				operationClassProperty.setOperationClassPath(eltOperationClassDialog.getOperationClassProperty().getOperationClassPath());
				operationClassProperty.setOperationClassFullPath(eltOperationClassDialog.getOperationClassProperty().getOperationClassFullPath());
				operationClassProperty.setParameter(eltOperationClassDialog.getOperationClassProperty().isParameter());
				if (eltOperationClassDialog.isCancelPressed() && (!(eltOperationClassDialog.isApplyPressed()))) {
					operationClassProperty.setNameValuePropertyList(oldOperationClassProperty.getNameValuePropertyList());
				}
				setToolTipMessage(eltOperationClassDialog.getTootlTipErrorMessage());
				
				if(eltOperationClassDialog.isYesPressed()){
					propertyDialog.pressOK();
				}
				
				if(eltOperationClassDialog.isNoPressed()){
					propertyDialog.pressCancel();
				}
				
				
			}
				showHideErrorSymbol(widgets);
				super.widgetSelected(e);
			}

			private List<FixedWidthGridRow> getInputSchema() {
				List<FixedWidthGridRow> fixedWidthGridRows=new ArrayList<>();
				for(Link link:getComponent().getTargetConnections()){
					ComponentsOutputSchema componentsOutputSchema=SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
					if(componentsOutputSchema!=null && componentsOutputSchema.getFixedWidthGridRowsOutputFields()!=null){
						fixedWidthGridRows = componentsOutputSchema.getFixedWidthGridRowsOutputFields();
					}
					break;
				}
				return fixedWidthGridRows;
			}
			
		});
	
} 
	
	@Override
	public LinkedHashMap<String, Object> getProperties() {		
		property.put(propertyName, operationClassProperty);
		return property;
	}

	@Override
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(operationClassProperty);
	}

	public void initialize()
	{
		if(operationClassProperty.isExpression())
		{
			((Button) expressionRadioButton.getSWTWidgetControl()).setSelection(true);
		}else{
			((Button) operationRadioButton.getSWTWidgetControl()).setSelection(true);
		}
	}
	
	public void addSelectionListenerOnOperation(){
		((Button) operationRadioButton.getSWTWidgetControl()).addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(((Button) operationRadioButton.getSWTWidgetControl()).getSelection()){
					operationClassProperty.setExpression(false);
				}else
					operationClassProperty.setExpression(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/*Do-Nothing*/}
		});
	}
	
	
	public void addSelectionListenerOnExpression(){
		
		((Button) expressionRadioButton.getSWTWidgetControl()).addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(((Button) expressionRadioButton.getSWTWidgetControl()).getSelection()){
					operationClassProperty.setExpression(true);
				}else
					operationClassProperty.setExpression(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/*Do-Nothing*/}
		} );
	}


	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;
	}

}
