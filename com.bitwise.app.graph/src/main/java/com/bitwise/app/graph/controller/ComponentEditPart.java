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

 
package com.bitwise.app.graph.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.Policy;
import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.component.config.Property;
import com.bitwise.app.common.datastructure.property.LookupConfigProperty;
import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.figure.ComponentBorder;
import com.bitwise.app.graph.figure.ComponentFigure;
import com.bitwise.app.graph.figure.ELTFigureConstants;
import com.bitwise.app.graph.figure.PortFigure;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.ComponentLabel;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwise.app.graph.propertywindow.ELTPropertyWindow;
import com.bitwise.app.graph.utility.SubGraphUtility;
import com.bitwise.app.logging.factory.LogFactory;

/**
 * The Class ComponentEditPart.
 * @author Bitwise
 */
public class ComponentEditPart extends AbstractGraphicalEditPart implements NodeEditPart, PropertyChangeListener {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ComponentEditPart.class);
	

	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((Component) getModel()).addPropertyChangeListener(this);
			((Component) getModel()).setComponentEditPart(this);
		}
	}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((Component) getModel()).removePropertyChangeListener(this);
		}
	}

	@Override
	protected void createEditPolicies() {
		String componentName = DynamicClassProcessor.INSTANCE.getClazzName(getModel().getClass());
		try {
			for (com.bitwise.app.common.component.config.Component component : XMLConfigUtil.INSTANCE.getComponentConfig()) {
				if (component.getName().equalsIgnoreCase(componentName)) {
					applyGeneralPolicy(component);
				}
			}
		} catch (Exception e) {
			logger.error("Error while creating edit policies", e);
		} 
	}

	/**
	 * Apply general policy.
	 * 
	 * @param component
	 *            the component
	 * @throws Exception
	 *             the exception
	 */
	public void applyGeneralPolicy(
			com.bitwise.app.common.component.config.Component component)
			throws Exception {

		for (Policy generalPolicy : XMLConfigUtil.INSTANCE.getPoliciesForComponent(component)) {
			try {
				AbstractEditPolicy editPolicy = (AbstractEditPolicy) Class.forName(generalPolicy.getValue()).newInstance();
				installEditPolicy(generalPolicy.getName(), editPolicy);
			} catch (Exception exception) {
				logger.error("Failed to apply policies", exception);
				throw exception;
			}
		}
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = createFigureForModel();
		figure.setOpaque(true); // non-transparent figure
		LinkedHashMap<String, Object> properties = getCastedModel().getProperties();
		if (StringUtils.equals(getCastedModel().getComponentName(), Constants.SUBGRAPH_COMPONENT)) {
			SubGraphUtility graphUtility=new SubGraphUtility();
				graphUtility.updateVersionOfSubgraph(getCastedModel());
		}
		String status = (String) properties.get(Component.Props.VALIDITY_STATUS.getValue());
		((ComponentFigure)figure).setStatus(status);
		return figure;
	}

	private IFigure createFigureForModel() {
		String componentName = DynamicClassProcessor.INSTANCE
				.getClazzName(getModel().getClass());
		
		String canvasIconPath = XMLConfigUtil.INSTANCE.getComponent(componentName).getCanvasIconPath();
		List<PortSpecification> portSpecification = XMLConfigUtil.INSTANCE.getComponent(componentName).getPort().getPortSpecification();
		
		String label = (String) getCastedModel().getPropertyValue(Component.Props.NAME_PROP.getValue());

		return new ComponentFigure(portSpecification, canvasIconPath, label);
	}

	public Component getCastedModel() {
		return (Component) getModel();
	}

	@Override
	protected List getModelChildren() {
		// return a list of models
		return ((Component)getModel()).getChildren();
	}
	
	/**
	 * Map connection anchor to terminal.
	 * 
	 * @param c
	 *            the c
	 * @return the string
	 */
	public final String mapConnectionAnchorToTerminal(ConnectionAnchor c) {

		return getComponentFigure().getConnectionAnchorName(c);
	}

	
	@Override
	protected List<Link> getModelSourceConnections() {
		return getCastedModel().getSourceConnections();
	}

	@Override
	protected List<Link> getModelTargetConnections() {
		return getCastedModel().getTargetConnections();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {

		Link wire = (Link) connection.getModel();
		return getComponentFigure().getConnectionAnchor(
				wire.getSourceTerminal());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return getComponentFigure().getSourceConnectionAnchorAt(pt);
		
	}

	protected ComponentFigure getComponentFigure() {
		return (ComponentFigure) getFigure();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {

		Link wire = (Link) connection.getModel();
		return getComponentFigure().getConnectionAnchor(
				wire.getTargetTerminal());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return getComponentFigure().getTargetConnectionAnchorAt(pt);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		String prop = evt.getPropertyName();
		if (Component.Props.SIZE_PROP.eq(prop)
				|| Component.Props.LOCATION_PROP.eq(prop)) {
			refreshVisuals();
		} else if (Component.Props.OUTPUTS.eq(prop)) {
			refreshSourceConnections();
		} else if (Component.Props.INPUTS.eq(prop)) {
			refreshTargetConnections();
		}
	}

	@Override
	protected void refreshVisuals() {
		// notify parent container of changed position & location
		// if this line is removed, the XYLayoutManager used by the parent
		// container
		// (the Figure of the ShapesDiagramEditPart), will not know the bounds
		// of this figure and will not draw it correctly.
		/*
		 * Component component = (Component) getModel();
		 * 
		 * Rectangle bounds = new Rectangle(component.getLocation(),
		 * component.getSize()); ((ContainerEditPart)
		 * getParent()).setLayoutConstraint(this, getFigure(), bounds);
		 */
		
		Component component = getCastedModel();
		ComponentFigure componentFigure = getComponentFigure();
				
		ComponentBorder componentBorder = new ComponentBorder(componentFigure.getBorderColor(), 0, componentFigure.getComponentLabelMargin());
		componentFigure.setBorder(componentBorder);
		List<AbstractGraphicalEditPart> childrenEditParts = getChildren();
		if (!childrenEditParts.isEmpty()){
			ComponentLabelEditPart lLabelEditPart = null;
			PortEditPart portEditPart = null;
			for(AbstractGraphicalEditPart part:childrenEditParts)
			{
				if(part instanceof ComponentLabelEditPart){
					lLabelEditPart = (ComponentLabelEditPart) part;
					lLabelEditPart.refreshVisuals();
				}
				
				if(part instanceof PortEditPart){
					portEditPart = (PortEditPart) part;
					portEditPart.refreshVisuals();
				}
			}
			
		}
		
		Rectangle bounds = new Rectangle(getCastedModel().getLocation(),
				getCastedModel().getSize());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
		
		
		if(component.getTooltipInformation() == null){
			addTooltipInfoToComponent();
		}
		
		component.updateTooltipInformation();
		componentFigure.setPropertyToolTipInformation(component.getTooltipInformation());
	}

	private void addTooltipInfoToComponent() {
		String componentName = DynamicClassProcessor.INSTANCE.getClazzName(getModel().getClass());
		com.bitwise.app.common.component.config.Component components = XMLConfigUtil.INSTANCE.getComponent(componentName);
		//attach tooltip information to component
		Map<String,PropertyToolTipInformation> tooltipInformation = new LinkedHashMap<>();
		for(Property property : components.getProperty()){
			tooltipInformation.put(property.getName(),new PropertyToolTipInformation(property.getName(), property.getShowAsTooltip().value(), property.getTooltipDataType().value()));
		}
		getCastedModel().setTooltipInformation(tooltipInformation);
	}

	@Override
	public void performRequest(Request req) {
		
		// Opens Property Window only on Double click.
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			String currentStatus=(String) getCastedModel().getProperties().get(Component.Props.VALIDITY_STATUS.getValue());
			ComponentFigure componentFigure=((ComponentEditPart)this).getComponentFigure();
			componentFigure.terminateToolTipTimer();
			ELTPropertyWindow eltPropertyWindow = new ELTPropertyWindow(getModel());
			eltPropertyWindow.open();
			
			logger.debug("Updated dimentions: " + getCastedModel().getSize().height + ":"
							+ getCastedModel().getSize().width);
			if(eltPropertyWindow.isPropertyChanged() && Constants.SUBGRAPH_COMPONENT.equalsIgnoreCase(getCastedModel().getComponentName())){
				SubGraphUtility subGraphUtility=new SubGraphUtility();
				subGraphUtility.updateSubgraphProperty((ComponentEditPart)this,null,null);
			} 
			if(eltPropertyWindow.isPropertyChanged())
			{updateSubgraphVersion();}
			adjustComponentFigure(getCastedModel(), getComponentFigure());
			getCastedModel().setComponentLabel((String) getCastedModel().getPropertyValue(Component.Props.NAME_PROP.getValue()));
			
			changePortSettings();
			

			if(getCastedModel().getComponentName().equalsIgnoreCase("lookup")){
				LookupConfigProperty lookup_in0 = (LookupConfigProperty)getCastedModel().getProperties().get("hash_join");
				if(lookup_in0 != null && !lookup_in0.isSelected()){
					getCastedModel().getPorts().get("in1").setLabelOfPort("lkp");
					getCastedModel().getPorts().get("in0").setLabelOfPort("drv");
				}else{
					getCastedModel().getPorts().get("in1").setLabelOfPort("drv");
					getCastedModel().getPorts().get("in0").setLabelOfPort("lkp");
				}
			}
			if(!StringUtils.equals(Constants.UPDATE_AVAILABLE,currentStatus))
			updateComponentStatus();			
			refresh();
			
			adjustExistingPorts();

			selectPorts();
			
			ELTGraphicalEditor eltGraphicalEditor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if(eltPropertyWindow.isPropertyChanged()){
				eltGraphicalEditor.setDirty(true);
				getCastedModel().updateTooltipInformation();
			}
			refreshComponentStatusOfAllComponent();
			super.performRequest(req);
		}
	}

	private void refreshComponentStatusOfAllComponent() {
		for(Component component:getCastedModel().getParent().getChildren()){
			((ComponentEditPart)component.getComponentEditPart()).updateComponentStatus();
		}
	}

	private void updateSubgraphVersion() {
		Component currentComponent=getCastedModel();
		if(currentComponent!=null && currentComponent.getParent().isCurrentGraphIsSubgraph()){
			currentComponent.getParent().updateSubgraphVersion();
		}
	}

	public void changePortSettings() {
		if(getCastedModel().isChangeInPortsCntDynamically()){
			setInPortsCountDynamically();
		}
		if(getCastedModel().isChangeOutPortsCntDynamically()){
			setOutPortsCountDynamically();
		}
		if(getCastedModel().isChangeUnusedPortsCntDynamically()){
			setUnusedPortsCountDynamically();
		}
		refresh();
		
		adjustExistingPorts();
	}


	private void setInPortsCountDynamically(){
		int prevInPortCount = getCastedModel().getInPortCount();
		int outPortCount = 0, newInPortCount = 0;
		if (getCastedModel().getProperties().get("inPortCount") != null) {
			newInPortCount = Integer.parseInt((String) getCastedModel().getProperties().get("inPortCount"));
		}
		if (getCastedModel().getProperties().get("outPortCount") != null) {
			outPortCount = Integer.parseInt((String) getCastedModel().getProperties().get("outPortCount"));
		}
		int inPortCountToBeApplied = newInPortCount!=prevInPortCount ? newInPortCount : prevInPortCount;

		ComponentFigure compFig = (ComponentFigure)getFigure();
		compFig.setHeight(inPortCountToBeApplied, outPortCount);

		Dimension newSize = new Dimension(compFig.getWidth(), compFig.getHeight()+getCastedModel().getComponentLabelMargin());
		getCastedModel().setSize(newSize);
		refresh();

		if(prevInPortCount != newInPortCount){

			if(prevInPortCount < newInPortCount){
				//Increment the ports
				getCastedModel().changeInPortCount(newInPortCount);
				adjustExistingPorts();
				getCastedModel().incrementInPorts(newInPortCount, prevInPortCount);

			}else if(prevInPortCount > newInPortCount){
				//decrement the ports
				List<String> portsToBeRemoved = populateInPortsToBeRemoved(prevInPortCount, newInPortCount);
				getCastedModel().decrementPorts(portsToBeRemoved);
				compFig.decrementAnchors(portsToBeRemoved);
				getCastedModel().changeInPortCount(newInPortCount);

			}
		}	
	}
	

	private void setOutPortsCountDynamically(){
		int prevOutPortCount = getCastedModel().getOutPortCount();
		int inPortCount = 0, newOutPortCount = 0;

		if (getCastedModel().getProperties().get("inPortCount") != null)
		{
			inPortCount = Integer.parseInt((String) getCastedModel().getProperties().get("inPortCount"));
		}

		if (getCastedModel().getProperties().get("outPortCount") != null)
		{
			newOutPortCount = Integer.parseInt((String) getCastedModel().getProperties().get("outPortCount"));
		}
		if(prevOutPortCount != newOutPortCount){
		int outPortCountToBeApplied = newOutPortCount!=prevOutPortCount ? newOutPortCount : prevOutPortCount;

		ComponentFigure compFig = (ComponentFigure)getFigure();
		compFig.setHeight(inPortCount, outPortCountToBeApplied);

		Dimension newSize = new Dimension(compFig.getWidth(), compFig.getHeight()+getCastedModel().getComponentLabelMargin());
		getCastedModel().setSize(newSize);
		refresh();
		
		

			if(prevOutPortCount < newOutPortCount){
				//Increment the ports
				getCastedModel().changeOutPortCount(newOutPortCount);
				adjustExistingPorts();
				getCastedModel().incrementOutPorts(newOutPortCount, prevOutPortCount);

			}else if(prevOutPortCount > newOutPortCount){
				//decrement the ports
				List<String> portsToBeRemoved = populateOutPortsToBeRemoved(prevOutPortCount, newOutPortCount);
				getCastedModel().decrementPorts(portsToBeRemoved);
				compFig.decrementAnchors(portsToBeRemoved);
				getCastedModel().changeOutPortCount(newOutPortCount);

			}
		}
	}
	
	private void setUnusedPortsCountDynamically(){
		
		int prevUnusedportCount = getCastedModel().getUnusedPortCount();
		int newUnunsedPortCount=0;
			if(getCastedModel().getProperties().get("unusedPortCount")!=null)
			{
			 newUnunsedPortCount=Integer.parseInt((String)getCastedModel().getProperties().get("unusedPortCount"));
			}
		
		int unusedPortCountToBeApplied = newUnunsedPortCount!=prevUnusedportCount ? newUnunsedPortCount : prevUnusedportCount;		

		ComponentFigure compFig = (ComponentFigure)getFigure();
		compFig.setWidth(unusedPortCountToBeApplied);

		Dimension newSize = new Dimension(compFig.getWidth(), compFig.getHeight()+getCastedModel().getComponentLabelMargin());
		getCastedModel().setSize(newSize);
		refresh();
		
		if(prevUnusedportCount != newUnunsedPortCount){

			if(prevUnusedportCount < newUnunsedPortCount){
				//Increment the ports
				getCastedModel().changeUnusedPortCount(newUnunsedPortCount);
				adjustExistingPorts();
				getCastedModel().incrementUnusedPorts(newUnunsedPortCount, prevUnusedportCount);
			}else if(prevUnusedportCount > newUnunsedPortCount){
				//decrement the ports
				List<String> portsToBeRemoved = populateUnusedPortsToBeRemoved(prevUnusedportCount, newUnunsedPortCount);
				getCastedModel().decrementPorts(portsToBeRemoved);
				compFig.decrementAnchors(portsToBeRemoved);
				getCastedModel().changeUnusedPortCount(newUnunsedPortCount);

			}
		}
	}
	
	private void adjustExistingPorts(){
		List<AbstractGraphicalEditPart> childrenEditParts = getChildren();
		PortEditPart portEditPart = null;
		for(AbstractGraphicalEditPart part:childrenEditParts)
		{
			if(part instanceof PortEditPart){ 
				portEditPart = (PortEditPart) part;
				portEditPart.adjustPortFigure(getCastedModel().getLocation());
			}
		}
	}
	
	private void selectPorts() {
		ComponentFigure compFig = (ComponentFigure)getFigure();
		List<Figure> childrenFigures = compFig.getChildren();
		if (!childrenFigures.isEmpty()){
			for(Figure figure:childrenFigures)
			{
				if(figure instanceof PortFigure)
					((PortFigure) figure).selectPort();
			}
		}
	}

	
	private List<String> populateInPortsToBeRemoved(int prevCount, int newCount){
		int keyCount = newCount;
		List<String> oldKeys = new ArrayList<>();
		List<String> oldInKeys = new ArrayList<>();
		List<String> newInKeys = new ArrayList<>();
		List<String> inKeysToBeRemoved = new ArrayList<>();

		oldKeys.addAll(getCastedModel().getPorts().keySet());

		for (String key : oldKeys) {
			if(key.contains("in"))
				oldInKeys.add(key);
		}

		for(int i=0; i<keyCount; i++)
		{
			newInKeys.add("in"+i);
		}
		for (String key : oldInKeys) {
			if(!newInKeys.contains(key))
				inKeysToBeRemoved.add(key);
		}
		return inKeysToBeRemoved;
	}
	
	private List<String> populateOutPortsToBeRemoved(int prevCount, int newCount){
		int keyCount = newCount;
		List<String> oldKeys = new ArrayList<>();
		List<String> oldOutKeys = new ArrayList<>();
		List<String> newOutKeys = new ArrayList<>();
		List<String> outKeysToBeRemoved = new ArrayList<>();

		oldKeys.addAll(getCastedModel().getPorts().keySet());

		for (String key : oldKeys) {
			if(key.contains("out"))
				oldOutKeys.add(key);
		}

		for(int i=0; i<keyCount; i++)
		{
			newOutKeys.add("out"+i);
		}
		for (String key : oldOutKeys) {
			if(!newOutKeys.contains(key))
				outKeysToBeRemoved.add(key);
		}
		return outKeysToBeRemoved;
	}
	
	private List<String> populateUnusedPortsToBeRemoved(int prevCount, int newCount){
		int keyCount = newCount;
		List<String> oldKeys = new ArrayList<>();
		List<String> oldUnusedKeys = new ArrayList<>();
		List<String> newUnusedKeys = new ArrayList<>();
		List<String> unusedKeysToBeRemoved = new ArrayList<>();

		oldKeys.addAll(getCastedModel().getPorts().keySet());

		for (String key : oldKeys) {
			if(key.contains("unused"))
				oldUnusedKeys.add(key);
		}

		for(int i=0; i<keyCount; i++)
		{
			newUnusedKeys.add("unused"+i);
		}
		for (String key : oldUnusedKeys) {
			if(!newUnusedKeys.contains(key))
				unusedKeysToBeRemoved.add(key);
		}
		return unusedKeysToBeRemoved;
	}
	
	private void adjustComponentFigure(Component component, ComponentFigure componentFigure){
		Dimension d = null;
		String label = (String) component.getPropertyValue(Component.Props.NAME_PROP.getValue());
		ComponentLabel componentLabel = component.getComponentLabel();
		Font font = new Font( Display.getDefault(), ELTFigureConstants.labelFont, 10,
				SWT.NORMAL );
		int labelLength = TextUtilities.INSTANCE.getStringExtents(label, font).width;
		component.setComponentLabel(label);
		if(labelLength >= ELTFigureConstants.compLabelOneLineLengthLimit && !componentFigure.isIncrementedHeight()){
			component.setSize(new Dimension(component.getSize().width, component.getSize().height +ELTFigureConstants.componentOneLineLabelMargin));
			//component.setSize(new Dimension(100, 82));
			componentLabel.setSize(new Dimension(componentLabel.getSize().width, componentLabel.getSize().height +ELTFigureConstants.componentOneLineLabelMargin));
			componentFigure.setIncrementedHeight(true);
			component.setComponentLabelMargin(ELTFigureConstants.componentTwoLineLabelMargin);
			componentFigure.setComponentLabelMargin(ELTFigureConstants.componentTwoLineLabelMargin);
		}else if(labelLength < ELTFigureConstants.compLabelOneLineLengthLimit && componentFigure.isIncrementedHeight()){
			component.setSize(new Dimension(component.getSize().width, component.getSize().height-ELTFigureConstants.componentOneLineLabelMargin));
			componentLabel.setSize(new Dimension(componentLabel.getSize().width, componentLabel.getSize().height -ELTFigureConstants.componentOneLineLabelMargin));
			componentFigure.setIncrementedHeight(false);
			component.setComponentLabelMargin(ELTFigureConstants.componentOneLineLabelMargin);
			componentFigure.setComponentLabelMargin(ELTFigureConstants.componentOneLineLabelMargin);
		}else if(labelLength < ELTFigureConstants.compLabelOneLineLengthLimit ){
			component.setSize(new Dimension(component.getSize().width, component.getSize().height));
		}
		componentFigure.repaint();
		
	}
	
	public void updateComponentStatus(){
		Component component = this.getCastedModel();
		LinkedHashMap<String, Object> properties = component.getProperties();
		String statusName = Component.Props.VALIDITY_STATUS.getValue();
		if(properties.containsKey(statusName)){
			((ComponentFigure)this.getFigure()).setStatus((String)properties.get(statusName));
			this.getFigure().repaint();
		}
	}
	
	
}
