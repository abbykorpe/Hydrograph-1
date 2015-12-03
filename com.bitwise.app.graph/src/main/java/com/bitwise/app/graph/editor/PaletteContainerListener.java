package com.bitwise.app.graph.editor;

import java.awt.MouseInfo;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.graph.command.ComponentCreateCommand;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.tooltip.window.PaletteToolTip;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving paletteContainer events. The class that is interested in processing a
 * paletteContainer event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addPaletteContainerListener<code> method. When
 * the paletteContainer event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see PaletteContainerEvent
 */
public class PaletteContainerListener implements MouseListener, MouseTrackListener , MouseMoveListener {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(PaletteContainerListener.class);
	private final PaletteViewer viewer;
	private Point defaultComponentLocation = new Point(0, 0);
	private Component genericComponent;
	private GraphicalViewer graphicalViewer;
	private PaletteToolTip paletteToolTip;
	private static final int TOOLTIP_SHOW_DELAY=600;
	
	/**
	 * Instantiates a new palette container listener.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param graphicalViewer
	 *            the graphical viewer
	 */
	public PaletteContainerListener(PaletteViewer viewer, GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
		this.viewer = viewer;
	}
	
	@Override
	public void mouseUp(MouseEvent e) {
		hidePaletteToolTip();
	}

	@Override
	public void mouseDown(MouseEvent e) {
	}

	@Override
	public void mouseDoubleClick(MouseEvent mouseEvent) {
		CreateRequest componentRequest = getComponentRequest(mouseEvent);
		placeComponentOnCanvasByDoubleClickOnPalette(componentRequest);

		logger.info(
				"Component is positioned at respective x and y location"
						+ defaultComponentLocation.getCopy().x + 20 + " and "
						+ defaultComponentLocation.getCopy().y + 20);
		logger.info(
				"Component is positioned at respective x and y location"
						+ defaultComponentLocation.getCopy().x + 20 + " and "
						+ defaultComponentLocation.getCopy().y + 20);

	}

	private CreateRequest getComponentRequest(MouseEvent mouseEvent) {
		EditPart paletteInternalController = viewer.findObjectAt(new Point(
				mouseEvent.x, mouseEvent.y));

		CombinedTemplateCreationEntry addedPaletteTool = (CombinedTemplateCreationEntry) paletteInternalController
				.getModel();

		CreateRequest componentRequest = new CreateRequest();
		componentRequest.setFactory(new SimpleFactory((Class) addedPaletteTool
				.getTemplate()));

		genericComponent = (Component) componentRequest
				.getNewObject();

		setComponentRequestParams(componentRequest);

		return componentRequest;
	}

	private void setComponentRequestParams(CreateRequest componentRequest) {
		componentRequest.setSize(genericComponent.getSize());

		defaultComponentLocation.setLocation(
				defaultComponentLocation.getCopy().x + 20,
				defaultComponentLocation.getCopy().y + 20);

		componentRequest.setLocation(defaultComponentLocation);
	}

	private void placeComponentOnCanvasByDoubleClickOnPalette(
			CreateRequest componentRequest) {
		GraphicalViewer graphViewer = graphicalViewer;

		ComponentCreateCommand createComponent = new ComponentCreateCommand(
				(com.bitwise.app.graph.model.Component) componentRequest
						.getNewObject(),
				(Container) graphViewer.getContents().getModel(),
				new Rectangle(componentRequest.getLocation(), componentRequest
						.getSize()));

		graphViewer.getEditDomain().getCommandStack().execute(createComponent);
	}

	@Override
	public void mouseEnter(MouseEvent e) {
		// Do Nothing
	}




	@Override
	public void mouseExit(MouseEvent e) {
		hidePaletteToolTip();
	}

	private void hidePaletteToolTip(){
		if(paletteToolTip!=null){
			java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			if(!paletteToolTip.getBounds().contains(mouseLocation.x, mouseLocation.y)){
				paletteToolTip.setVisible(false);
			}	
		}
	}
	
	private void showPaletteToolTip(String toolTipMessage) {
		paletteToolTip = new PaletteToolTip(Display.getDefault());
		java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		paletteToolTip.setLocation(mouseLocation.x +5 , mouseLocation.y +5);
		paletteToolTip.setToolTipText(toolTipMessage);
		paletteToolTip.setVisible(true);
	}


	@Override
	public void mouseHover(MouseEvent e) {
		final java.awt.Point mouseLocation1 = MouseInfo.getPointerInfo().getLocation();
		EditPart paletteInternalController = viewer.findObjectAt(new Point(
				e.x, e.y));

		if(paletteInternalController.getModel() instanceof CombinedTemplateCreationEntry){
			CombinedTemplateCreationEntry addedPaletteTool = (CombinedTemplateCreationEntry) paletteInternalController
					.getModel();

			CreateRequest componentRequest = new CreateRequest();
			componentRequest.setFactory(new SimpleFactory((Class) addedPaletteTool
					.getTemplate()));

			genericComponent = (Component) componentRequest
					.getNewObject();

			hidePaletteToolTip();
			
			Display.getDefault().timerExec(TOOLTIP_SHOW_DELAY, new Runnable() {
				public void run() {
					java.awt.Point mouseLocation2 = MouseInfo.getPointerInfo().getLocation();
					
					if(mouseLocation1.equals(mouseLocation2)){
						//showPaletteToolTip(genericComponent.getComponentLabel().getLabelContents());
						showPaletteToolTip(genericComponent.getComponentDescription());
					}
					
                }
			});
			
			
			
		}
	}
	@Override
	public void mouseMove(MouseEvent e) {
		if(paletteToolTip!=null){
			org.eclipse.swt.graphics.Rectangle tooltipBounds = paletteToolTip.getBounds();
			org.eclipse.swt.graphics.Rectangle newBounds = new org.eclipse.swt.graphics.Rectangle(tooltipBounds.x -5, tooltipBounds.y-5, tooltipBounds.width, tooltipBounds.height);
			java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			if(!newBounds.contains(mouseLocation.x,mouseLocation.y))
				hidePaletteToolTip();
		}
	}

	
}