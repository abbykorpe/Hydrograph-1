package com.bitwise.app.propertywindow.window;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.graph.model.Component;
import com.bitwise.app.propertywindow.adapters.ELTComponentPropertyAdapter;
import com.bitwise.app.propertywindow.factory.WidgetFactory.Widgets;
import com.bitwise.app.propertywindow.property.ELTComponenetProperties;
import com.bitwise.app.propertywindow.property.IPropertyTreeBuilder;
import com.bitwise.app.propertywindow.property.Property;
import com.bitwise.app.propertywindow.property.PropertyTreeBuilder;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialog;
import com.bitwise.app.propertywindow.testdata.ComponentModel;
import com.bitwise.app.propertywindow.testdata.RawProperties;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Sep 07, 2015
 * 
 */

public class TestWindow extends ApplicationWindow {

	LinkedHashMap<String, Object> inputCompProps;
	
	/**
	 * Create the application window.
	 */
	public TestWindow() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		
		ComponentModel componentModel = new ComponentModel(); 
		inputCompProps = componentModel.getProperties("Input");
		inputCompProps.put("name","cname");
	}

	private Property getComponentBaseTypeProperty(){
		Property property = new Property.Builder("String", "Base Type", Widgets.COMPONENT_BASETYPE_WIDGET.name()).group("GENERAL").subGroup("DISPLAY").build();
		return property;
	}
	
	private Property getComponentTypeProperty(){
		Property property = new Property.Builder("String", "Type", Widgets.COMPONENT_TYPE_WIDGET.name()).group("GENERAL").subGroup("DISPLAY").build();
		return property;
	}
	
	private ArrayList<Property> transformComponentPropertiesToPropertyWindowUnderstantableFormat(
			List<com.bitwise.app.common.component.config.Property> rowProperties) {
		ELTComponentPropertyAdapter eltComponentPropertyAdapter = new ELTComponentPropertyAdapter(rowProperties);
		eltComponentPropertyAdapter.transform();
		ArrayList<Property> componentProperties = eltComponentPropertyAdapter.getProperties();
		return componentProperties;
	}
	
	public LinkedHashMap<String, Object> getProperties() {
		LinkedHashMap<String, Object> properties= new LinkedHashMap<>();
		properties.put("name", "TestName");
		return properties;
	}
	
	private ELTComponenetProperties getELTComponenetProperties(){
		LinkedHashMap<String, Object> componentConfigurationProperties = getProperties();
		LinkedHashMap<String, Object> ComponentMiscellaneousProperties = getComponentMiscellaneousProperties();
		
		ELTComponenetProperties eltComponenetProperties = new ELTComponenetProperties(componentConfigurationProperties, ComponentMiscellaneousProperties);
		return eltComponenetProperties;
	}

	private LinkedHashMap<String, Object> getComponentMiscellaneousProperties() {
		LinkedHashMap<String, Object> ComponentMiscellaneousProperties = new LinkedHashMap<>();
		
		ArrayList<String> names = new ArrayList<>();
		
		names.add("TestName");
		names.add("abc");
		names.add("xyz");
		
		ComponentMiscellaneousProperties.put("componentNames", names);
		ComponentMiscellaneousProperties.put("componentBaseType", "InputFileDelimited");
		ComponentMiscellaneousProperties.put("componentType", "Input");
		return ComponentMiscellaneousProperties;
	}
	
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		
		
		Composite container = new Composite(parent, SWT.NONE);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ELTComponenetProperties eltComponenetProperties=getELTComponenetProperties();
				//PropertyWindow propertyWindow = new PropertyWindow(e.display.getActiveShell());
				//propertyWindow.open();
				
				//CopyOfPropertyWindowPOC2 copyOfPropertyWindowPOC2 = new CopyOfPropertyWindowPOC2(e.display.getActiveShell());
				//copyOfPropertyWindowPOC2.open();
				
				
				
				RawProperties rawProperties = new RawProperties();
								
				List<com.bitwise.app.common.component.config.Property> rowProperties = rawProperties.getRawProperties();		
				
					
					Shell shell = e.display.getActiveShell();
						
					ArrayList<Property> componentProperties = null;
					//try {
						componentProperties = transformComponentPropertiesToPropertyWindowUnderstantableFormat(rowProperties);
					/*} catch (EmptyComponentPropertiesException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					componentProperties.add(getComponentBaseTypeProperty());
					componentProperties.add(getComponentTypeProperty());
					
					IPropertyTreeBuilder propertyTreeBuilder = new PropertyTreeBuilder(componentProperties);

					PropertyDialog propertyDialog = new PropertyDialog(shell, propertyTreeBuilder.getPropertyTree(),
							eltComponenetProperties,null,new Component() {
								
								@Override
								public String getConverter() {
									// TODO Auto-generated method stub
									return null;
								}
							});
					propertyDialog.open();


					
				
				
				//PropertyStore propertyStore = new PropertyStore();
				
				//ArrayList<Property> inputComponentProperties = propertyStore.getProperties("Input");
				
				//IPropertyTreeBuilder propertyTreeBuilder = new PropertyTreeBuilder(inputComponentProperties);
				
				
				
				/*PropertyDialog testDialog = new PropertyDialog(e.display.getActiveShell(),propertyTreeBuilder.getPropertyTree(),inputCompProps,names);
				
				testDialog.open();*/
				
				
			}
			
		});
		btnNewButton.setBounds(0, 0, 75, 25);
		btnNewButton.setText("New Button");

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			TestWindow window = new TestWindow();
			window.setBlockOnOpen(true);
			
			window.open();
			Display.getCurrent().dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
