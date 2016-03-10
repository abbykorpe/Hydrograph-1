package com.bitwise.app.graph.figure;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.datastructures.tooltip.PropertyToolTipInformation;
import com.bitwise.app.common.interfaces.tooltip.ComponentCanvas;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.model.Component.ValidityStatus;
import com.bitwise.app.tooltip.tooltips.ComponentTooltip;

/**
 * The Class ComponentFigure.
 * 
 * @author Bitwise
 */
public class ComponentFigure extends Figure implements Validator{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ComponentFigure.class);

	private final XYLayout layout;
	private int height=0, width=0;

	private HashMap<String, FixedConnectionAnchor> connectionAnchors;
	private List<FixedConnectionAnchor> inputConnectionAnchors;
	private List<FixedConnectionAnchor> outputConnectionAnchors;
	private List<PortSpecification> portspecification;

	private int totalPortsofInType=0, totalPortsOfOutType=0, totalPortsOfUnusedType=0;

	private Color borderColor;
	private Color selectedBorderColor;	
	private Color componentColor;
	private Color selectedComponentColor;
	private boolean incrementedHeight;
	private int componentLabelMargin;

	private String canvasIconPath;
	private Image canvasIcon;

	private String status;

	private Map<String,PropertyToolTipInformation> propertyToolTipInformation;
	private ComponentCanvas componentCanvas;
	private ComponentTooltip componentToolTip;
	org.eclipse.swt.graphics.Rectangle componentBounds;
	private static final int TOOLTIP_SHOW_DELAY=800;
	private Display display;
	private Runnable timer;
	/**
	 * Instantiates a new component figure.
	 * 
	 * @param portSpecification
	 *            the port specification
	 * @param cIconPath
	 *            the canvas icon path
	 */
	public ComponentFigure(List<PortSpecification> portSpecification, String cIconPath, String label) {
		this.portspecification = portSpecification;
		this.canvasIconPath = XMLConfigUtil.CONFIG_FILES_PATH + cIconPath;

		layout = new XYLayout();
		setLayoutManager(layout);

		Font font = new Font( Display.getDefault(), ELTFigureConstants.labelFont, 9,
				SWT.NORMAL );
		int labelLength = TextUtilities.INSTANCE.getStringExtents(label, font).width;

		if(labelLength >= ELTFigureConstants.compLabelOneLineLengthLimit ){
			this.componentLabelMargin = ELTFigureConstants.componentTwoLineLabelMargin;
			this.incrementedHeight = true;
		}else if(labelLength < ELTFigureConstants.compLabelOneLineLengthLimit ){
			this.componentLabelMargin = ELTFigureConstants.componentOneLineLabelMargin;
			this.incrementedHeight = false;
		}

		canvasIcon = new Image(null, canvasIconPath);

		connectionAnchors = new HashMap<String, FixedConnectionAnchor>();
		inputConnectionAnchors = new ArrayList<FixedConnectionAnchor>();
		outputConnectionAnchors = new ArrayList<FixedConnectionAnchor>();


		setInitialColor();
		setComponentColorAndBorder();

		for(PortSpecification p:portspecification)
		{
			setPortCount(p);
			setHeight(totalPortsofInType, totalPortsOfOutType);
			setWidth(totalPortsOfUnusedType);
		}

		componentCanvas = getComponentCanvas();
		attachMouseListener();
	}

	private ComponentCanvas getComponentCanvas() {

		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof ComponentCanvas)
			return (ComponentCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}

	private void setInitialColor(){
		componentColor = ELTColorConstants.BG_COMPONENT;
		borderColor = ELTColorConstants.COMPONENT_BORDER;
		selectedComponentColor = ELTColorConstants.BG_COMPONENT_SELECTED;
		selectedBorderColor = ELTColorConstants.BLUE_BRAND_BODER;
	}

	/**
	 * Sets the component color and border.
	 */
	public void setComponentColorAndBorder(){
		setBackgroundColor(componentColor);
		setBorder(new ComponentBorder(borderColor, 0, componentLabelMargin));
	}

	/**
	 * Sets the selected component color and border.
	 */
	public void setSelectedComponentColorAndBorder(){
		setBackgroundColor(selectedComponentColor);
		setBorder(new ComponentBorder(selectedBorderColor, 2, componentLabelMargin));
	}

	private void setPortCount(PortSpecification p) {
		if(("in").equalsIgnoreCase(p.getTypeOfPort().value())){
			totalPortsofInType=p.getNumberOfPorts();
		}
		else if(("out").equalsIgnoreCase(p.getTypeOfPort().value())){
			totalPortsOfOutType=p.getNumberOfPorts();
		}
		else if(("unused").equalsIgnoreCase(p.getTypeOfPort().value())){
			totalPortsOfUnusedType=p.getNumberOfPorts();
		}

	}

	public void setHeight(int totalPortsofInType, int totalPortsOfOutType) {
		int heightFactor=totalPortsofInType > totalPortsOfOutType ? totalPortsofInType : totalPortsOfOutType;
		this.height = (heightFactor+1)*25;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int totalPortsofUnusedType) {
		int widthFactor=totalPortsofUnusedType;
		this.width=100;
		if(widthFactor > 1)
			this.width = (widthFactor+1)*33;
	}

	public int getWidth(){
		return width;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	private void hideToolTip(){
		if(componentCanvas != null){
			if(componentCanvas.getComponentTooltip() != null){
				componentCanvas.getComponentTooltip().setVisible(false);
				componentCanvas.issueToolTip(null, null);
			}
		}

		if(componentToolTip != null){
			componentToolTip.setVisible(false);
			componentToolTip = null;
			componentBounds=null;
		}
		componentCanvas=null;
	}

	public void setPropertyToolTipInformation(Map<String,PropertyToolTipInformation> propertyToolTipInformation){
		this.propertyToolTipInformation = propertyToolTipInformation;
	}

	private ComponentTooltip getStatusToolTip(Shell parent, org.eclipse.swt.graphics.Point location){
		ComponentTooltip tooltip = new ComponentTooltip(parent, "Click to focus", propertyToolTipInformation);
		tooltip.setSize(300, 100);
		tooltip.setLocation(location);
		return tooltip;
	}

	private ComponentTooltip getToolBarToolTip(Shell parent, org.eclipse.swt.graphics.Rectangle toltipBounds){
		ToolBarManager toolBarManager = new ToolBarManager();
		ComponentTooltip tooltip = new ComponentTooltip(parent, toolBarManager, propertyToolTipInformation);
		org.eclipse.swt.graphics.Point location=new org.eclipse.swt.graphics.Point(toltipBounds.x, toltipBounds.y);
		tooltip.setLocation(location);
		tooltip.setSize(toltipBounds.width + 20, toltipBounds.height + 20);
		return tooltip;
	}

	private void setStatusToolTipFocusListener() {

		componentToolTip.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// Do nothing
			}

			@Override
			public void focusGained(FocusEvent e) {
				showToolBarToolTip();
			}
		});
	}

	private void showStatusToolTip(
			org.eclipse.swt.graphics.Point location) {

		componentCanvas = getComponentCanvas();
		if(componentCanvas.getComponentTooltip() == null){
			componentToolTip = getStatusToolTip(componentCanvas.getCanvasControl().getShell(), location);
			componentBounds = getComponentBounds();			
			componentCanvas.issueToolTip(componentToolTip, componentBounds);

			componentToolTip.setVisible(true);
			setStatusToolTipFocusListener();
			org.eclipse.swt.graphics.Point tooltipSize = componentToolTip.computeSizeHint();
			//componentToolTip.setSizeConstraints(300, 100);
			if(tooltipSize.x > 300){
				tooltipSize.x = 300;
			}
			componentToolTip.setSize(tooltipSize.x, tooltipSize.y);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double width = screenSize.getWidth();
			double height = screenSize.getHeight();

			int newX,newY;
			int offset=10;
			if((componentToolTip.getBounds().x + componentToolTip.getBounds().width) > width){
				newX = componentToolTip.getBounds().x - (int) ((componentToolTip.getBounds().x + componentToolTip.getBounds().width) - width) - offset;
			}else{
				newX = componentToolTip.getBounds().x;
			}

			if((componentToolTip.getBounds().y + componentToolTip.getBounds().height) > height){
				newY = componentToolTip.getBounds().y - getBounds().height - componentToolTip.getBounds().height - offset;
			}else{
				newY= componentToolTip.getBounds().y;
			}
			org.eclipse.swt.graphics.Point newLocation = new org.eclipse.swt.graphics.Point(newX, newY);
			componentToolTip.setLocation(newLocation);

		}
	}

	private org.eclipse.swt.graphics.Rectangle getComponentBounds() {
		Rectangle tempComponuntBound = getBounds();
		org.eclipse.swt.graphics.Rectangle componentBound = new org.eclipse.swt.graphics.Rectangle(tempComponuntBound.x, tempComponuntBound.y, tempComponuntBound.width, tempComponuntBound.height);
		return componentBound;
	}

	private void hideToolTip2() {
		if(componentCanvas != null){
			if(componentCanvas.getComponentTooltip() != null){
				componentCanvas.getComponentTooltip().setVisible(false);
				if(componentCanvas!=null)
					componentCanvas.issueToolTip(null, null);
			}
		}

		if(componentToolTip != null){
			componentToolTip.setVisible(false);
			componentToolTip = null;
			componentBounds=null;
		}
		componentCanvas=null;
	}

	private void showToolBarToolTip() {
		org.eclipse.swt.graphics.Rectangle toltipBounds = componentToolTip.getBounds();

		hideToolTip();

		componentCanvas = getComponentCanvas();

		componentToolTip = getToolBarToolTip(componentCanvas.getCanvasControl().getShell(),toltipBounds);
		componentBounds = getComponentBounds();
		componentCanvas.issueToolTip(componentToolTip, componentBounds);

		componentToolTip.setVisible(true);

		componentToolTip.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				hideToolTip2();				
			}



			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		componentToolTip.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				hideToolTip2();
			}
		});
		componentToolTip.setFocus();
	}

	private org.eclipse.swt.graphics.Point getToolTipLocation(org.eclipse.swt.graphics.Point reletiveMouseLocation,org.eclipse.swt.graphics.Point mouseLocation, Rectangle rectangle) {
		int subtractFromMouseX,addToMouseY;

		subtractFromMouseX = reletiveMouseLocation.x - rectangle.x;
		addToMouseY = (rectangle.y + rectangle.height) - reletiveMouseLocation.y;

		return new org.eclipse.swt.graphics.Point((mouseLocation.x - subtractFromMouseX) ,( mouseLocation.y + addToMouseY));
	}

	private void attachMouseListener() {
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(org.eclipse.draw2d.MouseEvent arg0) {
				// Do nothing
			}

			@Override
			public void mouseHover(org.eclipse.draw2d.MouseEvent arg0) {
				arg0.consume();
				final org.eclipse.swt.graphics.Point location1 = new org.eclipse.swt.graphics.Point(arg0.x, arg0.y);
				java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
				final org.eclipse.swt.graphics.Point location = new org.eclipse.swt.graphics.Point(mouseLocation.x, mouseLocation.y);
				componentCanvas = getComponentCanvas();
				display=componentCanvas.getCanvasControl().getShell().getDisplay();
				timer=new Runnable() {
					public void run() {
						//if(componentCanvas.isToolTipTimerRunning())
						java.awt.Point mouseLocation2 = MouseInfo.getPointerInfo().getLocation();
						org.eclipse.swt.graphics.Point location2 = new org.eclipse.swt.graphics.Point(mouseLocation2.x, mouseLocation2.y);

						org.eclipse.swt.graphics.Point perfectToolTipLocation = getToolTipLocation(location1,location2,getBounds());

						if(location2.equals(location)){
							showStatusToolTip(perfectToolTipLocation);
							//showStatusToolTip(location);
						}

					}
				};
				display.timerExec(TOOLTIP_SHOW_DELAY, timer);
				
			}
			

			@Override
			public void mouseExited(org.eclipse.draw2d.MouseEvent arg0) {
				// Do nothing
			}

			@Override
			public void mouseEntered(org.eclipse.draw2d.MouseEvent arg0) {
				// Do nothing
			}

			@Override
			public void mouseDragged(org.eclipse.draw2d.MouseEvent arg0) {
				// Do nothing
			}
		});


		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(org.eclipse.draw2d.MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(org.eclipse.draw2d.MouseEvent arg0) {
				// TODO Auto-generated method stub
				hideToolTip();
			}

			@Override
			public void mouseDoubleClicked(org.eclipse.draw2d.MouseEvent arg0) {
				// TODO Auto-generated method stub
				hideToolTip();
			}
		});
	}



	public void setAnchors(FixedConnectionAnchor fCAnchor) {
		connectionAnchors.put(fCAnchor.getType()+fCAnchor.getSequence(), fCAnchor);
		if(("out").equalsIgnoreCase(fCAnchor.getType()) || ("unused").equalsIgnoreCase(fCAnchor.getType()))
			outputConnectionAnchors.add(fCAnchor);
		else
			inputConnectionAnchors.add(fCAnchor);
	}

	public void decrementAnchors(List<String> portsToBeRemoved){

		for(String portRemove: portsToBeRemoved){
			connectionAnchors.remove(portRemove);
		}

		for(String portRemove: portsToBeRemoved){
			for (Iterator<FixedConnectionAnchor> iterator = outputConnectionAnchors.iterator(); iterator.hasNext();) {
				FixedConnectionAnchor fca = iterator.next();
				if ((fca.getType()+fca.getSequence()).equals(portRemove)) {
					// Remove the current element from the iterator and the list.
					iterator.remove();
				}
			}
		}
		for(String portRemove: portsToBeRemoved){
			for (Iterator<FixedConnectionAnchor> iterator = inputConnectionAnchors.iterator(); iterator.hasNext();) {
				FixedConnectionAnchor fca = iterator.next();
				if ((fca.getType()+fca.getSequence()).equals(portRemove)) {
					// Remove the current element from the iterator and the list.
					iterator.remove();
				}
			}
		}
	}

	@Override
	protected void paintFigure(Graphics graphics) {

		Rectangle r = getBounds().getCopy();
		graphics.translate(r.getLocation());

		Rectangle q = new Rectangle(4, 4+componentLabelMargin, r.width-8, r.height-8-componentLabelMargin);
		graphics.fillRoundRectangle(q, 5, 5);

		graphics.drawImage(canvasIcon, new Point(q.width/2-16, q.height/2+componentLabelMargin-15));
		drawStatus(graphics);
	}

	/**
	 * Draws the status image to right corner of the component
	 * @param graphics
	 */
	private void drawStatus(Graphics graphics){
		Image statusImage = null;
		Rectangle rectangle = getBounds().getCopy();
		if(StringUtils.isNotBlank(getStatus()) && getStatus().equals(ValidityStatus.WARN.name())){
			statusImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + "/icons/warn.png");
		}
		else if (StringUtils.isNotBlank(getStatus()) && getStatus().equals(ValidityStatus.ERROR.name())){
			statusImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + "/icons/error.png");
		}
		logger.debug("Component has {} status.", getStatus());
		if(statusImage != null){
			graphics.drawImage(statusImage, new Point(rectangle.width - 25, 8+componentLabelMargin));
		}
	}


	/**
	 * Gets the connection anchor.
	 * 
	 * @param terminal
	 *            the terminal
	 * @return the connection anchor
	 */
	public ConnectionAnchor getConnectionAnchor(String terminal) {

		return connectionAnchors.get(terminal);
	}

	/**
	 * Gets the connection anchor name.
	 * 
	 * @param c
	 *            the c
	 * @return the connection anchor name
	 */
	public String getConnectionAnchorName(ConnectionAnchor c) {

		Set<String> keys = connectionAnchors.keySet();
		String key;
		Iterator<String> it = keys.iterator();

		while(it.hasNext()){
			key=it.next();
			if (connectionAnchors.get(key).equals(c))
				return key;
		}
		return null;
	}

	private ConnectionAnchor closestAnchor(Point p, List<FixedConnectionAnchor> connectionAnchors) {
		ConnectionAnchor closest = null;
		double min = Double.MAX_VALUE;
		for(ConnectionAnchor c : connectionAnchors){
			double d = p.getDistance(c.getLocation(null));
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	/**
	 * Gets the source connection anchor at.
	 * 
	 * @param p
	 *            the p
	 * @return the source connection anchor at
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		return closestAnchor(p, outputConnectionAnchors);
	}

	/**
	 * Gets the target connection anchor at.
	 * 
	 * @param p
	 *            the p
	 * @return the target connection anchor at
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		return closestAnchor(p, inputConnectionAnchors);
	}


	@Override
	public void validate() {
		super.validate();

		if (isValid())
			return;

	}
	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isIncrementedHeight() {
		return incrementedHeight;
	}

	public void setIncrementedHeight(boolean incrementedHeight) {
		this.incrementedHeight = incrementedHeight;
	}
	public int getComponentLabelMargin() {
		return componentLabelMargin;
	}

	public void setComponentLabelMargin(int componentLabelMargin) {
		this.componentLabelMargin = componentLabelMargin;
	}

	public void terminateToolTipTimer() {
		if(display != null && timer != null){
			display.timerExec(-1,timer);
		}

	}

}
