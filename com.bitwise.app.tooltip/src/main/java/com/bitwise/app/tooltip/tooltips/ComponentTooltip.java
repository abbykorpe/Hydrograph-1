package com.bitwise.app.tooltip.tooltips;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.common.datastructure.property.LookupConfigProperty;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
import com.bitwise.app.common.util.WordUtils;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;
import org.apache.commons.lang.StringUtils;
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
		if(propertyInfo.getPropertyValue()!=null)
		{
		if(propertyInfo.getPropertyName().equalsIgnoreCase(Constants.OPERATION_CLASS)){	
			logger.debug("ComponentTooltip.addToolTipContents(): Its Opeartion class");
			addOperationClassPropertyToToolTip(container, propertyInfo);
		}
			else if (propertyInfo.getPropertyName().equalsIgnoreCase(Constants.JOIN_CONFIG)) {
				int joinKeyIndex = 0, recordRequiredIndex = 0;
				if (propertyInfo.getPropertyValue() != null
						&& !((ArrayList<JoinConfigProperty>) propertyInfo.getPropertyValue()).isEmpty()) {
					for (JoinConfigProperty joinConfigProperty : ((ArrayList<JoinConfigProperty>) propertyInfo
							.getPropertyValue())) {
						addJoinKeysInTooltip(container, joinConfigProperty.getJoinKey(), joinKeyIndex);
						joinKeyIndex++;
					}
					for (JoinConfigProperty joinConfigProperty : ((ArrayList<JoinConfigProperty>) propertyInfo
							.getPropertyValue())) {
						String recordRequired = joinConfigProperty.getRecordRequired() == 0 ? Constants.TRUE
								: Constants.FALSE;
						addRecordRequiredInTooltip(container, recordRequired, recordRequiredIndex);
						recordRequiredIndex++;
					}
				}
			} else if (propertyInfo.getPropertyName().equalsIgnoreCase(Constants.HASH_JOIN)) {
				if (propertyInfo.getPropertyValue() != null) {
					LookupConfigProperty lookupConfigProperty = (LookupConfigProperty) propertyInfo.getPropertyValue();
					String lookupPort = lookupConfigProperty.isSelected() ? Constants.IN0 : Constants.IN1;
					addLookupConfigurationDetailsInTooltip(container, lookupConfigProperty.getDriverKey(),
							lookupConfigProperty.getLookupKey(), lookupPort);
				}
			} else if (propertyInfo.getPropertyName().equalsIgnoreCase(Constants.SCHEMA)) {
				if (propertyInfo.getPropertyValue() != null) {
					if(((Schema) propertyInfo.getPropertyValue()).getIsExternal())
					{
					String externalSchemaPath = ((Schema) propertyInfo.getPropertyValue()).getExternalSchemaPath();
					Label externalSchemaPathLabel = setExternalSchemaInTooltip(container, externalSchemaPath);
					showErrorMessageWhenFieldIsEmpty(externalSchemaPath, externalSchemaPathLabel,
							Constants.EXTERNAL_SCHEMA_PATH_ERROR_MESSAGE);
					}
				}
			} else {
				logger.debug("ComponentTooltip.addToolTipContents(): Its other property or with Opeartion class=null");
				addPropertyToToolTip(container, propertyInfo);
			}
		}
	}

	private Label setExternalSchemaInTooltip(final Composite container, String externalSchemaPath) {
		Label externalSchemaPathLabel = new Label(container, SWT.NONE);
		externalSchemaPathLabel.setText(Constants.EXTERNAL_SCHEMA_PATH + externalSchemaPath);
		externalSchemaPathLabel.setBackground(container.getDisplay().getSystemColor(
				SWT.COLOR_INFO_BACKGROUND));
		externalSchemaPathLabel.addListener(SWT.MouseUp, getMouseClickListener(container));
		return externalSchemaPathLabel;
	}
	private void addJoinKeysInTooltip(final Composite container, String joinKey, int index)
	{
		Label joinKeyLabel = new Label(container, SWT.NONE);
		joinKeyLabel.setText(Constants.JOIN_KEY+index+":"+joinKey);
		joinKeyLabel.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		joinKeyLabel.addListener(SWT.MouseUp, getMouseClickListener(container));
		showErrorMessageWhenFieldIsEmpty(joinKey,joinKeyLabel,Constants.JOIN_KEY_ERROR_MESSAGE);
	}
	
	private void addRecordRequiredInTooltip(final Composite container, String recordRequired, int index)
	{
		Label recordRequiredLabel = new Label(container, SWT.NONE);
		recordRequiredLabel.setText(Constants.RECORD_REQUIRED+index+":"+recordRequired);
		recordRequiredLabel.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		recordRequiredLabel.addListener(SWT.MouseUp, getMouseClickListener(container));
	}

	private void addLookupConfigurationDetailsInTooltip(final Composite container, String driverKey, String lookupKey, String lookupPort)
	{
		Label driverKeyLabel = new Label(container, SWT.NONE);
		Label lookupKeyLabel = new Label(container, SWT.NONE);
		Label lookupPortLabel = new Label(container, SWT.NONE);
		driverKeyLabel.setText(Constants.DRIVER_KEY+driverKey);
		lookupKeyLabel.setText(Constants.LOOKUP_KEY+lookupKey);
		lookupPortLabel.setText(Constants.LOOKUP_PORT+lookupPort);
		driverKeyLabel.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		driverKeyLabel.addListener(SWT.MouseUp, getMouseClickListener(container));
		lookupKeyLabel.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		lookupKeyLabel.addListener(SWT.MouseUp, getMouseClickListener(container));
		lookupPortLabel.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		lookupPortLabel.addListener(SWT.MouseUp, getMouseClickListener(container));
		showErrorMessageWhenFieldIsEmpty(driverKey,driverKeyLabel,Constants.DRIVER_KEY_ERROR_MESSAGE);
		showErrorMessageWhenFieldIsEmpty(lookupKey,lookupKeyLabel,Constants.LOOKUP_KEY_ERROR_MESSAGE);
	}
	private void showErrorMessageWhenFieldIsEmpty(String propertyValue,Control label,String errorMessage)
	{
		ControlDecoration decorator = WidgetUtility.addDecorator(label,errorMessage);
		if (StringUtils.isEmpty(propertyValue)) {
			decorator.show();
		} else {
			decorator.hide();
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
				boolean flag = FilterOperationClassUtility.openFileEditor(null,filePath);
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
		if (propertyInfo.getTooltipDataType().equalsIgnoreCase(Constants.LIST_DATA_TYPE)) {
			logger.debug("ComponentTooltip.addPropertyToToolTip() - property type is LIST=");
			String propertyValue = propertyInfo.getPropertyValue().toString()
					.substring(1, propertyInfo.getPropertyValue().toString().length() - 1);
			String formattedPropertyValue = "";
			if (propertyInfo.getPropertyName().equalsIgnoreCase(Constants.SECONDARY_KEYS)
					|| propertyInfo.getPropertyName().equalsIgnoreCase(Constants.PRIMARY_KEYS)) {
				formattedPropertyValue = setFormattingOfSecondaryKeysAndPrimaryKeys(propertyValue,
						formattedPropertyValue);
				lblTextProperty.setText(propertyNameCapitalized + " : " + formattedPropertyValue);
			} else {
				lblTextProperty.setText(propertyNameCapitalized + " : " + propertyValue);
			}
		} else {
			logger.debug("ComponentTooltip.addPropertyToToolTip() - property type is Text/Map=");
			if (propertyInfo.getPropertyName().equalsIgnoreCase(Constants.MATCH_VALUE)) {
				String propertyValue = propertyInfo.getPropertyValue().toString();
				String formattedPropertyValue = propertyValue.substring(propertyValue.lastIndexOf("=") + 1).replace(
						"]", "");
				if (!formattedPropertyValue.trim().equalsIgnoreCase("null")) {
					lblTextProperty.setText(propertyNameCapitalized + " : " + formattedPropertyValue);
				} else {
					lblTextProperty.setText(propertyNameCapitalized + " : " + "First");
				}
			} else if (propertyInfo.getPropertyName().equalsIgnoreCase(Constants.NO_OF_RECORDS)) {
				String formattedPropertyName = propertyNameCapitalized.toLowerCase().substring(0, 1).toUpperCase()
						+ propertyNameCapitalized.toLowerCase().substring(1);
				lblTextProperty.setText(formattedPropertyName + " : " + propertyInfo.getPropertyValue().toString());
			} else {
				lblTextProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue().toString());
			}
		}
	}

	private String setFormattingOfSecondaryKeysAndPrimaryKeys(String propertyValue, String formattedPropertyValue) {
		String[] rowData = propertyValue.split(",");

		for (int i = 0; i < rowData.length; i++) {
			formattedPropertyValue = formattedPropertyValue + rowData[i].replace("=", "(");
			if (!formattedPropertyValue.equalsIgnoreCase("")) {
				formattedPropertyValue = formattedPropertyValue + ")";
				if (i != rowData.length - 1) {
					formattedPropertyValue = formattedPropertyValue + ",";
				}
			}
		}
		return formattedPropertyValue;
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
			if (propertyInfo.getTooltipDataType().equalsIgnoreCase(Constants.LIST)) {
				ArrayList<String> keyFieldList;
				LinkedHashMap<String, Object> secondaryKeysList;
				if (propertyInfo.getPropertyName().equalsIgnoreCase(Constants.KEY_FIELDS)||propertyInfo.getPropertyName().equalsIgnoreCase(Constants.OPERATION_FIELDS)) {
					keyFieldList = (ArrayList<String>) propertyInfo.getPropertyValue();
					if (keyFieldList.size() == 0) {
						lblDecorator.show();
					} else {
						lblDecorator.hide();
					}
				} 
				if(propertyInfo.getPropertyName().equalsIgnoreCase(Constants.SECONDARY_KEYS)||propertyInfo.getPropertyName().equalsIgnoreCase(Constants.PRIMARY_KEYS)) {
					secondaryKeysList = (LinkedHashMap<String, Object>) propertyInfo.getPropertyValue();
					if (secondaryKeysList.size() == 0) {
						lblDecorator.show();
					} else {
						lblDecorator.hide();
					}
				}

			} else {
				if (propertyInfo.getPropertyValue() != null && (!propertyInfo.getPropertyValue().equals(""))) {
					lblDecorator.hide();
				} else {
					lblDecorator.show();
				}
			}
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
