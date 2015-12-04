package com.bitwise.app.tooltip.window;

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

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
import com.bitwise.app.common.util.WordUtils;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class ComponentTooltip extends AbstractInformationControl implements IInformationControlExtension2 {
	//Text text;
	private ToolBarManager toolBarManager=null;
	//private PropertyToolTipInformation propertyToolTipInformation;
	private Map<String,PropertyToolTipInformation> componentToolTipInformation;
	private Composite tooltipContainer;
	private String toolTipData;
	
	public ComponentTooltip(Shell parent, ToolBarManager toolBarManager,Map<String,PropertyToolTipInformation> propertyToolTipInformation) {
		super(parent, toolBarManager);
		this.toolBarManager= getToolBarManager();
		this.componentToolTipInformation = propertyToolTipInformation;		
		create();
	}
	
	public ComponentTooltip(Shell parent, String status,Map<String,PropertyToolTipInformation> propertyToolTipInformation) {
		super(parent, status);
		this.componentToolTipInformation = propertyToolTipInformation;
		create();
	}
	
	
	public boolean hasToolBarManager(){
		if(toolBarManager != null ){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void setLocation(Point location) {
		// TODO Auto-generated method stub
		super.setLocation(location);
	}
	
	
	@Override
	protected void createContent(Composite parent) {
	
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		final Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		
		for(String property: componentToolTipInformation.keySet()){
			PropertyToolTipInformation propertyInfo = componentToolTipInformation.get(property);
			if(propertyInfo.isShowAsTooltip()){
				if(propertyInfo.getTooltipDataType().equalsIgnoreCase("TEXT")){
					if(propertyInfo.getPropertyValue() != null){						
						Label lblTextProperty = new Label(container, SWT.NONE);
						String propertyName = propertyInfo.getPropertyName();
						//String propertyNameCapitalized = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
						String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
						lblTextProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue());
						lblTextProperty.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						lblTextProperty.addListener(SWT.MouseUp, getMouseClickListener(container));
						
						showErrors(propertyInfo, lblTextProperty);
					}else{
						Label lblTextProperty = new Label(container, SWT.NONE);
						String propertyName = propertyInfo.getPropertyName();
						//String propertyNameCapitalized = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
						String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
						lblTextProperty.setText(propertyNameCapitalized + " : ");
						lblTextProperty.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						lblTextProperty.addListener(SWT.MouseUp, getMouseClickListener(container));
						
						showErrors(propertyInfo, lblTextProperty);
					}
				}else if(propertyInfo.getTooltipDataType().equalsIgnoreCase("LINK")){
					if(propertyInfo.getPropertyValue() != null){
						if(propertyInfo.getPropertyName().equalsIgnoreCase("OPERATION_CLASS")){							
							Link link = new Link(container, SWT.NONE);
							String propertyName = propertyInfo.getPropertyName();
							//String propertyNameCapitalized = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
							String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
							
							String tempText= propertyNameCapitalized + " : <a>" + propertyInfo.getPropertyValue().toString() + "</a>";
							final OperationClassProperty filePath = (OperationClassProperty) propertyInfo.getPropertyValue();
							link.setText(tempText);
							link.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
							
							link.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									// TODO Auto-generated method stub
									super.widgetSelected(e);
									boolean flag = FilterOperationClassUtility.openFileEditor(null,filePath.getOperationClassFullPath());
									if (!flag) {
										WidgetUtility.errorMessage("File Not Found"); 
									} else {
										setVisible(false);
										//shell.close(); 
									}
								}
							});
							showErrors(propertyInfo, link);
						}else{
							Label lblLinkProperty = new Label(container, SWT.NONE);
							String propertyName = propertyInfo.getPropertyName();
							//String propertyNameCapitalized = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
							String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
							
							lblLinkProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue());
							lblLinkProperty.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
							lblLinkProperty.addListener(SWT.MouseUp, getMouseClickListener(container));
							
							showErrors(propertyInfo, lblLinkProperty);
								
							
						}	
					}else{
							Label lblLinkProperty = new Label(container, SWT.NONE);
							String propertyName = propertyInfo.getPropertyName();
							//String propertyNameCapitalized = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
							String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
							
							lblLinkProperty.setText(propertyNameCapitalized + " : ");
							lblLinkProperty.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
							lblLinkProperty.addListener(SWT.MouseUp, getMouseClickListener(container));
							showErrors(propertyInfo, lblLinkProperty);
					}
				}else if(propertyInfo.getTooltipDataType().equalsIgnoreCase("LIST")){
					if(propertyInfo.getPropertyValue() != null){						
						Label lblTextProperty = new Label(container, SWT.NONE);
						String propertyName = propertyInfo.getPropertyName();
						//String propertyNameCapitalized = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
						String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
						
						lblTextProperty.setText(propertyNameCapitalized + " : " + propertyInfo.getPropertyValue().toString());
						lblTextProperty.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						lblTextProperty.addListener(SWT.MouseUp, getMouseClickListener(container));
						showErrors(propertyInfo, lblTextProperty);
					}else{
						Label lblTextProperty = new Label(container, SWT.NONE);
						String propertyName = propertyInfo.getPropertyName();
						//String propertyNameCapitalized = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
						String propertyNameCapitalized = WordUtils.capitalize(propertyName.toLowerCase(), '_').replace("_", " ");
						
						lblTextProperty.setText(propertyNameCapitalized + " : ");
						lblTextProperty.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						lblTextProperty.addListener(SWT.MouseUp, getMouseClickListener(container));
						showErrors(propertyInfo, lblTextProperty);
					}
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

	private void showErrors(PropertyToolTipInformation propertyInfo,
			Control lblLinkProperty) {
		if(propertyInfo.getErrorMessage()!=null){
			//ControlDecoration lblDecorator = new ControlDecoration(lblLinkProperty, SWT.TOP|SWT.LEFT);
			ControlDecoration lblDecorator = WidgetUtility.addDecorator(lblLinkProperty, propertyInfo.getErrorMessage());
			lblDecorator.show();
		}
	}

	private Listener getMouseClickListener(final Composite container) {
		return new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				container.setFocus();
			}
		};
	}

	
	public Composite getTooltipContainer(){
		return tooltipContainer;
	}
	
	@Override
	public void setInput(Object input) {
		//text.setText(input.toString());
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
		//text.dispose();
		//tooltipContainer.dispose();
		super.handleDispose();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
}
