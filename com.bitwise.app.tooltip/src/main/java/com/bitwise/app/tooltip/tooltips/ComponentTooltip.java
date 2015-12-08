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
		logger.debug("ComponentTooltip.ComponentTooltip: Creating tooltip with toolbar: " + this.toolBarManager + " , " + this.componentToolTipInformation.toString());
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
		logger.debug("ComponentTooltip.ComponentTooltip: Creating tooltip with statusbar: " + status + " , " + this.componentToolTipInformation.toString());
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
			logger.debug("ComponentTooltip.hasToolBarManager(): true");
			return true;
		}else{
			logger.debug("ComponentTooltip.hasToolBarManager(): false");
			return false;
		}		
	}
		
	@Override
	protected void createContent(Composite parent) {
		ScrolledComposite scrolledComposite = createScrolledComposite(parent);
		
		final Composite container = createToolTipContainer(parent,
				scrolledComposite);
				
		addToolTipContents(container);
		
		refreshScrolledComposite(scrolledComposite, container);
	}

	private void refreshScrolledComposite(ScrolledComposite scrolledComposite,
			final Composite container) {
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * builds tooltip by adding property information on tooltip container
	 * 
	 * @param container
	 */
	private void addToolTipContents(final Composite container) {
		for(String property: componentToolTipInformation.keySet()){
			PropertyToolTipInformation propertyInfo = componentToolTipInformation.get(property);
			if(propertyInfo.isShowAsTooltip()){
				addPropertyInformationToToolTip(container, propertyInfo);
			}
		}
	}

	/**
	 * add Property Information To ToolTip
	 * @param container
	 * @param propertyInfo
	 */
	private void addPropertyInformationToToolTip(final Composite container,
			PropertyToolTipInformation propertyInfo) {
		if(propertyInfo.getPropertyValue()!=null && propertyInfo.getPropertyName().equalsIgnoreCase("OPERATION_CLASS")){	
			logger.debug("ComponentTooltip.addToolTipContents(): Its Opeartion class");
			addOperationClassPropertyToToolTip(container, propertyInfo);
		}else{
			logger.debug("ComponentTooltip.addToolTipContents(): Its other property or with Opeartion class=null");
			addPropertyToToolTip(container, propertyInfo);
		}
	}

	/**
	 * add operation class property to tooltip container
	 * 
	 * @param container
	 * @param propertyInfo
	 */
	private void addOperationClassPropertyToToolTip(final Composite container,
			PropertyToolTipInformation propertyInfo) {
		String propertyNameCapitalized = getCapitalizedName(propertyInfo);
		
		final String filePath = propertyInfo.getPropertyValue().toString();
		logger.debug("ComponentTooltip.addOperationClassPropertyToToolTip(): Opeartion class filePath=" + filePath);
		
		Link link = createOperationClassLink(container, propertyInfo,
				propertyNameCapitalized, filePath);
				
		showErrors(propertyInfo, link);
	}

	/**
	 * Create operation class link
	 * 
	 * @param container
	 * @param propertyInfo
	 * @param propertyNameCapitalized
	 * @param filePath
	 * @return
	 */
	private Link createOperationClassLink(final Composite container,
			PropertyToolTipInformation propertyInfo,
			String propertyNameCapitalized, final String filePath) {
		Link link = new Link(container, SWT.NONE);
		String tempText= propertyNameCapitalized + " : <a>" + propertyInfo.getPropertyValue().toString() + "</a>";		
		link.setText(tempText);
		link.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		addListenerToOpenOpeartionClassFile(filePath, link);
		
		logger.debug("ComponentTooltip.createOperationClassLink(): created opeartion class link=" + link);
		
		return link;
	}

	/**
	 * Add listener to open operation class file
	 * 
	 * @param filePath
	 * @param link
	 */
	private void addListenerToOpenOpeartionClassFile(final String filePath,
			Link link) {
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.debug("ComponentTooltip.widgetSelected(): Link clicked");
				super.widgetSelected(e);
				boolean flag = FilterOperationClassUtility.openFileEditor(filePath);
				if (!flag) {
					logger.debug("ComponentTooltip.widgetSelected(): Link clicked - error - File " + filePath + " Not Found");
					WidgetUtility.errorMessage("File Not Found"); 
				} else {
					logger.debug("ComponentTooltip.widgetSelected(): Link clicked - hiding tooltip");
					setVisible(false);
				}
			}
		});
		
		logger.debug("ComponentTooltip.addListenerToOpenOpeartionClassFile(): added opeartion class link listener");
	}

	/**
	 * get Capitalized property name
	 * @param propertyInfo
	 * @return
	 */
	private String getCapitalizedName(PropertyToolTipInformation propertyInfo) {
		logger.debug("ComponentTooltip.getCapitalizedName(): propertyInfo: " + propertyInfo.toString());
		
		String propertyName = propertyInfo.getPropertyName();
		String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
				
		logger.debug("ComponentTooltip.getCapitalizedName(): propertyNameCapitalized: " + propertyNameCapitalized);
		
		return propertyNameCapitalized;
	}

	/**
	 * Creates container for tooltip
	 * @param parent
	 * @param scrolledComposite
	 * @return
	 */
	private Composite createToolTipContainer(Composite parent,
			ScrolledComposite scrolledComposite) {
		final Composite container = addComposite(scrolledComposite);
		container.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		addToolTipContainerFocusListener(container);
		
		logger.debug("ComponentTooltip.createToolTipContainer() - created Tooltip container " + container);
		return container;
	}
	
	/**
	 * adds listener to focus container when clicked on container 
	 * @param container
	 */
	private void addToolTipContainerFocusListener(final Composite container) {
		container.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				container.setFocus();
				logger.debug("ComponentTooltip.addToolTipContainerFocusListener() - mouseUp - container focused");
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// Do nothing				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Do nothing				
			}
		});
		
		logger.debug("ComponentTooltip.addToolTipContainerFocusListener() - added container focus listener");
	}

	/**
	 * Add property to tooltip
	 * 
	 * @param container
	 * @param propertyInfo
	 */
	private void addPropertyToToolTip(final Composite container, PropertyToolTipInformation propertyInfo) {
		Label lblTextProperty = new Label(container, SWT.NONE);
		String propertyNameCapitalized = getCapitalizedName(propertyInfo);
		
		logger.debug("ComponentTooltip.addPropertyToToolTip() - propertyInfo=" + propertyInfo.toString());
		
		addText(propertyInfo, lblTextProperty, propertyNameCapitalized);
		
		lblTextProperty.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		lblTextProperty.addListener(SWT.MouseUp, getMouseClickListener(container));
		
		showErrors(propertyInfo, lblTextProperty);
	}

	/**
	 * Add text to tooltip
	 * @param propertyInfo
	 * @param lblTextProperty
	 * @param propertyNameCapitalized
	 */
	private void addText(PropertyToolTipInformation propertyInfo,
			Label lblTextProperty, String propertyNameCapitalized) {
		if(propertyInfo.getPropertyValue() != null){
			addPropertyNameValueText(propertyInfo, lblTextProperty,
					propertyNameCapitalized);
		}else{
			addPropertyWithNoValue(lblTextProperty, propertyNameCapitalized);
		}
	}

	/**
	 * Add text with no value
	 * @param lblTextProperty
	 * @param propertyNameCapitalized
	 */
	private void addPropertyWithNoValue(Label lblTextProperty,
			String propertyNameCapitalized) {
		logger.debug("ComponentTooltip.addPropertyToToolTip() - property with no value");
		lblTextProperty.setText(propertyNameCapitalized + " : ");
	}

	/**
	 * Add text with name:value
	 * 
	 * @param propertyInfo
	 * @param lblTextProperty
	 * @param propertyNameCapitalized
	 */
	private void addPropertyNameValueText(
			PropertyToolTipInformation propertyInfo, Label lblTextProperty,
			String propertyNameCapitalized) {
		if(propertyInfo.getTooltipDataType().equalsIgnoreCase("LIST")){
			logger.debug("ComponentTooltip.addPropertyToToolTip() - property type is LIST=");
			lblTextProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue());
		}else{
			logger.debug("ComponentTooltip.addPropertyToToolTip() - property type is Text/Map=");
			lblTextProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue().toString());
		}
	}

	/**
	 *  add scrolled composite to make tooltip scrollable.
	 * @param scrolledComposite
	 * @return
	 */
	private Composite addComposite(ScrolledComposite scrolledComposite) {
		final Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		return container;
	}

	/**
	 * Create scrolled composite
	 * @param parent
	 * @return
	 */
	private ScrolledComposite createScrolledComposite(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		logger.debug("ComponentTooltip.createScrolledComposite() - created scrolled composite " + scrolledComposite);
		
		return scrolledComposite;
	}

	/**
	 * Show errors balloon in case property is having errors.
	 * 
	 * @param propertyInfo
	 * @param lblLinkProperty
	 */
	private void showErrors(PropertyToolTipInformation propertyInfo,
			Control lblLinkProperty) {
		if(propertyInfo.getErrorMessage()!=null){
			logger.debug("ComponentTooltip.showErrors() - Showing error balloon on property " + propertyInfo.getPropertyName() + 
					", Error: " + propertyInfo.getErrorMessage());
			ControlDecoration lblDecorator = WidgetUtility.addDecorator(lblLinkProperty, propertyInfo.getErrorMessage());
			lblDecorator.show();
		}
	}

	/**
	 * 
	 * Mouse click listener to set focus on tooltip container
	 * 
	 * @param container
	 * @return
	 */
	private Listener getMouseClickListener(final Composite container) {
		return new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				logger.debug("ComponentTooltip.getMouseClickListener() - setting foucs to container");
				container.setFocus();
			}
		};
	}

	/**
	 * returns tooltip container object
	 * @return Composite - tooltip composite
	 */
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
