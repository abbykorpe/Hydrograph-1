package com.bitwise.app.graph.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.ConnectionAnchor;
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
import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.figure.ComponentBorder;
import com.bitwise.app.graph.figure.ComponentFigure;
import com.bitwise.app.graph.figure.ELTFigureConstants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.ComponentLabel;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.processor.DynamicClassProcessor;
import com.bitwise.app.graph.propertywindow.ELTPropertyWindow;

/**
 * The Class ComponentEditPart.
 * @author Bitwise
 */
public class ComponentEditPart extends AbstractGraphicalEditPart implements
		NodeEditPart, PropertyChangeListener {
	
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
		String componentName = DynamicClassProcessor.INSTANCE
				.getClazzName(getModel().getClass());
		try {
			for (com.bitwise.app.common.component.config.Component component : XMLConfigUtil.INSTANCE
					.getComponentConfig()) {
				if (component.getName().equalsIgnoreCase(componentName)) {
					applyGeneralPolicy(component);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		for (Policy generalPolicy : XMLConfigUtil.INSTANCE
				.getPoliciesForComponent(component)) {
			try {
				AbstractEditPolicy editPolicy = (AbstractEditPolicy) Class
						.forName(generalPolicy.getValue()).newInstance();
				installEditPolicy(generalPolicy.getName(), editPolicy);
			} catch (Exception exception) {
				// TODO : add logger
				logger.error(exception.getMessage());
				throw exception;
			}
		}
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = createFigureForModel();
		figure.setOpaque(true); // non-transparent figure
		LinkedHashMap<String, Object> properties = getCastedModel().getProperties();
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
			for(AbstractGraphicalEditPart part:childrenEditParts)
			{
				if(part instanceof ComponentLabelEditPart){
					lLabelEditPart = (ComponentLabelEditPart) part;
					lLabelEditPart.refreshVisuals();
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
		// TODO Auto-generated method stub
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
			ELTPropertyWindow eltPropertyWindow = new ELTPropertyWindow(getModel());
			eltPropertyWindow.open();
			
			logger.debug("Updated dimentions: " + getCastedModel().getSize().height + ":"
							+ getCastedModel().getSize().width);
			adjustComponentFigure(getCastedModel(), getComponentFigure());
			getCastedModel().setComponentLabel((String) getCastedModel().getPropertyValue(Component.Props.NAME_PROP.getValue()));
			String newPortCount =   (String)getCastedModel().getProperties().get("input_count");
			int numOfPort=0;
			if(StringUtils.isNotEmpty(newPortCount)){
				numOfPort = Integer.parseInt(newPortCount);
				
				
				ComponentFigure compFig = (ComponentFigure)getFigure();
				compFig.setHeight(numOfPort, 1);
				compFig.setWidth(numOfPort);
				Dimension newSize = new Dimension((numOfPort+1)*33, ((numOfPort+1)*25)+15);
				getCastedModel().setSize(newSize);
				
				getCastedModel().clearPorts();
				((ComponentFigure)getFigure()).clearAnchors(numOfPort);
				refresh();
				
				getCastedModel().changePortSettings(numOfPort);
			}

			updateComponentStatus();			
			refresh();
			List<AbstractGraphicalEditPart> childrenEditParts = getChildren();
			PortEditPart portEditPart = null;
//			for(AbstractGraphicalEditPart part:childrenEditParts)
//			{
//				
//				if(part instanceof PortEditPart){ 
//					portEditPart = (PortEditPart) part;
//					portEditPart.adjustPortFigure(getCastedModel().getLocation());
//				}
//			}

			ELTGraphicalEditor eltGraphicalEditor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if(eltPropertyWindow.isPropertyChanged()){
				eltGraphicalEditor.setDirty(true);
				getCastedModel().updateTooltipInformation();
			}
			
			super.performRequest(req);
		}
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
			component.setSize(new Dimension(component.getSize().width, component.getSize().height +15));
			componentLabel.setSize(new Dimension(componentLabel.getSize().width, componentLabel.getSize().height +15));
			componentFigure.setIncrementedHeight(true);
			component.setComponentLabelMargin(ELTFigureConstants.componentTwoLineLabelMargin);
			componentFigure.setComponentLabelMargin(ELTFigureConstants.componentTwoLineLabelMargin);
		}else if(labelLength < ELTFigureConstants.compLabelOneLineLengthLimit && componentFigure.isIncrementedHeight()){
			component.setSize(new Dimension(component.getSize().width, component.getSize().height-15));
			componentLabel.setSize(new Dimension(componentLabel.getSize().width, componentLabel.getSize().height -15));
			componentFigure.setIncrementedHeight(false);
			component.setComponentLabelMargin(ELTFigureConstants.componentOneLineLabelMargin);
			componentFigure.setComponentLabelMargin(ELTFigureConstants.componentOneLineLabelMargin);
		}else if(labelLength < ELTFigureConstants.compLabelOneLineLengthLimit ){
			component.setSize(new Dimension(component.getSize().width, component.getSize().height));
		}
		componentFigure.repaint();
		
	}
	
	private void updateComponentStatus(){
		Component component = getCastedModel();
		LinkedHashMap<String, Object> properties = component.getProperties();
		String statusName = Component.Props.VALIDITY_STATUS.getValue();
		if(properties.containsKey(statusName)){
			((ComponentFigure)getFigure()).setStatus((String)properties.get(statusName));
			getFigure().repaint();
		}
	}
}
