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
package hydrograph.ui.propertywindow.propertydialog;

import hydrograph.ui.common.cloneableinterface.IDataStructure;

import hydrograph.ui.common.util.ComponentCacheUtil;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.factory.WidgetFactory;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.ELTComponenetProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.utils.WordUtils;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.ELTFilePathWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroup;
import hydrograph.ui.validators.impl.IValidator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.osgi.framework.adaptor.FilePath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;


/**
 * 
 * @author Bitwise
 * Sep 07, 2015
 * 
 */

public class PropertyDialogBuilder {
	//<GroupName,<SubgroupName,[PropertyList...]>>
	private LinkedHashMap<String,LinkedHashMap<String,ArrayList<Property>>> propertyTree;
	private Composite container;
	private ArrayList<AbstractWidget> eltWidgetList;
	private ELTComponenetProperties eltComponenetProperties;
	private PropertyDialogButtonBar propertyDialogButtonBar;	
	private Component component;
	private AbstractWidget schemaWidget;
	private Schema setSchemaForInternalPapogation;
	private List<String> deletedInternalSchema;
	private List<String> operationFieldList;
	private PropertyDialog propertyDialog;
	private Map<String, String> propertyHelpTextMap;
	private final String TYPE = "Type";
	private final String BASE_TYPE = "Base Type";
	private final String TYPE_PROPERTY_HELP="Basic Category";
	private final String BASE_TYPE_PROPERTY_HELP="Abstraction";
	
    private TabFolder tabFolder;
	/**
	 * Instantiates a new property dialog builder.
	 * 
	 * @param container
	 *            the container
	 * @param propertyTree
	 *            the property tree
	 * @param eltComponenetProperties
	 *            the elt componenet properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 * @param component 
	 * @param propertyDialog 
	 */
	public PropertyDialogBuilder(Composite container, LinkedHashMap<String,LinkedHashMap<String,ArrayList<Property>>> propertyTree, 
			ELTComponenetProperties eltComponenetProperties,PropertyDialogButtonBar propertyDialogButtonBar, Component component, PropertyDialog propertyDialog){
		this.container = container;
		this.propertyTree = propertyTree;
		this.eltComponenetProperties = eltComponenetProperties;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		this.component = component;
		eltWidgetList= new ArrayList<>();
		
		this.propertyDialog = propertyDialog;
		propertyHelpTextMap = getPropertyHelpTextMap(component.getComponentName());
		
		initSchemaObject();
	}

	private void initSchemaObject() {
		setSchemaForInternalPapogation = new Schema();
		setSchemaForInternalPapogation.setIsExternal(false);
		List<GridRow> gridRows = new ArrayList<>();
		setSchemaForInternalPapogation.setGridRow(gridRows);
		setSchemaForInternalPapogation.setExternalSchemaPath("");
		operationFieldList = new LinkedList<>();
		deletedInternalSchema= new ArrayList<>();

	}
	
	/**
	 * Builds the property window.
	 */
	public void buildPropertyWindow(){
		tabFolder = addTabFolderToPropertyWindow();
        addTabsInTabFolder(tabFolder);
      
        tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
			
				if(schemaWidget!=null){
					if (schemaWidget instanceof ELTSchemaGridWidget) {
						ELTSchemaGridWidget eltSchemaGridWidget = (ELTSchemaGridWidget) schemaWidget;
						if (!eltSchemaGridWidget.isExternal()) {
							if (schemaWidget.getSchemaForInternalPapogation().getGridRow().size() != 0) {
								eltSchemaGridWidget.enableDisableButtons(schemaWidget.getSchemaForInternalPapogation()
										.getGridRow().size());
							} else {
								eltSchemaGridWidget.enableDisableButtons(eltSchemaGridWidget.getSizeOfTableViewer());
							}
						}
						((ELTSchemaGridWidget) schemaWidget).getListenerHelper(true);

					}
						schemaWidget.refresh(); 
				}
			}
		});
		
        
	}

	private void addTabsInTabFolder(TabFolder tabFolder) {
	
		for(String groupName : propertyTree.keySet()){
			ScrolledCompositeHolder scrolledCompositeHolder = getPropertyWindowTab(groupName,tabFolder);
			LinkedHashMap<String,ArrayList<Property>> subgroupTree = propertyTree.get(groupName);
			addGroupsInTab(scrolledCompositeHolder, subgroupTree);
			  
			addEmptyGroupWidget(scrolledCompositeHolder);
		}
		
			
	}

	private void addEmptyGroupWidget(
			ScrolledCompositeHolder scrolledCompositeHolder) {
		AbstractELTContainerWidget subGroupContainerx=addSubgroupToPropertyWindowTab("",scrolledCompositeHolder);
		ColumnLayout subGroupLayout = getGroupWidgetLayout();
		((Group)subGroupContainerx.getContainerControl()).setLayout(subGroupLayout);
		((Group)subGroupContainerx.getContainerControl()).setVisible(false);
	}

	private ColumnLayout getGroupWidgetLayout() {
		ColumnLayout subGroupLayout = new ColumnLayout();
		subGroupLayout.maxNumColumns = 1;
		subGroupLayout.bottomMargin = 0;
		subGroupLayout.topMargin = 0;
		subGroupLayout.rightMargin = 0;
		return subGroupLayout;
	}

	private void addGroupsInTab(
			ScrolledCompositeHolder scrolledCompositeHolder,
			LinkedHashMap<String, ArrayList<Property>> subgroupTree) {
		for(String subgroupName: subgroupTree.keySet()){
			Property property = subgroupTree.get(subgroupName).get(0);
			AbstractELTContainerWidget subGroupContainer = getGroupWidgetContainer(
					scrolledCompositeHolder, subgroupName, property);			
			addCustomWidgetsToGroupWidget(subgroupTree, subgroupName,
					subGroupContainer);			
		}
	}

	private AbstractELTContainerWidget getGroupWidgetContainer(
			ScrolledCompositeHolder scrolledCompositeHolder,
			String subgroupName, Property property) {
		AbstractELTContainerWidget subGroupContainer;
		if(property != null){
			subGroupContainer=addSubgroupToPropertyWindowTab(property.getPropertySubGroup(),scrolledCompositeHolder);
		
		}else{
			subGroupContainer=addSubgroupToPropertyWindowTab(subgroupName,scrolledCompositeHolder);
		}
		return subGroupContainer;
	}

	private void addCustomWidgetsToGroupWidget(
			LinkedHashMap<String, ArrayList<Property>> subgroupTree,
			String subgroupName, AbstractELTContainerWidget subGroupContainer) {
		boolean isError=false;
		for(final Property property: subgroupTree.get(subgroupName)){
			AbstractWidget eltWidget = addCustomWidgetInGroupWidget(
					subGroupContainer, property);	
			eltWidgetList.add(eltWidget);
			
			if(eltWidget.applyValidationRule())
				isError=true;
		
		}
		
		if(isError)
		{
			for(TabItem item:tabFolder.getItems())
			{
				if(StringUtils.equalsIgnoreCase(item.getText(),subgroupTree.get(subgroupName).get(0).getPropertyGroup()))
						{
					    item.setImage(new Image(null,XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.COMPONENT_ERROR_ICON));
						}			
			}		
		}
		
	}

	
	
	private AbstractWidget addCustomWidgetInGroupWidget(AbstractELTContainerWidget subGroupContainer, final Property property) {
		
		Object object = eltComponenetProperties.getComponentConfigurationProperty(property.getPropertyName());
		if(object != null && IDataStructure.class.isAssignableFrom(object.getClass())){
			object = ((IDataStructure)object).clone();
		}
		ComponentConfigrationProperty componentConfigProp = new ComponentConfigrationProperty(property.getPropertyName(), 
				object);
		
		ComponentMiscellaneousProperties componentMiscellaneousProperties = new ComponentMiscellaneousProperties(
				eltComponenetProperties.getComponentMiscellaneousProperties());

		AbstractWidget widget = WidgetFactory.INSTANCE.getWidget(property.getPropertyRenderer(),componentConfigProp,
				componentMiscellaneousProperties,propertyDialogButtonBar);
		if(property.getPropertyName().equalsIgnoreCase("join_config"));
		widget.setEltComponenetProperties(eltComponenetProperties);
		
		widget.setSchemaForInternalPapogation(setSchemaForInternalPapogation);
		widget.setDeletedInternalSchema(deletedInternalSchema);
		widget.setOperationFieldList(operationFieldList);
        
	    widget.setProperty(property);
		widget.setPropertyDialog(propertyDialog);
		widget.setComponent(component);
		widget.attachToPropertySubGroup(subGroupContainer);
		widget.setTabFolder(tabFolder);
		if(TYPE.equals(componentConfigProp.getPropertyName())){
			widget.setPropertyHelpText(BASE_TYPE_PROPERTY_HELP);
		}else if(BASE_TYPE.equals(componentConfigProp.getPropertyName())){
			widget.setPropertyHelpText(TYPE_PROPERTY_HELP);
		}else{
			if(propertyHelpTextMap.get(componentConfigProp.getPropertyName())!=null)
				widget.setPropertyHelpText(propertyHelpTextMap.get(componentConfigProp.getPropertyName()).replace("\\n", "\n"));
		}
		
		widget.setPropertyHelp();
        
		widget.addModifyListener(property,eltWidgetList);
		
		if(widget instanceof ELTSchemaGridWidget){
			schemaWidget = widget;
		}
		
		return widget;
	}

	/**
	 * Adds the tab folder to property window.
	 * 
	 * @return the tab folder
	 */
	public TabFolder addTabFolderToPropertyWindow(){
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		final ColumnLayoutData cld_tabFolder = new ColumnLayoutData();
		cld_tabFolder.heightHint = 303;
		tabFolder.setLayoutData(cld_tabFolder);
		
		container.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				cld_tabFolder.heightHint = container.getBounds().height - 50;
			}
		});
		
		tabFolder.addListener(SWT.FOCUSED,getMouseClickListener() );
       
		return tabFolder;
	}
	private Listener getMouseClickListener() {
		return new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				for(AbstractWidget abstractWidget: eltWidgetList){
			    	  if(abstractWidget.getFirstTextWidget() != null){
			    		   abstractWidget.getFirstTextWidget().setFocus();
			    	  }
			      }
			}
		};
	}
	/**
	 * Gets the property window tab.
	 * 
	 * @param groupName
	 *            the group name
	 * @param tabFolder
	 *            the tab folder
	 * @return the property window tab
	 */
	public ScrolledCompositeHolder getPropertyWindowTab(String groupName,TabFolder tabFolder){	
		TabItem tabItem = createTab(groupName, tabFolder);
		
		ScrolledComposite scrolledComposite = addScrolledCompositeToTab(tabFolder,tabItem);	
		Composite composite = addCompositeToScrolledComposite(scrolledComposite);
		return new ScrolledCompositeHolder(scrolledComposite,composite);
	}

	private Composite addCompositeToScrolledComposite(ScrolledComposite scrolledComposite) {
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		ColumnLayout cl_composite = new ColumnLayout();
		cl_composite.maxNumColumns = 1;
		cl_composite.bottomMargin = -10;
		
		composite.setLayout(cl_composite);
		
		return composite;
	}

	private ScrolledComposite addScrolledCompositeToTab(TabFolder tabFolder,TabItem tabItem) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder,SWT.V_SCROLL);
		tabItem.setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(false);
		attachMouseScrollButtonListener(scrolledComposite);
		return scrolledComposite;
	}

	private TabItem createTab(String groupName, TabFolder tabFolder) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		if (OSValidator.isMac()) {
			tabItem.setText(" "+WordUtils.capitalize(groupName.replace("_", " ").toLowerCase(), null));
		}
		else {
			tabItem.setText(WordUtils.capitalize(groupName.replace("_", " ").toLowerCase(), null));			
		}
		return tabItem;
	}

	private void attachMouseScrollButtonListener(final ScrolledComposite scrolledComposite){
		scrolledComposite.addListener(SWT.MouseWheel, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int wheelCount = event.count;
				wheelCount = (int) Math.ceil(wheelCount / 3.0f);
				while (wheelCount < 0) {
					scrolledComposite.getVerticalBar().setIncrement(4);
					wheelCount++;
				}

				while (wheelCount > 0) {
					scrolledComposite.getVerticalBar().setIncrement(-4);
					wheelCount--;
				}
			}
		});
	}

	/**
	 * Adds the subgroup to property window tab.
	 * 
	 * @param subgroupName
	 *            the subgroup name
	 * @param scrolledCompositeHolder
	 *            the scrolled composite holder
	 * @return the abstract elt container widget
	 */
	public AbstractELTContainerWidget addSubgroupToPropertyWindowTab(String subgroupName,ScrolledCompositeHolder scrolledCompositeHolder){
		AbstractELTContainerWidget eltDefaultSubgroup= new ELTDefaultSubgroup(scrolledCompositeHolder.getComposite()).subGroupName(WordUtils.capitalize(subgroupName.replace("_", " ").toLowerCase(), null));
		eltDefaultSubgroup.createContainerWidget();		
		if (OSValidator.isMac()) {
			((Group)eltDefaultSubgroup.getContainerControl()).setFont(new Font(null, "Arial", 13,SWT.BOLD));
		}
			scrolledCompositeHolder.getScrolledComposite().setContent(scrolledCompositeHolder.getComposite());
			scrolledCompositeHolder.getScrolledComposite().setMinSize(scrolledCompositeHolder.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			return eltDefaultSubgroup;
	}

	/**
	 * 
	 * Returns list of widgets in property window
	 * 
	 * @return list of {@link AbstractWidget}
	 */
	public ArrayList<AbstractWidget> getELTWidgetList(){
		return eltWidgetList;
	}
	
	/**
	 * 
	 * Returns help text for each property
	 * 
	 * @return
	 */
	private Map<String, String> getPropertyHelpTextMap(String componentName) {
		propertyHelpTextMap = new LinkedHashMap<>();
		 for(hydrograph.ui.common.component.config.Property property : XMLConfigUtil.INSTANCE.getComponent(componentName).getProperty()){
				propertyHelpTextMap.put(property.getName(), property.getPropertyHelpText());
			}
		return propertyHelpTextMap;
	}

}
