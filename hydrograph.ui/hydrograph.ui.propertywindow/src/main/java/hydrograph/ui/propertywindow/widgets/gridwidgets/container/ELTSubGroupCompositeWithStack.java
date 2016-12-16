package hydrograph.ui.propertywindow.widgets.gridwidgets.container;


import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
/**
 * Creates composite having stack layout for widgets
 * @author Bitwise
 *
 */
public class ELTSubGroupCompositeWithStack extends AbstractELTContainerWidget {
	
	Composite composite;

	public ELTSubGroupCompositeWithStack(Composite container) {
		super(container);
	}

	@Override
	public void createContainerWidget() {
		composite = new Composite(inputContainer, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 40;
		composite.setLayout(layout);
		super.outputContainer = composite;		   	
		
	}
	/**
	 * Create composite with stack layout
	 * @param layout
	 */
	public void createStackContainerWidget(StackLayout layout){
		
		composite = new Composite(inputContainer, SWT.NONE);
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
