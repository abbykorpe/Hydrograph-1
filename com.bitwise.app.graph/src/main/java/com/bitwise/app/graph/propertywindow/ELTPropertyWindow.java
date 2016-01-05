package com.bitwise.app.graph.propertywindow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwise.app.propertywindow.adapters.ELTComponentPropertyAdapter;
import com.bitwise.app.propertywindow.factory.WidgetFactory.Widgets;
import com.bitwise.app.propertywindow.property.ELTComponenetProperties;
import com.bitwise.app.propertywindow.property.IPropertyTreeBuilder;
import com.bitwise.app.propertywindow.property.Property;
import com.bitwise.app.propertywindow.property.PropertyTreeBuilder;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialog;

/**
 * 
 * @author Bitwise
 * Sep 25, 2015
 * 
 */

public class ELTPropertyWindow implements IELTPropertyWindow{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTPropertyWindow.class);
	
	Object componenetModel;
	ELTComponenetProperties eltComponenetProperties;
	Component component;
	private boolean propertyChanged = false;
	private Map<String, String> toolTipErrorMessages;

	
	/**
	 * Instantiates a new ELT property window.
	 * 
	 * @param componenetModel
	 *            the componenet model
	 */
	public ELTPropertyWindow(Object componenetModel){
		this.componenetModel = componenetModel;
		component = getCastedModel();
		eltComponenetProperties = getELTComponenetProperties();
		toolTipErrorMessages = component.getToolTipErrorMessages();
	}

	private Component getCastedModel() {
		return (Component) componenetModel;
	}
	
	private ELTComponenetProperties getELTComponenetProperties(){
		LinkedHashMap<String, Object> componentConfigurationProperties = component.getProperties();
		LinkedHashMap<String, Object> ComponentMiscellaneousProperties = getComponentMiscellaneousProperties();
		
		ELTComponenetProperties eltComponenetProperties = new ELTComponenetProperties(componentConfigurationProperties, ComponentMiscellaneousProperties);
		return eltComponenetProperties;
	}

	private LinkedHashMap<String, Object> getComponentMiscellaneousProperties() {
		LinkedHashMap<String, Object> ComponentMiscellaneousProperties = new LinkedHashMap<>();
		
		ComponentMiscellaneousProperties.put("componentNames", component.getParent().getComponentNames());
		ComponentMiscellaneousProperties.put("componentBaseType", component.getCategory());
		ComponentMiscellaneousProperties.put("componentType", component.getType());
		return ComponentMiscellaneousProperties;
	}
	
	private Property getComponentBaseTypeProperty(){
		Property property = new Property.Builder("String", "Base Type", Widgets.COMPONENT_BASETYPE_WIDGET.name()).group("GENERAL").subGroup("DISPLAY").build();
		return property;
	}
	
	private Property getComponentTypeProperty(){
		Property property = new Property.Builder("String", "Type", Widgets.COMPONENT_TYPE_WIDGET.name()).group("GENERAL").subGroup("DISPLAY").build();
		return property;
	}
	
	//@Override
	@Override
	public void open() {
		List<com.bitwise.app.common.component.config.Property> rowProperties = getComponentPropertiesFromComponentXML();		
		try {			
			Shell shell = getParentShellForPropertyWindow();
				
			ArrayList<Property> componentProperties = transformToPropertyWindowFormat(rowProperties);
			componentProperties.add(getComponentBaseTypeProperty());
			componentProperties.add(getComponentTypeProperty());
			
			IPropertyTreeBuilder propertyTreeBuilder = new PropertyTreeBuilder(componentProperties);

			PropertyDialog propertyDialog = new PropertyDialog(shell, propertyTreeBuilder.getPropertyTree(),
					eltComponenetProperties,toolTipErrorMessages,component);
			propertyDialog.open();

			//component.setSize(getNewComponentSize());
			
			propertyChanged = propertyDialog.isPropertyChanged();
			
		} catch (ELTComponentPropertyAdapter.EmptyComponentPropertiesException e) {
			logger.error("Failed in transforming properties", e);
		}
	}

	public boolean isPropertyChanged(){
			return propertyChanged;
	}
	
	private Shell getParentShellForPropertyWindow() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display.getActiveShell(), SWT.WRAP | SWT.APPLICATION_MODAL);
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);
		return shell;
	}

	private ArrayList<Property> transformToPropertyWindowFormat(
			List<com.bitwise.app.common.component.config.Property> rowProperties) throws ELTComponentPropertyAdapter.EmptyComponentPropertiesException {
		ELTComponentPropertyAdapter eltComponentPropertyAdapter = new ELTComponentPropertyAdapter(rowProperties);
		eltComponentPropertyAdapter.transform();
		ArrayList<Property> componentProperties = eltComponentPropertyAdapter.getProperties();
		return componentProperties;
	}

	private List<com.bitwise.app.common.component.config.Property> getComponentPropertiesFromComponentXML() {
		return XMLConfigUtil.INSTANCE.getComponent(DynamicClassProcessor.INSTANCE.getClazzName(componenetModel.getClass())).getProperty();
	}
}
