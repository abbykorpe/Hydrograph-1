package com.bitwise.app.propertywindow.widgets.customwidgets.operational;

import java.util.LinkedHashMap;
import java.util.List;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.datastructure.property.mapping.ErrorObject;
import com.bitwise.app.common.datastructure.property.mapping.InputField;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.MappingDialog;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

/**
 * The Class ELTOperationClassWidget.
 * 
 * @author Bitwise
 */
public class TransformWidget extends AbstractWidget {

	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private ATMapping atMapping;

	// private TransformPropertyGrid transformPropertyGrid;
	// private TransformDialog transformDialog;

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
	public TransformWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);

		this.atMapping = (ATMapping) componentConfigrationProperty.getPropertyValue();
		if (atMapping == null) {
			atMapping = new ATMapping();
		}

		/*
		 * this.transformPropertyGrid = (TransformPropertyGrid) componentConfigrationProperty.getPropertyValue();
		 * if(transformPropertyGrid == null){ transformPropertyGrid = new TransformPropertyGrid(); }
		 */
		this.propertyName = componentConfigrationProperty.getPropertyName();

	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(final AbstractELTContainerWidget container) {

		final ELTDefaultSubgroupComposite transformComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		transformComposite.createContainerWidget();
		OperationClassConfig operationClassConfig = (OperationClassConfig) widgetConfig;
		ELTDefaultLable defaultLable1 = new ELTDefaultLable(operationClassConfig.getComponentDisplayName());
		transformComposite.attachWidget(defaultLable1);

		ELTDefaultButton eltDefaultButton = new ELTDefaultButton("Edit").grabExcessHorizontalSpace(false);
		transformComposite.attachWidget(eltDefaultButton);

		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				/*
				 * transformDialog = new TransformDialog(transformComposite.getContainerControl().getShell(),
				 * propertyDialogButtonBar,transformPropertyGrid,widgetConfig); transformDialog.open();
				 * transformPropertyGrid = transformDialog.getTransformProperty();
				 * propertyDialogButtonBar.enableApplyButton(true);
				 */
				getPropagatedSChema();
				
				MappingDialog mappingDialog = new MappingDialog(transformComposite.getContainerControl().getShell(),
						propertyDialogButtonBar, atMapping, widgetConfig);
				mappingDialog.open();

				atMapping = mappingDialog.getATMapping();
				atMapping.getInputFields().clear();
				super.widgetSelected(e);
			}

		});

	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		// operationClassProperty = eltOperationClassDialog.getOperationClassProperty();
		atMapping.getInputFields().clear();
		property.put(propertyName, atMapping);

		return property;
	}

	private void getPropagatedSChema() {
		ComponentsOutputSchema outputSchema = null;
		InputField inputField = null;
		List<InputField> inputFieldsList = atMapping.getInputFields();
		for (Link link : getComponent().getTargetConnections()) {
			outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			if (outputSchema != null)
				for (FixedWidthGridRow row : outputSchema.getFixedWidthGridRowsOutputFields()) {
					inputField = new InputField(row.getFieldName(), new ErrorObject(false, ""));
					inputFieldsList.add(inputField);
				}
		}
	}
}
