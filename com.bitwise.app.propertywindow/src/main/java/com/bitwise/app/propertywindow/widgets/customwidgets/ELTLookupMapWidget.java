package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.lookupproperty.ELTLookupMapWizard;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

public class ELTLookupMapWidget extends AbstractWidget {

	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private ELTLookupMapWizard lookupMapWizard;
	private LookupMappingGrid lookupMappingGrid;

	public ELTLookupMapWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propertyDialogButtonBar);
		if (componentConfigProp.getPropertyValue() == null) {
			lookupMappingGrid = new LookupMappingGrid();
		} else {
			lookupMappingGrid = (LookupMappingGrid) componentConfigProp.getPropertyValue();
		}
		this.propertyName = componentConfigProp.getPropertyName();
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				subGroup.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Lookup\n Mapping");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getPropagatedSchema();
				lookupMapWizard = new ELTLookupMapWizard(((Button) eltDefaultButton.getSWTWidgetControl()).getShell(),
						lookupMappingGrid);
				lookupMapWizard.open();
				lookupMapWizard.getLookupPropertyGrid();
				propertyDialogButtonBar.enableApplyButton(true);

			}
		});
	}

	private void getPropagatedSchema() {
		List<List<FilterProperties>> sorceFieldList = SchemaPropagationHelper.INSTANCE
				.sortedFiledNamesBySocketId(getComponent());
		if (sorceFieldList != null)
			lookupMappingGrid.setLookupInputProperties(sorceFieldList);
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, lookupMappingGrid);
		return property;
	}

}
