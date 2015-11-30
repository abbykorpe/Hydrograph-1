package com.bitwise.app.graph.editor;

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
import org.eclipse.swt.events.MouseTrackListener;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.graph.command.ComponentCreateCommand;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;

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
public class PaletteContainerListener implements MouseListener, MouseTrackListener {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(PaletteContainerListener.class);
	private final PaletteViewer viewer;
	private Point defaultComponentLocation = new Point(0, 0);
	private Component genericComponent;
	private GraphicalViewer graphicalViewer;
	
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
		System.out.println("+++ This is mouse click");
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
		// TODO Auto-generated method stub
		System.out.println("+++ This is mouse Enter");
	}




	@Override
	public void mouseExit(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("+++ This is mouse Exit");
	}




	@Override
	public void mouseHover(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("+++ This is mouse Hover");
		EditPart paletteInternalController = viewer.findObjectAt(new Point(
				e.x, e.y));

		CombinedTemplateCreationEntry addedPaletteTool = (CombinedTemplateCreationEntry) paletteInternalController
				.getModel();

		CreateRequest componentRequest = new CreateRequest();
		componentRequest.setFactory(new SimpleFactory((Class) addedPaletteTool
				.getTemplate()));

		genericComponent = (Component) componentRequest
				.getNewObject();

		//setComponentRequestParams(componentRequest);
		System.out.println("This is mouse hover on " + genericComponent.getComponentLabel());
	}
}