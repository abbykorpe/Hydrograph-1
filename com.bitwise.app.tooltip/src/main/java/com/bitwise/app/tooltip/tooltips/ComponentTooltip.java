package com.bitwise.app.tooltip.tooltips;

import java.util.Map;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.WordUtils;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * 
 * Class for component tooltip
 * 
 * @author Bitwise
 *
 */

public class ComponentTooltip extends AbstractInformationControl implements IInformationControlExtension2 {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ComponentTooltip.class);
	
	private ToolBarManager toolBarManager=null;
	private Map<String,PropertyToolTipInformation> componentToolTipInformation;
	private Composite tooltipContainer;
	
	/**
	 * 
	 * create tooltip with toolbar
	 * 
	 * @param parent
	 * @param toolBarManager
	 * @param propertyToolTipInformation
	 */
	public ComponentTooltip(Shell parent, ToolBarManager toolBarManager,Map<String,PropertyToolTipInformation> propertyToolTipInformation) {
		super(parent, toolBarManager);
		this.toolBarManager= getToolBarManager();
		this.componentToolTipInformation = propertyToolTipInformation;		
		create();
	}
	
	/**
	 * 
	 * Create tooltip with status bar
	 * 
	 * @param parent - shell
	 * @param status - status message
	 * @param propertyToolTipInformation - Information to be display in tooltip
	 */
	public ComponentTooltip(Shell parent, String status,Map<String,PropertyToolTipInformation> propertyToolTipInformation) {
		super(parent, status);
		this.componentToolTipInformation = propertyToolTipInformation;
		create();
	}
	
	/**
	 * 
	 * Returns true if it is has toolbar, false otherwise
	 * 
	 * @return true/false
	 */
	public boolean hasToolBarManager(){
		if(toolBarManager != null ){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	protected void createContent(Composite parent) {
	
		ScrolledComposite scrolledComposite = createScrolledComposite(parent);
		
		final Composite container = addComposite(scrolledComposite);
		
		for(String property: componentToolTipInformation.keySet()){
			PropertyToolTipInformation propertyInfo = componentToolTipInformation.get(property);
			if(propertyInfo.isShowAsTooltip()){
				if(propertyInfo.getPropertyValue()!=null && propertyInfo.getPropertyName().equalsIgnoreCase("OPERATION_CLASS")){							
					Link link = new Link(container, SWT.NONE);
					String propertyName = propertyInfo.getPropertyName();
					String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
					
					String tempText= propertyNameCapitalized + " : <a>" + propertyInfo.getPropertyValue().toString() + "</a>";
					final String filePath = propertyInfo.getPropertyValue().toString();
					link.setText(tempText);
					link.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
					
					link.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							super.widgetSelected(e);
							boolean flag = FilterOperationClassUtility.openFileEditor(filePath);
							if (!flag) {
								WidgetUtility.errorMessage("File Not Found"); 
							} else {
								setVisible(false);
							}
						}
					});
					showErrors(propertyInfo, link);
				}else{
					addPropertyToToolTip(parent, container, propertyInfo);
				}
			}
		}
		
		container.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		container.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				container.setFocus();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//container.setFocus();
		
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void addPropertyToToolTip(Composite parent,
			final Composite container, PropertyToolTipInformation propertyInfo) {
		Label lblTextProperty = new Label(container, SWT.NONE);
		String propertyName = propertyInfo.getPropertyName();
		String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
		if(propertyInfo.getPropertyValue() != null){
			if(propertyInfo.getTooltipDataType().equalsIgnoreCase("LIST")){
				lblTextProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue());
			}else{
				lblTextProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue().toString());
			}
				
		}else{
			lblTextProperty.setText(propertyNameCapitalized + " : ");
		}
		lblTextProperty.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		lblTextProperty.addListener(SWT.MouseUp, getMouseClickListener(container));						
		showErrors(propertyInfo, lblTextProperty);
	}

	private Composite addComposite(ScrolledComposite scrolledComposite) {
		final Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		return container;
	}

	private ScrolledComposite createScrolledComposite(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		return scrolledComposite;
	}

	private void showErrors(PropertyToolTipInformation propertyInfo,
			Control lblLinkProperty) {
		if(propertyInfo.getErrorMessage()!=null){
			ControlDecoration lblDecorator = WidgetUtility.addDecorator(lblLinkProperty, propertyInfo.getErrorMessage());
			lblDecorator.show();
		}
	}

	private Listener getMouseClickListener(final Composite container) {
		return new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				container.setFocus();
			}
		};
	}

	
	public Composite getTooltipContainer(){
		return tooltipContainer;
	}
	
	@Override
	public void setInput(Object input) {
		// Do nothing
	}

	
	@Override
	public boolean hasContents() {
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		Shell shell = getShell();
		if (shell.isVisible() == visible) {
			return;
		}

		if (!visible) {
			super.setVisible(false);
			setInformation(null);
			return;
		}
		
		super.setVisible(true);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
	}

	@Override
	protected void handleDispose() {
		super.handleDispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
