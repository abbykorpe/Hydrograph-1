package com.bitwise.app.propertywindow.widgets.gridwidgets.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;

public class ELTSchemaSubgroupComposite extends AbstractELTContainerWidget{
	
	Composite composite;

	public ELTSchemaSubgroupComposite(Composite container) {
		super(container);
	}

	@Override
	public void createContainerWidget() {
		composite = new Composite(inputContainer, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		layout.horizontalSpacing = -45;
		layout.marginLeft = 350;
		composite.setLayout(layout);
		super.outputContainer = composite;	
	}

	@Override
	public AbstractELTContainerWidget numberOfBasicWidgets(int subWidgetCount) {
		composite.setLayout(new GridLayout(subWidgetCount, false));
		return this;
	}

	@Override
	public void attachWidget(AbstractELTWidget eltWidget) {
		eltWidget.attachWidget(composite);
		
	}

}
